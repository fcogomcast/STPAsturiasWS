package es.tributasenasturias.seguridad.servicio;

import java.util.ArrayList;
import java.util.List;


/**
 * Clase que contiene información sobre permisos del certificado en nuestros sistemas.
 * Si el certificado es válido.
 * Si el certificado está autorizado para la operación en nuestros sistemas
 * Los datos del usuario en nuestro sistema.
 * @author crubencvs
 *
 */
public class InfoPermisosCertificado {
	private boolean certificadoValido;
	private boolean certificadoAutorizado;
	private InfoCertificado infoCertificado;
	private List<DatosUsuarioTributas> listaUsuarios= new ArrayList<DatosUsuarioTributas>();
	/**
	 * Recupera la lista de usuarios asociados al certificado en Tributas, si existe.
	 * @return
	 */
	public List<DatosUsuarioTributas> getListaUsuarios() {
		return listaUsuarios;
	}
	/**
	 * Indica la lista de usuarios de Tributas asociados al certificado
	 * @param listaUsuarios
	 */
	public void setListaUsuarios(List<DatosUsuarioTributas> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}
	/**
	 * Indica si el certificado es válido (pero no indica que tenga permisos)
	 * @return
	 */
	public boolean isCertificadoValido() {
		return certificadoValido;
	}
	protected void setCertificadoValido(boolean certificadoValido) {
		this.certificadoValido = certificadoValido;
	}
	/**
	 * Indica que el certificado es válido Y tiene permisos de ejecución del servicio sobre
	 * el que se consultó en nuestro sistema.
	 * @return
	 */
	public boolean isCertificadoAutorizado() {
		return certificadoAutorizado;
	}
	protected void setCertificadoAutorizado(boolean certificadoAutorizado) {
		this.certificadoAutorizado = certificadoAutorizado;
	}
	public InfoCertificado getInfoCertificado() {
		return infoCertificado;
	}
	public void setInfoCertificado(InfoCertificado infoCertificado) {
		this.infoCertificado = infoCertificado;
	}
}
