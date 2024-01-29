package es.tributasenasturias.services.ws.imagenesmultas.bd;

import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.Binding;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.Handler;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import stpa.services.LanzaPL;
import stpa.services.LanzaPLService;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.ArchivoDigital;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.ArchivoDigital_Service;
import es.tributasenasturias.services.ws.imagenesmultas.Constantes;
import es.tributasenasturias.services.ws.imagenesmultas.ImagenType;
import es.tributasenasturias.services.ws.imagenesmultas.ImagenesType;
import es.tributasenasturias.services.ws.imagenesmultas.utils.ConversorParametrosLanzador;
import es.tributasenasturias.services.ws.imagenesmultas.utils.log.Logger;
import es.tributasenasturias.services.ws.imagenesmultas.utils.log.LogHandler.ClientLogHandler;
import es.tributasenasturias.services.ws.imagenesmultas.utils.log.Preferencias.Preferencias;

public class Documentos {

	private ConversorParametrosLanzador cpl;
	private Preferencias preferencias;
	private Logger logger;
	private String idLlamada;
	
	public Documentos(Preferencias pref, Logger log, String idLlamada) {

		this.preferencias= pref;
		this.logger = log;
		this.idLlamada= idLlamada;
	}
	
	@SuppressWarnings("unchecked")
	private void setHandler(javax.xml.ws.BindingProvider bpr) {
		Binding b= bpr.getBinding();
		List<Handler>handlers= b.getHandlerChain();
		if (handlers==null){
			handlers= new ArrayList<Handler>();
		}
		handlers.add(new ClientLogHandler());
		//Ponemos en contexto los objetos de la llamada
		bpr.getRequestContext().put(Constantes.ID_LLAMADA, this.idLlamada);
		bpr.getRequestContext().put(Constantes.PREFERENCIAS, this.preferencias);
		b.setHandlerChain(handlers);
	}
	private static String byteToHex(final byte[] hash)
	{
	    Formatter formatter = new Formatter();
	    for (byte b : hash)
	    {
	        formatter.format("%02x", b);
	    }
	    String result = formatter.toString();
	    formatter.close();
	    return result;
	}
	
	@SuppressWarnings("unused")
	private static String getHashCode(byte[] cadena)
	{
	    String sha1 = "";
	    try
	    {
	        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	        crypt.reset();
	        crypt.update(cadena);
	        sha1 = byteToHex(crypt.digest());
	    }
	    catch(NoSuchAlgorithmException e)
	    {
	        e.printStackTrace();
	    }

	    return sha1;
	}
	
	/**
	 * Establece los valores de retorno en función de los obtenidos
	 * @param resultado
	 * @return
	 */
	private String ParsearResultado(String resultado, Holder<String> error) {
		
		String aIdAdar = "";
		try {
			String codResult = GetValorNodo(resultado, "N1");
			String errorPL = GetValorNodo(resultado, "error");
			if (codResult.equals(preferencias.getCodResultadoOK())) {
				aIdAdar = GetValorNodo(resultado, "C1");
							} else {
				error.value = errorPL;
			}
		} catch (Exception e) {
			error.value = "Error: " + e.getMessage();
		}
		
		return aIdAdar;
	}
	
	/**
	 * Obtiene el valor del nodoABuscar dentro del xmlParsear
	 * @param xmlParsear
	 * @param nodoABuscar
	 * @return
	 */
	private String GetValorNodo(String xmlParsear, String nodoABuscar) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlParsear));

			Document doc = db.parse(is);
			// Obtenemos el valor del nodo
			NodeList nodes = doc.getElementsByTagName(nodoABuscar);
			logger.debug("NumElementos " + nodoABuscar + ": "+ nodes.getLength());
			String resultado = "";
			if (nodes.getLength() > 0)
				// Sólo hay un nodo de ese tipo
				resultado = (nodes.item(0).getFirstChild().getNodeValue());
			else
				resultado = "";

			return resultado;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "";
		}
	}
	
	public void ObtenerImagenes(String fechaInfraccion, String boletin, String idFiscal, 
			Holder<ImagenesType> imagenes, Holder<String> error)
	{
                        
        try
        {
        	LanzaPLService lanzaderaWS = new LanzaPLService();
        	LanzaPL lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
        	javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
			bpr.getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,preferencias.getEndPointISL());
			setHandler(bpr);
			cpl = new ConversorParametrosLanzador();
			cpl.setProcedimientoAlmacenado(preferencias.getNomProcedimiento());
			cpl.setParametro(fechaInfraccion, ConversorParametrosLanzador.TIPOS.Date,"DD/MM/YYYY"); 
			cpl.setParametro(boletin, ConversorParametrosLanzador.TIPOS.String); 
			cpl.setParametro(idFiscal, ConversorParametrosLanzador.TIPOS.String);

			String resultadoEjecutarPL=lanzaderaPort.executePL(preferencias.getEsquemaBaseDatos(), cpl.Codifica(),preferencias.getIPLanzador(), "", "", "");
			String resultadoConsulta = ParsearResultado(resultadoEjecutarPL, error);
			
			if (error.value != null)
				return;
			
       		String[] resultados = resultadoConsulta.split("#");
       		
       		ImagenesType imagenesAD = new ImagenesType();
       		
       		
       		for (int i=0; i<resultados.length; i++)
       			{
       				if(resultados[i] != "" && resultados[i] != null){
       					ImagenType imagen = new ImagenType();
       					
			       		int idArchivo = Integer.valueOf(resultados[i]);
			       		Holder<byte[]> archivo = new  Holder<byte[]>();
			       		Holder<String> datosArchivo = new Holder<String>();
						Holder<String> errorAD = new  Holder<String>();
	        		
			    		ArchivoDigital_Service ads = new ArchivoDigital_Service();
			    		ArchivoDigital ad = ads.getArchivoDigitalSOAP();
			    		bpr = (javax.xml.ws.BindingProvider) ad;
						bpr.getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,preferencias.getEndpointArchivoDigital());
						setHandler(bpr);
			    		ad.obtieneArchivoPorId("SYS", idArchivo, archivo, datosArchivo, errorAD);
			    		
			    		imagen.setArchivo(archivo.value);
			    		imagen.setError(errorAD.value);
			    		imagen.setNombre(GetValorNodo(datosArchivo.value,"NombreFichero"));
			    		imagen.setTipoArchivo(GetValorNodo(datosArchivo.value,"TipoArchivo"));
			    		imagenesAD.getImagenes().add(imagen);
       				}
       			}
       		imagenes.value = imagenesAD;
        }
        catch (Exception ex)
        {

            error.value = "Error al obtener las imágenes: " + ex.getMessage();
        }
	}
		
	
}
