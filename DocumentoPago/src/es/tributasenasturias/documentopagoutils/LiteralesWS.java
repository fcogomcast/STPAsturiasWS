package es.tributasenasturias.documentopagoutils;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import es.tributasenasturias.documentopagoutils.StpawsException;
import es.tributasenasturias.documentopagoutils.GeneralWS;
import es.tributasenasturias.docs.XMLUtils;

public class LiteralesWS extends GeneralWS {
	private static final String noutil="Esta cadena está aquí, única y exclusivamente, porque sin ella el antivirus detecta un falso positivo en el compilado";
	public static String obtenerLiteral(String procedimiento, List<CampoWS> campos, CampoRecuperarWS campoLiteral) throws StpawsException {
		String literal = null;
		try {
			
			//es.tributasenasturias.documentopagoutils.Logger.error("Dentro de aquí:",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			String peticion = generarPeticionObtenerLiteral(procedimiento, campos);

			//es.tributasenasturias.documentopagoutils.Logger.error("Antes de llamada a ws:",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			String xmlOut = llamadaWebService(peticion);	
			
			//es.tributasenasturias.documentopagoutils.Logger.error("Despues de llamada a ws:",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			
			
			literal = respuestaWSObtenerLiteral(procedimiento, xmlOut, campoLiteral);
			//es.tributasenasturias.documentopagoutils.Logger.error("Despues de obtenerliteral:",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			
		} catch (Exception e) {
			throw new StpawsException("LiteralesWS.obtenerLiteral: Error al obtener el literal: " + e.getMessage(), e);
		}
		
		return literal;		
	}
	
	private static String generarPeticionObtenerLiteral(String nombreProcedimiento, List<CampoWS> campos) throws Exception {
		
		XMLUtils xmlutils = new XMLUtils();
		try {
			xmlutils.crearXMLDoc();
		} catch (RemoteException re) {
			throw new StpawsException("Error en la creación del xml: " + re.getMessage(), re);
		}
		xmlutils.crearNode("peti", "", null, null);
		xmlutils.reParentar(1);

		xmlutils.crearNode("proc", "", new String[] { "nombre" }, new String[] { nombreProcedimiento });
		xmlutils.reParentar(1);
		
		//Creamos los nodos para parametros.
		if(campos != null){
			
			
			for (int i= 0; i< campos.size();i++){
				CampoWS campo = (CampoWS)campos.get(i);
				String atributo = campo.getCampo();
				String valor = campo.getValor();
				String tipo = campo.getTipoParam();
				
				if(valor != null){
					if(tipo != null && "1".equals(tipo) && (atributo != null && atributo.indexOf("texto") < 0)){
						valor = valor.toUpperCase();
					}
					else  if(tipo != null && "2".equals(tipo)){
						valor = valor.replace(".","");
					}
				}
				
				
				//p_in_vmodelo varchar2 (codigo del modelo)
				int id = Integer.parseInt(campo.getOrden())+1;
				xmlutils.crearNode("param", "", new String[] { "id" }, new String[] { String.valueOf(id) });
				xmlutils.reParentar(1);
				xmlutils.crearNode("valor", valor);
				xmlutils.crearNode("tipo", tipo);
				xmlutils.crearNode("formato", "");
				
				xmlutils.crearNode("nombreCampo", campo.getCampo());
				xmlutils.reParentar(-1);
			}
		}	
		
		xmlutils.crearNode("param", "", new String[] { "id" }, new String[] { String.valueOf(campos.size()+1) });
		xmlutils.reParentar(1);
		xmlutils.crearNode("valor", "P");
		xmlutils.crearNode("tipo", "1");
		xmlutils.crearNode("formato", "");		
		xmlutils.crearNode("nombreCampo", "p_in_vconoracle");
		xmlutils.reParentar(-1);
		
		//fin xml
		xmlutils.reParentar(-1);
		xmlutils.reParentar(-1);
		String xmlIn = "";
		try {
			xmlIn = xmlutils.informarXMLDoc();
		} catch (RemoteException re) {
			throw new StpawsException("Error en la creaciÃ³n del xml: " + re.getMessage(), re);
		}
		return xmlIn;
	}
	
	private static String respuestaWSObtenerLiteral(String tabla, String respuestaWebService, CampoRecuperarWS campoLiteral) throws Exception {
		String literal = null;
		
		if(respuestaWebService != null && campoLiteral != null){
			literal = respuestaWSObtenerLiteralFromXml(respuestaWebService, 
					campoLiteral.getEstructura(), campoLiteral.getCampo());
		}
		return literal;
	}
	
	private static String respuestaWSObtenerLiteralFromXml(String respuestaWebService, String cadena, String campo) throws Exception {
		String literal = null;		
		
		if(respuestaWebService != null){
			int idxError = respuestaWebService.indexOf("<error>");
			
			if(idxError >= 0){
				es.tributasenasturias.documentopagoutils.Logger.debug(respuestaWebService,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				throw new StpawsException("literales.error", new Exception());
			}
			es.tributasenasturias.documentopagoutils.Logger.debug(respuestaWebService,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			String[] columnasARecuperar = new String[] {"STRING_CADE", campo};
			String[] estructurasARecuperar = new String[] { "CADE_CADENA", cadena};
			Map<String, Object> respuestaAsMap;
			try {
				respuestaAsMap = XMLUtils.compilaXMLDoc(respuestaWebService, estructurasARecuperar, columnasARecuperar, false);
			} catch (RemoteException re) {
				throw new StpawsException("Error en la creaciÃ³n del xml: " + re.getMessage(), re);
			}
			String codiError = null;
			Object[] objcol = (Object[]) respuestaAsMap.get("CADE_CADENA");
			if(objcol != null){
				for (int i = 0; i < objcol.length; i++) {
					String[] objrow = (String[]) objcol[i];
					if (!objrow[0].equals("")) {
						codiError = objrow[0];
					}
				}			
			}
			if(codiError != null && "00".equals(codiError)){
				objcol = (Object[]) respuestaAsMap.get(cadena);
				if(objcol != null){
					for (int i = 0; i < objcol.length; i++) {
						String[] objrow = (String[]) objcol[i];
						if (!objrow[0].equals("")) {
							literal = objrow[0];
						}
					}			
				}	
			}			
		}		
		return literal;
	}
}
