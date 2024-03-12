package es.tributasenasturias.servicios.accesocertificado;

import es.tributasenasturias.servicios.accesocertificado.contextoLlamadas.CallContext;
import es.tributasenasturias.servicios.accesocertificado.exceptions.STPAException;

/**
 * Permite construir los objetos que procesan cada petición (recuperar usuario, confirmar, listar)
 * @author crubencvs
 *
 */
public class ProcesadorFactory {

	/**
	 * Permite contruir un objeto {@link ProcesadorRecuperacionUsuario} para procesar la recuperación de usuario.
	 * @param context Contexto de llamada que se pasará, conteniendo datos comunes a toda la llamada.
	 * @return Instancia del {@link ProcesadorRecuperacionUsuario}
	 * @throws STPAException
	 */
	public static ProcesadorRecuperacionUsuario newRecuperacionUsuario(CallContext context) throws STPAException
	{
		if (context!=null)
		{
			return new ProcesadorRecuperacionUsuario(context);
		}
		else
		{
			throw new STPAException ("No se ha indicado contexto en el constructor de clase " + ProcesadorRecuperacionUsuario.class.getName());
		}
	}
	
	/**
	 * Permite construir un objeto {@link ProcesadorConfirmacionAcceso} para procesar la confirmación de acceso.
	 * @param context Contexto de llamada que se pasará conteniendo datos comunes a la llamada.
	 * @return {@link ProcesadorConfirmacionAcceso}
	 * @throws STPAException
	 */
	public static ProcesadorConfirmacionAcceso newConfirmacionAcceso (CallContext context) throws STPAException
	{
		if (context!=null)
		{
			return new ProcesadorConfirmacionAcceso(context);
		}
		else
		{
			throw new STPAException ("No se ha indicado contexto en el constructor de clase " + ProcesadorConfirmacionAcceso.class.getName());
		}
	}
	/**
	 * Permite construir un objeto {@link ProcesadorListadoCertificados} para procesar el listado de certificados admitidos
	 * @param context Contexto de llamada que se pasará conteniendo datos comunes a la llamada.
	 * @return {@link ProcesadorListadoCertificados}
	 * @throws STPAException
	 */
	public static ProcesadorListadoCertificados newListadoCertificados (CallContext context) throws STPAException
	{
		if (context!=null)
		{
			return new ProcesadorListadoCertificados(context);
		}
		else
		{
			throw new STPAException ("No se ha indicado contexto en el constructor de clase " + ProcesadorListadoCertificados.class.getName());
		}
			
	}
	
	/**
	 * Permite construir un objeto {@link ProcesadorAsociarCertificadoUsuario} para procesar la asociación de un usuario a un certificado
	 * @param context Contexto de llamada que se pasará conteniendo datos comunes a la llamada.
	 * @return {@link ProcesadorAsociarCertificadoUsuario}
	 * @throws STPAException
	 */
	public static ProcesadorAsociarCertificadoUsuario newAsociarCertificadoUsuario(CallContext context) throws STPAException
	{
		if (context!=null)
		{
			return new ProcesadorAsociarCertificadoUsuario(context);
		}
		else
		{
			throw new STPAException ("No se ha indicado contexto en el constructor de clase " + ProcesadorAsociarCertificadoUsuario.class.getName());
		}
			
	}
}
