package es.tributasenasturias.pasarelas.comunicacion;

import es.tributasenasturias.exceptions.PasarelaPagoException;

public interface IOperacionesPasarelas {
	/**
	 * Realiza el pago contra una pasarela de pago de entidad remota.
	 * @throws PasarelaPagoException
	 */
	void realizarPago () throws PasarelaPagoException;
	/**
	 * Realiza la consulta contra una pasarela de pago de una entidad remota.
	 * @throws PasarelaPagoException
	 */
	void realizarConsulta() throws PasarelaPagoException;
	/**
	 * Realiza la anulación contra una pasarela de pago de una entidad remota.
	 * @throws PasarelaPagoException
	 */
	void realizarAnulacion() throws PasarelaPagoException;
}