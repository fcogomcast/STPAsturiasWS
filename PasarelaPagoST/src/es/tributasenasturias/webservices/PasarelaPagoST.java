package es.tributasenasturias.webservices;

import javax.annotation.Resource;
import javax.jws.*;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.WebServiceContext;

import es.tributasenasturias.conversor.PeticionServicioFactory;
import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.exceptions.PreferenciasException;
import es.tributasenasturias.objetos.AnulacionPago;
import es.tributasenasturias.objetos.AnulacionPagoTarjeta;
import es.tributasenasturias.objetos.ConsultaPago;
import es.tributasenasturias.objetos.InicioOperacionPago;
import es.tributasenasturias.objetos.InicioPagoTarjeta;
import es.tributasenasturias.objetos.ObjetosFactory;
import es.tributasenasturias.objetos.PeticionPago;
import es.tributasenasturias.plataforma.unipay.UniversalPay;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.CallContextManager;
import es.tributasenasturias.webservices.parametros.ResultadoAnulacion;
import es.tributasenasturias.webservices.parametros.ResultadoConsulta;
import es.tributasenasturias.webservices.parametros.ResultadoPeticion;
import es.tributasenasturias.webservices.types.AnulacionPagoTarjetaRequest;
import es.tributasenasturias.webservices.types.AnulacionPagoTarjetaResponse;
import es.tributasenasturias.webservices.types.InicioOperacionPagoRequest;
import es.tributasenasturias.webservices.types.InicioOperacionPagoResponse;
import es.tributasenasturias.webservices.types.InicioPagoTarjetaRequest;
import es.tributasenasturias.webservices.types.InicioPagoTarjetaResponse;
import es.tributasenasturias.webservices.types.ResultadoConsultaPagoTarjeta;
import es.tributasenasturias.tarjetas.unicajaInterfaz.resultado.RecepcionResultadoPagoUnicaja;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.GeneradorIdSesion;
import es.tributasenasturias.utils.GestorIdLlamada;
import es.tributasenasturias.utils.JustificantePago;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.utils.Preferencias;

@WebService (serviceName="PasarelaPagoST")
@HandlerChain(file = "LogMessage_handler.xml")
public class PasarelaPagoST {

	@Resource
		WebServiceContext wcontexto;
	@WebMethod (operationName="Peticion")
	public ResultadoPeticion Peticion( @WebParam (name="origen")String origen,
			@WebParam (name="modalidad") String modalidad,
			@WebParam (name="cliente") String cliente,
			@WebParam (name="entidad") String entidad,
			@WebParam (name="emisora")String emisora,
			@WebParam (name="modelo") String modelo,
			@WebParam (name="nif")String nifContribuyente,
			@WebParam (name="nombreContribuyente")String nombreContribuyente,
			@WebParam (name="fecha_devengo")String fechaDevengo,
			@WebParam (name="dato_especifico")String datoEspecifico,
			@WebParam (name="identificacion")String identificacion,
			@WebParam (name="referencia")String referencia,
			@WebParam (name="numero_autoliquidacion")String justificante,
			@WebParam (name="expediente")String expediente,
			@WebParam (name="importe")String importe,
			@WebParam (name="tarjeta")String tarjeta,
			@WebParam (name="fecha_caducidad")String fechaCaducidadTarjeta,
			@WebParam (name="ccc")String ccc,
			@WebParam (name="nif_operante")String nifOperante,
			@WebParam (name="aplicacion")String aplicacion,
			@WebParam (name="numero_unico")String numeroUnico,
			@WebParam (name="numero_peticion") String numeroPeticion,
			@WebParam (name="libre")String libre,
			@WebParam (name="mac") String mac
			) {
		Logger logger=new Logger();
		try
		{
			CallContext context = inicializaContexto(logger);
			//Hacemos la petición.
			ResultadoPeticion res= null;
			DatosEntradaServicio peti = PeticionServicioFactory.getPeticionServicioDO (origen, modalidad, cliente, emisora,
				modelo, entidad, nifContribuyente, nombreContribuyente,
				fechaDevengo, datoEspecifico, identificacion, referencia,
				justificante, expediente, importe, tarjeta,
				fechaCaducidadTarjeta, ccc, nifOperante, aplicacion,
				numeroUnico, numeroPeticion, libre, mac);
		
			PeticionPago pago = ObjetosFactory.newPeticionPago(peti,context); 
			res = pago.ejecutar();
			//*
			//*  Generación de MAC de respuesta.
			//*
			//*
			if (origen.equalsIgnoreCase(Constantes.getOrigenServicioWeb()) && modalidad.equalsIgnoreCase(Constantes.getModalidadAutoliquidacion()))
			{
				try
				{
				res.setMac(cliente);
				}
				catch (Exception ex)
				{
					logger.debug("Rellenamos el objeto a devolver");
					logger.error(Mensajes.getErrorMacGeneracion() + ".-Error al generar la MAC de respuesta:" + ex.getMessage());
					logger.trace(ex.getStackTrace());
					res.getRespuesta().setMessage(Mensajes.getErrorMacGeneracion());
				}
			}
			return res;
		}
		catch (Exception ex)
		{
			ResultadoPeticion res = new ResultadoPeticion();
			res.getRespuesta().setError(Constantes.getErrorGenericoPago());
			logger.error("Error imprevisto en la petición de pago : "+ ex.getMessage());
			logger.trace(ex.getStackTrace());
			return res;
		}
		finally
		{
			GestorIdLlamada.desasociarIdLlamada();
		}
	}
	@WebMethod (operationName="ConsultaCobros") 
	public ResultadoConsulta Consulta(@WebParam (name="origen")String origen,
			@WebParam (name="modalidad") String modalidad,
			@WebParam (name="cliente") String cliente,
			@WebParam (name="emisora")String emisora,
			@WebParam (name="identificacion")String identificacion,
			@WebParam (name="referencia")String referencia,
			@WebParam (name="numero_autoliquidacion")String justificante,
			@WebParam (name="aplicacion") String aplicacion,
			@WebParam (name="numero_unico") String numeroUnico,
			@WebParam (name="envio_justificante") String envioJustificante,
			@WebParam (name="libre") String libre,
			@WebParam (name="mac") String mac)
	{
		Logger logger=new Logger();
		try
		{
			CallContext context = inicializaContexto(logger);
			DatosEntradaServicio peti= PeticionServicioFactory.getPeticionServicioDO (origen, modalidad, cliente, emisora,
					"", "", "", "",
					"", "", identificacion, referencia,
					justificante, "", "", "",
					"", "", "", aplicacion,
					numeroUnico, "", libre, mac);
			
			ConsultaPago con = ObjetosFactory.newConsultaPago(peti, context); 
			ResultadoConsulta res = con.ejecutar();
			//*
			//*  Generación de MAC de respuesta.
			//*
			//*
			if (origen.equalsIgnoreCase(Constantes.getOrigenServicioWeb()) && modalidad.equalsIgnoreCase(Constantes.getModalidadAutoliquidacion()))
			{
				try
				{
				res.setMac(cliente);
				}
				catch (Exception ex)
				{
					logger.debug("Rellenamos el objeto a devolver");
					logger.error(Mensajes.getErrorMacGeneracion() + ".-Error al generar la MAC de respuesta:" + ex.getMessage());
					logger.trace(ex.getStackTrace());
					res.getRespuesta().setMessage(Mensajes.getErrorMacGeneracion());
				}
			}
			if ("S".equalsIgnoreCase(envioJustificante) && Mensajes.getTributoPagado().equals(res.getRespuesta().getError()))
			{
				res.getRespuesta().setDoc_justificante(new JustificantePago(context).getJustificante(res.getPeticion().getNumero_autoliquidacion()));
			}
			return res;
		}
		catch (Exception ex)
		{
			ResultadoConsulta res = new ResultadoConsulta();
			res.getRespuesta().setMessage(Constantes.getErrorGenericoConsulta());
			logger.error("Error imprevisto en la consulta de pago : " + ex.getMessage());
			logger.trace (ex.getStackTrace());
			return res;
		}
		finally
		{
			GestorIdLlamada.desasociarIdLlamada();
		}
		
	}
	@WebMethod (operationName="Anulacion")
	public ResultadoAnulacion Anulacion( @WebParam (name="origen")String origen,
			@WebParam (name="modalidad") String modalidad,
			@WebParam (name="cliente") String cliente,
			@WebParam (name="emisora")String emisora,
			@WebParam (name="modelo") String modelo,
			@WebParam (name="nif")String nifContribuyente,
			@WebParam (name="nombreContribuyente")String nombreContribuyente,
			@WebParam (name="fecha_devengo")String fechaDevengo,
			@WebParam (name="dato_especifico")String datoEspecifico,
			@WebParam (name="identificacion")String identificacion,
			@WebParam (name="referencia")String referencia,
			@WebParam (name="numero_autoliquidacion")String justificante,
			@WebParam (name="expediente")String expediente,
			@WebParam (name="importe")String importe,
			@WebParam (name="tarjeta")String tarjeta,
			@WebParam (name="fecha_caducidad")String fechaCaducidadTarjeta,
			@WebParam (name="ccc")String ccc,
			@WebParam (name="nif_operante")String nifOperante,
			@WebParam (name="aplicacion")String aplicacion,
			@WebParam (name="numero_unico")String numeroUnico,
			@WebParam (name="numero_peticion") String numeroPeticion,
			@WebParam (name="libre")String libre,
			@WebParam (name="mac") String mac
			) {
		Logger logger=new Logger();
		try
		{
			CallContext context = inicializaContexto(logger);
			ResultadoAnulacion res= null;
			DatosEntradaServicio peti = PeticionServicioFactory.getPeticionServicioDO (origen, modalidad, cliente, emisora,
				modelo, "", nifContribuyente, nombreContribuyente,
				fechaDevengo, datoEspecifico, identificacion, referencia,
				justificante, expediente, importe, tarjeta,
				fechaCaducidadTarjeta, ccc, nifOperante, aplicacion,
				numeroUnico, numeroPeticion, libre, mac);
		
			AnulacionPago anulacion = ObjetosFactory.newAnulacionPago(peti,context);
			res = anulacion.ejecutar();
			//*
			//*  Generación de MAC de respuesta.
			//*
			//*
			if (origen.equalsIgnoreCase(Constantes.getOrigenServicioWeb()) && modalidad.equalsIgnoreCase(Constantes.getModalidadAutoliquidacion()))
			{
				try
				{
				res.setMac(cliente);
				}
				catch (Exception ex)
				{
					logger.debug("Rellenamos el objeto a devolver");
					logger.error(Mensajes.getErrorMacGeneracion() + ".-Error al generar la MAC de respuesta:" + ex.getMessage());
					res.getRespuesta().setMessage(Mensajes.getErrorMacGeneracion());
				}
			}
			return res;
		}
		catch (Exception ex)
		{
			ResultadoAnulacion res = new ResultadoAnulacion();
			res.getRespuesta().setError(Constantes.getErrorGenericoAnulacion());
			logger.error("Error imprevisto en la anulación de pago : "+ ex.getMessage());
			logger.trace(ex.getStackTrace());
			return res;
		}
		finally
		{
			GestorIdLlamada.desasociarIdLlamada();
		}
	}
	
	/**
	 * Recupera el id de sesión de contexto, o genera uno nuevo. Ya existirá si se activaron
	 * los manejadores SOAP.
	 * @return Id de sesión.
	 */
	private String recuperarIdSesion()
	{
		String idSesion = (String)wcontexto.getMessageContext().get(Constantes.ID_SESION);
		if (idSesion==null)
		{
			idSesion=GeneradorIdSesion.generaIdSesion();
		}
		return idSesion;
	}
	/**
	 * Inicializa el contexto de llamada
	 * @param logger Objeto Logger
	 * @return CallContext con los objetos ID_SESION, Logger y Preferencias
	 * @throws PreferenciasException
	 */
	private CallContext inicializaContexto (Logger logger) throws  PreferenciasException
	{
		//Recuperamos los objetos de contexto.
		String idSesion = recuperarIdSesion();
		//Preferencias.
		Preferencias pref = Preferencias.getPreferencias();
		logger.setPrefijo(idSesion);
		//Lo metemos a contexto.
		CallContext contexto= CallContextManager.newCallContext();
		contexto.setItem(CallContextConstants.ID_SESION, idSesion);
		contexto.setItem(CallContextConstants.PREFERENCIAS, pref);
		contexto.setItem(CallContextConstants.LOG_APLICACION, logger);
		//Nuevo, guardar el id de sesión en datos de contexto automáticamente.
		GestorIdLlamada.asociarIdLlamada(idSesion);
		return contexto;
	}
	
	@WebMethod (operationName="iniciaPagoTarjeta")
	@RequestWrapper(localName = "iniciaPagoTarjeta", targetNamespace = "http://webservices.tributasenasturias.es/", className = "es.tributasenasturias.webservices.types.InicioPagoTarjetaRequest")
	public InicioPagoTarjetaResponse iniciaPagoTarjeta( 
			@WebParam (name="origen")String origen,
			@WebParam (name="modalidad") String modalidad,
			@WebParam (name="entidad") String entidad,
			@WebParam (name="emisora")String emisora,
			@WebParam (name="modelo") String modelo,
			@WebParam (name="nifContribuyente")String nifContribuyente,
			@WebParam (name="fechaDevengo")String fechaDevengo,
			@WebParam (name="datoEspecifico")String datoEspecifico,
			@WebParam (name="identificacion")String identificacion,
			@WebParam (name="referencia")String referencia,
			@WebParam (name="numeroAutoliquidacion")String justificante,
			@WebParam (name="expediente")String expediente,
			@WebParam (name="importe")String importe,
			@WebParam (name="nifOperante")String nifOperante,
			@WebParam (name="plataformaPago")String plataformaPago
			) {
		Logger logger=new Logger();
		InicioPagoTarjetaResponse response= null;
		try
		{
			CallContext context = inicializaContexto(logger);
			InicioPagoTarjetaRequest peticion= 
					new InicioPagoTarjetaRequest(
												origen,
												modalidad,
												entidad,
												emisora,
												modelo,
												nifContribuyente,
												fechaDevengo,
												datoEspecifico,
												identificacion,
												referencia,
												justificante,
												expediente,
												importe,
												nifOperante,
												plataformaPago);
												;
			
			
			InicioPagoTarjeta inicio= ObjetosFactory.newIniciaPagoTarjeta(peticion, context);
			response= inicio.ejecutar();
			return response;
		}
		catch (Exception ex)
		{
			logger.error("Error imprevisto en el inicio de la operación de pago con tarjeta : "+ ex.getMessage());
			logger.trace(ex.getStackTrace());
			response= new InicioPagoTarjetaResponse();
			response.setEsError(true);
			response.setCodigo(Constantes.getErrorCode());
			response.setMensaje("Error imprevisto en el inicio de la operación de pago con tarjeta : "+ ex.getMessage());
			return response;
		}
		finally
		{
			GestorIdLlamada.desasociarIdLlamada();
		}
	}
	/**
	 * Consulta de pago por tarjeta para integración de plataforma de pago
	 * @param origen
	 * @param modalidad
	 * @param cliente
	 * @param emisora
	 * @param identificacion
	 * @param referencia
	 * @param justificante
	 * @param aplicacion
	 * @param numeroUnico
	 * @param envioJustificante
	 * @param libre
	 * @param mac
	 * @return
	 */
	@WebMethod (operationName="ConsultaPagoTarjeta") 
	public ResultadoConsultaPagoTarjeta ConsultaPagoTarjeta(@WebParam (name="origen")String origen,
			@WebParam (name="modalidad") String modalidad,
			@WebParam (name="emisora")String emisora,
			@WebParam (name="identificacion")String identificacion,
			@WebParam (name="referencia")String referencia,
			@WebParam (name="numero_autoliquidacion")String justificante,
			@WebParam (name="aplicacion") String aplicacion,
			@WebParam (name="numero_unico") String numeroUnico,
			@WebParam (name="envio_justificante") String envioJustificante,
			@WebParam (name="mac") String mac)
	{
		
		Logger logger=new Logger();
		try
		{
			CallContext context = inicializaContexto(logger);
			
			es.tributasenasturias.objetos.ConsultaPagoTarjeta con = ObjetosFactory.newConsultaPagoTarjeta(context); 
			ResultadoConsultaPagoTarjeta res = con.ejecutar(null2empty(origen),
											                null2empty(modalidad),
											                null2empty(emisora),
											                null2empty(identificacion),
											                null2empty(referencia),
											                null2empty(justificante),
											                null2empty(aplicacion),
											                null2empty(numeroUnico),
											                null2empty(mac));

			if ("S".equalsIgnoreCase(envioJustificante) && Mensajes.getTributoPagado().equals(res.getRespuesta().getError()))
			{
				res.getRespuesta().setDoc_justificante(new JustificantePago(context).getJustificante(res.getPeticion().getNumero_autoliquidacion()));
			}
			return res;
		}
		catch (Exception ex)
		{
			ResultadoConsultaPagoTarjeta res = new ResultadoConsultaPagoTarjeta();
			res.getRespuesta().setMessage(Constantes.getErrorGenericoConsulta());
			logger.error("Error imprevisto en la consulta de pago : " + ex.getMessage());
			logger.trace (ex.getStackTrace());
			return res;
		}
		finally
		{
			GestorIdLlamada.desasociarIdLlamada();
		}
	}
	/**
	 * 
	 * @param json
	 * @return
	 */
	@WebMethod(operationName="recibirResultadoPagoUniversalPay")
	public String recibirResultadoPagoUniversalPay(
			@WebParam(name="json")
			String json
	)
	{
		Logger logger=new Logger();
		try
		{
			CallContext context = inicializaContexto(logger);
			logger.info("INICIO RECEPCIÓN RESULTADO PAGO");
			UniversalPay up= new UniversalPay(context);
			return up.recibirResultadoPago(json);
		}
		catch (Exception ex)
		{
			logger.error("Error en recepción de resultado de pago:"+ex.getMessage());
			return "KO";
		}
		finally
		{
			logger.info("FIN RECEPCIÓN RESULTADO PAGO");
			GestorIdLlamada.desasociarIdLlamada();
		}	
	}
	/**
	 * Anula un pago con tarjeta en plataforma de pago
	 * @param origen
	 * @param modalidad
	 * @param cliente
	 * @param emisora
	 * @param identificacion
	 * @param referencia
	 * @param justificante
	 * @param aplicacion
	 * @param numeroUnico
	 * @param mac
	 * @return
	 */
	@WebMethod (operationName="anularPagoTarjeta")
	@RequestWrapper(localName = "anulacionPagoTarjeta", targetNamespace = "http://webservices.tributasenasturias.es/", className = "es.tributasenasturias.webservices.types.AnulacionPagoTarjetaRequest")
	public AnulacionPagoTarjetaResponse anularPagoTarjeta( 
			@WebParam (name="origen")String origen,
			@WebParam (name="modalidad") String modalidad,
			@WebParam (name="cliente") String cliente,
			@WebParam (name="emisora")String emisora,
			@WebParam (name="identificacion")String identificacion,
			@WebParam (name="referencia")String referencia,
			@WebParam (name="numero_autoliquidacion")String justificante,
			@WebParam (name="aplicacion") String aplicacion,
			@WebParam (name="numero_unico") String numeroUnico,
			@WebParam (name="mac") String mac
			) {
		Logger logger=new Logger();
		AnulacionPagoTarjetaResponse response=null;
		try
		{
			CallContext context = inicializaContexto(logger);
			AnulacionPagoTarjetaRequest request= 
						new AnulacionPagoTarjetaRequest(
							origen,
							modalidad,
							cliente,
							emisora,
							identificacion,
							referencia,
							justificante,
							aplicacion,
							numeroUnico,
							mac	
						);
			
			AnulacionPagoTarjeta anulacion= ObjetosFactory.newAnulacionPagoTarjeta(request, context);
			response= anulacion.ejecutar();
			
			//*
			//*  Generación de MAC de respuesta.
			//*
			//*
			if (origen.equalsIgnoreCase(Constantes.getOrigenServicioWeb()) && modalidad.equalsIgnoreCase(Constantes.getModalidadAutoliquidacion()))
			{
				try
				{
				response.setMac(cliente);
				}
				catch (Exception ex)
				{
					logger.debug("Rellenamos el objeto a devolver");
					logger.error(Mensajes.getErrorMacGeneracion() + ".-Error al generar la MAC de respuesta:" + ex.getMessage());
					response.setEsError(true);
					response.setCodigo(Mensajes.getErrorMacGeneracion());
					response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
				}
			}
			return response;
		}
		catch (Exception ex)
		{
			response= new AnulacionPagoTarjetaResponse();
			response.setEsError(true);
			response.setCodigo(Mensajes.getErrorAnulacion());
			response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
			logger.error("Error imprevisto en la anulación de pago : "+ ex.getMessage());
			return response;
		}
		finally
		{
			GestorIdLlamada.desasociarIdLlamada();
		}
	}
	/**
	 * Permite solicitar el inicio de una operación de pago por parte de Principado de Asturias 
	 * @param origen
	 * @param modalidad
	 * @param emisora
	 * @param modelo
	 * @param nifContribuyente
	 * @param fechaDevengo
	 * @param datoEspecifico
	 * @param importe
	 * @param nifOperante
	 * @param aplicacion
	 * @param numeroUnico
	 * @param mac
	 * @return
	 */
	@WebMethod (operationName="iniciaOperacionPago")
	public InicioOperacionPagoResponse inicioOperacionPago( 
			@WebParam (name="origen")String origen,
			@WebParam (name="modalidad") String modalidad,
			@WebParam (name="emisora")String emisora,
			@WebParam (name="modelo")String modelo,
			@WebParam (name="nif_contribuyente")String nifContribuyente,
			@WebParam (name="fecha_devengo")String fechaDevengo,
			@WebParam (name="dato_especifico")String datoEspecifico,
			@WebParam (name="importe")String importe,
			@WebParam (name="nif_operante")String nifOperante,
			@WebParam (name="aplicacion")String aplicacion,
			@WebParam (name="numero_unico")String numeroUnico,
			@WebParam (name="mac") String mac
			) {
		
		
		InicioOperacionPagoResponse response=null;
		Logger logger=new Logger();
		try
		{
			CallContext context = inicializaContexto(logger);
			InicioOperacionPagoRequest request= 
				new InicioOperacionPagoRequest(
					origen,
					modalidad,
					emisora,
					modelo,
					nifContribuyente,
					fechaDevengo,
					datoEspecifico,
					importe,
					nifOperante,
					aplicacion,
					numeroUnico,
					mac
				);
			
			InicioOperacionPago inicio= ObjetosFactory.newInicioOperacionPago(request, context);
			response= inicio.ejecutar();
			return response;
		}
		catch (Exception ex)
		{
			logger.error("Error imprevisto en el inicio de la operación de pago: "+ ex.getMessage());
			logger.trace(ex.getStackTrace());
			response= new InicioOperacionPagoResponse();
			response.setEsError("S");
			response.setCodigo(Constantes.getErrorCode());
			response.setMensaje("Error imprevisto en el inicio de la operación de pago: "+ ex.getMessage());
			return response;
		}
		finally
		{
			GestorIdLlamada.desasociarIdLlamada();
		}
	}
	
	private static String null2empty(String valor) {
		return (valor == null ) ? "" : valor.trim();
	}
	
	//CRUBENCVS 47535 29/03/2023. Operaciones para integración con Unicaja Banco
	/**
	 * Recibe el JSON con la notificación del resultado de pago (correcto) 
	 * de Unicaja Banco.
	 * @param json
	 * @return
	 */
	@WebMethod(operationName="recibirNotificacionPagoUnicaja")
	public String recibirNotificacionPagoUnicaja(
			@WebParam (name="certificadoCliente")
			String certificadoCliente,
			@WebParam(name="json")
			String json
	)
	{
		Logger logger=new Logger();
		try
		{
			CallContext context = inicializaContexto(logger);
			logger.info("INICIO RECEPCIÓN RESULTADO PAGO UNICAJA");
			Preferencias pref= (Preferencias) context.get(CallContextConstants.PREFERENCIAS);
			Logger log = (Logger) context.get(CallContextConstants.LOG_APLICACION);
			String idSesion = (String) context.get(CallContextConstants.ID_SESION);
			return new RecepcionResultadoPagoUnicaja(pref, log, idSesion).recibirResultadoPago(certificadoCliente, json);
		}
		catch (Exception ex)
		{
			logger.error("Error en recepción de resultado de pago:"+ex.getMessage());
			return "KO";
		}
		finally
		{
			logger.info("FIN RECEPCIÓN RESULTADO PAGO UNICAJA");
			GestorIdLlamada.desasociarIdLlamada();
		}	
	}
	

	
}