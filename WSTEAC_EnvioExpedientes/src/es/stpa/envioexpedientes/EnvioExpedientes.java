package es.stpa.envioexpedientes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.Service.Mode;
import javax.xml.ws.soap.SOAPBinding;

import org.xml.sax.InputSource;

import es.stpa.envioexpedientes.log.TributasLogger;
import es.stpa.envioexpedientes.preferencias.Preferencias;

@WebServiceProvider(portName="ServicioEnvioExpedientesSOAP", serviceName="ServicioEnvioExpedientes",
targetNamespace = "http://expedientes.ws.teac.gob.es",
wsdlLocation="/wsdl/EnvioExpedientes.wsdl")
@ServiceMode(value=Mode.MESSAGE)
public class EnvioExpedientes implements Provider<Source>{

	private static enum Direccion
	{
		STPA,
		TEAC
	}
	private static enum Operacion
	{
		PETICION,
		RESPUESTA
	}
	@Override
	public Source invoke(Source request) {
		String idSesion = getIdLlamada();
		Preferencias pref = null;
		try {
			
			pref = new Preferencias();
			byte[] peticion = getContenido(request);
			//Registro de entrada
			logSoap(peticion, Direccion.STPA, Operacion.PETICION,idSesion, pref);
			//Registro de salida hacia TEAC, exactamente lo mismo
			logSoap(peticion, Direccion.TEAC,Operacion.PETICION,idSesion, pref);
			//Y envío al endpoint remoto
			Source respuestaTEAC = enviarRemoto(peticion,pref);
			byte[] respuesta = getContenido(respuestaTEAC);

			//Log en el cliente
			logSoap(respuesta, Direccion.TEAC,Operacion.RESPUESTA,idSesion, pref);
			//Log en el servidor
			logSoap(respuesta, Direccion.STPA,Operacion.RESPUESTA,idSesion, pref);
			return new StreamSource(new ByteArrayInputStream(respuesta));
		} catch (Exception e)
		{
			try {
				if (pref!=null){
					byte[] respuesta = getContenido(getErrorMessage(e));
					
					logSoap(respuesta, Direccion.STPA,Operacion.RESPUESTA,idSesion, pref);
				}
			} catch (Exception ex)
			{
				
			}
			return getErrorMessage(e);
		}
	}

	/**
	 * Recupera el contenido de un objeto Source como array de bytes
	 * @param request
	 * @return
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	private static byte[] getContenido(Source request) throws TransformerFactoryConfigurationError, TransformerException
	{
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		StreamResult r = new StreamResult(bo);
        Transformer trans = TransformerFactory.newInstance().newTransformer();
        trans.transform(request, r);
        return bo.toByteArray();
	}
	/**
	 * Log de mensajes SOAP
	 * @param mensaje
	 * @param direccion
	 * @param operacion
	 * @param sesionId
	 * @param pref
	 */
	private static void logSoap(byte[] mensaje, Direccion direccion, Operacion operacion,String sesionId, Preferencias pref)
	{
		TributasLogger log=null;
		String ficheroLog="";
		String dir = "";
		try
		{
			if (direccion.equals(Direccion.STPA)){
				ficheroLog = pref.getFicheroLogServer();
				if (operacion.equals(Operacion.PETICION)){
					dir = "Recepción";
				}
				else 
				{
					dir ="Envío";
				}
				
			}else {
				ficheroLog = pref.getFicheroLogClient();
				if (operacion.equals(Operacion.PETICION))
				{
					dir = "Envío";
				}
				else {
					dir = "Recepción";
				}
			}
			log=new TributasLogger(pref.getModoLog(), ficheroLog, sesionId);

	        String soapMessage= new String(mensaje);
	        log.info(dir+":"+soapMessage);
		}
		catch (Exception ex)
		{
			
		}
	}
	/**
	 * Envía el mensaje sin modificar al servicio remoto de TEAC.
	 * @param source
	 * @param pref
	 * @return
	 */
	private static Source enviarRemoto (byte[] mensaje, Preferencias pref){
		try {
			QName nombreServicio = new QName("http://expedientes.ws.teac.gob.es","ServicioEnvioExpedientes");
			Service srv = Service.create(nombreServicio);
			QName nombrePort = new QName("http://expedientes.ws.teac.gob.es","ServicioEnvioExpedientesSOAP");
			
			srv.addPort(nombrePort, SOAPBinding.SOAP11HTTP_BINDING,pref.getEndpointTEAC());
			Dispatch<Source> dispatch = srv.createDispatch(nombrePort, Source.class, Mode.MESSAGE);
			
			//dispatch.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, pref.getEndpointTEAC());
			ByteArrayInputStream bi = new ByteArrayInputStream(mensaje); 
			Source source = new SAXSource(new InputSource(bi));
			return dispatch.invoke(source);
		} catch (Exception e)
		{
			return getErrorMessage(e);
		}
	}
	/**
	 * Genera un mensaje de error que se devolverá cuando falle algo en el servicio.
	 * @param e
	 * @return
	 */
	private static Source getErrorMessage(Exception e)
	{
		String error ="<S:Envelope xmlns:S='http://schemas.xmlsoap.org/soap/envelope/'> "+
					 "<S:Body>"+
					 "<S:Fault xmlns:ns4='http://www.w3.org/2003/05/soap-envelope'>" +
					 "<faultcode>S:Server</faultcode>" +
					 "<faultstring>"+e.getMessage()+"</faultstring>"+
					 "</S:Fault>"+
					 "</S:Body>"+
					 "</S:Envelope>";
       return new StreamSource(new ByteArrayInputStream(error.getBytes()));		
	}
	/**
	 * Codifica los bytes que se indican en hexadecimal
	 * @param aInput
	 * @return
	 */
	private static String hexEncode( byte[] aInput){
		int MASK_240 = 0xf0;
		int SHIFT_DIVIDIR_16 = 4;
		int MASK_15 = 0x0f;
	    StringBuilder result = new StringBuilder();
	    char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};
	    for ( int idx = 0; idx < aInput.length; ++idx) {
	      byte b = aInput[idx];
	      result.append( digits[ (b&MASK_240) >> SHIFT_DIVIDIR_16 ] );
	      result.append( digits[ b&MASK_15] );
	    }
	    return result.toString();
	  }
	/**
	 * Recupera un checksum basado en un identificador único. Se asegura que el identificador es único.
	 */
	public static String getIdLlamada()
	{
		UUID id=UUID.randomUUID();
		String identificador=null;
		byte[] mensaje = id.toString().getBytes();
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(mensaje);
			identificador=hexEncode(md.digest());

		}catch(NoSuchAlgorithmException nsae){
			identificador="";
		}
		return identificador;
	}
	

}
