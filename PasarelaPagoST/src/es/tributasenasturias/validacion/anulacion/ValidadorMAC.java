package es.tributasenasturias.validacion.anulacion;


import java.util.ArrayList;
import java.util.List;

import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.validacion.CodigosResultadoValidacion;
import es.tributasenasturias.validacion.IValidador;
import es.tributasenasturias.validacion.ResultadoIValidador;
import es.tributasenasturias.validacion.ValidacionException;


public final class ValidadorMAC implements IValidador<DatosEntradaServicio>{

	private List<String> datosValidacion=new ArrayList<String>();
	@Override
	/**
	 * Valida la MAC pasada como parámetro de entrada.
	 * Los datos de comprobación de la MAC y la mac generada se podrán consultar una vez realizada la 
	 * validación mediante "getDatosComprobacion" y "getMacGenerada"
	 */
	public final ResultadoIValidador validar(DatosEntradaServicio datos) throws ValidacionException{
		ResultadoIValidador resultado = new ResultadoIValidador();
		resultado.setModulo(this.getClass().getName());
		resultado.setValido(true);
		String mac = datos.getMac();
		if (datos.getOrigen().equals(Constantes.getOrigenServicioWeb()) && datos.getModalidad().equalsIgnoreCase(Constantes.getModalidadAutoliquidacion()))
		{
			if(mac != null && mac.length() > 0)
			{
				String data = datos.getCliente()+datos.getEmisora()+datos.getModelo()+datos.getJustificante()+datos.getNifContribuyente()+
							  datos.getNombreContribuyente()+datos.getFechaDevengo()+datos.getDatoEspecifico()+
							  datos.getExpediente()+datos.getReferencia()+datos.getIdentificacion()+
							  datos.getImporte()+datos.getCcc()+datos.getTarjeta()+datos.getFechaCaducidadTarjeta()+
							  datos.getNifOperante()+datos.getAplicacion()+datos.getNumeroUnico()+datos.getNumeroPeticion()+datos.getLibre();
				datosValidacion.add("["+this.getClass().getName()+"]::datos de los que generar el MAC:"+data);
				try
				{
					String generatedMac = es.tributasenasturias.utils.GenerateMac.calculateHex(data,datos.getCliente());
					datosValidacion.add("["+this.getClass().getName()+"]::Mac Generado:"+generatedMac);
					if(mac.equals(generatedMac))
					{
						resultado.setValido(true);
					}
					else
					{
						resultado.setValido(false);
						resultado.setCodigoResultado(CodigosResultadoValidacion.MAC_INVALIDO);
					}
				}
				catch (Exception ex)
				{
					throw new ValidacionException (ex.getMessage(),ex);
				}
			}
			else
			{
				resultado.setValido(false);
				resultado.setCodigoResultado(CodigosResultadoValidacion.MAC_VACIA);
			}
		}
		return resultado;
	}
	
	@Override
	public List<String> getDatosValidacion() {
		return datosValidacion;
	}
	
	
}
