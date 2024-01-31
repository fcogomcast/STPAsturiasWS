package es.tributasenasturias.tarjetas.inicioOperacionPago;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.webservices.soap.LogMessageHandlerClient;
import es.tributasenasturias.webservices.types.InicioOperacionPagoRequest;

/**
 * Objeto que guarda el contexto de la llamada al inicio de la operación de pago
 * @author crubencvs
 *
 */
public class InicioOperacionPagoContexto {
	private final InicioOperacionPagoRequest peticion;
	private final Preferencias pref;
	private final Logger log;
	private final LogMessageHandlerClient soapClientHandler;
	private final String idSesion;
	
	/**
	 * @param peticion
	 * @param pref
	 * @param log
	 * @param soapClientHandler
	 * @param idSesion
	 */
	private InicioOperacionPagoContexto(InicioOperacionPagoRequest peticion,
									   Preferencias pref, 
									   Logger log,
									   LogMessageHandlerClient soapClientHandler, 
									   String idSesion) {
		this.peticion = peticion;
		this.pref = pref;
		this.log = log;
		this.soapClientHandler = soapClientHandler;
		this.idSesion = idSesion;
	}
	/**
	 * Builder para construir el objeto
	 * @author crubencvs
	 *
	 */
	public static class InicioOperacionPagoBuilder{
		private InicioOperacionPagoRequest peticion;
		private Preferencias pref;
		private Logger log;
		private LogMessageHandlerClient soapClientHandler;
		private String idSesion;
		
		public InicioOperacionPagoBuilder setPeticion(InicioOperacionPagoRequest request){
			this.peticion= request;
			return this;
		}
		
		public InicioOperacionPagoBuilder setPreferencias(Preferencias preferencias){
			this.pref= preferencias;
			return this;
		}
		
		public InicioOperacionPagoBuilder setLog(Logger log){
			this.log= log;
			return this;
		}
		
		public InicioOperacionPagoBuilder setIdSesion(String idSesion){
			this.idSesion= idSesion;
			return this;
		}
		
		/**
		 * Construye y devuelve el objeto de contexto
		 * @return
		 * @throws PasarelaPagoException
		 */
		public InicioOperacionPagoContexto build() throws PasarelaPagoException{
			if (pref==null){
				throw new PasarelaPagoException(InicioOperacionPagoContexto.class.getName()+" :No se han indicado preferencias");
			}
			
			if (log==null){
				throw new PasarelaPagoException(InicioOperacionPagoContexto.class.getName()+" :No se ha indicado log");
			}
			
			if (idSesion==null){
				throw new PasarelaPagoException(InicioOperacionPagoContexto.class.getName()+" :No se ha indicado Id de sesión");
			}
			
			if (peticion==null){
				throw new PasarelaPagoException(InicioOperacionPagoContexto.class.getName()+" :No se han indicado parámetros del servicio");
			}
			this.soapClientHandler= new LogMessageHandlerClient(idSesion);
			return new InicioOperacionPagoContexto(this.peticion, this.pref, this.log, this.soapClientHandler,this.idSesion);
		}
	}
	/**
	 * @return the peticion
	 */
	public final InicioOperacionPagoRequest getPeticion() {
		return peticion;
	}
	/**
	 * @return the pref
	 */
	public final Preferencias getPref() {
		return pref;
	}
	/**
	 * @return the log
	 */
	public final Logger getLog() {
		return log;
	}
	/**
	 * @return the soapClientHandler
	 */
	public final LogMessageHandlerClient getSoapClientHandler() {
		return soapClientHandler;
	}
	/**
	 * @return the idSesion
	 */
	public final String getIdSesion() {
		return idSesion;
	}
	
	
}
