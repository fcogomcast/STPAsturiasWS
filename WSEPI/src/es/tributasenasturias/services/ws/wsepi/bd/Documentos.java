package es.tributasenasturias.services.ws.wsepi.bd;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.Handler;

import stpa.services.LanzaPL;
import stpa.services.LanzaPLService;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.ArchivoDigital;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.ArchivoDigital_Service;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.CSVType;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.CaracteristicasCSVType;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.CaracteristicasFisicasCSVType;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.CertificadoType;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.FirmaType;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.PaginaCSVType;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.PaginasCSVType;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.PosicionCSVType;
import es.tributasenasturias.services.ws.wsepi.Constantes;
import es.tributasenasturias.services.ws.wsepi.utils.ConversorParametrosLanzador;
import es.tributasenasturias.services.ws.wsepi.utils.log.Logger;
import es.tributasenasturias.services.ws.wsepi.utils.log.LogHandler.ClientLogHandler;
import es.tributasenasturias.services.ws.wsepi.utils.log.Preferencias.Preferencias;

public class Documentos {

	private ConversorParametrosLanzador cpl;
	private Preferencias preferencias;
	private Logger logger = null;
	private String idLlamada;
	private static class DatosFirma {
		private boolean firmaCSV;
		private String firmante;
		
		public DatosFirma(String firmaCSV, String firmante) {
			this.firmaCSV= "S".equalsIgnoreCase(firmaCSV)?true:false;
			this.firmante= firmante;
		}
		public boolean isFirmaCSV() {
			return firmaCSV;
		}
		public void setFirmaCSV(boolean firmaCSV) {
			this.firmaCSV = firmaCSV;
		}
		public String getFirmante() {
			return firmante;
		}
		public void setFirmante(String firmante) {
			this.firmante = firmante;
		}
	}
	
	public Documentos(Preferencias pref, Logger log, String idLlamada) {
		this.preferencias= pref;
		this.logger = log;
		this.idLlamada = idLlamada;
	}
	/**
	 * Establece los manejadores para la llamada a servicio remoto
	 * @param bpr BindingProvider del  puerto de servicio web
	 */
	@SuppressWarnings("unchecked")
	private void setHandler(javax.xml.ws.BindingProvider bpr) {
		Binding b= bpr.getBinding();
		List<Handler>handlers= b.getHandlerChain();
		if (handlers==null){
			handlers= new ArrayList<Handler>();
		}
		handlers.add(new ClientLogHandler());
		//Ponemos en contexto los objetos de la llamada
		bpr.getRequestContext().put(Constantes.ID_LLAMADA, this.idLlamada);
		bpr.getRequestContext().put(Constantes.PREFERENCIAS, this.preferencias);
		b.setHandlerChain(handlers);
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
	
	@SuppressWarnings("unused")
	private static String getHashCode(byte[] cadena)
	{
	    String sha1 = "";
	    try
	    {
	        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	        crypt.reset();
	        crypt.update(cadena);
	        sha1 = byteToHex(crypt.digest());
	    }
	    catch(NoSuchAlgorithmException e)
	    {
	        e.printStackTrace();
	    }

	    return sha1;
	}
	
	private DatosFirma recuperaParametrosFirma (String idGdre) throws Exception {
		if ("".equals(idGdre) || idGdre==null) {
			return new DatosFirma("N",null);
		}
		LanzaPLService lanzaderaWS = new LanzaPLService();
    	LanzaPL lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
    	BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
		bpr.getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,preferencias.getEndPointISL());
		setHandler(bpr);
		cpl = new ConversorParametrosLanzador();
		cpl.setProcedimientoAlmacenado(preferencias.getParametrosFirma());
		cpl.setParametro(idGdre, ConversorParametrosLanzador.TIPOS.String); 
		cpl.setParametro("P", ConversorParametrosLanzador.TIPOS.String); 

		String resultadoEjecutarPL=lanzaderaPort.executePL(preferencias.getEsquemaBaseDatos(), cpl.Codifica(),preferencias.getIPLanzador(), "", "", "");
		cpl.setResultado(resultadoEjecutarPL);
		String error = cpl.getNodoResultado("ERROR");
		if (error!=null && !"".equals(error)) {
			throw new Exception ("Error al recuperar parámetros de la firma del reimprimible:" + error);
		}
		String firmaCSV= cpl.getNodoResultadoX("STRING1_CANU");
		String firmante= cpl.getNodoResultadoX("STRING2_CANU");
		DatosFirma df= new DatosFirma(firmaCSV, firmante);
		return df;
	}
	/**
	 * Conversión de los parámetros de firma del servicio a los que han de enviarse en el cliente a Archivo Digital
	 * @param parametrosFirma
	 * @return
	 */
	private FirmaType convertirParametrosFirma(es.tributasenasturias.services.ws.wsepi.FirmaType parametrosFirma){
		if (parametrosFirma==null){
			return null;
		}
		FirmaType firma=new FirmaType();
		CSVType csv= null;
		if (parametrosFirma.getCSV()!=null){
			csv= new CSVType();
			csv.setFirmaCSV(parametrosFirma.getCSV().getFirmaCSV());
			csv.setFirmante(parametrosFirma.getCSV().getFirmante());
			CaracteristicasCSVType cars= null;
			if (parametrosFirma.getCSV().getCaracteristicas()!=null){
				cars= new CaracteristicasCSVType();
				es.tributasenasturias.services.ws.wsepi.CaracteristicasFisicasCSVType paramFisicas= parametrosFirma.getCSV().getCaracteristicas().getFisicas();
				CaracteristicasFisicasCSVType fisicas=null;
				if (paramFisicas!=null){
					fisicas= new CaracteristicasFisicasCSVType();
					PaginasCSVType paginas=null;
					if (paramFisicas.getPaginas()!=null){
						paginas= new PaginasCSVType();
						for (es.tributasenasturias.services.ws.wsepi.PaginaCSVType p: paramFisicas.getPaginas().getPagina()){
							PaginaCSVType pagina= new PaginaCSVType();
							pagina.setNumero(p.getNumero());
							paginas.getPagina().add(pagina);
						}
					}
					fisicas.setPaginas(paginas);
					PosicionCSVType posicion=null;
					if (paramFisicas.getPosicion()!= null){
						posicion= new PosicionCSVType();
						posicion.setX(paramFisicas.getPosicion().getX());
						posicion.setY(paramFisicas.getPosicion().getY());
					}
					fisicas.setPosicion(posicion);
				}
				cars.setFisicas(fisicas);
			}
			csv.setCaracteristicas(cars);
		}
		firma.setCSV(csv);
		CertificadoType certificado=null;
		if (parametrosFirma.getCertificado()!=null){
			certificado= new CertificadoType();
			certificado.setFirmaCertificado(parametrosFirma.getCertificado().getFirmaCertificado());
			certificado.setLTV(parametrosFirma.getCertificado().getLTV());
		}
		firma.setCertificado(certificado);
		return firma;
	}
	public void CustodiaArchivo(
							String codigoUsuario, 
							String nombreFichero, 
							String tipoElemento, 
							byte[] archivo, 
							String idGdre, 
							String hash,
							//CRUBENCVS 43599. Este parámetro ya no tiene sentido, porque ahora se utilizará 
							//el de "firmaType"
							/* INIPETITRIBUTAS-30 ENAVARRO ** 21/04/2021 ** Buzon de notificaciones */
							//String custodiarCsv, 
							/* FINPETITRIBUTAS-30 ENAVARRO ** 21/04/2021 ** Buzon de notificaciones */
							es.tributasenasturias.services.ws.wsepi.FirmaType parametrosFirma,
							//FIN CRUBENCVS 43599.
							Holder<Integer> idAdar, Holder<String> datosArchivo, Holder<String> error)
	{
		// 1. Custodiamos el documento y obtenemos un IDADAR                         
        try
        {
        		Holder<String> csv = new Holder<String>();
        		String metadatos = "";
        		String comprimir = "N";
        		//Recuperamos los parámetros de firma del archivo
        		DatosFirma d= recuperaParametrosFirma(idGdre);
        		FirmaType firmaType= new FirmaType();
        		CSVType csvType= new CSVType();
        		//CRUBENCVS 43599. A partir de ahora, el parámetro de custodiarCsv ya no tendrá sentido,
        		//se pasarán los parámetros de firma en su totalidad.
        		//Sin embargo, dejamos el default de  "isFirmaCSV",  porque así, mientras se cambian las llamadas
        		//a WSEPI, las providencias de apremio, que utilizan eso, seguirán funcionando igual.
        		/* INIPETITRIBUTAS-30 ENAVARRO ** 21/04/2021 ** Buzon de notificaciones */
        		//String firmaCSV=d.isFirmaCSV()?"S":"N";
        		/*String firmaCSV="";
        		if(custodiarCsv != null && !custodiarCsv.isEmpty()){
        			firmaCSV=custodiarCsv;
        		}
        		else {
        			firmaCSV=d.isFirmaCSV()?"S":"N";
        		}*/
        		/* FINPETITRIBUTAS-30 ENAVARRO ** 21/04/2021 ** Buzon de notificaciones */
        		String firmaCSV="";
        		if (parametrosFirma!=null){
        			firmaType= convertirParametrosFirma(parametrosFirma);
        			if (firmaType==null){
        				firmaCSV= d.isFirmaCSV()?"S":"N";
        				String firmante=d.getFirmante();
                		csvType.setFirmaCSV(firmaCSV);
                		csvType.setFirmante(firmante);
                		firmaType.setCSV(csvType);
        			}
        		}else {
        			firmaCSV= d.isFirmaCSV()?"S":"N";
        			String firmante=d.getFirmante();
            		csvType.setFirmaCSV(firmaCSV);
            		csvType.setFirmante(firmante);
            		firmaType.setCSV(csvType);
        		}
        		//FIN CRUBENCVS 43599
        		ArchivoDigital_Service ads = new ArchivoDigital_Service();
        		ArchivoDigital ad = ads.getArchivoDigitalSOAP();
        		BindingProvider bpr = (javax.xml.ws.BindingProvider) ad;
				bpr.getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,preferencias.getEndpointArchivoDigital());
				setHandler(bpr);
				/* INIPETITRIBUTAS-30 ENAVARRO ** 21/04/2021 ** Buzon de notificaciones */
        		//ad.custodia(codigoUsuario, idGdre, tipoElemento, nombreFichero, comprimir, archivo, hash, metadatos, firmaType,idAdar, csv, error);
				Holder<String> hashResultado = new Holder<String>();
				ad.custodia(codigoUsuario, idGdre, tipoElemento, nombreFichero, comprimir, archivo, hash, metadatos, firmaType,idAdar, csv, hashResultado, error);
        		/* FINPETITRIBUTAS-30 ENAVARRO ** 21/04/2021 ** Buzon de notificaciones */
				
        		if (idAdar.value == null) idAdar.value = 0;
				
				if (error.value != null)
				{
		            return;
				}
                // 2. Asociamos el id_adar a la reimpresión
                if (idAdar.value != 0)
                {		
                	LanzaPLService lanzaderaWS = new LanzaPLService();
                	LanzaPL lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
                	bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
        			bpr.getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,preferencias.getEndPointISL());
        			setHandler(bpr);
        			cpl = new ConversorParametrosLanzador();
        			cpl.setProcedimientoAlmacenado(preferencias.getNomProcedimientoActualizarIdAdar());
   					cpl.setParametro(codigoUsuario, ConversorParametrosLanzador.TIPOS.String); // usuario
        			cpl.setParametro("33", ConversorParametrosLanzador.TIPOS.Integer); // organismo
        			cpl.setParametro(idGdre, ConversorParametrosLanzador.TIPOS.Integer); //ideper
        			cpl.setParametro(idAdar.value.toString(), ConversorParametrosLanzador.TIPOS.Integer); // idadar
        			/* INIPETITRIBUTAS-30 ENAVARRO ** 21/04/2021 ** Buzon de notificaciones */
        			//cpl.setParametro(hash, ConversorParametrosLanzador.TIPOS.String); // hash   
        			cpl.setParametro(hashResultado.value, ConversorParametrosLanzador.TIPOS.String); // hash 
        			/* FINPETITRIBUTAS-30 ENAVARRO ** 21/04/2021 ** Buzon de notificaciones */
        			cpl.setParametro(csv.value, ConversorParametrosLanzador.TIPOS.String); // csv
        			/* INIPETITRIBUTAS-101 ENAVARRO ** 29/07/2020 ** Dividir las escrituras públicas */
        			cpl.setParametro(tipoElemento, ConversorParametrosLanzador.TIPOS.String); // tipoElemento
        			/* FINPETITRIBUTAS-101 ENAVARRO ** 29/07/2020 ** Dividir las escrituras públicas */
        			
        			String resultadoEjecutarPL=lanzaderaPort.executePL(preferencias.getEsquemaBaseDatos(), cpl.Codifica(),preferencias.getIPLanzador(), "", "", "");
            		//ParsearResultado(resultadoEjecutarPL);
            		error.value = "";
                }
                else
                {
                    error.value = "Error al custodiar el documento";
                }
        }
        catch (Exception ex)
        {

            error.value = "Error al custodiar el documento" + ex.getMessage();
        }
	}
	
	
	public void EjecutarCallback(int idadar, int ideepi, int idgdre,
			Holder<Integer> idArchivo, Holder<String> datosArchivo,
			Holder<String> error)
	{
                   
        try
        {
        		LanzaPLService lanzaderaWS = new LanzaPLService();
            	LanzaPL lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
            	javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
    			bpr.getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,preferencias.getEndPointISL());
    			setHandler(bpr);
    			cpl = new ConversorParametrosLanzador();
    			cpl.setProcedimientoAlmacenado(preferencias.getNomProcedimientoEjecutarCallback());
				cpl.setParametro("SYS", ConversorParametrosLanzador.TIPOS.String); 
    			cpl.setParametro("33", ConversorParametrosLanzador.TIPOS.Integer); 
    			cpl.setParametro(String.valueOf(idgdre), ConversorParametrosLanzador.TIPOS.Integer);
    			cpl.setParametro(String.valueOf(idadar), ConversorParametrosLanzador.TIPOS.Integer); 
    			cpl.setParametro(String.valueOf(ideepi), ConversorParametrosLanzador.TIPOS.Integer); 

    			String resultadoEjecutarPL=lanzaderaPort.executePL(preferencias.getEsquemaBaseDatos(), cpl.Codifica(),preferencias.getIPLanzador(), "", "", "");
        		//ParsearResultado(resultadoEjecutarPL);
        		//if (resultado.getCodresultado().isEmpty()) resultado.setCodresultado(preferencias.getCodResultadoOK());
          
        }
        catch (Exception ex)
        {

            error.value = "Error al custodiar el documento" + ex.getMessage();
        }
	}
	
}