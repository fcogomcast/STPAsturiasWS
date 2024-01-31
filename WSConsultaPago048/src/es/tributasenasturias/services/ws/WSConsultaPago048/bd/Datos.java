package es.tributasenasturias.services.ws.WSConsultaPago048.bd;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import es.tributasenasturias.services.client.lanzador.ParamType;
import es.tributasenasturias.services.client.lanzador.TLanzador;
import es.tributasenasturias.services.client.lanzador.TParam;
import es.tributasenasturias.services.ws.WSConsultaPago048.utils.Log.Logger;
import es.tributasenasturias.services.ws.WSConsultaPago048.utils.Preferencias.Preferencias;


public class Datos {
	private Preferencias preferencias;
	private Logger logger=null;
	private TLanzador lanzadorWS=null;
	
	//Definición de Constantes
	private final String STRING_CADE = "STRING_CADE";
	private final String STRING_TRUE = "TRUE";
	
	
	public Datos()
	{
		
		logger= new Logger();
		try
		{
			logger.trace("INICIO CONSTRUCTOR DE DATOS");
			preferencias = Preferencias.getPreferencias();
			
			
			
			
//				lanzaderaPort =lanzaderaWS.getLanzaPLSoapPort();
//				javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort; // enlazador de protocolo para el servicio.
////				logger.debug ("Se utiliza el endpoint de lanzadera por defecto: " + bpr.getRequestContext().get (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
//				//Asociamos el log con este port.
//				Binding bi = bpr.getBinding();
//				List <Handler> handlerList = bi.getHandlerChain();
//				if (handlerList == null)
//				   handlerList = new ArrayList<Handler>();
//				handlerList.add(new es.tributasenasturias.utils.LogMessageHandlerClient());
//				bi.setHandlerChain(handlerList);
//			}
			logger.trace("FIN CONSTRUCTOR DE DATOS");
		}
		catch (Exception ex)
		{
			logger.error( "En constructor de clase Datos: " + ex.getMessage());
		}
	}
	
	/**
	 * Construye el lanzador 
	 * @param nomProc
	 * @return
	 */
	private void InicializarLanzador(String nomProc)
	{
		logger.trace("INICIO INICIALIZARLANZADOR");
		String endPointISL=preferencias.getEndPointISL();
		lanzadorWS = new TLanzador();
		lanzadorWS.setEndPoint(endPointISL);
		lanzadorWS.getPeticion().setProcName(nomProc);
		logger.trace("Inicializado Lanzador con EndpointISL " + endPointISL + " y nombre procedimiento = " + nomProc );
	}
	
	/**
	 * 
	 * @param xmlParsear
	 * @param nodoABuscar
	 * @return el valor del nodo que buscamos dentro del xml
	 */
	private String GetValorNodo(String xmlParsear, String nodoABuscar)
	{
		logger.trace("INICIO GETVALORNODO");
		 try {
		        DocumentBuilderFactory dbf =
		            DocumentBuilderFactory.newInstance();
		        DocumentBuilder db = dbf.newDocumentBuilder();
		        InputSource is = new InputSource();
		        is.setCharacterStream(new StringReader(xmlParsear));

		        Document doc = db.parse(is);
		        //Obtenemos el valor del nodo STRING_CADE - 
		        //Sabemos que sólo existe un nodo de ese tipo
		        NodeList nodes = doc.getElementsByTagName(this.STRING_CADE);
		        logger.trace("NumElementos " + this.STRING_CADE + ": " + nodes.getLength());
		        String resultado = "";
		        if (nodes.getLength()>0)
		        	//Sólo hay un nodo de ese tipo
     		        resultado =(nodes.item(0).getFirstChild().getNodeValue()); 
		        else
		        	resultado = "";
		        
		        logger.trace("Resultado " + this.STRING_CADE + ": " + resultado );
		        logger.trace("FIN GETVALORNODO");
		        return resultado;
		     }
		    catch (Exception e) {
		    	logger.error(e.getMessage());
		        return "";
		    }
	}
	
	/**
	 * Valida que existe la autoliquidación en la base de datos
	 * @param Autoliquidacion
	 * @return cierto si existe falso en caso contrario.
	 */
	public boolean ValidarAutoliquidacion(String Autoliquidacion)
	{
		logger.trace("ENTRADA VALIDARAUTOLIQUIDACION CON PARAMETRO Autoliq: " + Autoliquidacion);
				
		//Establecemos el nombre del procedimiento
		InicializarLanzador(preferencias.getNomProcedimientoValAutol());
		//lanzadorWS.getPeticion().setProcName(preferencias.getNomProcedimientoValAutol());
		//Establecemos los parámetros
		lanzadorWS.getPeticion().addParam(new TParam(ParamType.CADENA,Autoliquidacion,""));
		
		
		//Recogemos el resultado (->xml) y parseamos el nodo que nos interesa 
		String resultadoEjecutarPL = lanzadorWS.execute(preferencias.getEsquemaBaseDatos(), lanzadorWS.getPeticion().toXml(), preferencias.getIPLanzador(), "","" , "");
		logger.trace("Procedimiento " + lanzadorWS.getPeticion().getProcName());
		
		logger.info("Resultado: " + resultadoEjecutarPL);
		String result = GetValorNodo(resultadoEjecutarPL,STRING_CADE);
		
		//Convertimos de String a boolean y retornamos
		if (result.equals(STRING_TRUE))
		{
			logger.trace("Validación de Autoliquidación correcta");
			logger.trace("FIN VALIDARAUTOLIQUIDACION");
			return true;
		}
		else
		{
			logger.trace("Validación de Autoliquidación incorrecta.");
			logger.trace("FIN VALIDARAUTOLIQUIDACION");
			return false;
		}
		
	}
	
	/**
	 * Comprueba que el nº de autoliquidación y el nº de bastidor se corresponden.
	 * @param Autoliquidacion
	 * @param Bastidor
	 * @return
	 */
	public boolean ValidarAutoliquidacionYBastidor(String Autoliquidacion, String Bastidor)
	{
		logger.trace("ENTRADA AL METODO VALIDARAUTOLIQUIDACIONYBASTIDOR CON PARAMETROS Autoliq: " + Autoliquidacion +  " Y Bastidor: " + Bastidor);
		
		//Inicializamos el lanzador y le establecemos el nombre del procedimiento
		InicializarLanzador(preferencias.getNomProcedimientoValAutolYBastidor());
		
		//lanzadorWS.getPeticion().setProcName(preferencias.getNomProcedimientoValAutolYBastidor());
		lanzadorWS.getPeticion().addParam(new TParam(ParamType.CADENA, Autoliquidacion, ""));
		lanzadorWS.getPeticion().addParam(new TParam(ParamType.CADENA, Bastidor, ""));
				
		logger.trace("WSDL " + preferencias.getEndPointISL());
		logger.trace("Procedimiento " + lanzadorWS.getPeticion().getProcName());
		logger.trace("Peticion: " + lanzadorWS.getPeticion().toXml());
		logger.trace("EsquemaBD: " + preferencias.getEsquemaBaseDatos());
		logger.trace("IP_Lanzador: " + preferencias.getIPLanzador());
		
		//Recogemos el resultado (->xml) y parseamos el nodo que nos interesa 
		String resultadoEjecutarPL = lanzadorWS.execute(preferencias.getEsquemaBaseDatos(), lanzadorWS.getPeticion().toXml(), preferencias.getIPLanzador(), "","" , "");
		logger.trace("Procedimiento " + lanzadorWS.getPeticion().getProcName());
		
		logger.info("Resultado: " + resultadoEjecutarPL);
		String result = GetValorNodo(resultadoEjecutarPL,STRING_CADE);
		
		//Convertimos de String a boolean y retornamos
		if (result.equals(STRING_TRUE))
		{
			logger.trace("Validación de Autoliquidación y Bastidor correcta");
			logger.trace("FIN VALIDARAUTOLIQUIDACIONYBASTIDOR");
			return true;
		}
		else
		{
			logger.trace("Autoliquidación y Bastidor no se corresponden.");
			logger.trace("FIN VALIDARAUTOLIQUIDACIONYBASTIDOR");
			return false;
		}
		
	}
			
	
	
	
}
