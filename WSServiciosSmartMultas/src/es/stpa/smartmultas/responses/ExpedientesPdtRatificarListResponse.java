package es.stpa.smartmultas.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.stpa.smartmultas.entidades.ExpedientesList;

@XmlRootElement(name="ExpedientesPdtRatificarListResponse")
public class ExpedientesPdtRatificarListResponse {

	private ExpedientesList Expedientes;
	
	
	public ExpedientesPdtRatificarListResponse() { }

	public ExpedientesPdtRatificarListResponse(ExpedientesList expedientes) {
		super();
		Expedientes = expedientes;
	}
	

	@XmlElement(name="Expedientes")
	public ExpedientesList getExpedientes() { return Expedientes; }

	public void setExpedientes(ExpedientesList expedientes) { Expedientes = expedientes; }
}
