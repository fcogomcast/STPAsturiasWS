package com.stpa.ws.server.formularios;

import java.util.Map;

public interface IModeloFormBean {

	// los campos de fecha y datos bancarios cambian en el xml
	public Map<String, Object> getMapParametrosParaXml(Map<String, String[]> mapParams);

	public String[] getNoValidateMessagesKeys();

	public void rellenarCamposAltaDocumento();
}
