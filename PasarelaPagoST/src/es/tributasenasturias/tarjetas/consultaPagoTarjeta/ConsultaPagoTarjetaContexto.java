package es.tributasenasturias.tarjetas.consultaPagoTarjeta;

import es.tributasenasturias.dao.DatosPagoBD;
import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.webservices.soap.LogMessageHandlerClient;
import es.tributasenasturias.webservices.types.PeticionConsultaPagoTarjeta;
import es.tributasenasturias.webservices.types.ResultadoConsultaPagoTarjeta;

/**
 * Objeto que guarda información de contexto de la operación de consulta de pago con tarjeta 
 * @author crubencvs
 *
 */
public class ConsultaPagoTarjetaContexto {
	private PeticionConsultaPagoTarjeta peticion;
	private ResultadoConsultaPagoTarjeta respuesta;
	private DatosPagoBD registroPagoBD;
	
	private Preferencias pref;
	private Logger log;
	private LogMessageHandlerClient soapClientHandler;
	private String idSesion;
	
	//Pasarela utilizada para la operación. Es decir, si entramos por Unicaja,
	//se consultará con la lógica de Unicaja. Si se entra por UniversalPay,
	//la de Universal
	private String pasarelaUtilizada;
	
	/**
	 * Constructor. No se debe llamar desde el exterior, ya que al crear un objeto
	 * hay que validar la entrada.
	 * @param peticion
	 * @param pref
	 * @param log
	 * @param soapHandler
	 */
	private ConsultaPagoTarjetaContexto(PeticionConsultaPagoTarjeta peticion, Preferencias pref, Logger log, String idSesion,LogMessageHandlerClient soapHandler){
		this.peticion= peticion;
		respuesta= new ResultadoConsultaPagoTarjeta();
		this.pref= pref;
		this.log= log;
		this.idSesion= idSesion;
		this.soapClientHandler= soapHandler;
		
		//El valor por defecto de la preferencia
		this.pasarelaUtilizada= pref.getPlataformaPagoDefecto();
		
	}
	
	/**
	 * Builder del objeto de contexto de operación, los nuevos objetos se crearán
	 * así.
	 * Se asegurará de que el objeto creado es válido
	 * @author crubencvs
	 *
	 */
	public static class ContextoInicioPagoTarjetaBuilder{
		private Preferencias pref;
		private Logger log;
		private String idSesion;
		private LogMessageHandlerClient soapClientHandler;
		private PeticionConsultaPagoTarjeta peticion;
		
		public ContextoInicioPagoTarjetaBuilder setPeticion(PeticionConsultaPagoTarjeta peticion){
			this.peticion= peticion;
			return this;
		}
		
		public ContextoInicioPagoTarjetaBuilder setPreferencias(Preferencias pref){
			this.pref= pref;
			return this;
		}
		
		public ContextoInicioPagoTarjetaBuilder setLog(Logger log){
			this.log=log;
			return this;
		}
		
		public ContextoInicioPagoTarjetaBuilder setIdSesion(String idSesion){
			this.idSesion= idSesion;
			return this;
		}
		
		public ConsultaPagoTarjetaContexto build() throws PasarelaPagoException{
			if (pref==null){
				throw new PasarelaPagoException(ConsultaPagoTarjetaContexto.class.getName()+" :No se han indicado preferencias");
			}
			
			if (log==null){
				throw new PasarelaPagoException(ConsultaPagoTarjetaContexto.class.getName()+" :No se ha indicado log");
			}
			
			if (idSesion==null){
				throw new PasarelaPagoException(ConsultaPagoTarjetaContexto.class.getName()+" :No se ha indicado Id de sesión");
			}
			
			if (peticion==null){
				throw new PasarelaPagoException(ConsultaPagoTarjetaContexto.class.getName()+" :No se han indicado parámetros del servicio");
			}
			this.soapClientHandler= new LogMessageHandlerClient(idSesion);
			
			return new ConsultaPagoTarjetaContexto(peticion, pref, log, idSesion,soapClientHandler);
			
		}
		
	}
	
	
	/**
	 * @return the peticion
	 */
	public final PeticionConsultaPagoTarjeta getPeticion() {
		return peticion;
	}
	
	/**
	 * @return the respuesta
	 */
	public final ResultadoConsultaPagoTarjeta getRespuesta() {
		return respuesta;
	}
	
	/**
	 * @return the registroPagoBD
	 */
	public final DatosPagoBD getRegistroPagoBD() {
		return registroPagoBD;
	}
	/**
	 * @param registroPagoBD the registroPagoBD to set
	 */
	public final void setRegistroPagoBD(DatosPagoBD registroPagoBD) {
		this.registroPagoBD = registroPagoBD;
	}

	/**
	 * @return the pref
	 */
	public final Preferencias getPref() {
		return pref;
	}

	/**
	 * @param pref the pref to set
	 */
	public final void setPref(Preferencias pref) {
		this.pref = pref;
	}

	/**
	 * @return the log
	 */
	public final Logger getLog() {
		return log;
	}

	/**
	 * @param log the log to set
	 */
	public final void setLog(Logger log) {
		this.log = log;
	}

	/**
	 * @return the soapClientHandler
	 */
	public final LogMessageHandlerClient getSoapClientHandler() {
		return soapClientHandler;
	}


	/**
	 * @return the pasarelaUtilizada
	 */
	public final String getPasarelaUtilizada() {
		return pasarelaUtilizada;
	}

	/**
	 * @return the idSesion
	 */
	public final String getIdSesion() {
		return idSesion;
	}

	
	
	
	
}
