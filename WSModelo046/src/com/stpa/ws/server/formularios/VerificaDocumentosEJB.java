package com.stpa.ws.server.formularios;

import java.util.HashMap;

import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.SHAUtils;

public class VerificaDocumentosEJB {

	public String recuperarCodigoVerificacion(String doc_just_nif, boolean doValidate) throws StpawsException {
		if (!validateString(doc_just_nif) || (doValidate && doc_just_nif.length() < (1 + 14 + 8))){
			com.stpa.ws.server.util.Logger.error("Error de código de verificación",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException("Error de código de verificación" , null);
		}
		doc_just_nif = doc_just_nif.toUpperCase();
		String clave = (String) obtenerClave();
		String res = SHAUtils.hex_hmac_sha1(clave, doc_just_nif);
		if (res.length() < 16)
			return res;
		return res.substring(res.length() - 16, res.length());
		// return "";
	}

	public Object obtenerClave() throws StpawsException {

		VerificaDocumentosDAO dao = new VerificaDocumentosDAO();
		HashMap<String, String> parametros = new HashMap<String, String>();
		parametros.put("nombreProceso", "OBTENERCLAVE");
		parametros.put("P", "P");
		Object[] result = dao.recuperarListarAnadirDocumento(parametros);
		if (result.length > 0) {
			if (result[0] instanceof String[])
				return ((String[]) result[0])[0];
			else
				return (String) result[0];
		}
		return "";
	}
	
	private boolean validateString(String p_str) {
		return (p_str != null && !p_str.equals(""));
	}
}