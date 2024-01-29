package com.stpa.ws.server.bean;

import java.io.Serializable;

public class Modelo046DetalleLiquidacion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2448484511200741207L;
	private String descripcion = "";
	private Modelo046Detalle[] detalle;
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Modelo046Detalle[] getDetalle() {
		return detalle;
	}
	public void setDetalle(Modelo046Detalle[] detalle) {
		this.detalle = detalle;
	}
}
