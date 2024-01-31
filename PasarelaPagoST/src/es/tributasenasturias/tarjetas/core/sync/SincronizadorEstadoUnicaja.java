package es.tributasenasturias.tarjetas.core.sync;

import java.text.ParseException;
import java.text.SimpleDateFormat;


import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.pasarelas.unicaja.PreferenciasUnicaja;
import es.tributasenasturias.tarjetas.core.PateRecord;
import es.tributasenasturias.tarjetas.core.db.PLPasarelaRepository;
import es.tributasenasturias.tarjetas.core.db.PLPasarelaRepositoryException;
import es.tributasenasturias.tarjetas.unicajaInterfaz.MediacionUnicajaException;
import es.tributasenasturias.tarjetas.unicajaInterfaz.MediadorUnicaja;
import es.tributasenasturias.tarjetas.unicajaInterfaz.consultaTarjeta.SalidaOpConsultaPagoUnicaja;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;

/**
 * Clase para sincronizar el estado de pago por tarjeta en base de datos con la
 * entidad Unicaja.
 * Existe porque aunque la solución correcta sería llamar al servicio de consulta
 * directamente, eso añadiría tiempo de proceso que no queremos.
 * @author crubencvs
 *
 */
class SincronizadorEstadoUnicaja implements ISincronizadorPagoTarjetaEntidadRemota{

	
	private Preferencias preferencias;
	private Logger log;
	private String idSesion;
	
	public SincronizadorEstadoUnicaja(Preferencias pref, Logger log, String idSesion){
		this.preferencias= pref;
		this.log= log;
		this.idSesion= idSesion;
		
	}
	
	/**
	 * Realiza la sincronización de datos entre la base de datos y la entidad remota.
	 * No devuelvo nada concreto porque una vez sincronizado se podrá consultar el estado del
	 * registro en base de datos, a menos que haya terminado con error
	 * @param pateRecord
	 * @throws PasarelaPagoException
	 */
	@Override
	public boolean sincronizar(PateRecord pateRecord) throws PasarelaPagoException {
		PreferenciasUnicaja pu;
		PLPasarelaRepository repository= new PLPasarelaRepository(preferencias, log, idSesion);
		pu = new PreferenciasUnicaja(preferencias.getFicheroPreferenciasUnicaja());

		MediadorUnicaja med = new MediadorUnicaja(pu.getEndpointGeneracionToken(),pu.getEndpointConsultaTarjeta(), idSesion);
		SalidaOpConsultaPagoUnicaja resultadoConsulta;
		try {
			resultadoConsulta= med.consultarPago(pateRecord.getOperacionEpst());
		} catch (MediacionUnicajaException me){
			throw new PasarelaPagoException("Error en consulta de pago:"+me, me);
		}
		if (resultadoConsulta.isPagado()){
			pateRecord.setEstado("P");
			pateRecord.setNumeroOperacion(resultadoConsulta.getNumeroOperacion());
			pateRecord.setNrc(resultadoConsulta.getNrc());
			SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd_HHmmss");
			//FIXME: Primero, se pierde la hora si usamos el PL actual, y segundo,
			//el campo a actualizar es, por algún motivo, fechaOperacion,
			//no fecha de pago
			try {
				pateRecord.setFechaOperacion(sd.parse(resultadoConsulta.getFechaHora()));
			} catch (ParseException p){
				throw new PasarelaPagoException("Error al transformar la fecha de pago consultada ("+resultadoConsulta.getFechaHora()+"):"+p.getMessage(),p);
			}
			pateRecord.setResultado("0000");
			pateRecord.setXmlServicio(resultadoConsulta.getJsonRecibido());
		} else {
			if (resultadoConsulta.isPagadoSinNrc()){
				log.info("En Unicaja el tributo aparece como pagado, pero no se ha devuelto NRC.");
			}
			pateRecord.setEstado("E");
			//Como no tenemos código de error, pues lo pongo directamente
			pateRecord.setResultado("E0400");
			pateRecord.setXmlServicio(resultadoConsulta.getJsonRecibido());
		}
		try{
			repository.actualizarRegistroPago(pateRecord);
		} catch (PLPasarelaRepositoryException pl){
			throw new PasarelaPagoException("Error en actualización de estado de pago en base de datos:" + pl, pl);
		}
		return true;
	}
}
