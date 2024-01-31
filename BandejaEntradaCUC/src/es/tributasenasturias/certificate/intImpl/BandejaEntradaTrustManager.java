/**
 * 
 */
package es.tributasenasturias.certificate.intImpl;

import javax.net.ssl.TrustManager;
import javax.security.cert.X509Certificate;

/**
 * @author CarlosRuben
 *
 */
public class BandejaEntradaTrustManager implements TrustManager {
	public boolean certificateCallback(X509Certificate[] chain, int validateErr)
    {
        return true;
    }
	public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificate, String str) throws java.security.cert.CertificateException {
        //Only in server mode.
    }
    
    public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificate, String str) throws java.security.cert.CertificateException {
        //For example, if you only want that these program connect with a specific server, throw an exception if there isn?t the server.
    }
    
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        //Only in server mode.
        return null;
    }

}
