/**
 * 
 */
package es.tributasenasturias.conversor;


import es.tributasenasturias.dao.DatosEntradaServicio;

/**
 * @author crubencvs Permite convertir la entrada de datos del servicio a un
 *         objeto interno que será {@link DatosEntradaServicio} el que se maneje,
 *         para tener más facilidad de uso
 */
public final class PeticionServicioFactory {
	private PeticionServicioFactory(){}
	public static DatosEntradaServicio getPeticionServicioDO(String origen,
			String modalidad, String cliente, String emisora, String modelo,
			String entidad, String nifContribuyente,
			String nombreContribuyente, String fechaDevengo,
			String datoEspecifico, String identificacion, String referencia,
			String justificante, String expediente, String importe,
			String tarjeta, String fechaCaducidadTarjeta, String ccc,
			String nifOperante, String aplicacion, String numeroUnico,
			String numeroPeticion, String libre, String mac) {
		return new DatosEntradaServicio(origen, modalidad, cliente, emisora,
				modelo, entidad, nifContribuyente, nombreContribuyente,
				fechaDevengo, datoEspecifico, identificacion, referencia,
				justificante, expediente, importe, tarjeta,
				fechaCaducidadTarjeta, ccc, nifOperante, aplicacion,
				numeroUnico, numeroPeticion, libre, mac);
	}
}
