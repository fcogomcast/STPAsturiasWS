package com.stpa.ws.server.formularios;

import java.util.HashMap;

import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.UtilsComunes;
import com.stpa.ws.server.util.XMLUtils;

/**
 * Implementa las funciones de busqueda para la insercion, recuperacion y verificacion de modelos y justificantes.
 * 
 * @author david.gimenez.banos
 * 
 */
public class VerificaDocumentosDAO extends BasePortletDAO {
	
	public static final String NIF_USUARIO = "nifUsuario";
	public static final String CODIGO_VER = "codigoVerificacion";
	public static final String CODIGO_DOC = "primerCaracter";
	public static final String NUMERO_VER = "numeroVer";
	public static final String PATH_DOCUMENTO = "pathDocumento";
	public static final String VALIDAR_CODIGO = "validarCodigoVerificacion";


	/**
	 * Servicios por WebService, incluyendo alta, recuperacion y listado de documentos.
	 * @param p_parametros Mapa con los parametros de busqueda, incluyendo "nombreProceso"
	 * @return
	 * @throws WebServicesException
	 */
	public Object[] recuperarListarAnadirDocumento(HashMap<String, String> p_parametros) throws StpawsException {
		String XMLIn = "<error></error>";
		if (p_parametros.isEmpty()) {
			return new Object[0];
		}
		if (!UtilsComunes.isAccesoWebservices())
			return new String[0];
		String nombreProceso = p_parametros.get("nombreProceso");
		XMLUtils xmlutils = new XMLUtils();
		String proceso = "";
		try {
			String[] paramsnombre = { "id_documento", "nif_cif", "tipoid", "idsession" };
			String[] paramstipo = { "1", "1", "1", "1" };
			String[] paramsformato = { "1", "1", "1", "1" };

			xmlutils.crearXMLDoc();
			boolean procalmacenado = false;
			if (nombreProceso.equalsIgnoreCase("ALTA")) {
				proceso = "INTERNET_DOCUMENTOS.AltaDocumento";
				paramsnombre = new String[] { "tipo_doc", "nombre_documento", "cod_verificacion", "nif_cif", "tipo_id",
						"id_sesion", "origen_pt", "", "publicable", "documentozip","P" };
				paramstipo = new String[] { "1", "1", "1", "1", "1", "1", "1", "1", "1", "4","1" };
				paramsformato = new String[] { "", "", "", "", "", "", "", "", "", "","" };
			} else if (nombreProceso.equalsIgnoreCase("LISTA")) {
				proceso = "INTERNET_DOCUMENTOS.ListaDocumentos";
				paramsnombre = new String[] { "tipo_doc", "tipo_id", "nif_cif", "id_sesion", "cod_verificacion", "nombre_documento", "fecha_inicio", "fecha_fin","P"
				 };
				paramstipo = new String[] { "1", "1", "1", "1", "1", "1", "3", "3","1" };
				paramsformato = new String[] { "", "", "", "", "", "", "dd/mm/yyyy", "dd/mm/yyyy","" };
			} else if (nombreProceso.equalsIgnoreCase("RECUPERAR")) {
				proceso = "INTERNET_DOCUMENTOS.RecuperarDocumento";
				paramsnombre = new String[] { "id_documento", "tipo_id", "nif_cif", "id_sesion","P" };
				paramstipo = new String[] { "1", "1", "1", "1","1" };
				paramsformato = new String[] { "", "", "", "","" };
			} else if (nombreProceso.equalsIgnoreCase("NUMEROSERIE")) {
				proceso = "INTERNET.NumeroSerie";
				paramsnombre = new String[] { "modelo_doc", "P", };
				paramstipo = new String[] { "2", "1" };
				paramsformato = new String[] { "", "" };
				procalmacenado = true;
			} else if (nombreProceso.equalsIgnoreCase("EMISORA")) {
				proceso = "INTERNET.Emisor";
				paramsnombre = new String[] { "modelo_doc", "P", };
				paramstipo = new String[] { "2", "1" };
				paramsformato = new String[] { "", "" };
				procalmacenado = true;
			} else if (nombreProceso.equalsIgnoreCase("OBTENERCLAVE")) {
				proceso = "INTERNET.obtenerClave";
				paramsnombre = new String[] { "P", };
				paramstipo = new String[] { "1" };
				paramsformato = new String[] { "" };
				procalmacenado = false;
			}

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
				xmlutils.crearParamNode(String.valueOf(numNodes), valor, paramstipo[i], paramsformato[i]);
				numNodes++;
			}
			xmlutils.cerrarPetiProcNode();
			// la peticion a pasar
			XMLIn = xmlutils.informarXMLDoc();
			com.stpa.ws.server.util.Logger.debug("VerificaDocumentosDAO.recuperarListarAnadirDocumento",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);

		} catch (Throwable t) {
			com.stpa.ws.server.util.Logger.error("Error al construir XML de entrada", t,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException("Error al construir XML de entrada", t);
		}

		String[] paramsRecuperables = null;
		String[] estructRecuperables = null;
		if (proceso.equals("INTERNET_DOCUMENTOS.AltaDocumento")) {
			estructRecuperables = new String[] { "CADE_CADENA" };
			paramsRecuperables = new String[] { "STRING_CADE" };
		} else if (proceso.equals("INTERNET_DOCUMENTOS.ListaDocumentos")) {
			estructRecuperables = new String[] { "CADE_CADENA", "CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS",
					"CANU_CADENAS_NUMEROS", "", "CANU_CADENAS_NUMEROS" };
			paramsRecuperables = new String[] { "STRING_CADE", "NUME1_CANU", "STRING2_CANU", "STRING3_CANU",
					"FECHA1_CANU", "STRING4_CANU" };
		} else if (proceso.equals("INTERNET_DOCUMENTOS.RecuperarDocumento")) {
			estructRecuperables = new String[] { "CADE_CADENA", "CLOB_CLOB" };
			paramsRecuperables = new String[] { "STRING_CADE", "CLOB_DATA" };
		} else if (proceso.equals("INTERNET.NumeroSerie")) {
			estructRecuperables = new String[] { "" };
			paramsRecuperables = new String[] { "STRING_CADE" };
		} else if (proceso.equals("INTERNET.Emisor")) {
			estructRecuperables = new String[] { "" };
			paramsRecuperables = new String[] { "STRING_CADE" };
		} else if (proceso.equals("INTERNET.obtenerClave")) {
			estructRecuperables = new String[] { "" };
			paramsRecuperables = new String[] { "STRING_CADE" };
		} else {
			estructRecuperables = new String[] { "" };
			paramsRecuperables = new String[] { "" };
		}
		int numFilas = -1;
		if (proceso.equals("INTERNET_DOCUMENTOS.RecuperarDocumento"))
			numFilas = 1;
		boolean condParamError = false;
		Object[] lstResultados = null;
		if (!UtilsComunes.isAccesoWebservices())
			lstResultados = new Object[0];
		else
			lstResultados = findWsLanzador(XMLIn, estructRecuperables, paramsRecuperables, numFilas,
				condParamError);

		return lstResultados;
	}
}