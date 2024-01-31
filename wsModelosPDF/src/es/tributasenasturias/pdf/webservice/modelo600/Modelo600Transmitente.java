package es.tributasenasturias.pdf.webservice.modelo600;

import org.w3c.dom.Node;

public class Modelo600Transmitente extends Modelo600Interviniente{

	public Modelo600Transmitente() {}

	public Modelo600Transmitente(Node nodoTR,boolean notarios) {
		super(nodoTR,notarios);
	}
}