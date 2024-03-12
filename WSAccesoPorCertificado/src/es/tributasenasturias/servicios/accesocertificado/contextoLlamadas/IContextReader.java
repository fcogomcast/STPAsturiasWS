/**
 * 
 */
package es.tributasenasturias.servicios.accesocertificado.contextoLlamadas;

/**Funcionalidades de las clases que utilizarán los contextos de llamada para recuperar información.
 * @author crubencvs
 *
 */
public interface IContextReader {
	public void setCallContext(CallContext ctx);
	public CallContext getCallContext();
}
