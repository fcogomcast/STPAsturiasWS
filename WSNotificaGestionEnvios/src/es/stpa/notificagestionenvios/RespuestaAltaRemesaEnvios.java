/**
 * 
 */
package es.stpa.notificagestionenvios;

public class RespuestaAltaRemesaEnvios{
	private String codigoRespuesta;
	private String descripcionRespuesta;
	private ListaIdentificadoresType listaIdentificadores;
	public String getCodigoRespuesta() {
		return codigoRespuesta;
	}
	public void setCodigoRespuesta(String codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}
	public String getDescripcionRespuesta() {
		return descripcionRespuesta;
	}
	public void setDescripcionRespuesta(String descripcionRespuesta) {
		this.descripcionRespuesta = descripcionRespuesta;
	}
	public ListaIdentificadoresType getListaIdentificadores() {
		return listaIdentificadores;
	}
	public void setListaIdentificadores(
			ListaIdentificadoresType listaIdentificadores) {
		this.listaIdentificadores = listaIdentificadores;
	}
	
}