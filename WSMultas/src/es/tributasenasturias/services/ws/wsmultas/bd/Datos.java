package es.tributasenasturias.services.ws.wsmultas.bd;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Formatter;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.Holder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import sun.misc.BASE64Decoder;

import stpa.services.LanzaPL;
import stpa.services.LanzaPLService;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.ArchivoDigital;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.ArchivoDigital_Service;
import es.tributasenasturias.services.ws.wsmultas.CadenaSN;
import es.tributasenasturias.services.ws.wsmultas.IdTamuType;
import es.tributasenasturias.services.ws.wsmultas.ImagenesType;
import es.tributasenasturias.services.ws.wsmultas.PropiedadesType;
import es.tributasenasturias.services.ws.wsmultas.PropiedadType;
import es.tributasenasturias.services.ws.wsmultas.utils.ConversorParametrosLanzador;
import es.tributasenasturias.services.ws.wsmultas.utils.Respuesta;
import es.tributasenasturias.services.ws.wsmultas.utils.Log.Logger;
import es.tributasenasturias.services.ws.wsmultas.utils.Preferencias.Preferencias;
import es.tributasenasturias.services.ws.wsmultas.utils.UtilsDB2;


public class Datos {
	private Preferencias preferencias;
	private Logger logger = null;
	//private TLanzador lanzadorWS = null;
	private ConversorParametrosLanzador cpl;

	public <T> T nvl(T arg0, T arg1) {
	    return (arg0 == null)?arg1:arg0;
	}
	
	private static String byteToHex(final byte[] hash)
	{
	    Formatter formatter = new Formatter();
	    for (byte b : hash)
	    {
	        formatter.format("%02x", b);
	    }
	    String result = formatter.toString();
	    formatter.close();
	    return result;
	}
	
	private static String getHashCode(String cadena)
	{
	    String sha1 = "";
	    try
	    {
	        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	        crypt.reset();
	        crypt.update(cadena.getBytes("UTF-8"));
	        sha1 = byteToHex(crypt.digest());
	    }
	    catch(NoSuchAlgorithmException e)
	    {
	        e.printStackTrace();
	    }
	    catch(UnsupportedEncodingException e)
	    {
	        e.printStackTrace();
	    }
	    return sha1;
	}

	public Datos() {

		logger = new Logger();
		try {
			preferencias = Preferencias.getPreferencias();
		} catch (Exception ex) {
			logger.error("En constructor de clase Datos: " + ex.getMessage());
		}
	}

	public Respuesta AltaDocumentoMultas(String codigoUsuario, String nombreFichero, String imagenB64, String idEper)
    {          
		Respuesta resultado = new Respuesta();
        // 1. Custodiamos el documento y obtenemos un IDADAR                         
        try
        {
        		Holder<Integer> idAdar = new Holder<Integer>();
        		Holder<String> csv = new Holder<String>();
        		Holder<String> error = new Holder<String>();
        		String metadatos = "";
        		String comprimir = "N";
        		String hashCode = getHashCode(imagenB64);
        		BASE64Decoder decoder = new BASE64Decoder();
        		byte[] imageByte = decoder.decodeBuffer(imagenB64);
        		
        		ArchivoDigital_Service ads = new ArchivoDigital_Service();
        		ArchivoDigital ad = ads.getArchivoDigitalSOAP();
        		ad.custodia(codigoUsuario, nombreFichero, "IMAG", nombreFichero+".png", comprimir, imageByte, hashCode, metadatos, idAdar, csv, error);
				
        		if (idAdar.value == null) idAdar.value = 0;
				if (idEper == null) idEper = "0";
				else
				{
					if (idEper.isEmpty()) idEper = "0";				
				}
				
				if (error.value != null)
				{
		            resultado.setCodresultado("88");
		            resultado.setResultado("Error al custodia imagen: " + error.value);
		            return resultado;
				}
                // 2. Si tenemos IDADAR lo asociamos con un expediente de multas
                if (idAdar.value != 0 && !idEper.equals("0"))
                {		
                	LanzaPLService lanzaderaWS = new LanzaPLService();
                	LanzaPL lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
                	javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
        			bpr.getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,preferencias.getEndPointISL());
        			
        			cpl = new ConversorParametrosLanzador();
        			cpl.setProcedimientoAlmacenado(preferencias.getNomProcedimientoAltaDocMulta());
        			cpl.setParametro("0", ConversorParametrosLanzador.TIPOS.Integer); // conexion
        			cpl.setParametro("0", ConversorParametrosLanzador.TIPOS.Integer); // peticiom
   					cpl.setParametro(codigoUsuario, ConversorParametrosLanzador.TIPOS.String); // usuario
        			cpl.setParametro("33", ConversorParametrosLanzador.TIPOS.Integer); // organismo
        			cpl.setParametro(idEper, ConversorParametrosLanzador.TIPOS.Integer); //ideper
        			cpl.setParametro(idAdar.value.toString(), ConversorParametrosLanzador.TIPOS.Integer); // idadar
        			cpl.setParametro(nombreFichero, ConversorParametrosLanzador.TIPOS.String); // nombredocu
        			cpl.setParametro("F", ConversorParametrosLanzador.TIPOS.String); // tipodocu: Foto

        			String resultadoEjecutarPL=lanzaderaPort.executePL(preferencias.getEsquemaBaseDatos(), cpl.Codifica(),preferencias.getIPLanzador(), "", "", "");
            		resultado = ParsearResultado(resultadoEjecutarPL);
            		if (resultado.getCodresultado().isEmpty()) resultado.setCodresultado(preferencias.getCodResultadoOK());
                }
                else
                {
                    resultado.setCodresultado("76");
                    resultado.setResultado("Error al custodia imagen");
                }
        }
        catch (Exception ex)
        {
            resultado.setCodresultado("77");
            resultado.setResultado("Error al custodia imagen: " + ex.getMessage());
        }

        return resultado;
    }       

	public Respuesta ejecutaAltaMulta(String udid, String suborganismo,
			String numeroAgenteAgmu, String nifPropietario,
			String nombreCompletoPropietario, String paisPropietario,
			String callePropietario, String poblacionPropietario,
			String provinciaPropietario, String distritoPostalPropietario,
			String nifInfractor, String nombreCompletoInfractor,
			String paisInfractor, String calleInfractor,
			String poblacionInfractor, String provinciaInfractor,
			String distritoPostalInfractor, String nifConductor,
			String nombreCompletoConductor, String paisConductor,
			String calleConductor, String poblacionConductor,
			String provinciaConductor, String distritoPostalConductor,
			String matricula, String marca, String modelo, String numBoletin,
			String numExpediente, String codigoArmu, String hechoDenunciado,
			String hechoDenunciadoCoof, String idTimu, String idCamu,
			String idClve, IdTamuType idTamu, CadenaSN viaPenal,
			CadenaSN retencion, Double velocidad, Double velocidadMaxima,
			Double velocidadCorregida, Integer dispositivoMedicion,
			XMLGregorianCalendar fechaInfraccion, String horaInfraccion,
			String nombreVia, String numVia, String ampliacionVia,
			Double latitud, Double longitud, String pagado, String formaPago,
			XMLGregorianCalendar fechaCobro, String importe,
			String justificante, String motivoAnulacion, PropiedadesType propiedades,
			ImagenesType imagenes, String idComa,
			CadenaSN modificadaDireccion) throws Exception{

		Respuesta resultadoConsulta = new Respuesta();
		try{
			String propiedadesName = "";
			String propiedadesValue = "";
			if (propiedades != null)
			{
				// Pasar propiedades a dos cadenas de nombres/valor separadas por comas
				for (PropiedadType propiedad : propiedades.getPropiedades())
				{
					if (propiedadesName.isEmpty()) propiedadesName = propiedad.getNombre();
					else propiedadesName = propiedadesName +","+propiedad.getNombre();
					if (propiedadesValue.isEmpty()) propiedadesValue = propiedad.getValor();
					else propiedadesValue = propiedadesValue +","+propiedad.getValor();	
				}
			}
			
			LanzaPLService lanzaderaWS = new LanzaPLService();
			LanzaPL lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
			bpr.getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,preferencias.getEndPointISL());

			cpl = new ConversorParametrosLanzador();
			cpl.setProcedimientoAlmacenado(preferencias.getNomProcedimientoAltaMulta());
			cpl.setParametro(udid, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(suborganismo, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(numeroAgenteAgmu, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(nifInfractor, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(nombreCompletoInfractor, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(paisInfractor, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(calleInfractor, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(poblacionInfractor, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(provinciaInfractor, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(distritoPostalInfractor, ConversorParametrosLanzador.TIPOS.String); // 10
			cpl.setParametro(nifPropietario, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(nombreCompletoPropietario, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(paisPropietario, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(callePropietario, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(poblacionPropietario, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(provinciaPropietario, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(distritoPostalPropietario, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(nifConductor, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(nombreCompletoConductor, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(paisConductor, ConversorParametrosLanzador.TIPOS.String); // 20
			cpl.setParametro(calleConductor, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(poblacionConductor, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(provinciaConductor, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(distritoPostalConductor, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(matricula, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(marca, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(modelo, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(numBoletin, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(numExpediente, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(codigoArmu, ConversorParametrosLanzador.TIPOS.String); // 30
			cpl.setParametro(hechoDenunciado, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(hechoDenunciadoCoof, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(idTimu, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(idCamu, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(idClve, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(idTamu.toString(), ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(fechaInfraccion.getDay()+"/"+fechaInfraccion.getMonth() +"/"+fechaInfraccion.getYear(),  ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(horaInfraccion.substring(0, 5), ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro("", ConversorParametrosLanzador.TIPOS.String);// codigo via1
			cpl.setParametro("", ConversorParametrosLanzador.TIPOS.String);// sigla via1 // 40
			cpl.setParametro(nombreVia, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(numVia, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(ampliacionVia, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(pagado, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(formaPago, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro((fechaCobro==null)? "":fechaCobro.getDay()+"/"+fechaCobro.getMonth()+"/"+fechaCobro.getYear(), ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro((importe == "0")? "" : importe, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(justificante, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(latitud.toString(), ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(longitud.toString(), ConversorParametrosLanzador.TIPOS.String); // 50
			cpl.setParametro(viaPenal.toString(), ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro((velocidad == 0) ? null : velocidad.toString(), ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro((velocidadMaxima == 0) ? null: velocidadMaxima.toString(), ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro((velocidadCorregida == 0) ? null : velocidadCorregida.toString(), ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro((dispositivoMedicion == 0) ? null : dispositivoMedicion.toString(), ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(retencion.toString(), ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(propiedadesName, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro(propiedadesValue, ConversorParametrosLanzador.TIPOS.String);			
			cpl.setParametro(idComa, ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro((modificadaDireccion == null) ? "N" : modificadaDireccion.toString(), ConversorParametrosLanzador.TIPOS.String); // 60
		
			String resultadoEjecutarPL=lanzaderaPort.executePL(preferencias.getEsquemaBaseDatos(), cpl.Codifica(),preferencias.getIPLanzador(), "", "", "");
			resultadoConsulta = ParsearResultado(resultadoEjecutarPL);        }
        catch (Exception ex)
        {
        	resultadoConsulta.setCodresultado("88");
        	resultadoConsulta.setResultado("Error alta multa: " + ex.getMessage());
        }

        return resultadoConsulta;
	}

	/**
	 * Establece los valores de retorno en función de los obtenidos
	 * @param resultado
	 * @return
	 */
	private Respuesta ParsearResultado(String resultado) {
		Respuesta resultadoParseado = new Respuesta();
		try {
			String codResult = GetValorNodo(resultado, "C0");
			String error = GetValorNodo(resultado, "error");
			if (codResult.equals(preferencias.getCodResultadoOK())) {
				resultadoParseado.setCodresultado(preferencias.getCodResultadoOK());
				String idEper = null;
				idEper = GetValorNodo(resultado, "N1");
				String numBoletin = GetValorNodo(resultado, "C1");
				String numExpediente = GetValorNodo(resultado, "C2");
				// Datos de la carta de pago generada
				String referenciaCobro = GetValorNodo(resultado, "C3");
				String fechaUltimoPago = GetValorNodo(resultado, "C4");
				String referenciaLarga = GetValorNodo(resultado, "C5");
				if (referenciaCobro == null) referenciaCobro = "";
				if (fechaUltimoPago == null) fechaUltimoPago = "";
				if (referenciaLarga == null) referenciaLarga = "";

				resultadoParseado.setIdeper(idEper);
				resultadoParseado.setNumerorege(numExpediente);
				resultadoParseado.setResultado(numBoletin);
			} else {
				if (error.isEmpty())
				{
					// No hay error, pero el PL no ha devuelto el codigo 0 o lo ha devuelto vacio (suponemos ejecucion correcta)
					resultadoParseado.setCodresultado((codResult.isEmpty() ? preferencias.getCodResultadoOK(): codResult));
					resultadoParseado.setResultado(GetValorNodo(resultado, "C1"));
				}
				else
				{
					// Hay un error de PL
					resultadoParseado.setCodresultado((codResult.isEmpty() ? "65": codResult));
					resultadoParseado.setResultado(error);
				}
			}
		} catch (Exception e) {
			resultadoParseado.setCodresultado("66");
			resultadoParseado.setResultado("Error: " + e.getMessage());
		}
		return resultadoParseado;
	}
	
	/**
	 * Obtiene el valor del nodoABuscar dentro del xmlParsear
	 * @param xmlParsear
	 * @param nodoABuscar
	 * @return
	 */
	private String GetValorNodo(String xmlParsear, String nodoABuscar) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlParsear));

			Document doc = db.parse(is);
			// Obtenemos el valor del nodo
			NodeList nodes = doc.getElementsByTagName(nodoABuscar);
			logger.debug("NumElementos " + nodoABuscar + ": "+ nodes.getLength());
			String resultado = "";
			if (nodes.getLength() > 0)
				// Sólo hay un nodo de ese tipo
				resultado = (nodes.item(0).getFirstChild().getNodeValue());
			else
				resultado = "";

			return resultado;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "";
		}
	}

	public void multasExternasObtenerTablas(int municipio, String tabla,
			Holder<String> fecha, Holder<String> error,
			Holder<String> ficheroB64) {

		UtilsDB2 util = new UtilsDB2();
		Connection conn = null;
		CallableStatement cs = null;
		boolean autoCommit;

		try {
			Preferencias _pr=Preferencias.getPreferencias();
			Hashtable<String, String> ht = new Hashtable<String,String>();
			ht.put(Context.INITIAL_CONTEXT_FACTORY,	"weblogic.jndi.WLInitialContextFactory");
			//ht.put( Context.PROVIDER_URL,_pr.getProviderURL());

			Context ctx = new InitialContext(ht);
			javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup(_pr.getDataSource());
			
			conn = ds.getConnection();

			cs = util.multasExternasObtenerTablas(conn, municipio, tabla, fecha);

			if (cs == null) {
				error.value = "No se ha podido montar la llamada";
				return;
			}
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);

			cs.execute();
			conn.commit();
			conn.setAutoCommit(autoCommit);
			
			String resultado = cs.getString(1);
			error.value = GetValorNodo(resultado, "Error");
			if (error.value.isEmpty())
			{
				fecha.value = GetValorNodo(resultado, "Fecha");
				Clob clob = cs.getClob(5);
				ficheroB64.value =  clob.getSubString(1, (int) clob.length());
			}
			else
			{
				fecha.value = "";
				ficheroB64.value = "";
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
}
