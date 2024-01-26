package es.tributasenasturias.docs;

import java.io.OutputStream;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.tributasenasturias.documentopagoutils.DatosSalidaImpresa;
import es.tributasenasturias.documentopagoutils.Logger;
import es.tributasenasturias.documentopagoutils.Preferencias;
import es.tributasenasturias.documentos.util.ConversorParametrosLanzador;
import es.tributasenasturias.documentos.util.XMLUtils;
import es.tributasenasturias.soap.handler.HandlerUtil;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivo;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivoService;

public class JustificantePago extends PdfBase{

	private String ideper = new String();
	//private String codVerificacion = new String();
	private String docCumplimentado = new String();
	private String refCobro = new String();
	private String nifSP = "";
	
	static ConversorParametrosLanzador cpl;
	
	private Preferencias pref = new Preferencias();
	
	public JustificantePago(String idEper) {
		try {
			pref.CargarPreferencias();
			session.put("cgestor", "");
			plantilla = "recursos//impresos//xml//JustificanteCobroTributas.xml";
			this.ideper = idEper;						
		} catch (Exception e) {
			Logger.debug("Error al cargar preferencias y plantilla al dar de alta el documento. "+e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
			
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void compila(String id, String xml, String xsl, OutputStream output) throws RemoteException {
		try {			
			justificantepago(id, xml, xsl, output);			
		} catch (Exception e) {
			Logger.error(e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
		
	}
	
	@SuppressWarnings("static-access")
	public void justificantepago(String id, String xml, String xsl, OutputStream output) {
		
		DatosSalidaImpresa s = new DatosSalidaImpresa(xml, xsl, output);
		// llamar al servicio que obtiene los datos a mostrar.
		
		
		recuperarDatosPago(this.ideper);		
		
		if (!cpl.getResultado().equals(null)) {
			// una vez obtenidos los datos y comprobado que la respuesta no es nula
			// se debe inluir el xml con los datos en la base de datos zipped
			// y se pinta el pdf.
			try {
											
				Document docRespuesta = (Document) XMLUtils.compilaXMLObject(cpl.getResultado(), null);
				Element[] rsRica = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='RICA_RECIBO_INTER_CABECERA']/fila");
				
				Element[] rsCade = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='CADE_CADENA']/fila");
				
				String idioma=session.get("idioma");
				
				Logger.debug("Se comienza a componer el informe.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				
		    	s.Campo("salto",""); 	
		    //	s.Campo("Texto5",XMLUtils.selectSingleNode(rsRica[0],"FECHA_COBRO_RICA").getTextContent());
		    	
		    	
		    	
		    	
		    	
		     	String szFecha = XMLUtils.selectSingleNode(rsRica[0],"FECHA_COBRO_RICA").getTextContent();
		    	String formatoEntrada = "dd/MM/yyyy";				
				if(szFecha.length() == 8){
					formatoEntrada = "dd/MM/yy";
				}
	
				DateFormat df = new SimpleDateFormat(formatoEntrada); 
				Date fechaAux = df.parse(szFecha);  
				
				if (!fechaAux.equals(null))
				{
					SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
					
					s.Campo("Texto5",formateador.format(fechaAux));
				}
				
				
				Logger.debug("Se ha rellenado la fecha correctamente.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		    	
		    	s.Campo("Texto1","");
		    	
		        s.Campo("Texto2",XMLUtils.selectSingleNode(rsRica[0],"NOMBRE_CONTRIB_RICA").getTextContent());

		    	s.Campo("Texto6",XMLUtils.selectSingleNode(rsRica[0],"NIF_CONTRIB_RICA").getTextContent());
		    	this.nifSP = XMLUtils.selectSingleNode(rsRica[0],"NIF_CONTRIB_RICA").getTextContent();
		   // 	s.Campo("Texto20",s.IIf(s.Left(s.Right(XMLUtils.selectSingleNode(rsRica[0],"NUMERO_VALO").getTextContent(),7),1)=="L","Liquidación",s.IIf(s.Left(s.Right(XMLUtils.selectSingleNode(rsRica[0],"NUMERO_VALO").getTextContent(),7),1)=="A","Autoliquidación","Recibo" ) +XMLUtils.selectSingleNode(rsRica[0],"DESCRL_CONC").getTextContent()));
		  
		    	
		    	Logger.debug("Se recuperan los datos del contribuyente.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		    	
		    	s.Campo("Texto20",s.IIf(s.Left(s.Right(XMLUtils.selectSingleNode(rsRica[0],"NUMERO_VALO").getTextContent(),7),1)=="L",s.traduc("Liquidación",idioma)+" ",s.IIf(s.Left(s.Right(XMLUtils.selectSingleNode(rsRica[0],"NUMERO_VALO").getTextContent(),7),1)=="A",s.traduc("Autoliquidación",idioma)+" ",s.traduc("Recibo",idioma)+" ")+XMLUtils.selectSingleNode(rsRica[0],"DESCRL_CONC").getTextContent()));
		    	
		    	//s.Campo("Texto9",s.IIf(XMLUtils.selectSingleNode(rsCade[0],"STRING_CADE").getTextContent()=="A","del Ayuntamiento de" +" ",s.IIf(XMLUtils.selectSingleNode(rsCade[0],"STRING_CADE").getTextContent()=="C","de la ",s.IIf(XMLUtils.selectSingleNode(rsCade[0],"STRING_CADE").getTextContent()=="M","de la ","")))+XMLUtils.selectSingleNode(rsRica[0],"NOMBRE_ORGA").getTextContent()+s.IIf(XMLUtils.selectSingleNode(rsCade[0],"STRING_CADE").getTextContent()=="M","-"+XMLUtils.selectSingleNode(rsRica[0],"MUNICIPIO_VALO_RICA").getTextContent(),""));
		    	s.Campo("Texto9",s.IIf(XMLUtils.selectSingleNode(rsCade[0],"STRING_CADE").getTextContent()=="A",s.traduc("del Ayuntamiento de",idioma)+" ",s.IIf(XMLUtils.selectSingleNode(rsCade[0],"STRING_CADE").getTextContent()=="C","de la ",s.IIf(XMLUtils.selectSingleNode(rsCade[0],"STRING_CADE").getTextContent()=="M","de la ","")))+XMLUtils.selectSingleNode(rsRica[0],"NOMBRE_ORGA").getTextContent()+s.IIf(XMLUtils.selectSingleNode(rsCade[0],"STRING_CADE").getTextContent()=="M","-"+XMLUtils.selectSingleNode(rsRica[0],"MUNICIPIO_VALO_RICA").getTextContent(),""));
		  
		    	s.Campo("Texto22",s.IIf(XMLUtils.selectSingleNode(rsRica[0],"PERIODO_VALO").getTextContent()!="ANUAL",XMLUtils.selectSingleNode(rsRica[0],"PERIODO_VALO").getTextContent()+" del año"+" "+XMLUtils.selectSingleNode(rsRica[0],"ANYO_CARGO_CARG").getTextContent(),"Año"+" "+XMLUtils.selectSingleNode(rsRica[0],"ANYO_CARGO_CARG").getTextContent()));
	   
		    	
		    	s.Campo("Etiqueta174","Detalle "+s.IIf(s.Left(s.Right(XMLUtils.selectSingleNode(rsRica[0],"NUMERO_VALO").getTextContent(),7),1)=="L"," "+ "de la Liquidación"+" ",s.IIf(s.Left(s.Right(XMLUtils.selectSingleNode(rsRica[0],"NUMERO_VALO").getTextContent(),7),1)=="A"," "+ "de la Autoliquidación"," " +  "del Recibo")+" "));
		    	s.Campo("Texto229",XMLUtils.selectSingleNode(rsRica[0],"ACTIV_CONC").getTextContent());
		     	s.Campo("Texto27",XMLUtils.selectSingleNode(rsRica[0],"OBJ_TRIB_VALO").getTextContent());
		 
		     	s.Campo("Etiqueta177","Identificación" + s.IIf(s.Left(s.Right(XMLUtils.selectSingleNode(rsRica[0],"NUMERO_VALO").getTextContent(),7),1)=="L"," "+ "de la LID",s.IIf(s.Left(s.Right(XMLUtils.selectSingleNode(rsRica[0],"NUMERO_VALO").getTextContent(),7),1)=="A"," "+"de la Autoliq."," "+ "del Recibo")));
		    	 
		        s.Campo("Texto39",XMLUtils.selectSingleNode(rsRica[0],"NOMBRE_SP_VALO").getTextContent());
		        
		    	s.Campo("Texto38",XMLUtils.selectSingleNode(rsRica[0],"NUMERO_VALO").getTextContent());
		    	s.Campo("Texto28",XMLUtils.selectSingleNode(rsRica[0],"NUM_FIJO_VALO").getTextContent());
		    	
		    	Logger.debug("Se han recuperado los datos del valor.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		    	
		    	s.Campo("Texto145",XMLUtils.selectSingleNode(rsRica[0],"DETALLE_1_RICA").getTextContent());
		    	s.Campo("Texto146",XMLUtils.selectSingleNode(rsRica[0],"DETALLE_2_RICA").getTextContent());
		    	s.Campo("Texto147",XMLUtils.selectSingleNode(rsRica[0],"DETALLE_3_RICA").getTextContent());
		    	s.Campo("Texto148",XMLUtils.selectSingleNode(rsRica[0],"DETALLE_4_RICA").getTextContent());
		    	s.Campo("Texto149",XMLUtils.selectSingleNode(rsRica[0],"DETALLE_5_RICA").getTextContent());
		    	s.Campo("Texto150",XMLUtils.selectSingleNode(rsRica[0],"DETALLE_6_RICA").getTextContent());
		    	s.Campo("Texto151",XMLUtils.selectSingleNode(rsRica[0],"DETALLE_7_RICA").getTextContent());
		    	s.Campo("Texto152",XMLUtils.selectSingleNode(rsRica[0],"DETALLE_8_RICA").getTextContent());
		    	s.Campo("Texto153",XMLUtils.selectSingleNode(rsRica[0],"DETALLE_9_RICA").getTextContent());
		    	s.Campo("Texto154",XMLUtils.selectSingleNode(rsRica[0],"DETALLE_10_RICA").getTextContent());
		    	s.Campo("Texto155",XMLUtils.selectSingleNode(rsRica[0],"DETALLE_11_RICA").getTextContent());
		    	s.Campo("Texto156",XMLUtils.selectSingleNode(rsRica[0],"DETALLE_12_RICA").getTextContent());
		    	s.Campo("Texto157",XMLUtils.selectSingleNode(rsRica[0],"DETALLE_13_RICA").getTextContent());
		    	s.Campo("Texto158",XMLUtils.selectSingleNode(rsRica[0],"DETALLE_14_RICA").getTextContent());
		    	s.Campo("Texto159",XMLUtils.selectSingleNode(rsRica[0],"DETALLE_15_RICA").getTextContent());
		    	
		    	Logger.debug("Se ha recuperado el cuerpo del valor.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);

		    	if (s.parseInt(XMLUtils.selectSingleNode(rsRica[0],"DESCUENTO_RV").getTextContent())>0 )		
		    	{
		    		s.Campo("PRINCIPAL_D",s.toEuro(XMLUtils.selectSingleNode(rsRica[0],"PRINCIPAL_RV").getTextContent()));
		    		s.Campo("DESCUENTO_D",s.toEuro(XMLUtils.selectSingleNode(rsRica[0],"DESCUENTO_RV").getTextContent()));
		    		s.Campo("RECARGO_D",s.toEuro(XMLUtils.selectSingleNode(rsRica[0],"RECARGO_RV").getTextContent()));
		    		s.Campo("INTERESES_D",s.toEuro(XMLUtils.selectSingleNode(rsRica[0],"INTERESES_RV").getTextContent()));
		    		s.Campo("COSTAS_D",s.toEuro(XMLUtils.selectSingleNode(rsRica[0],"COSTAS_RV").getTextContent()));
		    		
		    		Number total = s.parseInt(XMLUtils.selectSingleNode(rsRica[0],"PRINCIPAL_RV").getTextContent()) +
						    		s.parseInt(XMLUtils.selectSingleNode(rsRica[0],"DESCUENTO_RV").getTextContent())+
						    		s.parseInt(XMLUtils.selectSingleNode(rsRica[0],"RECARGO_RV").getTextContent())+
						    		s.parseInt(XMLUtils.selectSingleNode(rsRica[0],"INTERESES_RV").getTextContent())+
						    		s.parseInt(XMLUtils.selectSingleNode(rsRica[0],"COSTAS_RV").getTextContent());
		    		
		    		s.Campo("Texto237_D", s.toEuro(total.toString()));
		    	
		    		Number total2 = s.parseInt(XMLUtils.selectSingleNode(rsRica[0],"importe_valo").getTextContent()) - 
					 				s.parseInt(XMLUtils.selectSingleNode(rsRica[0],"DESCUENTO_RV").getTextContent());
					 
		    		s.Campo("Texto185_D", s.toEuro(total2.toString()));		
		    	
		    		
		    	
		    	
		    		s.BorrarCampo("PRINCIPAL_RV");
		    		s.BorrarCampo("RECARGO_RV");
		    		s.BorrarCampo("INTERESES_RV");
		    		s.BorrarCampo("COSTAS_RV");
		    		s.BorrarCampo("Texto237");
		    		s.BorrarCampo("Texto185");		
		    		
		    		
		    		s.BorrarCampo("Línea243");
		    		s.BorrarCampo("Línea244");
		    		s.BorrarCampo("Línea246");
		    		s.BorrarCampo("Línea245");
		    		s.BorrarCampo("Línea247");
		    	
		    		
		    		s.BorrarCampo("Etiqueta232");
		    		s.BorrarCampo("Etiqueta230");
		    		s.BorrarCampo("Etiqueta234");
		    		s.BorrarCampo("Etiqueta235");
		    		s.BorrarCampo("Etiqueta236");
		    		s.BorrarCampo("Etiqueta231");		
		    	}	
		    	else
		    	{
		     		s.Campo("PRINCIPAL_RV",s.toEuro(XMLUtils.selectSingleNode(rsRica[0],"PRINCIPAL_RV").getTextContent()));
		    		s.Campo("RECARGO_RV",s.toEuro(XMLUtils.selectSingleNode(rsRica[0],"RECARGO_RV").getTextContent()));
		    		s.Campo("INTERESES_RV",s.toEuro(XMLUtils.selectSingleNode(rsRica[0],"INTERESES_RV").getTextContent()));
		    		s.Campo("COSTAS_RV",s.toEuro(XMLUtils.selectSingleNode(rsRica[0],"COSTAS_RV").getTextContent()));
		    		
		    		
		    		Number total = s.parseInt(XMLUtils.selectSingleNode(rsRica[0],"PRINCIPAL_RV").getTextContent()) +		    		
						    	   s.parseInt(XMLUtils.selectSingleNode(rsRica[0],"RECARGO_RV").getTextContent())+
						    	   s.parseInt(XMLUtils.selectSingleNode(rsRica[0],"INTERESES_RV").getTextContent())+
						    	   s.parseInt(XMLUtils.selectSingleNode(rsRica[0],"COSTAS_RV").getTextContent());
	
		    		s.Campo("Texto237", s.toEuro(total.toString()));
		    						   
		    		s.Campo("Texto185", s.toEuro(XMLUtils.selectSingleNode(rsRica[0],"IMPORTE_VALO").getTextContent()));
		    		
		    		
		    		s.BorrarCampo("PRINCIPAL_D");
		    		s.BorrarCampo("DESCUENTO_D");
		    		s.BorrarCampo("RECARGO_D");
		    		s.BorrarCampo("INTERESES_D");
		    		s.BorrarCampo("COSTAS_D");
		    		s.BorrarCampo("Texto237_D");
		    		s.BorrarCampo("Texto185_D");
		    		
		    		
		    		s.BorrarCampo("Línea249");
		    		s.BorrarCampo("Línea254");
		    		s.BorrarCampo("Línea251");
		    		s.BorrarCampo("Línea252");
		    		s.BorrarCampo("Línea253");
		    		s.BorrarCampo("Línea254");
		    		
		    		
		    		s.BorrarCampo("Etiqueta222");
		    		s.BorrarCampo("Etiqueta223");
		    		s.BorrarCampo("Etiqueta224");
		    		s.BorrarCampo("Etiqueta225");
		    		s.BorrarCampo("Etiqueta226");
		    		s.BorrarCampo("Etiqueta227");
		    		s.BorrarCampo("Etiqueta228");		
		    	}
		    	
		    	
		    	Logger.debug("Se ha rellenado los importes.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		    	
		    	s.Campo("Texto182",s.Formato(XMLUtils.selectSingleNode(rsRica[0],"REFERENCIA_RICA").getTextContent(),12));
		    	this.refCobro = s.Formato(XMLUtils.selectSingleNode(rsRica[0],"REFERENCIA_RICA").getTextContent(),12);
		    	s.Campo("IMPORTEPTERICA",s.toEuro(XMLUtils.selectSingleNode(rsRica[0],"IMPORTE_PTE_RICA").getTextContent()));
		        s.Campo("Texto190",s.toEuro(XMLUtils.selectSingleNode(rsRica[0],"IMPORTE_PTE_RICA").getTextContent()));
		       	s.Campo("Etiqueta192","Justificante de Pago - Ejemplar para el Contribuyente");
		    	
		       	Logger.debug("Se comienza con el pie del documento.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		  
		    	s.Campo("Texto120",XMLUtils.selectSingleNode(rsRica[0],"NOMBRE_CONTRIB_RICA").getTextContent());
		    			    	
		    	
		    	if (XMLUtils.selectSingleNode(rsRica[0],"CALLE_DIRE").getTextContent().length() > 52)
					s.Campo("Texto666", XMLUtils.selectSingleNode(rsRica[0],"CALLE_DIRE").getTextContent());
				else
					s.Campo("Texto219", XMLUtils.selectSingleNode(rsRica[0],"CALLE_DIRE").getTextContent());
		    	
									
				s.Campo("Texto217", s.IIf(s.validString(s.Trim(XMLUtils.selectSingleNode(rsRica[0],"CALLE_AMPLIACION_DIRE").getTextContent())), s.Trim(XMLUtils.selectSingleNode(rsRica[0],"CALLE_AMPLIACION_DIRE").getTextContent())
						+ "\n", " ")
						+ s.Trim(s.Formato(XMLUtils.selectSingleNode(rsRica[0],"DISTRITO_DIRE").getTextContent(), 5))
						+ " "
						+ s.Trim(XMLUtils.selectSingleNode(rsRica[0],"MUNICIPIO_RICA").getTextContent())
						+ s.IIf(s.validString(s.Trim(XMLUtils.selectSingleNode(rsRica[0],"CALLE_AMPLIACION_DIRE").getTextContent())), " ", "\n")
						+ s.Trim(XMLUtils.selectSingleNode(rsRica[0],"PROVINCIA_RICA").getTextContent()));
		    	
		 
		    	s.Campo("camporef",XMLUtils.selectSingleNode(rsRica[0],"NOMBRE_CONTRIB_RICA").getTextContent());
		    	s.Campo("camporef",s.Formato(XMLUtils.selectSingleNode(rsRica[0],"REFERENCIA_RICA").getTextContent(),12));
		    	
		    	s.Campo("CodBarras2", s.Right("000000000000" + s.Formato(XMLUtils.selectSingleNode(rsRica[0],"REFERENCIA_RICA").getTextContent()), 12));
		    	

		         s.Campo("Texto501",XMLUtils.selectSingleNode(rsRica[0],"NOMBRE_SP_VALO").getTextContent());
		    	 s.Campo("Texto502",XMLUtils.selectSingleNode(rsRica[0],"NIF_SP_VALO").getTextContent());
		    	
		    
		    	s.Campo("Texto211","euros"); //Cambio el literal del importe		
		    	s.BorrarCampo("Texto212"); //literal "euros" que va tras la conversión en la  información
		    	s.BorrarCampo("Euro");  //Simbolo del Euro de la información
		    	s.BorrarCampo("Etiqueta187");    //Etiqueta con información referente al Euro
		    	s.BorrarCampo("Texto190"); //Importe convertido al Euro de la información
		    
		    	Logger.debug("Se finaliza el rellenado de los campos.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				
				
				s.Mostrar();
				//Logger.info("Despues de mostrar",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);	
				//this.docCumplimentado = es.tributasenasturias.documentopagoutils.Utils.DOM2String(s.getXmlDatos());							
			} catch (Exception e) {
				Logger.error("Error al incluir datos en el pdf: "+e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			}
		}
		
	}
	
	public String getDocXml() {
		return this.docCumplimentado;
	}
	
	public String getRefCobro() {
		   return this.refCobro;
		}
		
		
	public String getNifSp(){
		return this.nifSP;
	}
	
	
	
	private void recuperarDatosPago (String idEper) {
		try {

			// llamar al servicio PXLanzador para ejecutar el procedimiento almacenado de alta de expediente
			// Se prepara la llamada al procedimiento almacenado
			//cpl = new es.tributasenasturias.pagopresentacionmodelo600utils.ConversorParametrosLanzador();
							
			cpl = new ConversorParametrosLanzador();
	        cpl.setProcedimientoAlmacenado("ACCESS_INFORMES.ImpresionJustificante");
	        // conexion
	        
	        /*
	        cpl.setParametro("1",es.tributasenasturias.pagopresentacionmodelo600utils.ConversorParametrosLanzador.TIPOS.Integer);
	        // peticion
	        cpl.setParametro("1",es.tributasenasturias.pagopresentacionmodelo600utils.ConversorParametrosLanzador.TIPOS.Integer);
	        // usuario
	        cpl.setParametro("USU_WEB_SAC",es.tributasenasturias.pagopresentacionmodelo600utils.ConversorParametrosLanzador.TIPOS.String);
	        // organismo
	        cpl.setParametro("33",es.tributasenasturias.pagopresentacionmodelo600utils.ConversorParametrosLanzador.TIPOS.Integer);	        
	        // codTactInforme
	        cpl.setParametro("NA",es.tributasenasturias.pagopresentacionmodelo600utils.ConversorParametrosLanzador.TIPOS.String);
	        // numAutoliquidacion
	        cpl.setParametro(this.numeroAutoliquidacion,es.tributasenasturias.pagopresentacionmodelo600utils.ConversorParametrosLanzador.TIPOS.String);
	        // conexion oracle
	        cpl.setParametro("P",es.tributasenasturias.pagopresentacionmodelo600utils.ConversorParametrosLanzador.TIPOS.String);            	        
	        */
	        
	        cpl.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
	        // peticion
	        cpl.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
	        // usuario
	        cpl.setParametro("USU_WEB_SAC",ConversorParametrosLanzador.TIPOS.String);
	        // organismo
	        cpl.setParametro("33",ConversorParametrosLanzador.TIPOS.Integer);	        
	        // codTactInforme
	        cpl.setParametro(idEper,ConversorParametrosLanzador.TIPOS.Integer);
	        // numAutoliquidacion	    
	        cpl.setParametro("CA",ConversorParametrosLanzador.TIPOS.String);
	        cpl.setParametro("P",ConversorParametrosLanzador.TIPOS.String);
	        cpl.setParametro("S",ConversorParametrosLanzador.TIPOS.String);

	        LanzaPLMasivoService lanzaderaWS = new LanzaPLMasivoService();					
			LanzaPLMasivo lanzaderaPort;			
			lanzaderaPort = lanzaderaWS.getLanzaPLMasivoSoapPort();
	        
	     //   Logger.info("Despues de lanzador 2",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			// enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
			//Logger.info("Despues de lanzador 3",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			// Cambiamos el endpoint
			bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointLanzador());
			//Logger.info("Despues de lanzador 4",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
	        String respuesta = "";	
	        //Logger.info("Despues de lanzador 5",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);

	        //Vinculamos con el Handler	        
	        HandlerUtil.setHandlerClient((javax.xml.ws.BindingProvider) lanzaderaPort);

	        
	        try {	        	
	        	respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");
	        	//Logger.info("Despues de respuesta",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
	        	cpl.setResultado(respuesta);	    
	        	Logger.info("FIN",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
	        }catch (Exception ex) {
	        		Logger.error("Error en lanzadera al recuperar datos del documento de pago: "+ex.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
	        		Logger.trace(ex.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
	        }
		} catch (Exception e) {
			Logger.error("Excepcion generica al recuperar los datos del documento de pago: "+e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
	}
	
}
