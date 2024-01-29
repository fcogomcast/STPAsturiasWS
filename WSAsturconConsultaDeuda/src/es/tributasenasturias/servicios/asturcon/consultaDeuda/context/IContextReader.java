/**
 * 
 */
package es.tributasenasturias.servicios.asturcon.consultaDeuda.context;

/**Funcionalidades de las clases que utilizar�n los contextos de llamada para recuperar informaci�n.
 * @author crubencvs
 *
 */
public interface IContextReader {
	public void setCallContext(CallContext ctx);
	public CallContext getCallContext();
}
