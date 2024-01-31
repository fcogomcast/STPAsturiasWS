package es.tributasenasturias.validacion.plataformasPago;



import java.text.ParseException;
import java.text.SimpleDateFormat;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.utils.Varios;
import es.tributasenasturias.validacion.CodigosResultadoValidacion;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.IContextReader;
import es.tributasenasturias.webservices.types.InicioPagoTarjetaRequest;

public final class ValidadorInicioPagoTarjeta implements IContextReader{
	
	private boolean valido;
	private String codigoError;
	private String mensajeError;
	private Preferencias pref;	
	private Logger logger;	
	private CallContext context;
	@SuppressWarnings("unused")
	private ValidadorInicioPagoTarjeta() {}
	
	public ValidadorInicioPagoTarjeta (CallContext context) throws PasarelaPagoException
	{
		this.context=context;
		if (context==null)
		{
			throw new PasarelaPagoException ("No se puede crear el objeto " + ValidadorInicioPagoTarjeta.class.getName() + " faltan datos de contexto.");
		}
		logger = (Logger) context.get(CallContextConstants.LOG_APLICACION);
		pref = (Preferencias) context.get(CallContextConstants.PREFERENCIAS);
		if (logger==null || pref==null)
		{
			throw new PasarelaPagoException ("No se puede crear el objeto " + ValidadorInicioPagoTarjeta.class.getName()+" porque faltan datos en el contexto de llamada.");
		}
		
	}
	
	//CRUBENCVS 29/03/2023. Mantengo la entrada con parámetros CallContext, pero añado esta
	//para evitar pasar el contexto de llamada.
	public ValidadorInicioPagoTarjeta (Preferencias pref, Logger log) throws PasarelaPagoException
	{
		this.logger = log;
		this.pref = pref;
		if (logger==null || pref==null)
		{
			throw new PasarelaPagoException ("No se puede crear el objeto " + ValidadorInicioPagoTarjeta.class.getName()+" porque faltan datos en el contexto de llamada.");
		}
		
	}
	
	public void validar(InicioPagoTarjetaRequest peticion){

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
		
		if (this.isValido() && !Constantes.getModalidadAutoliquidacion().equals(peticion.getModalidad())
		         && !Constantes.getModalidadLiquidacion().equals(peticion.getModalidad())){
			this.setCodigoError(CodigosResultadoValidacion.MODALIDAD_ERRONEA);
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
			
			if (this.isValido() 
					&& Constantes.getModalidadLiquidacion().equals(peticion.getModalidad())
					&& (!"".equals(peticion.getDatoEspecifico()) || !"".equals(peticion.getFechaDevengo()))){
					this.setCodigoError(CodigosResultadoValidacion.FECHA_DEVENGO_ESPECIFICO_INNECESARIO);
					this.setValido(false);
			}
				
		}
		
		if (this.isValido() && Constantes.getOrigenServicioWeb().equals(peticion.getOrigen())){
			if(!Constantes.getModalidadAutoliquidacion().equals(peticion.getModalidad()))
			{
				this.setCodigoError(CodigosResultadoValidacion.MODALIDAD_AUTOLIQ_SW);
				this.setValido(false);
			}
			
			if (this.isValido() && !"046".equals(peticion.getModelo())){
				this.setCodigoError(CodigosResultadoValidacion.MODELO_NO_046);
				this.setValido(false);
			}
			
			//Dado que es modelo 046, necesita fecha de devengo y dato específico
			
			if (this.isValido() && ("".equals(peticion.getFechaDevengo()) || "".equals(peticion.getDatoEspecifico()))){
				this.setCodigoError(CodigosResultadoValidacion.FECHA_DEVENGO_ESPECIFICO_VACIO);
				this.setValido(false);
			}
			
		}
		//Validamos el formato, cuando llega
		if (this.isValido() && !"".equals(peticion.getFechaDevengo())){
			try {
				SimpleDateFormat sdf= new SimpleDateFormat("ddMMyyyy");
				sdf.parse(peticion.getFechaDevengo());
			} catch (ParseException pe){
				this.setCodigoError(CodigosResultadoValidacion.ERROR_FORMATO_FECHA_DEVENGO);
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
