/**
 * 
 */
package es.tributasenasturias.abstracts;

import es.tributasenasturias.certificate.interfaces.IPortSecurityProvider;

/**
 * @author CarlosRuben
 *
 */
public abstract class ServiceProxy {
	protected IPortSecurityProvider secProv;
	public abstract String getEndPoint();
	public abstract void setEndPoint(String endPoint);
	public abstract Object getInnerObject();
}
