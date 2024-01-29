package es.tributasenasturias.webservices.Certificados.General;

import java.util.ArrayList;

import es.tributasenasturias.webservices.Certificados.validacion.ValidationResultOut;
/**Implementa la lista de resultados de las validaciones.
 * 
 * @author CarlosRuben
 *
 */
public class InfoResultado extends ArrayList<ValidationResultOut> implements IResultado{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3841312613872427452L;
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#toString()
	 */
	@Override
	public String toString() {
		StringBuffer res= new StringBuffer();
		for (ValidationResultOut i:this)
		{
			res.append(i.getMessageText());
			res.append(System.getProperty("line.separator"));
		}
		return res.toString();
	}
	//Método "addMessage" que inserta un nuevo mensaje en la lista
	public void addMessage(String message)
	{
		ValidationResultOut v = new ValidationResultOut();
		v.setMessageText(message);
		this.add(v);
	}
	
	public void addStackTrace(StackTraceElement[] stackTraceElements)
	{
		if (stackTraceElements == null)
            return;
        for (int i = 0; i < stackTraceElements.length; i++)
        {
            this.addMessage("StackTrace -> " + stackTraceElements[i].getFileName() + " :: " + stackTraceElements[i].getClassName() + " :: " + stackTraceElements[i].getFileName() + " :: " + stackTraceElements[i].getMethodName() + " :: " + stackTraceElements[i].getLineNumber());
        }
	}
}
