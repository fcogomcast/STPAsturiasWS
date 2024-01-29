/**
 * 
 */
package es.tributasenasturias.docs;


import es.tributasenasturias.Exceptions.PresentacionException;
import es.tributasenasturias.webservice.pagopresentacion.log.LogHelper;

/**Factor�a para los objetos relacionados con los documentos.
 * @author crubencvs
 *
 */
public class DocsFactory {
	
	private LogHelper log;//Log de la factor�a. Se pasar� a los objetos creados que lo entiendan.
	private LogHelper dummy = new LogHelper();
	/**
	 * Establecer� el log de la factor�a. Este log se pasar� a todos los objetos creados que implementen
	 * el interfaz ILoggable para recibir logs.
	 * @param plog Instancia de LogHelper que se pasar� a cada uno de los objetos creados.
	 */
	public void setLogger(LogHelper plog)
	{
		this.log= plog;
	}
	/**
	 * Recupera una nueva instancia de la clase que permite traer documentos de la base de datos.
	 * @param p_numAutoliq N�mero de autoliquidaci�n sobre la que buscar el documento.
	 * @return Objeto que permite recuperar los documentos relacionados con esa autoliquidaci�n.
	 * @throws PresentacionException En caso de no poder instanciar el objeto.
	 */
	public DocumentoDoin newDocumentoDoin(String p_numAutoliq) throws PresentacionException
	{
		DocumentoDoin doc;
		try
		{
			doc= new DocumentoDoin(p_numAutoliq);
			if (log!=null)
			{
				doc.setLogger(log);
			}
			else
			{
				doc.setLogger(dummy); //Este log que se le pasa no podr� emitir mensajes.
			}
		}
		catch (Exception ex)
		{
			throw new PresentacionException (this.getClass().getName() +"::Error al crear el objeto para recuperar documentos de la base de datos:" + ex.getMessage(),ex);
		}
		return doc;
	}
	/**
	 * Recupera una nueva instancia de la clase que permite realizar alta de documentos.
	 * @return Nueva instancia de la clase.
	 * @throws PresentacionException Cuando no puede instanciar el objeto.
	 */
	public AltaDocumento newAltaDocumento()throws PresentacionException
	{
		AltaDocumento doc;
		try
		{
			doc= new AltaDocumento();
			if (log!=null)
			{
				doc.setLogger(log);
			}
			else
			{
				doc.setLogger(dummy); //Este log no podr� visualizar mensajes.
			}
		}
		catch (Exception ex)
		{
			throw new PresentacionException (this.getClass().getName()+"::Error al crear el objeto de alta de documentos:" + ex.getMessage(),ex);
		}
		return doc;
	}
	/**
	 * Recupera una nueva instancia de documento de comparecencia.
	 * @param p_numAutoliq N�mero de autoliquidaci�n sobre la que se har� la comparecencia.
	 * @return Nueva instancia de documento de comparecencia.
	 * @throws PresentacionException En caso de no construir de forma correcta la instancia de documento.
	 */
	public Comparecencia newComparecencia(String p_numAutoliq) throws PresentacionException
	{
		Comparecencia com;
		try
		{
			com=new Comparecencia(p_numAutoliq);
			if (log!=null)
			{
				com.setLogger(log);
			}
			else
			{
				com.setLogger(dummy);
			}
		}
		catch (Exception ex)
		{
			throw new PresentacionException (this.getClass().getName()+"::Error al crear el objeto de comparecencia "+ ex.getMessage(),ex);
		}
		return com;
	}
	/**
	 * Recupera una instancia de un justificante de cobro sobre el pago de una autoliquidaci�n.
	 * @param p_numAutoliq N�mero de autoliquidaci�n sobre la que se generar� el justificante.
	 * @return Instancia de justificante de cobro.
	 * @throws PresentacionException En caso de que no pueda instanciar el justificante.
	 */
	public JustificanteCobro newJustificanteCobro(String p_numAutoliq) throws PresentacionException
	{
		JustificanteCobro just;
		try
		{
			just=new JustificanteCobro(p_numAutoliq);
			if (log!=null)
			{
				just.setLogger(log);
			}
			else
			{
				just.setLogger(dummy);
			}
		}
		catch (Exception ex)
		{
			throw new PresentacionException (this.getClass().getName()+"::Error al crear el justificante de cobro:"+ex.getMessage(),ex);
		}
		return just;
	}
	/**
	 * Recupera una nueva instancia de un documento de justificante de presentaci�n en base a una autoliquidaci�n.
	 * @param p_numAutoliq N�mero de la autoliquidaci�n que se ha presentado.
	 * @return Instancia de un documento de justificante de presentaci�n.
	 * @throws PresentacionException En caso de no poder instanciar un justificante.
	 */
	public JustificantePresentacion newJustificantePresentacion(String p_numAutoliq) throws PresentacionException
	{
		JustificantePresentacion just;
		try
		{
			just = new JustificantePresentacion(p_numAutoliq);
			if (log!=null)
			{
				just.setLogger(log);
			}
			else
			{
				just.setLogger(dummy);
			}
		}
		catch (Exception ex)
		{
			throw new PresentacionException (this.getClass().getName()+":: Error al crear el justificante de presentaci�n:"+ ex.getMessage(),ex);
		}
		return just;
	}
}
