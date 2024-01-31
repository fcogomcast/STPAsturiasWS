package es.tributasenasturias.validacion.consulta;

import java.util.ArrayList;
import java.util.List;

import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.validacion.CodigosResultadoValidacion;
import es.tributasenasturias.validacion.IValidador;
import es.tributasenasturias.validacion.ResultadoIValidador;
import es.tributasenasturias.validacion.ValidacionException;

public final class ValidadorOrigenServicioConsulta implements IValidador<DatosEntradaServicio>{


	private List<String> mensajes = new ArrayList<String>();
	@Override
	public List<String> getDatosValidacion() {
		return mensajes;
	}
	
	@Override
	public ResultadoIValidador validar(DatosEntradaServicio datos)
			throws ValidacionException {
		ResultadoIValidador resultado = new ResultadoIValidador();
		resultado.setValido(true);
		resultado.setModulo(this.getClass().getName());
		if (datos.getOrigen().equals(Constantes.getOrigenServicioWeb()))
		{
			if (!datos.getModalidad().equals(Constantes.getModalidadAutoliquidacion()))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.MODALIDAD_AUTOLIQ_SW);
				resultado.setValido(false);
			}
			else if ("".equals(datos.getMac()))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.MAC_VACIA);
				resultado.setValido(false);
			}
		}
		return resultado;

	}
}
