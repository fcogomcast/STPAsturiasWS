package es.tributasenasturias.pasarelas.comunicacion;

import es.tributasenasturias.dao.DatosProceso;
import es.tributasenasturias.pasarelas.PreferenciasPasarela;

/**
 * Clase abstracta de la que heredan las que realizan la comunicaci�n con cada pasarela.
 * Cada operaci�n que efect�en sus hijos generar� los datos de comunicaci�n {@link DatosComunicacion} pertinentes que se hayan podido generar.
 * @author crubencvs
 *
 */
public abstract class ComunicadorPasarela implements IOperacionesPasarelas{

	protected DatosProceso datosProceso;
	protected PreferenciasPasarela prefPasarela;
	protected DatosComunicacion datosComunicacion;
	protected String idPasarela;
	/**
	 * @return the idPasarela
	 */
	public final String getIdPasarela() {
		return idPasarela;
	}
	/**
	 * Constructor, permitir� incluir los datos del proceso que se utilizar�n para la construcci�n de
	 * la informaci�n a intercambiar, y las preferencias de una pasarela concreta.
	 * @param datosProceso
	 * @param pref
	 */
	protected ComunicadorPasarela(DatosProceso datosProceso, PreferenciasPasarela pref)
	{
		this.datosProceso = datosProceso;
		this.prefPasarela = pref;
	}
	/**
	 * @return the datosComunicacion
	 */
	public final DatosComunicacion getDatosComunicacion() {
		return datosComunicacion;
	}
}
