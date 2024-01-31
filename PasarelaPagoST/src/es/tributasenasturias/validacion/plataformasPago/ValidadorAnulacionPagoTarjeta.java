package es.tributasenasturias.validacion.plataformasPago;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.validacion.CodigosResultadoValidacion;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.IContextReader;
import es.tributasenasturias.webservices.types.AnulacionPagoTarjetaRequest;

public final class ValidadorAnulacionPagoTarjeta implements IContextReader{
	
	private boolean valido;
	private String codigoError;
	private String mensajeError;
	private Preferencias pref;	
	private Logger logger;
	private CallContext context;
	@SuppressWarnings("unused")
	private ValidadorAnulacionPagoTarjeta() {}
	
	public ValidadorAnulacionPagoTarjeta (CallContext context) throws PasarelaPagoException
	{
		this.context=context;
		if (context==null)
		{
			throw new PasarelaPagoException ("No se puede crear el objeto " + ValidadorAnulacionPagoTarjeta.class.getName() + " faltan datos de contexto.");
		}
		logger = (Logger) context.get(CallContextConstants.LOG_APLICACION);
		pref = (Preferencias) context.get(CallContextConstants.PREFERENCIAS);
		if (logger==null || pref==null)
		{
			throw new PasarelaPagoException ("No se puede crear el objeto " + ValidadorAnulacionPagoTarjeta.class.getName()+" porque faltan datos en el contexto de llamada.");
		}
		
	}
	
	public void validar(AnulacionPagoTarjetaRequest peticion){

		this.setValido(true);
		if ("".equals(peticion.getOrigen())){
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
		
		
		if (this.isValido() && !Constantes.getOrigenServicioWeb().equals(peticion.getOrigen())
				         && !Constantes.getOrigenPortal().equals(peticion.getOrigen())
				         && !Constantes.getOrigenS1().equals(peticion.getOrigen())){
			this.setCodigoError(CodigosResultadoValidacion.ORIGEN_INVALIDO);
			this.setValido(false);
		}
		
		if (this.isValido() && !Constantes.getModalidadAutoliquidacion().equals(peticion.getModalidad())
		         && !Constantes.getModalidadLiquidacion().equals(peticion.getModalidad())){
			this.setCodigoError(CodigosResultadoValidacion.MODALIDAD_ERRONEA);
			this.setValido(false);
		}
		
		if (this.isValido() && Constantes.getOrigenServicioWeb().equals(peticion.getOrigen())){
			if ("".equals(peticion.getAplicacion()) && "".equals(peticion.getNumeroUnico()) )
				this.setCodigoError(CodigosResultadoValidacion.APLICACION_NUMERO_UNICO_VACIO);
				this.setValido(false);
		}
		
		//Validaciones de portal
		if (this.isValido() && Constantes.getOrigenPortal().equals(peticion.getOrigen())){
			if(Constantes.getModalidadAutoliquidacion().equals(peticion.getModalidad()) 
			   && "".equals(peticion.getNumeroAutoliquidacion()))
			{
				this.setCodigoError(CodigosResultadoValidacion.JUSTIFICANTE_VACIO);
				this.setValido(false);
			}
			
			if (this.isValido() 
				&& Constantes.getModalidadLiquidacion().equals(peticion.getModalidad())
				&& ("".equals(peticion.getIdentificacion()) || "".equals(peticion.getReferencia()))){
				this.setCodigoError(CodigosResultadoValidacion.IDENTIF_REFER_VACIO);
				this.setValido(false);
			}
			
			if (this.isValido() 
					&& Constantes.getModalidadLiquidacion().equals(peticion.getModalidad())
					&& !"".equals(peticion.getNumeroAutoliquidacion())){
					this.setCodigoError(CodigosResultadoValidacion.JUSTIFICANTE_INNECESARIO);
					this.setValido(false);
			}
			
				
		}
		
		if (this.isValido() && Constantes.getOrigenServicioWeb().equals(peticion.getOrigen())){
			
			if(Constantes.getModalidadAutoliquidacion().equals(peticion.getModalidad()) 
					   && "".equals(peticion.getNumeroAutoliquidacion()))
			{
				this.setCodigoError(CodigosResultadoValidacion.MODALIDAD_AUTOLIQ_SW);
				this.setValido(false);
			}
			
			
			
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
