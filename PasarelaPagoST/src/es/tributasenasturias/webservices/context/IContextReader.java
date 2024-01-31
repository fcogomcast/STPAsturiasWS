/**
 * 
 */
package es.tributasenasturias.webservices.context;

/**Funcionalidades de las clases que utilizarán los contextos de llamada para recuperar información.
 * @author crubencvs
 *
 */
public interface IContextReader {
	void setCallContext(CallContext ctx);
	CallContext getCallContext();
}
