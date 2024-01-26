package es.tributasenasturias.docs;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


import es.tributasenasturias.Exceptions.ImpresionGDException;
import es.tributasenasturias.documentopagoutils.DatosSalidaImpresa;
import es.tributasenasturias.documentopagoutils.ListadoUtils;
import es.tributasenasturias.documentopagoutils.Logger;
import es.tributasenasturias.documentopagoutils.Oficina;
import es.tributasenasturias.documentopagoutils.Organismo;
import es.tributasenasturias.documentopagoutils.Preferencias;
import es.tributasenasturias.documentos.util.ConversorParametrosLanzador;
import es.tributasenasturias.lanzador.estructura.Estructuras;
import es.tributasenasturias.lanzador.estructura.Estructuras.Estruc;
import es.tributasenasturias.lanzador.estructura.Estructuras.Estruc.Fila;
import es.tributasenasturias.soap.handler.HandlerUtil;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivo;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivoService;


public class CartaPago extends PdfBase{

	private String ideper = "";
	private String tiponoti = "";
	private String docCumplimentado = "";
	private String refCobro = "";
	private String nifSP = "";
	private final String plantillaVoluntaria="recursos//impresos//xml//cartaPago.xml";
	private final String plantillaEjecutiva="recursos//impresos//xml//cartaPagoEjecutiva.xml";
	ConversorParametrosLanzador cpl;
	
	private Preferencias pref = new Preferencias();
	
	public CartaPago(String idEper, String tipoNoti, String origen) {
		try {
			pref.CargarPreferencias();
			session.put("cgestor", "");
			this.ideper = idEper;
			this.tiponoti = tipoNoti;
			//28004. Debemos recuperar los datos de la base en este punto porque necesitamos saber
			//el estado del valor.
			// llamar al servicio que obtiene los datos a mostrar.
			recuperarDatosPago(this.ideper, origen);
			//Establecemos la plantilla en base a los datos
			establecerPlantilla();
		} catch (Exception e) {
			Logger.debug("Error al cargar preferencias y plantilla al dar de alta el documento. "+e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
			
	}
	/**
	 * Establece la plantilla a utilizar para pintar el informe
	 */
	public void establecerPlantilla()
	{
		if (this.cpl.getResultado()!=null)
		{
			Estructuras respuesta=getEstructurasRespuestaBD(cpl);
			Fila rica0 = new Fila();
			for (Estruc e: respuesta.getEstruc())
			{
				if ("RICA_RECIBO_INTER_CABECERA".equals(e.getNombre()))
				{
					if (e.getFila().size()>0)
					{
						rica0 = e.getFila().get(0);
						break;
					}
				}
			}
			//  En este punto sí que podemos dar de alta la plantilla
			//Para ejecutiva ponemos la suya. Si no, dejamos la plantilla por defecto que será 
			//voluntaria
			if ("E".equalsIgnoreCase(rica0.getCODTESVRICA()))
			{
				this.plantilla=plantillaEjecutiva;
			}
			else
			{
				this.plantilla=plantillaVoluntaria;
			}
		}
	}
	public String getPlantilla() {
		return plantilla;
	}
	
	public void compila(String id, String xml, String xsl, OutputStream output) throws RemoteException {
		try {			
			cartapago(id, xml, xsl, output);			
		} catch (Exception e) {
			Logger.error(e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
		
	}
	
	@SuppressWarnings("static-access")
	public void cartapago(String id, String xml, String xsl, OutputStream output) {
		
		DatosSalidaImpresa s = new DatosSalidaImpresa(xml, xsl, output);
		//CRUBENCVS 41947.
		boolean esRecibo=false;
		boolean voluntaria=false;
		int paginaQR= Integer.parseInt(pref.getQRPagoVoluntariaPagina());
		//FIN CRUBENCVS 41947
		//28004. Antes se recuperaban aquí los datos del informe, ahora debe hacerse en la construcción
		//del objeto, porque es el único punto que se llama antes de este, y necesitamos saber
		//el estado (Voluntaria o Ejecutiva) para saber
		//qué pintar
		
		if (!cpl.getResultado().equals(null)) {
			// una vez obtenidos los datos y comprobado que la respuesta no es nula
			// se debe inluir el xml con los datos en la base de datos zipped
			// y se pinta el pdf.
			
			String idioma=session.get("idioma");
			
			try {
					
				Estructuras respuesta=getEstructurasRespuestaBD(cpl);
				Fila rica0 = new Fila();
				Fila canu0 =new Fila();
				Fila canu1 = new Fila();
				Fila cade = new Fila();
				Fila caor = new Fila();
				Fila teii = new Fila(); //25384
				List<Fila> ricas = new ArrayList<Fila>();
				List<Fila> tdcos = new ArrayList<Fila>();
				List<Fila> tdargs = new ArrayList<Fila>();
				
				//Recuperamos rica, canu y cade
				for (Estruc e: respuesta.getEstruc())
				{
					if ("RICA_RECIBO_INTER_CABECERA".equals(e.getNombre()))
					{
						ricas = e.getFila();
						if (e.getFila().size()>0)
						{
							rica0 = ricas.get(0);
						}
					}
					else if ("CANU_CADENAS_NUMEROS".equals(e.getNombre()))
					{
						if (e.getFila().size()>0)
						{
							canu0 = e.getFila().get(0);
							if (e.getFila().size()>1)
							{
								canu1 = e.getFila().get(1);
							}
						}
					}
					else if ("CADE_CADENA".equals(e.getNombre()))
					{
						if (e.getFila().size()>0)
						{
							cade = e.getFila().get(0);
						}
					}
					else if ("TDCO_TEX_DEPEN_CONCEPTO".equals(e.getNombre()))
					{
						tdcos = e.getFila();
						
					}
					else if ("DARG_DATOS_RESO_GESTION".equals (e.getNombre()))
					{
						tdargs = e.getFila();
					}
					else if ("CAOR_CADENA_ORDEN".equals(e.getNombre()))
					{
						if (e.getFila().size()>0)
						{
						caor = e.getFila().get(0);
						}
					}
					else if ("TEII_TEXTOS_INFORMES_INT".equals(e.getNombre()))
					{
						if (e.getFila().size()>0)
						{
							teii = e.getFila().get(0);
						}
					}
				}
				final String importeIngresar1 = rica0.getIMPORTEVALO();
				s.Campo("ImporteIngresar1",s.toEuro(importeIngresar1)+ " euros");
				final String nombreSPValo = rica0.getNOMBRESPVALO();
				s.Campo("DatosPie",nombreSPValo);
				//CRUBENCVS 33799. Se elimina la dirección en todos los casos.
				/*final String calleDire = rica0.getCALLEDIRE();
				final String calleAmpliacionDire = rica0.getCALLEAMPLIACIONDIRE();
				//CRUBENCVS  Feb/17. 
				//No se muestra la dirección del contribuyente en voluntaria en el ejemplar para la entidad bancaria en voluntaria
				if ("E".equalsIgnoreCase(rica0.getCODTESVRICA()))
				{
					s.Campo("DatosPie1",calleDire +" " +
										calleAmpliacionDire
							);
				}
				else {
					s.Campo("DatosPie1","");
				}
				*/
				//CRUBENCVS 33799.  Se elimina la dirección en todos los casos
				/*final String distritoDire = rica0.getDISTRITODIRE();
				final String municipioRica = rica0.getMUNICIPIORICA();
				final String provinciaRica = rica0.getPROVINCIARICA();
				if ("E".equalsIgnoreCase(rica0.getCODTESVRICA()))
				{
					s.Campo("DatosPie2",distritoDire +" " +
							municipioRica + " " +
							provinciaRica
					);
				}
				else 
				{
					s.Campo("DatosPie2","");
				}
				*/
				s.Campo("DatosPie1","");
				s.Campo("DatosPie2","");
				// FIN CRUBENCVS 33799
				//CRUBENCVS** 23177. Cambio de las entidades de pago
				//s.Campo("Etiqueta360", "Banesto, BBVA, Banco Sabadell-Herrero, Banco Santander, la Caixa, Cajastur, Caja Rural de Asturias o también en www.tributasenasturias.es");
				//s.Campo("Etiqueta360", "En cualquier oficina de BBVA, Caja Rural de Asturias, Cajastur, La Caixa, Sabadell - Herrero y Santander  o también en www.tributasenasturias.es");
				//CRUBENCVS** 25834
				s.Campo("Etiqueta360", teii.getTEXTO1TEII());
				//CRUBENCVS** 23177
				//Fin C.R.V. 13/05/2011.
				s.Campo("Etiqueta362", teii.getTEXTO2TEII());
				s.Campo("Etiqueta505", teii.getTEXTO2TEII());
				//FIN CRUBENCVS** 23584
				final String nifSpValo = rica0.getNIFSPVALO();
				this.nifSP = nifSpValo;			
				final String numeroValo = rica0.getNUMEROVALO();
				s.Campo("IdValor",numeroValo);
							
							
				final String string4Canu = canu0.getSTRING4CANU();
				final String string3Canu = canu0.getSTRING3CANU();
				if (string4Canu.equals("TP"))
				{
					s.Campo("DetalleLiqui", "Descripción de la Tasa");
					s.Campo("DetalleLiqui2", string3Canu);
					s.Campo("TextoLibre", "Descripción de la Tasa");
					s.Campo("TextoLibre2", string3Canu);
					//Campos de número de liquidación y referencia.
					s.Campo ("nliquidacion", canu1.getSTRING1CANU());
					s.Campo("Texto29", caor.getSTRINGCAOR());
				}
				else
				{
					//Eliminamos las de tasas
					s.BorrarCampo("Etiqueta174M");
					s.BorrarCampo("nliquidacion");
					s.BorrarCampo("Etiqueta30");
					s.BorrarCampo("Texto29");
					//Fin Eliminamos
					if  (string4Canu.equals("MP"))
					{
						s.Campo("DetalleLiqui", "Descripción de la Multa");
						s.Campo("DetalleLiqui2", string3Canu);
						s.Campo("TextoLibre", "Descripción de la Multa");
						s.Campo("TextoLibre2", string3Canu);
					}
					else
					{
						final String activConc = rica0.getACTIVCONC();
						s.Campo("DetalleLiqui",activConc);
						final String objTribValo = rica0.getOBJTRIBVALO();
						s.Campo("DetalleLiqui2",objTribValo);
						s.Campo("TextoLibre",activConc);
						s.Campo("TextoLibre2",objTribValo);
					}
				}
				
				final String modoEmisRica = rica0.getMODOEMISRICA();
				s.Campo("Modo",modoEmisRica);
				final String entidadVolu = rica0.getENTIDADVOLU();
				s.Campo("Emisora",entidadVolu);
				final String identificacionRica = rica0.getIDENTIFICACIONRICA();
				s.Campo("Identificacion",identificacionRica);
				final String numFijoValo = rica0.getNUMFIJOVALO();
				s.Campo("NumFijo",numFijoValo);
				s.Campo("IdLiquidacion",numeroValo);
				final String referenciaRica = rica0.getREFERENCIARICA();
				s.Campo("RefCobro1",referenciaRica);
				s.Campo("ReferCobro2",referenciaRica);
				this.refCobro =  s.Formato(referenciaRica,12);
				s.Campo("RefCobro1",s.Formato(referenciaRica,12));
				s.Campo("ReferCobro2",s.Formato(referenciaRica,12));
				s.Campo("ImporAIngresar2",s.toEuro(importeIngresar1) + " ");
		     	s.Campo("NTitular",nombreSPValo);
				s.Campo("IdTitular",nifSpValo);

				final String uscoRica = rica0.getUSCORICA();
				s.Campo("CodBarras2", uscoRica);
				
				s.Campo("CodigoBarras", uscoRica);
				String nCentro = "";			
				String codConc   = string4Canu;
				final String descrlConc = rica0.getDESCRLCONC();
				if (string4Canu.equals(null)) {
						s.Campo("TextoLiqui",s.IIf(s.Left(s.Right(numeroValo,8),1).equals("L"),s.traduc("Liquidación",idioma)+" ",s.IIf(s.Left(s.Right(numeroValo,8),1).equals("A"),s.traduc("Autoliquidación",idioma)+" ",s.traduc("Recibo",idioma)+" "))+descrlConc);				
						s.Campo("Etiqueta174",s.IIf(s.Left(s.Right(numeroValo,8),1).equals("L"),s.traduc("Detalle de la Liquidación",idioma)+" ",s.IIf(s.Left(s.Right(numeroValo,8),1).equals("A"),s.traduc("Detalle de la Liquidación",idioma)+" ",s.traduc("Detalle del Recibo",idioma)+" ")));
				}
				else
				{
				
				    if (string4Canu.equals("TP")) {
						s.Campo("TextoLiqui", s.traduc("Liquidación de la tasa", idioma));
					}
				    else
				    {
						if (string4Canu.equals("MP")) {
							s.Campo("TextoLiqui", s.traduc("Liquidación Global", idioma));					
						}
						else {
							s.Campo("TextoLiqui",s.IIf(s.Left(s.Right(numeroValo,8),1).equals("L"),s.traduc("Liquidación",idioma)+" ",s.IIf(s.Left(s.Right(numeroValo,8),1).equals("A"),s.traduc("Autoliquidación",idioma)+" ",s.traduc("Recibo",idioma)+" "))+descrlConc);
							s.Campo("Etiqueta174",s.IIf(s.Left(s.Right(numeroValo,8),1).equals("L"),s.traduc("Detalle de la Liquidación",idioma)+" ",s.IIf(s.Left(s.Right(numeroValo,8),1).equals("A"),s.traduc("Detalle de la Liquidación",idioma)+" ",s.traduc("Detalle del Recibo",idioma)+" ")));
							s.Campo("Etiqueta177",s.IIf(s.Left(s.Right(numeroValo,8),1).equals("L"),s.traduc("Identificación de la Liquidación",idioma)+" ",s.IIf(s.Left(s.Right(numeroValo,8),1).equals("A"),s.traduc("Identificación de la Liquidacion",idioma)+" ",s.traduc("Identificación del Recibo",idioma)+" "))+descrlConc);													
						}
							
				    }
				}
			    String campoPeriodoValo="";
				
				final String periodoValo = rica0.getPERIODOVALO();
				if (!(entidadVolu.equals(null)))
				{
					 campoPeriodoValo = periodoValo;
				}
				else
				{
					 campoPeriodoValo = "NA";
				}	
				
				String cadenaAnyo ="AAAAA";
				final String anyoCargoCarg = rica0.getANYOCARGOCARG();
				if (campoPeriodoValo.equals(null))
				{
						cadenaAnyo = s.traduc("Año ",idioma)+" "+anyoCargoCarg;
				}
				else
				{
				 if (campoPeriodoValo.equalsIgnoreCase("ANUAL") ||campoPeriodoValo.equalsIgnoreCase("NA") ||campoPeriodoValo.equalsIgnoreCase("NO APLICABLE"))
				 {
					 cadenaAnyo = s.traduc("Año ",idioma)+" "+anyoCargoCarg;		                
 			 	 }
	            else 
	             {
				    cadenaAnyo = s.IIf(!campoPeriodoValo.equals("ANUAL"),campoPeriodoValo+s.traduc(" del año",idioma)+" "+anyoCargoCarg,s.traduc("Año",idioma)+" "+ anyoCargoCarg);	        	   
                 }
				}
				final String stringCade = cade.getSTRINGCADE();
				final String string2Canu = canu0.getSTRING2CANU();
				final String string1Canu = canu0.getSTRING1CANU();
				final String nombreOrga = rica0.getNOMBREORGA();
				if (codConc.equals(null))
				{
					s.Campo("AnyoLiq",s.IIf(periodoValo!="ANUAL",periodoValo+"del año"+" "+anyoCargoCarg,"Año"+" "+anyoCargoCarg));
					s.Campo("Ayuntamiento",s.IIf(stringCade.equals("A"),s.traduc("del Ayuntamiento de",idioma)+" ",s.IIf(stringCade.equals("C"),"de la ",s.IIf(stringCade.equals("M"),"de la ","")))+nombreOrga+s.IIf(stringCade.equals("M"),"-"+rica0.getMUNICIPIOVALORICA(),""));
					s.Campo("Subo",nombreOrga);
				}
				else
				{
						if (codConc.equals("MP"))
						{
							nCentro = "CENTRO GESTOR " + string2Canu;
							s.Campo("Subo", nCentro + s.IIf(stringCade.equals("M"), "-" + nCentro, ""));
							s.Campo("Ayuntamiento",string1Canu);
							s.Campo("AnyoLiq",s.IIf(stringCade.equals("A"),s.traduc("del Ayuntamiento de",idioma)+" ",s.IIf(stringCade.equals("C"),"de la ",s.IIf(stringCade.equals("M"),"de la ","")))+nCentro+s.IIf(stringCade.equals("M"),"-"+nCentro,"")+"      "+cadenaAnyo);
						}
						else 
						{
							if (codConc.equals("TP"))
							{
								nCentro = string2Canu;
								s.Campo("Subo", nCentro + s.IIf(stringCade.equals("M"), "-" + nCentro, ""));
								s.Campo("Ayuntamiento",string1Canu);
								s.Campo("AnyoLiq",s.IIf(stringCade.equals("A"),s.traduc("del Ayuntamiento de",idioma)+" ",s.IIf(stringCade.equals("C"),"de la ",s.IIf(stringCade.equals("M"),"de la ","")))+nCentro+s.IIf(stringCade.equals("M"),"-"+nCentro,"")+"      "+cadenaAnyo);
							}
							else
							{
								s.Campo("AnyoLiq",s.IIf(periodoValo!="ANUAL",periodoValo+"del año"+" "+anyoCargoCarg,"Año"+" "+anyoCargoCarg));
								s.Campo("Ayuntamiento",s.IIf(stringCade.equals("A"),s.traduc("del Ayuntamiento de",idioma)+" ",s.IIf(stringCade.equals("C"),"de la ",s.IIf(stringCade.equals("M"),"de la ","")))+nombreOrga+s.IIf(stringCade.equals("M"),"-"+rica0.getMUNICIPIOVALORICA(),""));
								s.Campo("Subo",nombreOrga);
							}
						}
				}
				s.Campo("Etiqueta514",s.IIf(stringCade.equals("A"),s.traduc("Valor del Ayuntamiento de",idioma)+" ",s.traduc("Valor de",idioma)+" "));
		    	String szFecha = rica0.getFECHAFINVOLU();
		    	String formatoEntrada = "dd/MM/yyyy";				
				if(szFecha.length() == 8){
					formatoEntrada = "dd/MM/yy";
				}
	
				DateFormat df = new SimpleDateFormat(formatoEntrada); 
				Date fechaAux = df.parse(szFecha);  
				
				if (!fechaAux.equals(null))
				{
					SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
					
					s.Campo("FechaCadu",formateador.format(fechaAux));
				}
				//CRUBENCVS** 25483
				s.Campo("LugarPago",teii.getTEXTO1TEII());
				//FIN CRUBENCVS** 25483
				
				final String nombreContribRica = rica0.getNOMBRECONTRIBRICA();
				s.Campo("nombreContr",nombreContribRica);				
				final String nifContribRica = rica0.getNIFCONTRIBRICA();
				s.Campo("nifContr",nifContribRica);
			
				s.Campo("nombreSP",nombreSPValo);				
				s.Campo("nifSP",nifSpValo);
				
								
				s.Campo("Texto143",rica0.getDETALLE1RICA());
				s.Campo("Texto144",rica0.getDETALLE2RICA());
				s.Campo("Texto145",rica0.getDETALLE3RICA());
				s.Campo("Texto146",rica0.getDETALLE4RICA());
				s.Campo("Texto147",rica0.getDETALLE5RICA());
				s.Campo("Texto148",rica0.getDETALLE6RICA());
				s.Campo("Texto149",rica0.getDETALLE7RICA());
				s.Campo("Texto150",rica0.getDETALLE8RICA());
				s.Campo("Texto151",rica0.getDETALLE9RICA());
				s.Campo("Texto152",rica0.getDETALLE10RICA());
				s.Campo("Texto153",rica0.getDETALLE11RICA());
				s.Campo("Texto154",rica0.getDETALLE12RICA());
				s.Campo("Texto155",rica0.getDETALLE13RICA());										
				s.Campo("DETALLE_14",rica0.getDETALLE14RICA());
				s.Campo("DETALLE_15",rica0.getDETALLE15RICA());
				
				
				//28004
				//Si es ejecutiva, pintamos sus datos
				if ("E".equalsIgnoreCase(rica0.getCODTESVRICA()))
				{
					s.Campo("princ_ej",s.toEuro(rica0.getPRINCIPALRV()));
					s.Campo("porc_recargo_ej",rica0.getPORCRECARGORV()+"%");
					s.Campo("recargo_ej",s.toEuro(rica0.getRECARGORV()));
					s.Campo("intereses_ej",s.toEuro(rica0.getINTERESESRV()));
					s.Campo("costas_ej",s.toEuro(rica0.getCOSTASRV()));
					s.Campo("ingresos_ej",s.toEuro(rica0.getINGRESOSRV()));
					// CRUBENCVS 41947. 
					voluntaria=false;
					// FIN CRUBENCVS 41947
				}  
				else { //CRUBENCVS 41947
					voluntaria=true;
				}
				//Datos de la notificacion		
				if (this.tiponoti.equals("NT"))
				{
					Organismo organismo = es.tributasenasturias.documentopagoutils.ListadoUtils.obtenerOrganismo("33"); //Siempre estamos tratando con este organismo.
					//Element[] rsTDCO = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='TDCO_TEX_DEPEN_CONCEPTO']/fila");
					Fila tdco0 = new Fila();
					Fila tdco1 = new Fila();
					if (tdcos.size()>0)
					{
						tdco0= tdcos.get(0);
						if (tdcos.size()>1)
						{
							tdco1 = tdcos.get(1);
						}
					}
					//19/12/2011
					//Para no recuperar los textos fijos en varios viajes, se han añadido los textos significativos 
					//en la misma consulta de datos.
					//Element[] rsTDARG = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='DARG_DATOS_RESO_GESTION']/fila");
					Map<Integer,String> textosFijos = getTextosFijos(tdargs);
					//Estos textos fijos se pasarán a una nueva versión de "rellenaTexto" que ya no viajará a base de datos.
					s.Campo("Etiqueta357", s.traduc(s.rellenaTexto(tdco0.getCODTEXTTEXTOS(),"33", organismo),idioma));
					s.Campo("Etiqueta358", s.traduc(s.rellenaTexto(tdco1.getCODTEXTTEXTOS(),"33", organismo),idioma));
					s.Campo("Etiqueta192","Ejemplar para el contribuyente.  2 de 2");
					//String texto1 = s.rellenaTexto(85, 33, organismo);
					String texto1 = s.rellenaTexto(85, textosFijos);
					s.Campo("Etiqueta364", s.traduc(texto1, idioma));
					
					//CRUBENCVS 23584
					//s.Campo("punto4texto", s.traduc(s.rellenaTexto(87, textosFijos), idioma));
					s.Campo("Texto279", identificacionRica);
					s.Campo("Texto233", s.Left(nombreSPValo, 35));
					s.Campo("Texto234", nifSpValo);
					s.Campo("Texto275", s.Left(nombreContribRica, 38));
					s.Campo("Texto336",s.Left(rica0.getNOMBREREPRRICA(),35));
					s.Campo("Texto337",rica0.getNIFREPRRICA()); 
					
					String nombreCentro = "";			
					String tipoTasa   = string4Canu;
					if (tipoTasa.equals("MP"))
						nombreCentro = "CENTRO GESTOR " + string2Canu;
					else
						nombreCentro = string2Canu;
					
					s.Campo("Texto300", nombreCentro + s.IIf(stringCade.equals("M"), "-" + nombreCentro, ""));
					Logger.info("punto 11:",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
					//C.R.V. 19/12/2011
					//Estos dos no parecen ser necesarios, se comentan
					//s.Campo("Texto577", s.rellenaTexto(24,33, organismo));
					//s.Campo("Texto578", s.rellenaTexto(25,33, organismo));
					//Fin C.R.V. 19/12/2011
					//C.R.V. 19/12/2011. Los accesos a base de datos se intentan reducir.
					List<Oficina> oficinas = ListadoUtils.obtenerOficinasBySaleRecibo("S");
					s.Campo("textOfic1", s.rellenaOficinas(""
							+ s.Val(s.Left(s.Right(numeroValo,
									(numeroValo).length() - 4), 2)), 1, oficinas));
										
					s.Campo("Texto576", s.rellenaOficinas(""
							+ s.Val(s.Left(s.Right(numeroValo,
									(numeroValo).length() - 4), 2)), 2,oficinas));
					
				    if (string4Canu.equals("TP")) {
						s.Campo("Texto332", s.traduc("Liquidación de la tasa", idioma));
						s.Campo("Texto542", s.IIf(stringCade.equals("A"), s.traduc("Recibo del Ayuntamiento de",
								idioma)
								+ " ", s.traduc("Valor de", idioma) + " "));
					
						//CRUBENCVS** 25834. Eliminamos los datos de oficinas para tasas
						s.BorrarCampo("Etiqueta569");
						s.BorrarCampo("Etiqueta568");
						s.BorrarCampo("Etiqueta569");
						s.BorrarCampo("Etiqueta568");
						s.BorrarCampo("Etiqueta570");
						s.BorrarCampo("Etiqueta570");
						s.BorrarCampo("Linea2");
						s.BorrarCampo("Linea3");
						//CRUBENCVS 25834. Se eliminan las oficinas del informe
						s.BorrarCampo("textOfic1");
						s.BorrarCampo("Texto576");
						//Cambiamos "Titular" por "Sujeto Pasivo"
						s.Campo("Etiqueta413", "Sujeto Pasivo");
					}
					if (string4Canu.equals("MP")) {
						s.Campo("Texto332", s.traduc("Liquidación Global", idioma));
						s.Campo("Texto542", s.IIf(stringCade.equals("A"), s.traduc(
								"Recibo del Ayuntamiento de", idioma)
								+ " ", ""));
					}
					s.Campo("MODO_EMIS_RICA", modoEmisRica);
				
					if (string4Canu.equals("TP"))
							s.Campo("Texto353", "Descripción de la Tasa");
						else
							s.Campo("Texto353", "Descripción de la Multa"); //FIXME: ¿Y si tampoco es una multa?.
					
					s.Campo("Texto501", s.Left(nombreSPValo, 37));
					s.Campo("Texto502", nifSpValo);
					// CRUBENCVS 33799. Se elimina la dirección también de la notificación,   aunque nunca se va a mostrar.
					/*if (calleDire.length() > 52)
						
						s.Campo("Texto666", calleDire);
					else
						s.Campo("Texto347", calleDire);
	
					s.Campo("Texto346", s.IIf(s.validString(s.Trim(calleAmpliacionDire)), s.Trim(calleAmpliacionDire)
							+ "\n", " ")
							+ s.Trim(s.Formato(distritoDire, 5))
							+ " "
							+ s.Trim(municipioRica)
							+ s.IIf(s.validString(s.Trim(calleAmpliacionDire)), " ", "\n")
							+ s.Trim(provinciaRica));
					*/
					s.Campo("Texto666", "");
					s.Campo("Texto347", "");
					s.Campo("Texto346", "");
					// FIN CRUBENCV 33799
					//C.R.V. 19/12/2011
					//s.Campo("Etiqueta366", s.traduc(s.rellenaTexto(55, s.Val(s.Left(s.Right(numeroValo, (numeroValo).length() - 4), 2)), organismo), idioma));	
					s.Campo("Etiqueta366", s.traduc(s.rellenaTexto(55, textosFijos), idioma));
					//Fin C.R.V. 19/12/2011
					s.Campo("Texto276", s.Formato(referenciaRica, 12));
					s.Campo("Texto330", nifContribRica);
					s.Campo("Texto329", s.Left(nombreContribRica, 37));
					s.Campo("Texto303", numeroValo);
					s.Campo("Texto301", string3Canu);
					s.Campo("Texto281", s.Formato(referenciaRica, 12));
					s.Campo("Texto280", s.toEuro(importeIngresar1 + ""));
					s.Campo("Texto278", s.Right("000000" + entidadVolu, 6));
					
					
					s.Campo("Texto331",s.IIf(stringCade.equals("A"),s.traduc("del Ayuntamiento de",idioma)+" ",s.IIf(stringCade.equals("C"),"de la ",s.IIf(stringCade.equals("M"),"de la ","")))+nombreCentro+s.IIf(stringCade.equals("M"),"-"+nombreCentro,"")+"      "+cadenaAnyo);		
					
					s.Campo("consejeria",string1Canu);
					s.Campo("consejeria2",string1Canu);
					s.Campo("CodBarras1", s.Right("000000000000" + s.Formato(referenciaRica), 12));
					
					//CRUBENCVS 41947. Si hay notificación, la página de QR, si se va a pintar, es la siguiente a la indicada, porque se pone delante una.
					//Creo que para voluntaria no hay notificación, y sólo se pinta QR en voluntaria, pero 
					//por si acaso
					paginaQR++;
				}
				else
				{
					s.BorraPagina("notificacion");
				}
				//C.R.V. 12/09/2011. Si es recibo, y según los datos recibidos, se pinta un formato u otro.
				if (rica0.getTIPOINFORMEACCESS().equalsIgnoreCase("VO"))
				{
					esRecibo=true;
					informarBloqueReciboConCaducidad(s, ricas, cade);
					s.BorrarCampo("formatoGeneral");
					//Hay segundo registro y tiene identificación
					if (ricas.size()>1 && ricas.get(1)!=null && !"".equals(ricas.get(1).getIDENTIFICACIONRICA())) 
					{
						s.BorrarCampo("cabeceraSinCaducidad");
					}
					else
					{
						s.BorrarCampo("cabeceraConCaducidad");
					}
				}
				else
				{
					s.BorrarCampo("formatoRecibo");
				}
				//Fin C.R.V. 12/09/2011
				s.Mostrar();
					
				//CRUBENCVS 41947. Incrustamos el código QR, si es necesario.
				if (voluntaria && esRecibo){
					GeneradorCodigoBarras codBarra= new GeneradorCodigoBarras();
					//En este punto, si falla no devolverá el "output" y pintará lo que hay, sin código qr. De todas maneras, controlo el error.
					try {
						//output= codBarra.incrustaCodigoQRPagoReferencia(this.refCobro, output, paginaQR, 30, 30);
						codBarra.incrustaCodigoQRPagoReferencia(this.refCobro, s.pdfoutput, paginaQR, 
																Integer.parseInt(pref.getQRPagoVoluntariaX()), 
																Integer.parseInt(pref.getQRPagoVoluntariaY()), 
																Integer.parseInt(pref.getQRPagoVoluntariaAncho()), 
																Integer.parseInt(pref.getQRPagoVoluntariaAlto()));
						
					} catch (ImpresionGDException im){
						//Lo ignoro. Si no puede pintar el QR, que pinte el resto.
					}
				} else {
					s.BorrarCampo("lblPagoTele");
				}
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
	
	private void recuperarDatosPago (String idEper, String origen) {
		try {

			// llamar al servicio PXLanzador para ejecutar el procedimiento almacenado de alta de expediente
			// Se prepara la llamada al procedimiento almacenado
			//cpl = new es.tributasenasturias.pagopresentacionmodelo600utils.ConversorParametrosLanzador();
			//Logger.info("Antes de lanzador",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);					
			cpl = new ConversorParametrosLanzador();
	        cpl.setProcedimientoAlmacenado("INTERNET_RECIBOS.nuevoRecibosOnline");
	        // conexion
	        
	      
	        cpl.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
	        // peticion
	        cpl.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
	        // usuario
	        cpl.setParametro("USU_WEB_SAC",ConversorParametrosLanzador.TIPOS.String);
	        // organismo
	        cpl.setParametro("33",ConversorParametrosLanzador.TIPOS.Integer);	        
	        // idEper
	        cpl.setParametro(idEper,ConversorParametrosLanzador.TIPOS.Integer);
	        // origenPortal    
	        cpl.setParametro("P",ConversorParametrosLanzador.TIPOS.String);
	        //origen de la petición
	        cpl.setParametro(origen,ConversorParametrosLanzador.TIPOS.String);

	        LanzaPLMasivoService lanzaderaWS = new LanzaPLMasivoService();					
			LanzaPLMasivo lanzaderaPort;			
			lanzaderaPort = lanzaderaWS.getLanzaPLMasivoSoapPort();


			// enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;

			// Cambiamos el endpoint
			bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointLanzador());

	        String respuesta = "";	
	        //Vinculamos con el Handler	        
	        HandlerUtil.setHandlerClient((javax.xml.ws.BindingProvider) lanzaderaPort);

	        
	        try {	        	
	        	respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");
	        	cpl.setResultado(respuesta);	    
	        }catch (Exception ex) {
		        	Logger.error ("Error en lanzadera al recuperar datos del documento de pago: "+ex.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		        	Logger.trace(ex.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
	        }
		} catch (Exception e) {
			Logger.error("Excepcion generica al recuperar los datos del documento de pago: "+e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			Logger.trace(e.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
	}
	private void informarBloqueReciboConCaducidad (DatosSalidaImpresa s, List<Fila> ricas, Fila cade)
	{
		Fila rica0 = ricas.get(0);
		Fila rica1 = null;
		if (ricas.size()>1)
		{
			rica1 = ricas.get(1);
		}
		//Bloque de periodo voluntario, emisora, referencia, etc
		s.Campo("Texto130", formateaFecha(rica0.getFECHAFINVOLU()));
		final String entidadVolu = rica0.getENTIDADVOLU();
		s.Campo("Texto131", DatosSalidaImpresa.Right("000000"+entidadVolu, 6));
		final String modoEmisRica = rica0.getMODOEMISRICA();
		s.Campo("MODO_EMIS_RICA", modoEmisRica);
		final String referenciaRica = rica0.getREFERENCIARICA();
		s.Campo("Etiqueta132",DatosSalidaImpresa.Formato(referenciaRica,12));
		final String identificacionRica = rica0.getIDENTIFICACIONRICA();
		s.Campo("Texto133", identificacionRica);
		final String importeValo = rica0.getIMPORTEVALO();
		s.Campo("Texto134", DatosSalidaImpresa.toEuro(importeValo));
		//Texto de la segunda línea, del 5%, si existe.
		if (rica1!=null)
		{
			s.Campo("Texto1305", formateaFecha(rica1.getFECHAFINVOLU()));
			s.Campo("Etiqueta1325",DatosSalidaImpresa.Formato(rica1.getREFERENCIARICA(),12));
			s.Campo("Texto1335", rica1.getIDENTIFICACIONRICA());
			s.Campo("Texto1345", DatosSalidaImpresa.toEuro(rica1.getIMPORTEVALO()));
		}
		//Datos de titular, etc.
		final String stringCade = cade.getSTRINGCADE();
		s.Campo("Texto542",DatosSalidaImpresa.IIf(stringCade.equalsIgnoreCase("A"),"Valor del Ayuntamiento de","Valor de"));
		s.Campo("Texto60",DatosSalidaImpresa.Left(rica0.getNOMBRESPVALO(),32));
		s.Campo("Texto61",rica0.getNIFSPVALO());
		s.Campo("Texto197",rica0.getNOMBREORGA()+
				DatosSalidaImpresa.IIf(stringCade.equalsIgnoreCase("M"),
						"-"+rica0.getMUNICIPIOVALORICA(),""));
		s.Campo("Texto230",rica0.getACTIVCONC());
		s.Campo("Texto199",rica0.getOBJTRIBVALO());
		final String numeroValo = rica0.getNUMEROVALO();
		s.Campo("Texto201",numeroValo);
		//Datos de la domiciliación
		// INI 35012. Modificamos para incluir el IBAN
		//s.Campo("Texto214",
		//		DatosSalidaImpresa.referencia_dc(referenciaRica,
		//				entidadVolu));
		s.Campo("txtReferenciaMandatoDomiciliacion",
						"Referencia de Mandato:"+DatosSalidaImpresa.referencia_dc(referenciaRica,
								entidadVolu));
		//Datos del contribuyente
		s.Campo("Texto120",DatosSalidaImpresa.Left(rica0.getNOMBRECONTRIBRICA(),32));
		//CRUBENCVS 30704/2018. 33799
		/*final String calleDire = rica0.getCALLEDIRE();
		if (calleDire.length() > 52)
		{
			s.Campo("Texto666", calleDire);
		}
		else
		{
			s.Campo("Texto219", calleDire);
		}
		//Formatear la dirección.
		final String calleAmpliacionDire = rica0.getCALLEAMPLIACIONDIRE();
		String direccion=calleAmpliacionDire;
		if (direccion.equals(""))
		{
			//Formateamos la dirección.
			//direccion+="\n";
		}
		else
		{
			direccion=" ";
		}
		direccion +=DatosSalidaImpresa.Formato(rica0.getDISTRITODIRE(),5)+" ";
		direccion += rica0.getMUNICIPIORICA();
		if (calleAmpliacionDire.equals(""))
		{
			direccion+=" ";
		}
		else
		{
			direccion +="\n";
		}
		direccion += rica0.getPROVINCIARICA();
		s.Campo("Texto217",direccion);*/
		s.Campo("Texto666", "");
		s.Campo("Texto219", "");
		s.Campo("Texto217","");
		// FIN CRUBENCVS 33799
		final String uscoRica = rica0.getUSCORICA();
		s.Campo("Texto127",uscoRica);
		s.Campo("CodBarras2Recibo",uscoRica);
		
		
		s.Campo("Modo5",modoEmisRica);
		s.Campo("Emisora5",entidadVolu);
		s.Campo("Identificacion5",identificacionRica);
		s.Campo("NumFijo5",rica0.getNUMFIJOVALO());
		s.Campo("IdLiquidacion5",numeroValo);
		s.Campo("ReferCobro25",DatosSalidaImpresa.Formato(referenciaRica,12));
		s.Campo("ImporAIngresar25",DatosSalidaImpresa.toEuro(importeValo) + " ");
	}
	/**
	 * Formatea la fecha que se pasa como cadena.
	 * @param fecha Fecha a formatear.
	 * @return Fecha formateada, según "dd/MM/yyyy o dd/MM/yy
	 */
	private String formateaFecha (String fecha)
	{
    	String formatoEntrada = "dd/MM/yyyy";
    	String fechaFormateada;
		if(fecha.length() == 8){
			formatoEntrada = "dd/MM/yy";
		}

		DateFormat df = new SimpleDateFormat(formatoEntrada);
		try
		{
			Date fechaAux = df.parse(fecha);  
			
			if (!(fechaAux==null))
			{
				SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
				
				fechaFormateada=formateador.format(fechaAux);
			}
			else
			{
				fechaFormateada="";
			}
		}
		catch (ParseException e)
		{
			fechaFormateada="";
		}
		return fechaFormateada;
	}
	
	private Estructuras getEstructurasRespuestaBD (ConversorParametrosLanzador cpl)
	{
		Estructuras estruct=null;
		if (cpl.getResultado()!=null && !"".equals(cpl.getResultado()))
		{
			try {
				JAXBContext jxb = JAXBContext.newInstance("es.tributasenasturias.lanzador.estructura");
				Unmarshaller unmars = jxb.createUnmarshaller();
				estruct = (Estructuras)unmars.unmarshal(new ByteArrayInputStream(cpl.getResultado().getBytes("ISO-8859-1")));
			} catch (JAXBException e) {
				Logger.error("Error al leer la respuesta del lanzador. "+e.getMessage(),e,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			} catch (UnsupportedEncodingException e) {
				Logger.error("Error al leer la respuesta del lanzador. "+e.getMessage(),e,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			}
		}
		return estruct;
	}
	private Map<Integer, String> getTextosFijos(List<Fila> filas)
	{
		Map<Integer, String> textos= new HashMap<Integer, String>();
		String id;
		String text;
		for (int i=0;i<filas.size();i++)
		{
			id = filas.get(i).getIDTEXTRECURSO();
			text = filas.get(i).getPROPUESTADARG();
			if (text!=null && !"".equals(text))
			{
				textos.put(Integer.valueOf(id), text);
			}
		}
		return textos;
	}
}
