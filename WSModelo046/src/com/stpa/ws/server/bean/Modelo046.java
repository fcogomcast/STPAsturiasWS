package com.stpa.ws.server.bean;

import java.io.Serializable;

public class Modelo046 implements Serializable{

	private static final long serialVersionUID = -1419530293345168876L;
	
	private Modelo046Peticion peticion =  new Modelo046Peticion();
	private Modelo046Respuesta respuesta = new Modelo046Respuesta(); 
	private Modelo046Mac mac = new Modelo046Mac();
	
	public Modelo046Peticion getPeticion() {
		return peticion;
	}
	public void setPeticion(Modelo046Peticion peticion) {
		this.peticion = peticion;
	}
	public Modelo046Respuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Modelo046Respuesta respuesta) {
		this.respuesta = respuesta;
	}
	public Modelo046Mac getMac() {
		return mac;
	}
	public void setMac(Modelo046Mac mac) {
		this.mac = mac;
	}
}
