package es.tributasenasturias.webservices.types;

public class PeticionConsultaPagoTarjeta {
	
	 private String origen;
	 private String modalidad;
	 private String referencia;
	 private String identificacion;
	 private String numero_autoliquidacion;
	 private String emisora;
	 private String aplicacion;
	 private String numero_unico; 
	 private String envio_justificante;
	 private String mac;
	/**
	 * @return the origen
	 */
	public final String getOrigen() {
		return origen;
	}
	/**
	 * @param origen the origen to set
	 */
	public final void setOrigen(String origen) {
		this.origen = origen;
	}
	/**
	 * @return the modalidad
	 */
	public final String getModalidad() {
		return modalidad;
	}
	/**
	 * @param modalidad the modalidad to set
	 */
	public final void setModalidad(String modalidad) {
		this.modalidad = modalidad;
	}
	/**
	 * @return the referencia
	 */
	public final String getReferencia() {
		return referencia;
	}
	/**
	 * @param referencia the referencia to set
	 */
	public final void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	/**
	 * @return the identificacion
	 */
	public final String getIdentificacion() {
		return identificacion;
	}
	/**
	 * @param identificacion the identificacion to set
	 */
	public final void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}
	/**
	 * @return the numero_autoliquidacion
	 */
	public final String getNumero_autoliquidacion() {
		return numero_autoliquidacion;
	}
	/**
	 * @param numero_autoliquidacion the numero_autoliquidacion to set
	 */
	public final void setNumero_autoliquidacion(String numero_autoliquidacion) {
		this.numero_autoliquidacion = numero_autoliquidacion;
	}
	/**
	 * @return the emisora
	 */
	public final String getEmisora() {
		return emisora;
	}
	/**
	 * @param emisora the emisora to set
	 */
	public final void setEmisora(String emisora) {
		this.emisora = emisora;
	}
	/**
	 * @return the aplicacion
	 */
	public final String getAplicacion() {
		return aplicacion;
	}
	/**
	 * @param aplicacion the aplicacion to set
	 */
	public final void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}
	/**
	 * @return the numero_unico
	 */
	public final String getNumero_unico() {
		return numero_unico;
	}
	/**
	 * @param numero_unico the numero_unico to set
	 */
	public final void setNumero_unico(String numero_unico) {
		this.numero_unico = numero_unico;
	}
	/**
	 * @return the envio_justificante
	 */
	public final String getEnvio_justificante() {
		return envio_justificante;
	}
	/**
	 * @param envio_justificante the envio_justificante to set
	 */
	public final void setEnvio_justificante(String envio_justificante) {
		this.envio_justificante = envio_justificante;
	}
	/**
	 * @return the mac
	 */
	public final String getMac() {
		return mac;
	}
	/**
	 * @param mac the mac to set
	 */
	public final void setMac(String mac) {
		this.mac = mac;
	}
	
	// 30/03/2023
	// 47535. Para construir el objeto
	public static class PeticionConsultaPagoBuilder {
		    private String origen;
		    private String modalidad;
		    private String referencia;
		    private String identificacion;
		    private String numero_autoliquidacion;
		    private String emisora;
		    private String aplicacion;
		    private String numero_unico; 
		    private String envio_justificante;
		    private String mac;
		    
		    public PeticionConsultaPagoBuilder(){}
		    
		    public PeticionConsultaPagoBuilder setOrigen(String origen) {
		        this.origen = origen;
		        return this;
		    }
		    
		    public PeticionConsultaPagoBuilder setModalidad(String modalidad) {
		        this.modalidad = modalidad;
		        return this;
		    }
		    
		    public PeticionConsultaPagoBuilder setReferencia(String referencia) {
		        this.referencia = referencia;
		        return this;
		    }
		    
		    public PeticionConsultaPagoBuilder setIdentificacion(String identificacion) {
		        this.identificacion = identificacion;
		        return this;
		    }
		    
		    public PeticionConsultaPagoBuilder setNumeroAutoliquidacion(String numero_autoliquidacion) {
		        this.numero_autoliquidacion = numero_autoliquidacion;
		        return this;
		    }
		    
		    public PeticionConsultaPagoBuilder setEmisora(String emisora) {
		        this.emisora = emisora;
		        return this;
		    }
		    
		    public PeticionConsultaPagoBuilder setAplicacion(String aplicacion) {
		        this.aplicacion = aplicacion;
		        return this;
		    }
		    
		    public PeticionConsultaPagoBuilder setNumeroUnico(String numero_unico) {
		        this.numero_unico = numero_unico;
		        return this;
		    }
		    
		    public PeticionConsultaPagoBuilder setEnvioJustificante(String envio_justificante) {
		        this.envio_justificante = envio_justificante;
		        return this;
		    }
		    
		    public PeticionConsultaPagoBuilder setMac(String mac) {
		        this.mac = mac;
		        return this;
		    }
		    
		    public PeticionConsultaPagoTarjeta build(){
		    	PeticionConsultaPagoTarjeta p = new PeticionConsultaPagoTarjeta();
		    	p.setOrigen(this.origen);
		    	p.setModalidad(this.modalidad);
		    	p.setReferencia(this.referencia);
		    	p.setIdentificacion(this.identificacion);
		    	p.setNumero_autoliquidacion(this.numero_autoliquidacion);
		    	p.setEmisora(this.emisora);
		    	p.setAplicacion(this.aplicacion);
		    	p.setNumero_unico(this.numero_unico);
		    	p.setEnvio_justificante(this.envio_justificante);
		    	p.setMac(this.mac);
		    	return p;
		    }
		    
	}
}
