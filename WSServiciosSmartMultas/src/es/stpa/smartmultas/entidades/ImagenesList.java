package es.stpa.smartmultas.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Imagenes")
public class ImagenesList {
	
	@XmlElement(name = "Imagen")
	private List<Imagen> Items;

	public ImagenesList(){ }

	/**
	 * @return the items
	 */
	public List<Imagen> getItems() {
		return Items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<Imagen> items) {
		Items = items;
	}	
}

