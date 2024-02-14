package es.tributasenasturias.apremios;

import es.tributasenasturias.apremios.bd.DatosException;
import es.tributasenasturias.apremios.bd.GestorBD;
import es.tributasenasturias.apremios.exceptions.EnvioException;
import es.tributasenasturias.apremios.preferencias.Preferencias;
import es.tributasenasturias.apremios.proxy.MediadorAEAT;
import es.tributasenasturias.apremios.proxy.MediadorArchivoDigital;
import es.tributasenasturias.apremios.proxy.ResultadoEnvioPdf;
import es.tributasenasturias.apremios.util.Constantes;
import es.tributasenasturias.log.ILog;

/**
 * Clase principal de implementación
 * @author crubencvs
 *
 */
public class ApremioPdfImpl {

	private Preferencias pref;
	private ILog logger;
	private String idLlamada;
	
	public ApremioPdfImpl(Preferencias pref, String idLlamada, ILog logger) {
		this.pref= pref;
		this.logger= logger;
		this.idLlamada= idLlamada;
	}
	
	public ResultadoEnvioPdf enviarPdf(String claveLiquidacion, String nifDeudor, int idEperValor, String codUsuario) throws EnvioException{
		GestorBD bd = new GestorBD(this.pref, this.idLlamada);
		ResultadoEnvioPdf respuestaEnvio;
		try {
			logger.debug ("  Recuperamos el fichero pdf del archivo digital");
			int idArchivo= bd.getIdArchivo(claveLiquidacion, idEperValor);
			if (idArchivo==0){
				logger.error("No se ha podido recuperar identificador de archivo para esa clave de liquidación");
				respuestaEnvio= new ResultadoEnvioPdf.Builder().setEsError(true).setCodError(Constantes.ERROR_AD).build();
			} else {
				MediadorArchivoDigital archivoDigital= new MediadorArchivoDigital(pref, idLlamada);
				byte[] pdf= archivoDigital.obtenerArchivo(idArchivo,codUsuario);
				if (pdf==null) {
					logger.error("No se ha podido recuperar el contenido del PDF de apremio");
					respuestaEnvio= new ResultadoEnvioPdf.Builder().setEsError(true).setCodError(Constantes.ERROR_AD).build();
				}
				logger.debug("  Enviamos el pdf a la AEAT");
				MediadorAEAT aeat= new MediadorAEAT(pref, idLlamada);
				respuestaEnvio= aeat.enviarPdf(claveLiquidacion, nifDeudor, pdf);
				if (respuestaEnvio.esError()) {
					logger.error("No se ha podido enviar el pdf de apremio a la AEAT");
				}
			}
		} catch (DatosException de) {
			logger.error("Error al recuperar el identificador de archivo para esa clave de liquidación:" + de.getMessage(), de);
			respuestaEnvio= new ResultadoEnvioPdf.Builder().setEsError(true).setCodError(Constantes.ERROR_AD).build();
		}
		return respuestaEnvio;
	}
	
	
}
