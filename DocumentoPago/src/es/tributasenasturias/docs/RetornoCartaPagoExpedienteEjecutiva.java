package es.tributasenasturias.docs;

public class RetornoCartaPagoExpedienteEjecutiva {
	private String documento;
	private boolean esError;
	private String codigoResultado;
	private String descripcionResultado;

	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public boolean isEsError() {
		return esError;
	}
	public void setEsError(boolean esError) {
		this.esError = esError;
	}
	public String getCodigoResultado() {
		return codigoResultado;
	}
	public void setCodigoResultado(String codigoResultado) {
		this.codigoResultado = codigoResultado;
	}
	public String getDescripcionResultado() {
		return descripcionResultado;
	}
	public void setDescripcionResultado(String descripcionResultado) {
		this.descripcionResultado = descripcionResultado;
	}
	
}
