package es.tributasenasturias.tarjetas.core.sync;

import es.tributasenasturias.conversor.PeticionServicioFactory;
import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.dao.DatosProceso;
import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.pasarelas.SincronizadorBDEntidadRemota;
import es.tributasenasturias.pasarelas.comunicacion.TraductorPasarelaEntidad;
import es.tributasenasturias.tarjetas.core.PateRecord;
import es.tributasenasturias.tarjetas.core.Util;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.utils.Varios;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.CallContextManager;


class SincronizadorEstadoCuenta implements ISincronizadorPagoTarjetaEntidadRemota{

	private Preferencias preferencias;
	private Logger log;
	private String idSesion;
	
	public SincronizadorEstadoCuenta(Preferencias pref, Logger log, String idSesion){
		this.preferencias= pref;
		this.log= log;
		this.idSesion= idSesion;
	}
	
	/**
	 * Convierte de {@link PateRecord} a {@link DatosProceso}
	 * @param pateRecord Registro de bd
	 * @return
	 */
	private DatosProceso convertir (PateRecord pateRecord){
		DatosEntradaServicio peti= PeticionServicioFactory.getPeticionServicioDO (
				pateRecord.getOrigen(),  
				pateRecord.getModalidad(), 
				"", 
				pateRecord.getEmisora(),
				"", 
				"", 
				"", 
				"",
				"", 
				"", 
				pateRecord.getIdentificacion(), 
				pateRecord.getReferencia(),
				pateRecord.getJustificante(), 
				"", 
				"", 
				"",
				"", 
				"", 
				"", 
				pateRecord.getServicio(),
				pateRecord.getNumeroUnico(), 
				"", 
				"", 
				"");
		DatosProceso d= new DatosProceso(peti);
		d.setDatoEspecifico(pateRecord.getDatoEspecifico());
		d.setEstado(pateRecord.getEstado());
		d.setExpediente(pateRecord.getExpediente());
		d.setFechaDevengo(Util.convertFromDate(pateRecord.getFechaDevengo(),"dd/MM/yyyy"));
		d.setJustificante(pateRecord.getJustificante());
		d.setPasarelaPagoPeticion(pateRecord.getPasarelaPago());
		d.setImporte(pateRecord.getImporte());
		d.setNifContribuyente(pateRecord.getNif());
		d.setNifOperante(pateRecord.getNifOperante());
		return d;
	}
	
	/**
	 * Sincroniza el estado de la base de datos con el estado de la entidad de pago
	 * remota, sólo para pagos por cuenta
	 * @param pateRecord
	 * @return
	 * @throws PasarelaPagoException
	 */
	@Override
	public boolean sincronizar(PateRecord pateRecord) throws PasarelaPagoException{
		CallContext wsContextoLlamada= CallContextManager.newCallContext();
		wsContextoLlamada.setItem(CallContextConstants.PREFERENCIAS, preferencias);
		wsContextoLlamada.setItem(CallContextConstants.LOG_APLICACION, log);
		wsContextoLlamada.setItem(CallContextConstants.ID_SESION, idSesion);
		SincronizadorBDEntidadRemota sinc = new SincronizadorBDEntidadRemota(wsContextoLlamada);
		
		DatosProceso datosProceso= convertir(pateRecord);
		//08/06/2023. Tenemos que traducir la pasarela de número a texto.
		TraductorPasarelaEntidad traductor = new TraductorPasarelaEntidad(preferencias.getFicheroPasarelas());
        String pasarela = traductor.getPasarelaFromEntidad(Varios.normalizeNull(datosProceso.getPasarelaPagoPeticion()));
		sinc.sincronizar(pasarela,datosProceso);
		return sinc.huboError();
	}
}
