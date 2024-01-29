package es.tributasenasturias.business;

public final class ComponerRespuesta {
	
	ComponerRespuesta () {}
	
	public final static String getRespuestaCompuesta(String numAutoliquidacion, String errorData) {
		String v_respuesta = new String();				
		v_respuesta += "<autoliquidaciones>";
		if (!numAutoliquidacion.isEmpty()) {
			v_respuesta += "<autoliquidacion>"+numAutoliquidacion+"</autoliquidacion>";
		}
		if (!errorData.isEmpty()) {
			v_respuesta += "<error>"+errorData+"</error>";
		}
		v_respuesta += "</autoliquidaciones>";		
		return v_respuesta;
	}
	
	public final static String getRespuestaCifrada(String numAutoliquidacion, String errorData) {
		String v_respuesta = new String();
		v_respuesta += "<autoliquidaciones>";
		if (!numAutoliquidacion.isEmpty()) {
			v_respuesta += "<autoliquidacion>"+numAutoliquidacion+"</autoliquidacion>";
		}
		if (!errorData.isEmpty()) {
			v_respuesta += "<error>"+errorData+"</error>";
		}
		v_respuesta += "</autoliquidaciones>";
		Cifrar cifrar = new Cifrar(v_respuesta);
		return cifrar.getResultado();		
	}

}
