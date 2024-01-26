package es.tributasenasturias.documentopagoutils;

import java.util.ArrayList;
import java.util.List;

import es.tributasenasturias.documentopagoutils.StpawsException;


public class LiteralesUtils {
	
	public static String obtenerLiteralFromInorInfoOrga(String organismo, String tiio) {
		String literal = null;
		
		String tabla = Constantes.FUNCION_INOR_INFO_ORGA;
		
		List<CampoWS> camposEntrada = new ArrayList<CampoWS>(); 
		CampoWS campo1 = new CampoWS("0","p_in_idOrga", "NUMERO", organismo);
		CampoWS campo2 = new CampoWS("1","p_in_codTiio", "CADENA", tiio );
		
		camposEntrada.add(campo1);
		camposEntrada.add(campo2);			
		
		CampoRecuperarWS campoLiteral = new CampoRecuperarWS ("TMP_CADENAS_NUMEROS", "C1");
		try {
			literal = es.tributasenasturias.documentopagoutils.LiteralesWS.obtenerLiteral(tabla, camposEntrada, campoLiteral);
		} catch (StpawsException e) {
			es.tributasenasturias.documentopagoutils.Logger.error("LiteralesUtils.obtenerLiteralFromInorInfoOrga|"+organismo+"|"+tiio,e,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}		
		return literal;		
	}
	
	public static String obtenerLiteralFromTextTextos(String idText) {
		String literal = null;
		
		String tabla = Constantes.FUNCION_TEXT_TEXTOS;
	
		List<CampoWS> camposEntrada = new ArrayList<CampoWS>(); 
		CampoWS campo1 = new CampoWS("0","p_in_idText", "NUMERO", idText);		
		camposEntrada.add(campo1);
		
		CampoRecuperarWS campoLiteral = new CampoRecuperarWS ("MEMO_MEMO", "STRING_MEMO");		
		
		try {
			literal = LiteralesWS.obtenerLiteral(tabla, camposEntrada, campoLiteral);
		} catch (StpawsException e) {
			es.tributasenasturias.documentopagoutils.Logger.error("LiteralesUtils.obtenerLiteralFromTextTextos|"+idText,e,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}		
		return literal;		
	}
}
