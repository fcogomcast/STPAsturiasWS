package com.stpa.ws.server;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;

import com.stpa.ws.server.bean.NumAutoliquidacion;
import com.stpa.ws.server.op.NumAutoliquidacionOp;

@WebService
@HandlerChain (file="HandlerChainNA.xml")
public class Stpaws {
	
	@WebMethod
	public NumAutoliquidacion doNumAutoliquidacion(NumAutoliquidacion numAutoliquidacion){
		com.stpa.ws.server.util.Logger.debug("Entro en el servicio Numero de Autoliquidacion",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		NumAutoliquidacionOp naop = new NumAutoliquidacionOp();
		return (NumAutoliquidacion)naop.doOwnAction(numAutoliquidacion);
	}
}