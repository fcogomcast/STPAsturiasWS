package es.tributasenasturias.validacion.anulacion;

import java.util.ArrayList;
import java.util.List;

import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.validacion.CodigosResultadoValidacion;
import es.tributasenasturias.validacion.IValidador;
import es.tributasenasturias.validacion.ResultadoIValidador;
import es.tributasenasturias.validacion.ValidacionException;

public final class ValidadorOrigenServicioAnulacion implements IValidador<DatosEntradaServicio>{


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
			if (!datos.getModalidad().equals(Constantes.getModalidadAutoliquidacion()) && datos.getOrigen().equals(Constantes.getOrigenServicioWeb()))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.MODALIDAD_AUTOLIQ_SW);
				resultado.setValido(false);
			}
			else if(datos.getModalidad().equals(Constantes.getModalidadAutoliquidacion()) && "".equals(datos.getCliente()))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.CLIENTE_VACIO);
				resultado.setValido(false);
			}
			else if ("".equals(datos.getJustificante()))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.JUSTIFICANTE_VACIO);
				resultado.setValido(false);
			}
			else if (!datos.getModelo().equals("046"))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.MODELO_NO_046);
				resultado.setValido(false);
			}
			else if ("".equals(datos.getMac())) //Nunca debería ser cierto ya que se debería validar la MAC antes.
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.MAC_VACIA);
				resultado.setValido(false);
			}
			else if(datos.getModalidad().equals(Constantes.getModalidadAutoliquidacion()) && ("".equals(datos.getAplicacion()) || "".equals(datos.getNumeroUnico())))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.APLICACION_NUMERO_UNICO_VACIO);
				resultado.setValido(false);
			}
			//Se elimina la validación de un medio de pago en la anulación, para poder realizar 
			//esta operación desde Tributas
			/*else if ("".equals(datos.getTarjeta()) || "".equals(datos.getFechaCaducidadTarjeta()))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.DATOS_TARJETA_INCOMPLETOS);
				resultado.setValido(false);
			}*/
			else if (!"".equals(datos.getCcc()))
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.CCC_INNECESARIO);
				resultado.setValido(false);
			}
			else if(datos.getModelo().equals("046"))
			{
				if("".equals(datos.getFechaDevengo())|| "".equals(datos.getDatoEspecifico()))
				{
					resultado.setCodigoResultado(CodigosResultadoValidacion.FECHA_DEVENGO_ESPECIFICO_VACIO);
					resultado.setValido(false);
				}
			}
		}
		return resultado;

	}
}
