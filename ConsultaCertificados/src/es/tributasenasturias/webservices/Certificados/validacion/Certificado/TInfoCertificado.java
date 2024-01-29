package es.tributasenasturias.webservices.Certificados.validacion.Certificado;

/**
 * Tipo de datos de información de certificado. Se rellenará al comprobar si el certificado es válido.
 * @author crubencvs
 *
 */
public class TInfoCertificado {
	private String nifNie=null;
	private String razonSocial=null;
	private String cif=null;
	public String getNifNie() {
		return nifNie;
	}
	public void setNifNie(String nifNie) {
		this.nifNie = nifNie;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getCif() {
		return cif;
	}
	public void setCif(String cif) {
		this.cif = cif;
	}
}
