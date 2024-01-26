package es.tributasenasturias.documentopagoutils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.tributasenasturias.Exceptions.ValidacionException;
import es.tributasenasturias.documentos.util.ConversorParametrosLanzador;
import es.tributasenasturias.documentos.util.XMLUtils;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivo;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivoService;

public class ValidadorEstadoValor {

	public static enum TipoValor {CARTA_PAGO_NO_AGRUPADA, JUSTIFICANTE_PAGO}
	/**
	 * Recupera el estado del valor de la base de datos
	 * @param idEper Id Eper del valor
	 * @param tipo Tipo del valor {@link TipoValor}
	 * @return Código de estado de valor (00=Correcto, 01=No correcto, 03=Error)
	 * @throws ValidacionException
	 */
	private String recuperarEstadoValor (String idEper,TipoValor tipo) throws ValidacionException{
		String estado="";
		try {
			ConversorParametrosLanzador cpl;
			Preferencias pref = new Preferencias();					
			try {	
				pref.CargarPreferencias();
										
			} catch (Exception e) {
				throw new ValidacionException("Error al cargar preferencias y plantilla para comprobar el estado del valor. "+e.getMessage(),e);
			}			
							
			cpl = new ConversorParametrosLanzador();
			
	        cpl.setProcedimientoAlmacenado("INTERNET_RECIBOS.ComprobarEstadoValor");
	        cpl.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
	        // peticion	    
	        cpl.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
	        // usuario
	        cpl.setParametro("USU_WEB_SAC",ConversorParametrosLanzador.TIPOS.String);
	        // organismo
	        cpl.setParametro("33",ConversorParametrosLanzador.TIPOS.Integer);	        
	        // idEper
	        cpl.setParametro(idEper,ConversorParametrosLanzador.TIPOS.Integer);
	        if (tipo.equals(TipoValor.CARTA_PAGO_NO_AGRUPADA))
	        {
	        	cpl.setParametro("C",ConversorParametrosLanzador.TIPOS.String);
	        }
	        else if (tipo.equals(TipoValor.JUSTIFICANTE_PAGO))
	        {
	        	cpl.setParametro("J",ConversorParametrosLanzador.TIPOS.String);
	        }
	        cpl.setParametro("P",ConversorParametrosLanzador.TIPOS.String);
	        
	        LanzaPLMasivoService lanzaderaWS = new LanzaPLMasivoService();					
			LanzaPLMasivo lanzaderaPort;			
			lanzaderaPort = lanzaderaWS.getLanzaPLMasivoSoapPort();

	        javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
			
	        // Cambiamos el endpoint
			bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointLanzador());
			
	        //Vinculamos con el Handler	        
	        //HandlerUtil.setHandlerClient((javax.xml.ws.BindingProvider) lanzaderaPort);		
			
	        String respuesta = "";	
	        
	        try {	        	
	        	respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");
	        	cpl.setResultado(respuesta);	    
	        	Document docRespuesta = (Document) XMLUtils.compilaXMLObject(cpl.getResultado(), null);					
				Element[] rsCade = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='CADE_CADENA']/fila");
				estado= XMLUtils.selectSingleNode(rsCade[0],"STRING_CADE").getTextContent();
	        	
	        }catch (Exception ex) {	       
	        	throw new ValidacionException("Error ejecutando llamada a comprobación de estado de valor: " +ex.getMessage(),ex);
	        }
		} catch (Exception e) {
			throw new ValidacionException("Error ejecutando llamada a comprobación de estado de valor:" +e.getMessage(),e);
		}
		return estado;
	}
	/**
	 * Comprueba si un id eper es válido para la operación de carta de pago no agrupada o justificante de pago.
	 * @param idEper id Eper del valor
	 * @param tipo Tipo del valor {@link TipoValor}
	 * @return true si es válido, false si no.
	 * @throws ValidacionException
	 */
	public boolean esValido (String idEper, TipoValor tipo) throws ValidacionException
	{
		Logger.debug("Vamos a recuperar el estado del valor:" + idEper,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		String estado=recuperarEstadoValor (idEper, tipo);
		Logger.debug("Despues de recuperar el estado del valor:" + idEper,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		if (estado.equals("00"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
