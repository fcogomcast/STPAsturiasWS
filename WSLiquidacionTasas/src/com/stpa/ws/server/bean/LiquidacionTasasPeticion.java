package com.stpa.ws.server.bean;

public class LiquidacionTasasPeticion {
	private String cliente = "";
	private String operacion = "";
	private String numeroUnico = "";
	private LiquidacionTasasLiquidacion liquidacion = new LiquidacionTasasLiquidacion();
	private String libre = "";
	
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getOperacion() {
		return operacion;
	}
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
	public String getNumeroUnico() {
		return numeroUnico;
	}
	public void setNumeroUnico(String numeroUnico) {
		this.numeroUnico = numeroUnico;
	}
	public LiquidacionTasasLiquidacion getLiquidacion() {
		return liquidacion;
	}
	public void setLiquidacion(LiquidacionTasasLiquidacion liquidacion) {
		this.liquidacion = liquidacion;
	}
	public String getLibre() {
		return libre;
	}
	public void setLibre(String libre) {
		this.libre = libre;
	}
	
		
}
