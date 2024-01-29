package com.stpa.ws.server.bean;

import java.io.Serializable;

public class Modelo046Respuesta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2977695701505733293L;
	private String resultado = "";
	private String modeloPdf = "";
	
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	public String getModeloPdf() {
		return modeloPdf;
	}
	public void setModeloPdf(String modeloPdf) {
		this.modeloPdf = modeloPdf;
	}
}
