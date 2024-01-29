package es.tributasenasturias.validacion;

import java.rmi.RemoteException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.tributasenasturias.documentos.util.XMLUtils;

public class XMLEntValidator implements IValidator<String> {

	String xmlAValidar;
	Document docValidado;
	ResultadoValidacion res;
	@Override
	public IResultadoValidacion getResultado() {
		return res;
		
	}
	public XMLEntValidator()
	{
		res= new ResultadoValidacion();
	}
	@Override
	public boolean isValid(String xmlData) {
		xmlAValidar=xmlData;
		boolean valid=true;
		if (xmlAValidar == null || xmlAValidar.length() == 0) {
			res.addMessage("El xml de entrada est� vac�o");
			valid=false;
		}
		if (valid)
		{
			try {
				docValidado=(Document) XMLUtils.compilaXMLObject(xmlAValidar, null);
			} catch (RemoteException e) {
				res.addMessage("El par�metro de entrada no es un xml v�lido");
				valid=false;
			}
			catch (Exception e)
			{
				res.addMessage("El par�metro de entrada no es un xml v�lido");
				valid=false;
			}
		}
		if (valid)
		{
			//Si lo convierte, comprobamos que existe el nodo identificaci�n. Deber�amos 
			//validar contra el XSD, pero eso podr�a llevar a que no funcionara el servicio.
			//El nodo identificaci�n es necesario para saber qui�n nos env�a la petici�n,
			// este al menos deber�a estar.
			Element[] identificacion = XMLUtils.selectNodes(docValidado,"//remesa//declaracion//Identificacion");
			if (identificacion.length==0)
			{
				res.addMessage("Falta el nodo Identificacion. El formato de XML no est� correcto");
				valid=false;
			}
		}
		return valid;
	}
	/**
	 * Permite recuperar el XML que el m�todo considera correcto, como un org.w3c.dom.document.
	 * No es muy correcto desde el punto de vista de separaci�n de responsabilidad, pero as� nos 
	 * evitamos hacer dos veces una misma operaci�n.
	 * @return Document con la entrada.
	 */
	public Document getXMLValidado()
	{
		return this.docValidado;
	}
}
