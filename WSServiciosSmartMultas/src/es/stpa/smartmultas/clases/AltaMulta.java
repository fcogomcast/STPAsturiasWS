package es.stpa.smartmultas.clases;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;

import es.stpa.smartmultas.configuracion.utils.ConfiguracionUtils;
import es.stpa.smartmultas.configuracion.utils.Utiles;
import es.stpa.smartmultas.entidades.Imagen;
import es.stpa.smartmultas.entidades.ImagenesList;
import es.stpa.smartmultas.entidades.Nota;
import es.stpa.smartmultas.entidades.NotasList;
import es.stpa.smartmultas.entidades.Propiedad;
import es.stpa.smartmultas.entidades.PropiedadesList;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.stpa.smartmultas.requests.AltaDocumentoMultasRequest;
import es.stpa.smartmultas.requests.AltaMultaRequest;
import es.stpa.smartmultas.responses.AltaMultaResponse;
import es.stpa.smartmultas.soap.SoapClientHandler;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.services.lanzador.client.LanzadorFactory;
import es.tributasenasturias.services.lanzador.client.ParamType;
import es.tributasenasturias.services.lanzador.client.ProcedimientoAlmacenado;
import es.tributasenasturias.services.lanzador.client.TLanzador;
import es.tributasenasturias.services.lanzador.client.response.RespuestaLanzador;
import es.tributasenasturias.xml.XMLDOMUtils;

// ServiciosSmartFines.Multas.AltaMulta
public class AltaMulta extends A_MultasBase {

	final String ESUN = "ESUN_ESTRUCTURA_UNIVERSAL";

	public AltaMulta(Preferencias pref, String idLlamada, Document datosEntrada, ILog log) {
		super(pref, idLlamada, datosEntrada, log);
	}

	@Override
	public String Inicializar() {

		String datosRespuesta = null;

		try 
		{
			String tmpXML = XMLDOMUtils.getXMLText(_datosEntrada);
			
			// Validamos los datos con el xsd "C:\gtt\ycampos\WSServiciosSmartMultas\WSServiciosSmartMultas\esquemas\AltaMulta.xsd"
			//String pathXsd = "C:/gtt/ycampos/WSServiciosSmartMultas/WSServiciosSmartMultas/esquemas/AltaMulta.xsd";
			//File ficXsd = new File("C:/gtt/ycampos/WSServiciosSmartMultas/WSServiciosSmartMultas/esquemas/AltaMulta.xsd");//new File("../esquemas/AltaMulta.xsd");
			
			//String directorio = new File (".").getAbsolutePath() + "\\esquemas\\AltaMulta.xsd";
			//String directorio2 = new File ("esquemas/AltaMulta.xsd").getAbsolutePath();

			String mensajeError = null;//XMLDOMUtils.validateXMLSchema(pathXsd, tmpXML); //revisar

			if (mensajeError == null) 
			{
				// Quitamos este posible tag de la petición para que no falle la serialización
				tmpXML = tmpXML.replace("xmlns=\"tns:tributos\"", "");
				AltaMultaRequest datosEntrada = XMLDOMUtils.XMLtoObject(tmpXML, AltaMultaRequest.class);
				
				Integer idSubo = Integer.parseInt(_datosEntrada.getElementsByTagName("Suborganismo").item(0).getTextContent());
				datosRespuesta = ConfiguracionUtils.GetTextoValidacionUDID(_pref, _idLlamada, _datosEntrada, idSubo, _log);

				if (datosRespuesta == null) 
				{
					Long idEper = new Long(0);

					cargarDatosMulta(datosEntrada);
					comprobarDatosMultaVoluntaria(datosEntrada);

					RespuestaLanzador responseAlta = altaMultaSmartfines(datosEntrada);

					if (responseAlta != null && responseAlta.getNumFilasEstructura(ESUN) > 0) 
					{
						String err = responseAlta.getValue(ESUN, 1, "C9");
						String valor = responseAlta.getValue(ESUN, 1, "N1");

						if (err != null && err.toUpperCase().equals("ERROR")) 
						{
							datosRespuesta = Utiles.MensajeErrorXml(Integer.parseInt(valor), responseAlta.getValue(ESUN, 1, "C1"), _log);
						} 
						else 
						{
							idEper = Long.parseLong(valor);
							String numBoletin = responseAlta.getValue(ESUN, 1, "C1");
							String numExp = responseAlta.getValue(ESUN, 1, "C2");
							String refCobro = responseAlta.getValue(ESUN, 1, "C3");
							String fechaUltimoPago = responseAlta.getValue(ESUN, 1, "C4");
							String refLarga = responseAlta.getValue(ESUN, 1, "C5");

							// Si el número de expediente es el mismo que el de boletín, no lo pasamos
							AltaMultaResponse boletin = new AltaMultaResponse(idEper, numBoletin, numExp.equals(numBoletin) ? "" : numExp, fechaUltimoPago, refCobro, refLarga);

							datosRespuesta = XMLDOMUtils.Serialize(boletin);

							InsertarNotaExpediente(err, numExp, datosEntrada);
							GuardarImagenes(err, idEper, datosEntrada);
							ComprobarImagenOCR(datosEntrada, idEper);
						}
					} 
					else 
					{
						datosRespuesta = Utiles.MensajeErrorXml(0, responseAlta.getTextoError(), _log);
					}
				}
			} 
			else 
			{
				datosRespuesta = Utiles.MensajeErrorXml(2, "Error xsd: " + mensajeError, _log);
			}
		} 
		catch (Exception ex) 
		{
			datosRespuesta = Utiles.MensajeErrorXml(0, "Error inesperado: " + ex.getMessage(), _log);
		}

		return datosRespuesta;
	}

	private void cargarDatosMulta(AltaMultaRequest datosEntrada) {

		// Mandamos los datos variables en los datos de la multa, si es necesario
		PropiedadesList propiedades = datosEntrada.getPropiedades();
		if (propiedades != null) 
		{
			List<Propiedad> items = propiedades.getItems();

			if (items != null && items.size() > 0) 
			{
				for (int i = 0; i < items.size(); i++) 
				{
					String valor = items.get(i).getValor();
					String nombre = items.get(i).getNombre();

					if (nombre == "VELOCIDAD") 
					{
						datosEntrada.setVelocidad(new Integer(valor));
					} 
					else if (nombre == "VELOCIDADLIMITE") 
					{
						datosEntrada.setVelocidadMaxima(new Integer(valor));
					} 
					else if (nombre == "VELOCIDADCORREGIDA") 
					{
						datosEntrada.setVelocidadCorregida(Float.parseFloat(valor));
					} 
					else if (nombre == "DISPOSITIVO") 
					{
						datosEntrada.setDispositivoMedicion(valor);
					}
				}
			}
		}
	}

	private void comprobarDatosMultaVoluntaria(AltaMultaRequest datosEntrada) {

		String volNombre = datosEntrada.getVoluntariaNombreDenunciante();
		String volDireccion = datosEntrada.getVoluntariaDireccionDenunciante();
		String volHechos = datosEntrada.getVoluntariaHechosComprobados();

		if (volNombre != null && !volNombre.isEmpty() && volDireccion != null
				&& !volDireccion.isEmpty() && volHechos != null && !volHechos.isEmpty()) 
		{
			datosEntrada.setTipoDenuncia("V");
		}
	}

	private RespuestaLanzador altaMultaSmartfines(AltaMultaRequest datosEntrada) {

		RespuestaLanzador responseAltaMulta = null;

		try 
		{
			TLanzador lanzador = LanzadorFactory.newTLanzador(_pref.getEndpointLanzador(), new SoapClientHandler(this._idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.ALTAMULTA_SMARTFINES", _pref.getEsquemaBD());
			Utiles.AgregarCabeceraGeneralPL(proc);

			anyadirParametrosPL(proc, datosEntrada);

			String soapResponse = lanzador.ejecutar(proc);
			responseAltaMulta = new RespuestaLanzador(soapResponse);
		} 
		catch (Exception ex) 
		{
			String h = ex.getMessage();
		}

		return responseAltaMulta;
	}

	private void InsertarNotaExpediente(String err, String numExp, AltaMultaRequest datosEntrada) {

		try 
		{
			if (err != null && !err.toUpperCase().equals("REINTENTO")) 
			{
				String agente = _datosEntrada.getElementsByTagName("NumeroAgenteAgmu").item(0).getTextContent();

				NotasList auxNotas = datosEntrada.getNotas();

				if (auxNotas != null && auxNotas.getItems() != null) 
				{
					List<Nota> notas = auxNotas.getItems();
					for (Nota n : notas) 
					{
						n.setNumRege(numExp);
						n.setAgente(agente);

						ConfiguracionUtils.GuardarDatosNotaEnExpediente(_pref, _idLlamada, _log, agente, n);
					}
				}
			}
		} 
		catch (Exception ex) { } // debe continuar
	}

	private String GuardarImagenes(String err, Long ideper, AltaMultaRequest datosEntrada) {

		String datosRespuesta = null; // revisar.. no se gestiona error en la imagen?

		ImagenesList listadoImag = datosEntrada.getImagenes();

		if (listadoImag != null) 
		{
			List<Imagen> listado = listadoImag.getItems();

			if (listado != null && listado.size() > 0) 
			{
				try 
				{
					if (err != null && !err.toUpperCase().equals("REINTENTO"))
					{
						for (Imagen img : listado) 
						{
							AltaDocMultaIndividual(datosEntrada.getNumBoletin(), img, ideper, "Imagen SmartFines ");
						}
					}
				} 
				catch (Exception ex)  //revisar... estos logs
				{
					// GuardarLog((int)request.IdOrganismo, request.Operacion, "Error insertar imágenes: " + mensajeError, null, null, LevelLog.Fatal, TipoLog.Debug);
				}
			}
		}

		return datosRespuesta;
	}

	private String ComprobarImagenOCR(AltaMultaRequest datosEntrada, Long idEper) {

		String documento = null;	//revisar

		try 
		{
			Imagen auxImgOCR = datosEntrada.getImagenOCR();
			if (auxImgOCR != null) 
			{
				String baseOCR = auxImgOCR.getBase64();
				String nombreOCR = auxImgOCR.getNombre();

				if (baseOCR != null && !baseOCR.isEmpty()) 
				{
					Imagen imgOCR = new Imagen(nombreOCR == null || nombreOCR.isEmpty() ? "Foto OCR" : nombreOCR, baseOCR);

					documento = AltaDocMultaIndividual(datosEntrada.getNumBoletin(), imgOCR, idEper, "Imagen SmartFines OCR ");
				}
			}
		} 
		catch (Exception e) //revisar... estos logs
		{
			// GuardarLog((int)request.IdOrganismo, request.Operacion, "Error insertar imágenes: " + mensajeError, null, null, LevelLog.Fatal, TipoLog.Debug);
		}
		return documento;
	}

	private String AltaDocMultaIndividual(String numBoletin, Imagen img, Long ideper, String txtImagen) {

		String documento = null;

		try 
		{
			Integer suborga = Integer.parseInt(_datosEntrada.getElementsByTagName("Suborganismo").item(0).getTextContent());
			String udid = _datosEntrada.getElementsByTagName("UDID").item(0).getTextContent();
			Integer sesionOrga = 33;
			String sesionId = null; // estos datos no los pasamos
			String codAgente = "agprueba"; 
			String pwdAgente = null; 

			Date fechaActual = Calendar.getInstance().getTime();

			AltaDocumentoMultasRequest entradaDoc = new AltaDocumentoMultasRequest(
					numBoletin, 
					img.getBase64(), 
					txtImagen + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaActual), 
					img.getNombre(),
					new SimpleDateFormat("dd/MM/yyyy").format(fechaActual),
					new SimpleDateFormat("HH:mm:ss").format(fechaActual),
					ideper, suborga, codAgente, pwdAgente, udid, sesionId,
					sesionOrga);

			String respuestaAltaDoc = XMLDOMUtils.Serialize(entradaDoc);

			Document auxXmlDoc = XMLDOMUtils.parseXml(respuestaAltaDoc);
			AltaDocumentoBoletinMultas respAltaDocBoletin = new AltaDocumentoBoletinMultas(_pref, _idLlamada, auxXmlDoc, suborga, _log);
			documento = respAltaDocBoletin.Inicializar(); // revisar... respuesta?

		} 
		catch (Exception e) 
		{
			// GuardarLog((int)request.IdOrganismo, request.Operacion, "Error insertar imágenes: " + mensajeError, null, null, LevelLog.Fatal, TipoLog.Debug);
		}

		return documento;
	}

	private void anyadirParametrosPL(ProcedimientoAlmacenado proc, AltaMultaRequest multa) {

		String udid = _datosEntrada.getElementsByTagName("UDID").item(0).getTextContent();
		Integer subOrga = Integer.parseInt(_datosEntrada.getElementsByTagName("Suborganismo").item(0).getTextContent());

		proc.param(udid, ParamType.CADENA); 										// udid
		proc.param(subOrga + "", ParamType.NUMERO); 								// suborganismo
		proc.param(multa.getNumeroAgenteAgmu(), ParamType.CADENA); 					// numagente
		proc.param(multa.getNifInfractor(), ParamType.CADENA); 						// nifinfr
		proc.param(multa.getNombreCompletoInfractor(), ParamType.CADENA); 			// nombreinfr
		proc.param(multa.getPaisInfractor(), ParamType.CADENA); 					// paisinfr
		proc.param(multa.getCalleInfractor(), ParamType.CADENA); 					// calleinfr
		proc.param(multa.getPoblacionInfractor(), ParamType.CADENA); 				// poblacioninfr
		proc.param(multa.getProvinciaInfractor(), ParamType.CADENA); 				// provinciainfr
		proc.param(multa.getDistritoPostalInfractor(), ParamType.CADENA); 			// dpostalinfr
		proc.param(multa.getNifPropietario(), ParamType.CADENA); 					// nifprop
		proc.param(multa.getNombreCompletoPropietario(), ParamType.CADENA); 		// nombreprop
		proc.param(multa.getPaisPropietario(), ParamType.CADENA); 					// paisprop
		proc.param(multa.getCallePropietario(), ParamType.CADENA); 					// calleprop
		proc.param(multa.getPoblacionPropietario(), ParamType.CADENA); 				// poblacionprop
		proc.param(multa.getProvinciaPropietario(), ParamType.CADENA); 				// provinciaprop
		proc.param(multa.getDistritoPostalPropietario(), ParamType.CADENA); 		// dpostalprop
		proc.param(multa.getNifConductor(), ParamType.CADENA); 						// nifconductor
		proc.param(multa.getNombreCompletoConductor(), ParamType.CADENA); 			// nombreconductor
		proc.param(multa.getPaisConductor(), ParamType.CADENA); 					// paisconductor
		proc.param(multa.getCalleConductor(), ParamType.CADENA); 					// calleconductor
		proc.param(multa.getPoblacionConductor(), ParamType.CADENA); 				// poblacionconductor
		proc.param(multa.getProvinciaConductor(), ParamType.CADENA); 				// provinciaconductor
		proc.param(multa.getDistritoPostalConductor(), ParamType.CADENA); 			// dpostalconductor
		proc.param(multa.getMatricula().toUpperCase(), ParamType.CADENA); 			// matricula
		proc.param(multa.getMarca(), ParamType.CADENA); 							// marca
		proc.param(multa.getModelo(), ParamType.CADENA); 							// modelo
		proc.param(multa.getNumBoletin(), ParamType.CADENA); 						// numboletin
		proc.param(multa.getNumExpediente(), ParamType.CADENA); 					// numexpediente
		proc.param(multa.getCodigoArmu() + "", ParamType.NUMERO); 					// codarmu
		proc.param(multa.getHechoDenunciado(), ParamType.CADENA); 					// hechodenunciado
		proc.param(multa.getHechoDenunciadoCooficial(), ParamType.CADENA); 			// hechodenunciadocoof
		proc.param(multa.getIdTimu(), ParamType.CADENA); 							// idtimu
		proc.param(multa.getIdCamu() + "", ParamType.NUMERO); 						// idcamu
		proc.param(multa.getIdClve() + "", ParamType.NUMERO); 						// idclve
		proc.param(multa.getIdTamu(), ParamType.CADENA); 							// idtamu
		proc.param(multa.getFechaInfraccion(), ParamType.CADENA); 					// p_fechainfraccion
		proc.param(multa.getHoraInfraccion().substring(0, 5), ParamType.CADENA); 	// horainfraccion
		proc.param(null, ParamType.NUMERO); 										// codvia1
		proc.param(null, ParamType.CADENA); 										// siglavia1
		proc.param(multa.getNombreVia(), ParamType.CADENA); 						// nombrevia1
		proc.param(multa.getNumVia() + "", ParamType.NUMERO); 						// numvia1
		proc.param(multa.getAmpliacionVia(), ParamType.CADENA); 					// ampliacionvia
		proc.param(multa.getPagado() + "", ParamType.NUMERO); 						// pagada
		proc.param(multa.getFormaPago(), ParamType.CADENA); 						// formapago
		proc.param(multa.getFechaCobro() + "", ParamType.CADENA); 					// p_fechacobro

		Integer auxImporte = multa.getImporte();
		proc.param((auxImporte == null || auxImporte == 0) ? null : auxImporte + "", ParamType.NUMERO); // importe

		String auxJusti = multa.getJustificante();
		String justificante = (auxJusti == null || auxJusti.isEmpty()) ? "": auxJusti.substring(auxJusti.length() - 13, 13); // revisar??
		proc.param(justificante, ParamType.CADENA); 								// justificante

		proc.param(multa.getViaPenal(), ParamType.CADENA); 							// viapenal
		proc.param(multa.getVelocidad() == null ? null : multa.getVelocidad() + "", ParamType.NUMERO); 					// velocidad
		proc.param((multa.getVelocidadMaxima() == null) ? null : multa.getVelocidadMaxima() + "", ParamType.NUMERO); 	// velmaxima

		Float veloCorregida = multa.getVelocidadCorregida();
		proc.param((veloCorregida == null || veloCorregida == 0) ? null : veloCorregida + "", ParamType.NUMERO); 		// velcorregida

		String dispositivo = multa.getDispositivoMedicion();
		proc.param(dispositivo == "" ? null : dispositivo, ParamType.CADENA); 					// dismedicion

		proc.param(multa.getRetencion() != null ? multa.getRetencion() : "N",ParamType.CADENA); // retencion
		proc.param(ObtenerPropiedadesNombres(multa.getPropiedades()),ParamType.CADENA); 		// variablesmultas (listado)
		proc.param(ObtenerPropiedadesValores(multa.getPropiedades()),ParamType.CADENA); 		// valoresvariables (listado)
		
		String auxIdComa = null;
		if(Utiles.isNumeric(multa.getIdComa() + ""))
		{
			auxIdComa = multa.getIdComa() + "";
		}
		proc.param(auxIdComa, ParamType.NUMERO); // idcoma
		
		proc.param(multa.getModificadaDireccion() == null ? "N" : multa.getModificadaDireccion(), ParamType.CADENA); // modificadadireccion
		proc.param(multa.getIdTamu2(), ParamType.CADENA); 								// idtamu2
		proc.param(multa.getNumeroAgenteAgmu2(), ParamType.CADENA); 					// numagente2
		proc.param(multa.getTipoDenuncia(), ParamType.CADENA); 							// p_vtipodenuncia
		proc.param(multa.getVoluntariaNombreDenunciante(), ParamType.CADENA); 			// p_nombredenunciante
		proc.param(multa.getVoluntariaDireccionDenunciante(), ParamType.CADENA); 		// p_direcciondenunciante
		proc.param(multa.getVoluntariaOtrosDatosDenunciante(),ParamType.CADENA); 		// p_otrosdatosdenunciante
		proc.param(multa.getVoluntariaHechosComprobados(), ParamType.CADENA); 			// p_hechoscomprobados
	}

	private String ObtenerPropiedadesNombres(PropiedadesList prop) {

		String resultado = "";

		if (prop != null) 
		{
			List<Propiedad> listado = prop.getItems();

			for (Propiedad propiedad : listado) {

				resultado += (propiedad.getNombre() + "@@");
			}

			resultado = resultado.substring(0, resultado.length() - 2);
		}

		return resultado;
	}

	private String ObtenerPropiedadesValores(PropiedadesList prop) {

		String resultado = "";

		if (prop != null) 
		{
			List<Propiedad> listado = prop.getItems();

			for (Propiedad propiedad : listado) 
			{

				resultado += (propiedad.getValor() + "@@");
			}

			resultado = resultado.substring(0, resultado.length() - 2);
		}

		return resultado;
	}
}
