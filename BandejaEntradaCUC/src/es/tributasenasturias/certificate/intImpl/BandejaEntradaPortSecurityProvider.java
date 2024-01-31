/**
 * 
 */
package es.tributasenasturias.certificate.intImpl;

import https.www3_aeat_es.adua.internet.es.aeat.dit.adu.adht.banent.listadecv3.ListaDecV3;

import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import es.tributasenasturias.certificate.interfaces.IPortSecurityProvider;
import es.tributasenasturias.certificate.interfaces.IPropertiesProvider;

/**
 * @author CarlosRuben
 *
 */
public class BandejaEntradaPortSecurityProvider implements IPortSecurityProvider {

	/* (non-Javadoc)
	 * @see es.tributasenasturias.certificate.interfaces.IPortSecurityProvider#setPortSecurity(java.lang.Object)
	 */
	@Override
	public final void setPortSecurity(Object port) throws Exception{
		ListaDecV3 miPort= (ListaDecV3) port;
		List<CredentialProvider> credProviders = new ArrayList<CredentialProvider>();
		es.tributasenasturias.certificate.interfaces.ICertificateProvider certProvider = new es.tributasenasturias.certificate.intImpl.BandejaEntradaCertificateProvider();
		IPropertiesProvider pref = new Preferencias();
		pref.refreshPreferences();
		try{
			CredentialProvider cp = certProvider.getCredentialProvider(((Preferencias)pref).getNombreAlmacen(),
					((Preferencias)pref).getClaveAlmacen(), "aeatcert", ((Preferencias)pref).getClaveAlmacen());
			if (cp!=null)
			{
				credProviders.add(cp);
				Map <String, Object> reqContext = ((BindingProvider)miPort).getRequestContext();
				reqContext.put(WSSecurityContext.CREDENTIAL_PROVIDER_LIST, credProviders);
				javax.net.ssl.TrustManager tManager= new BandejaEntradaTrustManager();
				//reqContext.put (WSSecurityContext.TRUST_MANAGER,tManager);
				reqContext.put (WSSecurityContext.TRUST_MANAGER,tManager);
			}
			else
			{
				throw new Exception ("Imposible establecer seguridad basada en certificado.");
			}
		}
		catch (UnrecoverableKeyException kex)
		{
			throw new UnrecoverableKeyException ("No se puede recuperar la clave buscada: "+ kex.getMessage());
		}
		catch (Exception ex)
		{
			throw new Exception ("Imposible establecer seguridad basada en certificado: "+ ex.getMessage());
		}

	}
}
