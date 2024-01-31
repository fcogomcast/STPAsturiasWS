package es.tributasenasturias.validacion.consulta;

import java.util.ArrayList;
import java.util.List;

import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.validacion.CodigosResultadoValidacion;
import es.tributasenasturias.validacion.IValidador;
import es.tributasenasturias.validacion.ResultadoIValidador;
import es.tributasenasturias.validacion.ValidacionException;

public final class ValidadorComunesConsulta implements IValidador<DatosEntradaServicio>{

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
		else if ("".equals(datos.getEmisora()))
		{
			resultado.setCodigoResultado(CodigosResultadoValidacion.EMISORA_VACIA);
			resultado.setValido(false);
		}
		//Validez de datos
				
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
		
		if (resultado.isValido())
		{
			int [] parametros = new int[3]; //Se guardará un 1 por cada grupo de parámetros: 
											//aplicacion-número único, identificación- referencia
											//justificante
			if (datos.getAplicacion()!=null && !"".equals(datos.getAplicacion()) && 
					 datos.getNumeroUnico()!=null && !"".equals(datos.getNumeroUnico()))
			{
				parametros[0] =1;
			}
			else
			{
				parametros[0]=0;
			}
			
			if (datos.getIdentificacion()!=null && !"".equals(datos.getIdentificacion()) &&
					  datos.getReferencia()!=null && !"".equals(datos.getReferencia()))
			{
				parametros[1] = 1;
			}
			else
			{
				parametros[1]= 0;
			}
			if (datos.getJustificante()!=null && !"".equals(datos.getJustificante()))
			{
				parametros[2]= 1;
			}
			else
			{
				parametros[2]= 0;
			}
			
			//Comprobamos que haya uno y sólo un grupo de parámetros
			int totGrupos = parametros[0]+ parametros[1]+ parametros[2];
			if (totGrupos>1 || totGrupos==0)
			{
				resultado.setCodigoResultado(CodigosResultadoValidacion.ERROR_CRITERIO_CONSULTA);
				resultado.setValido(false);
			}
		}
		
		return resultado;
	}

}
