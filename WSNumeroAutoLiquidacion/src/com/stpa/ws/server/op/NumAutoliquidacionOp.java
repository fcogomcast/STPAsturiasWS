package com.stpa.ws.server.op;

import com.stpa.ws.server.base.IStpawsBase;
import com.stpa.ws.server.bean.NumAutoliquidacion;
import com.stpa.ws.server.bean.NumAutoliquidacionRespuesta;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.Logger;
import com.stpa.ws.server.util.PropertiesUtils;
import com.stpa.ws.server.util.SHAUtils;
import com.stpa.ws.server.util.StpawsUtil;
import com.stpa.ws.server.util.WebServicesUtils;
import com.stpa.ws.server.validation.NumAutoliquidacionValidation;

public class NumAutoliquidacionOp implements IStpawsBase{
	
	public Object doOwnAction(Object oin){
		com.stpa.ws.server.util.Logger.debug("Entro en el servicio Numero Autoliquidacion",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		NumAutoliquidacionValidation nav = new NumAutoliquidacionValidation();
		if(oin instanceof NumAutoliquidacion){
			NumAutoliquidacion na = (NumAutoliquidacion)oin;
			try{
				if(nav.isValid(na)) na = obtenerNumSerie(na);
			}catch(StpawsException stpae){
				com.stpa.ws.server.util.Logger.error(stpae.getError(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				oin = tratarError(stpae.getError(),na);
			}catch(Throwable te){
				String s = "";
				try{
					s = PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen.num");
				}catch(Throwable t){
					com.stpa.ws.server.util.Logger.error(t.getMessage(),t,Logger.LOGTYPE.APPLOG);
				}
				com.stpa.ws.server.util.Logger.error(s + te.getMessage(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				oin = tratarError(s + te.getMessage(),na);
			}
			try{
				com.stpa.ws.server.util.Logger.debug("Generamos la mac para la respuesta...",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				oin = tratarMac(na);
				com.stpa.ws.server.util.Logger.debug("Mac generada.",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			}catch(Throwable t){
				com.stpa.ws.server.util.Logger.error(t.getMessage(),t,Logger.LOGTYPE.APPLOG);
			}
		}else{
			String s = "";
			try{
				s = PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.obj.no.valid");
			}catch(Throwable t){
				com.stpa.ws.server.util.Logger.error(t.getMessage(),t,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			}
			com.stpa.ws.server.util.Logger.error(s,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			oin = tratarError(s,null); 
		}
		com.stpa.ws.server.util.Logger.debug("Salgo del servicio Numero Autoliquidacion",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return oin;
	}
	
	private NumAutoliquidacion obtenerNumSerie(NumAutoliquidacion na) throws StpawsException{
		String peticion = WebServicesUtils.generarPeticionNumeroSerie(na);
		String xmlOut = WebServicesUtils.wsCall(peticion);
		try{
			if(xmlOut.indexOf("<error>")==-1)
				na = WebServicesUtils.wsResponseNumeroSerie_AsList(xmlOut,na);
			else
				tratarError(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen.num"),na);
		}catch(Exception e){
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen.num"), e);
		}
		return na;
	}
	
	private Object tratarError(String error, NumAutoliquidacion na){
		NumAutoliquidacionRespuesta nar = new NumAutoliquidacionRespuesta();
		if(na==null) na = new NumAutoliquidacion();
		nar.setResultado(error);
		nar.setTimestamp(StpawsUtil.getTimeStamp());
		na.setRespuesta(nar);
		return na;
	}
	
	private Object tratarMac(NumAutoliquidacion na) throws StpawsException{
		String clave = WebServicesUtils.obtenerClave();
		//Generamos la nueva MAC
		na.getMac().setMac(StpawsUtil.genMac(na.getPeticion().getP_cliente(),na.getPeticion().getP_timestamp(),
				na.getPeticion().getP_modelo_autoliquidacion(),na.getPeticion().getP_libre(),
				na.getRespuesta().getTimestamp(),na.getRespuesta().getResultado(),
				na.getRespuesta().getNumero_autoliquidacion(), clave));
		return na;
	}
	
	public static void main(String[] args){
		try{
			
			String s = SHAUtils.hex_sha1("claveConsejerias" + "26/11/2009" + "600" + "" + "clave");
			
			
			NumAutoliquidacionOp naop = new NumAutoliquidacionOp();
			NumAutoliquidacion na = new NumAutoliquidacion();
			na.getMac().setMac("679E97E737C67B72191E474C24D4DAA617F5B416");
			na.getPeticion().setP_cliente("claveConsejerias");
			na.getPeticion().setP_modelo_autoliquidacion("600");
			na.getPeticion().setP_timestamp("26/11/2009");
			na = naop.obtenerNumSerie(na);
			
			System.out.println("");
		}catch(Throwable te){
			te.printStackTrace();
		}
	}
}
