package es.tributasenasturias.docs;

import java.io.ByteArrayOutputStream;

import org.w3c.dom.Document;

import es.tributasenasturias.Exceptions.ImpresionGDException;
import es.tributasenasturias.documentopagoutils.Base64;
import es.tributasenasturias.documentopagoutils.DocumentoGD;
import es.tributasenasturias.documentopagoutils.GZIPImplWeb;
import es.tributasenasturias.documentopagoutils.Logger;
import es.tributasenasturias.documentopagoutils.Preferencias;
import es.tributasenasturias.documentos.util.ConversorParametrosLanzador;
import es.tributasenasturias.soap.handler.HandlerUtil;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivo;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivoService;

/**
 * Permite la reimpresión de un documento de tributas. Recuperará el xml de documento
 * preparado para la impresión que será ofrecido por la base de datos.
 * @author crubencvs
 *
 */
public class Reimpresion {

	private Reimpresion()
	{}
	/**
	 * Método generador. No se utilizará directamente el constructor, por si en el futuro 
	 * fuera necesario alguna inicialización compleja.
	 * @return
	 */
	public static Reimpresion newReimpresion()
	{
		return new Reimpresion();
	}
	
	/**
	 * Recupera el PDF reimprimido.
	 * @param elemento Elemento identificador del reimprimible
	 * @param tipo Tipo del reimprimible
	 * @param codigoVerificacion Código de verificación a imprimir en cada página.
	 * @return Una cadena en base 64 con el contenido del pdf.
	 * @throws ImpresionGDException
	 */
	public String getPDFReimprimido(String elemento, String tipo, String codigoVerificacion) throws ImpresionGDException
	{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		//ByteArrayOutputStream[] bufferArray = new ByteArrayOutputStream[]{new ByteArrayOutputStream()};
		
		Document doc=null;
		doc = getXmlReimpresion (elemento, tipo);

		//Creamos el objeto DocumentoGD y generamos el PDF
		DocumentoGD gd = new DocumentoGD(doc, buffer);
		gd.setCodigoVerificacion(codigoVerificacion);
		byte [] res = gd.Generar();
		char[] c=null;
		if (res!=null)
		{
			c = Base64.encode(res);
		}
		return new String(c);
	}
	/**
	 * Recupera el documento xml con los datos necesarios para la reimpresión.
	 * @param elemento Elemento identificador del reimprimible
	 * @param tipo Tipo del reimprimible.
	 * @return
	 */
	private Document getXmlReimpresion (String elemento, String tipo)
	{
	 Document doc=null;
	 //String iniCDATA = "<![CDATA["; 
	 try{
		ConversorParametrosLanzador cpl;
		Preferencias pref = new Preferencias();
		cpl = new ConversorParametrosLanzador();
        cpl.setProcedimientoAlmacenado(pref.getPaGetReimprimible());
        //Elemento
        cpl.setParametro(elemento,ConversorParametrosLanzador.TIPOS.String);
        // Tipo
        cpl.setParametro(tipo,ConversorParametrosLanzador.TIPOS.String);
        //Conexion
        cpl.setParametro("P",ConversorParametrosLanzador.TIPOS.String);

        LanzaPLMasivoService lanzaderaWS = new LanzaPLMasivoService();					
		LanzaPLMasivo lanzaderaPort;			
		lanzaderaPort = lanzaderaWS.getLanzaPLMasivoSoapPort();
        
		// enlazador de protocolo para el servicio.
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
		// Cambiamos el endpoint
		bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointLanzador());
        String respuesta = "";	

        //Vinculamos con el Handler	        
        HandlerUtil.setHandlerClient((javax.xml.ws.BindingProvider) lanzaderaPort);
        try {	        	
        	respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");
        	cpl.setResultado(respuesta);	
        	//Convertimos la respuesta en un XML
        	String clob=cpl.getNodoResultado("CLOB_DATA");
        	//Extraemos el contenido del nodo CLOB_DATA.
        	/*if (clob.indexOf(iniCDATA)==0) //Si viene incrustado en una sección CDATA
        	{
        		clob = clob.substring(clob.indexOf(iniCDATA)+iniCDATA.length());
        		clob = clob.substring(0, clob.length()-3);
        	}*/
        	doc= XMLUtils.getXMLDoc(GZIPImplWeb.descomprimeWeb(clob));
        }catch (Exception ex) {
        		Logger.error("Error en lanzadera al recuperar datos del xml para reimprimir: "+ex.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
        		Logger.trace(ex.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
        }
	} catch (Exception e) {
		Logger.error("Excepcion generica al recuperar los datos del xml para reimprimir: "+e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
	}
		return doc;
	}
	
}
