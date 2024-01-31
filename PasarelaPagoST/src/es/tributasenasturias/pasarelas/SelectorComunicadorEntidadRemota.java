package es.tributasenasturias.pasarelas;

import es.tributasenasturias.dao.DatosProceso;
import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.pasarelas.bbva.ComunicacionBBVA;
import es.tributasenasturias.pasarelas.bbva.PreferenciasBBVA;
import es.tributasenasturias.pasarelas.caixa.ComunicacionCaixa;
import es.tributasenasturias.pasarelas.caixa.PreferenciasCaixa;
import es.tributasenasturias.pasarelas.cajarural.ComunicacionCajaRural;
import es.tributasenasturias.pasarelas.cajarural.PreferenciasCajaRural;
import es.tributasenasturias.pasarelas.cajastur.ComunicacionCajastur;
import es.tributasenasturias.pasarelas.cajastur.PreferenciasCajastur;
import es.tributasenasturias.pasarelas.comunicacion.ComunicadorPasarela;
import es.tributasenasturias.pasarelas.unicaja.ComunicacionUnicaja;
import es.tributasenasturias.pasarelas.unicaja.PreferenciasUnicaja;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.IContextReader;

/**
 * Selecciona el objeto que se comunicará con cada entidad remota en función de un identificador de pasarela.
 * @author crubencvs
 *
 */
public class SelectorComunicadorEntidadRemota implements IContextReader{

	private CallContext context;
	private Preferencias pref;
	
	/**
	 * Constructor, recuperará del contexto de llamada las preferencias.
	 * @param context Contexto de llamada.
	 * @throws PasarelaPagoException
	 */
	public SelectorComunicadorEntidadRemota(CallContext context) throws PasarelaPagoException
	{
		this.context = context;
		this.pref=(Preferencias) context.get(CallContextConstants.PREFERENCIAS);
		if (pref==null)
		{
			throw new PasarelaPagoException ("No se ha inicializado correctamente el contexto de llamada en "+SelectorComunicadorEntidadRemota.class.getName());
		}
	}
	
	/**
	 * Selecciona la pasarela en función del código que se le pasa.
	 * @param datosProceso
	 * @return
	 * @throws PasarelaPagoException
	 */
	public ComunicadorPasarela seleccionarComunicadorPasarela (String idPasarela, DatosProceso datosProceso) throws PasarelaPagoException
	{
		return getComunicadorPasarela(datosProceso, idPasarela);
	}
	
	/**
	 * Recupera el comunicador de pasarela a utilizar.
	 * @param datosProceso Datos del proceso.
	 * @param idPasarela Identificador de la pasarela
	 * @return Comunicador de pasarela a utilizar para ese identificador de pasarela
	 * @throws PasarelaPagoException
	 */
	private ComunicadorPasarela getComunicadorPasarela(DatosProceso datosProceso, String idPasarela) throws PasarelaPagoException {
		ComunicadorPasarela c=null;
		String idCajastur= pref.getIdPasarelaCajastur();
		String idBBVA = pref.getIdPasarelaBBVA();
		String idCajaRural = pref.getIdPasarelaCajaRural();
		String idLaCaixa = pref.getIdPasarelaLaCaixa();
		String idUnicaja = pref.getIdPasarelaUnicaja();
		//CRUBENCVS 44613 05/04/2022. Soporte para Unicaja
		if (idPasarela.equalsIgnoreCase(idCajastur))
		{
			//Objetos de Cajastur
			PreferenciasPasarela prefCajastur= new PreferenciasCajastur(pref.getFicheroPreferenciasCajastur());
			c = new ComunicacionCajastur(pref.getIdPasarelaCajastur(),datosProceso,prefCajastur, context);
		}
		else if (idPasarela.equalsIgnoreCase(idBBVA))
		{
			//Objetos de BBVA
			PreferenciasPasarela prefBBVA= new PreferenciasBBVA(pref.getFicheroPreferenciasBBVA());
			c = new ComunicacionBBVA(pref.getIdPasarelaBBVA(),datosProceso,prefBBVA, context);
		}
		else if (idPasarela.equalsIgnoreCase(idCajaRural))
		{
			//Objetos de Caja Rural
			PreferenciasPasarela prefCajaRural= new PreferenciasCajaRural(pref.getFicheroPreferenciasCajaRural());
			c = new ComunicacionCajaRural(pref.getIdPasarelaCajaRural(),datosProceso,prefCajaRural, context);
		}
		else if (idLaCaixa.equalsIgnoreCase(idPasarela)){
			PreferenciasPasarela prefCajaLaCaixa= new PreferenciasCaixa(pref.getFicheroPreferenciasLaCaixa());
			c = new ComunicacionCaixa(pref.getIdPasarelaLaCaixa(),datosProceso,prefCajaLaCaixa, context);
		}
		//CRUBENCVS 44613 05/04/2022
		else if (idPasarela.equalsIgnoreCase(idUnicaja))
		{
			PreferenciasPasarela prefUnicaja= new PreferenciasUnicaja(pref.getFicheroPreferenciasUnicaja());
			c = new ComunicacionUnicaja(pref.getIdPasarelaUnicaja(),datosProceso,prefUnicaja, context);
		}
		else {
			throw new PasarelaPagoException ("No se reconoce el identificador de pasarela a la que enviar el mensaje:"+ idPasarela);
		}
		return c;
	}
	
	@Override
	public CallContext getCallContext() {
		return context;
	}

	
	@Override
	public void setCallContext(CallContext ctx) {
		context=ctx;
	}
}
