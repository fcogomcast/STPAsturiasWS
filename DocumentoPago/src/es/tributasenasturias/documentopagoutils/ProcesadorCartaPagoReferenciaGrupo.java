package es.tributasenasturias.documentopagoutils;

import java.io.ByteArrayOutputStream;

import es.tributasenasturias.Exceptions.ProcesadorCartaPagoException;
import es.tributasenasturias.docs.AltaDocumento;
import es.tributasenasturias.docs.CartaPagoReferenciaGrupo;
import es.tributasenasturias.docs.PDFUtils;

public class ProcesadorCartaPagoReferenciaGrupo implements ProcesadorCartaPagoInterface{

	@Override
	public String procesar(String ideper, String tipoNoti, String comprimido, String origen)
			throws ProcesadorCartaPagoException {
		
		String resultado="";
		String pdfPago;
		try
		{
			Logger.info("Entramos a generar el pdf",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			CartaPagoReferenciaGrupo oDocumentoPago = new CartaPagoReferenciaGrupo(String.valueOf(Math.abs(Integer.parseInt(ideper))), tipoNoti);
											
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
		}
		catch (Exception ex)
		{
			throw new ProcesadorCartaPagoException ("Error al procesar la carta de pago con referencias de grupo:"+ ex.getMessage(),ex);
		}
		return resultado;
	}

}
