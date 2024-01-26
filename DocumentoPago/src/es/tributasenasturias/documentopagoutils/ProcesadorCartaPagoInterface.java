package es.tributasenasturias.documentopagoutils;

import es.tributasenasturias.Exceptions.ProcesadorCartaPagoException;

public interface ProcesadorCartaPagoInterface {

	/**
	 * Procesa la carta de pago.
	 * @param ideper Id Eper del valor.
	 * @param tipoNoti Tipo notificaci�n.
	 * @param comprimido Indicador de si se devolver� el PDF comprimido.
	 * @param origen  Origen de la petici�n a imprimir carta de pago (Para distinguir el contexto en el que se pide, por ejemplo impresi�n de carta de pago en voluntaria)
	 * @return PDF a devolver, o un indicador de error.
	 * @throws ProcesadorCartaPagoException
	 */
	public String procesar(String ideper, String tipoNoti, String comprimido, String origen) throws ProcesadorCartaPagoException;
}
