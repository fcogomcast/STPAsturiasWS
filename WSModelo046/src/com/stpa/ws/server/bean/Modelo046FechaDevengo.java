package com.stpa.ws.server.bean;

import java.io.Serializable;

public class Modelo046FechaDevengo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7835189824200580271L;
	private String dia = "";
	private String mes = "";
	private String ano = "";
	
	public String getDia() {
		return dia;
	}
	public void setDia(String dia) {
		this.dia = dia;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public String getAno() {
		return ano;
	}
	public void setAno(String ano) {
		this.ano = ano;
	}
}
