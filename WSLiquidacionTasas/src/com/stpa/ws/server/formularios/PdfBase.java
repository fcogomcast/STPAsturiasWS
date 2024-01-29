package com.stpa.ws.server.formularios;

import java.util.HashMap;
import java.util.Map;

public abstract class PdfBase {

	public String plantilla;
	public Map<String, String> Session = new HashMap<String, String>();
	
	public abstract String getPlantilla();
}
