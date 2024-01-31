package es.tributasenasturias.tarjetas.inicioPagoTarjeta;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.tarjetas.core.PateRecord;
import es.tributasenasturias.tarjetas.core.PeticionPagoTarjetaParameters;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.webservices.soap.LogMessageHandlerClient;
import es.tributasenasturias.webservices.types.InicioPagoTarjetaResponse;

/**
 * Objeto que guarda información de contexto de la operación de inicio de pago con tarjeta 
 * @author crubencvs
 *
 */
public class InicioPagoTarjetaContexto {
	private PeticionPagoTarjetaParameters peticion;
	private InicioPagoTarjetaResponse respuesta;
	private PateRecord pateRecord;
	
	private Preferencias pref;
	private Logger log;
	private LogMessageHandlerClient soapClientHandler;
	private String idSesion;
	
	private String pasarelaUtilizada;
	
	/**
	 * Constructor. No se debe llamar desde el exterior, ya que al crear un objeto
	 * hay que validar la entrada.
	 * @param peticion
	 * @param pref
	 * @param log
	 * @param soapHandler
	 */
	private InicioPagoTarjetaContexto(PeticionPagoTarjetaParameters peticion, Preferencias pref, Logger log, String idSesion,LogMessageHandlerClient soapHandler){
		this.peticion= peticion;
		respuesta= new InicioPagoTarjetaResponse();
		this.pref= pref;
		this.log= log;
		this.idSesion= idSesion;
		this.soapClientHandler= soapHandler;
		
		if (this.getPeticion().getPlataformaPago()!= null
				&& !"".equals(this.getPeticion().getPlataformaPago().trim())){
				this.pasarelaUtilizada= this.getPeticion().getPlataformaPago();
		} else {
			//El valor por defecto de la preferencia
			this.pasarelaUtilizada= pref.getPlataformaPagoDefecto();
		}
		
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
		private PeticionPagoTarjetaParameters peticion;
		
		public ContextoInicioPagoTarjetaBuilder setPeticion(PeticionPagoTarjetaParameters peticion){
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
		
		public InicioPagoTarjetaContexto build() throws PasarelaPagoException{
			if (pref==null){
				throw new PasarelaPagoException(InicioPagoTarjetaContexto.class.getName()+" :No se han indicado preferencias");
			}
			
			if (log==null){
				throw new PasarelaPagoException(InicioPagoTarjetaContexto.class.getName()+" :No se ha indicado log");
			}
			
			if (idSesion==null){
				throw new PasarelaPagoException(InicioPagoTarjetaContexto.class.getName()+" :No se ha indicado Id de sesión");
			}
			
			if (peticion==null){
				throw new PasarelaPagoException(InicioPagoTarjetaContexto.class.getName()+" :No se han indicado parámetros del servicio");
			}
			this.soapClientHandler= new LogMessageHandlerClient(idSesion);
			
			return new InicioPagoTarjetaContexto(peticion, pref, log, idSesion,soapClientHandler);
			
		}
		
	}
	
	
	/**
	 * @return the peticion
	 */
	public final PeticionPagoTarjetaParameters getPeticion() {
		return peticion;
	}
	
	/**
	 * @return the respuesta
	 */
	public final InicioPagoTarjetaResponse getRespuesta() {
		return respuesta;
	}
	
	

	/**
	 * @return the pateRecord
	 */
	public final PateRecord getPateRecord() {
		return pateRecord;
	}

	/**
	 * @param pateRecord the pateRecord to set
	 */
	public final void setPateRecord(PateRecord pateRecord) {
		this.pateRecord = pateRecord;
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
