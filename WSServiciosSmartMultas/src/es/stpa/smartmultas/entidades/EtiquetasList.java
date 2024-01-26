package es.stpa.smartmultas.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Etiquetas")
public class EtiquetasList {
	
	private List<EtiquetasBoletin> Items;

	public EtiquetasList(){ }

	@XmlElement(name = "EtiquetasBoletin")
	public List<EtiquetasBoletin> getItems() {
		return Items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<EtiquetasBoletin> items) {
		Items = items;
	}

	public EtiquetasList(List<EtiquetasBoletin> items) {
		super();
		Items = items;
	}	
}

