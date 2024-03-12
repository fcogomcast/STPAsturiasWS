package es.tributasenasturias.servicios.accesocertificado;

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
import es.tributasenasturias.servicios.accesocertificado.preferencias.Preferencias;
import es.tributasenasturias.servicios.accesocertificado.preferencias.PreferenciasFactory;
import es.tributasenasturias.servicios.accesocertificado.soap.SoapClientHandler;
import es.tributasenasturias.utils.log.Logger;

public class ProcesadorListadoCertificados implements IContextReader{
	
	private static final String COL_RESU = "STRING_CADE";
	private static final String TAB_RESU = "CADE_CADENA";
	private static final String COL_DN = "STRING_MEMO";
	private static final String TABLA_DN = "MEMO_MEMO";
	private final String idProceso="[ListadoCertificados]";
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
	 * Constructor, no se debería utilizar nunca.
	 * @throws STPAException
	 */
	protected ProcesadorListadoCertificados() throws STPAException{
		throw new STPAException("No se puede instanciar directamente el objeto "+ProcesadorListadoCertificados.class.getName());
	}
	/**
	 * Constructor con CallContext para pasar la información del contexto de llamada.
	 * @param ctx Contexto de llamada. CallContext
	 * @throws STPAException si se produce un error en construcción.
	 */
	protected ProcesadorListadoCertificados(CallContext ctx) throws STPAException{
		context = ctx;
		prefs = PreferenciasFactory.getPreferenciasContexto(context);
		log = (Logger)ctx.get(CallContextConstants.LOG);
	}
	
	ListarCertificadosConAccesoResponse process(
			ListarCertificadosConAccesoRequest request) throws STPAException
	{
		ListarCertificadosConAccesoResponse response = new ListarCertificadosConAccesoResponse();
		StringBuilder listaCertificados= new StringBuilder();
		log.info(idProceso+"0.Se entra al proceso de listado de Certificados");
		String idLlamada = (String) context.get(CallContextConstants.IDSESION);
		try {
			log.info(idProceso+"1.Consulta de los certificados en base de datos con fecha:" + request.getFecha());
			TLanzador lanz = LanzadorFactory.newTLanzador(prefs.getEndpointLanzador(), new SoapClientHandler(idLlamada,prefs));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado(prefs.getProcAlmacenadoListaCertificados(),prefs.getEsquemaBD());
			proc.param(request.getFecha(),ParamType.CADENA);
			String res=lanz.ejecutar(proc);
			RespuestaLanzador respuesta = new RespuestaLanzador(res);
			if (respuesta.esErronea())
			{
				throw new STPAException (idProceso + "No se ha podido recuperar el listado de certificados en base de datos:"+respuesta.getTextoError());
			}
			String code = respuesta.getValue(TAB_RESU, 1, COL_RESU);
			if ("304".equals(code))
			{
				log.info(idProceso+"No hay cambios desde la última fecha de consulta.");
				response.setResultado(code);
			}else if ("00".equals(code))
			{
				log.info(idProceso+"2.Se procesa la lista de certificados recibida.");
				int numFilas=respuesta.getNumFilasEstructura(TABLA_DN);
				String cert="";
				for (int i=1;i<=numFilas;i++)
				{
					cert = respuesta.getValue(TABLA_DN, i, COL_DN);
					listaCertificados.append(cert);
					listaCertificados.append("\n");
				}
				log.info (idProceso+"3.Se devuelve la lista de certificados.");
				response.setCertificados(listaCertificados.toString());
				response.setResultado("00");
			}
			else 
			{
				throw new STPAException (idProceso+"La consulta de certificados en base de datos ha devuelto un error:"+ code);
			}
				
			
		} catch (LanzadorException e) {
			throw new STPAException (idProceso+"Error en conexión a base de datos al listar los certificados con acceso:"+ e.getMessage(),e);
		}
		return response;
	}
	
}
