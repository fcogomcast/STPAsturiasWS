package es.tributasenasturias.servicios.accesocertificado;

import java.util.ArrayList;
import java.util.List;

import es.tributasenasturias.seguridad.servicio.DatosUsuarioTributas;
import es.tributasenasturias.seguridad.servicio.InfoCertificado;
import es.tributasenasturias.seguridad.servicio.InfoPermisosCertificado;
import es.tributasenasturias.seguridad.servicio.PropertyConfigurator;
import es.tributasenasturias.seguridad.servicio.SeguridadException;
import es.tributasenasturias.seguridad.servicio.SeguridadFactory;
import es.tributasenasturias.seguridad.servicio.VerificadorCertificado;
import es.tributasenasturias.seguridad.servicio.VerificadorPermisoServicio;
import es.tributasenasturias.seguridad.servicio.InfoCertificado.Validez;
import es.tributasenasturias.services.lanzador.client.LanzadorException;
import es.tributasenasturias.services.lanzador.client.LanzadorFactory;
import es.tributasenasturias.services.lanzador.client.ParamType;
import es.tributasenasturias.services.lanzador.client.ProcedimientoAlmacenado;
import es.tributasenasturias.services.lanzador.client.TLanzador;
import es.tributasenasturias.services.lanzador.client.response.EstructuraLanzador;
import es.tributasenasturias.services.lanzador.client.response.RespuestaLanzador;
import es.tributasenasturias.servicios.accesocertificado.contextoLlamadas.CallContext;
import es.tributasenasturias.servicios.accesocertificado.contextoLlamadas.CallContextConstants;
import es.tributasenasturias.servicios.accesocertificado.contextoLlamadas.IContextReader;
import es.tributasenasturias.servicios.accesocertificado.exceptions.STPAException;
import es.tributasenasturias.servicios.accesocertificado.preferencias.Preferencias;
import es.tributasenasturias.servicios.accesocertificado.preferencias.PreferenciasFactory;
import es.tributasenasturias.servicios.accesocertificado.soap.SoapClientHandler;
import es.tributasenasturias.utils.log.Logger;

public class ProcesadorRecuperacionUsuario implements IContextReader{

	private final String idProceso = "[RecuperacionUsuario]"; 
	CallContext context;
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
	 * Constructor, no debería utilizarse.
	 * @throws STPAException
	 */
	protected ProcesadorRecuperacionUsuario() throws STPAException{
		throw new STPAException("No se puede instanciar directamente el objeto "+ProcesadorRecuperacionUsuario.class.getName());
	}
	/**
	 * Constructor, se pasa el contexto de llamada.
	 * @param ctx Contexto de llamada.
	 * @throws STPAException Si se produce un error.
	 */
	protected ProcesadorRecuperacionUsuario(CallContext ctx) throws STPAException
	{
		context = ctx;
		prefs = PreferenciasFactory.getPreferenciasContexto(context);
		log = (Logger)ctx.get(CallContextConstants.LOG);
	}
	
	/**
	 * Procesa la petición de recuperación de usuario.
	 * @param request Datos de entrada de la petición de recuperación de usuario.
	 * @return Datos de usuario de tributas.
	 * @throws STPAException Si se produce un error en proceso.
	 */
	public RecuperarUsuarioTributasResponse recuperarUsuarioTributas (RecuperarUsuarioTributasRequest request) throws STPAException
	{
		if ("N".equalsIgnoreCase(request.getCertificadoConfirmado()))
		{
			return recuperarUsuariosNoConfirmados(request);
			
		}
		else
		{
			return recuperarUsuariosConfirmados(request);
		}
	}
	/**
	 * Implementación cuando debe recuperar información de usuarios con solicitud confirmada (de ayuntamientos y externos)
	 * @param request
	 * @return
	 * @throws STPAException
	 */
	public RecuperarUsuarioTributasResponse recuperarUsuariosConfirmados (RecuperarUsuarioTributasRequest request) throws STPAException
	{
		RecuperarUsuarioTributasResponse response=new RecuperarUsuarioTributasResponse();
		//1. Comprobar la validez del certificado.
		log.info(idProceso+"0.Comienzo de proceso de recuperación de usuarios confirmados");
		if (request.getCertificado()==null || "".equals(request.getCertificado()))
		{
			throw new STPAException (idProceso+"El certificado está vacío.");
		}
		log.info(idProceso+"1.Se comprueba la validez y permiso del certificado");
		try
		{
			InfoPermisosCertificado info = SeguridadFactory.newInfoPermisosCertificado();
			String idLlamada = (String) context.get(CallContextConstants.IDSESION);
			VerificadorPermisoServicio perm = SeguridadFactory.newVerificadorPermisoServicio(new PropertyConfigurator(prefs.getEndpointAutenticacion(),prefs.getEndpointLanzador(),prefs.getProcAlmacenadoPermisosServicio(),prefs.getEsquemaBD(),new SoapClientHandler(idLlamada,prefs)));
			info = perm.tienePermisosCertificado(request.getCertificado(), prefs.getAliasServicio());
			if (info.isCertificadoValido())
			{
				log.info(idProceso+"1.1.Comprobar si el certificado tiene permisos");
				if (!info.isCertificadoAutorizado())
				{
					throw new STPAException (idProceso+"1.1.1. El certificado recibido no está autorizado.");
				}
				log.info(idProceso+"1.2. El certificado está autorizado");
				log.info(idProceso+"1.3. Rellenamos estructuras de salida");
				log.debug(idProceso+"1.3.1. Datos de los usuarios de Tributas asociados al identificador");
				DatosTributasType datTributas = new DatosTributasType();
				
				//Rellenamos la estructura.
				for (DatosUsuarioTributas usu: info.getListaUsuarios())
				{
					DatosUsuarioTributasType datSalida = new DatosUsuarioTributasType();
					datSalida.setUsuarioTributas(usu.getUsuarioTributas());
					datSalida.setPasswordTributas(usu.getPasswordTributas());
					datSalida.setIdFiscal(usu.getIdFiscal());
					datSalida.setIdSuborganismo(usu.getIdSuborganismo());
					datSalida.setSuborganismo(usu.getSuborganismo());
					datTributas.getDatosUsuario().add(datSalida);
				}
				response.setDatosTributas(datTributas);
				log.debug(idProceso+"1.3.2. Información del certificado: CIF-NIF, Nombre...");
				//Informacion del certificado
				String id="";
				InfoCertificado infoCertificado= info.getInfoCertificado();
				if (infoCertificado.getCif()!=null && !"".equals(infoCertificado.getCif())){
					id = infoCertificado.getCif();
				}
				response.setCif(id);
				response.setRazonSocial(infoCertificado.getRazonSocial());
				id="";
				if (infoCertificado.getNifNie()!=null && !"".equals(infoCertificado.getNifNie())){
					id = infoCertificado.getNifNie();
				}
				response.setNifNie(id);
				response.setName(infoCertificado.getNombre());
				response.setSurname1(infoCertificado.getApellido1());
				response.setSurname2(infoCertificado.getApellido2());
				response.setIdThirdParty("");
			}
			else
			{
				throw new STPAException (idProceso+"El certificado recibido no es válido.");
			}
		}
		catch (SeguridadException e)
		{
			throw new STPAException (idProceso+"Error al verificar los permisos del certificado:"+ e.getMessage(),e);
		}
		log.info(idProceso+"2.Se termina el proceso de recuperación de usuario.");
		return response;
	}
	/**
	 * Implementación para recuperar usuarios no confirmados, solo se comprueba la identidad del certificado
	 * @param request
	 * @return
	 * @throws STPAException
	 */
	public RecuperarUsuarioTributasResponse recuperarUsuariosNoConfirmados (RecuperarUsuarioTributasRequest request) throws STPAException
	{
		RecuperarUsuarioTributasResponse response=new RecuperarUsuarioTributasResponse();
		//1. Comprobar la validez del certificado.
		log.info(idProceso+"0.Comienzo de proceso de recuperación de usuarios no confirmados.");
		if (request.getCertificado()==null || "".equals(request.getCertificado()))
		{
			throw new STPAException (idProceso+"El certificado está vacío.");
		}
		log.info(idProceso+"1.Se comprueba la validez del certificado");
		try
		{
			String idLlamada = (String) context.get(CallContextConstants.IDSESION);
			SoapClientHandler soapClient= new SoapClientHandler(idLlamada,prefs);
			InfoCertificado info = SeguridadFactory.newInfoCertificado();
			VerificadorCertificado ver = SeguridadFactory.newVerificadorCertificado(prefs.getEndpointAutenticacion(), soapClient);
			info = ver.login(request.getCertificado());
			if (Validez.VALIDO.equals(info.getValidez()))
			{
				log.info(idProceso+"1.1.El certificado es válido");
				log.info(idProceso+"1.2. Consultamos lista de usuarios para ese NIF");
				String nif="";
				if (info.getNifNie()!=null && !"".equals(info.getNifNie()))
				{
					nif= info.getNifNie();
				}
				else
				{
					throw new STPAException (idProceso+" No se ha podido recuperar un NIF del certificado proporcionado.", "Error al verificar la identidad del certificado.");
				}
				List<DatosUsuarioTributas> usuarios= consultarUsuariosPorIdentidad(nif, soapClient);
				log.debug(idProceso+"1.3. Rellenamos las estructuras");
				DatosTributasType datTributas = new DatosTributasType();
				
				//Rellenamos la estructura.
				for (DatosUsuarioTributas usu: usuarios)
				{
					DatosUsuarioTributasType datSalida = new DatosUsuarioTributasType();
					datSalida.setUsuarioTributas(usu.getUsuarioTributas());
					datSalida.setPasswordTributas(usu.getPasswordTributas());
					datSalida.setIdFiscal(usu.getIdFiscal());
					datSalida.setIdSuborganismo(usu.getIdSuborganismo());
					datSalida.setSuborganismo(usu.getSuborganismo());
					datTributas.getDatosUsuario().add(datSalida);
				}
				response.setDatosTributas(datTributas);
				log.debug(idProceso+"1.3.2. Información del certificado: CIF-NIF, Nombre...");
				//Informacion del certificado
				String cif="";
				if (info.getCif()!=null && !"".equals(info.getCif())){
					cif = info.getCif();
				}
				response.setCif(cif);
				response.setRazonSocial(info.getRazonSocial());
				response.setNifNie(nif);
				response.setName(info.getNombre());
				response.setSurname1(info.getApellido1());
				response.setSurname2(info.getApellido2());
				response.setIdThirdParty("");
			}
			else
			{
				throw new STPAException (idProceso+"El certificado recibido no es válido.", "No se ha podido validar el certificado.");
			}
		}
		catch (SeguridadException e)
		{
			throw new STPAException (idProceso+"Error al verificar los permisos del certificado:"+ e.getMessage(),e, "Error al verificar permisos del certificado.");
		}
		catch (LanzadorException e)
		{
			throw new STPAException (idProceso+"Error al realizar la llamada a base de datos para recuperar usuarios asociados:" + e.getMessage(),e, "Error al comprobar usuarios asociados al certificado.");
		}
		log.info(idProceso+"2.Se termina el proceso de recuperación de usuario.");
		return response;
	}
	
	private List<DatosUsuarioTributas> consultarUsuariosPorIdentidad (String nif, SoapClientHandler soapHandler) throws LanzadorException, STPAException
	{
		List<DatosUsuarioTributas> usuarios=new ArrayList<DatosUsuarioTributas>();
		TLanzador lanz = LanzadorFactory.newTLanzador(prefs.getEndpointLanzador(), soapHandler);
		ProcedimientoAlmacenado proc  = new ProcedimientoAlmacenado(prefs.getProcAlmacenadoUsuarioPorIdentidad(),prefs.getEsquemaBD());
		proc.param(nif, ParamType.CADENA);
		proc.param("P", ParamType.CADENA);
		String res=lanz.ejecutar(proc);
		RespuestaLanzador respuesta = new RespuestaLanzador(res);
		if (respuesta.esErronea())
		{
			throw new STPAException (idProceso+"Error al realizar la asociación de certificado y usuario en Base de Datos:" + respuesta.getTextoError());
		}
		EstructuraLanzador canu=respuesta.getEstructura("CANU_CADENAS_NUMEROS");
		int n=respuesta.getNumFilasEstructura("CANU_CADENAS_NUMEROS");
		DatosUsuarioTributas dutt=null;
		for (int i=1;i<=n;i++)
		{
			dutt=new DatosUsuarioTributas();
			dutt.setIdFiscal(canu.getFila(i).getCampo("STRING1_CANU"));
			dutt.setUsuarioTributas(canu.getFila(i).getCampo("STRING2_CANU"));
			dutt.setSuborganismo(canu.getFila(i).getCampo("STRING4_CANU"));
			dutt.setIdSuborganismo(canu.getFila(i).getCampo("NUME1_CANU"));
			usuarios.add(dutt);
		}
		
		return usuarios;
	}
}
