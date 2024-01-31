package es.tributasenasturias.servicios.asturcon.consultaDeuda.soap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import es.tributasenasturias.utils.log.Logger;

/**
 * 
 * @author crubencvs
 *
 */
public class RecuperadorIdMensaje extends DefaultHandler{

	
	Logger log=null;
	String id=""; //Id de la ejecución
	/**
	 * Constructor, se hace privadao para obligar a utilizar el otro.
	 */
	private RecuperadorIdMensaje()
	{
		super();
	}
	/**
	 * Constructor que permite indicar el log a usar.
	 * @param log log que se pasará al objeto para que lo utilice.
	 */
	public RecuperadorIdMensaje(Logger log) {
		this();
		this.log=log;
	}
	
	/**
	 * Convierte de un elemento DOM a un stream. Necesario para convertir de DOMSource
	 * a InputSource, que es el que permite la operación con SAX.
	 * @param element {@link Element} a transformar.
	 * @param out {@link OutputStream} que se devuelve.
	 */
	private static void ElementToStream(Element element, OutputStream out) {
	    try {
	      DOMSource source = new DOMSource(element);
	      StreamResult result = new StreamResult(out);
	      TransformerFactory transFactory = TransformerFactory.newInstance();
	      Transformer transformer = transFactory.newTransformer();
	      transformer.transform(source, result);
	    } catch (Exception ex) {
	    }
	  }
	/**
	 * Convierte de un {@link Source} genérico a {@link InputSource} para poder utilizar SAX.
	 * @param source {@link Source} que contiene los datos. Según el tipo de fuente, se aplicará una transformación u otra. 
	 * @return {@link InputSource} que puede utilizarse para utilizar SAX en mensajes.
	 */
	private static InputSource sourceToInputSource(Source source) {
	      if (source instanceof SAXSource) {
	          return ((SAXSource) source).getInputSource();
	      } else if (source instanceof DOMSource) {
	          ByteArrayOutputStream baos = new ByteArrayOutputStream();
	          Node node = ((DOMSource) source).getNode();
	          if (node instanceof Document) {
	              node = ((Document) node).getDocumentElement();
	          }
	          Element domElement = (Element) node;
	          ElementToStream(domElement, baos);
	          InputSource isource = new InputSource(source.getSystemId());
	          isource.setByteStream(new ByteArrayInputStream(baos.toByteArray()));
	          return isource;
	      } else if (source instanceof StreamSource) {
	          StreamSource ss = (StreamSource) source;
	          InputSource isource = new InputSource(ss.getSystemId());
	          isource.setByteStream(ss.getInputStream());
	          isource.setCharacterStream(ss.getReader());
	          isource.setPublicId(ss.getPublicId());
	          return isource;
	      } 
	      else
	      {
	    	  return null;
	      }
	}
	/**
	 * Recupera el ID de ejecución de la carga.
	 * @param message Mensaje de entrada al servicio, como {@link SOAPMessage}
	 * @return Id de ejecución que se encuentra en el mensaje, o cadena vacía si no se encuentra.
	 */
	public String getIdEjecucion(SOAPMessage message)
	{
		try {
			XMLReader xr=XMLReaderFactory.createXMLReader();
			DefaultHandler handler = this;
			InputSource source= sourceToInputSource(message.getSOAPPart().getContent());
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
			xr.parse(source);
		} catch (Exception e) {
			id="";	
			log.error("Error al recuperar el id de mensaje:" +e.getMessage());
		}
		return id;
	}
	/**
	 * Recupera el  ID de ejecución de la carga
	 * @param mensaje Mensaje de entrada al servicio, como {@link SOAPMessage}
	 * @return Id de ejecución que se encuentra en el mensaje, o cadena vacía si no se encuentra.
	 */
	public String getIdEjecucion (String mensaje)
	{
		try {
			XMLReader xr=XMLReaderFactory.createXMLReader();
			DefaultHandler handler = this;
			InputSource source= new InputSource(new ByteArrayInputStream(mensaje.getBytes("UTF-8")));
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
			xr.parse(source);
		} catch (Exception e) {
			id="";	
			log.error("Error al recuperar el id de mensaje:" +e.getMessage());
		}
		return id;
	}
	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		//Procesamos únicamente el elemento "EJECUCION"
		if (localName.equalsIgnoreCase("EJECUCION"))
		{
			//Comprobamos sus atributos, para buscar el de tag ID
			for (int a=0;a<attributes.getLength();a++)
			{
				if (attributes.getLocalName(a).equalsIgnoreCase("ID"))
				{
					this.id=attributes.getValue(a);
					break;
				}
			}
		}
	}
}
