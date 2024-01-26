package es.tributasenasturias.docs;

import java.io.OutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.tributasenasturias.Exceptions.CartaPagoException;
import es.tributasenasturias.documentopagoutils.DatosSalidaImpresa;
import es.tributasenasturias.documentopagoutils.ListadoUtils;
import es.tributasenasturias.documentopagoutils.Logger;
import es.tributasenasturias.documentopagoutils.Oficina;
import es.tributasenasturias.documentopagoutils.Preferencias;
import es.tributasenasturias.documentos.util.ConversorParametrosLanzador;
import es.tributasenasturias.soap.handler.HandlerUtil;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivo;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivoService;


public class CartaPagoReferenciaGrupo extends PdfBase{

	private String ideper = "";
	private String docCumplimentado = "";
	private String refCobro = "";
	private String nifSP = "";
	
	ConversorParametrosLanzador cpl;
	
	private Preferencias pref = new Preferencias();
	
	public CartaPagoReferenciaGrupo(String idEper, String tipoNoti) throws CartaPagoException{
		try {
			pref.CargarPreferencias();
			this.ideper=idEper;
			session.put("cgestor", "");
			plantilla = "recursos//impresos//xml//genRefUnicaEmi.xml";
		} catch (Exception e) {
			throw new CartaPagoException("Error al cargar preferencias y plantilla para la carta de pago agrupada:"+e.getMessage(),e);
		}
			
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void compila(String id, String xml, String xsl, OutputStream output) throws RemoteException {
		try {			
			cartapagoRefGrupo(id, xml, xsl, output);			
		} catch (Exception e) {
			Logger.error(e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
		
	}
	
	
	/**********************
	 * 
	 **********************/
 
	public static class RicaComparator implements Comparator<Element>,Serializable
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -5560265997651028551L;

		@Override
		public int compare(Element o1, Element o2) {
			String codtfva1;
			String codtfva2;
			Element e1=XMLUtils.selectSingleNode(o1,"cod_tfva");
			Element e2=XMLUtils.selectSingleNode(o2,"cod_tfva");
			if (e1!=null)
			{
				codtfva1=e1.getNodeValue();
			}
			else
			{
				codtfva1="";
			}
			if (e2!=null)
			{
				codtfva2 = e2.getNodeValue();
			}
			else
			{
				codtfva2="";
			}
			int comparacion=codtfva1.compareTo(codtfva2);
			if (comparacion==0)
			{
				return 0;
			} else if (comparacion<0)
			{
				return -1;
			}
			else if (comparacion > 0)
			{
				return 1;
			}
			return 0;
		}
		
	}
	
	/**
	 * Recupera el valor de un nodo del vector de elementos.
	 * @param Array de {@link Element} de donde se recuperarán los datos
	 * @param nombreNodo Nombre del nodo a recuperar
	 * @param fila Índice en el array de elementos de donde recuperar el valor.
	 * @return Valor del elemento o cadena vacía si no se encuentra.
	 */
private String campo (Element[] recordset, String nombreNodo, int fila)
{
	Element nodo=XMLUtils.selectSingleNode(recordset[fila],nombreNodo);
	if (nodo!=null)
	{
		String texto=XMLUtils.getNodeText(nodo);
		if (texto!=null)
		{
			return texto;
		}else
		{
			return "";
		}
	}
	else
	{
		return "";
	}
}
/**
 * Recupera el valor de un nodo del vector de elementos, en el índice 0
 * @param recordset Array de {@link Element} de donde se recuperarán los datos.
 * @param nombreNodo Nombre del nodo a recuperar
 * @return Valor del elemento o cadena vacía si no se encuentra.
 */
private String campo (Element[] recordset, String nombreNodo)
{
	return campo (recordset, nombreNodo,0);
}

/**
 * Devuelve el valor del campo como una fecha en formato dd/MM/yyyy, para la primera fila del array
 * @param rs Array de elementos de donde obtener los valores.
 * @param nombre Nombre del campo a buscar en el array de elementos.
 * @return Valor del campo en formato dd/MM/yyyy
 */
public String campoFecha (Element [] rs, String nombre)
{
	return campoFecha (rs, nombre, 0);
}
/**
 * Devuelve el valor del campo como una fecha en formato dd/MM/yyyy
 * @param rs Array de elementos de donde obtener los valores.
 * @param nombre Nombre del campo a buscar en el array de elementos.
 * @param fila Número de la fila.
 * @return Valor del campo en formato dd/MM/yyyy
 */
public String campoFecha(Element[] rs, String nombre, int fila) {
	String szFecha = campo(rs, nombre,fila);
	if (szFecha == null || szFecha.equals("")) {
		return "";
	} else {
		try {
			String formatoEntrada = "dd/MM/yyyy";				
			if(szFecha.length() == 8){
				formatoEntrada = "dd/MM/yy";
			}

			DateFormat df = new SimpleDateFormat(formatoEntrada); 
			Date fecha = df.parse(szFecha);  
			
			if (fecha == null)
				return "";
			SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
			return formateador.format(fecha);
		} catch (Exception e) {
			Logger.trace(e.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			return "";
		}
	}
}

private void recuperarDatosPago (String idEper) throws CartaPagoException{
	try {
		cpl = new ConversorParametrosLanzador();
        cpl.setProcedimientoAlmacenado("INTERNET_RECIBOS.referenciaGrupo");
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

        
       	
    	respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");
    	cpl.setResultado(respuesta);	    
	} catch (Exception e) {
    	throw new CartaPagoException ("Error en lanzador al recuperar los datos de carta de pago con referencia de grupo:" + e.getMessage(),e);
	}
}

public void cartapagoRefGrupo(String id, String xml, String xsl, OutputStream output) throws CartaPagoException{

		//Se supone que se han recuperado los datos antes de este punto.
		DatosSalidaImpresa s = new DatosSalidaImpresa(xml, xsl, output);
		recuperarDatosPago (this.ideper);
		
		if (!cpl.getResultado().equals(null)) {
			
			try {
				
				double costasExpediente=0;
				
				Document docRespuesta = (Document) XMLUtils.compilaXMLObject(cpl.getResultado(), null);			
				Element[] rsRece = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='RECE_RECI_EXPED']/fila");
				
				Element[] rsTeii = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='TEII_TEXTOS_INFORMES_INT']/fila");
				
				if (rsRece!= null && rsRece.length>0)
				{
					s.Campo("nombreCont",campo(rsRece,"NOMBRECONTRIBUYENTE_RECE"));
					s.Campo("nifCont",campo(rsRece,"NIFCONTRIBUYENTE_RECE"));
					s.Campo("nombreSP",campo(rsRece,"NOMBRE_SP_RECE"));	
					s.Campo("nifSP",campo(rsRece,"NIF_SP_RECE"));	
					s.Campo("fechaCaducidad",campoFecha(rsRece,"DIAPAGO_RECE"));	
					s.Campo("Emisora",campo(rsRece,"EMISORA_RECE"));
					s.Campo("Modo",campo(rsRece,"MODO_EMIS_RICA"));
					s.Campo("RefCobro",DatosSalidaImpresa.Right("000000000000"+ campo(rsRece,"REFERENCIA_RECE"),12));
					s.Campo("fechaCaducidadpag2",campoFecha(rsRece,"DIAPAGO_RECE"));	
					s.Campo("Emisorapag2",campo(rsRece,"EMISORA_RECE"));
					s.Campo("Modopag2",campo(rsRece,"MODO_EMIS_RICA"));
					s.Campo("RefCobropag2",DatosSalidaImpresa.Right("000000000000"+ campo(rsRece,"REFERENCIA_RECE"),12));
					s.Campo("referenciaLarga",campo(rsRece,"REF_LARGA_RECE"));
					s.Campo("Indentificacion",campo(rsRece,"IDENTIFICACION_RECE"));
					s.Campo("Indentificacionpag2",campo(rsRece,"IDENTIFICACION_RECE"));
					s.Campo("TitPad",campo(rsRece,"NOMBRE_SP_RECE"));
					s.Campo("NifTitPad",campo(rsRece,"NIF_SP_RECE"));
					s.Campo("Indentificacion",campo(rsRece,"IDENTIFICACION_RECE"));
					s.Campo("vnombreCont",campo(rsRece,"NOMBRECONTRIBUYENTE_RECE"));
					s.Campo("vnifCont",campo(rsRece,"NIFCONTRIBUYENTE_RECE"));
					s.Campo("vnombreSP",campo(rsRece,"NOMBRE_SP_RECE"));
					s.Campo("vnifSP",campo(rsRece,"NIF_SP_RECE"));
					s.Campo("nombreDest",campo(rsRece,"NOMBRECONTRIBUYENTE_RECE"));
					s.Campo("CodBarras2",campo(rsRece,"REF_LARGA_RECE"));
					

					s.Campo("PrincipalTotal",DatosSalidaImpresa.toEuro(campo(rsRece,"PRINCIPAL_RECE")));
					s.Campo("recargoTotal",DatosSalidaImpresa.toEuro(campo(rsRece,"APREMIO_RECE")));
					s.Campo("interesesTotal",DatosSalidaImpresa.toEuro(campo(rsRece,"INTERESES_RECE")));
					s.Campo("costasTotal",DatosSalidaImpresa.toEuro(campo(rsRece,"COSTAS_RECE")+costasExpediente));
					s.Campo("ingCtaTotal",DatosSalidaImpresa.toEuro(String.valueOf(Math.abs(DatosSalidaImpresa.parseInt(campo(rsRece,"INGRESOCUENTA_RECE"))))));
					s.Campo("importeIngresarTotal",DatosSalidaImpresa.toEuro(campo(rsRece,"DEUDA_RECE")));
					s.Campo("ImpAIngresar",DatosSalidaImpresa.toEuro(campo(rsRece,"DEUDA_RECE")));
					s.Campo("ImpAIngresarpag2",DatosSalidaImpresa.toEuro(campo(rsRece,"DEUDA_RECE")));
					
					costasExpediente = DatosSalidaImpresa.parseInt(campo(rsRece,"TOTAL_RECE"));
				}
				//CRUBENCVS** 25834. Textos de validación y lugar de pago
				if (rsTeii!=null && rsTeii.length>0)
				{
					s.Campo("Eti_Sup3", campo(rsTeii,"TEXTO2_TEII"));
					s.Campo("Eti_Sup5", campo(rsTeii,"TEXTO1_TEII"));
				}
				
				//C.R.V. 19/12/2011. Los accesos a base de datos se intentan reducir.
				List<Oficina> oficinas = ListadoUtils.obtenerOficinasBySaleRecibo("S");
				s.Campo("textOfic1",DatosSalidaImpresa.rellenaOficinas("33",1, oficinas));
				s.Campo("textOfic2",DatosSalidaImpresa.rellenaOficinas("33",2, oficinas));
				//CRUBENCVS** 23177. Cambiamos las oficinas de pago
				// CRUBENCVS ** 25834. Se comenta esta
				//s.Campo("Eti_Sup5","En cualquier oficina de BBVA, Caja Rural de Asturias, Cajastur, La Caixa, Sabadell - Herrero y Santander  o también en www.tributasenasturias.es");
				// FIN CRUBENCVS** 25384
				//FIN CRUBENCVS ** 23177
				String texto1 = "El presente documento de pago únicamente será válido para hacer efectivas las deudas "+
					     "que en el mismo se especifican en el día de su expedición; y ello sin perjuicio de los "+
					     "plazos generales de ingreso de la deuda, que será en todo caso los previstos en el "+
					     "art. 62 de la L.G.T.\n\nDe no hacer uso del mismo para el pago conjunto de todas las "+
					     "deudas podrá obtener nuevos documentos de pago individuales o colectivos en cualquiera "+
					     "de nuestras oficinas.";
					     
				s.Campo("texto1",texto1);
				
				Element[] rsRica = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='RICA_RECIBO_INTER_CABECERA']/fila");
				int limite = rsRica.length;
				
				//CRUBENCVS. Feb/ 17. No se mostrará en el ejemplar para la entidad financiera la dirección
				//del contribuyente.
				/*if (rsRica !=null && rsRica.length>0)
				{
					if ("S".equals(campo(rsRica,"TISU_RICA")))
					{
						s.Campo("calleDest",campo(rsRica,"CALLE_DIRE"));
						s.Campo("ampliacion",campo(rsRica,"CALLE_AMPLIACION_DIRE"));
						s.Campo("municipioDest",campo(rsRica,"DISTRITO_DIRE")+" "+campo(rsRica,"MUNICIPIO_RICA")+"       "+campo(rsRica,"PROVINCIA_RICA"));
					}
				}*/
				
				//Ahora necesitamos ordenar los elementos, ya que esto no es una SELECT que nos permita ORDER BY
				
				java.util.Arrays.sort(rsRica,new RicaComparator());
				
				//Elementos ordenados, ahora seguimos con el proceso.
				
				int numValores=23;
				int resto = limite%numValores;
				int numpag = (int)limite/numValores;
				int tot=numpag+1;
				
				if (resto==0)
				{
					resto=numValores;
					numpag= numpag-1;
					tot= tot-1;
				}
				
				s.Campo("numPag","Página 1 de "+(tot+1));
				
				List<String> datos= new ArrayList<String>();
				
				int indiceBase = limite - resto; //El índice a partir del cual se comienza a acceder al array de Rica. Menos 1 porque empieza en 0
				int limiteCostas = resto;
				int aux=1;
				String dummy="";
				for (int i=indiceBase;i<rsRica.length;i++)
				{
					datos.add(campo(rsRica,"DETALLE_2_RICA",i));//Ident. Valor
					dummy=campo(rsRica,"DETALLE_1_RICA",i);
					if (dummy.length()>25)
					{
						datos.add(campo(rsRica,"DETALLE_1_RICA",i).substring(0,25));//Concepto
					}else
					{
						datos.add(campo(rsRica,"DETALLE_1_RICA",i));//Concepto
					}
					dummy=campo(rsRica,"PERIODO_VALO",i);
					if (dummy.length()>10)
					{
						datos.add(campo(rsRica,"PERIODO_VALO",i).substring(0,10));
					}
					else
					{
						datos.add(campo(rsRica,"PERIODO_VALO",i));
					}
					datos.add(DatosSalidaImpresa.toEuro(campo(rsRica,"PRINCIPAL_RV",i)));
					datos.add(DatosSalidaImpresa.toEuro(campo(rsRica,"INTERESES_RV",i)));
					datos.add(DatosSalidaImpresa.toEuro(campo(rsRica,"INGRESOS_RV",i)));
					dummy = campo(rsRica,"OBJ_TRIB_VALO",i);
					if (dummy.length()>45)
					{
						datos.add(campo(rsRica,"OBJ_TRIB_VALO",i).substring(0,45));  
					}
					else
					{
						datos.add(campo(rsRica,"OBJ_TRIB_VALO",i));
					}
					datos.add(campo(rsRica,"NUM_FIJO_VALO",i) +"/"+campo(rsRica,"ANYO_CARGO_CARG",i));
					datos.add(campoFecha(rsRica,"FECHA_FIN_VOLU",i));
					datos.add(DatosSalidaImpresa.toEuro(campo(rsRica,"RECARGO_RV",i)));
					datos.add(DatosSalidaImpresa.toEuro(campo(rsRica,"COSTAS_RV",i)));
					datos.add(DatosSalidaImpresa.toEuro(campo(rsRica,"IMPORTE_VALO",i)));
					
					if (costasExpediente !=0 && aux==limiteCostas)
					{
						datos.add("Costas de expediente de ejecutiva: ");
						datos.add(DatosSalidaImpresa.toEuro(String.valueOf(campo(rsRece,"TOTAL_RECE"))));
						costasExpediente =0;
					}
					else
					{
						datos.add("");
						datos.add("");
					}
					aux++;
				}
				String []datosArr = (String [])datos.toArray(new String[datos.size()]);
				s.Fila("LinDetalle",datosArr,20);
				//s.Campo("numPagDetalle","Página "+ (tot+1) +" de "+ (tot+1));
				int muevelinea = resto;
				
				int cont=0;
				for (int i=1;i<=numpag;i++)
				{
					s.NuevaPagina("valores");
					datos = new ArrayList<String>();
					s.Campo("numPagDetalle","Página "+ ((tot+1)-i) +" de "+ (tot+1));
					indiceBase=limite-resto-(i*numValores);
					int indice=indiceBase;
					cont=0;
					for (int k=1;k<muevelinea;k++)
					{
						s.BorrarCampo("LinDetalle");
					}
					
					while (cont< numValores)
					{
						datos.add(campo(rsRica,"DETALLE_2_RICA", indice));//Ident. Valor
						dummy = campo(rsRica,"DETALLE_1_RICA",indice);
						if (dummy.length()>25)
						{
							datos.add(campo(rsRica,"DETALLE_1_RICA",indice).substring(0,25));//Concepto
						}
						else
						{
							datos.add(campo(rsRica,"DETALLE_1_RICA",indice));//Concepto
						}
						dummy = campo(rsRica,"PERIODO_VALO",indice);
						if (dummy.length()>10)
						{
							datos.add(campo(rsRica,"PERIODO_VALO",indice).substring(0,10));
						}
						else
						{
							datos.add(campo(rsRica,"PERIODO_VALO",indice));
						}
						datos.add(DatosSalidaImpresa.toEuro(campo(rsRica,"PRINCIPAL_RV",indice)));
						datos.add(DatosSalidaImpresa.toEuro(campo(rsRica,"INTERESES_RV",indice)));
						datos.add(DatosSalidaImpresa.toEuro(campo(rsRica,"INGRESOS_RV",indice)));
						dummy = campo(rsRica,"OBJ_TRIB_VALO",indice);
						if (dummy.length()>45)
						{
							datos.add(campo(rsRica,"OBJ_TRIB_VALO",indice).substring(0,45));
						}
						else
						{
							datos.add(campo(rsRica,"OBJ_TRIB_VALO",indice));
						}
						datos.add(campo(rsRica,"NUM_FIJO_VALO",indice) +"/"+campo(rsRica,"ANYO_CARGO_CARG",indice));
						datos.add(campoFecha(rsRica,"FECHA_FIN_VOLU",indice));
						datos.add(DatosSalidaImpresa.toEuro(campo(rsRica,"RECARGO_RV",indice)));
						datos.add(DatosSalidaImpresa.toEuro(campo(rsRica,"COSTAS_RV",indice)));
						datos.add(DatosSalidaImpresa.toEuro(campo(rsRica,"IMPORTE_VALO",indice)));	

						datos.add("");
						datos.add("");
						indice++;
						cont++;
					}
					datosArr = (String[])datos.toArray(new String[datos.size()]);
					s.Fila("LinDetalle",datosArr,20);
					s.MueveBloque ("Detalle",String.valueOf((muevelinea-2)*(-20)));		
					muevelinea = numValores;
				}
				
				s.Mostrar();
					
			} catch (Exception e) {
				throw new CartaPagoException("Error al incluir datos en el pdf de carta de pago con referencia de grupo: "+e.getMessage(),e);
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
}
