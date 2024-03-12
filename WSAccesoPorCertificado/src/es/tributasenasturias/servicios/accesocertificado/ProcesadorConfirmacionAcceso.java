package es.tributasenasturias.servicios.accesocertificado;


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

public class ProcesadorConfirmacionAcceso implements IContextReader{
	CallContext context;
	private static final String idProceso="[ConfirmacionAcceso]";
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
	protected ProcesadorConfirmacionAcceso() throws STPAException{
		throw new STPAException("No se puede instanciar directamente el objeto "+ProcesadorConfirmacionAcceso.class.getName());
	}
	/**
	 * Constructor con contexto de llamada. Es el que debería utilizarse.
	 * @param context Contexto de la llamada.
	 * @throws STPAException
	 */
	protected ProcesadorConfirmacionAcceso(CallContext context) throws STPAException
	{
		this.context = context;
		prefs = PreferenciasFactory.getPreferenciasContexto(context);
		log = LogFactory.getLoggerFromContext(context);
	}
	/**
	 * Realiza el proceso de confirmación de acceso en Base de datos.
	 * @param request Datos de la petición de entrada al servicio de acceso.
	 * @return ConfirmacionAcce
	 * @throws STPAException En caso de que no se pueda realizar la confirmación.
	 */
	public ConfirmarAccesoPorCertificadoResponse process (ConfirmarAccesoPorCertificadoRequest request) throws STPAException
	{
		String idLlamada = (String) context.get(CallContextConstants.IDSESION);
		SoapClientHandler soapClient= new SoapClientHandler(idLlamada,prefs);
		ConfirmarAccesoPorCertificadoResponse response = new ConfirmarAccesoPorCertificadoResponse();
		log.info(idProceso+"0. Entrada al proceso de confirmación de acceso.");
		
		log.info (idProceso+"1. Se valida que el certificado sea correcto.");
		try {
			if (request.getCertificado()==null || "".equals(request.getCertificado()))
			{
				throw new STPAException (idProceso+"El certificado recibido está vacío.");
			}
			String certificado = request.getCertificado();
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
				if (info.getNifNie()!=null && !"".equals(info.getNifNie()))
				{
					id = info.getNifNie();
				}
				else
				{
					throw new STPAException (idProceso+"No se ha podido recuperar un NIF del certificado proporcionado.");
				}
				log.info  (idProceso+"2.Pasamos los datos a la base de datos");
				String code=confirmacionBD (request.getHash(),id, request.getDnCertificado(), soapClient);
				response.setResultado(code);
			}
			else
			{
				throw new STPAException (idProceso+"El certificado no es válido.");
			}
		} catch (SeguridadException e) {
			throw new STPAException (idProceso+"Error en el proceso de confirmación de acceso:" + e.getMessage(),e);
		} catch (LanzadorException e)
		{
			throw new STPAException (idProceso+"Error al realizar la llamada a base de datos en confirmación de acceso:" + e.getMessage(),e);
		}
		return response;
		
	}
	public String confirmacionBD (String hash, String nif, String dn, SoapClientHandler soapHandler) throws LanzadorException, STPAException
	{
		TLanzador lanz = LanzadorFactory.newTLanzador(prefs.getEndpointLanzador(), soapHandler);
		ProcedimientoAlmacenado proc  = new ProcedimientoAlmacenado(prefs.getProcAlmacenadoConfirmacionAcceso(),prefs.getEsquemaBD());
		proc.param(nif, ParamType.CADENA);
		proc.param(hash, ParamType.CADENA);
		proc.param(dn, ParamType.CADENA);
		String res=lanz.ejecutar(proc);
		RespuestaLanzador respuesta = new RespuestaLanzador(res);
		if (respuesta.esErronea())
		{
			throw new STPAException (idProceso+"Error al realizar la confirmación en Base de Datos:" + respuesta.getTextoError());
		}
		String code=respuesta.getValue("CADE_CADENA", 1, "STRING_CADE");

		if (!"00".equals(code) && !"01".equals(code) && !"03".equals(code))
		{
			throw new STPAException (idProceso+"La confirmación en base de datos no ha terminado correctamente, código:" + code);
		}
		return code;
	}
	
}
