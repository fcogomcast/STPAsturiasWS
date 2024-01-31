package es.tributasenasturias.pasarelas.cajarural;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.tributasenasturias.dao.DatosProceso;
import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.utils.Varios;

/**
 * Realiza las validaciones propias de Caja Rural. Estas validaciones sólo se efectuarán cuando
 * se vayan a realizar las operaciones contra la pasarela.
 * @author crubencvs
 *
 */
public class ValidacionesPropiasCajaRural {
	
	private ValidacionesPropiasCajaRural(){};
	/**
	 * Comprueba si se ha excedido la fecha máxima de pago, comparándola con el margen registrado.
	 * @param fechaPago
	 * @param margenAnulacionMins
	 * @return
	 * @throws ParseException
	 */
	private static boolean fechaExcedida (String fechaPago, String margenAnulacionMins)  throws ParseException
	{
		boolean excedido=false;
		Date fecha_operacion=null;
	
		fecha_operacion = new SimpleDateFormat("yyyyMMdd").parse(fechaPago);
		long diff = Varios.diffDate(fecha_operacion,java.util.Calendar.getInstance().getTime(),"M");
		// Si la diferencia es superior a la registrada en configuración,
		// no se continúa.
		if (Long.parseLong(margenAnulacionMins)< diff)
		{
			excedido=true;
		}
		return excedido;
	}
	/**
	 * Realiza las comprobaciones propias de CajaRural.
	 * @param datosProceso
	 * @param pref
	 * @return
	 * @throws PasarelaPagoException
	 */
	public static boolean validacionesCajaRural(DatosProceso datosProceso, PreferenciasCajaRural pref) throws PasarelaPagoException
	{
		boolean valido=true;
		try
		{
			//Validamos si la fecha se ha excedido.
			if (fechaExcedida(datosProceso.getFechaPago(),pref.getMargenTiempoAnulacion()))
			{
				valido=false;
			}
			return valido;
		}
		catch (ParseException e)
		{
			throw new PasarelaPagoException ("Error en la fecha de pago o en el margen de tiempo de anulación:"+ e.getMessage(),e);
		}
	}
}
