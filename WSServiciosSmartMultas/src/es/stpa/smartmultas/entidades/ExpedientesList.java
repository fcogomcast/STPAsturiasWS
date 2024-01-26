package es.stpa.smartmultas.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.stpa.smartmultas.responses.ExpedientePdtRatificar;

@XmlRootElement(name = "Expedientes")
public class ExpedientesList {
	
	private List<ExpedientePdtRatificar> Items;

	public ExpedientesList(){ }


	@XmlElement(name = "ExpedientePdtRatificar")
	public List<ExpedientePdtRatificar> getItems() {
		return Items;
	}


	public void setItems(List<ExpedientePdtRatificar> items) {
		Items = items;
	}	
}

