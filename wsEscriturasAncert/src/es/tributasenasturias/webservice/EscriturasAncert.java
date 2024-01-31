package es.tributasenasturias.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.parsers.ParserConfigurationException;

import es.tributasenasturias.webservice.business.TratarXMLEscritura;
import es.tributasenasturias.webservice.escriturasUtils.ConversorParametrosLanzador;
import es.tributasenasturias.webservice.escriturasUtils.Logger;
import es.tributasenasturias.webservice.escriturasUtils.Preferencias;
import es.tributasenasturias.webservice.escriturasUtils.StringUtil;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPL;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLService;

@WebService (serviceName="EscriturasAncertService")
public class EscriturasAncert {

	public EscriturasAncert () {
		//de este modo, al instalar el webservice, se creara el fichero de preferencias si no existe
		new Preferencias().CompruebaFicheroPreferencias();
	}
	
	@WebMethod(operationName="consultaEscritura")
	public String consultaEscritura(@WebParam (name="codNotario") 
									String codNotario,
									@WebParam (name="codNotaria")
									String codNotaria,
									@WebParam (name="numProtocolo")
									String numProtocolo,
									@WebParam (name="protocoloBis")
									String protocoloBis,
									@WebParam (name="fechaDevengo")
									String fechaDevengo,
									@WebParam (name="codUsuario")
									String codUsuario) {
		ConversorParametrosLanzador cpl;
		String identificador=String.format("notario: %1$s , notaría: %2$s , protocolo: %3$s , protocolo bis: %4$s , fecha Devengo: %5$s, Usuario:%6$s"
		          , codNotario,codNotaria,numProtocolo,protocoloBis,fechaDevengo, codUsuario);
		Logger.info("Recibida petición para consultar escritura con "+identificador);
		Preferencias pref = new Preferencias();
		try
	    {
	      pref.CargarPreferencias();
	    } catch (Exception e) {
	      Logger.error("Error cargar las preferencias");
	      return "Error cargar las preferencias para "+identificador;
	    }
	    try
	    {
	      cpl = new ConversorParametrosLanzador();
	    } catch (ParserConfigurationException pce) {
	      if (pref.getDebug().equals("1")) {
	        Logger.error("Para" + identificador+ " :Error al crear la llamada al procedimiento almacenado: " + pce.getMessage());
	      }
	      return "Error al crear la llamada al procedimiento almacenado: " + pce.getMessage();
	    }
	    if (pref.getDebug().equals("1")){
	    	Logger.debug("Para " + identificador +" ,procedimiento almacenado de consulta: "+pref.getPAConsultaEscritura());
	    }
	    cpl.setProcedimientoAlmacenado(pref.getPAConsultaEscritura());

	    cpl.setParametro(codNotario, ConversorParametrosLanzador.TIPOS.String);

	    cpl.setParametro(codNotaria, ConversorParametrosLanzador.TIPOS.String);

	    cpl.setParametro(numProtocolo, ConversorParametrosLanzador.TIPOS.String);

	    cpl.setParametro(protocoloBis, ConversorParametrosLanzador.TIPOS.String);

	    cpl.setParametro(fechaDevengo, ConversorParametrosLanzador.TIPOS.Date, "dd/mm/rrrr");
	    
	    cpl.setParametro(codUsuario, ConversorParametrosLanzador.TIPOS.String);

	    cpl.setParametro("P", ConversorParametrosLanzador.TIPOS.String);

	    LanzaPLService lanzaderaWS = new LanzaPLService();
	    LanzaPL lanzaderaPort;
	    if (!pref.getEndpointLanzador().equals(""))
	    {
	      if (pref.getDebug().equals("1")) {
	        Logger.debug("Para " + identificador +" se utiliza el endpoint de lanzadera: " + pref.getEndpointLanzador());
	      }
	      lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();

	      javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider)lanzaderaPort;

	      bpr.getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, pref.getEndpointLanzador());
	    }
	    else
	    {
	      if (pref.getDebug().equals("1")) {
	        Logger.debug("Para " + identificador + " se utiliza el endpoint de lanzadera por defecto.");
	      }
	      lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
	    }

	    String respuesta = "";
	    try {
	      if (pref.getDebug().equals("1")){
	    	  Logger.debug("Para " + identificador + " se utiliza el endpoint de lanzadera:" + cpl.Codifica());
	      }

	      respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");
	      cpl.setResultado(respuesta);
	      String error=cpl.getNodoResultadoX("error");
	      if (!error.equals(""))
	      {
	     	Logger.error("Para " + identificador + " :El lanzador ha devuelto un error:" + error );
	     	return "Error en petición a base de datos.";
	      }
	    } catch (Exception ex) {
	        Logger.error("Para " + identificador + " se ha producido un error en Lanzador: ".concat(ex.getMessage()));
	        return ex.getMessage();
	    }
	    if (pref.getDebug().equals("1")) {
	      Logger.debug("Para " + identificador + " la respuesta es: ".concat(respuesta));
	    }
	    Logger.info ("Para " + identificador + " se ha encontrado una escritura.");
	    return cpl.getNodoResultadoX("pdf");
	}
	
	@WebMethod (operationName="setEscritura")
	public String setEscritura(@WebParam (name="codNotario") 
								String codNotario,
								@WebParam (name="codNotaria")
								String codNotaria,
								@WebParam (name="numProtocolo")
								String numProtocolo,
								@WebParam (name="protocoloBis")
								String protocoloBis,
								@WebParam (name="fechaDevengo")
								String fechaDevengo,
								@WebParam (name="firmaEscritura")
								String firmaEscritura,
								@WebParam (name="docEscritura")
								String docEscritura) {
		
		Preferencias pref = new Preferencias();
		String identificador=String.format("notario: %1$s , notaría: %2$s , protocolo: %3$s , protocolo bis: %4$s , fecha Devengo: %5$s"
		          , codNotario,codNotaria,numProtocolo,protocoloBis,fechaDevengo);
		//cargamos los datos del almacen de un fichero xml preferencias
		try {
			pref.CargarPreferencias();
		} catch (Exception e) {
			Logger.error("Error cargar las preferencias para "+ identificador);
			return "Error cargar las preferencias";
		}
		Logger.info("Para "+ identificador + " ,recibida alta de escritura para "+identificador);
		ConversorParametrosLanzador cpl;
		try {
		cpl = new ConversorParametrosLanzador();
		} catch (ParserConfigurationException pce) {
			if (pref.getDebug().equals("1")) {
				Logger.error("Para " + identificador +"  se ha producido un error al crear la llamada al procedimiento almacenado : "+pce.getMessage() + " para "+ identificador);
			}
			return "Error al crear la llamada al procedimiento almacenado: "+pce.getMessage();
		}
		if (pref.getDebug().equals("1")) {
			Logger.debug("Para " + identificador +" se utiliza el procedimiento almacenado de inserción: "+pref.getPAInsertaDatosEscr());
		}
      cpl.setProcedimientoAlmacenado(pref.getPAInsertaDatosEscr());
      //codNotario
      cpl.setParametro(codNotario,ConversorParametrosLanzador.TIPOS.String);
      //codNotaria
      cpl.setParametro(codNotaria,ConversorParametrosLanzador.TIPOS.String);
      //numProtocolo
      cpl.setParametro(numProtocolo,ConversorParametrosLanzador.TIPOS.String);        
      //protocoloBis
      cpl.setParametro(protocoloBis,ConversorParametrosLanzador.TIPOS.String);        
      //fechaDevengo
      cpl.setParametro(fechaDevengo,ConversorParametrosLanzador.TIPOS.Date,"dd/mm/rrrr");  
      //firmaEscritura
      cpl.setParametro(firmaEscritura,ConversorParametrosLanzador.TIPOS.Clob);        
      //docEscritura
      cpl.setParametro(docEscritura,ConversorParametrosLanzador.TIPOS.Clob);
      // conexion oracle
      cpl.setParametro("P",ConversorParametrosLanzador.TIPOS.String);            
      
      LanzaPLService lanzaderaWS = new LanzaPLService();
      LanzaPL lanzaderaPort;
		if (!pref.getEndpointLanzador().equals(""))
		{
			if (pref.getDebug().equals("1")) {
				Logger.debug ("Se utiliza el endpoint de lanzadera: " + pref.getEndpointLanzador() + " para "+ identificador);
			}
			lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
			// enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort; 
			// Cambiamos el endpoint
			bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointLanzador()); 
		}
		else
		{
			if (pref.getDebug().equals("1")) {
				Logger.debug ("Se utiliza el endpoint de lanzadera por defecto: " + javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY + " para " + identificador);
			}
			lanzaderaPort =lanzaderaWS.getLanzaPLSoapPort(); 				
		}
      
      String respuesta = "";
      try {
      	respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");
      	cpl.setResultado(respuesta);
      	String error=cpl.getNodoResultado("error");
      	if (!error.equals(""))
      	{
      		Logger.error("Para " + identificador + " :El lanzador ha devuelto un error:" + error );
      		return "KO";
      	}
      }catch (Exception ex) {
      	if (pref.getDebug().equals("1")) {
      		Logger.error("Para " + identificador + " ,Error en la ejecución del lanzador: ".concat(ex.getMessage()));
      		return "KO";
      	}
     }
      Logger.info("Para "+identificador + "la respuesta de lanzador ha sido: "+ StringUtil.toOneLine(respuesta));
		return cpl.getNodoResultado("STRING_CADE");
	
	}
	
	@WebMethod (operationName="setEscrituraSolicitudes")
	public String setEscrituraSolicitudes(@WebParam (name="codNotario") 
								String codNotario,
								@WebParam (name="codNotaria")
								String codNotaria,
								@WebParam (name="numProtocolo")
								String numProtocolo,
								@WebParam (name="protocoloBis")
								String protocoloBis,
								@WebParam (name="fechaDevengo")
								String fechaDevengo,
								@WebParam (name="firmaEscritura")
								String firmaEscritura,
								@WebParam (name="docEscritura")
								String docEscritura,
								@WebParam (name="autorizadoDiligencias")
								String autorizadoDiligencias,
								@WebParam (name="gestionarSolicitud")
								String gestionarSolicitud) {
		
		String identificador=String.format("notario: %1$s , notaría: %2$s , protocolo: %3$s , protocolo bis: %4$s , fecha Devengo: %5$s"
		          , codNotario,codNotaria,numProtocolo,protocoloBis,fechaDevengo);
		Preferencias pref = new Preferencias();
		//cargamos los datos del almacen de un fichero xml preferencias
		try {
			pref.CargarPreferencias();
		} catch (Exception e) {
			Logger.error("Error cargar las preferencias para "+ identificador);
			return "Error cargar las preferencias";
		}
		Logger.info("Para "+ identificador + " ,recibida alta de escritura para "+identificador);
		ConversorParametrosLanzador cpl;
		try {
		cpl = new ConversorParametrosLanzador();
		} catch (ParserConfigurationException pce) {
			if (pref.getDebug().equals("1")) {
				Logger.error("Para " + identificador +"  se ha producido un error al crear la llamada al procedimiento almacenado : "+pce.getMessage() + " para "+ identificador);
			}
			return "Error al crear la llamada al procedimiento almacenado: "+pce.getMessage();
		}
		if (pref.getDebug().equals("1")) {
			Logger.debug("Para " + identificador +" se utiliza el procedimiento almacenado de inserción: "+pref.getPAInsertaDatosEscr());
		}
      cpl.setProcedimientoAlmacenado(pref.getPAInsertaDatosEscr());
      //codNotario
      cpl.setParametro(codNotario,ConversorParametrosLanzador.TIPOS.String);
      //codNotaria
      cpl.setParametro(codNotaria,ConversorParametrosLanzador.TIPOS.String);
      //numProtocolo
      cpl.setParametro(numProtocolo,ConversorParametrosLanzador.TIPOS.String);        
      //protocoloBis
      cpl.setParametro(protocoloBis,ConversorParametrosLanzador.TIPOS.String);        
      //fechaDevengo
      cpl.setParametro(fechaDevengo,ConversorParametrosLanzador.TIPOS.Date,"dd/mm/rrrr");  
      //firmaEscritura
      cpl.setParametro(firmaEscritura,ConversorParametrosLanzador.TIPOS.Clob);        
      //docEscritura
      cpl.setParametro(docEscritura,ConversorParametrosLanzador.TIPOS.Clob);
      //Autorizado Diligencias
      cpl.setParametro(autorizadoDiligencias,ConversorParametrosLanzador.TIPOS.String);
      //Gestionar solicitud
      cpl.setParametro(gestionarSolicitud,ConversorParametrosLanzador.TIPOS.String);
      // conexion oracle
      cpl.setParametro("P",ConversorParametrosLanzador.TIPOS.String);            
      
      LanzaPLService lanzaderaWS = new LanzaPLService();
      LanzaPL lanzaderaPort;
		if (!pref.getEndpointLanzador().equals(""))
		{
			if (pref.getDebug().equals("1")) {
				Logger.debug ("Se utiliza el endpoint de lanzadera: " + pref.getEndpointLanzador() + " para "+ identificador);
			}
			lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
			// enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort; 
			// Cambiamos el endpoint
			bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointLanzador()); 
		}
		else
		{
			if (pref.getDebug().equals("1")) {
				Logger.debug ("Se utiliza el endpoint de lanzadera por defecto: " + javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY + " para " + identificador);
			}
			lanzaderaPort =lanzaderaWS.getLanzaPLSoapPort(); 				
		}
      
      String respuesta = "";
      try {
      	respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");
      	cpl.setResultado(respuesta);
      	String error=cpl.getNodoResultado("error");
      	if (!error.equals(""))
      	{
      		Logger.error("Para " + identificador + " :El lanzador ha devuelto un error:" + error );
      		return "KO";
      	}
      }catch (Exception ex) {
      	if (pref.getDebug().equals("1")) {
      		Logger.error("Para " + identificador + " ,Error en la ejecución del lanzador: ".concat(ex.getMessage()));
      		return "KO";
      	}
     }
      Logger.info("Para "+identificador + "la respuesta de lanzador ha sido: "+ StringUtil.toOneLine(respuesta));
		return cpl.getNodoResultado("STRING_CADE");
	
	}
	
	@WebMethod(operationName="setEscrituraXML")
	public String setEscrituraXML(
									@WebParam(name = "xmlData") String xmlData) {
		Preferencias pref = new Preferencias();
		try
        {
			//cargamos los datos del almacen de un fichero xml preferencias
			pref.CargarPreferencias();

            if (pref.getDebug().equals("1")) {
            	Logger.info("el debug esta activado".concat(pref.getPAInsertaDatosEscr()));
            }
            //Se comprueban los datos de entrada
            if(xmlData == null || xmlData.length() == 0)
            {
            	// lanza excepcion
                throw new Exception("El campo xmlData es obligatorio.");
            }
            // Recuperar del xml de entrada los datos para insertar la escritura.
            TratarXMLEscritura xmldoc = new TratarXMLEscritura(xmlData);
            
            return setEscritura(xmldoc.getNodoByName("codNotarioEscritura"),
            					xmldoc.getNodoByName("codNotariaEscritura"),
            					xmldoc.getNodoByName("numProtocoloEscritura"),
            					xmldoc.getNodoByName("protocoloBisEscritura"),
            					xmldoc.getNodoByName("fechaAutorizacionEscritura"),
            					xmldoc.getNodoByName("firmaDocumentoEscritura"),
            					xmldoc.getNodoByName("documentoEscritura")
            					);
        }
        catch (Exception e)
        {
        	if (pref.getDebug().equals("1")) {
        		Logger.error("error".concat(e.getMessage()));
        	}
            return "error".concat(e.getMessage());
        }
	}
	
	@WebMethod (operationName="setEscrituraOrigen")
	public String setEscrituraOrigen(@WebParam (name="codNotario") 
								String codNotario,
								@WebParam (name="codNotaria")
								String codNotaria,
								@WebParam (name="numProtocolo")
								String numProtocolo,
								@WebParam (name="protocoloBis")
								String protocoloBis,
								@WebParam (name="fechaAutorizacion")
								String fechaAutorizacion,
								@WebParam (name="firmaEscritura")
								String firmaEscritura,
								@WebParam (name="docEscritura")
								String docEscritura,
								@WebParam (name="origenEscritura")
								String origen) {
		
		Preferencias pref = new Preferencias();
		String identificador=String.format("notario: %1$s , notaría: %2$s , protocolo: %3$s , protocolo bis: %4$s , fecha Devengo: %5$s, origen: %6$s"
		          , codNotario,codNotaria,numProtocolo,protocoloBis,fechaAutorizacion, origen);
		//cargamos los datos del almacen de un fichero xml preferencias
		try {
			pref.CargarPreferencias();
		} catch (Exception e) {
			Logger.error("Error cargar las preferencias para "+ identificador);
			return "Error cargar las preferencias";
		}
		Logger.info("Para "+ identificador + " ,recibida alta de escritura para "+identificador);
		ConversorParametrosLanzador cpl;
		try {
		cpl = new ConversorParametrosLanzador();
		} catch (ParserConfigurationException pce) {
			if (pref.getDebug().equals("1")) {
				Logger.error("Para " + identificador +"  se ha producido un error al crear la llamada al procedimiento almacenado : "+pce.getMessage() + " para "+ identificador);
			}
			return "Error al crear la llamada al procedimiento almacenado: "+pce.getMessage();
		}
		if (pref.getDebug().equals("1")) {
			Logger.debug("Para " + identificador +" se utiliza el procedimiento almacenado de inserción: "+pref.getPAInsertaDatosEscr());
		}
      cpl.setProcedimientoAlmacenado(pref.getPAInsertaDatosEscrOrigen());
      //codNotario
      cpl.setParametro(codNotario,ConversorParametrosLanzador.TIPOS.String);
      //codNotaria
      cpl.setParametro(codNotaria,ConversorParametrosLanzador.TIPOS.String);
      //numProtocolo
      cpl.setParametro(numProtocolo,ConversorParametrosLanzador.TIPOS.String);        
      //protocoloBis
      cpl.setParametro(protocoloBis,ConversorParametrosLanzador.TIPOS.String);        
      //fechaDevengo
      cpl.setParametro(fechaAutorizacion,ConversorParametrosLanzador.TIPOS.Date,"dd/mm/rrrr");  
      //firmaEscritura
      cpl.setParametro(firmaEscritura,ConversorParametrosLanzador.TIPOS.Clob);        
      //docEscritura
      cpl.setParametro(docEscritura,ConversorParametrosLanzador.TIPOS.Clob);
      //Origen
      cpl.setParametro(origen,ConversorParametrosLanzador.TIPOS.String);
      // conexion oracle
      cpl.setParametro("P",ConversorParametrosLanzador.TIPOS.String);            
      
      LanzaPLService lanzaderaWS = new LanzaPLService();
      LanzaPL lanzaderaPort;
		if (!pref.getEndpointLanzador().equals(""))
		{
			if (pref.getDebug().equals("1")) {
				Logger.debug ("Se utiliza el endpoint de lanzadera: " + pref.getEndpointLanzador() + " para "+ identificador);
			}
			lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
			// enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort; 
			// Cambiamos el endpoint
			bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointLanzador()); 
		}
		else
		{
			if (pref.getDebug().equals("1")) {
				Logger.debug ("Se utiliza el endpoint de lanzadera por defecto: " + javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY + " para " + identificador);
			}
			lanzaderaPort =lanzaderaWS.getLanzaPLSoapPort(); 				
		}
      
      String respuesta = "";
      try {
      	respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");
      	cpl.setResultado(respuesta);
      	String error=cpl.getNodoResultado("error");
      	if (!error.equals(""))
      	{
      		Logger.error("Para " + identificador + " :El lanzador ha devuelto un error:" + error );
      		return "KO";
      	}
      }catch (Exception ex) {
      	if (pref.getDebug().equals("1")) {
      		Logger.error("Para " + identificador + " ,Error en la ejecución del lanzador: ".concat(ex.getMessage()));
      		return "KO";
      	}
     }
      Logger.info("Para "+identificador + "la respuesta de lanzador ha sido: "+ StringUtil.toOneLine(respuesta));
		return cpl.getNodoResultado("STRING_CADE");
	
	}
	//Nueva función para añadir escritura y solicitud de una sola vez.
	@WebMethod (operationName="altaEscrituraPlusvalia")
	public String altaEscrituraPlusvalia(@WebParam (name="codNotario") 
								String codNotario,
								@WebParam (name="codNotaria")
								String codNotaria,
								@WebParam (name="numProtocolo")
								String numProtocolo,
								@WebParam (name="protocoloBis")
								String protocoloBis,
								@WebParam (name="fechaDevengo")
								String fechaDevengo,
								@WebParam (name="firmaEscritura")
								String firmaEscritura,
								@WebParam (name="docEscritura")
								String docEscritura,
								@WebParam(name="cod_ine_ayto")
								String cod_ine_ayto
								) {
		
		String identificador=String.format("notario: %1$s , notaría: %2$s , protocolo: %3$s , protocolo bis: %4$s , fecha Devengo: %5$s"
		          , codNotario,codNotaria,numProtocolo,protocoloBis,fechaDevengo);
		Preferencias pref = new Preferencias();
		//cargamos los datos del almacen de un fichero xml preferencias
		try {
			pref.CargarPreferencias();
		} catch (Exception e) {
			Logger.error("Error cargar las preferencias para "+ identificador);
			return "Error cargar las preferencias";
		}
		Logger.info("Para "+ identificador + " ,recibida alta de escritura para "+identificador);
		ConversorParametrosLanzador cpl;
		try {
		cpl = new ConversorParametrosLanzador();
		} catch (ParserConfigurationException pce) {
			if (pref.getDebug().equals("1")) {
				Logger.error("Para " + identificador +"  se ha producido un error al crear la llamada al procedimiento almacenado : "+pce.getMessage() + " para "+ identificador);
			}
			return "Error al crear la llamada al procedimiento almacenado: "+pce.getMessage();
		}
		if (pref.getDebug().equals("1")) {
			Logger.debug("Para " + identificador +" se utiliza el procedimiento almacenado de inserción: "+pref.getPAInsertaDatosEscr());
		}
      cpl.setProcedimientoAlmacenado(pref.getPAInsertaEscrituraPlusvalia());
      //codNotario
      cpl.setParametro(codNotario,ConversorParametrosLanzador.TIPOS.String);
      //codNotaria
      cpl.setParametro(codNotaria,ConversorParametrosLanzador.TIPOS.String);
      //numProtocolo
      cpl.setParametro(numProtocolo,ConversorParametrosLanzador.TIPOS.String);        
      //protocoloBis
      cpl.setParametro(protocoloBis,ConversorParametrosLanzador.TIPOS.String);        
      //fechaDevengo
      cpl.setParametro(fechaDevengo,ConversorParametrosLanzador.TIPOS.Date,"dd/mm/rrrr");  
      //firmaEscritura
      cpl.setParametro(firmaEscritura,ConversorParametrosLanzador.TIPOS.Clob);        
      //docEscritura
      cpl.setParametro(docEscritura,ConversorParametrosLanzador.TIPOS.Clob);
      //Identificador de ayuntamiento
      cpl.setParametro(cod_ine_ayto,ConversorParametrosLanzador.TIPOS.String);
      // conexion oracle
      cpl.setParametro("P",ConversorParametrosLanzador.TIPOS.String);            
      
      LanzaPLService lanzaderaWS = new LanzaPLService();
      LanzaPL lanzaderaPort;
		if (!pref.getEndpointLanzador().equals(""))
		{
			if (pref.getDebug().equals("1")) {
				Logger.debug ("Se utiliza el endpoint de lanzadera: " + pref.getEndpointLanzador() + " para "+ identificador);
			}
			lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
			// enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort; 
			// Cambiamos el endpoint
			bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointLanzador()); 
		}
		else
		{
			if (pref.getDebug().equals("1")) {
				Logger.debug ("Se utiliza el endpoint de lanzadera por defecto: " + javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY + " para " + identificador);
			}
			lanzaderaPort =lanzaderaWS.getLanzaPLSoapPort(); 				
		}
      
      String respuesta = "";
      try {
      	respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");
      	cpl.setResultado(respuesta);
      	String error=cpl.getNodoResultado("error");
      	if (!error.equals(""))
      	{
      		Logger.error("Para " + identificador + " :El lanzador ha devuelto un error:" + error );
      		return "KO";
      	}
      }catch (Exception ex) {
      	if (pref.getDebug().equals("1")) {
      		Logger.error("Para " + identificador + " ,Error en la ejecución del lanzador: ".concat(ex.getMessage()));
      		return "KO";
      	}
     }
      Logger.info("Para "+identificador + "la respuesta de lanzador ha sido: "+ StringUtil.toOneLine(respuesta));
		return cpl.getNodoResultado("STRING_CADE");
	
	}
}
