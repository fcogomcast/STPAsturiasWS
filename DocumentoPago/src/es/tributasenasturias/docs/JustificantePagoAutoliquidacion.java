package es.tributasenasturias.docs;

import javax.xml.parsers.ParserConfigurationException;

import es.tributasenasturias.documentopagoutils.ConversorParametrosLanzador;
import es.tributasenasturias.documentopagoutils.Preferencias;
import es.tributasenasturias.servicios.documentos.WSDocumentos;
import es.tributasenasturias.servicios.documentos.WSDocumentos_Service;
import es.tributasenasturias.soap.handler.HandlerUtil;

public class JustificantePagoAutoliquidacion {
	
	/**
	 * Impresión de justificante de pago de autoliquidación.
	 * @param justificante Número de justificante de autoliquidación.
	 * @param altaDocumento true si se debe dar de alta el documento generado en base de datos, false en otro caso.
	 * @return Cadena con el documento PDF de justificante de pago de autoliquidación codificado en Base 64.
	 * @throws Exception
	 */
	public String imprimirJustificante (String justificante, boolean altaDocumento) throws Exception
	{
		ConversorParametrosLanzador conversor;
		Preferencias pref = new Preferencias();
		try {
			conversor = new ConversorParametrosLanzador();
			conversor.setProcedimientoAlmacenado(pref.getPaJustificanteAutoliquidacion());
			conversor.setParametro(justificante, ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro("P", ConversorParametrosLanzador.TIPOS.String); // Devolución a servicio web. 
			
			//ImpresionGD impr = ImpresionGD.newImpresionGD();
			//return impr.getPDFImpresion(conversor.Codifica(), "");
			WSDocumentos_Service srv = new WSDocumentos_Service();
			WSDocumentos port= srv.getWSDocumentosPort();
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) port;
			// Cambiamos el endpoint
			bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointDocumentos());
	        //Vinculamos con el Handler	        
	        HandlerUtil.setHandlerClient((javax.xml.ws.BindingProvider) port);
			return port.impresionGD(conversor.Codifica(), "",altaDocumento);
		} catch (ParserConfigurationException e) {
			throw new Exception ("Error en la impresión de justificante de autoliquidación:" + e.getMessage(),e);
		} 
		
	}
}
