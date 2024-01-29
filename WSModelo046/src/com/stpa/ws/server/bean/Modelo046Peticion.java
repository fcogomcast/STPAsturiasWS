package com.stpa.ws.server.bean;

import java.io.Serializable;

public class Modelo046Peticion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 393032870016483554L;
	private Modelo046Autoliquidacion autoliquidacion = new Modelo046Autoliquidacion();
	private String libre = "";
	private String cliente = "";
	
	public Modelo046Autoliquidacion getAutoliquidacion() {
		return autoliquidacion;
	}
	public void setAutoliquidacion(Modelo046Autoliquidacion autoliquidacion) {
		this.autoliquidacion = autoliquidacion;
	}
	public String getLibre() {
		return libre;
	}
	public void setLibre(String libre) {
		this.libre = libre;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
}
