package es.tributasenasturias.services.ws.wsmultas.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import javax.xml.ws.Holder;

import es.tributasenasturias.services.ws.wsmultas.utils.Log.Logger;
/**
 * Permite acceso a base de datos, y soporta parámetros de tipo array.
 * @author crubencvs
 *
 */
public class UtilsDB2 {

	private Logger logger = null;
	
	public UtilsDB2() {

		logger = new Logger();
		try {

		} catch (Exception ex) {
			logger.error("En constructor de clase Datos: " + ex.getMessage());
		}
	}

	public CallableStatement multasExternasObtenerTablas(Connection conn, int municipio, String tabla,
			Holder<String> fecha) throws java.sql.SQLException, java.io.IOException
	{
		CallableStatement cs=null;
		//SQLXML cResult = null;

		//Recuperamos de la petición la cadena de llamada jdbc.
		String call = "{?= call sw_multas.multasexternasobtenerxmltabla(?,?,?,?)}";
		cs = conn.prepareCall(call);
		cs.registerOutParameter(1, java.sql.Types.VARCHAR);
		cs.setInt(2, municipio);
		cs.setString(3, fecha.value);
		cs.setString(4, tabla);
		cs.registerOutParameter(5, java.sql.Types.CLOB);
		//cs.execute();
		//cResult = cs.getSQLXML(1);
		//return cResult.getString();
		return cs;
	}
}
