package es.tributasenasturias.validacion;

import java.util.List;

import es.tributasenasturias.dao.DatosProceso;
import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.exceptions.PasarelaPagoException;

public class ValidadorOperacion {

	private boolean valido;
	private String codigoError;
	protected GestorValidadores<DatosEntradaServicio> ges = new GestorValidadores<DatosEntradaServicio>();

	public ValidadorOperacion() {
		super();
	}

	public void validar(DatosProceso peticion) throws PasarelaPagoException {
		try
		{
			DatosEntradaServicio peticionServicio = peticion.getPeticionServicio();
			ges.valida(peticionServicio);
			if (ges.isValido())
			{
				this.valido=true;
			}
			else
			{
				this.valido=false;
				this.codigoError=ges.getCodigoMensaje();
			}
			return;
		}
		catch (ValidacionException e)
		{
			throw new PasarelaPagoException ("Error en validaciones:" + e.getMessage(),e);
		}
	}
	
	public List<String> getMensajesValidacion()
	{
		return ges.getMensajes();
	}
	/**
	 * @return the valido
	 */
	public final boolean isValido() {
		return valido;
	}

	/**
	 * @return the codigoError
	 */
	public final String getCodigoError() {
		return codigoError;
	}

}