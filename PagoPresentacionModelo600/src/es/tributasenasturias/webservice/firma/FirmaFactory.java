package es.tributasenasturias.webservice.firma;

import es.tributasenasturias.Exceptions.PresentacionException;
import es.tributasenasturias.webservice.pagopresentacion.log.LogHelper;
/**
 * Instancia clases en relaci�n con la firma
 * @author crubencvs
 *
 */
public class FirmaFactory {
	private LogHelper log;//Log de la factor�a. Se pasar� a los objetos creados que lo entiendan.
	private LogHelper dummy= new LogHelper(); //Para aquellos que no tengan otro log que pasarles.
	
	/**
	 * Instancia la clase de gesti�n de firma.
	 * @return Instancia de la clase de gesti�n de firma FirmaHelper
	 * @throws PresentacionException En caso de no poder instanciar.
	 */
	public FirmaHelper newFirmaHelper() throws PresentacionException
	{
		FirmaHelper firma;
		try
		{
			firma=new FirmaHelper();
			if (log!=null)
			{
				firma.setLogger(log);
			}
			else
			{
				firma.setLogger(dummy);
			}
		}
		catch (Exception ex)
		{
			throw new PresentacionException (this.getClass().getName()+"::Error al instanciar el objeto de firma:"+ ex.getMessage(),ex);
		}
		return firma;
	}
	/**
	 * Asigna una instancia de LogHelper a la instancia de FirmaFactory para que todos los mensajes se env�en a ese LogHelper 
	 * y todos los objetos instanciados mediante la factor�a reciban ese log si implementan la interfaz ILoggable 
	 * @param plog Instancia de LogHelper
	 */
	public void setLogger(LogHelper plog)
	{
		this.log=plog;
	}
}
