package es.tributasenasturias.seguridad.servicio;

import java.util.ArrayList;
import java.util.List;

/**
 * Datos recuperados acerca de los permisos del servicio.
 * Incluyen datos sobre los usuarios asociados en la comprobaci�n de permisos,
 * lo cual es mezclar dos conceptos diferentes, y deber�an separarse.
 * @author crubencvs
 *
 */
public class DatosPermisosServicio
{
	//TODO: la autorizaci�n y los datos de usuario deber�an ser conceptos separados.
	public static enum AutorizacionServicio {AUTORIZADO,NO_AUTORIZADO,ERROR};
	private AutorizacionServicio autorizacion;
	private List<DatosUsuarioTributas> listaUsuarios= new ArrayList<DatosUsuarioTributas>();
	protected void setAutorizacion(AutorizacionServicio autorizacion) {
		this.autorizacion = autorizacion;
	}
	public AutorizacionServicio getAutorizacion() {
		return autorizacion;
	}
	public List<DatosUsuarioTributas> getListaUsuarios() {
		return listaUsuarios;
	}
	public void setListaUsuarios(List<DatosUsuarioTributas> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}
	
}