package es.tributasenasturias.services.ws.wsconsultadoindocumentos;

import java.io.ByteArrayOutputStream;

import es.tributasenasturias.services.ws.wsconsultadoindocumentos.bd.Datos;
import es.tributasenasturias.services.ws.wsconsultadoindocumentos.utils.Base64;
import es.tributasenasturias.services.ws.wsconsultadoindocumentos.utils.PdfComprimidoUtils;
import es.tributasenasturias.services.ws.wsconsultadoindocumentos.utils.Log.Logger;
import es.tributasenasturias.services.ws.wsconsultadoindocumentos.utils.Preferencias.Preferencias;
import es.tributasenasturias.services.ws.wsconsultadoindocumentos.utils.SalidaConsulta;

public class OperacionesDoinDocumentosInternet {

	Datos data = null;
	private Preferencias preferencias = null;
	private Logger logger = null;

	/**
	 * Constructor por defecto
	 */
	public OperacionesDoinDocumentosInternet() {
		// Realizamos inicializaciones
		data = new Datos();
		logger = new Logger();
		logger.trace("Inicio Constructor ConsultaDoinDocumentoInternetImpl ");
		try {
			preferencias = Preferencias.getPreferencias();
		} catch (Exception ex) {

		} finally {
			logger.debug("Fin Constructor ConsultaDoinDocumentoInternetImpl ");
		}

	}

	public SalidaConsulta Ejecutar(String nombre, String tipo,boolean descomprimir) throws Exception {

		SalidaConsulta resultadoConsulta = new SalidaConsulta();
		try {
			logger.debug("Inicio Ejecutar");

			resultadoConsulta = data.GetDocumentoDoin(nombre, tipo);
			
			//Comprobamos la existencia de documento a tratar.
			if (!resultadoConsulta.getError().equals(preferencias.getCodResultadoOK())){
				logger.error("Error obteniendo documento. Código de error: " + resultadoConsulta.getError());
				return resultadoConsulta;
			}

			// Llegados a este punto, tenemos un documento que tratar.
			String docDyD = DecodificarYDescomprimir(resultadoConsulta.getDocumento(), descomprimir);
			SalidaConsulta resultadoConsultaDyD = new SalidaConsulta();
			resultadoConsultaDyD.setDocumento(docDyD);
			resultadoConsultaDyD.setError(resultadoConsulta.getError());
			resultadoConsultaDyD.setResultado(resultadoConsulta.getResultado());
			return resultadoConsultaDyD;
		} catch (Exception ex) {
			logger.error("Error Ejecutar:"+ex.getMessage());
			return new SalidaConsulta();
		} finally {
			logger.debug("Fin Ejecutar");
		}
	}

	public SalidaConsulta altaDocumento(String nombre,String tipo,
							  			String codVerif, String nifSp, String nifPr, String doc,
							  			String tipoDoc, String idSesion,boolean comprimir) throws Exception{

		SalidaConsulta resultadoConsulta = new SalidaConsulta();
		try {
			logger.debug("Inicio altaDocumento");
			resultadoConsulta = data.SetDocumentoDoin(nombre, tipo, codVerif, nifSp, nifPr,doc,tipoDoc,idSesion,comprimir);
			return resultadoConsulta;
		} catch (Exception e) {
			logger.error("Error altaDocumento:"+e.getMessage());
			throw e;
			//return new SalidaConsulta();
		} finally {
			logger.debug("Fin altaDocumento");
		}
	}

	private String DecodificarYDescomprimir(String documento,boolean descomprimir) throws Exception {
		logger.debug("Inicio DecodificarYDescomprimir");
		ByteArrayOutputStream documentoDescomp = new ByteArrayOutputStream();
		try {
			// Descomprimimos
			if (descomprimir) {
				logger.trace("Descomprimiendo documento");
				documentoDescomp = PdfComprimidoUtils.descomprimirPDF(documento);
				logger.trace("Documento descomprimido correctamente");
				return new String(Base64.encode(documentoDescomp.toByteArray()));
			}
			// Si no hay que descomprimir retornamos el documento decodificado
			return documento;
		} catch (Exception e) {
			logger.error("Error DecodificarYDescomprimir:"+e.getMessage());
			throw e;
		} finally {
			logger.debug("Fin DecodificarYDescomprimir");
		}
	}
}