package es.tributasenasturias.validacion.consulta;

import java.util.ArrayList;
import java.util.List;

import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.validacion.CodigosResultadoValidacion;
import es.tributasenasturias.validacion.IValidador;
import es.tributasenasturias.validacion.ResultadoIValidador;
import es.tributasenasturias.validacion.ValidacionException;

public final class ValidadorOrigenPortalConsulta implements IValidador<DatosEntradaServicio>{


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
		
		if (datos.getOrigen().equals(Constantes.getOrigenPortal()) || datos.getOrigen().equals(Constantes.getOrigenS1()))
		{
			if(datos.getModalidad().equals(Constantes.getModalidadAutoliquidacion()) && "".equals(datos.getJustificante()))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.JUSTIFICANTE_VACIO);
				resultado.setValido(false);
			}
			else if(datos.getModalidad().equals(Constantes.getModalidadLiquidacion()) && ("".equals(datos.getIdentificacion()) || "".equals(datos.getReferencia())))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.IDENTIF_REFER_VACIO);
				resultado.setValido(false);
			}
			else if (datos.getModalidad().equals(Constantes.getModalidadLiquidacion()) && !"".equals(datos.getJustificante()))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.JUSTIFICANTE_INNECESARIO);
				resultado.setValido(false);
			}
		}
		return resultado;

	}
}
