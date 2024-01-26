package es.stpa.smartmultas.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Propiedades")
public class PropiedadesList {
	
	@XmlElement(name = "Propiedad")
	private List<Propiedad> Items;

	public PropiedadesList(){ }

	/**
	 * @return the items
	 */
	public List<Propiedad> getItems() {
		return Items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<Propiedad> items) {
		Items = items;
	}	
}

