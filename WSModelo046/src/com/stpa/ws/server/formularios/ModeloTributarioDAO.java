package com.stpa.ws.server.formularios;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.XMLUtils;

/**
 * Implementa las funciones de busqueda para los modelos tributarios.
 * @author david.gimenez.banos
 *
 */
public class ModeloTributarioDAO extends BasePortletDAO {

	public static final String MUNICIPIO = "INTERNET.obtenerMunicipios";
	public static final String BINGO = "INTERNET.obtenerSalasBingo";
	public static final String TIPOVIA = "INTERNET.obtenerSiglasVia";
	public static final String PROVINCIA = "INTERNET.obtenerProvincias";
	public static final String SUBORG = "INTERNET.obtenerSubOrganismos";
	public static final String VEHICULO = "INTERNET.obtenerTarifasVehiculos";
	public static final String OFICINA = "INTERNET.obtenerOficinas";
	public static final String FABRICANTE = "INTERNET.obtenerMarcasTransportes";
	public static final String MODELO = "INTERNET.obtenerModelosTransportes";
	public static final String NOTARIOS = "INTERNET.obtenerNotarios";
	public static final String DATOSPERSONA = "INTERNET.obtenerDatosPersona";
	public static final String DOMICILIABLES = "INTERNET_DOMICILIACIONES.bobjdomiciliablespersona";
	public static final String PORCVEH = "INTERNET.obtenerPorcVehiculos";
	public static final String PORC_EB = "INTERNET.obtenerPorcEmbarcaciones";

	public static final String NOMBREPROCESO = "nombreProceso";
	public boolean debugclase = false;
	
	/**
	 * Busqueda de informacion.
	 * @param p_parametros Mapa con los parametros de busqueda, incluyendo "nombreProceso".
	 * @return
	 * @throws WebServicesException
	 */
	public Object[] find(HashMap<String, String> p_parametros) throws StpawsException {
		return findWsLanzador(p_parametros);
	}

	/**
	 * Busqueda de informacion en WebService.
	 * @param p_parametros Mapa con los parametros de busqueda, incluyendo "nombreProceso".
	 * @return Lista de Strings o Objects
	 * @throws WebServicesException
	 */
	public Object[] findWsLanzador(HashMap<String, String> p_parametros) throws StpawsException {
		String XMLIn = "<error></error>";
		if (p_parametros.isEmpty()) {
			return new Object[0];
		}
		String nombreProceso = p_parametros.get(NOMBREPROCESO);
		XMLUtils xmlutils = new XMLUtils();
		String proceso = nombreProceso;
		try {
			String[] paramsnombre = { "id_documento" };
			String[] paramstipo = { "1" };
			String[] paramsformato = { "1" };

			xmlutils.crearXMLDoc();
			boolean procalmacenado = false;
			if (nombreProceso.equals(BINGO) || nombreProceso.equals(MUNICIPIO) || nombreProceso.equals(NOTARIOS)
					|| nombreProceso.equals(VEHICULO) || nombreProceso.equals(MODELO)
					|| nombreProceso.equals(FABRICANTE))
				procalmacenado = true;
			Object[] lstParamsEntrada = getParametrosEntrada("o", nombreProceso);
			paramsnombre = (String[]) lstParamsEntrada[0];
			paramstipo = (String[]) lstParamsEntrada[1];
			paramsformato = (String[]) lstParamsEntrada[2];

			xmlutils.abrirPetiProcNode("", "", null, null, new String[] { "nombre" }, new String[] { proceso });

			int numNodes = 1;
			if (procalmacenado) {
				xmlutils.crearParamNode(String.valueOf(numNodes), "1", "2", "");
				numNodes++;
				xmlutils.crearParamNode(String.valueOf(numNodes), "1", "2", "");
				numNodes++;
				xmlutils.crearParamNode(String.valueOf(numNodes), "USU_WEB_SAC", "1", "");
				numNodes++;
				xmlutils.crearParamNode(String.valueOf(numNodes), "33", "2", "");
				numNodes++;
			}
			for (int i = 0; i < paramsnombre.length; i++) {
				String valor = (String) p_parametros.get(paramsnombre[i]);
				if (paramsnombre[i].equals("P")) {
					valor = "P";
				} else if (paramsnombre[i].equals("S")) {
					valor = "S";
				}

				xmlutils.crearParamNode(String.valueOf(numNodes), valor, paramstipo[i], paramsformato[i]);
				numNodes++;
			}
			xmlutils.cerrarPetiProcNode();
			// la peticion a pasar
			XMLIn = xmlutils.informarXMLDoc();
			com.stpa.ws.server.util.Logger.debug("XMLIn: " + XMLIn,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		} catch (Throwable t) {
			com.stpa.ws.server.util.Logger.error("Error al construir XML de entrada", t,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException("Error al construir XML de entrada", t);
		}

		String[] paramsRecuperables = null;
		String[] estructRecuperables = null;
		Object[] lstRecuperables = getParametrosSalida("o", proceso);
		paramsRecuperables = (String[]) lstRecuperables[0];
		estructRecuperables = (String[]) lstRecuperables[1];

		int numFilas = -1;// asignar a otro valor para recuperar N filas de datos
		boolean condParamError = false;
		Object[] lstResultados = findWsLanzador(XMLIn, estructRecuperables, paramsRecuperables, numFilas,
				condParamError);

		/*
		 * condiciones finales: hay que eliminar los resultados fuera de rango segun la hoja de formularios
		 */
		if (nombreProceso.equals(TIPOVIA)) {
			for (int i = 0; i < lstResultados.length; i++) {
				if (((String[]) lstResultados[i]).length > 0) {
					// nada de descripciones vacias
					String tipoOfic = null;
					if (((String[]) lstResultados[i]).length > 1)
						tipoOfic = ((String[]) lstResultados[i])[1];
					if (StringUtils.isBlank(tipoOfic))
						lstResultados[i] = new String[0];
				}
			}
		} else if (nombreProceso.equals(OFICINA)) {
			for (int i = 0; i < lstResultados.length; i++) {
				if (((String[]) lstResultados[i]).length > 0) {
					// las oficinas son de tipo S y no la 61
					String codigoOrg = ((String[]) lstResultados[i])[0];
					String tipoOfic = "S";
					if (((String[]) lstResultados[i]).length > 2)
						tipoOfic = ((String[]) lstResultados[i])[2];
					if (codigoOrg.equals("61") || !tipoOfic.equalsIgnoreCase("S")) {
						lstResultados[i] = new String[0];
					}
				}
			}
		} else if (nombreProceso.equals(SUBORG)) {
			for (int i = 0; i < lstResultados.length; i++) {
				if (((String[]) lstResultados[i]).length > 0) {
					// los ayuntaminetos encima de 33079 NO son correctos
					String codigoOrg = ((String[]) lstResultados[i])[0];
					int num = 0;
					try {
						Integer n = Integer.parseInt(codigoOrg);
						if (n != null)
							num = n.intValue();
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
					if (num == 33034 || num > 33079) {
						lstResultados[i] = new String[0];
					}
				}
			}
		}

	
		if (nombreProceso.equals(PROVINCIA) || nombreProceso.equals(SUBORG) || nombreProceso.equals(OFICINA)
				|| nombreProceso.equals(TIPOVIA) || nombreProceso.equals(BINGO)) {
			int numtotal = 0;
			for (int i = 0; i < lstResultados.length; i++) {
				if (((String[]) lstResultados[i]).length > 1)
					numtotal++;
			}
			String[] lstCodigos = new String[numtotal];
			String[] lstValores = new String[numtotal];
			numtotal = 0;
			for (int i = 0; i < lstResultados.length; i++) {
				if (((String[]) lstResultados[i]).length > 1) {
					lstCodigos[numtotal] = ((String[]) lstResultados[i])[0];
					lstValores[numtotal] = ((String[]) lstResultados[i])[1];
					numtotal++;
				}
			}
			return new Object[] { lstCodigos, lstValores };
		}
		return lstResultados;
	}

	/**
	 * Devuelve la lista de los parametros a informar en la busqueda de WebService.
	 * @param modelo Modelo del formulario
	 * @param proceso Busqueda a realizar
	 * @return Lista de String[] de parametros,tipos y formatos
	 */
	private Object[] getParametrosEntrada(String modelo, String proceso) {
		String[] paramsnombre = new String[0];
		String[] paramstipo = new String[0];
		String[] paramsformato = new String[0];
		Object[] result = new Object[] { paramsnombre, paramstipo, paramsformato };
		if (!validateString(modelo) || !validateString(proceso))
			return result;

		if (proceso.equals(MUNICIPIO)) {
			paramsnombre = new String[] { "provincia", "P" };
			paramstipo = new String[] { "1", "1" };
			paramsformato = new String[] { "", "" };
		} else if (proceso.equals(BINGO)) { // 048, multiples salidas tipo p22
			paramsnombre = new String[] { "nombre_sala", "P" };
			paramstipo = new String[] { "1", "1" };
			paramsformato = new String[] { "", "" };
		} else if (proceso.equals(TIPOVIA)) { // 048, multiples salidas tipo p22
			paramsnombre = new String[] { "P" };
			paramstipo = new String[] { "1" };
			paramsformato = new String[] { "" };
		} else if (proceso.equals(PROVINCIA)) { // 048, multiples salidas tipo p22
			paramsnombre = new String[] { "P" };
			paramstipo = new String[] { "1" };
			paramsformato = new String[] { "" };
		} else if (proceso.equals(SUBORG)) { // 048, multiples salidas tipo p22
			paramsnombre = new String[] { "P" };
			paramstipo = new String[] { "1" };
			paramsformato = new String[] { "" };
		} else if (proceso.equals(VEHICULO)) { // 048, multiples salidas tipo p22
			paramsnombre = new String[] { "anyo", "num_ayuntamiento", "clase_vehic","P" };
			paramstipo = new String[] { "2", "2", "1","1" };
			paramsformato = new String[] { "", "", "","" };
		} else if (proceso.equals(FABRICANTE)) { // 048, multiples salidas tipo p22
			paramsnombre = new String[] { "tipo_veh", "anyo_trans", "P" };
			paramstipo = new String[] { "1", "2", "1" };
			paramsformato = new String[] { "", "", "","" };
		} else if (proceso.equals(MODELO)) { // 048, multiples salidas tipo p22
			paramsnombre = new String[] { "tipo_veh", "marca_veh", "anyo_trans", "","P" };
			paramstipo = new String[] { "1", "1", "2","1", "1" };
			paramsformato = new String[] { "", "", "", "","" };
		} else if (proceso.equals(OFICINA)) { // 048, multiples salidas tipo p22
			paramsnombre = new String[] { "P" };
			paramstipo = new String[] { "1" };
			paramsformato = new String[] { "" };
		} else if (proceso.equals(NOTARIOS)) { // 048, multiples salidas tipo p22
			paramsnombre = new String[] { "nif", "nombre", "fechaDoc", "P" };
			paramstipo = new String[] { "1", "1", "3", "1" };
			paramsformato = new String[] { "", "", "dd/mm/yyyy", "" };
		} else if (proceso.equals("INTERNET.obtenerDatosPersona")) { // 048, multiples salidas tipo p22
			paramsnombre = new String[] { "modelo", "tipo_persona", "nif", "cif" };
			paramstipo = new String[] { "1", "1", "1", "1" };
			paramsformato = new String[] { "", "", "", "" };
		} else if (proceso.equals(PORCVEH)) { // 048, multiples salidas tipo p22
			paramsnombre = new String[] { "P" };
			paramstipo = new String[] { "1" };
			paramsformato = new String[] { "" };
		} else if (proceso.equals(PORC_EB)) { // 048, multiples salidas tipo p22
			paramsnombre = new String[] { "P" };
			paramstipo = new String[] { "1" };
			paramsformato = new String[] { "" };
		}
		result = new Object[] { paramsnombre, paramstipo, paramsformato };
		return result;
	}

	/**
	 * Devuelve la lista de los parametrosa recuperar de la busqueda de WebService.
	 * @param modelo Modelo del formulario
	 * @param proceso Busqueda a realizar
	 * @return  Lista de String[] de parametros y estructuras
	 */
	private Object[] getParametrosSalida(String modelo, String proceso) {

		String[] paramsRecuperables = new String[] { "" };
		String[] estructRecuperables = new String[] { "" };
		Object[] result = new Object[] { paramsRecuperables, estructRecuperables };
		if (!validateString(modelo) || !validateString(proceso))
			return result;
		if (proceso.equals(MUNICIPIO)) { // 048, multiples salidas tipo p22
			paramsRecuperables = new String[] { "NUME1_CANU", "STRING1_CANU" };
			estructRecuperables = new String[] { "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS" };
		} else if (proceso.equals(BINGO)) { // 048, multiples salidas tipo p22
			paramsRecuperables = new String[] { "STRING1_CANU", "STRING2_CANU" };
			estructRecuperables = new String[] { "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS" };
		} else if (proceso.equals(TIPOVIA)) { // 048, multiples salidas tipo p22
			paramsRecuperables = new String[] { "STRING1_CANU", "STRING2_CANU", "STRING_CADE" };
			estructRecuperables = new String[] { "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CADE_CADENA" };
		} else if (proceso.equals(PROVINCIA)) { // 048, multiples salidas tipo p22
			paramsRecuperables = new String[] { "NUME1_CANU", "STRING1_CANU", "STRING_CADE" };
			estructRecuperables = new String[] { "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CADE_CADENA" };
		} else if (proceso.equals(SUBORG)) { // 048, multiples salidas tipo p22
			paramsRecuperables = new String[] { "NUME1_CANU", "STRING1_CANU", "STRING_CADE" };
			estructRecuperables = new String[] { "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CADE_CADENA" };
		} else if (proceso.equals("INTERNET.obtenerTarifasVehiculos")) { // 048, multiples salidas tipo p22
			paramsRecuperables = new String[] { "NUME1_CANU", "STRING1_CANU" };
			estructRecuperables = new String[] { "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS" };
		} else if (proceso.equals(FABRICANTE)) { // 048, multiples salidas tipo p22
			paramsRecuperables = new String[] { "STRING1_CANU" };
			estructRecuperables = new String[] { "CANU_CADENAS_NUMEROS" };
		} else if (proceso.equals(MODELO)) { // 048, multiples salidas tipo p22
			paramsRecuperables = new String[] { "FECHA1_CANU", "FECHA2_CANU", "FECHA3_CANU", "FECHA4_CANU",
					"NUME1_CANU", "NUME2_CANU", "NUME3_CANU", "NUME4_CANU", "STRING1_CANU", "STRING2_CANU",
					"STRING3_CANU", "STRING4_CANU" };
			estructRecuperables = new String[] { "", "", "", "", "", "", "", "", "", "", "", "" };
		} else if (proceso.equals(OFICINA)) { // 048, multiples salidas tipo p22
			paramsRecuperables = new String[] { "NUME1_CANU", "STRING1_CANU", "STRING2_CANU", "STRING_CADE" };
			estructRecuperables = new String[] { "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS",
					"CANU_CADENAS_NUMEROS", "CADE_CADENA" };
		} else if (proceso.equals(NOTARIOS)) { // 048, multiples salidas tipo p22
			paramsRecuperables = new String[] { "NUME1_CANU", "STRING1_CANU", "STRING3_CANU", "STRING2_CANU",
					"STRING5_CANU", "STRING4_CANU", "STRING_CADE" };
			estructRecuperables = new String[] { "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS",
					"CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS",
					"CADE_CADENA" };
		} else if (proceso.equals("INTERNET.obtenerDatosPersona")) { // 048, multiples salidas tipo p22
			paramsRecuperables = new String[] { "NOMBRE_PEDB", "ID_PERS", "TELEFONO_PEDB", "COD_SIGL", "NOMBRE_CALL",
					"NUM_DICO", "ESCALERA_DICO", "PLANTA_DICO", "PUERTA_DICO", "NOMBRE_MUNP", "OBSERVACIONES_DIRE",
					"NOMBRE_PROV", "COD_POSTAL" };
			estructRecuperables = new String[] { "", "", "", "", "", "", "", "", "", "", "", "", "" };
		} else if (proceso.equals(PORCVEH)) { // 048, multiples salidas tipo p22
			paramsRecuperables = new String[] { "STRING1_CANU", "NUME1_CANU", "NUME2_CANU", "NUME3_CANU", "NUME4_CANU",
					"STRING_CADE" };
			estructRecuperables = new String[] { "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS",
					"CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CADE_CADENA" };
		} else if (proceso.equals(PORC_EB)) { // 048, multiples salidas tipo p22
			paramsRecuperables = new String[] { "STRING1_CANU", "STRING2_CANU", "NUME1_CANU", "NUME2_CANU",
					"NUME3_CANU", "NUME4_CANU", "STRING_CADE" };
			estructRecuperables = new String[] { "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS",
					"CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS",
					"CADE_CADENA" };
		}
		result = new Object[] { paramsRecuperables, estructRecuperables };
		return result;
	}


}