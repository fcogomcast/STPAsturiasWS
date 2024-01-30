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
import es.tributasenasturias.documentos.util.SHAUtils;
import es.tributasenasturias.documentos.util.XMLUtils;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPL;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLService;

public class Comparecencia extends PdfBase{

	private String numeroAutoliquidacion = new String();
	private String codVerificacion = new String();
	private String docCumplimentado = new String();
	static ConversorParametrosLanzador cpl;
	
	private Preferencias pref = new Preferencias();
	
	public Comparecencia(String p_numAutoliq) {
		try {
			pref.CargarPreferencias();
			Session.put("cgestor", "");
			plantilla = "recursos//impresos//xml//comparecenciaCedidos.xml";
			this.numeroAutoliquidacion = p_numAutoliq;						
		} catch (Exception e) {
			Logger.error("Error al cargar preferencias y plantilla al dar de alta el documento. "+e.getMessage());
		}
			
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void compila(String id, String xml, String xsl, OutputStream output) throws RemoteException {
		try {			
			comparecencia(id, xml, xsl, output);			
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
		
	}
	
	@SuppressWarnings("static-access")
	public void comparecencia(String id, String xml, String xsl, OutputStream output) {
		
		DatosSalidaImpresa s = new DatosSalidaImpresa(xml, xsl, output);
		// llamar al servicio que obtiene los datos a mostrar.
		
				
		recuperarDatosComparecencia();		
						
		if (!cpl.getResultado().equals(null)) {
			// una vez obtenidos los datos y comprobado que la respuesta no es nula
			// se debe inluir el xml con los datos en la base de datos zipped
			// y se pinta el pdf.
			try {
											
				Document docRespuesta = (Document) XMLUtils.compilaXMLObject(cpl.getResultado(), null);				

				String nifTr = new String();
				String nomTr = new String();
				
				Element[] rsPedb = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='PEDB_PERSONA_DATOS_BASICOS']/fila");
				for (int i=0; i<rsPedb.length; i++) {
					String persona = new String();
					persona = XMLUtils.selectSingleNode(rsPedb[i], "AEAT_PEDB").getTextContent();
					//System.out.println(persona+"--"+modelo);
					if (persona.equalsIgnoreCase("1")) {
						s.Campo("Texto20", XMLUtils.selectSingleNode(rsPedb[i], "NOMBRE_PEDB").getTextContent());
						s.Campo("Texto26", XMLUtils.selectSingleNode(rsPedb[i], "NIF_PEDB").getTextContent());
						s.Campo("Texto29", XMLUtils.selectSingleNode(rsPedb[i], "TELEFONO_PEDB").getTextContent());
						s.Campo("Texto23", XMLUtils.selectSingleNode(rsPedb[i], "SIGLA_PEDB").getTextContent()+" "
								+ XMLUtils.selectSingleNode(rsPedb[i], "CALLE_PEDB").getTextContent()+". "
								+ XMLUtils.selectSingleNode(rsPedb[i], "CP_PEDB").getTextContent()+" - "
								+ XMLUtils.selectSingleNode(rsPedb[i], "POBLACION_PEDB").getTextContent()+", "
								+ XMLUtils.selectSingleNode(rsPedb[i], "PROVINCIA_PEDB").getTextContent()+". ");
					}
					if (persona.equalsIgnoreCase("2")) {

						s.BorrarCampo("nomUsu");				
						s.BorrarCampo("idUsu");					
						s.BorrarCampo("fechaExti");
						s.BorrarCampo("tnomUsu");				
						s.BorrarCampo("tidUsu");					
						s.BorrarCampo("tfechaExti");
						
					    s.Campo("tnomSP",XMLUtils.selectSingleNode(rsPedb[i], "NOMBRE_PEDB").getTextContent());
					    s.Campo("tnifSP",XMLUtils.selectSingleNode(rsPedb[i], "NIF_PEDB").getTextContent());
					}
					if (persona.equalsIgnoreCase("3")) {
					   	nifTr = XMLUtils.selectSingleNode(rsPedb[i], "NIF_PEDB").getTextContent();
					   	nomTr = XMLUtils.selectSingleNode(rsPedb[i], "NOMBRE_PEDB").getTextContent();
					}
				}
				Element[] rsCanu = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='CANU_CADENAS_NUMEROS']/fila");

				s.Campo("NUMERO_NUME",XMLUtils.selectSingleNode(rsCanu[0], "STRING1_CANU").getTextContent());
				s.Campo("Texto103",XMLUtils.selectSingleNode(rsCanu[0], "STRING2_CANU").getTextContent());
				s.Campo("Texto10",XMLUtils.selectSingleNode(rsCanu[0], "STRING3_CANU").getTextContent());
				s.Campo("Texto101",XMLUtils.selectSingleNode(rsCanu[0], "STRING4_CANU").getTextContent());
				s.Campo("Texto4",XMLUtils.selectSingleNode(rsCanu[0], "STRING5_CANU").getTextContent());
				s.Campo("Texto67",XMLUtils.selectSingleNode(rsCanu[0], "FECHA1_CANU").getTextContent());
				s.Campo("fechaAlta",XMLUtils.selectSingleNode(rsCanu[0], "FECHA2_CANU").getTextContent());
				s.Campo("Texto160",XMLUtils.selectSingleNode(rsCanu[0], "FECHA3_CANU").getTextContent());
				
				s.Campo("Texto6",XMLUtils.selectSingleNode(rsCanu[1], "STRING1_CANU").getTextContent());
				s.Campo("tnumDOCORI",XMLUtils.selectSingleNode(rsCanu[1], "STRING2_CANU").getTextContent());
				s.Campo("tnombrenotDOCORI",XMLUtils.selectSingleNode(rsCanu[1], "STRING3_CANU").getTextContent());
				
				s.Campo("ttipoDOCORI",XMLUtils.selectSingleNode(rsCanu[1], "STRING5_CANU").getTextContent());
				s.Campo("tfechaprotDOCORI",XMLUtils.selectSingleNode(rsCanu[1], "FECHA1_CANU").getTextContent());
				

				s.Campo("etiFECHAFALL","TRANSMITENTE");
				s.Campo("fechaFALL","Apellidos y Nombre:");
				s.Campo("tFECHAFALL",nomTr);
				s.Campo("tipoDECLA","Id. Fiscal:");
				s.Campo("ttipoDECLA",nifTr);
				 
				s.Campo("Texto99",XMLUtils.selectSingleNode(rsCanu[2], "STRING1_CANU").getTextContent());
				

				s.BorrarCampo("cuadroCausantes");
				s.BorrarCampo("etiCaus");
				s.BorrarCampo("nombreCaus");
				s.BorrarCampo("nifCaus");			
				s.BorrarCampo("fechaCaus");
				s.BorrarCampo("tnombreCaus1");
				s.BorrarCampo("tfechaCaus1");			
				s.BorrarCampo("tnombreCaus2");
				s.BorrarCampo("tfechaCaus2");
				s.BorrarCampo("tnombreCaus3");
				s.BorrarCampo("tfechaCaus3");
				s.BorrarCampo("tnombreCaus4");
				s.BorrarCampo("tfechaCaus4");			
				s.BorrarCampo("tnombreCaus5");
				s.BorrarCampo("tfechaCaus5");

				s.BorrarCampo("etiDocVehi");
				s.BorrarCampo("cuadroDOCVehi");
				s.BorrarCampo("matriculaVehi");
				s.BorrarCampo("dmatriculaVehi");
				s.BorrarCampo("CETVehi");
				s.BorrarCampo("dCETVehi");
				
				Element[] rsMemo = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='MEMO_MEMO']/fila");
				s.Campo("etiTexto1",XMLUtils.selectSingleNode(rsMemo[0], "STRING_MEMO").getTextContent());
				s.Campo("etiTexto2","1.- "+XMLUtils.selectSingleNode(rsMemo[1], "STRING_MEMO").getTextContent());
				s.Campo("etiTexto3","2.- "+XMLUtils.selectSingleNode(rsMemo[2], "STRING_MEMO").getTextContent());
				String texto3 = XMLUtils.selectSingleNode(rsMemo[3], "STRING_MEMO").getTextContent();
				texto3 = texto3 + XMLUtils.selectSingleNode(rsMemo[4], "STRING_MEMO").getTextContent();
				s.Campo("etiTexto4","3.- "+texto3);
				s.Campo("etiTexto6"," - "+XMLUtils.selectSingleNode(rsMemo[5], "STRING_MEMO").getTextContent());
				s.Campo("etiTexto7"," - "+XMLUtils.selectSingleNode(rsMemo[6], "STRING_MEMO").getTextContent());
				s.Campo("etiTexto8",XMLUtils.selectSingleNode(rsMemo[7], "STRING_MEMO").getTextContent());
				
				String sNoAportados = new String();
				Element[] rsCaor = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='CAOR_CADENA_ORDEN']/fila");
				for (int i=0; i<rsCaor.length; i++) {
					sNoAportados = sNoAportados + XMLUtils.selectSingleNode(rsCaor[i], "STRING_CAOR").getTextContent()+"\r";
				}

				s.Campo("grande2",sNoAportados);
				
				String sAportados = new String();
				Element[] rsDimd = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='DIMD_DIRECC_MAS_DATOS']/fila");
				for (int i=0; i<rsDimd.length; i++) {
					sAportados = sAportados + XMLUtils.selectSingleNode(rsDimd[i], "PROV_DIMD").getTextContent()+"\r";
				}
				
	
				s.Campo("Etiqueta100", "Relaci�n con el sujeto pasivo:");
					
				
				s.Campo("grande1",sAportados);
		
				s.Campo("TextoAuto",this.numeroAutoliquidacion);
				//s.Campo("TextoVeri",tipoDocu+this.numeroAutoliquidacion+"-"+codigoVerificacion(tipoDocu+numAutoliquidacion+nif));
				this.codVerificacion = codigoVerificacion("J"+this.numeroAutoliquidacion+nifTr);
				s.Campo("TextoVeri","J"+this.numeroAutoliquidacion+"-"+this.codVerificacion);
				
				s.Mostrar();
				
				this.docCumplimentado = Utils.DOM2String(s.getXmlDatos());
				
				
				
			} catch (Exception e) {
				Logger.error("Error al incluir datos en el pdf: "+e.getMessage());
			}
		}
		
	}
	
	public String getDocXml() {
		return this.docCumplimentado;
	}
	
	public String getCodVerificacion () {
		return this.codVerificacion;
	}
	
	private String codigoVerificacion(String valor) {
		try {
			String resultado = SHAUtils.hex_hmac_sha1("clave               ", valor);
			return resultado.substring(resultado.length() -16, resultado.length());
		} catch (Exception e) {
			System.out.println("Error al obtener el codigo de verificacion seguro: "+e.getMessage());
			return null;
		}
		
	}
	
	private void recuperarDatosComparecencia () {
		try {

			// llamar al servicio PXLanzador para ejecutar el procedimiento almacenado de alta de expediente
			// Se prepara la llamada al procedimiento almacenado
			//cpl = new es.tributasenasturias.GenerarDocs600ANCERTutils.ConversorParametrosLanzador();
								
			cpl = new ConversorParametrosLanzador();
	        cpl.setProcedimientoAlmacenado("PROGRAMAS_AYUDA4.obtenerDatosInformes");
	        // conexion
	        
	        /*
	        cpl.setParametro("1",es.tributasenasturias.GenerarDocs600ANCERTutils.ConversorParametrosLanzador.TIPOS.Integer);
	        // peticion
	        cpl.setParametro("1",es.tributasenasturias.GenerarDocs600ANCERTutils.ConversorParametrosLanzador.TIPOS.Integer);
	        // usuario
	        cpl.setParametro("USU_WEB_SAC",es.tributasenasturias.GenerarDocs600ANCERTutils.ConversorParametrosLanzador.TIPOS.String);
	        // organismo
	        cpl.setParametro("33",es.tributasenasturias.GenerarDocs600ANCERTutils.ConversorParametrosLanzador.TIPOS.Integer);	        
	        // codTactInforme
	        cpl.setParametro("NA",es.tributasenasturias.GenerarDocs600ANCERTutils.ConversorParametrosLanzador.TIPOS.String);
	        // numAutoliquidacion
	        cpl.setParametro(this.numeroAutoliquidacion,es.tributasenasturias.GenerarDocs600ANCERTutils.ConversorParametrosLanzador.TIPOS.String);
	        // conexion oracle
	        cpl.setParametro("P",es.tributasenasturias.GenerarDocs600ANCERTutils.ConversorParametrosLanzador.TIPOS.String);            	        
	        */
	        
	        cpl.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
	        // peticion
	        cpl.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
	        // usuario
	        cpl.setParametro("USU_WEB_SAC",ConversorParametrosLanzador.TIPOS.String);
	        // organismo
	        cpl.setParametro("33",ConversorParametrosLanzador.TIPOS.Integer);	        
	        // codTactInforme
	        cpl.setParametro("NA",ConversorParametrosLanzador.TIPOS.String);
	        // numAutoliquidacion
	        cpl.setParametro(this.numeroAutoliquidacion,ConversorParametrosLanzador.TIPOS.String);
	        // conexion oracle
	        cpl.setParametro("P",ConversorParametrosLanzador.TIPOS.String);

	        LanzaPLService lanzaderaWS = new LanzaPLService();
	        LanzaPL lanzaderaPort;				      
	        
	        lanzaderaPort =lanzaderaWS.getLanzaPLSoapPort();
	        
			// enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort; 
			// Cambiamos el endpoint
			bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointLanzador()); 	        
	        String respuesta = new String();	        
	        
	        try {	        	
	        	respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");	        	
	        	cpl.setResultado(respuesta);	        	        	
	        }catch (Exception ex) {
	        		System.out.println("Error en lanzadera al recuperar datos de la comparecencia: "+ex.getMessage());
	        }
		} catch (Exception e) {
			System.out.println("Excepcion generica al recuperar los datos del documento de comparecencia: "+e.getMessage());
		}
	}
	
}
