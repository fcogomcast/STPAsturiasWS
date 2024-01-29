package es.tributasenasturias.services.ws.archivodigital.bd;

import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Random;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.Holder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import es.tributasenasturias.services.ws.archivodigital.ListaArchivosType;
/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
import es.tributasenasturias.services.ws.archivodigital.utils.Base64;
/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
import es.tributasenasturias.services.ws.archivodigital.utils.TripleDes;
import es.tributasenasturias.services.ws.archivodigital.utils.UtilsDB2;
import es.tributasenasturias.services.ws.archivodigital.utils.XMLDOMUtils;
import es.tributasenasturias.services.ws.archivodigital.utils.Preferencias.Preferencias;


public class Datos {

	public Datos() {
	}

	 @Deprecated
	 public String GenerarCSV(String hash, String Usuario, String BaseCSV) throws Exception
     {
         String clave = "fa43fet4tdgeq";
         Random rn = new Random();
         BaseCSV = Usuario + Calendar.getInstance().getTime() + rn.nextInt(); 
         hash = BaseCSV + hash;

         String res = new TripleDes().encrypt(hash, clave);
         return res.substring(res.length() - 16, res.length());                     // Devolvemos las 16 últimas posiciones
     }
	 
	 @SuppressWarnings("unchecked")
	public String ExisteCsv(String csv){
		 
		 UtilsDB2 util = new UtilsDB2();
		 	CallableStatement cs = null;
			Connection conn = null;
			boolean autoCommit;

			try {
				Preferencias _pr=Preferencias.getPreferencias();
				Hashtable ht = new Hashtable();
				ht.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
				//ht.put( Context.PROVIDER_URL,_pr.getProviderURL());
				Context ctx = new InitialContext(ht);
				
				javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup(_pr.getDataSource());
				conn = ds.getConnection();

				cs = util.existeCSV(conn, csv);
				autoCommit = conn.getAutoCommit();
				conn.setAutoCommit(false);

				cs.execute();
				conn.commit();
				conn.setAutoCommit(autoCommit);
				return cs.getString(1);
				
			} catch (SQLException e) {
				String err = "";
				try {
					conn.rollback();
				} catch (java.sql.SQLException ex) {
					err = "No se ha podido realizar el rollback: "
							+ ex.getMessage();
				}

				err += e.getMessage() + e.toString();
				return "S";

			} catch (Exception e) {
				String err = "";
				try {
					conn.rollback();
				} catch (java.sql.SQLException ex) {
					err = "No se ha podido realizar el rollback: "
							+ ex.getMessage();
				}

				err += e.getMessage() + e.toString();
				return "S";
			} finally {				
				if (conn != null) {
					try {
						conn.close();
					} catch (Exception e) {
					}
				}

			}

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
	public void CustodiaArchivo(String codigoUsuario, String identificador,
			String tipoElemento, String nombreFichero, String comprimido,
			byte[] archivo, String hashArchivo, String metadatos,
			String firmadoPorCSV, String firmadoPorCertificadoInterno, String firmadoPorCertificadoExterno,Date fechaValidezCertificado,
			String csv, String baseCSV,
			/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
			String hashFirma, String nombreFirmante, String fechaFirma, String idTransac, String cargoUsua, String formatoFirma,
			/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
			Holder<Integer> idArchivo, Holder<String> error) {

		UtilsDB2 util = new UtilsDB2();
		Connection conn = null;
		CallableStatement cs = null;
		boolean autoCommit;

		try {
			Preferencias _pr=Preferencias.getPreferencias();
			Hashtable ht = new Hashtable();
			ht.put(Context.INITIAL_CONTEXT_FACTORY,
					"weblogic.jndi.WLInitialContextFactory");
			//ht.put( Context.PROVIDER_URL,_pr.getProviderURL());
			// ht.put( Context.PROVIDER_URL,"t3://localhost:7001");
			Context ctx = new InitialContext(ht);
			javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup(_pr.getDataSource());
			conn = ds.getConnection();

			cs = util.guardarArchivo(conn, codigoUsuario, identificador,
					tipoElemento, nombreFichero, comprimido, archivo,
					hashArchivo, firmadoPorCSV, firmadoPorCertificadoInterno, 
					firmadoPorCertificadoExterno, fechaValidezCertificado,csv, metadatos, baseCSV
					/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
					,hashFirma, nombreFirmante, fechaFirma, idTransac, cargoUsua, formatoFirma
					/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
					);

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
				idArchivo.value =  Integer.parseInt(xml.getElementsByTagName("IdArchivo").item(0).getTextContent());
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

	@SuppressWarnings("unchecked")
	public void obtieneArchivo(String codigoUsuario, int idArchivo,String obtenerSoloDatosArchivo, Holder<String> datosArchivo,
			Holder<byte[]> archivo, Holder<String> error) {

		UtilsDB2 util = new UtilsDB2();
		Connection conn = null;
		CallableStatement cs = null;
		boolean autoCommit;

		try {
			Preferencias _pr=Preferencias.getPreferencias();
			Hashtable ht = new Hashtable();
			ht.put(Context.INITIAL_CONTEXT_FACTORY,
					"weblogic.jndi.WLInitialContextFactory");
			//ht.put( Context.PROVIDER_URL,_pr.getProviderURL());

			Context ctx = new InitialContext(ht);
			//javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup("gtpaDS");
			javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup(_pr.getDataSource());
			
			conn = ds.getConnection();

			cs = util.obtenerArchivo(conn, idArchivo, codigoUsuario, obtenerSoloDatosArchivo);

			if (cs == null) {
				datosArchivo.value = "No se ha podido montar la llamada";
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
				datosArchivo.value = cs.getString(1);
				/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
				// Primero intentamos cargar la firma y comprobamos si existe, si no, cargamos el archivo sin firmar
				String firmaClob = cs.getString(5);
				if(firmaClob != null && firmaClob != "") {
					char[] charArray = firmaClob.toCharArray();
					archivo.value = Base64.decode(charArray);
				}
				else {
					/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
					archivo.value = cs.getBytes(4);
				/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
				}
				/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
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
		
		return;
	}
	/**
	 * Obtención del archivo mediante su CSV
	 * @param codigoUsuario Código de usuario que realiza la operación
	 * @param csv CSV por el que se buscará el archivo
	 * @param datosArchivo Datos del archivo encontrado, si hubiera
	 * @param archivo Contenido del archivo, si hubiera
	 * @param error Error producido al buscarlo
	 */
	@SuppressWarnings("unchecked")
	public void obtieneArchivoPorCSV(String codigoUsuario, String csv, Holder<String> datosArchivo,
			Holder<byte[]> archivo, Holder<String> error) {

		UtilsDB2 util = new UtilsDB2();
		Connection conn = null;
		CallableStatement cs = null;
		boolean autoCommit;

		try {
			Preferencias _pr=Preferencias.getPreferencias();
			Hashtable ht = new Hashtable();
			ht.put(Context.INITIAL_CONTEXT_FACTORY,
					"weblogic.jndi.WLInitialContextFactory");
			//ht.put( Context.PROVIDER_URL,_pr.getProviderURL());

			Context ctx = new InitialContext(ht);
			//javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup("gtpaDS");
			javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup(_pr.getDataSource());
			
			conn = ds.getConnection();

			cs = util.obtenerArchivoPorCSV(conn, csv, codigoUsuario);

			if (cs == null) {
				datosArchivo.value = "No se ha podido montar la llamada";
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
				datosArchivo.value = cs.getString(1);
				/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
				// Primero intentamos cargar la firma y comprobamos si existe, si no, cargamos el archivo sin firmar
				String firmaClob = cs.getString(4);
				if(firmaClob != null && firmaClob != "") {
					char[] charArray = firmaClob.toCharArray();
					archivo.value = Base64.decode(charArray);
				}
				else {
					/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
					archivo.value = cs.getBytes(3);
				/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
				}
				/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
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
		
		return;
	}
	
	/**
	 * Obtención de la lista de archivos custodiados que coincidan con un hash determinado.
	 * @param codigoUsuario Código de usuario que realiza la operación
	 * @param hash Hash por el que se buscará el archivo
	 * @param datosArchivo Datos del archivo encontrado, si hubiera
	 * @param archivo Contenido del archivo, si hubiera
	 * @param error Error producido al buscarlo
	 */
	@SuppressWarnings("unchecked")
	public void existeArchivoHash(String codigoUsuario, String hash, Holder<String> existeArchivo,
			Holder<ListaArchivosType> listaArchivos, Holder<String> error) {

		UtilsDB2 util = new UtilsDB2();
		Connection conn = null;
		CallableStatement cs = null;
		boolean autoCommit;

		try {
			Preferencias _pr=Preferencias.getPreferencias();
			Hashtable ht = new Hashtable();
			ht.put(Context.INITIAL_CONTEXT_FACTORY,
					"weblogic.jndi.WLInitialContextFactory");
			//ht.put( Context.PROVIDER_URL,_pr.getProviderURL());

			Context ctx = new InitialContext(ht);
			//javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup("gtpaDS");
			javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup(_pr.getDataSource());
			
			conn = ds.getConnection();

			cs = util.obtenerArchivosPorHash(conn, hash, codigoUsuario);

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
			if (xml==null) {
				existeArchivo.value="N";
				return;
			}
			NodeList errNodes = xml.getElementsByTagName("Error");
			if (errNodes.getLength() > 0) {
					error.value = errNodes.item(0).getTextContent();
			} else { 
				listaArchivos.value= new ListaArchivosType();
				//Tratamos el XML de resultado
				NodeList archivos= xml.getElementsByTagName("IDARCHIVO");
				if (archivos.getLength()==0) {
					existeArchivo.value="N";
				} else
				{
					existeArchivo.value="S";
					for (int i=0;i<archivos.getLength();i++){
						String idArchivo= XMLDOMUtils.getNodeText(archivos.item(i));
						listaArchivos.value.getIdArchivo().add(idArchivo);
					}
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
		
		return;
	}
	
	
	/* INIPETITRIBUTAS-2 ENAVARRO ** 04/09/2019 ** Custodia */
	@SuppressWarnings("unchecked")
	public void CustodiaFirma(String codigoUsuario, int idadar, String hashFirma, String nombreFirmante, String fechaFirma, String idTransac,
			/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
    		/* String justificanteFirma, String cargoUsua, String csv, String formatoFirma, String metadatosFirma, String base, String tipoFirma, */
    		String cargoUsua, String csv, String formatoFirma, String metadatosFirma, String base, String tipoFirma,
    		/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
    		String idFirmaExt, String fechaFirmaHuella, Holder<String> datosArchivo, Holder<String> error) {

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

			cs = util.guardarFirma(conn, codigoUsuario, idadar,
					hashFirma, nombreFirmante, fechaFirma, idTransac,
					/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
					/* justificanteFirma, cargoUsua, csv, formatoFirma, */
					cargoUsua, csv, formatoFirma,
					/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
					metadatosFirma, base, tipoFirma, idFirmaExt, fechaFirmaHuella);

			if (cs == null) {
				datosArchivo.value = "No se ha podido montar la llamada";
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
				datosArchivo.value = cs.getString(1);
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
	/* FINPETITRIBUTAS-2 ENAVARRO ** 04/09/2019 ** Custodia */	
	/* INIPETITRIBUTAS-88 MCMARCO ** 28/04/2020 ** Formato XML del expediente electrónico */	
	@SuppressWarnings("unchecked")
	public void GuardarMetadatos(String codigoUsuario, int idadar, String metadatos, Holder<String> datosArchivo, Holder<String> error) {

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

			cs = util.guardarMetadatos(conn, codigoUsuario, idadar,metadatos);

			if (cs == null) {
				datosArchivo.value = "No se ha podido montar la llamada";
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
				datosArchivo.value = cs.getString(1);
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
	/* FINPETITRIBUTAS-88 MCMARCO ** 28/04/2020 ** Formato XML del expediente electrónico */
	/* INIPETITRIBUTAS-26 ENAVARRO ** 17/07/2020 ** Firma en tableta */
	@SuppressWarnings("unchecked")
	public void vaciarFirmas(int idArchivo, Holder<String> datosArchivo, Holder<String> error) {

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

			cs = util.vaciarFirmas(conn, idArchivo);

			if (cs == null) {
				datosArchivo.value = "No se ha podido montar la llamada";
				return;
			}
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);

			cs.execute();
			conn.commit();
			conn.setAutoCommit(autoCommit);
			
			datosArchivo.value = cs.getString(1);
			String resultadoUp = (cs.getString(1)).toUpperCase();
			if(resultadoUp != "OK") {
				if(resultadoUp.length() != 2 || resultadoUp.charAt(0) != 'O' || resultadoUp.charAt(1) != 'K') {
					error.value = "Ha ocurrido un fallo inesperado en el PL";
				}
				else {
					error.value = "";
				}
			}
			else {
				error.value = "";
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
		
		return;
	}
	/* FINPETITRIBUTAS-26 ENAVARRO ** 17/07/2020 ** Firma en tableta */
	
	// CRUBENCVS 41435. 
	@SuppressWarnings("unchecked")
	public void CustodiaVersion(String codigoUsuario, 
								String identificador,
								String tipoElemento, 
								String nombreFichero, 
								String comprimido,
								byte[] archivo, 
								String hashArchivo, 
								String metadatos,
								String firmadoPorCSV, 
								String firmadoPorCertificadoInterno, 
								String firmadoPorCertificadoExterno,
								Date fechaValidezCertificado,
								String csv, 
								String baseCSV,
								/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
								String hashFirma, 
								String nombreFirmante,
								String fechaFirma,
								String idTransac, 
								String cargoUsua, 
								String formatoFirma,
								String idArchivoAnterior,
								/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
								Holder<Integer> idArchivo, 
								Holder<String> error) {

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

			cs = util.custodiarVersion(conn, 
									  codigoUsuario, 
									  identificador,
									  tipoElemento, 
									  nombreFichero, 
									  comprimido, 
									  archivo,
									  hashArchivo, 
									  firmadoPorCSV, 
									  firmadoPorCertificadoInterno, 
									  firmadoPorCertificadoExterno, 
									  fechaValidezCertificado,
									  csv, 
									  metadatos, 
									  baseCSV
									  /* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
									  ,hashFirma, 
									  nombreFirmante, 
									  fechaFirma, 
									  idTransac, 
									  cargoUsua,
									  formatoFirma,
									  /* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
									  idArchivoAnterior
					);

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
				idArchivo.value =  Integer.parseInt(xml.getElementsByTagName("IdArchivo").item(0).getTextContent());
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
