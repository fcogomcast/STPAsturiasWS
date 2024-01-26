package es.tributasenasturias.seguridad;

import java.util.ArrayList;
/**Implementa la lista de resultados de las validaciones.
 * 
 * @author CarlosRuben
 *
 */
public class ResultadoValidacion extends ArrayList<ValidationResultOut> implements IResultadoValidacion{
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
}
