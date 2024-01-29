package com.stpa.ws.server.formularios;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stpa.ws.server.util.*;
import com.stpa.ws.server.exception.StpawsException;

public class ObtenerListadosWS extends GeneralWS {

	/**
	 * @param procedimiento  -> procedimiento a ejecutar por el WSLanzador
	 * @param camposEntrada -> valores de entrada
	 * @param camposRecuperar -> definición de los campos a recuperar a la vuelta del procedimiento
	 * @param campoError -> determina el campo de código de error del procedimiento
	 * @return List<Map<String,String>>  -> listado de mapas con clave el nombre del estructura.campo recuperado 
	 * 										y valor el valor recuperado para este campo. 
	 * 										Cada mapa es un item devuelto por el WS  
	 * @throws ValidationException
	 */
	public static List<Map<String,String>> obtenerListado(String procedimiento, List<CampoWS> camposEntrada, 
			List<CampoRecuperarWS> camposRecuperar, CampoRecuperarWS campoError) throws StpawsException {
		List<Map<String,String>> listado = null;
		try {			
			String peticion = generarPeticion(procedimiento, camposEntrada);		
			String xmlOut = llamadaWebService(peticion);						
			listado = respuestaWSObtenerListado(xmlOut, camposRecuperar, campoError);
			
		} catch (Exception e) {
			throw new StpawsException("ObtencionListadosWS.obtenerListado: Error al obtener el literal: " + e.getMessage(), e);
		}
		return listado;		
	}
	
	private static String generarPeticion(String nombreProcedimiento, List<CampoWS> campos) throws Exception {
		
		XMLUtils xmlutils = new XMLUtils();
		try {
			xmlutils.crearXMLDoc();
		} catch (RemoteException re) {
			throw new StpawsException("Error en la creación del xml: " + re.getMessage() , re);
		}
		xmlutils.crearNode("peti", "", null, null);
		xmlutils.reParentar(1);

		xmlutils.crearNode("proc", "", new String[] { "nombre" }, new String[] { nombreProcedimiento });
		xmlutils.reParentar(1);
		
		int posicionP = 1;
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
			
			posicionP += campos.size();
		}	
		
		xmlutils.crearNode("param", "", new String[] { "id" }, new String[] { String.valueOf(posicionP) });
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
			throw new StpawsException("Error en la creación del xml: " + re.getMessage(), re);
		}
		return xmlIn;
	}
	
	private static List<Map<String,String>> respuestaWSObtenerListado(String respuestaWebService, 
			List<CampoRecuperarWS> camposRecuperar, CampoRecuperarWS campoError) throws Exception {
		List<Map<String,String>> listado = null;
		
		if(respuestaWebService != null && camposRecuperar != null){
			listado = respuestaWSObtenerListadoFromXml(respuestaWebService, 
					camposRecuperar, campoError);
		}
		return listado;
	}
	
	private static List<Map<String,String>> respuestaWSObtenerListadoFromXml(String respuestaWebService, 
			List<CampoRecuperarWS> camposRecuperar, CampoRecuperarWS campoError) throws Exception {
		List<Map<String,String>> listado = null;		
		
		if(respuestaWebService != null && camposRecuperar != null){
			int idxError = respuestaWebService.indexOf("<error>");
			
			if(idxError >= 0){
				//com.stpa.ws.server.util.Logger.debug(respuestaWebService,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				throw new StpawsException("ObtencionListadosWS.respuestaWSObtenerListadoFromXml: Error al obtener el literal:",null);
			}

			//com.stpa.ws.server.util.Logger.debug(respuestaWebService,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			
			if(campoError != null){
				camposRecuperar.add(0, campoError);
			}
			String[] columnasARecuperar = new String[camposRecuperar.size()];
			String[] estructurasARecuperar = new String[camposRecuperar.size()];
			
			for(int i=0; i< camposRecuperar.size(); i++){
				CampoRecuperarWS campo = (CampoRecuperarWS)camposRecuperar.get(i);
				columnasARecuperar[i] = campo.getCampo();
				estructurasARecuperar[i] = campo.getEstructura();
			}
			
			
			Map<String, Object> respuestaAsMap;
			try {
				respuestaAsMap = XMLUtils.compilaXMLDoc(respuestaWebService, estructurasARecuperar, columnasARecuperar, false);
			} catch (RemoteException re) {
				throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.conexion"), re);
			}
			String codiError = null;
			
			if(campoError  != null){
				Object[] objcol = (Object[]) respuestaAsMap.get(campoError.getEstructura());
				if(objcol != null){
					for (int i = 0; i < objcol.length; i++) {
						String[] objrow = (String[]) objcol[i];
						if (!objrow[0].equals("")) {
							codiError = objrow[0];
						}
					}			
				}
			}

			if(codiError == null || (codiError != null && "00".equals(codiError))){
				int iniIdx = 0;
				if(campoError != null){
					iniIdx = 1;
				}								
				
				List<String> estructuras = new ArrayList<String>();
				
				//Nos quedamos solo con las estructuras distintas
				for(int i=iniIdx; i < estructurasARecuperar.length; i++){
					if(estructuras.indexOf(estructurasARecuperar[i]) < 0){
						estructuras.add(estructurasARecuperar[i]);
					}
				}
				listado = new ArrayList<Map<String,String>>();
				for(int k=0; k < estructuras.size(); k++){
					String estructura = estructuras.get(k);
					
					Object[] listaItems = (Object[]) respuestaAsMap.get(estructura);
					if(listaItems != null){
						for (int i = 0; i < listaItems.length; i++) {
							Map<String,String> item = new HashMap<String, String>();
							
							String[] objrow = (String[]) listaItems[i];
							for(int j=0; j< objrow.length && (j+iniIdx)< camposRecuperar.size(); j++){
								CampoRecuperarWS campo = (CampoRecuperarWS)camposRecuperar.get(j+iniIdx);
								
								if(estructura.equals(campo.getEstructura())){
									String key = estructura + "." + campo.getCampo();
									item.put(key, objrow[j]);
								}
							}							
							listado.add(item);
						}			
					}						
				}										
			}			
		}		
		return listado;
	}
	
}
