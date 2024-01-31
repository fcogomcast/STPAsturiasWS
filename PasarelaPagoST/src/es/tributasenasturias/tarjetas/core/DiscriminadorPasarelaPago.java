package es.tributasenasturias.tarjetas.core;


/**
 * Permite discriminar la pasarela de pago que se está utilizando en función de un registro de base de datos
 * Existen una serie de flags que permiten conocer la pasarela escogida
 *  isUniversalPay
 *  isUnicaja
 *  isCuenta
 *  sinDatos: no hay datos en base a los cuales escoger la pasarela
 * @author crubencvs
 *
 */
public class DiscriminadorPasarelaPago {

	private String nombrePasarela;
	
	
	private final String UNIVERSALPAY= "UNIVERSALPAY";
	
	private final String UNICAJA = "UNICAJA";
	
	private final String CUENTA = "CUENTA";
	
	public DiscriminadorPasarelaPago(PateRecord pateRecord) {
		nombrePasarela= getNombrePasarelaUtilizada(pateRecord);
	}
	
	private static boolean isNullOrEmpty(String dato){
		return dato == null  || "".equals(dato);
	}
	/**
	 * Devuelve el nombre la pasarela de pago que se ha usado en los datos de pago que se pasan,
	 * como cadena de texto, en función de los datos del registro.
	 * Los valores son por ahora:
	 * 
	 * UniversalPay: el pago se realizó por UniversalPay
	 * unicaja: el pago se realizó por Unicaja tarjeta.
	 * cuenta: el pago se realizó por cuenta. Dado que esto implicará la funcionalidad
	 * antigua, no necesito definirlo más, aunque se podría.
	 * @param pateRecord
	 * @return Nombre de la pasarela
	 */
	private String getNombrePasarelaUtilizada (PateRecord pateRecord) {
		String operacionEpst= pateRecord.getOperacionEpst();
		String medioPago= pateRecord.getMedioPago();
		String pasarela = pateRecord.getPasarelaPago();
		String nombrePasarela="";
		if ("C".equals(medioPago)){
			nombrePasarela=CUENTA;
		} else if (!isNullOrEmpty(operacionEpst) && "T".equals(medioPago)){
			if ("2048".equals(pasarela)){
				nombrePasarela=UNIVERSALPAY;
			} else if ("2103".equals(pasarela)){
				nombrePasarela=UNICAJA;
			}
		}
		return nombrePasarela;
	}
	
	public boolean isUniversalPay(){
		return "UNIVERSALPAY".equals(nombrePasarela.toUpperCase());
	}
	
	public boolean isUnicajaTarjeta(){
		return "UNICAJA".equals(nombrePasarela.toUpperCase());
	}
	
	public boolean isCuenta(){
		return "CUENTA".equals(nombrePasarela.toUpperCase());
	}
}
