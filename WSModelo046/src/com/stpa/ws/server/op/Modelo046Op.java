package com.stpa.ws.server.op;

import com.stpa.ws.server.base.IStpawsBase;
import com.stpa.ws.server.bean.Modelo046;
import com.stpa.ws.server.bean.Modelo046Autoliquidacion;
import com.stpa.ws.server.bean.Modelo046DatoEspecifico;
import com.stpa.ws.server.bean.Modelo046Detalle;
import com.stpa.ws.server.bean.Modelo046DetalleLiquidacion;
import com.stpa.ws.server.bean.Modelo046FechaDevengo;
import com.stpa.ws.server.bean.Modelo046Peticion;
import com.stpa.ws.server.bean.Modelo046Respuesta;
import com.stpa.ws.server.bean.Modelo046SujetoPasivo;
import com.stpa.ws.server.constantes.Modelo046Constantes;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.Logger;
import com.stpa.ws.server.util.PDFUtils;
import com.stpa.ws.server.util.PropertiesUtils;
import com.stpa.ws.server.util.SHAUtils;
import com.stpa.ws.server.util.StpawsUtil;
import com.stpa.ws.server.util.WebServicesUtil;
import com.stpa.ws.server.validation.Modelo046Validation;

public class Modelo046Op implements IStpawsBase{
		
	public Object doOwnAction(Object oin){
		
		Modelo046 m046 = null;
		Modelo046Validation m046v = new Modelo046Validation();

		if(oin instanceof Modelo046){
			m046 = (Modelo046)oin;

			try{
				if(m046v.isValid(oin)) m046=generarPdf(m046);
			}catch(StpawsException e1){
				m046 = tratarError(e1.getError(),m046);
			}catch(Throwable e2){
				try {
					com.stpa.ws.server.util.Logger.error(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.err.gen") + e2.getMessage(),e2,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
					m046 = tratarError(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.err.gen"),m046);
				} catch (StpawsException e) {
					com.stpa.ws.server.util.Logger.error("Error al tratar el error de la operación 2",e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				}	
			}
			com.stpa.ws.server.util.Logger.debug("Generamos la mac para la respuesta...",Logger.LOGTYPE.APPLOG);
			try{
				m046.getMac().setMac(StpawsUtil.genMac(StpawsUtil.mountMacResp(m046)));
			}catch(Throwable t){
				com.stpa.ws.server.util.Logger.error(t.getMessage(),t,Logger.LOGTYPE.APPLOG);
			}
			com.stpa.ws.server.util.Logger.debug("Mac generada.",Logger.LOGTYPE.APPLOG);
		}else{
			try {
				com.stpa.ws.server.util.Logger.debug("Parámetro de llegada no es válido",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				m046 = tratarError(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"msg.obj.no.valid"),null);
			} catch (StpawsException e) {
				com.stpa.ws.server.util.Logger.error("Error al tratar el error de la operación 2",e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			} 
		}
		return m046;
	}
	
	private Modelo046 generarPdf(Modelo046 m046) throws StpawsException {
		String modeloPdf=PDFUtils.generarPdfMod046(m046.getPeticion());
		
		m046.getRespuesta().setModeloPdf(modeloPdf);
		m046.getRespuesta().setResultado("0000");
		return m046;
	}
	
	private Modelo046 tratarError(String error, Modelo046 m046){
		Modelo046Respuesta m046r= new Modelo046Respuesta();
		if(m046==null) m046 = new Modelo046();
		m046r.setResultado(error);
		m046.setRespuesta(m046r);
		return m046;
	}
	

	public static void main(String[] args) {
	//claveConsejerias046101220091101120130000012341237384542CAlcaraz MiguelcSant Pere76-4-GavàBarcelona08850661282404Detalle DescripcionDetalle 1100100100100claveConsejerias
	//E955E8AE5B6C1F9A9EEAB71EC560A21F9E4C92F1
		//1C307ABC5CA9E0EA52220085A75ACE4DB6901FE8
		try{
			//String clave = WebServicesUtil.obtenerClave();
			String macMount = "claveconsejerias04620012009100412013160022009019371710EFERNANDEZplles Panes391casaunicaGavabarcelona08850661282404Desc1Pagos varios12002Pagos Varios 241005300clave";
					//"1101120130000012341237384542CAlcaraz MiguelcSant Pere76-4-GavàBarcelona08850661282404Detalle Descripcion1Detalle 1100100100100claveConsejerias";
			String s = SHAUtils.hex_sha1(macMount);
			System.out.println(s);
		}catch(Throwable t){
			t.printStackTrace();
		}
//		Modelo046 m046 = new Modelo046();
//		Modelo046Peticion m046p=new Modelo046Peticion();
//		Modelo046Autoliquidacion m046a= new Modelo046Autoliquidacion();
//		Modelo046DatoEspecifico m046de= new Modelo046DatoEspecifico();
//		Modelo046SujetoPasivo m046sp= new Modelo046SujetoPasivo();
//		Modelo046FechaDevengo m046fd=new Modelo046FechaDevengo();
//		Modelo046DetalleLiquidacion m046dl= new Modelo046DetalleLiquidacion();
//		Modelo046Detalle[] m046d= new Modelo046Detalle[2];
//		
//		m046d[0]=new Modelo046Detalle();
//		m046d[0].setDescripcionConcepto("descripcionConcepto");
//		m046d[0].setId("1");
//		m046d[0].setImporte("100");
//		m046d[0].setTarifa("200");
//		m046d[0].setValor("300");
//		
//		m046d[1]=new Modelo046Detalle();
//		m046d[1].setDescripcionConcepto("descripcionConcepto");
//		m046d[1].setId("2");
//		m046d[1].setImporte("100");
//		m046d[1].setTarifa("200");
//		m046d[1].setValor("300");
//				
//		m046dl.setDescripcion("descripcion");
//		m046dl.setDetalle(m046d);
//		
//		m046de.setAplicacion("1201328004");
//		m046de.setCentroGestor("9999");
//		m046de.setEjercicio("2009");
//		m046de.setPeriodo("3T");
//		
//		m046sp.setApellidosNombre("apellidosNombre");
//		m046sp.setCodPostal("17002");
//		m046sp.setEscalera("A");
//		m046sp.setLocalidad("Cudillero");
//		m046sp.setNif("44338436Y");
//		m046sp.setNombreVia("Joan Riera");
//		m046sp.setNumero("4");
//		m046sp.setPiso("4");
//		m046sp.setProvincia("Asturias");
//		m046sp.setPuerta("c");
//		m046sp.setSiglasVia("C");
//		m046sp.setTelefono("972513335");
//		
//		m046fd.setAno("2009");
//		m046fd.setDia("21");
//		m046fd.setMes("12");
//		
//		m046a.setCopia("0");
//		m046a.setFechaDevengo(m046fd);
//		m046a.setDatoEspecifico(m046de);
//		m046a.setSujetoPasivo(m046sp);
//		m046a.setDetalleLiquidacion(m046dl);
//		m046a.setModelo("046");
//		m046a.setNumeroAutoliquidacion("0001");
//		m046a.setTotalImporte("200");
//		m046a.setTotalValor("600");
//		
//		m046p.setCliente("cliente");
//		m046p.setLibre("libre");
//		m046p.setAutoliquidacion(m046a);
//		
//		m046.setPeticion(m046p);
//		m046.getMac().setMac(StpawsUtil.genMac(StpawsUtil.mountMac(m046.getPeticion())));
//		
//		Modelo046Validation val=new Modelo046Validation();
//		Modelo046Op op = new Modelo046Op();
//		try {
//			//val.isValid(m046);
//			m046 = op.generarPdf(m046);
//		} catch (StpawsException e) {
//			e.printStackTrace();
//		}
	}

}
