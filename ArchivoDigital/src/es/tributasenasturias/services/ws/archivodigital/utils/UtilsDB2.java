package es.tributasenasturias.services.ws.archivodigital.utils;

import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;


import es.tributasenasturias.services.ws.archivodigital.utils.Preferencias.Preferencias;
import es.tributasenasturias.services.ws.archivodigital.utils.Preferencias.PreferenciasException;
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
	
	public CallableStatement guardarArchivo(Connection conn, String codUsua, 
			String identificador, 
			String tipoElemento, 
			String nombreFichero, 
			String comprimido,
			byte[] archivo, 
			String hashArchivo, 
			String firmadoPorCSV,
			String firmadoPorCertificadoInterno,
			String firmadoPorCertificadoExterno,
			Date fechaCertificado,
			String csv, 
			String metadatos, 
			String baseCsv
			/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
			,String hashFirma,
			String nombreFirmante,
			String fechaFirma,
			String idTransac,
			String cargoUsua,
			String formatoFirma
			/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
	) throws java.sql.SQLException, java.io.IOException
	{
		CallableStatement cs=null;
		// CRUBENCVS 41435. Procedimientos  como preferencias
		Preferencias pref;
		try {
			pref = Preferencias.getPreferencias();
		} catch (PreferenciasException pe){
			throw new java.io.IOException("Error al recuperar las preferencias", pe);
		}
		//Recuperamos de la petición la cadena de llamada jdbc.
		/* INIPETITRIBUTAS-5 ENAVARRO ** 22/10/2019 ** Compulsa */
		/* String call = "{?= call archivo_digital.guardararchivo(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"; */
		//String call = "{?= call archivo_digital.guardararchivo(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		String call = "{?= call "+pref.getPAGuardarArchivo() +"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		/* FINPETITRIBUTAS-5 ENAVARRO ** 22/10/2019 ** Compulsa */
		cs = conn.prepareCall(call);
		cs.registerOutParameter(1, java.sql.Types.VARCHAR);
		cs.setString(2, codUsua);
		cs.setString(3, identificador);
		cs.setString(4, tipoElemento);
		cs.setString(5, nombreFichero);
		cs.setString(6, comprimido);		
		Blob imgDataBlob = conn.createBlob();
        imgDataBlob.setBytes(1, archivo);
        cs.setBlob(7, imgDataBlob);
        //cs.setString(7, "");
		cs.setString(8, hashArchivo);
		cs.setString(9, csv);
		/* INIPETITRIBUTAS-7 MCMARCO ** 31/03/2020 ** Metadatos */
		//cs.setString(10, metadatos);
		Clob metadatosClob = conn.createClob();
		metadatosClob.setString(1, metadatos);
		cs.setClob(10, metadatosClob);
		/* FINPETITRIBUTAS-7 MCMARCO ** 31/03/2020 ** Metadatos */
				
		cs.setString(11, baseCsv);
		cs.setString(12, firmadoPorCSV);
		cs.setString(13, firmadoPorCertificadoInterno);
		cs.setString(14, firmadoPorCertificadoExterno);
		if (fechaCertificado!=null) {
			Timestamp t= new Timestamp(fechaCertificado.getTime());
			cs.setTimestamp(15, t);
		} else {
			cs.setTimestamp(15, null);
		}
		/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
		Clob hashFirmaClob = conn.createClob();
		hashFirmaClob.setString(1, hashFirma);
		cs.setClob(16, hashFirmaClob);
		cs.setString(17, nombreFirmante);
		cs.setString(18, fechaFirma);
		cs.setString(19, idTransac);
		cs.setString(20, cargoUsua);
		cs.setString(21, formatoFirma);
		/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
		
		//logger.info("PL: call archivo_digital.guardararchivo(" + codUsua + "," + identificador + "," + tipoElemento + "," + 
		//		nombreFichero  + "," +  comprimido  + "," +  "<archivo>," + hashArchivo+ csv  + "," +  metadatos  + "," + baseCsv);
		//cs.execute();
		//cResult = cs.getSQLXML(1);
		//return cResult.getString();
		return cs;
	}

	public CallableStatement obtenerArchivo(Connection conn, Integer idArchivo, String codUsua, String obtenerSoloDatosArchivo) throws java.sql.SQLException, java.io.IOException
	{
		CallableStatement cs=null;
		// CRUBENCVS 41435. Procedimientos  como preferencias
		Preferencias pref;
		try {
			pref = Preferencias.getPreferencias();
		} catch (PreferenciasException pe){
			throw new java.io.IOException("Error al recuperar las preferencias", pe);
		}
		//Recuperamos de la petición la cadena de llamada jdbc.
		/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
		// String call = "{?= call archivo_digital.obtenerxmldatosarchivo(?,?,?,?)}";
		//String call = "{?= call archivo_digital.obtenerxmldatosarchivo(?,?,?,?,?)}";
		String call = "{?= call "+pref.getPAObtenerArchivo() + "(?,?,?,?,?)}";
		/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
		cs = conn.prepareCall(call);
		cs.registerOutParameter(1, java.sql.Types.CLOB);
		cs.setString(2, codUsua);
		cs.setInt(3, idArchivo);
		//Blob imgDataBlob = conn.createBlob();
        cs.registerOutParameter(4, java.sql.Types.BLOB);
        /* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
        cs.registerOutParameter(5, java.sql.Types.CLOB);
        /* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
        if ("S".equalsIgnoreCase(obtenerSoloDatosArchivo)) {
        	/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
        	/* cs.setString(5, "S"); */
        	cs.setString(6, "S");
        	/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
        } else 
        {
        	/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
        	/* cs.setString(5, "N"); */
        	cs.setString(6, "N");
        	/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
        }
		//cs.execute();
		//cResult = cs.getSQLXML(1);
		//return cResult.getString();
		return cs;
	}
	
	public CallableStatement existeCSV(Connection conn, String csv) throws java.sql.SQLException, java.io.IOException
	{
		CallableStatement cs=null;
		// CRUBENCVS 41435. Procedimientos  como preferencias
		Preferencias pref;
		try {
			pref = Preferencias.getPreferencias();
		} catch (PreferenciasException pe){
			throw new java.io.IOException("Error al recuperar las preferencias", pe);
		}
		//Recuperamos de la petición la cadena de llamada jdbc.
		//String call = "{?= call archivo_digital.existecsv(?)}";
		String call = "{?= call "+pref.getPAExisteCSV()+"(?)}";
		cs = conn.prepareCall(call);
		cs.registerOutParameter(1, java.sql.Types.VARCHAR);
		cs.setString(2, csv);
		//cs.execute();

		return cs;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private String lanzaPLguardarArchivo (String codUsua, 
			String identificador, String tipoElemento, String nombreFichero, String comprimido,
			byte[] archivo, String hashArchivo, String csv, String metadatos, String baseCsv) {
		Connection conn = null;
		CallableStatement cs = null;
		FileOutputStream myFO = null;
		String sOutDB = new String();
		List<Clob> arrClob=new ArrayList<Clob>(); //Para almacenar referencias a los CLOBs creados
		//y poder liberar su memoria posteriormente
		try {
			Preferencias _pr=Preferencias.getPreferencias();
			//Obtener conexion de base de datos
			Hashtable ht = new Hashtable();
			ht.put( Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory" );
			//ht.put( Context.PROVIDER_URL,"t3://localhost:7001"); 
			ht.put( Context.PROVIDER_URL,_pr.getProviderURL());
			Context ctx = new InitialContext( ht ); 
			//javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup("gtpaDS");
			//javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup(_pr.getDataSource());
			/*conn = ds.getConnection();

			//Liberamos la referencia al objeto de petición, puede ser muy grande y no sabemos
			//cuanto tiempo tarda la llamada en base de datos, de esta forma el recolector
			//puede liberar su memoria inmediatamente.
			if (cs==null)
			{
				logger.error("No se ha podido montar la llamada");
				return "No ejecutado ok";
			}*/
			//autoCommit=conn.getAutoCommit();
			//conn.setAutoCommit(false);
			//sOutDB = guardarArchivo(conn, codUsua, identificador, tipoElemento, nombreFichero, comprimido, archivo, hashArchivo, csv, metadatos, baseCsv);

			//cs.execute();
			//conn.commit();
			//conn.setAutoCommit(autoCommit);
			// Enviar resultado de parametro de salida como respuesta al WebService
		} /*catch (SQLException e)
		{
			String err="";
			try
			{
				conn.rollback();
			} catch (java.sql.SQLException ex)
			{
				err="No se ha podido realizar el rollback: "+ ex.getMessage();
			}
			
			err+=e.getMessage()+e.toString();
			logger.error(err);
			logger.trace(e.getStackTrace());
			return "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?><estructuras><error><![CDATA["+
            err
            +"]]></error></estructuras>";
			
		} */catch (Exception e) {
			String err="";
			/*try
			{
				conn.rollback();
			}
			catch (java.sql.SQLException ex)
			{
				err="No se ha podido realizar el rollback: "+ ex.getMessage();
			}*/
			
			err+=e.getMessage()+e.toString();
			//logger.error(err);
			//logger.trace(e.getStackTrace());
			return "No ejecutado ok: " + err;
		} finally {
			if (cs != null) {
				try {cs.close();} catch (Exception e) {}
			}
			if (conn != null) {
				try {conn.close();} catch (Exception e) {}
			}
			if (myFO != null) {
				try {myFO.close();} catch (Exception e) {}
			}
			if (arrClob.size()>0)
			{
				for (Clob c: arrClob)
				{
					try
					{
						c.free();
					}
					catch (SQLException e)
					{
						
					}
				}
			}
			
		}
		//if (cResult == null) {
		if (sOutDB.length()==0) {
			return "No ejecutado ok";
		} else {
			return sOutDB;
		}
		
	}

	public CallableStatement obtenerArchivoPorCSV(Connection conn, String csv, String codUsua) throws java.sql.SQLException, java.io.IOException
	{
		CallableStatement cs=null;
		// CRUBENCVS 41435. Procedimientos  como preferencias
		Preferencias pref;
		try {
			pref = Preferencias.getPreferencias();
		} catch (PreferenciasException pe){
			throw new java.io.IOException("Error al recuperar las preferencias", pe);
		}
		//Recuperamos de la petición la cadena de llamada jdbc.
		/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
		/* String call = "{?= call archivo_digital.obtenerarchivoporcsv(?,?)}"; */
		//String call = "{?= call archivo_digital.obtenerarchivoporcsv(?,?,?)}";
		String call = "{?= call "+pref.getPAObtenerArchivoCSV() +"(?,?,?)}";
		/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
		cs = conn.prepareCall(call);
		cs.registerOutParameter(1, java.sql.Types.CLOB);
		cs.setString(2, csv);
        cs.registerOutParameter(3, java.sql.Types.BLOB);
        /* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
        cs.registerOutParameter(4, java.sql.Types.CLOB);
        /* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
		return cs;
	}
	
	public CallableStatement obtenerArchivosPorHash(Connection conn, String hash, String codUsua) throws java.sql.SQLException, java.io.IOException
	{
		CallableStatement cs=null;
		// CRUBENCVS 41435. Procedimientos  como preferencias
		Preferencias pref;
		try {
			pref = Preferencias.getPreferencias();
		} catch (PreferenciasException pe){
			throw new java.io.IOException("Error al recuperar las preferencias", pe);
		}
		//Recuperamos de la petición la cadena de llamada jdbc.
		//String call = "{?= call archivo_digital.obtenerarchivosporhash(?,?)}";
		String call = "{?= call "+ pref.getPAObtenerArchivoHash() +"(?,?)}";
		cs = conn.prepareCall(call);
		cs.registerOutParameter(1, java.sql.Types.VARCHAR);
		cs.setString(2, hash);
		cs.setString(3, codUsua);
		return cs;
	}
	
	/* INIPETITRIBUTAS-2 ENAVARRO ** 04/09/2019 ** Custodia */
	public CallableStatement guardarFirma(Connection conn, String codigoUsuario, int idadar, String hashFirma, String nombreFirmante, String fechaFirma, 
			/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
			/* String idTransac, String justificanteFirma, String cargoUsua, String csv, String formatoFirma, String metadatosFirma, String base, String tipoFirma,*/ 
			String idTransac, String cargoUsua, String csv, String formatoFirma, String metadatosFirma, String base, String tipoFirma, 
			/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
    		String idFirmaExt, String fechaFirmaHuella) throws java.sql.SQLException, java.io.IOException
	{
		CallableStatement cs=null;
		// CRUBENCVS 41435. Procedimientos  como preferencias
		Preferencias pref;
		try {
			pref = Preferencias.getPreferencias();
		} catch (PreferenciasException pe){
			throw new java.io.IOException("Error al recuperar las preferencias", pe);
		}
		//Recuperamos de la petición la cadena de llamada jdbc.
		/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
		/* String call = "{?= call archivo_digital.guardarfirma(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"; */
		//String call = "{?= call archivo_digital.guardarfirma(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		String call = "{?= call "+pref.getPAGuardarFirma()+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
		cs = conn.prepareCall(call);
		// Se indica que el primer interrogante (?= call...) es de salida.
		cs.registerOutParameter(1, java.sql.Types.CLOB);
		cs.setString(2, codigoUsuario);
		cs.setInt(3, idadar);
		cs.setString(4, hashFirma);
		cs.setString(5, nombreFirmante);
		cs.setString(6, fechaFirma);		
        cs.setString(7, idTransac);
        /* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
		/*cs.setString(8, justificanteFirma);
		cs.setString(9, cargoUsua);
		cs.setString(10, csv);		
		cs.setString(11, formatoFirma);
		cs.setString(12, metadatosFirma);
		cs.setString(13, base);
		cs.setString(14, tipoFirma);
		cs.setString(15, idFirmaExt);
		cs.setString(16, fechaFirmaHuella);*/
		cs.setString(8, cargoUsua);
		cs.setString(9, csv);		
		cs.setString(10, formatoFirma);
		cs.setString(11, metadatosFirma);
		cs.setString(12, base);
		cs.setString(13, tipoFirma);
		cs.setString(14, idFirmaExt);
		cs.setString(15, fechaFirmaHuella);
		/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
		
		return cs;
	}
	/* FINPETITRIBUTAS-2 ENAVARRO ** 04/09/2019 ** Custodia */
	/* INIPETITRIBUTAS-88 MCMARCO ** 28/04/2020 ** Formato XML del expediente electrónico */	
	public CallableStatement guardarMetadatos(Connection conn, String codigoUsuario, int idadar, 
			String metadatos) throws java.sql.SQLException, java.io.IOException
	{
		CallableStatement cs=null;
		// CRUBENCVS 41435. Procedimientos  como preferencias
		Preferencias pref;
		try {
		    pref = Preferencias.getPreferencias();
		} catch (PreferenciasException pe){
			throw new java.io.IOException("Error al recuperar las preferencias", pe);
		}
		//String call = "{?= call archivo_digital.guardametadatos(?,?)}";
		String call = "{?= call "+pref.getPAGuardarMetadatos()+"(?,?)}";
		
		cs = conn.prepareCall(call);
		// Se indica que el primer interrogante (?= call...) es de salida.
		cs.registerOutParameter(1, java.sql.Types.VARCHAR);
		//cs.setString(2, codigoUsuario);
		cs.setInt(2, idadar);
		
		Clob metadatosClob = conn.createClob();
		metadatosClob.setString(1, metadatos);
		cs.setClob(3, metadatosClob);
	
		
		return cs;
	}
	/* FINPETITRIBUTAS-88 MCMARCO ** 28/04/2020 ** Formato XML del expediente electrónico */
	/* INIPETITRIBUTAS-26 ENAVARRO ** 17/07/2020 ** Firma en tableta */
	public CallableStatement vaciarFirmas(Connection conn, Integer idArchivo) throws java.sql.SQLException, java.io.IOException
	{
		CallableStatement cs=null;
		// CRUBENCVS 41435. Procedimientos  como preferencias
		Preferencias pref;
		try {
			pref = Preferencias.getPreferencias();
		} catch (PreferenciasException pe){
			throw new java.io.IOException("Error al recuperar las preferencias", pe);
		}
		//Recuperamos de la petición la cadena de llamada jdbc.
		//String call = "{?= call archivo_digital.vaciarFirmasBiometricas(?)}";
		String call = "{?= call "+pref.getPAVaciarFirmas() + "(?)}";
		cs = conn.prepareCall(call);
		cs.registerOutParameter(1, java.sql.Types.VARCHAR);
		cs.setInt(2, idArchivo);

		return cs;
	}
	/* FINPETITRIBUTAS-26 ENAVARRO ** 17/07/2020 ** Firma en tableta */
	
	// CRUBENCVS 41435. Custodia de versión
	public CallableStatement custodiarVersion(
			Connection conn, 
			String codUsua, 
			String identificador, 
			String tipoElemento, 
			String nombreFichero, 
			String comprimido,
			byte[] archivo, 
			String hashArchivo, 
			String firmadoPorCSV,
			String firmadoPorCertificadoInterno,
			String firmadoPorCertificadoExterno,
			Date fechaCertificado,
			String csv, 
			String metadatos, 
			String baseCsv
			/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
			,String hashFirma,
			String nombreFirmante,
			String fechaFirma,
			String idTransac,
			String cargoUsua,
			String formatoFirma,
			/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
			String idArchivoAnterior
	) throws java.sql.SQLException, java.io.IOException
	{
		CallableStatement cs=null;
		// CRUBENCVS 41435. Procedimientos  como preferencias
		Preferencias pref;
		try {
			pref = Preferencias.getPreferencias();
		} catch (PreferenciasException pe){
			throw new java.io.IOException("Error al recuperar las preferencias", pe);
		}
		//String call = "{?= call archivo_digital.custodiar_version(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		String call = "{?= call "+pref.getPACustodiarVersion()+"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		cs = conn.prepareCall(call);
		cs.registerOutParameter(1, java.sql.Types.VARCHAR);
		cs.setString(2, codUsua);
		cs.setString(3, identificador);
		cs.setString(4, tipoElemento);
		cs.setString(5, nombreFichero);
		cs.setString(6, comprimido);		
		Blob imgDataBlob = conn.createBlob();
        imgDataBlob.setBytes(1, archivo);
        cs.setBlob(7, imgDataBlob);
        //cs.setString(7, "");
		cs.setString(8, hashArchivo);
		cs.setString(9, csv);
		/* INIPETITRIBUTAS-7 MCMARCO ** 31/03/2020 ** Metadatos */
		//cs.setString(10, metadatos);
		Clob metadatosClob = conn.createClob();
		metadatosClob.setString(1, metadatos);
		cs.setClob(10, metadatosClob);
		/* FINPETITRIBUTAS-7 MCMARCO ** 31/03/2020 ** Metadatos */
				
		cs.setString(11, baseCsv);
		cs.setString(12, firmadoPorCSV);
		cs.setString(13, firmadoPorCertificadoInterno);
		cs.setString(14, firmadoPorCertificadoExterno);
		if (fechaCertificado!=null) {
			Timestamp t= new Timestamp(fechaCertificado.getTime());
			cs.setTimestamp(15, t);
		} else {
			cs.setTimestamp(15, null);
		}
		/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
		Clob hashFirmaClob = conn.createClob();
		hashFirmaClob.setString(1, hashFirma);
		cs.setClob(16, hashFirmaClob);
		cs.setString(17, nombreFirmante);
		cs.setString(18, fechaFirma);
		cs.setString(19, idTransac);
		cs.setString(20, cargoUsua);
		cs.setString(21, formatoFirma);
		/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
		
		cs.setString(22, idArchivoAnterior);

		return cs;
	}
}
