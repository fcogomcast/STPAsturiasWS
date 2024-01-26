package es.stpa.notificagestionenvios;

import https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios.Contenido;
import https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios.Destinatarios;
import https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios.Documento;
import https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios.Envio;
import https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios.Envios;
import https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios.Opcion;
import https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios.Opciones;
import https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios.Persona;
import https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios.ResultadoEnvio;
import https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios.ResultadoEnvios;
import https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2.Certificacion;
import https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2.CodigoDIR;
import https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2.Datados;
import https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2.EntregaDEH;
import https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2.EntregaPostal;
import https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2.Procedimiento;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Binding;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPFaultException;

import es.gob.administracionelectronica.notifica.ws.notificaws_v2.NotificaWsV2PortType;
import es.gob.administracionelectronica.notifica.ws.notificaws_v2.NotificaWsV2Service;
import es.gob.administracionelectronica.notifica.ws.sincronizarenvio._1.SincronizarEnvioWsPortType;
import es.gob.administracionelectronica.notifica.ws.sincronizarenvio._1.SincronizarEnvioWsService;
import es.gob.administracionelectronica.notifica.ws.sincronizarenvio._1_0.sincronizarenviooe.Acuse;
import es.gob.administracionelectronica.notifica.ws.sincronizarenvio._1_0.sincronizarenviooe.Receptor;
import es.stpa.notificagestionenvios.exceptions.AltaRemesaException;
import es.stpa.notificagestionenvios.exceptions.SincronizacionEnvioException;
import es.stpa.notificagestionenvios.preferencias.Preferencias;
import es.stpa.notificagestionenvios.soap.GestorUsernameToken;
import es.stpa.notificagestionenvios.soap.SoapClientHandler;
import es.tributasenasturias.log.ILog;

/**
 * Mediador con notifica, para realizar el envío
 * @author crubencvs
 * @version 2: se incluye soporte para sincronización de envío.
 */
public class MediadorNotifica {

	private ILog log;
	private Preferencias pref;
	private String idLlamada; 
	
	@SuppressWarnings("unchecked")
	public MediadorNotifica(Preferencias pref, ILog logger, String idLlamada){
		this.pref= pref;
		this.log= logger;
		this.idLlamada= idLlamada;
	}
	
	
	public static class IdentificadoresRemesa{
		private String identificador;
		private String nif;
		
		public IdentificadoresRemesa(String identificador, String nif){
			this.identificador=identificador;
			this.nif= nif;
		}
		public String getIdentificador() {
			return identificador;
		}
		public void setIdentificador(String identificador) {
			this.identificador = identificador;
		}
		public String getNif() {
			return nif;
		}
		public void setNif(String nif) {
			this.nif = nif;
		}
		
	}
	public static class RespuestaAlta{
		private boolean esError;
		private String codRespuesta;
		private String descripcionRespuesta;
		private List<IdentificadoresRemesa> identificadores;
		public boolean isEsError() {
			return esError;
		}
		public void setEsError(boolean esError) {
			this.esError = esError;
		}
		public String getCodRespuesta() {
			return codRespuesta;
		}
		public void setCodRespuesta(String codRespuesta) {
			this.codRespuesta = codRespuesta;
		}
		public String getDescripcionRespuesta() {
			return descripcionRespuesta;
		}
		public void setDescripcionRespuesta(String descripcionRespuesta) {
			this.descripcionRespuesta = descripcionRespuesta;
		}
		public List<IdentificadoresRemesa> getIdentificadores() {
			if (identificadores==null){
				identificadores= new ArrayList<IdentificadoresRemesa>();
			}
			return identificadores;
		}
	}
	//CRUBENCVS 43816 23/11/2021
	public static class RespuestaSincronizacion{
		private boolean esError;
		private String codRespuesta;
		private String descripcionRespuesta;
		public boolean isEsError() {
			return esError;
		}
		public void setEsError(boolean esError) {
			this.esError = esError;
		}
		public String getCodRespuesta() {
			return codRespuesta;
		}
		public void setCodRespuesta(String codRespuesta) {
			this.codRespuesta = codRespuesta;
		}
		public String getDescripcionRespuesta() {
			return descripcionRespuesta;
		}
		public void setDescripcionRespuesta(String descripcionRespuesta) {
			this.descripcionRespuesta = descripcionRespuesta;
		}
	}
	
	//CRUBENCVS 44788.  01/04/2022.
	public static class RespuestaConsulta{
		public static class Datado{
			private String organismoEmisor;
			private Date fecha;
			private String resultado;
			public Date getFecha() {
				return fecha;
			}
			public String getResultado() {
				return resultado;
			}
			public String getOrganismoEmisor() {
				return organismoEmisor;
			}
			
			
		}
		
		public static class Certificacion{
			private byte[] contenido;
			private Date fecha;
			private String hash;
			public byte[] getContenido() {
				return contenido;
			}
			public Date getFecha() {
				return fecha;
			}
			public String getHash() {
				return hash;
			}
			
		}
		private boolean error;
		private String codRespuesta;
		private String descripcionRespuesta;
		private String identificador;
		private String estado;
		private Datado datado;
		private Certificacion certificacion;
		public String getIdentificador() {
			return identificador;
		}
		public String getEstado() {
			return estado;
		}
		public Datado getDatado() {
			return datado;
		}
		public Certificacion getCertificacion() {
			return certificacion;
		}
		public boolean isError() {
			return error;
		}
		public String getCodRespuesta() {
			return codRespuesta;
		}
		public String getDescripcionRespuesta() {
			return descripcionRespuesta;
		} 
	}
	//CRUBENCVS 43816. 23/11/2021
	private byte[] hash256(byte[] contenido) throws Exception{
		MessageDigest md= MessageDigest.getInstance("SHA-256");
		md.update(contenido);
		return md.digest();
	}
	/**
	 * Realiza el hash del contenido del archivo que se indica.
	 * En notifica lo necesitan en base64, así que lo codificamos así.
	 * @param contenido Contenido del archivo
	 * @return Cadena con el HASH SHA256 en base64
	 * @throws Exception
	 */
	private String hashArchivo(byte[] contenido) throws Exception{
		return new String(Base64.encode(hash256(contenido)));
	}
	/**
	 * Establece el endpoint y el manejador por defecto del port 
	 * que se le pasa, como BindingProvider 
	 * @param bpr BindingProvider (port) a modificar
	 * @param endpoint
	 */
	@SuppressWarnings("unchecked")
	private void establecePort(javax.xml.ws.BindingProvider bpr, String endpoint){

		bpr.getRequestContext().put(
				javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				endpoint);
		Binding bi = bpr.getBinding();
		
		List<Handler> handlerList = bi.getHandlerChain();
		if(handlerList == null)
			handlerList = new ArrayList<Handler>();
		
		handlerList.add(new SoapClientHandler(this.idLlamada));
		bi.setHandlerChain(handlerList);
	}
	/**
	 * Establece la seguridad del mensaje de salida, por usernametoken
	 * @param bpr
	 */
	@SuppressWarnings("unchecked")
	private void estableceSeguridadMensaje(javax.xml.ws.BindingProvider bpr){

		Binding bi = bpr.getBinding();
		
		List<Handler> handlerList = bi.getHandlerChain();
		if(handlerList == null)
			handlerList = new ArrayList<Handler>();
		
		handlerList.add(new GestorUsernameToken());
		bi.setHandlerChain(handlerList);
	}
	//FIN CRUBENCVS 43816
	/**
	 * Realiza la llamada al alta de remesa de envíos en Notifica.
	 * @param codOrganismoEmisor
	 * @param tipoEnvio
	 * @param concepto
	 * @param titNif
	 * @param titNombre
	 * @param titApellidos
	 * @param titRazonSocial
	 * @param titTelefono
	 * @param titEmail
	 * @param destNif
	 * @param destNombre
	 * @param destApellidos
	 * @param destRazonSocial
	 * @param destTelefono
	 * @param destEmail
	 * @param destRefEmisor
	 * @param destCaducidad
	 * @param codSia
	 * @param pdf
	 * @return
	 * @throws AltaRemesaException
	 */
	public RespuestaAlta altaRemesaEnvios(
			String codOrganismoEmisor, 
			String tipoEnvio, 
			String concepto, 
			String titNif, 
			String titNombre,
			String titApellidos, 
			String titRazonSocial,
			String titTelefono, 
			String titEmail,
			String destNif, 
			String destNombre, 
			String destApellidos,
			String destRazonSocial,
			String destTelefono, 
			String destEmail, 
			String destRefEmisor,
			String destCaducidad, 
			String codSia,
			byte[] pdf) throws AltaRemesaException{
		
		log.debug("Rellenamos los campos del envío a Notifica");
		Holder<String> codigoOrganismoEmisor = new Holder<String>();
		codigoOrganismoEmisor.value=codOrganismoEmisor;
		
		BigInteger tipoEnv= new BigInteger(tipoEnvio);
		Documento doc= new Documento();
		Contenido contenidoDoc= new Contenido();
		//CRUBENCVS 46641 23/11/2022
		//Soporte para los envíos por SOAP with Attachments
		if ("S".equals(pref.getEnvioFicheroAltaAdjunto())){
			log.debug("Tratamiento del fichero. Se adjunta al mensaje y se obtiene su hash");
			//Uso la clase de javax.mail que me permite definir un datasource sobre 
			//el array de bytes, porque si defino directamente el DataHandler me indica
			//no object DCH for MIME type application/octet-stream
			ByteArrayDataSource ds = new ByteArrayDataSource(pdf,"application/pdf");
			DataHandler dh = new DataHandler(ds);
			contenidoDoc.setHref(dh);
		} else {
			log.debug("Tratamiento del fichero. Se codifica en base64 y se obtiene su hash");
			contenidoDoc.setValue(new String(Base64.encode(pdf)));
		}
		//FIN CRUBENCVS 46641 23/11/2022
		doc.setContenido(contenidoDoc);
		try {
			doc.setHash(hashArchivo(pdf));
		} catch (Exception e){
			throw new AltaRemesaException("Error al calcular el hash del archivo:"+e.getMessage(),e);
		}
		
		Opciones opcsRemesa= new Opciones();
		Opcion opcCaducidad= new Opcion();
		opcCaducidad.setTipo("caducidad");
		opcCaducidad.setValue(destCaducidad);
		opcsRemesa.getOpcion().add(opcCaducidad);
		
		Persona destinatario= new Persona();
		destinatario.setNif(destNif);
		destinatario.setNombre(destNombre);
		destinatario.setApellidos(destApellidos);
		destinatario.setTelefono(destTelefono);
		destinatario.setEmail(destEmail);
		destinatario.setRazonSocial(destRazonSocial);
		
		Destinatarios destinatarios = new Destinatarios();
		destinatarios.getDestinatario().add(destinatario);
		
		Persona titular= new Persona();
	    titular.setNif(titNif);
	    titular.setNombre(titNombre);
	    titular.setApellidos(titApellidos);
	    titular.setTelefono(titTelefono);
	    titular.setEmail(titEmail);
	    titular.setRazonSocial(titRazonSocial);
	    
	    Envios envios= new Envios();
	    Envio envio= new Envio();
	    envio.setDestinatarios(destinatarios);
	    envio.setTitular(titular);
	    envio.setReferenciaEmisor(destRefEmisor);
	    
	    envios.getEnvio().add(envio);

	    Holder<String> codigoRespuesta= new Holder<String>();
	    Holder<String> descripcionRespuesta= new Holder<String>();
	    Holder<XMLGregorianCalendar> fechaCreacion= new Holder<XMLGregorianCalendar>();
	    Holder<ResultadoEnvios> resultadoEnvios= new Holder<ResultadoEnvios>();
	    log.debug("Creamos el port contra Notifica");
	    NotificaWsV2Service srv= new NotificaWsV2Service();
	    NotificaWsV2PortType portAlta= srv.getNotificaWsV2Port();
	    if ("S".equals(pref.getEnvioFicheroAltaAdjunto())){
	    	establecePort((javax.xml.ws.BindingProvider) portAlta, pref.getEndpointGestionEnviosNotificaSwA());
	    } else {
	    	establecePort((javax.xml.ws.BindingProvider) portAlta, pref.getEndpointGestionEnvioNotifica());
	    }
	    log.debug("Llamada a altaRemesaEnvios");
	    RespuestaAlta respuesta= new RespuestaAlta();
	    //Pueden enviarnos errores encapsulados en SOAPFault
	    try {
		    portAlta.altaRemesaEnvios(
		    			codigoOrganismoEmisor, 
		    			tipoEnv, 
		    			concepto, 
		    			null, 
		    			null, 
		    			codSia, 
		    			doc, 
		    			envios, 
		    			opcsRemesa, 
		    			pref.getNotificaAPIKey(), 
		    			codigoRespuesta, 
		    			descripcionRespuesta, 
		    			fechaCreacion, 
		    			resultadoEnvios);
	    
	    
		    if (Constantes.ALTA_OK.equals(codigoRespuesta.value)){
		    	respuesta.setEsError(false);
		    	//Hay resultados
		    	for (ResultadoEnvio res:resultadoEnvios.value.getItem()){
		    		respuesta.getIdentificadores().add(new IdentificadoresRemesa(res.getIdentificador(),res.getNifTitular()));
		    	}
		    } else {
		    	respuesta.setEsError(true);
		    }
		    respuesta.setCodRespuesta(codigoRespuesta.value);
		    respuesta.setDescripcionRespuesta(descripcionRespuesta.value);
	    } catch (SOAPFaultException soap){
	    	respuesta.setEsError(true);
	    	if (soap.getFault()!=null){
	    		respuesta.setCodRespuesta(soap.getFault().getFaultCode());
	    		respuesta.setDescripcionRespuesta(soap.getFault().getFaultString());
	    	} else {
	    		throw soap;
	    	}
	    }
	    
	    return respuesta;
	}
	/**
	 * Sincroniza el estado del envío con Notifica
	 * @param organismoEmisor
	 * @param identificador
	 * @param tipoEntrega
	 * @param modoNotificacion
	 * @param estado
	 * @param fechaEstado
	 * @param nifReceptor
	 * @param nombreReceptor
	 * @param vinculoReceptor
	 * @param pdf
	 * @return
	 * @throws SincronizacionEnvioException
	 */
	public RespuestaSincronizacion sincronizarEnvio(
			String organismoEmisor, 
			String identificador,
			int tipoEntrega, 
			int modoNotificacion, 
			String estado,
			XMLGregorianCalendar fechaEstado, 
			String nifReceptor,
			String nombreReceptor, 
			int vinculoReceptor, 
			byte[] pdf) throws SincronizacionEnvioException{
		
		log.debug("Rellenamos los campos de la sincronización con  Notifica");
		
		log.debug("Tratamiento del fichero. Se codifica en base64 y se obtiene su hash");
		Acuse acusePdf= new Acuse();
		acusePdf.setContenido(pdf);
		try {
			acusePdf.setHash(hashArchivo(pdf));
		} catch (Exception e){
			throw new SincronizacionEnvioException("Error al calcular el hash del archivo:"+e.getMessage(),e);
		}
		
		log.debug("Tratamiento del receptor");
		Receptor receptor= new Receptor();
		receptor.setNifReceptor(nifReceptor);
		receptor.setNombreReceptor(nombreReceptor);
		receptor.setVinculoReceptor(BigInteger.valueOf(vinculoReceptor));
	    
	    
	    log.debug("Creamos el port contra Notifica");
	    SincronizarEnvioWsService srv= new SincronizarEnvioWsService();
	    SincronizarEnvioWsPortType portSincronizacion= srv.getSincronizarEnvioWsPort();
	    //Usernametoken y log de mensaje
	    estableceSeguridadMensaje((javax.xml.ws.BindingProvider) portSincronizacion);
	    establecePort((javax.xml.ws.BindingProvider) portSincronizacion, pref.getEndpointNotificaSincronizarEnvio());
	    log.debug("Llamada a sincronizaEnvio");
	    RespuestaSincronizacion respuesta= new RespuestaSincronizacion();
	    Holder<String> codigoRespuesta= new Holder<String>();
	    Holder<String> descripcionRespuesta= new Holder<String>();
	    Holder<String> holderEstado = new Holder<String>();
	    Holder<XMLGregorianCalendar> holderFechaEstado= new Holder<XMLGregorianCalendar>();
	    holderEstado.value=estado;
	    holderFechaEstado.value= fechaEstado;
	    Acuse acuseXML=null;
	    es.gob.administracionelectronica.notifica.ws.sincronizarenvio._1_0.common.Opciones opcionesSincronizarEnvio=null;
	    //Pueden enviarnos errores encapsulados en SOAPFault
	    try {
	    	//No utilizo estas opciones para nada, pero dado que Notifica puede depositar información 
	    	//en un futuro, lo creo.
	    	Holder<es.gob.administracionelectronica.notifica.ws.sincronizarenvio._1_0.common.Opciones> opcionesRespuestaSincronizar= new Holder<es.gob.administracionelectronica.notifica.ws.sincronizarenvio._1_0.common.Opciones>();
	    	portSincronizacion.sincronizarEnvioOE(
	    			organismoEmisor,
	    			identificador, 
	    			BigInteger.valueOf(tipoEntrega), 
	    			BigInteger.valueOf(modoNotificacion), 
	    			holderEstado, 
	    			holderFechaEstado, 
	    			"", //Sólo necesario en anulación, para indicar el motivo 
	    			receptor, 
	    			acusePdf, 
	    			acuseXML,  
	    			opcionesSincronizarEnvio,  
	    			codigoRespuesta, 
	    			descripcionRespuesta, 
	    			opcionesRespuestaSincronizar);
	    
	    
		    respuesta.setCodRespuesta(codigoRespuesta.value);
		    respuesta.setDescripcionRespuesta(descripcionRespuesta.value);
	    } catch (SOAPFaultException soap){
	    	respuesta.setEsError(true);
	    	if (soap.getFault()!=null){
	    		respuesta.setCodRespuesta(soap.getFault().getFaultCode());
	    		respuesta.setDescripcionRespuesta(soap.getFault().getFaultString());
	    	} else {
	    		throw soap;
	    	}
	    }
	    
	    return respuesta;
	}
	
	/**
	 * Recupera el último datado de los que vengan en la respuesta de la consulta
	 * @param datados Lista de los datados
	 * @return
	 */
	private RespuestaConsulta.Datado recuperaUltimoDatado(List<https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2.Datado> datados){
		RespuestaConsulta.Datado datado= null;
		if (datados==null || datados.size()>0){
			https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2.Datado mayor= datados.get(0);
			if (datados.size()>1){
				for (https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2.Datado d : datados){
					if (d.getFecha().compare(mayor.getFecha())==DatatypeConstants.GREATER){
						mayor= d;
					}
				}
			}
			datado= new RespuestaConsulta.Datado();
			datado.fecha= mayor.getFecha().toGregorianCalendar().getTime();
			datado.resultado= mayor.getResultado();
		} 
		return datado;
	}
	/**
	 * Consulta de la información del envío en Notific@
	 * @param identificadorEnvio
	 * @return
	 */
	public RespuestaConsulta consultarEnvio(String identificadorEnvio) {
		
		log.debug("Rellenamos los campos de la consulta  contra Notifica");

		Holder<String> identificadorHolder= new Holder<String>();
        Holder<String> estadoHolder=  new Holder<String>();
        Holder<String> conceptoHolder = new Holder<String>();
        Holder<String> descripcionHolder = new Holder<String>();
        Holder<CodigoDIR> codigoOrganismoEmisorHolder= new Holder<CodigoDIR>();
        Holder<CodigoDIR> codigoOrganismoEmisorRaizHolder = new Holder<CodigoDIR>();
        Holder<String> tipoEnvioHolder = new Holder<String>();
        Holder<XMLGregorianCalendar> fechaCreacionHolder= new Holder<XMLGregorianCalendar>();
        Holder<XMLGregorianCalendar> fechaPuestaDisposicionHolder= new Holder<XMLGregorianCalendar>();
        Holder<XMLGregorianCalendar> fechaCaducidadHolder= new Holder<XMLGregorianCalendar>();
        Holder<BigInteger> retardoHolder = new Holder<BigInteger>();
        Holder<Procedimiento> procedimientoHolder = new Holder<Procedimiento>();
        Holder<https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2.Documento> documentoHolder= new Holder<https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2.Documento>();
        Holder<String> referenciaEmisorHolder= new Holder<String>();
        Holder<https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2.Persona> titularHolder= new Holder<https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2.Persona>();
        Holder<https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2.Destinatarios> destinatariosHolder = new Holder<https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2.Destinatarios>();
        Holder<EntregaPostal> entregaPostalHolder= new Holder<EntregaPostal>();
        Holder<EntregaDEH> entregaDEHHolder = new Holder<EntregaDEH>();
        Holder<Datados> datadosHolder= new Holder<Datados>();
        Holder<Certificacion> certificacionHolder = new Holder<Certificacion>(); 
        Holder<https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2.Opciones> opcionesEnvioHolder = new Holder<https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2.Opciones>();
	    log.debug("Creamos el port contra Notifica");
	    NotificaWsV2Service srv= new NotificaWsV2Service();
	    NotificaWsV2PortType portActualizacion= srv.getNotificaWsV2Port();
	    establecePort((javax.xml.ws.BindingProvider) portActualizacion, pref.getEndpointGestionEnvioNotifica());
	    log.debug("Llamada a infoEnvioV2");
	    RespuestaConsulta respuesta= new RespuestaConsulta();
	    identificadorHolder.value= identificadorEnvio;
	    //Pueden enviarnos errores encapsulados en SOAPFault
	    try {
		    
	    	portActualizacion.infoEnvioV2(identificadorHolder, 
	    								  pref.getNotificaAPIKey(), 
	    								  estadoHolder, 
	    								  conceptoHolder, 
	    								  descripcionHolder, 
	    								  codigoOrganismoEmisorHolder, 
	    								  codigoOrganismoEmisorRaizHolder, 
	    								  tipoEnvioHolder, 
	    								  fechaCreacionHolder, 
	    								  fechaPuestaDisposicionHolder, 
	    								  fechaCaducidadHolder, 
	    								  retardoHolder, 
	    								  procedimientoHolder, 
	    								  documentoHolder, 
	    								  referenciaEmisorHolder, 
	    								  titularHolder, 
	    								  destinatariosHolder, 
	    								  entregaPostalHolder, 
	    								  entregaDEHHolder, 
	    								  datadosHolder, 
	    								  certificacionHolder, 
	    								  opcionesEnvioHolder);
	    	
	    	respuesta.estado= estadoHolder.value;
	    	if (respuesta.estado!=null){
	    		if (!respuesta.estado.startsWith("pendiente")){
	    			// Saco los datos del último datado, si hubiera.
	    			if (datadosHolder.value!=null && datadosHolder.value.getDatado().size()>0){
	    				respuesta.datado= recuperaUltimoDatado(datadosHolder.value.getDatado());
	    				respuesta.datado.organismoEmisor = codigoOrganismoEmisorHolder.value.getCodigo();
	    			}
	    			if (certificacionHolder.value!=null){
	    				respuesta.certificacion= new RespuestaConsulta.Certificacion();
	    				respuesta.certificacion.contenido= Base64.decode(certificacionHolder.value.getContenidoCertificacion().getValue().toCharArray());
	    				respuesta.certificacion.fecha= certificacionHolder.value.getFechaCertificacion().toGregorianCalendar().getTime();
	    				respuesta.certificacion.hash= certificacionHolder.value.getHash();
	    			}
	    		}
	    		//Si es pendiente, no nos interesa actualizar nada
	    	}
	    	respuesta.error=false;
	    } catch (SOAPFaultException soap){
	    	respuesta.error=true;;
	    	if (soap.getFault()!=null){
	    		respuesta.codRespuesta=soap.getFault().getFaultCode();
	    		respuesta.descripcionRespuesta=soap.getFault().getFaultString();
	    	} else {
	    		throw soap;
	    	}
	    }
	    
	    return respuesta;
	}
}
