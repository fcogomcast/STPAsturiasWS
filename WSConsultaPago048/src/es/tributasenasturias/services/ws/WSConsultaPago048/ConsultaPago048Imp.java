package es.tributasenasturias.services.ws.WSConsultaPago048;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import es.tributasenasturias.services.ws.WSConsultaPago048.bd.Datos;
import es.tributasenasturias.services.ws.WSConsultaPago048.utils.Log.Logger;
import es.tributasenasturias.services.ws.WSConsultaPago048.utils.Log.LogHandler.ClientLogHandler;
import es.tributasenasturias.services.ws.WSConsultaPago048.utils.Preferencias.Preferencias;
import es.tributasenasturias.webservices.PasarelaPagoST;
import es.tributasenasturias.webservices.PasarelaPagoST_Service;
import es.tributasenasturias.webservices.ResultadoConsulta;
import es.tributasenasturias.webservices.SalidaConsulta;

public class ConsultaPago048Imp {

	Datos data = null;
	private Preferencias preferencias = null;
	private Logger logger=null;

	/**
	 * Constructor por defecto
	 */
	public ConsultaPago048Imp()
	{
		
		//Realizamos inicializaciones
		data = new Datos();
		logger= new Logger();
		logger.trace("Inicio Constructor ConsultaPago048Imp ");
		try
		{
			//logger.trace("ENTRADA EN CONSULTAPAGO048IMP()");
			preferencias = Preferencias.getPreferencias();
			//logger.trace("SALIDA DE CONSULTAPAGO048IMP()");
		}
	catch (Exception ex)
	{
		
	}
		
	}
	
	/**
	 * 
	 * @param Autoliquidacion
	 * @param Bastidor
	 */
	public ResultadoConsulta Ejecutar(String Autoliquidacion, String Bastidor)
	{
		//Instanciamos un objeto de tipo salida del método de consulta de pasarela de pago
		ResultadoConsulta resultado = new ResultadoConsulta();
		try
		{
			logger.trace("INICIO CONSULTAPAGO048IMP/EJECUTAR");
			logger.trace("a)Comprobando existencia Autoliquidación...");
			//1.Comprobamos que existe el nº de autoliquidación
			if(!data.ValidarAutoliquidacion(Autoliquidacion))
			{
				
				logger.trace("Resultado: No existe autoliquidación " + Autoliquidacion);
				SalidaConsulta salida = new SalidaConsulta();
				salida.setResultado(preferencias.getMsgErrorNoExisteAutoliquidacion());
				salida.setError(preferencias.getCodErrorNoExisteAutoliquidacion());
				salida.setFechaPago("");
				salida.setOperacion("");
				resultado.setRespuesta(salida);
				
				return resultado;
			}
			logger.trace("Resultado: Sí existe autoliquidación " + Autoliquidacion);
			
			//2.Validamos que la autoliquidación y el nº de bastidor se correspondan.
			logger.trace("b) Comprobando correspondencia entre Autoliquidación y Bastidor...");
			if (data.ValidarAutoliquidacionYBastidor(Autoliquidacion, Bastidor))
			{		
				logger.trace("Resultado: Autoliquidación " + Autoliquidacion + " y Bastidor "+ Bastidor  + " validados");
				//2.Llamamos al WS de pasarela de pago para comprobar que el nº de autoliquidación está pagado.

				//2.1 Creamos el servicio y el interfaz del servicio
				//logger.trace("WSDLLocation: " + preferencias.getWSDLLocationPasarelaPagoST());
				PasarelaPagoST_Service pasarelaService = new PasarelaPagoST_Service(new URL( preferencias.getWSDLLocationPasarelaPagoST()),new QName("http://webservices.tributasenasturias.es/","PasarelaPagoST"));
				PasarelaPagoST port = pasarelaService.getPasarelaPagoSTPort();

				//2.2 Se modifica el tiempo de ejecución el endpoint del interfaz de servicio.
				BindingProvider bp = (BindingProvider)port;
				bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, preferencias.getEndPointPasarelaPagoST());
				//Necesario para establecer el log SOAP_CLIENT
				//1.Obtenemos los enlaces
				Binding bi = bp.getBinding();
				//2.De entre ellos obtenemos la lista de cadenas de manejadores
				List<Handler> handlerList = bi.getHandlerChain();
				//3.Si no existen cadenas de manejadores creamos una vacía
				if (handlerList== null)
					handlerList = new ArrayList<Handler>();
				//4.Añadimos un nuevo manejador
				handlerList.add(new ClientLogHandler());
				//5.Actualizamos la lista de manejadores
				bi.setHandlerChain(handlerList);
				
				logger.trace("Parámetros de la llamada************************************");
				logger.trace("EndPoint pasarela de pago:" + preferencias.getEndPointPasarelaPagoST());
				logger.trace("Origen: " + preferencias.getOrigen());
				logger.trace("Modalidad: " + preferencias.getModalidad());
				logger.trace("Entidad: " + preferencias.getEntidad());
				logger.trace("Num.Autoliquidacion: " + Autoliquidacion);
				//2.3 Realizamos la llamada al servicio web.
				resultado = port.consultaCobros(preferencias.getOrigen(), preferencias.getModalidad(), "", preferencias.getEntidad(), "", "", Autoliquidacion, "", "");
				
				logger.trace("Resultado consulta*****************************************");
				logger.trace("Error: " + resultado.getRespuesta().getError());
				logger.trace("FechaPago: " + resultado.getRespuesta().getFechaPago());
				logger.trace("Resultado: " + resultado.getRespuesta().getResultado());
				logger.trace("Operacion (NRC): " + resultado.getRespuesta().getOperacion());
				
				//2.4 Retornamos el resultado
				logger.trace("FIN CONSULTAPAGO048IMP/EJECUTAR");

				//Encapsulamos los posibles mesajes de la pasarela
				return EncapsularRespuesta(resultado);
				
			}
			else 
			{
				logger.trace("Resultado: Autoliquidación " + Autoliquidacion + " y Bastidor "+ Bastidor  + " no se corresponden");
				logger.trace(preferencias.getMsgErrorValidacionAutoliquidacionYBastidor());
				SalidaConsulta salida = new SalidaConsulta();
				salida.setResultado(preferencias.getMsgErrorValidacionAutoliquidacionYBastidor());
				salida.setError(preferencias.getCodErrorValidacionAutoliquidacionYBastidor());
				salida.setFechaPago("");
				salida.setOperacion("");
				resultado.setRespuesta(salida);
				
				return resultado;
			}
		}
		catch (Exception e) {
			System.out.println(e);
			logger.error(e.getMessage());
			//Definimos el mensaje de error en caso de excepción
			SalidaConsulta salida = new SalidaConsulta();
			salida.setError("Excepcion controlada: " + e.getMessage());
			resultado.setRespuesta(salida);
			return resultado;
			
		}
	}

	/**
	 * Establece los valores de respuesta finales condicionados al los pasados
	 * como parámetro
	 * @param resultado a encapsular
	 * @return ResultadoConsulta modificado
	 */
	private ResultadoConsulta EncapsularRespuesta(ResultadoConsulta resultado)
	{
		logger.trace("INICIO ENCAPSULARRESPUESTA");
		logger.trace("Error recibido: " + resultado.getRespuesta().getError() + ": " + resultado.getRespuesta().getResultado() );
		if (resultado.getRespuesta().getError().equals(preferencias.getCodPasarelaTributoPagado())) 
		{
			resultado.getRespuesta().setError(preferencias.getCodTributoPagado());
			resultado.getRespuesta().setResultado(preferencias.getMsgTributoPagado());
		}
		else if (resultado.getRespuesta().getError().equals(preferencias.getCodPasarelaTributoNoPagado()))
		{
			resultado.getRespuesta().setError(preferencias.getCodTributoNoPagado());
			resultado.getRespuesta().setResultado(preferencias.getMsgTributoNoPagado());
		}
		else
		{
			resultado.getRespuesta().setError(preferencias.getCodOtrosErrores());
			resultado.getRespuesta().setResultado(preferencias.getMsgOtrosErrores());
		}
		logger.trace("Error retornado: " + resultado.getRespuesta().getError() + ": " + resultado.getRespuesta().getResultado());
		logger.trace("FIN ENCAPSULARRESPUESTA");
		return resultado;
	}
}
