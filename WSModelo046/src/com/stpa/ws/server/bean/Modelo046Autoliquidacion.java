package com.stpa.ws.server.bean;

import java.io.Serializable;

public class Modelo046Autoliquidacion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5022707748415601312L;
	private String modelo = "";
	private String numeroAutoliquidacion = "";
	private String copia = "";
	private Modelo046FechaDevengo fechaDevengo= new Modelo046FechaDevengo();
	private Modelo046DatoEspecifico datoEspecifico = new Modelo046DatoEspecifico();
	private Modelo046SujetoPasivo sujetoPasivo = new Modelo046SujetoPasivo();
	private Modelo046DetalleLiquidacion detalleLiquidacion = new Modelo046DetalleLiquidacion();
	private String totalValor = "";
	private String totalImporte = "";
	
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public String getNumeroAutoliquidacion() {
		return numeroAutoliquidacion;
	}
	public void setNumeroAutoliquidacion(String numeroAutoliquidacion) {
		this.numeroAutoliquidacion = numeroAutoliquidacion;
	}
	public String getCopia() {
		return copia;
	}
	public void setCopia(String copia) {
		this.copia = copia;
	}
	public Modelo046FechaDevengo getFechaDevengo() {
		return fechaDevengo;
	}
	public void setFechaDevengo(Modelo046FechaDevengo fechaDevengo) {
		this.fechaDevengo = fechaDevengo;
	}
	public Modelo046DatoEspecifico getDatoEspecifico() {
		return datoEspecifico;
	}
	public void setDatoEspecifico(Modelo046DatoEspecifico datoEspecifico) {
		this.datoEspecifico = datoEspecifico;
	}
	public Modelo046SujetoPasivo getSujetoPasivo() {
		return sujetoPasivo;
	}
	public void setSujetoPasivo(Modelo046SujetoPasivo sujetoPasivo) {
		this.sujetoPasivo = sujetoPasivo;
	}
	public Modelo046DetalleLiquidacion getDetalleLiquidacion() {
		return detalleLiquidacion;
	}
	public void setDetalleLiquidacion(Modelo046DetalleLiquidacion detalleLiquidacion) {
		this.detalleLiquidacion = detalleLiquidacion;
	}
	public String getTotalValor() {
		return totalValor;
	}
	public void setTotalValor(String totalValor) {
		this.totalValor = totalValor;
	}
	public String getTotalImporte() {
		return totalImporte;
	}
	public void setTotalImporte(String totalImporte) {
		this.totalImporte = totalImporte;
	}
}
