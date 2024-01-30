package es.tributasenasturias.pdf.webservice.modelo600;

import org.w3c.dom.Node;

public class Modelo600SujetoPasivo extends Modelo600Interviniente {

	public Modelo600SujetoPasivo() {}

	public Modelo600SujetoPasivo(Node nodoSP,boolean notarios) {
		super(nodoSP,notarios);
	}
}