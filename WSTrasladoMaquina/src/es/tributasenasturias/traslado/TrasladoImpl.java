package es.tributasenasturias.traslado;

import javax.naming.ConfigurationException;

import es.tributasenasturias.lanzador.LanzadorException;
import es.tributasenasturias.lanzador.LanzadorFactory;
import es.tributasenasturias.lanzador.ParamType;
import es.tributasenasturias.lanzador.ProcedimientoAlmacenado;
import es.tributasenasturias.lanzador.TLanzador;
import es.tributasenasturias.lanzador.response.RespuestaLanzador;
import es.tributasenasturias.log.TributasLogger;
import es.tributasenasturias.traslado.documentos.GestorDocumento;
import es.tributasenasturias.traslado.exceptions.DocumentoException;
import es.tributasenasturias.traslado.exceptions.TrasladoException;
import es.tributasenasturias.traslado.mensajes.GestorMensajes;
import es.tributasenasturias.traslado.mensajes.Mensaje;
import es.tributasenasturias.traslado.preferencias.Preferencias;
import es.tributasenasturias.traslado.soap.SoapClientHandler;

/**
 * Implementación de la funcionalidad del traslado de máquinas, independientemente del SEI.
 * @author crubencvs
 *
 */
public class TrasladoImpl {

	private static enum ESTADOS_TRASLADO {
		TRASLADO_CORRECTO,
		TRASLADO_CON_DOCUMENTO,
		TRASLADO_SIN_DOCUMENTO,
		OTRO;
		
		public static ESTADOS_TRASLADO traducir(String resultadoBD) {
			if ("00".equals(resultadoBD))
			{
				return TRASLADO_CORRECTO;
			} else if ("01".equals(resultadoBD)) {
				return TRASLADO_CON_DOCUMENTO;
			} else if ("02".equals(resultadoBD)) {
				return TRASLADO_SIN_DOCUMENTO;
			} else {
				return OTRO;
			}
		}
		
	}
	private Preferencias pref;
	private String idSesion;
	private TributasLogger log;
	public static class ResultadoTraslado {
		private String codigo;
		private String mensaje;
		private byte[] pdf;
		public String getCodigo() {
			return codigo;
		}
		public String getMensaje() {
			return mensaje;
		}
		public byte[] getPdf() {
			return pdf;
		}
		
	}
	
	private static class ResultadoBD {
		private String codigo;
		private String mensaje;
		private String idReimprimible;
		private String tipoReimprimible;
		private String nifOperante;
		private String hashVerificacion;
		
		public ResultadoBD(String codigo, String mensaje, String idReimprimible, String tipoReimprimible, String nifOperante, String hashVerificacion)
		{
			this.codigo= codigo;
			this.mensaje= mensaje;
			this.idReimprimible= idReimprimible;
			this.tipoReimprimible= tipoReimprimible;
			this.nifOperante= nifOperante;
			this.hashVerificacion= hashVerificacion;
		}
		public String getCodigo() {
			return codigo;
		}
		public String getMensaje() {
			return mensaje;
		}
		public String getIdReimprimible() {
			return idReimprimible;
		}
		public String getTipoReimprimible() {
			return tipoReimprimible;
		}
		public String getNifOperante() {
			return nifOperante;
		}
		public String getHashVerificacion() {
			return hashVerificacion;
		}
		
		
	}
	
	public TrasladoImpl (TributasLogger log, Preferencias pref, String idSesion)
	{
		this.pref=pref;
		this.log=log;
		this.idSesion=idSesion;
	}
	/**
	 * Operación de traslado.
	 * @param cif Identificador fiscal de la empresa
	 * @param autoliquidacion Autoliquidación
	 * @param local Número de registro local
	 * @param maquina Número de registro de máquina
	 * @param log {@link TributasLogger}
	 * @param pref {@link Preferencias}
	 * @param idSesion
	 * @return {@link ResultadoTraslado}
	 */
	public ResultadoTraslado trasladar(String cif, String autoliquidacion,String local, String maquina
									   ) throws TrasladoException
	{
		ResultadoTraslado resultado = new ResultadoTraslado();
		try
		{
			Mensaje mn;
			GestorMensajes gm = new GestorMensajes(pref.getFicheroMensajes());
			this.log.info ("Se realiza el traslado en base de datos");
			ResultadoBD resBD= trasladoBD(cif, autoliquidacion, local, maquina);
			ESTADOS_TRASLADO estado = ESTADOS_TRASLADO.traducir(resBD.getCodigo());
			GestorDocumento gd;
			switch (estado) {
				case TRASLADO_CORRECTO:
				case TRASLADO_SIN_DOCUMENTO:
					if (estado.equals(ESTADOS_TRASLADO.TRASLADO_CORRECTO))
					{
						this.log.info ("Traslado correcto, se imprime el documento");
					}
					else
					{
						this.log.info ("Traslado ya realizado anteriormente sin documento impreso, se reimprime");
					}
					gd= new GestorDocumento(resBD.getIdReimprimible(), resBD.tipoReimprimible,autoliquidacion, resBD.getHashVerificacion(),pref, idSesion);
					try {
						String documento= gd.reimprimirDocumento();
						log.info("Impresión correcta, se guarda el documento");
						gd.guardarDocumento(documento,cif, resBD.getNifOperante());
						log.info("Documento guardado");
						if (estado.equals(ESTADOS_TRASLADO.TRASLADO_CORRECTO))
						{
							mn = gm.terminacionOK();
						}
						else 
						{
							mn= gm.terminacionYaTrasladado();
						}
						resultado.codigo=mn.getCodigo();
						resultado.mensaje=mn.getDescripcion();
						resultado.pdf= gd.recuperarBinario(documento);
					}
					catch (DocumentoException e)
					{
						log.info("Error al almacenar el documento");
						mn= gm.getErrorAplicacion("error.impresion");
						resultado.codigo=mn.getCodigo();
						resultado.mensaje=mn.getDescripcion();
					}
					break;
				case TRASLADO_CON_DOCUMENTO:
					this.log.info ("Traslado ya realizado y documento ya guardado. Se recupera el documento");
					gd= new GestorDocumento(resBD.getIdReimprimible(), resBD.tipoReimprimible,autoliquidacion, resBD.getHashVerificacion(),pref, idSesion);
					try {
						String documento= gd.recuperarDocumento();
						log.info("Documento recuperado");
						mn = gm.terminacionYaTrasladado();
						resultado.codigo=mn.getCodigo();
						resultado.mensaje=mn.getDescripcion();
						resultado.pdf= gd.recuperarBinario(documento);
					}
					catch (DocumentoException e)
					{
						log.info("Error al recuperar el documento");
						mn= gm.getErrorAplicacion("error.impresion");
						resultado.codigo=mn.getCodigo();
						resultado.mensaje=mn.getDescripcion();
					}
					break;
				default:
					log.info ("Error controlado en respuesta de base de datos:" + resBD.getCodigo()+ "===="+resBD.getMensaje());
					mn=gm.getErrorBD(resBD.getCodigo(), resBD.getMensaje());
					resultado.codigo=mn.getCodigo();
					resultado.mensaje=mn.getDescripcion();
			}
			return resultado;
		} catch (ConfigurationException c)
		{
			throw new TrasladoException ("Error al configurar los mensajes de servicio:"+ c.getMessage(),c);
		} 
		
	}
	/**
	 * Realiza el traslado en base de datos. Devuelve el código de resultado y su mensaje asociado, si hubiera
	 * Debería estar en otra clase de acceso a base de datos, pero en un servicio tan pequeño
	 * no merece la pena
	 * @param cif NIF de la empresa operadora
	 * @param autoliquidacion Número de autoliquidación
	 * @param local Número de registro del local a donde se envía la máquina
	 * @param maquina Número de registro de la máquina
	 * @return Código de resultado de la operación recibido de base de datos
	 * @throws TrasladoException En caso de error de comunicación o de ejecución en base de datos
	 */
	private ResultadoBD trasladoBD(String cif, String autoliquidacion,String local, String maquina) throws TrasladoException
	{
		try{
			TLanzador lanzador = LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(idSesion));
			ProcedimientoAlmacenado p = new ProcedimientoAlmacenado(pref.getProcAlmacenadoEmisionTraslado(), pref.getEsquemaBD());
			p.param("1", ParamType.NUMERO); //Conexión
			p.param("1", ParamType.NUMERO); //Petición
			p.param("USU_WEB_SAC", ParamType.CADENA); //Usuario
			p.param("33", ParamType.NUMERO); //Organismo
			p.param(autoliquidacion, ParamType.CADENA);
			p.param(local, ParamType.CADENA);
			p.param(maquina, ParamType.CADENA);
			p.param(cif, ParamType.CADENA);
			p.param("P", ParamType.CADENA); //Conexión oracle
			RespuestaLanzador res= new RespuestaLanzador(lanzador.ejecutar(p));
			if (res.esErronea())
			{
				throw new TrasladoException ("Error al ejecutar el traslado en base de datos:" + res.getTextoError());
			}
			String codigo=res.getValue("CANU_CADENAS_NUMEROS", 1, "STRING1_CANU");
			String mensaje= res.getValue("CANU_CADENAS_NUMEROS", 1, "STRING2_CANU");
			String idReimprimible= res.getValue("TMP_CADENAS_NUMEROS",1,"N1");
			String tipoReimprimible= res.getValue("TMP_CADENAS_NUMEROS",1,"C1");
			String nifOperante= res.getValue("CANU_CADENAS_NUMEROS", 1, "STRING3_CANU");
			String codigoVerificacion= res.getValue("CANU_CADENAS_NUMEROS", 1, "STRING4_CANU");
			return new ResultadoBD(codigo, mensaje, idReimprimible, tipoReimprimible, nifOperante, codigoVerificacion);
		}catch (LanzadorException le)
		{
			throw new TrasladoException ("Error al realizar el traslado en base de datos:" + le.getMessage(), le); 
		}
	}
}
