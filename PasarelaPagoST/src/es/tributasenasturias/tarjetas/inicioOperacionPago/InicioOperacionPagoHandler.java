package es.tributasenasturias.tarjetas.inicioOperacionPago;

import java.text.SimpleDateFormat;
import java.util.Date;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.tarjetas.core.PateRecord;
import es.tributasenasturias.tarjetas.core.db.PLPasarelaRepository;
import es.tributasenasturias.tarjetas.core.db.PLPasarelaRepositoryException;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.webservices.types.InicioOperacionPagoResponse;

/**
 * Implementación del inicio de operación de pago.
 * @author crubencvs
 *
 */
public class InicioOperacionPagoHandler implements InicioOperacionPagoHandlerInterface{
	
	private final PateRecord pateRecord;
	protected final InicioOperacionPagoContexto contexto;
	public InicioOperacionPagoHandler(InicioOperacionPagoContexto contexto, PateRecord pateRecord){
		this.contexto= contexto;
		this.pateRecord= pateRecord;
	}

	private String convertFromDate(Date fecha, String formato) {
		if (fecha==null || "".equals(fecha)){
			return null;
		}
		SimpleDateFormat sd = new SimpleDateFormat(formato);
		return sd.format(fecha);
	}
	/* (non-Javadoc)
	 * @see es.tributasenasturias.tarjetas.inicioOperacionPago.InicioOperacionPagoHandler#ejecutar()
	 */
	@Override
	public InicioOperacionPagoResponse ejecutar() throws PasarelaPagoException {
		Logger log = this.contexto.getLog();
		InicioOperacionPagoResponse response= new InicioOperacionPagoResponse();
		boolean continuar=true;
		if (pateRecord!=null){
			if (!datosConsistentes(pateRecord)){
				log.info("Error, los datos de pago que se quiere iniciar no coinciden con los almacenados en la base de datos");
				response.setEsError("S");
				response.setCodigo(Mensajes.getErrorDatosInconsistentes());
				response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
				continuar=false;
			} else {
				//Como el registro que se ha pasado sabemos que está sincronizado, no hay que consultar.
				//Si queremos hacerlo independiente, tendríamos que consultar de nuevo. Por eficiencia no voy a hacerlo
				String estado= pateRecord.getEstado();
				if ("P".equals(estado)){
					log.info("Tributo pagado");
					response.setCodigo(Mensajes.getTributoPagado());
					response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
					response.setPagado("S");
					response.setHash(pateRecord.getHashDatos());
					response.setJustificante(pateRecord.getJustificante());
					response.setNrc(pateRecord.getNrc());
					response.setNumeroOperacion(pateRecord.getNumeroOperacion());
					response.setFechaPago(convertFromDate(pateRecord.getFechaPago(),"yyyy-MM-dd"));
					response.setEsError("N");
					continuar=false;
				} else if ("A".equals(estado)){
					log.info("Tributo anulado");
					response.setCodigo(Mensajes.getTributoAnulado());
					response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
					response.setEsError("S");
					response.setPagado("N");
					continuar=false;
				} else if ("G".equals(estado)){
					log.info("Tributo grabado, no iniciado");
				}
			}
		}
		if (continuar){
			//FIXME:Debería utilizar PLPasarelaRepository para independizarlo del código antiguo, pero implica más cambios.
			//Prefiero hacerlo así por ahora
			/*GestorLlamadasBD gestorBD = BDFactory.newGestorLlamadasBD(pref, idSesion, log);
			ResultadoLlamadaBD resInicio= gestorBD.iniciarOperacionPago(this.contexto.getPeticion());
			if (!resInicio.isError()){
				response.setCodigo(Mensajes.getOk());
				response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
				response.setPagado("N");
				response.setEsError("N");
				response.setHash(resInicio.getDatosPagoBD().getHashDatos());
			} else {
				response.setCodigo(Mensajes.getErrorPeticionBD());
				response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
				response.setEsError("S");
			}*/
			PLPasarelaRepository plRepository= new PLPasarelaRepository(contexto.getPref(), contexto.getLog(), contexto.getIdSesion());
			try {
				PateRecord record= plRepository.inicioOperacionPago(contexto.getPeticion());
				response.setCodigo(Mensajes.getOk());
				response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
				response.setPagado("N");
				response.setEsError("N");
				response.setHash(record.getHashDatos());
			} catch (PLPasarelaRepositoryException pl){
				response.setCodigo(Mensajes.getErrorPeticionBD());
				response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
				response.setEsError("S");
			}
		}
		return response;
	}
	
	/**
	 * Comprueba si los datos de la petición son consistentes con los recuperados de la base de datos
	 * @param pate
	 * @return
	 */
	private boolean datosConsistentes(PateRecord pate){
		boolean consistente=true;
		if (pate!=null){
			consistente &= contexto.getPeticion().getEmisora().equals(pate.getEmisora());
			consistente &= Long.valueOf(contexto.getPeticion().getImporte()).equals(Long.valueOf(pate.getImporte()));
			consistente &= contexto.getPeticion().getNifContribuyente().toUpperCase().equals(pate.getNif().toUpperCase());
			consistente &= contexto.getPeticion().getDatoEspecifico().toUpperCase().equals(pate.getDatoEspecifico().toUpperCase());
		}
		return consistente;
	}
	
}
