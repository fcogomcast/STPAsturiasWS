package es.tributasenasturias.tarjetas.core.sync;

import java.text.SimpleDateFormat;
import java.util.Date;

import es.tributasenasturias.dao.DatosPagoBD;
import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.plataforma.unipay.UniversalPay;
import es.tributasenasturias.plataforma.unipay.UniversalPay.ResultadoSincronizacionUniversalPayBD;
import es.tributasenasturias.tarjetas.core.PateRecord;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.CallContextManager;

/**
 * Clase para sincronizar el estado de pago en base de datos con la
 * entidad UniversalPay.
 * Existe porque aunque la solución correcta sería llamar al servicio de consulta
 * directamente, eso añadiría tiempo de proceso que no queremos.
 * @author crubencvs
 *
 */
class SincronizadorEstadoUniversalPay implements ISincronizadorPagoTarjetaEntidadRemota{

	
	private Preferencias preferencias;
	private Logger log;
	private String idSesion;
	
	public SincronizadorEstadoUniversalPay(Preferencias pref, Logger log, String idSesion){
		this.preferencias= pref;
		this.log= log;
		this.idSesion= idSesion;
		
	}
	
	/**
	 * Transformación de {@link PateRecord} a {@link DatosPagoBD}
	 * @param pateRecord
	 * @return {@link DatosPagoBD} equivalente
	 */
	private DatosPagoBD convertir(PateRecord pateRecord){
		//Necesito que los datos de fecha tengan el formato que se recupera
		//de la base de datos
		return new DatosPagoBD(
										pateRecord.getOrigen(),
										pateRecord.getModalidad(),
										pateRecord.getEmisora(),
										pateRecord.getNumeroOperacion(),
										pateRecord.getImporte(),
										pateRecord.getReferencia(),
										pateRecord.getIdentificacion(),
										pateRecord.getJustificante(),
										pateRecord.getNif(),
										pateRecord.getNombreContribuyente(),
										convertFromDate(pateRecord.getFechaDevengo(),"dd/MM/yyyy"),
										pateRecord.getDatoEspecifico(),
										pateRecord.getExpediente(),
										convertFromDate(pateRecord.getFechaOperacion(),"yyyyMMdd"),
										pateRecord.getEstado(),
										pateRecord.getNrc(),
										convertFromDate(pateRecord.getFechaPago(),"yyyyMMdd"),
										pateRecord.getResultado(),
										pateRecord.getServicio(),
										pateRecord.getNumeroUnico(),
										pateRecord.getNifOperante(),
										convertFromDate(pateRecord.getFechaAnulacion(),"dd/MM/yyyy"),
										pateRecord.getPasarelaPago(),
										pateRecord.getHashDatos(),
										pateRecord.getOperacionEpst(),
										pateRecord.getMedioPago(),
										true
				        );
	}
	/**
	 * Realiza la sincronización de datos entre la base de datos y la entidad remota.
	 * No devuelvo nada concreto porque una vez sincronizado se podrá consultar el estado del
	 * registro en base de datos, a menos que haya terminado con error
	 * @param pateRecord
	 * @throws PasarelaPagoException
	 */
	@Override
	public boolean sincronizar(PateRecord pateRecord) throws PasarelaPagoException{
		CallContext wsContextoLlamada= CallContextManager.newCallContext();
		wsContextoLlamada.setItem(CallContextConstants.PREFERENCIAS, preferencias);
		wsContextoLlamada.setItem(CallContextConstants.LOG_APLICACION, log);
		wsContextoLlamada.setItem(CallContextConstants.ID_SESION, idSesion);
		UniversalPay universalPay= new UniversalPay(wsContextoLlamada);
		ResultadoSincronizacionUniversalPayBD res=universalPay.sincronizarEstadoBDConPlataforma(convertir(pateRecord));
		return !res.isError(); //Si no hubo error, habrá sincronizado correctamente.
	}
	
	/**
	 * 
	 * @param fecha
	 * @param formato
	 * @return
	 */
	private String convertFromDate(Date fecha, String formato) {
		if (fecha==null || "".equals(fecha)){
			return null;
		}
		SimpleDateFormat sd = new SimpleDateFormat(formato);
		return sd.format(fecha);
	} 
}
