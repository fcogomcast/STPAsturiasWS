package es.tributasenasturias.seguridad.servicio;

/**
 * Clase interna para los datos de usuario de Tributas
 * @author crubencvs
 *
 */
public class DatosUsuarioTributas
{
	private String usuarioTributas;
	private String passwordTributas;
	private String idFiscal;
	private String idSuborganismo;
	private String suborganismo;
	public String getUsuarioTributas() {
		return usuarioTributas;
	}
	public void setUsuarioTributas(String usuarioTributas) {
		this.usuarioTributas = usuarioTributas;
	}
	public String getPasswordTributas() {
		return passwordTributas;
	}
	public void setPasswordTributas(String passwordTributas) {
		this.passwordTributas = passwordTributas;
	}
	public String getIdFiscal() {
		return idFiscal;
	}
	public void setIdFiscal(String idFiscal) {
		this.idFiscal = idFiscal;
	}
	public String getIdSuborganismo() {
		return idSuborganismo;
	}
	public void setIdSuborganismo(String idSuborganismo) {
		this.idSuborganismo = idSuborganismo;
	}
	public String getSuborganismo() {
		return suborganismo;
	}
	public void setSuborganismo(String suborganismo) {
		this.suborganismo = suborganismo;
	}
	
}