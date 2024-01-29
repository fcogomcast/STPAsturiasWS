package es.tributasenasturias.services.ws.archivodigital.utils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.aowagie.text.pdf.AcroFields;
import com.aowagie.text.pdf.PdfPKCS7;
import com.aowagie.text.pdf.PdfReader;

import es.tributasenasturias.services.ws.archivodigital.exception.ArchivoException;

/** 
 *  Verifica la firma por certificado.
 *  Esta clase no depende de iText-2.1.6,  sino de la versión de @firma, afirma-lib-itext-1.1
 */
public final class VerificadorFirmaCertificado {

	private VerificadorFirmaCertificado(){};
	
	/**
	 * Recupera la fecha de validez del certificado de firma. Si hay más de un 
	 * certificado, recupera la del último.
	 * @param pdfFirmado  contenido del Pdf firmado
	 * @return Fecha en {@link Date}, null si no se encuentra ninguna firma
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public static Date getFechaValidezCertificadoFirma(byte[] pdfFirmado) throws ArchivoException{
		Date fecha=null;
		try {
		PdfReader reader = new PdfReader(pdfFirmado);
        AcroFields af = reader.getAcroFields();
        List<String> signatureNames = af.getSignatureNames();
        if (signatureNames.size()>0) {
        	for (String name: signatureNames){
        		PdfPKCS7 p = af.verifySignature(name);
        		Date caducidadCertificado= p.getSigningCertificate().getNotAfter();
        		if (fecha==null || fecha.compareTo(caducidadCertificado)<0) {
        			fecha=caducidadCertificado;
        		}
        	}
        }
        reader.close();
        return fecha;
		} catch (IOException io) {
			throw new ArchivoException("Error al extraer la fecha de validez del certificado");
		}
	}
}
