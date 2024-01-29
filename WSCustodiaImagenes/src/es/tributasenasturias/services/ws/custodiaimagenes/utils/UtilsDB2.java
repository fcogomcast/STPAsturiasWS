package es.tributasenasturias.services.ws.custodiaimagenes.utils;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * Permite acceso a base de datos, y soporta parámetros de tipo array.
 * @author crubencvs
 *
 */
public class UtilsDB2 {


	public UtilsDB2() {
	}
	
	/**
	 * Convierte la fecha de formato Oracle a fecha de Java. Como no es cuestión de hacerse un parser,
	 * se hace enviando la fecha a BD para que nos la devuelva.
	 * @param valor     Cadena que contiene una fecha
	 * @param formato   Formato de fecha según Oracle
	 * @param conn      Conexión a base de datos
	 * @return          Fecha como objeto {@link Date}
	 * @throws java.sql.SQLException
	 */
	@SuppressWarnings("unused")
	private java.sql.Date convertDateFromOracle (String valor, String formato, Connection conn) throws java.sql.SQLException
	{
		Statement st=null;
		java.sql.Date result=null;
		ResultSet rs=null;
		try
		{
			String call=null;
		if (formato!=null)
		{
			call = "SELECT to_date('"+valor+"','"+formato+"') from dual";
		}
		else
		{
			call = "SELECT to_date('"+valor+"') from dual";
		}
		st = conn.createStatement();
		rs = st.executeQuery(call);
		if (rs.next())
		{
			result = rs.getDate(1);
		}
		return result;
		}
		finally 
		{
			if (rs!=null)
			{
				rs.close();
			}
		}
		
	}
	
	public CallableStatement custodiaficmultas(Connection conn, String nombreArchivo, byte[] archivo,
			String codUsuario, int limite) throws java.sql.SQLException, java.io.IOException
	{
		CallableStatement cs=null;

		//Recuperamos de la petición la cadena de llamada jdbc.
		String call = "{?= call archivo_digital.custodiaficmultas(?,?,?,?,?)}";
		cs = conn.prepareCall(call);
		cs.registerOutParameter(1, java.sql.Types.VARCHAR);
		cs.setString(2, nombreArchivo);
		Blob imgDataBlob = conn.createBlob();
        imgDataBlob.setBytes(1, archivo);
        cs.setBlob(3, imgDataBlob);
		cs.setString(4, codUsuario);
		cs.registerOutParameter(5, java.sql.Types.BLOB);
		cs.setInt(6, limite);
		return cs;
	}


}
