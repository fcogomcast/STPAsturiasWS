package com.stpa.ws.server.bean;

public class LiquidacionTasas {
	private LiquidacionTasasPeticion peticion = new LiquidacionTasasPeticion();
	private LiquidacionTasasRespuesta respuesta = new LiquidacionTasasRespuesta();
	private Mac mac = new Mac();
	
	public LiquidacionTasasPeticion getPeticion() {
		return peticion;
	}
	public void setPeticion(LiquidacionTasasPeticion peticion) {
		this.peticion = peticion;
	}
	public LiquidacionTasasRespuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(LiquidacionTasasRespuesta respuesta) {
		this.respuesta = respuesta;
	}
	public Mac getMac() {
		return mac;
	}
	public void setMac(Mac mac) {
		this.mac = mac;
	}
	
	
}
