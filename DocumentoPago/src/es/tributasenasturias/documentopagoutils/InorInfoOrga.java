package es.tributasenasturias.documentopagoutils;

import java.util.Map;

import es.tributasenasturias.documentopagoutils.Constantes;



public class InorInfoOrga {
	
	
	public String descrInor = null;
	public String codInor = null;
	public String boolInor = null;
	public String fechaInor = null;
	public String numInor = null;

	
	
	public InorInfoOrga() {
	}



	public InorInfoOrga(String descrInor, String codInor, String boolInor,
			String fechaInor, String numInor) {
		this.descrInor = descrInor;
		this.codInor = codInor;
		this.boolInor = boolInor;
		this.fechaInor = fechaInor;
		this.numInor = numInor;
	}

	public InorInfoOrga(Map<String, String> inorInfoOrga) {
		if(inorInfoOrga != null){
			this.descrInor = (String)inorInfoOrga.get(Constantes.INOR_INFO_ORGA_ESTRUCTURA + "." + Constantes.INOR_INFO_ORGA_CAMPO_descr_inor);
			this.codInor = (String)inorInfoOrga.get(Constantes.INOR_INFO_ORGA_ESTRUCTURA + "." + Constantes.INOR_INFO_ORGA_CAMPO_cod_inor);
			this.boolInor =(String)inorInfoOrga.get(Constantes.INOR_INFO_ORGA_ESTRUCTURA + "." + Constantes.INOR_INFO_ORGA_CAMPO_bool_inor);
			this.fechaInor = (String)inorInfoOrga.get(Constantes.INOR_INFO_ORGA_ESTRUCTURA + "." + Constantes.INOR_INFO_ORGA_CAMPO_fecha_inor);
			this.numInor = (String)inorInfoOrga.get(Constantes.INOR_INFO_ORGA_ESTRUCTURA + "." + Constantes.INOR_INFO_ORGA_CAMPO_num_inor);
		}
	}

	public String getDescrInor() {
		return descrInor;
	}

	public void setDescrInor(String descrInor) {
		this.descrInor = descrInor;
	}

	public String getCodInor() {
		return codInor;
	}

	public void setCodInor(String codInor) {
		this.codInor = codInor;
	}

	public String getBoolInor() {
		return boolInor;
	}

	public void setBoolInor(String boolInor) {
		this.boolInor = boolInor;
	}

	public String getFechaInor() {
		return fechaInor;
	}

	public void setFechaInor(String fechaInor) {
		this.fechaInor = fechaInor;
	}

	public String getNumInor() {
		return numInor;
	}
	
	public void setNumInor(String numInor) {
		this.numInor = numInor;
	}
}
