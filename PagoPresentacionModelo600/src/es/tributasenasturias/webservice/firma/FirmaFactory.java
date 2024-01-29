package es.tributasenasturias.webservice.firma;

import es.tributasenasturias.Exceptions.PresentacionException;
import es.tributasenasturias.webservice.pagopresentacion.log.LogHelper;
/**
 * Instancia clases en relación con la firma
 * @author crubencvs
 *
 */
public class FirmaFactory {
	private LogHelper log;//Log de la factoría. Se pasará a los objetos creados que lo entiendan.
	private LogHelper dummy= new LogHelper(); //Para aquellos que no tengan otro log que pasarles.
	
	/**
	 * Instancia la clase de gestión de firma.
	 * @return Instancia de la clase de gestión de firma FirmaHelper
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
	 * Asigna una instancia de LogHelper a la instancia de FirmaFactory para que todos los mensajes se envíen a ese LogHelper 
	 * y todos los objetos instanciados mediante la factoría reciban ese log si implementan la interfaz ILoggable 
	 * @param plog Instancia de LogHelper
	 */
	public void setLogger(LogHelper plog)
	{
		this.log=plog;
	}
}
