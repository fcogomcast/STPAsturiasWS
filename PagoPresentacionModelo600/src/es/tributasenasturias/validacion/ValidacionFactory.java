package es.tributasenasturias.validacion;

import es.tributasenasturias.Exceptions.PresentacionException;

import es.tributasenasturias.webservice.pagopresentacion.log.LogHelper;

public class ValidacionFactory{
	private LogHelper log; //Log de la factor�a. Se pasar� a los objetos creados si tiene sentido hacerlo.
	private LogHelper dummy=new LogHelper(); //Log que no escribe en ning�n sitio, para pasar cuando el primario no est� inicializado.
	/**
	 * Instancia un objeto de validador de certificado, uno que implementa la interfaz IValidator.
	 * @return Instancia del objeto validador.
	 * @throws PresentacionException En caso de no poder instanciar.
	 */
	public IValidator<?> newCertificadoValidator() throws PresentacionException
	{
		CertificadoValidator cer;
		try
		{
			cer = new CertificadoValidator();
			if (log!=null)
			{
				cer.setLogger(log);
			}
			else
			{
				cer.setLogger(dummy);
			}
		}
		catch (Exception ex)
		{
			throw new PresentacionException (this.getClass().getName()+"::Error al instanciar el objeto para validador de certificado:"+ ex.getMessage(),ex);
		}
		return cer;
	}
	/**
	 * Instancia la clase para validar el par�metro XML de entrada
	 * @return Instancia de validador.
	 * @throws PresentacionException En caso de no poder instanciar
	 */
	public IValidator<?> newParameterValidator() throws PresentacionException
	{
		XMLEntValidator xlv= new XMLEntValidator();
		try
		{
			xlv= new XMLEntValidator();
		}
		catch (Exception ex)
		{
			throw new PresentacionException (this.getClass().getName()+"::Error al instanciar el objeto para validar los par�metros de entrada:"+ ex.getMessage(),ex);
		}
		return xlv;
	}
	/**
	 * Hace que el log Helper de la instancia de factor�a sea el que se pasa.
	 * Se utilizar� para pasarlo a los objetos creados que puedan manejar este log.
	 * @param plog LogHelper que se usar� para mostrar mensajes.
	 */
	public void setLogger(LogHelper plog)
	{
		this.log=plog;
	}
}
