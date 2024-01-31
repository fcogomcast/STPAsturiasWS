package es.tributasenasturias.pasarelas.comunicacion;

import es.tributasenasturias.dao.DatosProceso;
import es.tributasenasturias.pasarelas.PreferenciasPasarela;

/**
 * Clase abstracta de la que heredan las que realizan la comunicación con cada pasarela.
 * Cada operación que efectúen sus hijos generará los datos de comunicación {@link DatosComunicacion} pertinentes que se hayan podido generar.
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
	 * Constructor, permitirá incluir los datos del proceso que se utilizarán para la construcción de
	 * la información a intercambiar, y las preferencias de una pasarela concreta.
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
