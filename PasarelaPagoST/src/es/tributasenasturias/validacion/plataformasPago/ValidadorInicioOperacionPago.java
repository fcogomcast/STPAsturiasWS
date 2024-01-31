package es.tributasenasturias.validacion.plataformasPago;



import es.tributasenasturias.bd.BDFactory;
import es.tributasenasturias.bd.GestorLlamadasBD;
import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.utils.Varios;
import es.tributasenasturias.validacion.CodigosResultadoValidacion;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.IContextReader;
import es.tributasenasturias.webservices.types.InicioOperacionPagoRequest;

public final class ValidadorInicioOperacionPago implements IContextReader{
	
	private boolean valido;
	private String codigoError;
	private String mensajeError;
	private Preferencias pref;
	private Logger logger;
		
	private CallContext context;
	@SuppressWarnings("unused")
	private ValidadorInicioOperacionPago() {}
	
	public ValidadorInicioOperacionPago (CallContext context) throws PasarelaPagoException
	{
		this.context=context;
		if (context==null)
		{
			throw new PasarelaPagoException ("No se puede crear el objeto " + ValidadorInicioOperacionPago.class.getName() + " faltan datos de contexto.");
		}
		logger = (Logger) context.get(CallContextConstants.LOG_APLICACION);
		pref = (Preferencias) context.get(CallContextConstants.PREFERENCIAS);
		if (logger==null || pref==null)
		{
			throw new PasarelaPagoException ("No se puede crear el objeto " + ValidadorInicioOperacionPago.class.getName()+" porque faltan datos en el contexto de llamada.");
		}
		
	}
	/**
	 * Validación de la MAC recibida
	 * @param peticion
	 * @return
	 */
	private boolean validarMac(InicioOperacionPagoRequest peticion) {
		GestorLlamadasBD gbd= BDFactory.newGestorLlamadasBD(context);
		try {
			//En peticion, los campos tienen vacío si son nulos, así que 
			//podemos montar la mac sin temor a que nos devuelva la cadena "null"
			//uno de ellos
			return gbd.validarMac(peticion.getOrigen(), 
								  peticion.getEmisora()+
								  peticion.getModelo()+
								  peticion.getNifContribuyente()+
								  peticion.getFechaDevengo()+
								  peticion.getDatoEspecifico()+
								  peticion.getImporte()+
								  peticion.getNifOperante()+
								  peticion.getAplicacion()+
								  peticion.getNumeroUnico(), 
								  peticion.getMac());
		} catch (PasarelaPagoException ppe){
			return false;
		}
	}
	/**
	 * Validación de entrada
	 * @param peticion
	 */
	public void validar(InicioOperacionPagoRequest peticion){

		this.setValido(true);
		
		if ("".equals(peticion.getMac())){
			this.setCodigoError(CodigosResultadoValidacion.MAC_VACIA);
			this.setValido(false);
		}
		
		if (this.isValido() && pref.getValidaMac().equals(Constantes.getSI()) && !validarMac(peticion)){
			this.setCodigoError(CodigosResultadoValidacion.MAC_INVALIDO);
			this.setValido(false);
		}
		
		if (this.isValido() && "".equals(peticion.getOrigen())){
			this.setCodigoError(CodigosResultadoValidacion.ORIGEN_VACIO);
			this.setValido(false);
		}
		
		if (this.isValido() && "".equals(peticion.getModalidad())){
			this.setCodigoError(CodigosResultadoValidacion.MODALIDAD_VACIA);
			this.setValido(false);
		}
		
		if (this.isValido() && "".equals(peticion.getEmisora())){
			this.setCodigoError(CodigosResultadoValidacion.EMISORA_VACIA);
			this.setValido(false);
		}
		
		if (this.isValido() && "".equals(peticion.getNifOperante())){
			this.setCodigoError(CodigosResultadoValidacion.NIF_OPERANTE_VACIO);
			this.setValido(false);
		}
		
		if (this.isValido() && "".equals(peticion.getNifContribuyente())){
			this.setCodigoError(CodigosResultadoValidacion.NIF_INVALIDO);
			this.setValido(false);
		}
		
		if (this.isValido() && !Constantes.getOrigenServicioWeb().equals(peticion.getOrigen())
				         && !Constantes.getOrigenPortal().equals(peticion.getOrigen())
				         && !Constantes.getOrigenS1().equals(peticion.getOrigen())){
			this.setCodigoError(CodigosResultadoValidacion.ORIGEN_INVALIDO);
			this.setValido(false);
		}
		
		if (this.isValido() && !Constantes.getModalidadAutoliquidacion().equals(peticion.getModalidad())){
			this.setCodigoError(CodigosResultadoValidacion.MODALIDAD_AUTOLIQ_SW);
			this.setValido(false);
		}
		
		if (this.isValido() && "".equals(peticion.getImporte())){
			this.setCodigoError(CodigosResultadoValidacion.IMPORTE_ERRONEO);
			this.setValido(false);
		}
		if (this.isValido() && !"".equals(peticion.getImporte())){
			try {
				if (Integer.parseInt(peticion.getImporte())==0){
					this.setCodigoError(CodigosResultadoValidacion.IMPORTE_ERRONEO);
					this.setValido(false);
				}
			} catch (NumberFormatException ex){
				this.setCodigoError(CodigosResultadoValidacion.IMPORTE_ERRONEO);
				this.setValido(false);
			}
		}
		
		if (this.isValido()){
			if (!Varios.esNif(peticion.getNifContribuyente())
			    && !Varios.esNie(peticion.getNifContribuyente())
			    && !Varios.esCif(peticion.getNifContribuyente())){
				this.setCodigoError(CodigosResultadoValidacion.NIF_INVALIDO);
				this.setValido(false);
			}
			if (!Varios.esNif(peticion.getNifOperante())
		        && !Varios.esCif(peticion.getNifOperante())
		        && !Varios.esNie(peticion.getNifOperante())){
				this.setCodigoError(CodigosResultadoValidacion.NIF_INVALIDO);
				this.setValido(false);
			}
			
		}
		
		if (this.isValido() && !"046".equals(peticion.getModelo())){
			this.setCodigoError(CodigosResultadoValidacion.MODELO_NO_046);
			this.setValido(false);
		}
		
		if (this.isValido() && ("".equals(peticion.getAplicacion()) || "".equals(peticion.getNumeroUnico()))){
			this.setCodigoError(CodigosResultadoValidacion.APLICACION_NUMERO_UNICO_VACIO);
			this.setValido(false);
		}
		
		//Dado que es modelo 046, necesita fecha de devengo y dato específico
		
		if (this.isValido() && ("".equals(peticion.getFechaDevengo()) || "".equals(peticion.getDatoEspecifico()))){
			this.setCodigoError(CodigosResultadoValidacion.FECHA_DEVENGO_ESPECIFICO_VACIO);
			this.setValido(false);
		}
		
	}
	@Override
	public CallContext getCallContext() {
		return context;
	}

	
	@Override
	public void setCallContext(CallContext ctx) {
		this.context= ctx;
	}

	public final boolean isValido() {
		return valido;
	}

	public final void setValido(boolean valido) {
		this.valido = valido;
	}

	public final String getCodigoError() {
		return codigoError;
	}

	public final void setCodigoError(String codigoError) {
		this.codigoError = codigoError;
	}

	public final String getMensajeError() {
		return mensajeError;
	}

	public final void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	
}
