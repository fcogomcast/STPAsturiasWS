package es.tributasenasturias.webservices.Certificados.impl;

import es.tributasenasturias.webservices.Certificados.SERVICIOWEB;



public class SalidaServicio extends SERVICIOWEB{
	public SalidaServicio(SERVICIOWEB entrada)
	{
		if (entrada!=null)
		{
			this.peticion= entrada.getPETICION();
			this.setCliente(entrada.getCliente());
			this.setServicio(entrada.getServicio());
		}
		//Se elimina de la salida el certificado.
		//this.peticion.getCERTIFICADO().setX509Cert(null);
		respuesta = new RESPUESTA();
	}
	public void setErrorResponse (String resultado)
	{
		RESPUESTA respuesta= this.getRESPUESTA();
		respuesta.setRESULTADO(resultado);
		respuesta.setIDENTIFICACION("");
		respuesta.setNIF("");
		respuesta.setNOMBRE("");
		respuesta.setFECHAGENERACION("");
		respuesta.setFECHAVALIDEZ("");
	}
}
