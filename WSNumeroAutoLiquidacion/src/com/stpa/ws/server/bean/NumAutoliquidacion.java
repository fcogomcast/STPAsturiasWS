package com.stpa.ws.server.bean;

public class NumAutoliquidacion {

	private NumAutoliquidacionPeticion peticion = new NumAutoliquidacionPeticion();
	private NumAutoliquidacionRespuesta respuesta = new NumAutoliquidacionRespuesta();
	private NumAutoliquidacionMac mac = new NumAutoliquidacionMac();
	
	public NumAutoliquidacionPeticion getPeticion() {
		return peticion;
	}
	public void setPeticion(NumAutoliquidacionPeticion peticion) {
		this.peticion = peticion;
	}
	public NumAutoliquidacionRespuesta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(NumAutoliquidacionRespuesta respuesta) {
		this.respuesta = respuesta;
	}
	public NumAutoliquidacionMac getMac() {
		return mac;
	}
	public void setMac(NumAutoliquidacionMac mac) {
		this.mac = mac;
	}
	
}
