package es.tributasenasturias.documentopagoutils;

public class CampoRecuperarWS {

	public String estructura = null;
	public String campo = null;	
	public String valor = null;
	
	public CampoRecuperarWS() {
	}
	
	public CampoRecuperarWS(String estructura, String campo) {
		this.estructura = estructura;
		this.campo = campo;
	}

	public CampoRecuperarWS(String estructura, String campo, String valor) {
		super();
		this.estructura = estructura;
		this.campo = campo;
		this.valor = valor;
	}

	public String getEstructura() {
		return estructura;
	}

	public void setEstructura(String estructura) {
		this.estructura = estructura;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
}
