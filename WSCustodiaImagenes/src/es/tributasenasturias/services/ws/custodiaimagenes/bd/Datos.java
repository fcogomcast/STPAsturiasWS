package es.tributasenasturias.services.ws.custodiaimagenes.bd;

import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.Holder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import es.tributasenasturias.services.ws.custodiaimagenes.utils.UtilsDB2;
import es.tributasenasturias.services.ws.custodiaimagenes.utils.Preferencias.Preferencias;


public class Datos {


	public Datos() {
	}

	
	 private static Document convertStringToDocument(String xmlStr) {
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	        DocumentBuilder builder;  
	        try  
	        {  
	            builder = factory.newDocumentBuilder();  
	            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
	            return doc;
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } 
	        return null;
	    }
	 
	@SuppressWarnings("unchecked")
	public void CustodiaFicMultas(String nombreArchivo, byte[] archivo,
			String codUsuario, int limite, Holder<byte[]> archivoRes,
			Holder<String> codError, Holder<String> error,
			Holder<String> nomArchivo) {

		UtilsDB2 util = new UtilsDB2();
		Connection conn = null;
		CallableStatement cs = null;
		boolean autoCommit;

		try {
			Preferencias _pr=Preferencias.getPreferencias();
			Hashtable ht = new Hashtable();
			ht.put(Context.INITIAL_CONTEXT_FACTORY,
					"weblogic.jndi.WLInitialContextFactory");
			Context ctx = new InitialContext(ht);
			javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup(_pr.getDataSource());
			
			conn = ds.getConnection();

			cs = util.custodiaficmultas(conn, nombreArchivo, archivo, codUsuario, limite);

			// Liberamos la referencia al objeto de petición, puede ser muy
			// grande y no sabemos
			// cuanto tiempo tarda la llamada en base de datos, de esta forma el
			// recolector
			// puede liberar su memoria inmediatamente.
			if (cs == null) {
				error.value = "No se ha podido montar la llamada";
				return;
			}
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);

			cs.execute();
			conn.commit();
			conn.setAutoCommit(autoCommit);
			Document xml = convertStringToDocument(cs.getString(1));

			NodeList errNodes = xml.getElementsByTagName("Error");
			if (errNodes.getLength() > 0) {
					error.value = errNodes.item(0).getTextContent();
			} else { 
				archivoRes.value = cs.getBytes(5);
				NodeList nombreFic = xml.getElementsByTagName("nombrearchivo");
				if (nombreFic.getLength() > 0) {
						nomArchivo.value = nombreFic.item(0).getTextContent();
				}
			}

		} catch (SQLException e) {
			String err = "";
			try {
				conn.rollback();
			} catch (java.sql.SQLException ex) {
				err = "No se ha podido realizar el rollback: "
						+ ex.getMessage();
			}

			err += e.getMessage() + e.toString();
			error.value = err;
			return;

		} catch (Exception e) {
			String err = "";
			try {
				conn.rollback();
			} catch (java.sql.SQLException ex) {
				err = "No se ha podido realizar el rollback: "
						+ ex.getMessage();
			}

			err += e.getMessage() + e.toString();
			error.value = err;
			return;
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (Exception e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}

		}
	}

	
}
