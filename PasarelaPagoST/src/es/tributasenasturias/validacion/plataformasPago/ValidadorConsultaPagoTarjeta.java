package es.tributasenasturias.validacion.plataformasPago;





import es.tributasenasturias.bd.BDFactory;
import es.tributasenasturias.bd.GestorLlamadasBD;
import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.validacion.CodigosResultadoValidacion;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.IContextReader;

public final class ValidadorConsultaPagoTarjeta implements IContextReader{
	
	private boolean valido;
	private String codigoError;
	private String mensajeError;
	private Preferencias pref;	
	private Logger logger;
	private CallContext context;
	@SuppressWarnings("unused")
	private ValidadorConsultaPagoTarjeta() {}
	
	public ValidadorConsultaPagoTarjeta (CallContext context) throws PasarelaPagoException
	{
		this.context=context;
		if (context==null)
		{
			throw new PasarelaPagoException ("No se puede crear el objeto " + ValidadorConsultaPagoTarjeta.class.getName() + " faltan datos de contexto.");
		}
		logger = (Logger) context.get(CallContextConstants.LOG_APLICACION);
		pref = (Preferencias) context.get(CallContextConstants.PREFERENCIAS);
		if (logger==null || pref==null)
		{
			throw new PasarelaPagoException ("No se puede crear el objeto " + ValidadorConsultaPagoTarjeta.class.getName()+" porque faltan datos en el contexto de llamada.");
		}
		
	}
	private boolean isEmpty(String valor){
		return (valor==null || "".equals(valor));
	}
	/**
	 * Valida los datos de entrada
	 * @param origen
	 * @param modalidad
	 * @param emisora
	 * @param identificacion
	 * @param referencia
	 * @param justificante
	 * @param aplicacion
	 * @param numeroUnico
	 * @param mac
	 */
	public void validar(String origen,
						String modalidad,
						String emisora,
						String identificacion,
						String referencia,
						String justificante,
						String aplicacion,
						String numeroUnico,
						String mac){

		this.setValido(true);
		if (isEmpty(origen)){
			this.setCodigoError(CodigosResultadoValidacion.ORIGEN_VACIO);
			this.setValido(false);
		}
		
		if (Constantes.getOrigenServicioWeb().equals(origen)){
			if (isEmpty(mac)){
				this.setCodigoError(CodigosResultadoValidacion.MAC_VACIA);
				this.setValido(false);
			}
			
			if (this.isValido() && pref.getValidaMac().equals(Constantes.getSI()) &&
								   !validarMac(origen, 
					   						   emisora,
					   						   justificante,
					   						   aplicacion,
					   						   numeroUnico,
					   						   mac)){
				this.setCodigoError(CodigosResultadoValidacion.MAC_INVALIDO);
				this.setValido(false);
			}
		}
		if (this.isValido() && "".equals(modalidad)){
			this.setCodigoError(CodigosResultadoValidacion.MODALIDAD_VACIA);
			this.setValido(false);
		}
		
		if (this.isValido() && "".equals(emisora)){
			this.setCodigoError(CodigosResultadoValidacion.EMISORA_VACIA);
			this.setValido(false);
		}
		
		if (this.isValido() && !Constantes.getOrigenServicioWeb().equals(origen)
				         && !Constantes.getOrigenPortal().equals(origen)
				         && !Constantes.getOrigenS1().equals(origen)){
			this.setCodigoError(CodigosResultadoValidacion.ORIGEN_INVALIDO);
			this.setValido(false);
		}
		
		if (this.isValido() && !Constantes.getModalidadAutoliquidacion().equals(modalidad)
		         && !Constantes.getModalidadLiquidacion().equals(modalidad)){
			this.setCodigoError(CodigosResultadoValidacion.MODALIDAD_ERRONEA);
			this.setValido(false);
		}
		
		
		
		//Validaciones de portal
		if (this.isValido() && Constantes.getOrigenPortal().equals(origen)){
			if(Constantes.getModalidadAutoliquidacion().equals(modalidad) 
			   && "".equals(justificante))
			{
				this.setCodigoError(CodigosResultadoValidacion.JUSTIFICANTE_VACIO);
				this.setValido(false);
			}
			
			if (this.isValido() 
				&& Constantes.getModalidadLiquidacion().equals(modalidad)
				&& ("".equals(identificacion) || "".equals(referencia))){
				this.setCodigoError(CodigosResultadoValidacion.IDENTIF_REFER_VACIO);
				this.setValido(false);
			}
			
			if (this.isValido() 
					&& Constantes.getModalidadLiquidacion().equals(modalidad)
					&& !"".equals(justificante)){
					this.setCodigoError(CodigosResultadoValidacion.JUSTIFICANTE_INNECESARIO);
					this.setValido(false);
			}
			
		}
		//Servicio web
		if (this.isValido() && Constantes.getOrigenServicioWeb().equals(origen)){
			if(!Constantes.getModalidadAutoliquidacion().equals(modalidad))
			{
				this.setCodigoError(CodigosResultadoValidacion.MODALIDAD_AUTOLIQ_SW);
				this.setValido(false);
			}
			
		}
	}
	
	/**
	 * Validación de la MAC recibida
	 * @param peticion
	 * @return
	 */
	private boolean validarMac(String origen,
							   String emisora,
							   String justificante,
							   String aplicacion,
							   String numeroUnico,
							   String mac) {
		GestorLlamadasBD gbd= BDFactory.newGestorLlamadasBD(context);
		try {
			return gbd.validarMac(origen, 
								  emisora+
								  justificante+
								  aplicacion+
								  numeroUnico, 
								  mac);
		} catch (PasarelaPagoException ppe){
			return false;
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
