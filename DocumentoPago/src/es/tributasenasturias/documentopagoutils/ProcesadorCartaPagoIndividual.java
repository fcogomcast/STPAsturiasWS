package es.tributasenasturias.documentopagoutils;

import java.io.ByteArrayOutputStream;

import es.tributasenasturias.Exceptions.ProcesadorCartaPagoException;
import es.tributasenasturias.Exceptions.ValidacionException;
import es.tributasenasturias.docs.AltaDocumento;
import es.tributasenasturias.docs.CartaPago;
import es.tributasenasturias.docs.GeneradorCodigoBarras;
import es.tributasenasturias.docs.PDFUtils;
import es.tributasenasturias.documentopagoutils.ValidadorEstadoValor.TipoValor;

/**
 * Procesa la carta de pago no agrupada.
 * @author crubencvs
 *
 */
public class ProcesadorCartaPagoIndividual implements ProcesadorCartaPagoInterface{
	
	@Override
	public String procesar(String ideper, String tipoNoti, String comprimido, String origen) throws ProcesadorCartaPagoException{
		
		String resultado="";
		ValidadorEstadoValor val = new ValidadorEstadoValor();
		try
		{
		if (val.esValido(ideper, TipoValor.CARTA_PAGO_NO_AGRUPADA))
		{
			//Seguimos con el proceso.
			String pdfPago;
			try {		
				
				Logger.info("Entramos a generar el pdf",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				CartaPago oDocumentoPago = new CartaPago(ideper, tipoNoti, origen);
												
				pdfPago = PDFUtils.generarPdf(oDocumentoPago);
				
				if (pdfPago.equals(null) || pdfPago.equalsIgnoreCase(""))
				{	
					resultado = "KO";
					Logger.debug("No se ha podido generar el pdf" ,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				}
				else
				{	
					resultado = pdfPago;
					
					/*
					 * Alta documento
					 */
					
					Logger.debug("Se va a dar de alta el documento:" ,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
					
					AltaDocumento docu = new AltaDocumento ();								
					docu.setDocumento("R", oDocumentoPago.getRefCobro(), null, oDocumentoPago.getNifSp(),oDocumentoPago.getNifSp(), pdfPago, "PDF");
					
					
					Logger.debug("Finalizado el alta de documento:" + ideper,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
					
				}																						
				
				Logger.debug("RESULTADO:" +resultado,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);

				Logger.debug("Se comprueba si es necesario comprimir el pdf",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				
				if (!(comprimido==null))
				{
					Logger.debug("Se comprime el pdf",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
					if (comprimido.equalsIgnoreCase("S") && !resultado.equalsIgnoreCase("KO")) {
						byte[] docDecodificado = Base64.decode(pdfPago.toCharArray());
						String documentzippeado = "";
						ByteArrayOutputStream resulByteArray = new ByteArrayOutputStream();
						resulByteArray.write(docDecodificado);
						documentzippeado = PdfComprimidoUtils.comprimirPDF(resulByteArray);
						resultado = documentzippeado; 
					}
					
				}									
				Logger.debug("Se finaliza correctamente.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				
			} catch (Exception e) {							
				Logger.error("Error al procesar la carta de pago individual:"+ e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);								
				resultado ="KO";
				Logger.debug("RESULTADO:" +resultado,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				throw new ProcesadorCartaPagoException ("Error al procesar la carta de pago individual:"+ e.getMessage(),e);
			}	
		}
		else
		{
			resultado = "KO";
			Logger.debug("El valor para la carta de pago individual no está en el estado correcto:" + ideper,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
		}
		catch (ValidacionException e)
		{
			Logger.error("Error de validación al procesar la carta de pago individual:"+ e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			throw new ProcesadorCartaPagoException ("Error al procesar la carta de pago individual:"+ e.getMessage(),e);
		}
		return resultado;
	}
}
