package es.tributasenasturias.servicios.accesocertificado;


import java.util.HashMap;

import javax.xml.ws.Holder;

import es.tributasenasturias.seguridad.servicio.InfoCertificado;
import es.tributasenasturias.seguridad.servicio.SeguridadException;
import es.tributasenasturias.seguridad.servicio.SeguridadFactory;
import es.tributasenasturias.seguridad.servicio.VerificadorCertificado;
import es.tributasenasturias.seguridad.servicio.InfoCertificado.Validez;
import es.tributasenasturias.services.lanzador.client.LanzadorException;
import es.tributasenasturias.services.lanzador.client.LanzadorFactory;
import es.tributasenasturias.services.lanzador.client.ParamType;
import es.tributasenasturias.services.lanzador.client.ProcedimientoAlmacenado;
import es.tributasenasturias.services.lanzador.client.TLanzador;
import es.tributasenasturias.services.lanzador.client.response.RespuestaLanzador;
import es.tributasenasturias.servicios.accesocertificado.contextoLlamadas.CallContext;
import es.tributasenasturias.servicios.accesocertificado.contextoLlamadas.CallContextConstants;
import es.tributasenasturias.servicios.accesocertificado.contextoLlamadas.IContextReader;
import es.tributasenasturias.servicios.accesocertificado.exceptions.STPAException;
import es.tributasenasturias.servicios.accesocertificado.log.LogFactory;
import es.tributasenasturias.servicios.accesocertificado.preferencias.Preferencias;
import es.tributasenasturias.servicios.accesocertificado.preferencias.PreferenciasFactory;
import es.tributasenasturias.servicios.accesocertificado.soap.SoapClientHandler;
import es.tributasenasturias.utils.log.Logger;

public class ProcesadorAsociarCertificadoUsuario implements IContextReader{
	CallContext context;
	private static final String idProceso="[AsociacionCertificadoUsuario]";
	private static final ResultadoAsociacionType resultadoOK =  new ResultadoAsociacionType();
	private static final ResultadoAsociacionType resultadoUsuarioNoValido =  new ResultadoAsociacionType();
	private static final ResultadoAsociacionType resultadoUsuarioYaAsociado =  new ResultadoAsociacionType();
	private static final ResultadoAsociacionType resultadoErrorGrave =  new ResultadoAsociacionType();
	private static final HashMap<String, ResultadoAsociacionType> mapeoResultadosBD=new HashMap<String, ResultadoAsociacionType>();
	
	static {
		synchronized(mapeoResultadosBD)
		{
			resultadoOK.setCodigo("0000");
			resultadoOK.setMensaje("Asociación correcta");
			
			mapeoResultadosBD.put("00",resultadoOK);
			
			resultadoUsuarioNoValido.setCodigo("0001");
			resultadoUsuarioNoValido.setMensaje("El usuario indicado no está autorizado.");
			
			mapeoResultadosBD.put("01", resultadoUsuarioNoValido);
			
			resultadoUsuarioYaAsociado.setCodigo("0002");
			resultadoUsuarioYaAsociado.setMensaje("Usuario ya asociado a un identificador fiscal. No se puede asociar de nuevo.");
			
			mapeoResultadosBD.put("02", resultadoUsuarioYaAsociado);
			
			resultadoErrorGrave.setCodigo("9999");
			resultadoErrorGrave.setMensaje("Error grave.");
			mapeoResultadosBD.put("50", resultadoErrorGrave);
			
		}
	}
	
	public static class ResultadoProceso {
		private boolean esError;
		private String codigo;
		private String mensaje;
		public boolean isEsError() {
			return esError;
		}
		public void setEsError(boolean esError) {
			this.esError = esError;
		}
		public String getCodigo() {
			return codigo;
		}
		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}
		public String getMensaje() {
			return mensaje;
		}
		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
		
	}
	@Override
	public CallContext getCallContext() {
		return context;
	}
	@Override
	public void setCallContext(CallContext ctx) {
		context=ctx;
	}
	Preferencias prefs;
	Logger log;
	/**
	 * Constructor por defecto, no se debería poder utilizar.
	 * @throws STPAException
	 */
	protected ProcesadorAsociarCertificadoUsuario() throws STPAException{
		throw new STPAException("No se puede instanciar directamente el objeto "+ProcesadorAsociarCertificadoUsuario.class.getName());
	}
	/**
	 * Constructor con contexto de llamada. Es el que debería utilizarse.
	 * @param context Contexto de la llamada.
	 * @throws STPAException
	 */
	protected ProcesadorAsociarCertificadoUsuario(CallContext context) throws STPAException
	{
		this.context = context;
		prefs = PreferenciasFactory.getPreferenciasContexto(context);
		log = LogFactory.getLoggerFromContext(context);
	}
	/**
	 * Realiza el proceso de asociación de usuario de tributas con un certificado.
	 * @param certificado certificado en base 64
	 * @param dncertificado DN del certificado
	 * @param Usuario usuario de tributas
	 * @param password clave de tributas
	 * @param esError parámetro de salida que indica si se ha producido un error
	 * @param resultado resultado de la operación, contendrá un código y un mensaje
	 * @return ConfirmacionAcce
	 * @throws STPAException En caso de que no se pueda realizar la confirmación.
	 */
	public void process (String certificado,
			String dnCertificado, String usuario, String password,
			Holder<Boolean> esError, Holder<ResultadoAsociacionType> resultado) throws STPAException
	{
		String idLlamada = (String) context.get(CallContextConstants.IDSESION);
		SoapClientHandler soapClient= new SoapClientHandler(idLlamada,prefs);
		log.info(idProceso+"0. Entrada al proceso de asociación de usuario al certificado.");
		
		log.info (idProceso+"1. Se valida que el certificado sea correcto.");
		try {
			if (certificado==null || "".equals(certificado))
			{
				throw new STPAException (idProceso+"El certificado recibido está vacío.");
			}
			InfoCertificado info = SeguridadFactory.newInfoCertificado();
			VerificadorCertificado ver = SeguridadFactory.newVerificadorCertificado(prefs.getEndpointAutenticacion(), soapClient);
			info = ver.login(certificado);
			if (Validez.VALIDO.equals(info.getValidez()))
			{
				//******************************************************
				//Se recupera siempre el NIF porque se supone que 
				//se utilizan certificados de persona física, 
				//o el NIF de la persona la identifica 
				//como persona autorizada en los certificados jurídicos.
				//******************************************************
				String id="";
				String nombre="";
				if (info.getNifNie()!=null && !"".equals(info.getNifNie()))
				{
					id = info.getNifNie();
					nombre=info.getNombre() + " "+ info.getApellido1()+ " " + info.getApellido2();
				}
				else
				{
					throw new STPAException (idProceso+" No se ha podido recuperar un NIF del certificado proporcionado.");
				}
				log.info  (idProceso+"2.Pasamos los datos a la base de datos");
				String code=asociarCertificado (usuario,password, id, nombre, dnCertificado, soapClient);
				resultado.value= mapeoResultadosBD.get(code);
			}
			else
			{
				throw new STPAException (idProceso+"El certificado no es válido.");
			}
		} catch (SeguridadException e) {
			throw new STPAException (idProceso+"Error en el proceso de asociación de certificado a usuario de tributas:" + e.getMessage(),e);
		} catch (LanzadorException e)
		{
			throw new STPAException (idProceso+"Error al realizar la llamada a base de datos en asociación de certificado a usuario:" + e.getMessage(),e);
		}

		if (resultadoOK.equals(resultado.value))
		{
			esError.value=Boolean.FALSE;
		}
		else
		{
			esError.value=Boolean.TRUE;
		}
		return;
		
	}
	private String asociarCertificado (String usuario, String password, String nif, String nombre, String dn, SoapClientHandler soapHandler) throws LanzadorException, STPAException
	{
		TLanzador lanz = LanzadorFactory.newTLanzador(prefs.getEndpointLanzador(), soapHandler);
		ProcedimientoAlmacenado proc  = new ProcedimientoAlmacenado(prefs.getProcAlmacenadoAsociarUsuaCert(),prefs.getEsquemaBD());
		proc.param(usuario, ParamType.CADENA);
		proc.param(password, ParamType.CADENA);
		proc.param(nif, ParamType.CADENA);
		proc.param(dn, ParamType.CADENA);
		proc.param("P", ParamType.CADENA);
		String res=lanz.ejecutar(proc);
		RespuestaLanzador respuesta = new RespuestaLanzador(res);
		if (respuesta.esErronea())
		{
			throw new STPAException (idProceso+"Error al realizar la asociación de certificado y usuario en Base de Datos:" + respuesta.getTextoError());
		}
		String code=respuesta.getValue("CADE_CADENA", 1, "STRING_CADE");

		if (!mapeoResultadosBD.containsKey(code))
		{
			throw new STPAException (idProceso+" La asociación de certificado y usuario en base de datos no ha terminado correctamente, código:" + code);
		}
		return code;
	}
	
}
