/**
 * 
 */
package es.tributasenasturias.webservices.context;

/**Funcionalidades de las clases que utilizar�n los contextos de llamada para recuperar informaci�n.
 * @author crubencvs
 *
 */
public interface IContextReader {
	void setCallContext(CallContext ctx);
	CallContext getCallContext();
}
