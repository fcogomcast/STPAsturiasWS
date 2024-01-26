package es.tributasenasturias.soap.handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.ws.LogicalMessage;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import es.tributasenasturias.log.Logger;
import es.tributasenasturias.utils.GestorIdLlamada;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.utils.Utils;

/**
 * Manejador que se ocupa de la validación del mensaje de entrada.
 * En caso de error en la validación, devuelve una excepción con información.
 * @author crubencvs
 *
 */
public class ValidacionEsquema implements LogicalHandler<LogicalMessageContext>{

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public boolean handleFault(LogicalMessageContext context) {
		return true;
	}

	@Override
	public boolean handleMessage(LogicalMessageContext context) {
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		Logger log = new Logger(GestorIdLlamada.getIdLlamada());
		if (salida.booleanValue())
		{
			return true;
		}
		LogicalMessage msg = context.getMessage();
		Source payload = msg.getPayload();
		StreamResult res = new StreamResult(new StringWriter());
		try {
			Transformer tra = TransformerFactory.newInstance().newTransformer();
			tra.transform(payload, res);
			String xml = res.getWriter().toString();
			validar(xml);
		} catch (TransformerConfigurationException e) {
			log.error("Error: imposible realizar la validación del mensaje de entrada:"+e.getMessage());
			throw new WebServiceException(Mensajes.MSG_ERROR_F002,e);
		} catch (TransformerFactoryConfigurationError e) {
			log.error("Error: imposible realizar la validación del mensaje de entrada:"+e.getMessage());
			throw new WebServiceException(Mensajes.MSG_ERROR_F002,e);
		} catch (TransformerException e) {
			log.error("Error: imposible realizar la validación del mensaje de entrada:"+e.getMessage());
			throw new WebServiceException(Mensajes.MSG_ERROR_F002,e);
		} catch (MalformedURLException e) {
			log.error("Error: imposible realizar la validación del mensaje de entrada:"+e.getMessage());
			throw new WebServiceException(Mensajes.MSG_ERROR_F002,e);
		} catch (ParserConfigurationException e) {
			log.error("Error: imposible realizar la validación del mensaje de entrada:"+e.getMessage());
			throw new WebServiceException(Mensajes.MSG_ERROR_F002,e);
		} catch (IOException e) {
			log.error("Error: imposible realizar la validación del mensaje de entrada:"+e.getMessage());
			throw new WebServiceException(Mensajes.MSG_ERROR_F002,e);
		} catch (SAXException e) {
			log.error("Error: el mensaje de entrada no corresponde al esquema utilizado:"+e.getMessage());
			Utils.generarSOAPFault("F001", Mensajes.MSG_ERROR_F001);
		}
		return true;
	}

	/**
	 * Realiza la validación de un mensaje contra el esquema de entrada del servicio.
	 * @param xml Cadena de texto conteniendo el payload del mensaje (el contenido de "Body")
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws SAXException Excepción que engloba los errores de validación contra el esquema.
	 */
	private void validar(String xml) throws ParserConfigurationException,
		IOException, MalformedURLException, SAXException {

	DocumentBuilder parser = null;
	Document document = null;
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	dbf.setNamespaceAware(true);
	parser = dbf.newDocumentBuilder();

	byte bytes[] = xml.getBytes();
	document = parser.parse(new ByteArrayInputStream(bytes));
	SchemaFactory factory = SchemaFactory
			.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

	Schema schema = null;
	schema = factory.newSchema(this.getClass().getResource(
			"/wsdl/xsd/recepcionDocumentacion.xsd"));
	javax.xml.validation.Validator validator = schema.newValidator();
	validator.validate(new DOMSource(document));
}  
}
