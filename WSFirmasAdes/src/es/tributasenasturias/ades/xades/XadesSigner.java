package es.tributasenasturias.ades.xades;

import java.security.KeyStore.PrivateKeyEntry;
import java.util.Properties;

import es.gob.afirma.core.AOException;
import es.gob.afirma.core.signers.AOSignConstants;
import es.gob.afirma.signers.xades.AOXAdESSigner;
import es.tributasenasturias.ades.AlgoritmoHashType;
import es.tributasenasturias.ades.FormatoXadesType;
import es.tributasenasturias.ades.ModoFirmaXadesType;
import es.tributasenasturias.ades.PoliticaFirmaXadesType;
import es.tributasenasturias.ades.certificados.AlmacenController;
import es.tributasenasturias.ades.exceptions.AlmacenException;
import es.tributasenasturias.ades.exceptions.UpgradeException;
import es.tributasenasturias.ades.exceptions.XadesException;
import es.tributasenasturias.ades.preferencias.Preferencias;
import es.tributasenasturias.ades.soap.SoapClientHandler;
import es.tributasenasturias.ades.upgrade.UpgraderFirma;
import es.tributasenasturias.log.ILog;

/**
 * Clase para implementar la firma mediante Xades
 * Firma INTERNALLY_DETACHED y ENVELOPED
 * @author crubencvs
 *
 */
public class XadesSigner {

	private final Preferencias pref;
	private final ILog logger;
	private final String idLlamada;

	private XadesSigner(Preferencias pref, ILog logger, String idLlamada)
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
	public static XadesSigner newInstance(Preferencias pref, ILog logger, String idLlamada) throws XadesException {
		if (pref==null || logger==null) {
			throw new XadesException ("Error de configuración del servicio");
		}
		return new XadesSigner(pref, logger, idLlamada);
	}
	
	private void validaEntrada(ModoFirmaXadesType modoFirmaXades, FormatoXadesType formatoXades, AlgoritmoHashType algoritmoHash, PoliticaFirmaXadesType politicaFirmaXades ) throws XadesException{
		if (modoFirmaXades==null) {
			throw new XadesException("El modo de firma indicado es nulo o no es válido");
		}
		if (formatoXades==null) {
			throw new XadesException("El formato de firma indicado es nulo o no es válido");
		}
		if (algoritmoHash==null) {
			throw new XadesException("No se ha indicado un algoritmo de hash, o el valor no es válido");
		}
		if (FormatoXadesType.EPES.equals(formatoXades) && politicaFirmaXades==null) {
			throw new XadesException("No se puede realizar firma EPES sin indicar política de firma.");
		}
	}
	
	/**
	 * Realiza la firma de los datos XML que se pasan
	 * @param datosAFirmar Datos XML a firmar. Se considera que no van firmados.
	 * @param modoFirmaXades modo de firma, como {@link ModoFirmaXadesType}
	 * @param formatoXades formato de firma, como {@link FormatoXadesType}
	 * @param algoritmo Algoritmo de firma. Se considera que siempre es RSA
	 * @param politicaFirma Política de firma, para EPES
	 * @return XML firmado
	 * @throws XadesException
	 */
	public byte[] procesaFirma(byte[] datosAFirmar, ModoFirmaXadesType modoFirmaXades, FormatoXadesType formatoXades, AlgoritmoHashType algoritmo, PoliticaFirmaXadesType politicaFirma, String nodoAFirmar, String xpathUbicacion) throws XadesException{
		
		validaEntrada(modoFirmaXades, formatoXades, algoritmo,politicaFirma);
		logger.debug("Formato firma:" + formatoXades.value() + " , Modo Firma:" + modoFirmaXades.value() + " ,Algoritmo:" + algoritmo.value());
		switch (formatoXades) {
		case BES:
				return signBES(datosAFirmar, algoritmo, modoFirmaXades, nodoAFirmar, xpathUbicacion);
		case EPES:
				return signEPES(datosAFirmar, algoritmo, modoFirmaXades, politicaFirma, nodoAFirmar, xpathUbicacion);
		case ES_T:
		case ES_C:
		case ES_X:
		case ES_X_L:
		case ES_A:
			byte[] xmlFirmado;
			if (politicaFirma!=null) {
				xmlFirmado= signEPES(datosAFirmar, algoritmo, modoFirmaXades, politicaFirma, nodoAFirmar, xpathUbicacion);
			}else {
				xmlFirmado= signBES(datosAFirmar, algoritmo, modoFirmaXades, nodoAFirmar, xpathUbicacion);
			}
			UpgraderFirma upf= UpgraderFirma.newInstance(pref, logger, idLlamada,new SoapClientHandler(idLlamada));
			try {
				
				xmlFirmado=upf.upgradeXades(xmlFirmado, formatoXades.value());
				if (upf.isRespuestaOk()){
					return xmlFirmado; 
				}
				else  {
					throw new XadesException("Error en la actualización de firma:" + upf.getTextoResultadoUpgrade());
				}
			} catch (UpgradeException ue) {
				throw new XadesException ("Error en la actualización de firma Xades:"+ ue.getMessage(),ue);
			}
		default:
				throw new XadesException ("No se admite el formato de firma indicado:" + formatoXades.value());
		}
	}
	/**
	 * Parte local de la firma, soporta BES y EPES
	 * @param datosAFirmar
	 * @param algoritmoHash
	 * @param modoFirma
	 * @param paramsPoliticaFirma
	 * @return
	 * @throws XadesException
	 */
	private byte[] localSign(byte[] datosAFirmar, AlgoritmoHashType algoritmoHash, ModoFirmaXadesType modoFirma, Properties paramsPoliticaFirma, String nodoAFirmar, String xpathUbicacion) throws XadesException{
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
				throw new XadesException ("No se soporta el algoritmo de firma " + algoritmoHash.value());
			}
			
			switch (modoFirma){
			case INTERNALLY_DETACHED:
				paramsFirma.setProperty("format", AOSignConstants.SIGN_FORMAT_XADES_DETACHED); 
				break;
			case ENVELOPED:
				paramsFirma.setProperty("format", AOSignConstants.SIGN_FORMAT_XADES_ENVELOPED);
				if (xpathUbicacion!=null && !"".equals(xpathUbicacion)){
					paramsFirma.setProperty("insertEnvelopedSignatureOnNodeByXPath", xpathUbicacion);
				}
				break;
			default:
				throw new XadesException ("No se soporta el modo de firma "+ modoFirma.value());
			}
			//Información de la clave, no la necesitamos pero la incluyo porque puede ser útil (sobre todo si se firmase con más de un certificado)
			paramsFirma.setProperty("addKeyInfoKeyValue", "true");
			paramsFirma.setProperty("addKeyInfoKeyName", "true");
			
			if (nodoAFirmar!=null && !"".equals(nodoAFirmar)) {
				paramsFirma.setProperty("nodeToSign", nodoAFirmar);
			}
			
			PrivateKeyEntry privateKeyEntry= AlmacenController.getPrivateKey(pref.getAlmacen(), pref.getTipoAlmacen(), pref.getClaveAlmacen(), pref.getCertificado(), pref.getClaveCertificado());
			
			AOXAdESSigner signer= new AOXAdESSigner();
			return signer.sign(datosAFirmar, algoritmo, privateKeyEntry.getPrivateKey(), privateKeyEntry.getCertificateChain(),paramsFirma);
			
		} catch (AOException sign){
			throw new XadesException (sign.getMessage(), sign);
		} catch (AlmacenException ae) {
			throw new XadesException ("Error al recuperar el certificado de firma:" + ae,ae);
		}
	}
	
	/**
	 * Firma según perfil BES
	 * @param datosAFirmar
	 * @param algoritmoHash
	 * @param modoFirma
	 * @return
	 * @throws XadesException
	 */
	private byte[] signBES(byte[] datosAFirmar, AlgoritmoHashType algoritmoHash, ModoFirmaXadesType modoFirma, String nodoAFirmar, String xpathUbicacion) throws XadesException{
		return localSign(datosAFirmar,algoritmoHash, modoFirma,null, nodoAFirmar, xpathUbicacion);
	}
	
	/**
	 * Firma según EPES
	 * @param datosAFirmar
	 * @param algoritmoHash
	 * @param modoFirma
	 * @param politicaFirma
	 * @return
	 * @throws XadesException
	 */
	private byte[] signEPES(byte[] datosAFirmar, AlgoritmoHashType algoritmoHash, ModoFirmaXadesType modoFirma, PoliticaFirmaXadesType politicaFirma, String nodoAFirmar, String xpathUbicacion) throws XadesException{
		Properties paramsPoliticaFirma= new Properties();
		if (politicaFirma==null) {
			throw new XadesException ("No se puede firmar perfil EPES sin indicar los datos de la política de firma");
		}
		if (politicaFirma.getPolicyIdentifier()==null ||
			politicaFirma.getPolicyIdentifierHash()==null||
			politicaFirma.getPolicyIdentifierHashAlgorithm()==null||
			politicaFirma.getPolicyQualifier()==null
			|| politicaFirma.getPolicyDescription()==null){
			throw new XadesException ("No se puede firmar perfil EPES sin indicar los datos de la política de firma");
		}
		paramsPoliticaFirma.setProperty("policyIdentifier", politicaFirma.getPolicyIdentifier()); 
        paramsPoliticaFirma.setProperty("policyIdentifierHash", politicaFirma.getPolicyIdentifierHash());
        paramsPoliticaFirma.setProperty("policyIdentifierHashAlgorithm", politicaFirma.getPolicyIdentifierHashAlgorithm());
        paramsPoliticaFirma.setProperty("policyDescription", politicaFirma.getPolicyDescription());
        paramsPoliticaFirma.setProperty("policyQualifier", politicaFirma.getPolicyQualifier());
        return localSign(datosAFirmar, algoritmoHash,modoFirma, paramsPoliticaFirma, nodoAFirmar, xpathUbicacion);
	}
	
	
}
