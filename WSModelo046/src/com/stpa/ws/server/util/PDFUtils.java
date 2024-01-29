package com.stpa.ws.server.util;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.stp.webservices.utils.Base64;
import com.stpa.ws.server.bean.Modelo046Detalle;
import com.stpa.ws.server.bean.Modelo046Peticion;
import com.stpa.ws.server.constantes.Modelo046Constantes;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.formularios.CodiDescBean;
import com.stpa.ws.server.formularios.DetalleLiquidacion;
import com.stpa.ws.server.formularios.FormBean046;
import com.stpa.ws.server.formularios.GenericModeloFormBean;
import com.stpa.ws.server.formularios.IModeloFormBean;
import com.stpa.ws.server.formularios.ModeloFillUtils;
import com.stpa.ws.server.formularios.PdfBean;

public class PDFUtils {
	
	public static String generarPdfMod046(Modelo046Peticion m046p) throws StpawsException {
		com.stpa.ws.server.util.Logger.debug("Entramos en generarPdfMod046...",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		String idioma="es";	
		
		//1º Rellenar bean: 
		FormBean046 fb046=rellenarBean(m046p);
		
		//2º Generar PdfBean
		PdfBean b046 = generarPdf(fb046, Modelo046Constantes.TIPO_MODELO, idioma);
		
		//3º portletbridge excribir Pdf
		String copia=obtenerCopia(m046p.getAutoliquidacion().getCopia());
		boolean isvalid = ModeloFillUtils.escribePDF(b046.getXMLDatos(), b046.getModelo(), null, buffer, copia);
		if (!isvalid) {
			com.stpa.ws.server.util.Logger.debug("Error en la creacion del pdf",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			buffer = null;
			b046.addError("Se ha producido un error en las comunicaciones. Disculpe las molestias");
		} else {
			com.stpa.ws.server.util.Logger.debug("compiladoA ModeloFillUtils",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}

		char[] c = Base64.encode(buffer.toByteArray());
		com.stpa.ws.server.util.Logger.debug("fin generación Pdf",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return new String(c);
	}
	
	private static String obtenerCopia(String pCopia){
		String copia=null;
		if("0".equals(pCopia)){
			copia=Modelo046Constantes.STR_COPIA_TODAS;
		}else if("1".equals(pCopia)){
			copia=Modelo046Constantes.STR_COPIA_ADMINISTRACION;
		}else if("2".equals(pCopia)){
			copia=Modelo046Constantes.STR_COPIA_INTERESADO;
		}else if("3".equals(pCopia)){
			copia=Modelo046Constantes.STR_COPIA_ENTIDAD;
		}
		return copia;
	}
	
	private static PdfBean generarPdf(IModeloFormBean formulario, 
			String tipoModelo,
			String idioma) throws StpawsException {
		
		PdfBean pdf = new PdfBean();

		GenericModeloFormBean elem = (GenericModeloFormBean) formulario;
		String modelo = elem.getModelo(); 
		
		String detalle = "";
		FormBean046 f46 = (FormBean046)formulario;
		List<DetalleLiquidacion> listaDetalle = f46.getDetalle();
		int numDetalles = 0;
		for(int i=0;i<listaDetalle.size();i++){
			DetalleLiquidacion dl = listaDetalle.get(i);
			HashMap<String,Object> paramsHijos = UtilsComunes.rellenarXMLDocMapaRecursiu(dl,null);
			String xmlHijos = UtilsComunes.rellenarXMLDoc(modelo,paramsHijos,null,null);
			xmlHijos = xmlHijos.substring(xmlHijos.indexOf("datos")+14);
			xmlHijos = xmlHijos.substring(0,xmlHijos.indexOf("datos")-11);
			xmlHijos = "<DetalleLiquidacion id='" + (i+1) + "'>" + xmlHijos + "</DetalleLiquidacion>";
			com.stpa.ws.server.util.Logger.debug("########xmlHijos: " + xmlHijos,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			detalle += xmlHijos;
			com.stpa.ws.server.util.Logger.debug("########detalle: " + detalle,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);	
			numDetalles++;
		}
		
		HashMap<String,Object> params = UtilsComunes.rellenarXMLDocMapaRecursiu(formulario,null);
		String xml = UtilsComunes.rellenarXMLDoc(modelo,params,null,null);
		com.stpa.ws.server.util.Logger.debug("########xml: " + xml,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		
		xml = xml.replace("<detalle/>",detalle);
		String numDeta = "<numDetalles>" + numDetalles + "</numDetalles>";
		xml = xml.replace("<numDetalles/>",numDeta);
		com.stpa.ws.server.util.Logger.debug("########xml(final): " + xml,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		
		pdf.setXMLDatos(xml);
		pdf.setModelo(modelo);
		pdf.setNombreDoc("_");
		pdf.setTipoPdf("A");
	
		
		pdf.setTipoModelo(tipoModelo);
		pdf.setAuthTipo(false);
		pdf.setTitulo("");
		pdf.setMensaje("");
		
		return pdf;
	}
	
	/**
	 * Se rellenan los campos del bean
	 * @param m046p
	 * @return
	 * @throws StpawsException
	 */
	private static FormBean046 rellenarBean(Modelo046Peticion m046p) throws StpawsException{
		com.stpa.ws.server.util.Logger.debug("Rellenamos el bean",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		FormBean046 bean046= new FormBean046();
		
		bean046.setModelo(m046p.getAutoliquidacion().getModelo());
		bean046.setNumerodeserie(m046p.getAutoliquidacion().getNumeroAutoliquidacion());
		bean046.setFd_dia_devengo(m046p.getAutoliquidacion().getFechaDevengo().getDia());
		bean046.setFd_mes_devengo(m046p.getAutoliquidacion().getFechaDevengo().getMes());
		bean046.setFd_ano_devengo(m046p.getAutoliquidacion().getFechaDevengo().getAno());
		bean046.setCentroGestor(m046p.getAutoliquidacion().getDatoEspecifico().getCentroGestor());
		bean046.setAplicacion(m046p.getAutoliquidacion().getDatoEspecifico().getAplicacion());
		bean046.setEjercicio(m046p.getAutoliquidacion().getDatoEspecifico().getEjercicio());
		bean046.setPeriodo(m046p.getAutoliquidacion().getDatoEspecifico().getPeriodo());
		bean046.setC06(m046p.getAutoliquidacion().getSujetoPasivo().getNif());
		bean046.setC07(m046p.getAutoliquidacion().getSujetoPasivo().getApellidosNombre());
		bean046.setC08(m046p.getAutoliquidacion().getSujetoPasivo().getSiglasVia());
		bean046.setC09(m046p.getAutoliquidacion().getSujetoPasivo().getNombreVia());
		bean046.setC10(m046p.getAutoliquidacion().getSujetoPasivo().getNumero());
		bean046.setC11(m046p.getAutoliquidacion().getSujetoPasivo().getEscalera());
		bean046.setC12(m046p.getAutoliquidacion().getSujetoPasivo().getPiso());
		bean046.setC13(m046p.getAutoliquidacion().getSujetoPasivo().getPuerta());
		/////////////////////////////////////////////////////////////////////
		//Set<CodiDescBean> listaProvincias = UtilsComunes.findProvincias();
		//bean046.setListaProvincias(listaProvincias);
		/////////////////////////////////////////////////////////////////////
		bean046.setC15(m046p.getAutoliquidacion().getSujetoPasivo().getProvincia());
		bean046.setDescripcionProvincia(m046p.getAutoliquidacion().getSujetoPasivo().getProvincia());
		bean046.setC14(m046p.getAutoliquidacion().getSujetoPasivo().getLocalidad());
		bean046.setDescripcionMunicipio(m046p.getAutoliquidacion().getSujetoPasivo().getLocalidad());
		bean046.setC16(m046p.getAutoliquidacion().getSujetoPasivo().getCodPostal());
		bean046.setC17(m046p.getAutoliquidacion().getSujetoPasivo().getTelefono());
		bean046.setC18(m046p.getAutoliquidacion().getDetalleLiquidacion().getDescripcion());
		bean046.setNumDetalles(m046p.getAutoliquidacion().getDetalleLiquidacion().getDetalle().length);
		List<DetalleLiquidacion> lDetalles = new ArrayList<DetalleLiquidacion>();
		Modelo046Detalle[] aDetalles = m046p.getAutoliquidacion().getDetalleLiquidacion().getDetalle();
		
		for (int i = 0; i < aDetalles.length; i++) {
			DetalleLiquidacion detalle = new DetalleLiquidacion();
			com.stpa.ws.server.util.Logger.debug("aDetalles["+i+"].getTarifa(): " + aDetalles[i].getTarifa(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			detalle.setD01_1(aDetalles[i].getTarifa());
			com.stpa.ws.server.util.Logger.debug("aDetalles["+i+"].getDescripcionConcepto(): " + aDetalles[i].getDescripcionConcepto(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			detalle.setD02_1(aDetalles[i].getDescripcionConcepto());
			com.stpa.ws.server.util.Logger.debug("aDetalles["+i+"].getValor(): " + aDetalles[i].getValor(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			detalle.setD03_1(aDetalles[i].getValor());
			com.stpa.ws.server.util.Logger.debug("aDetalles["+i+"].getImporte(): " + aDetalles[i].getImporte(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			detalle.setD04_1(aDetalles[i].getImporte());
			lDetalles.add(detalle);
		}
		bean046.setDetalle(lDetalles);
		
		bean046.setTOT_V(m046p.getAutoliquidacion().getTotalValor());
		bean046.setTOT_I(m046p.getAutoliquidacion().getTotalImporte());
		
		com.stpa.ws.server.util.Logger.debug("Bean relleno: " + bean046.toString(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		
		return bean046;
	}
}

