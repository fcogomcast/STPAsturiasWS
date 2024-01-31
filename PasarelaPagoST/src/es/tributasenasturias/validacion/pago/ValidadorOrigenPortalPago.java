package es.tributasenasturias.validacion.pago;

import java.util.ArrayList;
import java.util.List;

import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.validacion.CodigosResultadoValidacion;
import es.tributasenasturias.validacion.IValidador;
import es.tributasenasturias.validacion.ResultadoIValidador;
import es.tributasenasturias.validacion.ValidacionException;

public final class ValidadorOrigenPortalPago implements IValidador<DatosEntradaServicio>{


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
			else if ("".equals(datos.getTarjeta()) && "".equals(datos.getCcc()))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.DATOS_PAGO_INCOMPLETOS);
				resultado.setValido(false);
			}
			else if (!"".equals(datos.getTarjeta()) && !"".equals(datos.getCcc()))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.DATOS_PAGO_INCOMPLETOS);
				resultado.setValido(false);
			}
			else if (!"".equals(datos.getTarjeta()) && "".equals(datos.getFechaCaducidadTarjeta()))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.DATOS_TARJETA_INCOMPLETOS);
				resultado.setValido(false);
			}
			else if (!"".equals(datos.getAplicacion()) || !"".equals(datos.getNumeroUnico()))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.APLICACION_NUM_UNICO_INNECESARIO);
				resultado.setValido(false);
			}
			else if (datos.getModalidad().equals(Constantes.getModalidadLiquidacion()) && !"".equals(datos.getJustificante()))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.JUSTIFICANTE_INNECESARIO);
				resultado.setValido(false);
			}
			else if (datos.getModalidad().equals(Constantes.getModalidadLiquidacion()) && (!"".equals(datos.getDatoEspecifico()) || !"".equals(datos.getFechaDevengo())))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.FECHA_DEVENGO_ESPECIFICO_INNECESARIO);
				resultado.setValido(false);
			}
		}
		return resultado;

	}
}
