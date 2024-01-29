package es.tributasenasturias.services.ws.archivodigital.ades;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.Handler;

import es.tributasenasturias.services.ws.archivodigital.Constantes;
import es.tributasenasturias.services.ws.archivodigital.utils.Preferencias.Preferencias;
import es.tributasenasturias.services.ws.archivodigital.utils.Preferencias.PreferenciasException;
import es.tributasenasturias.services.ws.archivodigital.utils.log.LogHandler.ClientLogHandler;
import es.tributasenasturias.servicios.firmasades.AlgoritmoHashType;
import es.tributasenasturias.servicios.firmasades.DatosFirmaPadesType;
import es.tributasenasturias.servicios.firmasades.DocumentoPadesInType;
import es.tributasenasturias.servicios.firmasades.DocumentoPadesOutType;
import es.tributasenasturias.servicios.firmasades.FirmasAdes;
import es.tributasenasturias.servicios.firmasades.FirmasAdes_Service;
import es.tributasenasturias.servicios.firmasades.FormatoPadesType;
import es.tributasenasturias.servicios.firmasades.PoliticaFirmaPadesType;
import es.tributasenasturias.servicios.firmasades.ResultadoType;

/**
 * Clase que aplica la firma Pades a un PDF
 * @author crubencvs
 *
 */
public class PadesSigner {
	private Preferencias pref;
	private String idLlamada;
	private FirmasAdes port;
	private PadesSigner(Preferencias preferencias, String idLlamada){
		this.pref= preferencias;
		this.idLlamada= idLlamada;
	}
	/**
	 * Inicializa la interfaz mediante la que se llamará al servicio
	 */
	@SuppressWarnings({"unchecked" })
	private final void inicializaInterfaz(){
		FirmasAdes_Service srv = new FirmasAdes_Service();
		port= srv.getFirmasAdesSOAP();
		String endpoint= pref.getEndpointFirmasAdes();
		BindingProvider bprovider= (BindingProvider) port;
		if (!"".equals(endpoint)) {
			bprovider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
		}
		Binding bi= bprovider.getBinding();
		List<Handler> manejadores= bi.getHandlerChain();
		if (manejadores==null){
			manejadores= new ArrayList<Handler>();
		}
		manejadores.add(new ClientLogHandler());
		//Ponemos en contexto los objetos de la llamada
		bprovider.getRequestContext().put(Constantes.ID_LLAMADA, this.idLlamada);
		bprovider.getRequestContext().put(Constantes.PREFERENCIAS, this.pref);
		bi.setHandlerChain(manejadores);
	}
	public static  PadesSigner newInstance(Preferencias pref, String idLlamada) {
		return new PadesSigner(pref, idLlamada);
	}
	
	/**
	 * Realiza la firma Pades. Los parámetros de firma los tomará de preferencias.
	 * @param contenido Contenido del documento a firmar
	 * @return contenido firmado del documento
	 * @throws FirmaException en caso de producirse un error en la firma.
	 */
	/* INIPETITRIBUTAS-24 ENAVARRO ** 08/04/2020 ** Firma de sello de tiempo */
	//public final byte[] firmar (byte[] contenido, FormatoPadesType formato) throws FirmaException{
	public final byte[] firmar (byte[] contenido, FormatoPadesType formato, boolean firmar) throws FirmaException{
		/* FINPETITRIBUTAS-24 ENAVARRO ** 08/04/2020 ** Firma de sello de tiempo */
		inicializaInterfaz();
		//Recuperamos la parametrización.
		Properties propiedades=null;
		try{
			propiedades= pref.getPreferenciasPadesENI();
		} catch (PreferenciasException pe) {
			throw new FirmaException ("No se pueden recuperar las propiedades de firma PAdES.");
		}
		DocumentoPadesInType documentoPades= new DocumentoPadesInType();
		documentoPades.setBase64Pdf(contenido);
		DatosFirmaPadesType datosFirma= new DatosFirmaPadesType();
		datosFirma.setAlgoritmoHash(AlgoritmoHashType.fromValue(propiedades.getProperty("algoritmoHash")));
		datosFirma.setFormatoPades(formato);
		PoliticaFirmaPadesType politicaFirma=null;
		String policyIdentifier= propiedades.getProperty("policyIdentifier");
		String policyIdentifierHash= propiedades.getProperty("policyIdentifierHash");
		String policyIdentifierHashAlgorithm= propiedades.getProperty("policyIdentifierHashAlgorithm");
		String policyQualifier= propiedades.getProperty("policyQualifier");
		//Si una cualquiera de estas propiedades existe, intentamos nivel EPES.
		//Si no están todas las necesarias fallará, pero será por problema en configuración y mejor eso que el que se crea que 
		//se está firmando EPES y no se esté haciendo.
		if (policyIdentifier!=null || 
			policyIdentifierHash!=null ||
			policyIdentifierHashAlgorithm!=null||
			policyQualifier!=null) {
			politicaFirma=new PoliticaFirmaPadesType();
			politicaFirma.setPolicyIdentifier(policyIdentifier);
			politicaFirma.setPolicyIdentifierHash(policyIdentifierHash);
			politicaFirma.setPolicyIdentifierHashAlgorithm(policyIdentifierHashAlgorithm);
			politicaFirma.setPolicyQualifier(policyQualifier);
		}
		datosFirma.setPoliticaFirma(politicaFirma); 
		Holder<ResultadoType> resultado = new Holder<ResultadoType>();
		Holder<DocumentoPadesOutType> documentoResultado= new Holder<DocumentoPadesOutType>();
		/* INIPETITRIBUTAS-24 ENAVARRO ** 02/04/2020 ** Firma de sello de tiempo */
		if(!firmar) {
			port.upgradeFirmaPades(documentoPades, datosFirma, resultado, documentoResultado);
		}
		else {
		/* FINPETITRIBUTAS-24 ENAVARRO ** 02/04/2020 ** Firma de sello de tiempo */
			port.firmaPades(documentoPades, datosFirma, resultado, documentoResultado);
		/* INIPETITRIBUTAS-24 ENAVARRO ** 02/04/2020 ** Firma de sello de tiempo */
		}
		/* FINPETITRIBUTAS-24 ENAVARRO ** 02/04/2020 ** Firma de sello de tiempo */
		if (resultado!=null && resultado.value!=null) {
			if (!resultado.value.isEsError()){
				if (documentoResultado!=null && documentoResultado.value!=null){
					return documentoResultado.value.getBase64Pdf();
				} 
				else {
					throw new FirmaException ("No se ha recibido un documento firmado.");
				}
			}
			else {
				throw new FirmaException("Error recibido en la firma:" + resultado.value.getCodigo() + "--:" + resultado.value.getDescripcion());
			}
		} else {
			throw new FirmaException ("No se ha recibido resultado de la llamada a firma.");
		}
	}
}
