/**
 * 
 */
package es.tributasenasturias.validacion;

/**
 * @author crubencvs
 * Tipo de salida de las validaciones.
 */
public class ValidationResultOut {
  //Texto del resultado de la validación.
  // Por el momento no se guardan más datos.
  private String messageText="";

public String getMessageText() {
	return messageText;
}

public void setMessageText(String resultText) {
	this.messageText = resultText;
} 
}
