package com.stpa.ws.server.validation;

import com.stpa.ws.server.base.IStpawsValidation;
import com.stpa.ws.server.bean.Modelo046;
import com.stpa.ws.server.bean.Modelo046Detalle;
import com.stpa.ws.server.bean.Modelo046FechaDevengo;
import com.stpa.ws.server.bean.Modelo046Mac;
import com.stpa.ws.server.bean.Modelo046Peticion;
import com.stpa.ws.server.constantes.Modelo046Constantes;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.PropertiesUtils;
import com.stpa.ws.server.util.StpawsUtil;

public class Modelo046Validation implements IStpawsValidation{
	
	public boolean isValid(Object obj) throws StpawsException{
		boolean isValid = true;
		com.stpa.ws.server.util.Logger.debug("Valido datos de entrada...",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		Modelo046Peticion mp = ((Modelo046)obj).getPeticion();
		Modelo046Mac mm = ((Modelo046)obj).getMac();
		com.stpa.ws.server.util.Logger.debug("Validamos la mac...",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		if(mm!=null){
			if(!StpawsUtil.isStrValid(mm.getMac(),0,null)){
				throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.peti.mac.vacio"),null);
			}else{
				String macAux = StpawsUtil.mountMac(mp);
				com.stpa.ws.server.util.Logger.debug("Mac calculada: " + macAux,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				boolean isMacValid = StpawsUtil.isMacValid(macAux, mm.getMac());
				if(!isMacValid)
					throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.peti.mac.no.valid"),null);
			}
		}else{
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.peti.mac.vacio"),null);
		}
		com.stpa.ws.server.util.Logger.debug("MAC ok",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		com.stpa.ws.server.util.Logger.debug("Validamos datos obligatorios",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		if(mp!=null){
			if(!StpawsUtil.isStrValid(mp.getCliente(),0,null))
				throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.peti.client.vacio"),null);
			if(!isModeloValid(mp.getAutoliquidacion().getModelo()))
				throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.modelo.no.valid"),null);
			if(!StpawsUtil.isNumber(mp.getAutoliquidacion().getNumeroAutoliquidacion()))
				throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.numero.autoliquidacion.no.valid"),null);
			if(!StpawsUtil.isStrValid(mp.getAutoliquidacion().getCopia(),1,new String[]{"0","1","2","3"}))
				throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.copia.no.valid"),null);
			if(!isFechaDevengoValid(mp.getAutoliquidacion().getFechaDevengo()))	
				throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.fecha.devengo.no.valid"),null);
			if(!isCentroGestorValid(mp.getAutoliquidacion().getDatoEspecifico().getCentroGestor()))
				throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.centro.gestor.no.valid"),null);
			if(!isAppValid(mp.getAutoliquidacion().getDatoEspecifico().getAplicacion()))
				throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.aplicacion.no.valid"),null);
			if(!isEjercicioValid(mp.getAutoliquidacion().getDatoEspecifico().getEjercicio()))
				throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.ejercicio.no.valid"),null);
			if(!StpawsUtil.isStrValid(mp.getAutoliquidacion().getDatoEspecifico().getPeriodo(),2,new String[]{"01", "02", "03","04","05", "06", "07", "08", "09", "10", "11", "12", "1T", "2T", "3T", "4T", "1S", "2S", "1A"}))
				throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.periodo.no.valid"),null);
			if(!isDetalleValid(mp.getAutoliquidacion().getDetalleLiquidacion().getDetalle()))
				throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.detalle.no.valid"),null);
			if(!isTotalValorValid(mp.getAutoliquidacion().getTotalValor(),mp.getAutoliquidacion().getDetalleLiquidacion().getDetalle()))
				throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.total.valor.no.valid"),null);
			if(!isTotalImporteValid(mp.getAutoliquidacion().getTotalImporte(),mp.getAutoliquidacion().getDetalleLiquidacion().getDetalle()))
				throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.total.importe.no.valid"),null);
		}else{
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.peti.vacia"),null);
		}
		com.stpa.ws.server.util.Logger.debug("Datos obligatorios ok",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return isValid;
	}

	private boolean isTotalImporteValid(String totalImporte,Modelo046Detalle[] detalles) {
		boolean result=true;
		if(!StpawsUtil.isNumber(totalImporte)){
			result=false;
		}
		else if(calcularTotalImporte(detalles) != Long.parseLong(totalImporte)){
			result=false;
		}
		return result;
	}
	
	private boolean isTotalValorValid(String totalValor,Modelo046Detalle[] detalles) {
		boolean result=true;
		if(!StpawsUtil.isNumber(totalValor)){
			result=false;
		}
		else if(calcularTotalValor(detalles) != Long.parseLong(totalValor)){
			result=false;
		}
		return result;
	}


	public static boolean isCentroGestorValid(String centroGestor) {
		boolean result=true;
		if(!StpawsUtil.isStrValid(centroGestor,4,null))
			result=false;
		if(!StpawsUtil.isNumber(centroGestor))
			result=false;
		return result;
	}

	private boolean isModeloValid(String modelo) {
		boolean result=true;
		try {
			if(!"046".equals(modelo))
				result= false;
		} catch (Exception e) {
			result=false;
		}
		return result;
	} 
	
	private long calcularTotalValor(Modelo046Detalle[] detalles) {
		long totalValor=0;
		for (int i = 0; i < detalles.length; i++) {
			totalValor=totalValor+Long.parseLong(detalles[i].getValor());
		}
		return totalValor;
	}
	
	private long calcularTotalImporte(Modelo046Detalle[] detalles) {
		long totalImporte=0;
		for (int i = 0; i < detalles.length; i++) {
			totalImporte=totalImporte+Long.parseLong(detalles[i].getImporte());
		}
		return totalImporte;
	}

	private boolean isDetalleValid(Modelo046Detalle[] detalles) {
		boolean result=true;
		int i = 0;
		while(i < detalles.length && result) {
			if(!StpawsUtil.isNumber(detalles[i].getValor())){
				result=false;
			}else if(!StpawsUtil.isNumber(detalles[i].getImporte())){
				result=false;
			}
			i++;
		}
		return result;
	}

	private boolean isEjercicioValid(String ejercicio) {
		boolean result = true;
		if(!StpawsUtil.isStrValid(ejercicio,4,null))result = false;
		if(!StpawsUtil.isNumber(ejercicio))result = false;
		return result;
	}
	
	private boolean isFechaDevengoValid(Modelo046FechaDevengo fd) {
		boolean result = true;
		if(!StpawsUtil.isStrValid(fd.getDia(),2,null))result = false;
		if(!StpawsUtil.isStrValid(fd.getMes(),2,null))result = false;
		if(!StpawsUtil.isStrValid(fd.getAno(),4,null))result = false;
		if(!StpawsUtil.isDate(fd.getAno()+fd.getMes()+fd.getDia()))result = false;
		return result;
	}

	private boolean isAppValid(String aplicacion){
		boolean result = false;
		if(StpawsUtil.isStrValid(aplicacion,10,null) && 
				StpawsUtil.isNumber(aplicacion) && 
				aplicacion.substring(0,4).equals("1201")){
			String app = aplicacion.substring(4,aplicacion.length());
			String[] valores = new String[]{
					"300000","302000","302001","306000","306001","307001","308000","310001","314000","314001",
					"314002","314003","314004","314005","314006","314007","314008","314009","314010","314011",
					"315001","315005","315006","315007","316000","316001","316002","317000","318000","320000",
					"320001","320002","321000","322000","322001","322002","322003","322004","324001","324002",
					"325002","325003","325004","326000","326001","326002","327000","327001","327002","327005",
					"327006","327007","327008","327009","327010","327011","327012","328000","328001","328002",
					"328003","328004","328005","329000","329001","332000","382000","382001","388000","390000",
					"390001","391000","392000","392001","392002","392003","392004","392006","392007","394000",
					"394001","394002","395000","395001","395002","395003","395004","396000","396001","396002",
					"396003","396004","397000","397002","397003","398000","399000","399001"
			};
			result = StpawsUtil.constains(app,valores);
		}
		return result;
	}
	
}
