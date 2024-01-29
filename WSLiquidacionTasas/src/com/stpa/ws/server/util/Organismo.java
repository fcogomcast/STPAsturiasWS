package com.stpa.ws.server.util;
import java.util.Map;

import com.stpa.ws.server.formularios.Constantes;



public class Organismo {

	public String nombreOrga = null;
	public String nombreLOrga = null;
	public String nifOrga = null;
	public String codEemi = null;
	public String directorOrga = null;
	public String maxFraccOrga = null;
	public String minFraccionarOrga = null;
	public String minFraccionOrga = null;
	
	
	public Organismo() {
	}


	public Organismo(String nombreOrga, String nombreLOrga, String nifOrga,
			String codEemi, String directorOrga, String maxFraccOrga,
			String minFraccionarOrga, String minFraccionOrga) {
		this.nombreOrga = nombreOrga;
		this.nombreLOrga = nombreLOrga;
		this.nifOrga = nifOrga;
		this.codEemi = codEemi;
		this.directorOrga = directorOrga;
		this.maxFraccOrga = maxFraccOrga;
		this.minFraccionarOrga = minFraccionarOrga;
		this.minFraccionOrga = minFraccionOrga;
	}
	
	public Organismo(Map<String, String> organismo) {
		if(organismo != null) {
			this.nombreOrga = (String)organismo.get(Constantes.ORGANISMO_ESTRUCTURA + "." + Constantes.ORGANISMO_CAMPO_nombre_orga);
			this.nombreLOrga = (String)organismo.get(Constantes.ORGANISMO_ESTRUCTURA + "." + Constantes.ORGANISMO_CAMPO_nombre_l_orga);
			this.nifOrga = (String)organismo.get(Constantes.ORGANISMO_ESTRUCTURA + "." + Constantes.ORGANISMO_CAMPO_nif_orga);
			this.codEemi = (String)organismo.get(Constantes.ORGANISMO_ESTRUCTURA + "." + Constantes.ORGANISMO_CAMPO_cod_eemi);
			this.directorOrga = (String)organismo.get(Constantes.ORGANISMO_ESTRUCTURA + "." + Constantes.ORGANISMO_CAMPO_director_orgaa);
			this.maxFraccOrga = (String)organismo.get(Constantes.ORGANISMO_ESTRUCTURA + "." + Constantes.ORGANISMO_CAMPO_max_fracc_orga);
			this.minFraccionarOrga = (String)organismo.get(Constantes.ORGANISMO_ESTRUCTURA + "." + Constantes.ORGANISMO_CAMPO_min_fraccionar_orga);
			this.minFraccionOrga = (String)organismo.get(Constantes.ORGANISMO_ESTRUCTURA + "." + Constantes.ORGANISMO_CAMPO_min_fraccion_orga);
		}
	}

	public String getNombreOrga() {
		return nombreOrga;
	}


	public void setNombreOrga(String nombreOrga) {
		this.nombreOrga = nombreOrga;
	}


	public String getNombreLOrga() {
		return nombreLOrga;
	}


	public void setNombreLOrga(String nombreLOrga) {
		this.nombreLOrga = nombreLOrga;
	}


	public String getNifOrga() {
		return nifOrga;
	}


	public void setNifOrga(String nifOrga) {
		this.nifOrga = nifOrga;
	}


	public String getCodEemi() {
		return codEemi;
	}


	public void setCodEemi(String codEemi) {
		this.codEemi = codEemi;
	}


	public String getDirectorOrga() {
		return directorOrga;
	}


	public void setDirectorOrga(String directorOrga) {
		this.directorOrga = directorOrga;
	}


	public String getMaxFraccOrga() {
		return maxFraccOrga;
	}


	public void setMaxFraccOrga(String maxFraccOrga) {
		this.maxFraccOrga = maxFraccOrga;
	}


	public String getMinFraccionarOrga() {
		return minFraccionarOrga;
	}


	public void setMinFraccionarOrga(String minFraccionarOrga) {
		this.minFraccionarOrga = minFraccionarOrga;
	}


	public String getMinFraccionOrga() {
		return minFraccionOrga;
	}


	public void setMinFraccionOrga(String minFraccionOrga) {
		this.minFraccionOrga = minFraccionOrga;
	}
}
