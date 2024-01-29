package es.tributasenasturias.business;

import es.tributasenasturias.Exceptions.PresentacionException;
import es.tributasenasturias.webservice.pagopresentacion.log.LogHelper;

/**
 * Factoría para los Business Objects del servicio.
 * @author crubencvs
 *
 */
public class BOFactory {
	private LogHelper log; //Log de la factoría. Se pasará a los objetos creados si tiene sentido hacerlo.
	private LogHelper dummy=new LogHelper(); //Log que no escribe en ningún sitio, para pasar cuando el primario no esté inicializado.
	/**
	 * Instancia un objeto de alta de escritura.
	 * @return Instancia de la objeto de escritura.
	 * @throws PresentacionException En caso de no poder instanciar.
	 */
	public AltaEscritura newAltaEscritura() throws PresentacionException
	{
		AltaEscritura escritura;
		try
		{
			escritura = new AltaEscritura();
			if (log!=null)
			{
				escritura.setLogger(log);
			}
			else
			{
				escritura.setLogger(dummy);
			}
		}
		catch (Exception ex)
		{
			throw new PresentacionException (this.getClass().getName()+"::Error al instanciar el objeto para el alta de escritura:"+ ex.getMessage(),ex);
		}
		return escritura;
	}
	/**
	 * Instancia un nuevo objeto para el alta de expediente.
	 * @return Instancia de un objeto para el alta de expediente.
	 * @throws PresentacionException En caso de no poder instanciar.
	 */
	public AltaExpediente newAltaExpediente() throws PresentacionException
	{
		AltaExpediente expediente;
		try
		{
			expediente = new AltaExpediente();
			if (log!=null)
			{
				expediente.setLogger(log);
			}
			else
			{
				expediente.setLogger(dummy);
			}
		}
	    catch (Exception ex)
	    {
	    	throw new PresentacionException (this.getClass().getName()+"::Error al instanciar el objeto para alta de expediente:" +ex.getMessage(),ex);
	    }
	    return expediente;
	}
	/**
	 * Instancia un clase de integración con tributas en tablas de modelo.
	 * @return Instancia de clase de integración con tributas
	 * @throws PresentacionException En caso de no poder instanciar.
	 */
	public IntegraTributas newIntegraTributas() throws PresentacionException
	{
		IntegraTributas integra;
		try
		{
			integra=new IntegraTributas();
			if (log!=null)
			{
				integra.setLogger(log);
			}
			else
			{
				integra.setLogger(dummy);
			}
		}
		catch (Exception ex)
		{
			throw new PresentacionException (this.getClass().getName()+"::Error al instanciar el objeto de integración en tributas:" + ex.getMessage(),ex);
		}
		return integra;
	}
	/**
	 * Instancia la clase de generación de modelo de autoliquidación en PDF
	 * @return Instancia de la clase de generación del modelo de autoliquidación en un PDF.
	 * @throws PresentacionException En caso de no poder instanciar
	 */
	public ModelosPDF newModelosPdf () throws PresentacionException
	{
		ModelosPDF model;
		try
		{
			model =new ModelosPDF();
			if (log!=null)
			{
				model.setLogger(log);
			}
			else
			{
				model.setLogger(dummy);
			}
			
		}
		catch (Exception ex)
		{
			throw new PresentacionException (this.getClass().getName()+"::Error al instanciar el objeto de dibujo del modelo de autoliquidación:"+ ex.getMessage(),ex);
		}
		return model;
	}
	/**
	 * Instancia la clase de petición de pago.
	 * @return Instancia de la clase.
	 * @throws PresentacionException En caso de que no pueda instanciar.
	 */
	public Pago newPago() throws PresentacionException
	{
		Pago p;
		try{
			p=new Pago();
			if (log!=null)
			{
				p.setLogger(log);
			}
			else
			{
				p.setLogger(dummy);
			}
		}
		catch (Exception ex)
		{
			throw new PresentacionException (this.getClass().getName()+"::Error al instanciar el objeto de petición de pago :"+ex.getMessage(),ex);
		}
		return p;
	}
	/**
	 * Hace que el log Helper de la instancia de factoría sea el que se pasa.
	 * Se utilizará para pasarlo a los objetos creados que puedan manejar este log.
	 * @param plog LogHelper que se usará para mostrar mensajes.
	 */
	public void setLogger(LogHelper plog)
	{
		this.log=plog;
	}
}
