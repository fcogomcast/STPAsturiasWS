package es.stpa.smartmultas.configuracion.ATEX;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import es.stpa.smartmultas.configuracion.utils.Utiles;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.stpa.smartmultas.requests.ConsultaDOIATEX5Request;
import es.stpa.smartmultas.requests.ConsultaMatriculaATEX5Request;
import es.stpa.smartmultas.soap.SoapClientHandler;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.services.lanzador.client.LanzadorFactory;
import es.tributasenasturias.services.lanzador.client.ParamType;
import es.tributasenasturias.services.lanzador.client.ProcedimientoAlmacenado;
import es.tributasenasturias.services.lanzador.client.TLanzador;
import es.tributasenasturias.services.lanzador.client.response.RespuestaLanzador;
import es.tributasenasturias.xml.XMLDOMUtils;

public class ConsultasATEX_service {

	final static String WTEA = "WTEA_ESTRUCTURA_ARQUITECTURA";
	final static String ESUN = "ESUN_ESTRUCTURA_UNIVERSAL";
	final static String ES05 = "ES05_ESTRUCTURA_UNI05";


	public static String ConsultaVehiculoATEX5(Preferencias pref, String idLlamada, ILog log, ConsultaMatriculaATEX5Request atex5, Integer idSubo){
		
		String idComa = "";
		String datosAtex = null;
		
		try
		{					
			TLanzador lanzador = LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(idLlamada));
			//ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.RECUPERA_DATOS_DGT_VEHICULO", pref.getEsquemaBD());
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.RECUPERA_DATOS_DGT_VEHICULO_2", pref.getEsquemaBD());
			
			proc.param(atex5.getMatricula(), ParamType.CADENA);
						
			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);
			
			if (!response.esErronea() && response.getNumFilasEstructura(WTEA) > 0) 
			{
				datosAtex = response.getValue(WTEA, 1, "CL1");
				
				try
				{
					idComa = insertarDatosCOMA(pref, idLlamada, atex5, idSubo); //de momento insertamos en la tabla COMA, pero sin datos
					datosAtex = insertarIdComa(datosAtex, idComa);
				}
				catch (Exception e) 
				{
					// No hacemos nada si no podemos obtener el idcoma
				}
				
				datosAtex = formateaRespuestaAtex(pref, idLlamada, datosAtex, idSubo); // Ahora mismo no incluimos el idComa porque no se usa desde la app
			}
			else
			{
				datosAtex = Utiles.MensajeErrorXml(1, response.getTextoError(), log);
			}
		}
		catch (Exception ex) 
		{
			datosAtex = Utiles.MensajeErrorXml(1, ex.getMessage(), log);
		}
		
		return datosAtex;
	}
	
	
	private static String formateaRespuestaAtex(Preferencias pref, String idLlamada, String datosAtexIni, Integer idsubo){
		
		// Creamos el XML que contiene la información que necesita la app de movildad	
		// En principio siempre cogemos la forma nueva de atex (DatosCompletos = true)
		String resFormateo = datosAtexIni.replace("\r\n", "")
											.replace("<?xml version=\"1.0\" encoding=\"utf-16\"?>", "")
											.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "")
											.replace("<string>", "")
											.replace("</string>", "")
											.replace("&amp;", "&")
											.replace("&gt;", ">")
											.replace("&lt;", "<");
		try
		{		
			resFormateo = gestionarDatosVehiculo(pref, idLlamada, resFormateo, idsubo);
			
			resFormateo = anyadirDatosMensaje(resFormateo);
		}
		catch (Exception e)
		{ 
			// Si falla algo devolvemos los datos iniciales sin formatear
		}
		
		return resFormateo;
	}
	
	
	private static String anyadirDatosMensaje(String datosAtex){
		
		//Comprobar tags
		String resultado = datosAtex;
		String mensaje = "";

		try
		{						
			Document xmlTmp = XMLDOMUtils.parseXml(datosAtex);	

			NodeList tagsInd = XMLDOMUtils.getAllNodes(xmlTmp, "indicadores");

			String sustraccion = XMLDOMUtils.getTextFromNodeOfList(tagsInd, "sustraccion");
			String bajaDefinitiva = XMLDOMUtils.getTextFromNodeOfList(tagsInd, "bajaDefinitiva");
			String bajaTemporal = XMLDOMUtils.getTextFromNodeOfList(tagsInd, "bajaTemporal");

			if(sustraccion.equals("S"))
			{
				mensaje = "VEHICULO_ROBADO";
			}
			else if(bajaDefinitiva.equals("S") ||  bajaTemporal.equals("S"))
			{
				mensaje = "VEHICULO_BAJA";
			}
			
			if(!mensaje.isEmpty()) 
			{ 		
				// Añadimos el nodo <mensaje> al xml
				Element newEle = xmlTmp.createElement("Mensaje");
				Text newText = xmlTmp.createTextNode(mensaje);
				newEle.appendChild(newText);

				xmlTmp.getElementsByTagName("vehiculo").item(0).appendChild(newEle);
				
				resultado = XMLDOMUtils.getXMLText(xmlTmp);
			} 
		}
		catch (Exception e)
		{ 
			// Si falla algo devolvemos los datos iniciales sin cambios
		}
		
		return resultado;
	}

	
	private static String insertarDatosCOMA(Preferencias pref, String idLlamada, ConsultaMatriculaATEX5Request atex, Integer idSubo){ //revisar!!!
		
		String idComa = "";
		
		try
		{	
			 // Insertamos en COMA y obtenemos el idcoma para pasarlo en el alta de la multa (si se hace el alta)
			String matricula = atex.getMatricula();
			
			TLanzador lanzador = LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("EXPTRAFICO_GENERAL.INSERTAR_DATOS_COMA", pref.getEsquemaBD());
			Utiles.AgregarCabeceraGeneralPL(proc);
			
			String cadena = String.format("%s#######################@#@#@#@#@#@#@#@#@##@", matricula);
						
			proc.param(idSubo + "", ParamType.NUMERO);	
			proc.param(matricula, ParamType.CADENA);
			proc.param(cadena, ParamType.CADENA); // De momento la pasamos con este formato
						
			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);
			
			if (!response.esErronea() && response.getNumFilasEstructura(ES05) >0 ) 
			{
				idComa = response.getValue(ES05, 1, "N1");
			}				
		}
		catch (Exception ex) 
		{
			// No hacemos nada si no podemos obtener el idcoma
		}

		return idComa;
	}

	
	private static String insertarIdComa(String respuesta, String idComa) {
		
		return XMLDOMUtils.insertarNodoTexto(respuesta, "IdComa", idComa);
	}
	
	
	private static String gestionarDatosVehiculo(Preferencias pref, String idLlamada, String datosAtex, Integer idsubo){
		
		String datosVehiculo = "";

		try 
		{	
			Document xmlTmp = XMLDOMUtils.parseXml(datosAtex);	

			NodeList tagsInd = XMLDOMUtils.getAllNodes(xmlTmp, "tipoIndustria");
			NodeList tagsTipoVehi = XMLDOMUtils.getAllNodes(xmlTmp, "tipoVehiculo");
			String tipoIndustriaCod = XMLDOMUtils.getTextFromNodeOfList(tagsInd, "codigo");
			String tipoVehiculoCod = XMLDOMUtils.getTextFromNodeOfList(tagsTipoVehi, "codigo");
			String tipoVehiculoDesc = XMLDOMUtils.getTextFromNodeOfList(tagsTipoVehi, "descripcion");
			
			TipoVehiculo tipoVehiculo = traducirCodigoVehiculoATEX(pref, idLlamada, tipoIndustriaCod, tipoVehiculoCod, idsubo);
			 
			String codVehiculo = tipoVehiculo.getCodigoVehiculo();
			String descrVehiculo = tipoVehiculo.getDecripcionVehiculo();
			
			String tipoVehiculoDescripcion = (descrVehiculo == null || descrVehiculo.isEmpty()) ? tipoVehiculoDesc : descrVehiculo;  
			String tipoVehiculoCodigo = (codVehiculo == null || codVehiculo.isEmpty()) ? tipoVehiculoCod : codVehiculo;
		
			Node nodoTipoVehiculo = XMLDOMUtils.getFirstNodeFromList(tagsTipoVehi, "tipoVehiculo");
			Node nodoHijoCod = XMLDOMUtils.getFirstChildNode(nodoTipoVehiculo, "codigo");
			Node nodoHijoDesc = XMLDOMUtils.getFirstChildNode(nodoTipoVehiculo, "descripcion");
			XMLDOMUtils.setNodeText(xmlTmp, nodoHijoDesc, tipoVehiculoDescripcion);
			XMLDOMUtils.setNodeText(xmlTmp, nodoHijoCod, tipoVehiculoCodigo);
			
			datosVehiculo = XMLDOMUtils.getXMLText(xmlTmp);
		} 
		catch (Exception ex) 
		{
			// Si algo falla devolvemos los datos iniciales
			datosVehiculo = datosAtex;		
		}

		return datosVehiculo;
	}
	
	
	private static TipoVehiculo traducirCodigoVehiculoATEX(Preferencias pref, String idLlamada, String tipoIndustria, String tipoDgt, Integer idsubo){
		
		TipoVehiculo tipo = new TipoVehiculo(null, null);

		try 
		{
			// Obtenemos el tipo de vehículo (CLVE) a partir de los datos de la DGT			
			TLanzador lanzador = LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.TRADUCEDATOSATEX", pref.getEsquemaBD());
			Utiles.AgregarCabeceraGeneralPL(proc);
			
			proc.param(idsubo + "", ParamType.NUMERO);
			proc.param(tipoIndustria, ParamType.CADENA);
			proc.param(tipoDgt, ParamType.CADENA); 
			
			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);
			
			int numFilas = response.getNumFilasEstructura(ESUN);
			if (!response.esErronea() && numFilas > 0) 
			{		
				String resultado = response.getValue(ESUN, 1, "C4"); //Puede contener "ERROR", "OK", otros..
						
				if(resultado.equals("OK"))
				{
					boolean salir = false;
					for (int i = 1; i <= numFilas && !salir; i++) 
					{
						String reg = response.getValue(ESUN, i, "C0");
						if(reg.equals("CLASE"))
						{
							String codigo = response.getValue(ESUN, i, "C1");
							String descr = response.getValue(ESUN, i, "C3");
							tipo = new TipoVehiculo(codigo, descr);
							salir = true;
						}
					}
				}
			}
		} 
		catch (Exception ex) { }

		return tipo;
	}
	
	public static String ConsultaDOIATEX5(Preferencias pref, String idLlamada, ILog log, ConsultaDOIATEX5Request atex5, Integer idSubo){
		
		String datosAtex = null;
		
		try
		{					
			TLanzador lanzador = LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.RECUPERA_DATOS_DGT_PERSONA", pref.getEsquemaBD());
			
			proc.param(atex5.getDOI(), ParamType.CADENA);
						
			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);
			
			if (!response.esErronea() && response.getNumFilasEstructura(WTEA) > 0) 
			{
				datosAtex = response.getValue(WTEA, 1, "CL1");
				
				//formateamos la respuesta
				datosAtex= datosAtex.replace("\r\n", "")
				.replace("<?xml version=\"1.0\" encoding=\"utf-16\"?>", "")
				.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "")
				.replace("<string>", "")
				.replace("</string>", "")
				.replace("&amp;", "&")
				.replace("&gt;", ">")
				.replace("&lt;", "<");
			}
			else
			{
				datosAtex = Utiles.MensajeErrorXml(1, response.getTextoError(), log);
			}
		}
		catch (Exception ex) 
		{
			datosAtex = Utiles.MensajeErrorXml(1, ex.getMessage(), log);
		}
		
		return datosAtex;
	}
}
