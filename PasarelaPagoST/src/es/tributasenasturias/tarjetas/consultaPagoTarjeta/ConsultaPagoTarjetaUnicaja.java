package es.tributasenasturias.tarjetas.consultaPagoTarjeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.tarjetas.core.PateRecord;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.webservices.types.ResultadoConsultaPagoTarjeta;
import es.tributasenasturias.webservices.types.HistoricoOperacionesPago.AnulacionRealizada;
import es.tributasenasturias.webservices.types.HistoricoOperacionesPago.Operacion;


public class ConsultaPagoTarjetaUnicaja extends ConsultaPagoTarjetaHandler{

	private PateRecord pateRecord;
	//Para ser totalmente independiente del resto de clases, debería consultar el estado en base
	//de datos, pero para no realizar operaciones de más y dado que es una lógica
	//sencilla, espera que le pasen el registro ya consultado
	public ConsultaPagoTarjetaUnicaja(ConsultaPagoTarjetaContexto contexto, PateRecord pateRecord){
		super(contexto);
		this.pateRecord= pateRecord;
	}
	
	private String convertFromDate(Date fecha, String formato) {
		if (fecha==null || "".equals(fecha)){
			return null;
		}
		SimpleDateFormat sd = new SimpleDateFormat(formato);
		return sd.format(fecha);
	}
	/**
	 * Devuelve una operación OK con la operación de pago.
	 * @param operacionEpst
	 * @param fechaPago
	 * @return
	 */
	private Operacion operacionOK(String operacionEpst, Date fechaPago){
		Operacion op = new Operacion();
		op.setFecha(convertFromDate(fechaPago,"yyyy-MM-dd HH:mm:ss"));
		op.setIdentificador(operacionEpst);
		op.setResultado("OK");
		op.setAnulaciones(new ArrayList<AnulacionRealizada>());
		return op;
	}
	/**
	 * Devuelve una operación KO
	 * @return
	 */
	private Operacion operacionKO(String operacionEpst, Date fechaOperacion){
		Operacion op = new Operacion();
		op.setFecha(convertFromDate(fechaOperacion,"yyyy-MM-dd HH:mm:ss"));
		op.setIdentificador(operacionEpst);
		op.setResultado("KO");
		op.setAnulaciones(new ArrayList<AnulacionRealizada>());
		return op;
	}
	@Override
	public ResultadoConsultaPagoTarjeta consultar()
			throws PasarelaPagoException {
		
		ResultadoConsultaPagoTarjeta result = new ResultadoConsultaPagoTarjeta(this.consultaPagoContexto.getPeticion());
		Logger log=consultaPagoContexto.getLog();
		if (pateRecord==null){ //Nunca debería serlo
			result.getRespuesta().setMessage(Mensajes.getTributoNoPagado());
			result.getOperaciones().getListaOperaciones().add(new Operacion());
			log.info ("Tributo no pagado.");
		}
		if ("P".equals(pateRecord.getEstado())){
			//Pagado
			result.getRespuesta().setMessage(Mensajes.getTributoPagado());
			result.getPeticion().setNumero_autoliquidacion(pateRecord.getJustificante());
			result.getRespuesta().setOperacion(pateRecord.getNrc());
			result.getRespuesta().setFechaPago(convertFromDate(pateRecord.getFechaPago(),"yyyy-MM-dd"));
			result.getOperaciones().getListaOperaciones().add(operacionOK(pateRecord.getOperacionEpst(),pateRecord.getFechaPago()));
			log.info("Tributo pagado.");
		} else if ("A".equals(pateRecord.getEstado())){
			result.getRespuesta().setMessage(Mensajes.getTributoAnulado());
			result.getOperaciones().getListaOperaciones().add(operacionKO(pateRecord.getOperacionEpst(),pateRecord.getFechaOperacion()));
			log.info ("Tributo anulado.");
		} else {
			result.getRespuesta().setMessage(Mensajes.getTributoNoPagado());
			result.getOperaciones().getListaOperaciones().add(operacionKO(pateRecord.getOperacionEpst(),pateRecord.getFechaOperacion()));
			log.info ("Tributo no pagado.");
		}
		return result;
	}

}
