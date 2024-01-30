package es.tributasenasturias.docs;

import java.io.OutputStream;
import java.rmi.RemoteException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.tributasenasturias.GenerarDocs600ANCERTutils.Logger;
import es.tributasenasturias.GenerarDocs600ANCERTutils.Preferencias;
import es.tributasenasturias.GenerarDocs600ANCERTutils.Utils;
import es.tributasenasturias.documentos.DatosSalidaImpresa;
import es.tributasenasturias.documentos.util.ConversorParametrosLanzador;
import es.tributasenasturias.documentos.util.NumberUtil;
import es.tributasenasturias.documentos.util.SHAUtils;
import es.tributasenasturias.documentos.util.XMLUtils;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPL;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLService;

public class JustificantePresentacion extends PdfBase {

	private String numeroAutoliquidacion = new String();
	private String codVerificacion = new String();
	private String docCumplimentado = new String();
	static ConversorParametrosLanzador cpl;

	private Preferencias pref = new Preferencias();

	public JustificantePresentacion(String p_numAutoliq) {

		try {
			pref.CargarPreferencias();
			Session.put("cgestor", "");
			plantilla = "recursos//impresos//xml//justificantePresentacion.xml";
			this.numeroAutoliquidacion = p_numAutoliq;
		} catch (Exception e) {
			Logger
					.error("Error al cargar preferencias y plantailla al dar de alta el documento. "
							+ e.getMessage());
		}

	}

	public String getPlantilla() {
		return plantilla;
	}

	public void compila(String id, String xml, String xsl, OutputStream output)
			throws RemoteException {
		try {
			justificantePresentacion(id, xml, xsl, output);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("static-access")
	public void justificantePresentacion(String id, String xml, String xsl,
			OutputStream output) {

		DatosSalidaImpresa s = new DatosSalidaImpresa(xml, xsl, output);
		// llamar al servicio que obtiene los datos a mostrar.
		recuperarDatosJustificantePr();
		System.out.println(cpl.getResultado());
		if (!cpl.getResultado().equals(null)) {
			try {
				Document docRespuesta = (Document) XMLUtils.compilaXMLObject(
						cpl.getResultado(), null);
				@SuppressWarnings("unused")
				String modelo = this.numeroAutoliquidacion.substring(0, 3);

				Element[] rsCanu = XMLUtils.selectNodes(docRespuesta,
						"//estruc[@nombre='CANU_CADENAS_NUMEROS']/fila");

				// Oficina de alta
				s.Campo("Texto103_2", XMLUtils.selectSingleNode(rsCanu[0],
						"STRING2_CANU").getTextContent());
				// Organismo
				s.Campo("Texto101_2", XMLUtils.selectSingleNode(rsCanu[0],
						"STRING4_CANU").getTextContent());
				// Nº Expediente
				s.Campo("Texto70_2", XMLUtils.selectSingleNode(rsCanu[0],
						"STRING1_CANU").getTextContent());
				// Fecha de alta
				s.Campo("Texto67_2", XMLUtils.selectSingleNode(rsCanu[0],
						"FECHA1_CANU").getTextContent());
				// Oficina de destino
				s.Campo("Texto10_2", XMLUtils.selectSingleNode(rsCanu[0],
						"STRING3_CANU").getTextContent());
				// Hora de alta
				s.Campo("Texto160_2", XMLUtils.selectSingleNode(rsCanu[0],
						"FECHA2_CANU").getTextContent());
				// Fecha de presentación
				s.Campo("tFechaPresent_2", XMLUtils.selectSingleNode(rsCanu[0],
						"FECHA1_CANU").getTextContent());

				// Tipo/subtipo de expediente
				s.Campo("Texto4_2", XMLUtils.selectSingleNode(rsCanu[0],
						"STRING5_CANU").getTextContent());
				s.Campo("Texto6_2", XMLUtils.selectSingleNode(rsCanu[1],
						"STRING1_CANU").getTextContent());

				s.Campo("tnumDOCORI_2", XMLUtils.selectSingleNode(rsCanu[1],
						"STRING2_CANU").getTextContent());
				s.Campo("tnombrenotDOCORI_2", XMLUtils.selectSingleNode(
						rsCanu[1], "STRING3_CANU").getTextContent());
				s.Campo("ttipoDOCORI_2", XMLUtils.selectSingleNode(rsCanu[1],
						"STRING5_CANU").getTextContent());
				s.Campo("tfechaprotDOCORI_2", XMLUtils.selectSingleNode(
						rsCanu[1], "FECHA1_CANU").getTextContent());

				s.Campo("concepto", XMLUtils.selectSingleNode(rsCanu[3],
						"STRING1_CANU").getTextContent());

				Element[] rsPedb = XMLUtils.selectNodes(docRespuesta,
						"//estruc[@nombre='PEDB_PERSONA_DATOS_BASICOS']/fila");
				for (int i = 0; i < rsPedb.length; i++) {
					String persona = new String();
					persona = XMLUtils.selectSingleNode(rsPedb[i], "AEAT_PEDB")
							.getTextContent();
					// System.out.println(persona+"--"+modelo);
					if (persona.equalsIgnoreCase("1")) {
						s.Campo("Texto20_2", XMLUtils.selectSingleNode(
								rsPedb[i], "NOMBRE_PEDB").getTextContent());
						s.Campo("Texto26_2", XMLUtils.selectSingleNode(
								rsPedb[i], "NIF_PEDB").getTextContent());
						s.Campo("Texto29_2", XMLUtils.selectSingleNode(
								rsPedb[i], "TELEFONO_PEDB").getTextContent());
						s.Campo("Texto23_2", XMLUtils.selectSingleNode(
								rsPedb[i], "SIGLA_PEDB").getTextContent()
								+ " "
								+ XMLUtils.selectSingleNode(rsPedb[i],
										"CALLE_PEDB").getTextContent()
								+ ". "
								+ XMLUtils.selectSingleNode(rsPedb[i],
										"CP_PEDB").getTextContent()
								+ " - "
								+ XMLUtils.selectSingleNode(rsPedb[i],
										"POBLACION_PEDB").getTextContent()
								+ ", "
								+ XMLUtils.selectSingleNode(rsPedb[i],
										"PROVINCIA_PEDB").getTextContent()
								+ ". ");
					}
					if (persona.equalsIgnoreCase("2")) {

						s.Campo("tetiSP", "SUJETO PASIVO");
						s.Campo("tnomSP_2", XMLUtils.selectSingleNode(
								rsPedb[i], "NOMBRE_PEDB").getTextContent());
						s.Campo("tnifSP_2", XMLUtils.selectSingleNode(
								rsPedb[i], "NIF_PEDB").getTextContent());
					}
				}

				@SuppressWarnings("unused")
				String tipo = new String();
				if (XMLUtils.selectSingleNode(rsCanu[2], "STRING2_CANU")
						.getTextContent().equalsIgnoreCase("S")) {
					tipo = "S";
				}

				@SuppressWarnings("unused")
				String totalIngresado = new String(XMLUtils.selectSingleNode(
						rsCanu[2], "NUME1_CANU").getTextContent());

				s.Campo("autoliq1", this.numeroAutoliquidacion);

				s.Campo("tipoautoliq1", NumberUtil
						.getImporteFormateado(XMLUtils.selectSingleNode(
								rsCanu[2], "NUME1_CANU").getTextContent())
						+ " €");

				
				
				s.Campo("tipoautoliq1", XMLUtils.selectSingleNode(
						rsCanu[2], "STRING3_CANU").getTextContent());
				
				
				String tipoSujecion =XMLUtils.selectSingleNode(rsCanu[2], "STRING2_CANU").getTextContent();				
				
				if (tipoSujecion.equalsIgnoreCase("S")) {
						//if (!totalIngresado.equalsIgnoreCase("0")) {										
					s.Campo("tipoautoliq1", NumberUtil
							.getImporteFormateado(XMLUtils.selectSingleNode(
									rsCanu[2], "NUME1_CANU").getTextContent())
							+ " €");								
					
					@SuppressWarnings("unused")
					String fecha = new String(XMLUtils.selectSingleNode(
							rsCanu[2], "FECHA1_CANU").getTextContent());
				} else {

					/*if ((XMLUtils.selectSingleNode(rsCanu[2], "STRING3_CANU")
							.getTextContent().equalsIgnoreCase("Sujeto"))
							&& ((modelo.equalsIgnoreCase("651")) || (modelo
									.equalsIgnoreCase("600"))))
						s.Campo("tipoautoliq1", "No sujeto");
					else*/											
						s.Campo("tipoautoliq1", XMLUtils.selectSingleNode(
								rsCanu[2], "STRING3_CANU").getTextContent());
				}
				

				Element[] rsMemo = XMLUtils.selectNodes(docRespuesta,
						"//estruc[@nombre='MEMO_MEMO']/fila");

				String textoTitulo = new String(XMLUtils.selectSingleNode(
						rsMemo[9], "STRING_MEMO").getTextContent());
				// String textoTitulo = new String(rsMemo.length+"");

				String texto = new String(XMLUtils.selectSingleNode(rsMemo[10],
						"STRING_MEMO").getTextContent()
						+ "\r\r");

				texto = texto
						+ XMLUtils.selectSingleNode(rsMemo[11], "STRING_MEMO")
								.getTextContent() + "\r\r";
				
				
				s.Campo("titulopagina2", textoTitulo);

				s.Campo("textoPpal", texto.replace("¿", "€"));

				s.Campo("jefe", "JEFE DEL ÁREA DE GESTIÓN TRIBUTARIA");
				s.Campo("firma", "Carlos Franco García");
				this.codVerificacion = codigoVerificacion("C"
						+ this.numeroAutoliquidacion
						+ XMLUtils.selectSingleNode(rsPedb[0], "NIF_PEDB")
								.getTextContent());
				s.Campo("TextoVeri", "C" + this.numeroAutoliquidacion + "-"
						+ this.codVerificacion);

			} catch (Exception e) {
				System.out
						.println("Error al componer el xml para pintar el pdf del justificante de presentacion: "
								+ e.getMessage());
			}
		}
		s.Mostrar();
		this.docCumplimentado = Utils.DOM2String(s.getXmlDatos());
	}

	public String getDocXml() {
		return this.docCumplimentado;
	}

	public String getCodVerificacion() {
		return this.codVerificacion;
	}

	private String codigoVerificacion(String valor) {
		try {
			String resultado = SHAUtils.hex_hmac_sha1("clave               ",
					valor);
			return resultado.substring(resultado.length() - 16, resultado
					.length());
		} catch (Exception e) {
			System.out
					.println("Error al obtener el codigo de verificacion seguro: "
							+ e.getMessage());
			return null;
		}
	}

	private void recuperarDatosJustificantePr() {
		try {
			// llamar al servicio PXLanzador para ejecutar el procedimiento
			// almacenado de alta de expediente
			// Se prepara la llamada al procedimiento almacenado
			cpl = new ConversorParametrosLanzador();

			cpl
					.setProcedimientoAlmacenado("PROGRAMAS_AYUDA4.obtenerDatosInformes");
			// conexion
			cpl.setParametro("1", ConversorParametrosLanzador.TIPOS.Integer);
			// peticion
			cpl.setParametro("1", ConversorParametrosLanzador.TIPOS.Integer);
			// usuario
			cpl.setParametro("USU_WEB_SAC",ConversorParametrosLanzador.TIPOS.String);
			// organismo
			cpl.setParametro("33", ConversorParametrosLanzador.TIPOS.Integer);
			// codTactInforme
			cpl.setParametro("NA", ConversorParametrosLanzador.TIPOS.String);
			// numAutoliquidacion
			cpl.setParametro(this.numeroAutoliquidacion, ConversorParametrosLanzador.TIPOS.String);
			// conexion oracle
			cpl.setParametro("P", ConversorParametrosLanzador.TIPOS.String);

			// Logger.info("antes de crear objeto pA");
			LanzaPLService lanzaderaWS = new LanzaPLService();
			LanzaPL lanzaderaPort;
			lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();

			// enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
			// Cambiamos el endpoint
			bpr.getRequestContext().put(
					javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
					pref.getEndpointLanzador());

			String respuesta = new String();
			try {
				respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl
						.Codifica(), "", "", "", "");
				cpl.setResultado(respuesta);
			} catch (Exception ex) {
				System.out
						.println("Error en lanzadera al recuperar datos del Justificante Presentacion: "
								+ ex.getMessage());
			}
		} catch (Exception e) {
			System.out
					.println("Excepcion generica al recuperar los datos del documento de comparecencia: "
							+ e.getMessage());
		}
	}

}
