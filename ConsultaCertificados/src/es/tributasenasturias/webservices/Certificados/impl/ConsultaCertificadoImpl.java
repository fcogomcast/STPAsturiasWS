package es.tributasenasturias.webservices.Certificados.impl;


import es.tributasenasturias.webservices.Certificados.SERVICIOWEB;
import es.tributasenasturias.webservices.Certificados.utils.Constantes;
import es.tributasenasturias.webservices.Certificados.utils.Log.GenericAppLogger;
import es.tributasenasturias.webservices.Certificados.utils.Log.TributasLogger;
import es.tributasenasturias.webservices.Certificados.validacion.IValidator;
import es.tributasenasturias.webservices.Certificados.validacion.ParameterValidator;


public class ConsultaCertificadoImpl {
	/**
	 * Realiza la consulta de certificado de estar al corriente de la deuda
	 * @param entrada Entrada del servicio web
	 * @param flags Flags que puedan interesar para el proceso de certificados
	 * @return
	 */
	public SERVICIOWEB consultarCertificado(SERVICIOWEB entrada, Flags flags)
	{
		GenericAppLogger log = new TributasLogger();
		SalidaServicio salida= new SalidaServicio(entrada);
		log.info(">>>Se valida la entrada.");
		//Validamos la entrada
		IValidator<SERVICIOWEB> validador = new ParameterValidator();
		if (!validador.isValid(entrada))
		{
			log.error("No son válidos los parámetros");
			salida.setErrorResponse(Constantes.getFatalError() + "-" + validador.getResultado().toString());
		}
		else //Entrada válida. Procesamiento normal.
		{
			log.info(">>>Se realiza la consulta de certificado.");
			ConsultaCertificadoAction cons = new ConsultaCertificadoAction();
			if (!cons.doConsulta(entrada, flags))
			{
				salida.setErrorResponse(Constantes.getFatalError() + "-" + cons.getResultado().toString());
			}
			else
			{
				//Recuperamos del resultado las variables.
				
				salida.setRESPUESTA(((ConsultaCertificadoAction)cons).getRespuesta());
			}
		}
		return salida;
	}
}
