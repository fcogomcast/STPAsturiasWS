package com.stpa.ws.server;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;

import com.stpa.ws.server.bean.Modelo046;
import com.stpa.ws.server.op.Modelo046Op;

@WebService
@HandlerChain (file="HandlerChainM042.xml")
public class Stpaws {

	@WebMethod
	public Modelo046 doModelo046(Modelo046 modelo046){
		com.stpa.ws.server.util.Logger.debug("Entro en el servicio modelo046.",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		Modelo046Op mop = new Modelo046Op();
		return (Modelo046)mop.doOwnAction(modelo046);
	}
}