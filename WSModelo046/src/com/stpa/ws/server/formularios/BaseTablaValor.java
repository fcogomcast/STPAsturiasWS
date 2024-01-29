package com.stpa.ws.server.formularios;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.stpa.ws.server.util.UtilsComunes;

public class BaseTablaValor  extends BaseElementsForm {
	
	public String valor;

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
	
	// COMO ES PARTE DE UNA LISTA
	public int posicio = -1;


	public Map<String, Object> getMapParametrosParaXml(
			Map<String, String[]> mapParams, String p_cadena) {
		Map<String, Object> mapParamTemp = UtilsComunes.rellenarXMLDocMapaRecursiu(
				this, mapParams);

		Map<String, Object> mapSortida = new HashMap<String, Object>();
		Iterator<String> it = mapParamTemp.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String novaClau = key;
			if (p_cadena!= null){
				novaClau+=p_cadena;
			}
			if (posicio != -1) {
				novaClau += (posicio);
			}
			
			mapSortida.put(novaClau, mapParamTemp.get(key));
		}
		return mapSortida;
	}

	public int getPosicio() {
		return posicio;
	}

	public void setPosicio(int posicio) {
		this.posicio = posicio;
	}
	
	protected static String generarCadena (String key, String p_cadena, int p_varNum){
		String novaClau = key;
		if (p_cadena!= null){
			novaClau+=p_cadena;
		}
		if (p_varNum != -1) {
			novaClau += (p_varNum);
		}
		return novaClau;
	}
}