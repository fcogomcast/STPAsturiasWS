package es.stpa.smartmultas.configuracion.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

import com.google.gson.Gson;

import es.stpa.smartmultas.entidades.EtiquetasBoletin;
import es.stpa.smartmultas.entidades.EtiquetasList;
import es.stpa.smartmultas.entidades.Nota;
import es.stpa.smartmultas.entidades.Propiedad;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.stpa.smartmultas.responses.MultasConfiguracionResponse;
import es.stpa.smartmultas.soap.SoapClientHandler;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.services.lanzador.client.LanzadorFactory;
import es.tributasenasturias.services.lanzador.client.ParamType;
import es.tributasenasturias.services.lanzador.client.ProcedimientoAlmacenado;
import es.tributasenasturias.services.lanzador.client.TLanzador;
import es.tributasenasturias.services.lanzador.client.response.RespuestaLanzador;
import es.tributasenasturias.xml.XMLDOMUtils;

public class ConfiguracionUtils {

	final static String WTEA = "WTEA_ESTRUCTURA_ARQUITECTURA";
	final static String ES05 = "ES05_ESTRUCTURA_UNI05";
	final static String ESUN = "ESUN_ESTRUCTURA_UNIVERSAL";
	
	
	public static boolean ValidarUDID(Preferencias pref, String idLlamada, String udid, Integer idSubo) {

		boolean esValido = false;

		try 
		{
			TLanzador lanzador = LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.VALIDARDISPOSITIVOMOVIL", pref.getEsquemaBD());
			Utiles.AgregarCabeceraGeneralPL(proc);

			proc.param(idSubo + "", ParamType.NUMERO);
			proc.param(udid, ParamType.CADENA);

			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);

			if (!response.esErronea() && response.getNumFilasEstructura(ES05) > 0) 
			{
				String datosDisp = response.getValue(ES05, 1, "C1");

				esValido = datosDisp.equals(udid);
			}
		} 
		catch (Exception ex) 
		{
			esValido = false;
		}

		return esValido;
	}
	
	
	public static String GetTextoValidacionUDID(Preferencias pref, String idLlamada, Document datosEntrada, Integer idSubo, ILog log) {
		
		int codigo = 1;
		String mensaje = "Dispositivo no admitido.";
		String datosRespuesta = null;

		try 
		{
			String udid = datosEntrada.getElementsByTagName("UDID").item(0).getTextContent();
			
			if (!ValidarUDID(pref, idLlamada, udid, idSubo)) 
			{
				datosRespuesta = Utiles.MensajeErrorXml(codigo, mensaje, log);
			}
		} 
		catch (Exception ex) 
		{
			datosRespuesta = Utiles.MensajeErrorXml(codigo, mensaje, log);
		}

		return datosRespuesta;
	}


	public static boolean ValidarPermisos(Preferencias pref, String idLlamada, String codPropiedad) {
		
		boolean resultado = false;
		
		try 
		{
			String valorProp = Utiles.ObtenerValorPropiedad(pref, idLlamada, codPropiedad);

			if (valorProp == null) 
			{	
				resultado = true;
			} 
			else if (valorProp.toUpperCase().equals("TRUE")) 
			{
				resultado = true;
			}
		}
		catch (Exception ex)
		{
			resultado = false;
		}

		return resultado;
	}
	

	public static String ObtenerDetallesConfig(Preferencias pref, String idLlamada, ILog log, Document datosEntrada, int idSubo, boolean etiquetas) {

		String datosRespuesta = null;

		try 
		{
			String udid = datosEntrada.getElementsByTagName("UDID").item(0).getTextContent();

			TLanzador lanzador = LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("SMARTFINES.DETALLECONFIGSF", pref.getEsquemaBD());
			Utiles.AgregarCabeceraGeneralPL(proc);

			proc.param(idSubo + "", ParamType.NUMERO);
			proc.param(udid, ParamType.CADENA);

			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);

			if (!response.esErronea() && response.getNumFilasEstructura(WTEA) > 0) 
			{
				String pago = Utiles.ObtenerValorPropiedadPago(pref, idLlamada);

				// Rellenamos con los datos generales
				MultasConfiguracionResponse config = new MultasConfiguracionResponse(
								response.getValue(WTEA, 1, "C1"), 					//idioma
								idSubo, 											// municipio
								Integer.parseInt(response.getValue(WTEA, 1, "N1")),	// tiempoRetencion
								response.getValue(WTEA, 1, "C2"), 					// datosConexion
								response.getValue(WTEA, 1, "CL1"), 					// impresion
								response.getValue(WTEA, 1, "C3"), 					// textoGeneral
								response.getValue(WTEA, 1, "C4"),					// textoAyuntamiento
								response.getValue(WTEA, 1, "CL2"), 					// logoBase64
								response.getValue(WTEA, 1, "C5"), 					// codDispositivo
								response.getValue(WTEA, 1, "C6"), 					// ultimoNumeroBoletin
								response.getValue(WTEA, 1, "C7"), 					// urlCsv
								pago);												// pago

				if (etiquetas) 
				{
					String err = datosReferenciaC603(pref, idLlamada, log, idSubo, response, config);
					if (err.startsWith("Error")) { throw new Exception(err); }
				}

				datosRespuesta = XMLDOMUtils.Serialize(config);
			} 
			else 
			{
				datosRespuesta = Utiles.MensajeErrorXml(0, "Datos de acceso no válidos.", log);
			}
		} 
		catch (Exception ex) 
		{
			datosRespuesta = Utiles.MensajeError(ex.getMessage(), log);
		}

		return datosRespuesta;
	}

	private static String datosReferenciaC603(Preferencias pref, String idLlamada, ILog log, Integer idSubo, RespuestaLanzador response, MultasConfiguracionResponse config) {

		String err = "";

		try 
		{
			// Generar referencias C60.3
			String idAppC603 = Utiles.ObtenerValorPropiedad(pref,idLlamada, "SMARTFINES.IDENTIFICADORAPLICACIONC603");
			String tipoFormC603 = Utiles.ObtenerValorPropiedad(pref, idLlamada, "SMARTFINES.TIPOFORMATOC603");
			String ultimoC603 = response.getValue(WTEA, 1, "N2");

			config.setIdentificadorAplicacionC603(idAppC603);
			config.setTipoFormatoC603(tipoFormC603);
			config.setEmisoraC603(response.getValue(WTEA, 1, "C10"));
			config.setCodigoTributoC603(response.getValue(WTEA, 1, "C9"));
			config.setIndicadorCapturarC603(0);
			config.setUltimoC603((ultimoC603 != null && !ultimoC603.isEmpty()) ? Integer.parseInt(ultimoC603): null);

			// sólo tendremos 1 idioma en este caso
			List<EtiquetasBoletin> etiquetasBoletin = new ArrayList<EtiquetasBoletin>();
			Map<String, String> tmp = obtenerEtiquetas(pref, idLlamada);

			String tmpJson = new Gson().toJson(tmp);
			etiquetasBoletin.add(new Gson().fromJson(tmpJson, EtiquetasBoletin.class));

			config.setEtiquetas(new EtiquetasList(etiquetasBoletin));
		} 
		catch (Exception ex) 
		{
			err = Utiles.MensajeError("Error: " + ex.getMessage(), log);
		}

		return err;
	}

	// Etiquetas fijas del boletín de denuncia
	private static Map<String, String> obtenerEtiquetas(Preferencias pref, String idLlamada) {

		Map<String, String> traducciones = new HashMap<String, String>();

		try 
		{
			// sólo un idioma (ESM) en Asturias; IdOrga = 0, Genérico, por defecto.
			TLanzador lanzador = LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.OBTENER_ETIQUETAS_BOLETIN", pref.getEsquemaBD());
			Utiles.AgregarCabeceraGeneralPL(proc);

			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);

			int numFilas = response.getNumFilasEstructura(ESUN);
			if (!response.esErronea() && numFilas > 0) 
			{
				for (int i = 1; i <= numFilas; i++) 
				{
					traducciones.put(response.getValue(ESUN, i, "C2"), response.getValue(ESUN, i, "C3"));
				}
			}
		} 
		catch (Exception ex) { } // debe continuar

		return traducciones;
	}
	
	public static String GuardarDatosNotaEnExpediente(Preferencias pref, String idLlamada, ILog log, String agente, Nota nuevaNota) {

		String resultado = "Ok";

		try 
		{
			if (agente != null && agente.trim() != "") 
			{
				TLanzador lanzador = LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(idLlamada));
				ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.NUEVANOTAEXP", pref.getEsquemaBD());
				Utiles.AgregarCabeceraGeneralPL(proc);

				proc.param("SMARTFINES", ParamType.CADENA); 			// Usuario
				proc.param(nuevaNota.getNumRege(), ParamType.CADENA);
				proc.param(nuevaNota.getDescripcion(), ParamType.CADENA);
				proc.param(nuevaNota.getFechaAgenda(), ParamType.CADENA);
				proc.param(nuevaNota.getHoraDesde(), ParamType.CADENA);
				proc.param(nuevaNota.getHoraHasta(), ParamType.CADENA);
				proc.param(agente, ParamType.CADENA);

				String soapResponse = lanzador.ejecutar(proc);
				RespuestaLanzador response = new RespuestaLanzador(soapResponse);

				if (!response.esErronea() && response.getNumFilasEstructura(ES05) > 0 
						&& response.getValue(ES05, 1, "C1").toUpperCase().equals("OK")) 
				{
					// Si va correctamente no devolvemos nada, sólo Ok
				} 
				else 
				{
					resultado = Utiles.MensajeErrorXml(0, response.getTextoError(), log);
				}
			} 
			else 
			{
				resultado = "Debe indicar el agente";
			}
		} 
		catch (Exception ex) 
		{
			resultado = "Error: " + ex.getMessage();
		}

		return resultado;
	}
	
	
	public static String CustodiarArchivo(Preferencias pref, String idLlamada, ILog log, Document datosEntrada, 
										String identificador, String tipoElemento, String tipoDocumento,
										String nomFichero, String base64, Propiedad idAdarResultado) {
		
		String datosResultado = "";
		
		try
		{
			// 1. Custodiamos el documento y obtenemos un IDADAR
			String usuario = "SMARTFINES"; 
			
			TLanzador lanzador = LanzadorFactory.newTLanzador(pref.getEndpointLanzadorMasivo(), new SoapClientHandler(idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.CUSTODIA_ARCHIVO", pref.getEsquemaBD());
	
			proc.param(usuario, ParamType.CADENA); 					//p_in_usuario
			proc.param(identificador, ParamType.CADENA); 			//p_in_id_interno_tributas
			proc.param(nomFichero, ParamType.CADENA); 				//p_in_nom_fichero
			proc.param(base64, ParamType.CLOB); 					//p_in_contenido_b64
			proc.param(tipoElemento, ParamType.CADENA); 			//p_in_tipo_elemento
			
			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);
			
			if (!response.esErronea() && response.getNumFilasEstructura(WTEA) > 0) 
			{		
				idAdarResultado.setValor(response.getValue(WTEA, 1, "N1")); // Variable auxiliar
				Integer idAdar = Integer.parseInt(response.getValue(WTEA, 1, "N1"));
						
				if(idAdar > 0) // 2. Si tenemos IDADAR lo asociamos con un expeditente de multas
				{
					datosResultado = AdjuntarImagenesExp(pref, idLlamada, log, datosEntrada, idAdar, tipoDocumento, nomFichero);	
				}
				else
				{
					datosResultado = Utiles.MensajeErrorXml(1, "Error al custodiar la imagen", log);
				}	
			}
			else
			{
				datosResultado = Utiles.MensajeErrorXml(1, "Error al custodiar la imagen: " + response.getTextoError(), log);
			}
		}
		catch (Exception e) {}
		
		return datosResultado;
	}
	
	public static String AdjuntarImagenesExp(Preferencias pref, String idLlamada, ILog log, Document datosEntrada, Integer idAdar,
												String tipoDocumento, String nomFichero)
	{
		
		String datosRespuestaAux = null;
		
		try
		{
			String idEper = datosEntrada.getElementsByTagName("IdEper").item(0).getTextContent(); 

			TLanzador lanzador = LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("EXPTRAFICO_CARGAS.ADJUNTARIMAGENESEXP", pref.getEsquemaBD());
			Utiles.AgregarCabeceraGeneralPL(proc);

			proc.param(idEper, ParamType.NUMERO);
			proc.param(idAdar + "", ParamType.NUMERO);
			proc.param(nomFichero, ParamType.CADENA);
			proc.param(tipoDocumento, ParamType.CADENA);

			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);
			
			if (!response.esErronea() && response.getNumFilasEstructura(ES05) > 0) 
			{
				datosRespuestaAux = response.getValue(ES05, 1, "C1"); // Debe ser OK
			}
			else
			{
				datosRespuestaAux = Utiles.MensajeErrorXml(1, response.getTextoError(), log);
			}
		}
		catch (Exception ex) 
		{
			datosRespuestaAux = Utiles.MensajeErrorXml(1, "Error al custodiar la imagen", log);
		}
		
		return datosRespuestaAux;
	}
	
	
	public static String obtenerReimpresionPorGdre(Preferencias pref, String idLlamada, String idGdre){
		
		// Devolvemos el base64 a partir de un gdre
		String resBase64 = null;
		
		try 
		{			
			if(idGdre != null && !idGdre.isEmpty())
			{
				TLanzador lanzador = LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(idLlamada));
				ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.OBTENERBASE64PORGDRE", pref.getEsquemaBD());
				
				proc.param("33", ParamType.NUMERO);			// idorga
				proc.param("SMARTFINES", ParamType.CADENA);	// usuario
				proc.param(idGdre, ParamType.NUMERO);
			
				String soapResponse = lanzador.ejecutar(proc);
				RespuestaLanzador response = new RespuestaLanzador(soapResponse);
				
				if (!response.esErronea() && response.getNumFilasEstructura(WTEA) > 0) 
				{				
					resBase64 = response.getValue(WTEA, 1, "CL1");
				}
			}
		} 
		catch (Exception ex){ }
		
		return resBase64;
	}
}
