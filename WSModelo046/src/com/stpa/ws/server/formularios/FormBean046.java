package com.stpa.ws.server.formularios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.stpa.ws.server.util.NumberUtil;
import com.stpa.ws.server.util.UtilsComunes;

public class FormBean046 extends GenericModeloFormBean implements IModeloFormBean {

	public static final int MAX_DETALLE = 7;
	public int numDetalles = 1;
	
	//fd_dia_devengo;
	public String c01d1;
	public String c01d2;
	
	//fd_mes_devengo;
	public String c01m1;
	public String c01m2;
	
	//fd_ano_devengo;
	public String c01a1;
	public String c01a2;
	public String c01a3;
	public String c01a4;

	//centroGestor;
	public String c02a;
	public String c02b;
	public String c02c;
	public String c02d;

	//aplicacion;
	public String c03a = "1";
	public String c03b = "2";
	public String c03c = "0";
	public String c03d = "1";
	public String c03e;
	public String c03f;
	public String c03g;
	public String c03h;
	public String c03i;
	public String c03j;
	//ejercicio;
	public String c04a1;
	public String c04a2;
	public String c04a3;
	public String c04a4;
	//periodo;
	public String c05a;
	public String c05b;
	
	
	public String fd_dia_devengo;
	public String fd_mes_devengo;
	public String fd_ano_devengo;

	public String centroGestor;
	public String aplicacion;
	public String ejercicio;
	public String periodo;
	public String c06;
	public String c07;
	public String c08;
	public String c09;
	public String c10;
	public String c11;
	public String c12;
	public String c13;
	public String c15;
	public String descripcionProvincia;
	public String c14;
	public String descripcionMunicipio;
	public String c16;
	public String c17;
	public String c18;

	public String TOT_V;
	public String TOT_I;

	public Set<CodiDescBean> listaProvincias;
	public Set<CodiDescBean> listaMunicipios;
	public List<DetalleLiquidacion> detalle = new ArrayList<DetalleLiquidacion>();

	public String getImporte() {
		setImporte(TOT_I);
		return TOT_I;
	}

	public Map<String, Object> getMapParametrosParaXml(Map<String, String[]> mapParams) {
		setC14(c14);
		setC15(c15);
		formatearImportesDetalle();
		formatearTotales();

		Map<String, Object> mapParamTemp = UtilsComunes.rellenarXMLDocMapa(this, mapParams);
		Iterator<String> it = mapParamTemp.keySet().iterator();
		Map<String, Object> mapParamTempSalida = new HashMap<String, Object>();
		while (it.hasNext()) {
			String key = it.next();
			if (!key.contains("#")) {
				mapParamTempSalida.put(key, mapParamTemp.get(key));
			}
		}

		return mapParamTempSalida;
	}

	public void formatearImportesDetalle() {
		// Recorro cada objeto detalle formateando su campo importe solo si es
		// correcto
		for (int i = 0; i < this.getNumDetalles(); i++) {
			DetalleLiquidacion unDetalle = this.detalle.get(i);

			String valor = unDetalle.getD03_1();
			unDetalle.setD03_1(NumberUtil.getDoubleAsFormatString(valor, MAX_DECIMALES, true));

			String importe = unDetalle.getD04_1();
			unDetalle.setD04_1(NumberUtil.getDoubleAsFormatString(importe, MAX_DECIMALES, true));
		}
	}

	public void formatearTotales() {
		this.TOT_V = NumberUtil.getDoubleAsFormatString(this.TOT_V, MAX_DECIMALES, true);
		this.TOT_I = NumberUtil.getDoubleAsFormatString(this.TOT_I, MAX_DECIMALES, true);
	}

	public static Map<String, List<String>> llenarMapPDF() {
		Map<String, List<String>> mapSalida = new HashMap<String, List<String>>();
		List<String> xml = new ArrayList<String>();
		List<String> pdf = new ArrayList<String>();
		Map<String, List<String>> mapAux = null;
		for (int i = 0; i < MAX_DETALLE; i++) {
			mapAux = DetalleLiquidacion.generarSortidaPdf("detalle", i);
			xml.addAll(mapAux.get(BaseElementsForm.MAP));
			pdf.addAll(mapAux.get(BaseElementsForm.PDF));
		}
		mapSalida.put(BaseElementsForm.MAP, xml);
		mapSalida.put(BaseElementsForm.PDF, pdf);
		return mapSalida;
	}

	public void rellenarCamposAltaDocumento() {
		setNifSujetoPasivo(c06);
		setNifPresentador(c06);
		setNombreSujetoPasivo(c07);
	}

	public String getFd_dia_devengo() {
		return fd_dia_devengo;
	}

	public void setFd_dia_devengo(String fd_dia_devengo) {
		this.fd_dia_devengo = fd_dia_devengo;
		if(fd_dia_devengo.length()==2){
			c01d1 = fd_dia_devengo.substring(0,1);
			c01d2 = fd_dia_devengo.substring(1,2);
		}else if(fd_dia_devengo.length()==1){
			c01d1 = "0";
			c01d2 = fd_dia_devengo.substring(0,1);
		}
	}

	public String getFd_mes_devengo() {
		return fd_mes_devengo;
	}

	public void setFd_mes_devengo(String fd_mes_devengo) {
		this.fd_mes_devengo = fd_mes_devengo;
		if(fd_mes_devengo.length()==2){
			c01m1 = fd_mes_devengo.substring(0,1);
			c01m2 = fd_mes_devengo.substring(1,2);
		}else if(fd_mes_devengo.length()==1){
			c01m1 = "0";
			c01m2 = fd_mes_devengo.substring(0,1);
		}
	}

	public String getFd_ano_devengo() {
		return fd_ano_devengo;
	}

	public void setFd_ano_devengo(String fd_ano_devengo) {
		this.fd_ano_devengo = fd_ano_devengo;
		if(fd_ano_devengo.length()==4){
			c01a1 = fd_ano_devengo.substring(0,1);
			c01a2 = fd_ano_devengo.substring(1,2);
			c01a3 = fd_ano_devengo.substring(2,3);
			c01a4 = fd_ano_devengo.substring(3,4);
		}else if(fd_ano_devengo.length()==2){
			c01a1 = "2";
			c01a2 = "0";
			c01a3 = fd_ano_devengo.substring(0,1);
			c01a4 = fd_ano_devengo.substring(1,2);
		}
	}

	public String getCentroGestor() {
		return centroGestor;
	}

	public void setCentroGestor(String centroGestor) {
		this.centroGestor = centroGestor;
		c02a = centroGestor.substring(0,1);
		c02b = centroGestor.substring(1,2);
		c02c = centroGestor.substring(2,3);
		c02d = centroGestor.substring(3,4);
	}

	public String getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
		c03e = aplicacion.substring(4,5);
		c03f = aplicacion.substring(5,6);
		c03g = aplicacion.substring(6,7);
		c03h = aplicacion.substring(7,8);
		c03i = aplicacion.substring(8,9);
		c03j = aplicacion.substring(9,10);
	}

	public String getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(String ejercicio) {
		this.ejercicio = ejercicio;
		c04a1 = ejercicio.substring(0,1);
		c04a2 = ejercicio.substring(1,2);
		c04a3 = ejercicio.substring(2,3);
		c04a4 = ejercicio.substring(3,4);
	}
	
	public String getC04a2() {
		return c04a2;
	}

	public void setC04a2(String c04a2) {
		this.c04a2 = c04a2;
	}

	public String getC04a3() {
		return c04a3;
	}

	public void setC04a3(String c04a3) {
		this.c04a3 = c04a3;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
		c05a = periodo.substring(0,1);
		c05b = periodo.substring(1,2);
	}

	public String getC06() {
		return c06;
	}

	public void setC06(String c06) {
		this.c06 = c06;
	}

	public String getC07() {
		return c07;
	}

	public void setC07(String c07) {
		this.c07 = c07;
	}

	public String getC08() {
		return c08;
	}

	public void setC08(String c08) {
		this.c08 = c08;
	}

	public String getC09() {
		return c09;
	}

	public void setC09(String c09) {
		this.c09 = c09;
	}

	public String getC10() {
		return c10;
	}

	public void setC10(String c10) {
		this.c10 = c10;
	}

	public String getC11() {
		return c11;
	}

	public void setC11(String c11) {
		this.c11 = c11;
	}

	public String getC12() {
		return c12;
	}

	public void setC12(String c12) {
		this.c12 = c12;
	}

	public String getC13() {
		return c13;
	}

	public void setC13(String c13) {
		this.c13 = c13;
	}

	public String getC15() {
		return c15;
	}

	public void setC15(String c15) {
		this.c15 = c15;
		if (StringUtils.isBlank(c15)) {
			this.descripcionProvincia = null;
		} else {
			CodiDescBean objProvincia = UtilsComunes.getCodiDescBeanPerCodigo(c15, listaProvincias);
			descripcionProvincia = (objProvincia != null) ? objProvincia.getDesc() : null;
		}
	}

	public String getC14() {
		return c14;
	}

	public void setC14(String c14) {
		this.c14 = c14;
		if (StringUtils.isBlank(c14)) {
			this.descripcionMunicipio = null;
		} else {
			CodiDescBean objMunicipio = UtilsComunes.getCodiDescBeanPerCodigo(c14, this.listaMunicipios);
			descripcionMunicipio = (objMunicipio != null) ? objMunicipio.getDesc() : null;
		}
	}

	public String getC16() {
		return c16;
	}

	public void setC16(String c16) {
		this.c16 = c16;
	}

	public String getC17() {
		return c17;
	}

	public void setC17(String c17) {
		this.c17 = c17;
	}

	public String getC18() {
		return c18;
	}

	public void setC18(String c18) {
		this.c18 = c18;
	}

	public String getTOT_V() {
		return TOT_V;
	}

	public void setTOT_V(String tot_v) {
		TOT_V = tot_v;
	}

	public String getTOT_I() {
		return TOT_I;
	}

	public void setTOT_I(String tot_i) {
		TOT_I = tot_i;
	}

	public Set<CodiDescBean> getListaProvincias() {
		return listaProvincias;
	}

	public void setListaProvincias(Set<CodiDescBean> listaProvincias) {
		this.listaProvincias = listaProvincias;
	}

	public Set<CodiDescBean> getListaMunicipios() {
		return listaMunicipios;
	}

	public void setListaMunicipios(Set<CodiDescBean> listaMunicipios) {
		this.listaMunicipios = listaMunicipios;
	}

	public List<DetalleLiquidacion> getDetalle() {
		return detalle;
	}

	public void setDetalle(List<DetalleLiquidacion> detalle) {
		this.detalle = detalle;
	}

	public int getNumDetalles() {
		return numDetalles;
	}

	public void setNumDetalles(int numDetalles) {
		this.numDetalles = numDetalles;
	}

	public String getDescripcionProvincia() {
		return descripcionProvincia;
	}

	public void setDescripcionProvincia(String descripcionProvincia) {
		this.descripcionProvincia = descripcionProvincia;
	}

	public String getDescripcionMunicipio() {
		return descripcionMunicipio;
	}

	public void setDescripcionMunicipio(String descripcionMunicipio) {
		this.descripcionMunicipio = descripcionMunicipio;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
	    String NEW_LINE = System.getProperty("line.separator");
	    
	    result.append(this.getClass().getName() + " Object {" + NEW_LINE);
	    if(fd_dia_devengo!=null)
	    	result.append(" fd_dia_devengo	    	: " + fd_dia_devengo.toString() + NEW_LINE);      
	    	if(fd_mes_devengo!=null)
	    	result.append(" fd_mes_devengo		    : " + fd_mes_devengo.toString() + NEW_LINE);      
	    	if(fd_ano_devengo!=null)
	    	result.append(" fd_ano_devengo		    : " + fd_ano_devengo.toString() + NEW_LINE);      
	    	if(centroGestor!=null)
	    	result.append(" centroGestor		    : " + centroGestor.toString() + NEW_LINE);        
	    	if(aplicacion!=null)
	    	result.append(" aplicacion		    	: " + aplicacion.toString() + NEW_LINE);          
	    	if(ejercicio!=null)
	    	result.append(" ejercicio		    	: " + ejercicio.toString() + NEW_LINE);           
	    	if(c04a2!=null)
	    	result.append(" c04a2		    		: " + c04a2.toString() + NEW_LINE);               
	    	if(c04a3!=null)
	    	result.append(" c04a3		    		: " + c04a3.toString() + NEW_LINE);               
	    	if(periodo!=null)
	    	result.append(" periodo		    		: " + periodo.toString() + NEW_LINE);             
	    	if(c06!=null)
	    	result.append(" c06		   	 			: " + c06.toString() + NEW_LINE);                 
	    	if(c07!=null)
	    	result.append(" c07		    			: " + c07.toString() + NEW_LINE);                 
	    	if(c08!=null)
	    	result.append(" c08		    			: " + c08.toString() + NEW_LINE);                 
	    	if(c09!=null)
	    	result.append(" c09		    			: " + c09.toString() + NEW_LINE);                 
	    	if(c10!=null)
	    	result.append(" c10		    			: " + c10.toString() + NEW_LINE);                 
	    	if(c11!=null)
	    	result.append(" c11		   	 			: " + c11.toString() + NEW_LINE);                 
	    	if(c12!=null)
	    	result.append(" c12		    			: " + c12.toString() + NEW_LINE);                 
	    	if(c13!=null)
	    	result.append(" c13		    			: " + c13.toString() + NEW_LINE);                 
	    	if(c15!=null)
	    	result.append(" c15		    			: " + c15.toString() + NEW_LINE);                 
	    	if(descripcionProvincia!=null)
	    	result.append(" descripcionProvincia	: " + descripcionProvincia.toString() + NEW_LINE);
	    	if(c14!=null)
	    	result.append(" c14		    			: " + c14.toString() + NEW_LINE);                 
	    	if(descripcionMunicipio!=null)
	    	result.append(" descripcionMunicipio	: " + descripcionMunicipio.toString() + NEW_LINE);
	    	if(c16!=null)
	    	result.append(" c16		    			: " + c16.toString() + NEW_LINE);                 
	    	if(c17!=null)
	    	result.append(" c17		    			: " + c17.toString() + NEW_LINE);                 
	    	if(c18!=null)
	    	result.append(" c18		    			: " + c18.toString() + NEW_LINE);                 
	    	if(TOT_V!=null)
	    	result.append(" TOT_V		    		: " + TOT_V.toString() + NEW_LINE);               
	    	if(TOT_I!=null)
	    	result.append(" TOT_I		    		: " + TOT_I.toString() + NEW_LINE);     	
		    
	    result.append("}");
	    return result.toString();
	}

	public String getC01d1() {
		return c01d1;
	}

	public void setC01d1(String c01d1) {
		this.c01d1 = c01d1;
	}

	public String getC01d2() {
		return c01d2;
	}

	public void setC01d2(String c01d2) {
		this.c01d2 = c01d2;
	}

	public String getC01m1() {
		return c01m1;
	}

	public void setC01m1(String c01m1) {
		this.c01m1 = c01m1;
	}

	public String getC01m2() {
		return c01m2;
	}

	public void setC01m2(String c01m2) {
		this.c01m2 = c01m2;
	}

	public String getC01a1() {
		return c01a1;
	}

	public void setC01a1(String c01a1) {
		this.c01a1 = c01a1;
	}

	public String getC01a2() {
		return c01a2;
	}

	public void setC01a2(String c01a2) {
		this.c01a2 = c01a2;
	}

	public String getC01a3() {
		return c01a3;
	}

	public void setC01a3(String c01a3) {
		this.c01a3 = c01a3;
	}

	public String getC01a4() {
		return c01a4;
	}

	public void setC01a4(String c01a4) {
		this.c01a4 = c01a4;
	}

	public String getC02a() {
		return c02a;
	}

	public void setC02a(String c02a) {
		this.c02a = c02a;
	}

	public String getC02b() {
		return c02b;
	}

	public void setC02b(String c02b) {
		this.c02b = c02b;
	}

	public String getC02c() {
		return c02c;
	}

	public void setC02c(String c02c) {
		this.c02c = c02c;
	}

	public String getC02d() {
		return c02d;
	}

	public void setC02d(String c02d) {
		this.c02d = c02d;
	}

	public String getC03a() {
		return c03a;
	}

	public void setC03a(String c03a) {
		this.c03a = c03a;
	}

	public String getC03b() {
		return c03b;
	}

	public void setC03b(String c03b) {
		this.c03b = c03b;
	}

	public String getC03c() {
		return c03c;
	}

	public void setC03c(String c03c) {
		this.c03c = c03c;
	}

	public String getC03d() {
		return c03d;
	}

	public void setC03d(String c03d) {
		this.c03d = c03d;
	}

	public String getC03e() {
		return c03e;
	}

	public void setC03e(String c03e) {
		this.c03e = c03e;
	}

	public String getC03f() {
		return c03f;
	}

	public void setC03f(String c03f) {
		this.c03f = c03f;
	}

	public String getC03g() {
		return c03g;
	}

	public void setC03g(String c03g) {
		this.c03g = c03g;
	}

	public String getC03h() {
		return c03h;
	}

	public void setC03h(String c03h) {
		this.c03h = c03h;
	}

	public String getC03i() {
		return c03i;
	}

	public void setC03i(String c03i) {
		this.c03i = c03i;
	}

	public String getC03j() {
		return c03j;
	}

	public void setC03j(String c03j) {
		this.c03j = c03j;
	}

	public String getC04a1() {
		return c04a1;
	}

	public void setC04a1(String c04a1) {
		this.c04a1 = c04a1;
	}

	public String getC04a4() {
		return c04a4;
	}

	public void setC04a4(String c04a4) {
		this.c04a4 = c04a4;
	}

	public String getC05a() {
		return c05a;
	}

	public void setC05a(String c05a) {
		this.c05a = c05a;
	}

	public String getC05b() {
		return c05b;
	}

	public void setC05b(String c05b) {
		this.c05b = c05b;
	}
}
