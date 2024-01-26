package com.stpa.ws.server.validation;

import com.stpa.ws.server.base.IStpawsValidation;
import com.stpa.ws.server.bean.NumAutoliquidacion;
import com.stpa.ws.server.bean.NumAutoliquidacionMac;
import com.stpa.ws.server.bean.NumAutoliquidacionPeticion;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.PropertiesUtils;
import com.stpa.ws.server.util.StpawsUtil;
import com.stpa.ws.server.util.WebServicesUtils;

public class NumAutoliquidacionValidation implements IStpawsValidation{
	
	public boolean isValid(Object obj) throws StpawsException{
		boolean isValid = true;
		com.stpa.ws.server.util.Logger.debug("Valido datos de entrada...",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		NumAutoliquidacionPeticion nap = ((NumAutoliquidacion)obj).getPeticion();
		NumAutoliquidacionMac nam = ((NumAutoliquidacion)obj).getMac();
		com.stpa.ws.server.util.Logger.debug("Validamos la mac...",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		if(nam!=null){
			if(nam.getMac()==null || "".equals(nam.getMac())){
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.mac.vacio"),null);
			}else{
				String clave = WebServicesUtils.obtenerClave();
				boolean isMacValid = StpawsUtil.isMacValid(nap.getP_cliente(),nap.getP_timestamp(),nap.getP_modelo_autoliquidacion(),nap.getP_libre(),clave,nam.getMac());
				if(!isMacValid)
					throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.mac.no.valid"),null);
			}
		}else{
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.mac.vacio"),null);
		}
		com.stpa.ws.server.util.Logger.debug("MAC ok",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		com.stpa.ws.server.util.Logger.debug("Validamos datos obligatorios",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		if(nap!=null){
			if(nap.getP_cliente()==null || "".equals(nap.getP_cliente()))
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.client.vacio"),null);
			if(nap.getP_modelo_autoliquidacion()==null || "".equals(nap.getP_modelo_autoliquidacion()) || nap.getP_modelo_autoliquidacion().length()!=3)
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.modelo.vacio"),null);
			if(nap.getP_timestamp()==null || "".equals(nap.getP_timestamp()))
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.time.vacio"),null);
		}else{
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.peti.vacia"),null);
		}
		com.stpa.ws.server.util.Logger.debug("Datos obligatorios ok",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return isValid;
	}
	
	public static void main(String[] args){
		try{
			WebServicesUtils.obtenerClave();
		}catch(Exception e){
			e.printStackTrace();	
		}
		//StpawsUtil.isMacValid("claveConsejerias","26/11/2009","600","","");
	}
}
