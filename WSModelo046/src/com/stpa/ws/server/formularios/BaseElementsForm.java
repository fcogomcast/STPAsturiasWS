package com.stpa.ws.server.formularios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stpa.ws.server.util.UtilsComunes;

public class BaseElementsForm extends GenericModeloFormBean implements
		IModeloFormBean {
		
	public static final String COMOD = "#";
	public static final String PDF = "PDF";
	public static final String MAP = "MAP";
	
	public List<String> validar(String p_remplazo) {

		List<String> llistaErrors = new ArrayList<String>();
		List<String> llistaErrorsSortida = new ArrayList<String>();
		for (String cadena : llistaErrors) {
			String cad = "";
			if (p_remplazo != null) {
				cad += p_remplazo;
			}
			llistaErrorsSortida.add(cadena.replace(COMOD, cad).replace("..", "."));
		}
		return llistaErrorsSortida;

	}

	public Map<String, Object> getMapParametrosParaXml(
			Map<String, String[]> mapParams, String p_cadena) {
		Map<String, Object> mapParamTemp = UtilsComunes.rellenarXMLDocMapaRecursiu(
				this, mapParams);
		Map<String, Object> mapSortida = new HashMap<String, Object>();
		Iterator<String> it = mapParamTemp.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String novaClau = key;
			if (p_cadena!= null)
				novaClau+=p_cadena;
			mapSortida.put(novaClau, mapParamTemp.get(key));
		}
		return mapSortida;
	}


	public String getImporte() {
		//  Auto-generated method stub
		return null;
	}


	public void assignarValoresDesplegable(Map<String, Object> param, String propiedad, String nombre, Set<CodiDescBean> lista){
		UtilsComunes.asignarCampoDesplegable(param, propiedad+nombre, propiedad+nombre, lista);
	}

	public boolean isValid() {
		//  Auto-generated method stub
		return false;
	}

	public void rellenarCamposAltaDocumento() {
		//  Auto-generated method stub
	}

	public Map<String, Object> getMapParametrosParaXml(
			Map<String, String[]> mapParams) {
		//  Auto-generated method stub
		return null;
	}
	protected static String generarCadena (String key, String p_cadena){
		String novaClau = key;
		if (p_cadena!= null){
			novaClau+=p_cadena;
		}
		return novaClau;
	}
}
