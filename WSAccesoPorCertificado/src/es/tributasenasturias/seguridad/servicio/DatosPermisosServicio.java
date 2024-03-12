package es.tributasenasturias.seguridad.servicio;

import java.util.ArrayList;
import java.util.List;

/**
 * Datos recuperados acerca de los permisos del servicio.
 * Incluyen datos sobre los usuarios asociados en la comprobación de permisos,
 * lo cual es mezclar dos conceptos diferentes, y deberían separarse.
 * @author crubencvs
 *
 */
public class DatosPermisosServicio
{
	//TODO: la autorización y los datos de usuario deberían ser conceptos separados.
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