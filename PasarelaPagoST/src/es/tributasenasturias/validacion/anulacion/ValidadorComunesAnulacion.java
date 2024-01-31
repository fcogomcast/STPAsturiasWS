package es.tributasenasturias.validacion.anulacion;

import java.util.ArrayList;
import java.util.List;

import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.Varios;
import es.tributasenasturias.validacion.CodigosResultadoValidacion;
import es.tributasenasturias.validacion.IValidador;
import es.tributasenasturias.validacion.ResultadoIValidador;
import es.tributasenasturias.validacion.ValidacionException;

public final class ValidadorComunesAnulacion implements IValidador<DatosEntradaServicio>{

	List<String> mensajes = new ArrayList<String>();
	@Override
	public List<String> getDatosValidacion() {
		return mensajes;
	}

	@Override
	public final ResultadoIValidador validar(DatosEntradaServicio datos)
			throws ValidacionException {
		ResultadoIValidador resultado = new ResultadoIValidador();
		resultado.setValido(true);
		resultado.setModulo(this.getClass().getName());
		//Existencia
		if ("".equals(datos.getOrigen()))
		{
			resultado.setCodigoResultado(CodigosResultadoValidacion.ORIGEN_VACIO);
			resultado.setValido(false);
		}
		else if ("".equals(datos.getModalidad()))
		{
			resultado.setCodigoResultado(CodigosResultadoValidacion.MODALIDAD_VACIA);
			resultado.setValido(false);
		}
		else if ("".equals(datos.getEmisora()) )
		{
			resultado.setCodigoResultado(CodigosResultadoValidacion.EMISORA_VACIA);
			resultado.setValido(false);
		}
		else if ("".equals(datos.getNifOperante()))
		{
			resultado.setCodigoResultado(CodigosResultadoValidacion.NIF_OPERANTE_VACIO);
			resultado.setValido(false);
		}
		//Validez de datos
		
		else if (("".equals(datos.getNifContribuyente()) && "".equals(datos.getNombreContribuyente())))
		{
			resultado.setCodigoResultado(CodigosResultadoValidacion.NIF_O_NOMBRE);
			resultado.setValido(false);
		}
		
		else if (!datos.getOrigen().equals(Constantes.getOrigenServicioWeb()) && (!datos.getOrigen().equals(Constantes.getOrigenPortal()))&&(!datos.getOrigen().equals(Constantes.getOrigenS1())))
		{
			resultado.setCodigoResultado(CodigosResultadoValidacion.ORIGEN_INVALIDO);
			resultado.setValido(false);
		}
		
		else if (!datos.getModalidad().equals(Constantes.getModalidadAutoliquidacion())&& !datos.getModalidad().equals(Constantes.getModalidadLiquidacion()))
		{
			resultado.setCodigoResultado(CodigosResultadoValidacion.MODALIDAD_ERRONEA);
			resultado.setValido(false);
		}
		else if ((!"".equals(datos.getNifContribuyente()) && !"".equals(datos.getNombreContribuyente())))
		{
			resultado.setCodigoResultado(CodigosResultadoValidacion.NIF_O_NOMBRE);
			resultado.setValido(false);
		}
		else if ("".equals(datos.getImporte()))
		{
			try
			{
				if (Integer.parseInt(datos.getImporte())==0)
				{
					resultado.setCodigoResultado(CodigosResultadoValidacion.IMPORTE_ERRONEO);
					resultado.setValido(false);
				}
			}
			catch (NumberFormatException ex)
			{
				// Si no puede parsear el importe, es que no es una cantidad en general, se pone el mismo error.
				resultado.setCodigoResultado(CodigosResultadoValidacion.IMPORTE_ERRONEO);
				resultado.setValido(false);
			}
		}
		
		//Formato de los NIF.
		if (resultado.isValido())
		{
			Varios util = new Varios();
			if (util.formateaNIF(datos.getNifOperante()).equals(""))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.NIF_INVALIDO);
				resultado.setValido(false);
			}
			else if (util.formateaNIF(datos.getNifContribuyente()).equals(""))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.NIF_INVALIDO);
				resultado.setValido(false);
			}
			
		}
		
		//CRUBENCVS. 04/05/2016. Si hay tarjeta, debe contener sólo números
		if (!"".equals(datos.getTarjeta()) && !Varios.formatoTarjetaValido(datos.getTarjeta()))
		{
			resultado.setCodigoResultado(CodigosResultadoValidacion.ERROR_TARJETA_PAGO);
			resultado.setValido(false);
		}
		return resultado;
	}

}
