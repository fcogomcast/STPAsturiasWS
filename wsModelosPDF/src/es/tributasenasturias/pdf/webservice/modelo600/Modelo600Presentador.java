package es.tributasenasturias.pdf.webservice.modelo600;

import org.w3c.dom.Node;

public class Modelo600Presentador extends Modelo600Interviniente{

	public Modelo600Presentador() {}

	public Modelo600Presentador(Node nodoPR,boolean notarios) {
		super(nodoPR,notarios);
	}
}