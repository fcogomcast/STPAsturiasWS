package com.stpa.ws.server.bean;

import java.io.Serializable;

public class Modelo046DatoEspecifico implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4158169639370457527L;
	private String centroGestor = "";
	private String aplicacion = "";
	private String ejercicio = "";
	private String periodo = "";
	
	public String getCentroGestor() {
		return centroGestor;
	}
	public void setCentroGestor(String centroGestor) {
		this.centroGestor = centroGestor;
	}
	public String getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}
	public String getEjercicio() {
		return ejercicio;
	}
	public void setEjercicio(String ejercicio) {
		this.ejercicio = ejercicio;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	
}
