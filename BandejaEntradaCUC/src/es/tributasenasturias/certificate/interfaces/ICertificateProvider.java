package es.tributasenasturias.certificate.interfaces;

import weblogic.xml.crypto.wss.provider.CredentialProvider;
public interface ICertificateProvider {
	CredentialProvider getCredentialProvider (String idKeyStore,String idKeyStorePassword,
										String idKeyAlias, String idKeyPassword) throws Exception;
}
