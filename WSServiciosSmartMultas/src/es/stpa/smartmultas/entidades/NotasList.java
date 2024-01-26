package es.stpa.smartmultas.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Notas")
public class NotasList {
	
	@XmlElement(name = "Nota")
	private List<Nota> Items;

	public NotasList(){ }

	/**
	 * @return the items
	 */
	public List<Nota> getItems() {
		return Items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<Nota> items) {
		Items = items;
	}	
}

