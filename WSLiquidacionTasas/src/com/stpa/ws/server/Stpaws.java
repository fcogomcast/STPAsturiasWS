package com.stpa.ws.server;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import com.stpa.ws.server.bean.LiquidacionTasas;
import com.stpa.ws.server.formularios.Constantes;
import com.stpa.ws.server.op.LiquidacionTasasOp;
import com.stpa.ws.server.util.GestorIdLlamada;
import com.stpa.ws.server.util.Logger;

@WebService
@HandlerChain (file="HandlerChainLT.xml")
public class Stpaws {
	@Resource
	private WebServiceContext ctx;
	@WebMethod
	public LiquidacionTasas doLiquidacionTasas(LiquidacionTasas liquidacionTasas){
		String idLlamada=(String) ctx.getMessageContext().get(Constantes.ID_LLAMADA);
		try
		{
			GestorIdLlamada.asociarIdLlamada(idLlamada);
			Logger log = new Logger(idLlamada);
			log.debug("Entro en el servicio Liquidacion Tasas.",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			LiquidacionTasasOp ltop = new LiquidacionTasasOp(idLlamada);
			return (LiquidacionTasas)ltop.doOwnAction(liquidacionTasas);
		}
		finally
		{
			GestorIdLlamada.desasociarIdLlamada();
		}
	}
	
}