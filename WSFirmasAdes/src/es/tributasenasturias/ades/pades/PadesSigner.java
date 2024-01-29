package es.tributasenasturias.ades.pades;

import java.io.IOException;
import java.security.KeyStore.PrivateKeyEntry;
import java.util.Properties;

import es.gob.afirma.core.AOException;
import es.gob.afirma.core.signers.AOSignConstants;
import es.gob.afirma.signers.pades.AOPDFSigner;
import es.tributasenasturias.ades.AlgoritmoHashType;
import es.tributasenasturias.ades.FormatoPadesType;
import es.tributasenasturias.ades.PoliticaFirmaPadesType;
import es.tributasenasturias.ades.certificados.AlmacenController;
import es.tributasenasturias.ades.exceptions.AlmacenException;
import es.tributasenasturias.ades.exceptions.PadesException;
import es.tributasenasturias.ades.exceptions.UpgradeException;
import es.tributasenasturias.ades.preferencias.Preferencias;
import es.tributasenasturias.ades.soap.SoapClientHandler;
import es.tributasenasturias.ades.upgrade.UpgraderFirma;
import es.tributasenasturias.log.ILog;

/**
 * Clase para implementar la firma mediante Pades
 * @author crubencvs
 *
 */
public class PadesSigner {

	private final Preferencias pref;
	private final ILog logger;
	private final String idLlamada;

	private PadesSigner(Preferencias pref, ILog logger, String idLlamada)
	{this.pref= pref;
	 this.logger=logger;
	 this.idLlamada= idLlamada;
	}
	/**
	 * Única forma de construir un nuevo objeto
	 * @param pref Preferencias del servicio 
	 * @param logger Objeto para hacer log
	 * @param idLlamada Identificador de la llamada a la que se da servicio
	 * @return 
	 */
	public static PadesSigner newInstance(Preferencias pref, ILog logger, String idLlamada) throws PadesException {
		if (pref==null || logger==null) {
			throw new PadesException ("Error de configuración del servicio");
		}
		return new PadesSigner(pref, logger, idLlamada);
	}
	
	private void validaEntrada(FormatoPadesType formatoPades, AlgoritmoHashType algoritmoHash, PoliticaFirmaPadesType politicaFirmaPades ) throws PadesException{
		if (formatoPades==null) {
			throw new PadesException("El formato de firma indicado es nulo o no es válido");
		}
		if (algoritmoHash==null) {
			throw new PadesException("No se ha indicado un algoritmo de hash, o el valor no es válido");
		}
		if (FormatoPadesType.EPES.equals(formatoPades) && politicaFirmaPades==null) {
			throw new PadesException("No se puede realizar firma EPES sin indicar política de firma.");
		}
	}
	
	/**
	 * Realiza la firma de un PDF.
	 * @param datosAFirmar Contenido del PDF. Se considera que está sin firmar.
	 * @param formatoPades Formato de firma como {@link FormatoPadesType}
	 * @param algoritmo Algoritmo de digest de firma. Siempre RSA
	 * @param politicaFirma Política de firma, para EPES
	 * @return Contenido del PDF firmado.
	 * @throws PadesException
	 */
	public byte[] procesaFirma(byte[] datosAFirmar, FormatoPadesType formatoPades, AlgoritmoHashType algoritmo, PoliticaFirmaPadesType politicaFirma) throws PadesException{
		
		validaEntrada(formatoPades, algoritmo,politicaFirma);
		logger.debug("Formato firma:" + formatoPades.value() + "  ,Algoritmo:" + algoritmo.value());
		switch (formatoPades) {
		case BES:
				return signBES(datosAFirmar, algoritmo);
		case EPES:
				return signEPES(datosAFirmar, algoritmo, politicaFirma);
		case LTV:
			byte[] pdfFirmado;
			if (politicaFirma!=null) {
				pdfFirmado= signEPES(datosAFirmar, algoritmo, politicaFirma);
			}else {
				pdfFirmado= signBES(datosAFirmar, algoritmo);
			}
			UpgraderFirma upf= UpgraderFirma.newInstance(pref, logger, idLlamada,new SoapClientHandler(idLlamada));
			try {
				
				pdfFirmado=upf.upgradePades(pdfFirmado, formatoPades.value());
				if (upf.isRespuestaOk()){
					return pdfFirmado; 
				}
				else  {
					throw new PadesException("Error en la actualización de firma:" + upf.getTextoResultadoUpgrade());
				}
			} catch (UpgradeException ue) {
				throw new PadesException ("Error en la actualización de firma Pades:"+ ue.getMessage(),ue);
			}
		default:
				throw new PadesException ("No se admite el formato de firma indicado:" + formatoPades.value());
		}
	}
	
	/* INIPETITRIBUTAS-5 ENAVARRO ** 01/04/2020 ** Compulsa */
	/**
	 * Actualiza la firma de un PDF habilitándole LTV.
	 * @param datosAFirmar Contenido del PDF. Se considera que está sin firmar.
	 * @param formatoPades Formato de firma como {@link FormatoPadesType}
	 * @param algoritmo Algoritmo de digest de firma. Siempre RSA
	 * @param politicaFirma Política de firma, para EPES
	 * @return Contenido del PDF firmado.
	 * @throws PadesException
	 */
	public byte[] procesaFirmaUpgrade(byte[] datosAFirmar, FormatoPadesType formatoPades, AlgoritmoHashType algoritmo, PoliticaFirmaPadesType politicaFirma) throws PadesException{
		
		validaEntrada(formatoPades, algoritmo,politicaFirma);
		logger.debug("Formato firma:" + formatoPades.value() + "  ,Algoritmo:" + algoritmo.value());
		switch (formatoPades) {
		case BES:
				return signBES(datosAFirmar, algoritmo);
		case EPES:
				return signEPES(datosAFirmar, algoritmo, politicaFirma);
		case LTV:
			UpgraderFirma upf= UpgraderFirma.newInstance(pref, logger, idLlamada,new SoapClientHandler(idLlamada));
			try {
				
				datosAFirmar=upf.upgradePades(datosAFirmar, formatoPades.value());
				if (upf.isRespuestaOk()){
					return datosAFirmar; 
				}
				else  {
					throw new PadesException("Error en la actualización de firma:" + upf.getTextoResultadoUpgrade());
				}
			} catch (UpgradeException ue) {
				throw new PadesException ("Error en la actualización de firma Pades:"+ ue.getMessage(),ue);
			}
		default:
				throw new PadesException ("No se admite el formato de firma indicado:" + formatoPades.value());
		}
		/* FINPETITRIBUTAS-5 ENAVARRO ** 01/04/2020 ** Compulsa */
	}
	
	/**
	 * Firma local, soporta solamente BES y EPES
	 * @param datosAFirmar contenido a firmar
	 * @param algoritmoHash Algoritmo
	 * @param paramsPoliticaFirma Parámetros de política, sólo en EPES
	 * @return 
	 * @throws PadesException
	 */
	private byte[] localSign(byte[] datosAFirmar, AlgoritmoHashType algoritmoHash, Properties paramsPoliticaFirma) throws PadesException{
		try{
			String algoritmo;
			Properties paramsFirma= new Properties();
			if (paramsPoliticaFirma!=null && !paramsPoliticaFirma.isEmpty()){
				paramsFirma.putAll(paramsPoliticaFirma);
			}
			switch (algoritmoHash) {
			case SHA_1:
				algoritmo= AOSignConstants.SIGN_ALGORITHM_SHA1WITHRSA;
				break;
			case SHA_256:
				algoritmo=AOSignConstants.SIGN_ALGORITHM_SHA256WITHRSA;
				break;
			case SHA_384:
				algoritmo=AOSignConstants.SIGN_ALGORITHM_SHA384WITHRSA;
				break;
			case SHA_512:
				algoritmo= AOSignConstants.SIGN_ALGORITHM_SHA512WITHRSA;
				break;
			default:
				throw new PadesException ("No se soporta el algoritmo de firma " + algoritmoHash.value());
			}
			paramsFirma.setProperty("signatureSubFilter", AOSignConstants.PADES_SUBFILTER_BES);
			paramsFirma.setProperty("addKeyInfoKeyValue", "true");
			paramsFirma.setProperty("addKeyInfoKeyName", "true");
			
			PrivateKeyEntry privateKeyEntry= AlmacenController.getPrivateKey(pref.getAlmacen(), pref.getTipoAlmacen(), pref.getClaveAlmacen(), pref.getCertificado(), pref.getClaveCertificado());
			
			AOPDFSigner signer= new AOPDFSigner();
			return signer.sign(datosAFirmar, algoritmo, privateKeyEntry.getPrivateKey(), privateKeyEntry.getCertificateChain(),paramsFirma);
			
		} catch (AOException sign){
			throw new PadesException (sign.getMessage(), sign);
		} catch (IOException io) {
			throw new PadesException (io.getMessage(),io);
		} catch (AlmacenException ae) {
			throw new PadesException ("Error al recuperar el certificado de firma:" + ae,ae);
		}
	}
	
	/**
	 * Firma según PADES-BES
	 * @param datosAFirmar
	 * @param algoritmoHash
	 * @return
	 * @throws PadesException
	 */
	private byte[] signBES(byte[] datosAFirmar, AlgoritmoHashType algoritmoHash) throws PadesException{
		return localSign(datosAFirmar,algoritmoHash, null);
	}
	/**
	 * Firma según EPES
	 * @param datosAFirmar
	 * @param algoritmoHash
	 * @param politicaFirma
	 * @return
	 * @throws PadesException
	 */
	private byte[] signEPES(byte[] datosAFirmar, AlgoritmoHashType algoritmoHash, PoliticaFirmaPadesType politicaFirma) throws PadesException{
		Properties paramsPoliticaFirma= new Properties();
		if (politicaFirma==null) {
			throw new PadesException ("No se puede firmar perfil EPES sin indicar los datos de la política de firma");
		}
		if (politicaFirma.getPolicyIdentifier()==null ||
			politicaFirma.getPolicyIdentifierHash()==null||
			politicaFirma.getPolicyIdentifierHashAlgorithm()==null||
			politicaFirma.getPolicyQualifier()==null
			){
			throw new PadesException ("No se puede firmar perfil EPES sin indicar los datos de la política de firma");
		}
		paramsPoliticaFirma.setProperty("policyIdentifier", politicaFirma.getPolicyIdentifier()); 
        paramsPoliticaFirma.setProperty("policyIdentifierHash", politicaFirma.getPolicyIdentifierHash());
        paramsPoliticaFirma.setProperty("policyIdentifierHashAlgorithm", politicaFirma.getPolicyIdentifierHashAlgorithm());
        paramsPoliticaFirma.setProperty("policyQualifier", politicaFirma.getPolicyQualifier());
        return localSign(datosAFirmar, algoritmoHash,paramsPoliticaFirma);
	}
	
	
}
