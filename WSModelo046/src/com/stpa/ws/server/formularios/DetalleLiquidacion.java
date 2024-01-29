package com.stpa.ws.server.formularios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.stpa.ws.server.util.UtilsComunes;

public class DetalleLiquidacion extends BaseTablaValor {

	public String d01_1; // tarifa
	public String d02_1; // descripcion
	public String d03_1; // valores
	public String d04_1; // importe

	public static Map<String, List<String>> generarSortidaPdf(String p_cadena, int posicio) {

		List<String> pdf = new ArrayList<String>();
		List<String> xml = new ArrayList<String>();

		Map<String, List<String>> sortida = new HashMap<String, List<String>>();
		sortida.put(MAP, xml);
		sortida.put(PDF, pdf);

		xml.add(generarCadena("d01_1", p_cadena, posicio));
		pdf.add("d01_" + (posicio + 1));

		xml.add(generarCadena("d02_1", p_cadena, posicio));
		pdf.add("d02_" + (posicio + 1));

		xml.add(generarCadena("d03_1", p_cadena, posicio));
		pdf.add("d03_" + (posicio + 1));

		xml.add(generarCadena("d04_1", p_cadena, posicio));
		pdf.add("d04_" + (posicio + 1));

		return sortida;
	}

	public Map<String, Object> getMapParametrosParaXml(Map<String, String[]> mapParams, String p_cadena) {
		Map<String, Object> mapParamTemp = UtilsComunes.rellenarXMLDocMapaRecursiu(this, mapParams);

		Map<String, Object> mapSortida = new HashMap<String, Object>();
		Iterator<String> it = mapParamTemp.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String novaClau = key;
			if (p_cadena != null) {
				novaClau += p_cadena;
			}
			if (posicio != -1) {
				novaClau += (posicio - 1);
			}

			mapSortida.put(novaClau, mapParamTemp.get(key));

		}
		return mapSortida;
	}

	public String getD01_1() {
		return d01_1;
	}

	public void setD01_1(String d01_1) {
		this.d01_1 = d01_1;
	}

	public String getD02_1() {
		return d02_1;
	}

	public void setD02_1(String d02_1) {
		this.d02_1 = d02_1;
	}

	public String getD03_1() {
		return d03_1;
	}

	public void setD03_1(String d03_1) {
		this.d03_1 = d03_1;
	}

	public String getD04_1() {
		return d04_1;
	}

	public void setD04_1(String d04_1) {
		this.d04_1 = d04_1;
	}
}

