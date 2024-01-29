package com.stpa.ws.server.bean;

import java.io.Serializable;

public class Modelo046Detalle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4449058998345415003L;
	private String id = "";
	private String tarifa = "";
	private String descripcionConcepto = "";
	private String valor = "";
	private String importe = "";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTarifa() {
		return tarifa;
	}
	public void setTarifa(String tarifa) {
		this.tarifa = tarifa;
	}
	public String getDescripcionConcepto() {
		return descripcionConcepto;
	}
	public void setDescripcionConcepto(String descripcionConcepto) {
		this.descripcionConcepto = descripcionConcepto;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getImporte() {
		return importe;
	}
	public void setImporte(String importe) {
		this.importe = importe;
	}
	
}
