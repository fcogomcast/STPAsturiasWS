/**
 * 
 */
package es.tributasenasturias.certificate.intImpl;

import weblogic.wsee.security.bst.ClientBSTCredentialProvider;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import es.tributasenasturias.certificate.interfaces.ICertificateProvider;

/**
 * @author CarlosRuben
 *
 */
public class BandejaEntradaCertificateProvider implements ICertificateProvider {

	/* (non-Javadoc)
	 * @see es.tributasenasturias.certificate.interfaces.ICertificateProvider#getCredentialProvider(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	/** Recupera el proveedor de credenciales basado en el almacén y alias que se le pasan.
	 * @param idKeyStore -Nombre del almacén de claves.
	 * @param keyStorePassword -Clave del almacén.
	 * @param idKeyAlias -Alias del certificado.
	 * @param aliasPassword -Clave del alias del certificado.
	 * @return {@link CredentialProvider}   
	 * @throws {@link Exception} - Se lanza si no se puede recuperar la clave por cualquier motivo. 
	 */
	@Override
	public final CredentialProvider getCredentialProvider(String idKeyStore,
			String keyStorePassword, String idKeyAlias, String aliasPassword) throws Exception{
		//Properties _properties = new Properties(System.getProperties());
		CredentialProvider cp=null;
		try
		{	//TODO:¿De dónde se toman las propiedades en nuestra aplicación?.
			cp = new ClientBSTCredentialProvider(
			    idKeyStore,
			    keyStorePassword,
			    idKeyAlias,
			    aliasPassword
			);
		}
		catch (Exception ex)
		{
			//FIXME:¿Qué hacemos?.¿Ignoramos?
			throw ex;
			
		}
		return cp;
	}

}
