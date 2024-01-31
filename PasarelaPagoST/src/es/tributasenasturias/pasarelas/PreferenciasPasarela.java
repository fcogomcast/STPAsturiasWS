package es.tributasenasturias.pasarelas;

/**
 * Clase de la que heredar�n las clases que servir�n de contenedor de preferencias de cada una de las pasarelas.
 * @author crubencvs
 *
 */
public abstract class PreferenciasPasarela  {
	protected String endpointPago;
	protected String endpointConsulta;
	protected String endpointAnulacion;
	protected String idPasarela;
	
	protected PreferenciasPasarela (){}
}
