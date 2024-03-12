/**
 * 
 */
package es.tributasenasturias.utils.log;

import java.util.Date;




/** Formatea el mensaje que se le pasa. Esto permite que ante un mismo mensaje, se tenga m�s de una 
 * forma de mostrarlo.
 * @author crubencvs
 *
 */
public class FormateadorTributas implements IFormateadorMensaje{

		private String infoExtra;//Informaci�n extra que se quiere incluir en cada l�nea de log.
		/**
		 * Se indica la informaci�n extra a incluir en cada l�nea de log.
		 * @param infoExtra Identificador.
		 */
		public synchronized void setInfoExtra(String infoExtra)
		{
			this.infoExtra=infoExtra;
		}
		/**
		 * Constructor por defecto.
		 */
		protected FormateadorTributas()
		{
			
		}
		
		/**
		 * M�todo que realiza el formateo del mensaje con un nivel de mensajes especificado.
		 * @param message Mensaje a logear
		 */
		@Override
		public String formatea(String message,NIVEL_LOG nivel)
		{
				Date today = new Date();
	            String completeMsg = today + " :: " + nivel + " :: " + message;
	            if (infoExtra!=null)
	            {
	            	completeMsg+="::"+infoExtra;
	            }
	            return completeMsg;
		}
}
