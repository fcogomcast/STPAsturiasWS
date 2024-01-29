package com.stpa.ws.server.util;

import java.util.Map;

import com.stpa.ws.server.formularios.Constantes;
import com.stpa.ws.server.formularios.CampoRecuperarWS;

public class Oficina {

	public String idOfic = null;
	public String nombreOfic = null;
	public String ofLiqOfic = null;
	public String saleReciboOfic = null;
	public String telefonoOfic = null;
	public String dirCortaOfi = null;
	
	public Oficina(){
	}
	
	public Oficina(String id_ofic, String nombre_ofic, String of_liq_ofic,
			String sale_recibo_ofic, String telefono_ofic, String dir_corta_ofi) {
		this.idOfic = id_ofic;
		this.nombreOfic = nombre_ofic;
		this.ofLiqOfic = of_liq_ofic;
		this.saleReciboOfic = sale_recibo_ofic;
		this.telefonoOfic = telefono_ofic;
		this.dirCortaOfi = dir_corta_ofi;
	}

	/**
	 * setea a partir del resultado de WS
	 * 
	 * 
	 * @param ofic
	 */
	public Oficina(Map<String, String> ofic) {
		if(ofic != null){
			
			this.idOfic = (String)ofic.get(Constantes.OFICINAS_ESTRUCTURA + "." + Constantes.OFICINAS_CAMPO_id_ofic);
			this.nombreOfic = (String)ofic.get(Constantes.OFICINAS_ESTRUCTURA + "." + Constantes.OFICINAS_CAMPO_nombre_ofic);
			this.ofLiqOfic = (String)ofic.get(Constantes.OFICINAS_ESTRUCTURA + "." + Constantes.OFICINAS_CAMPO_of_liq_ofic);
			this.saleReciboOfic = (String)ofic.get(Constantes.OFICINAS_ESTRUCTURA + "." + Constantes.OFICINAS_CAMPO_sale_recibo_ofic);
			this.telefonoOfic = (String)ofic.get(Constantes.OFICINAS_ESTRUCTURA + "." + Constantes.OFICINAS_CAMPO_telefono_ofic);
			this.dirCortaOfi = (String)ofic.get(Constantes.OFICINAS_ESTRUCTURA + "." + Constantes.OFICINAS_CAMPO_dir_corta_ofi);
		}
	}
	
	public String getIdOfic() {
		return idOfic;
	}

	public void setIdOfic(String idOfic) {
		this.idOfic = idOfic;
	}

	public String getNombreOfic() {
		return nombreOfic;
	}

	public void setNombreOfic(String nombreOfic) {
		this.nombreOfic = nombreOfic;
	}

	public String getOfLiqOfic() {
		return ofLiqOfic;
	}

	public void setOfLiqOfic(String ofLiqOfic) {
		this.ofLiqOfic = ofLiqOfic;
	}

	public String getSaleReciboOfic() {
		return saleReciboOfic;
	}

	public void setSaleReciboOfic(String saleReciboOfic) {
		this.saleReciboOfic = saleReciboOfic;
	}

	public String getTelefonoOfic() {
		return telefonoOfic;
	}

	public void setTelefonoOfic(String telefonoOfic) {
		this.telefonoOfic = telefonoOfic;
	}

	public String getDirCortaOfi() {
		return dirCortaOfi;
	}

	public void setDirCortaOfi(String dirCortaOfi) {
		this.dirCortaOfi = dirCortaOfi;
	}
}
