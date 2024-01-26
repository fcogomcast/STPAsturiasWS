package es.tributasenasturias.docs;

import java.io.ByteArrayOutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import es.tributasenasturias.Exceptions.ImpresionGDException;
import es.tributasenasturias.Exceptions.XMLDOMDocumentException;
import es.tributasenasturias.documentopagoutils.Base64;
import es.tributasenasturias.documentopagoutils.DocumentoGD;
import es.tributasenasturias.documentopagoutils.GZIPImplWeb;
import es.tributasenasturias.documentopagoutils.Preferencias;
import es.tributasenasturias.documentos.util.ConversorParametrosLanzador;
import es.tributasenasturias.soap.handler.HandlerUtil;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivo;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivoService;

/**
 * Permite la impresión de un documento utilizando el gestor Documental.
 * @author crubencvs
 *
 */
public class ImpresionGD {

	/**
	 * No se llama directamente.
	 */
	private ImpresionGD()
	{
	}
	/**
	 * Utilizado para devolver una nueva instancia del objeto.
	 * @return
	 */
	public static ImpresionGD newImpresionGD()
	{
		return new ImpresionGD();
	}
	
	/**
	 * Recupera el PDF impreso.
	 * @param origenDatos la cadena de petición a base de datos que devolverá los datos del informe.
	 * @param codigoVerificacion El código de verificación a imprimir en el documento, o null si no hay ninguno.
	 * @return Una cadena en base 64 con el contenido del pdf.
	 * @throws ImpresionGDException
	 */
	public String getPDFImpresion(String origenDatos, String codigoVerificacion) throws ImpresionGDException
	{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		Document doc=null;
		doc = getXmlImpresion (origenDatos);

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
	private Document getXmlImpresion (String origenDatos) throws ImpresionGDException
	{
	 Document doc=null;
		ConversorParametrosLanzador cpl;
		Preferencias pref = new Preferencias();
		try {
			cpl = new ConversorParametrosLanzador();
		} catch (ParserConfigurationException e) {
			throw new ImpresionGDException ("Error en llamada para recuperar documento a imprimir:" + e.getMessage(),e);
		}
		cpl.setProcedimientoAlmacenado(pref.getPaImpresion());
        //Origen
		//Se ha de pasar como base64 para que no haya problemas, ya que la petición
		//al lanzador se pasa como texto, y puede haber problemas por tener xml codificado en texto
		// (el origen de datos) dentro de otro xml Codificado en texto (petición).
		//Ejemplo: &lt;peti&gt;&lt;proc nombre="ImpresionGDSW.getxmlImpresion"&gt;&lt;param id="1"&gt;&lt;valor&gt;&lt;peti&gt; ...
        cpl.setParametro(GZIPImplWeb.comprimeWeb(origenDatos),ConversorParametrosLanzador.TIPOS.Clob);
        cpl.setParametro("S",ConversorParametrosLanzador.TIPOS.String); // Comprimido
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
       	
        	respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");
        	cpl.setResultado(respuesta);
        	if (!"".equals(cpl.getNodoResultado("error")))
        	{
        		throw new ImpresionGDException("No se han recuperado datos para imprimir. Resultado:" + respuesta);
        	}
        	//Convertimos la respuesta en un XML
        	String clob=cpl.getNodoResultado("CLOB_DATA");
        	try {
				doc= XMLUtils.getXMLDoc(GZIPImplWeb.descomprimeWeb(clob));
			} catch (XMLDOMDocumentException e) {
				throw new ImpresionGDException ("Los datos recibidos para imprimir no se pueden interpretar como XML:"+e.getMessage(),e);
			}
		return doc;
	}
}
