package es.tributasenasturias.docs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.tributasenasturias.documentos.util.XMLUtils;

import es.tributasenasturias.pagopresentacionmodelo600utils.ConversorParametrosLanzador;
import es.tributasenasturias.pagopresentacionmodelo600utils.PdfComprimidoUtils;
import es.tributasenasturias.pagopresentacionmodelo600utils.Preferencias;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPL;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLService;
import es.tributasenasturias.webservice.pagopresentacion.log.ILoggable;
import es.tributasenasturias.webservice.pagopresentacion.log.LogHelper;

/**
 * Implementa una utilidad para recuperar un documento de la base de datos, de esta forma se podrá 
 * comprobar si ya está generado y traerlo en lugar de generarlo de nuevo.
 * @author crubencvs
 *
 */
public class DocumentoDoin implements ILoggable{

	private String numeroAutoliquidacion = new String();
	private String docRecuperado = "";
	private boolean generado=false;
	private ConversorParametrosLanzador cpl;
	//Log.
	private LogHelper log; //Se instanciará desde la factory que crea estos objetos.
	
	public enum TipoDoc {PAGO,XML,COMPARECENCIA,PRESENTACION,MODELO}
	
	private Preferencias pref = new Preferencias();
	
	
	protected DocumentoDoin(String p_numAutoliq) throws Exception{
			pref.CargarPreferencias();			
			this.numeroAutoliquidacion = p_numAutoliq;
			
	}
	
	public String getDocumento()
	{
		return docRecuperado;
	}
	public boolean isGenerado()
	{
		return generado;
	}
	
	public void recuperarDocumento(TipoDoc tipoDoc) throws Exception
	{
		String numAutoliquidacion = this.numeroAutoliquidacion;
		recuperarDocumentoInsertado(tipoDoc,numAutoliquidacion);
		String docRetorna="";
		if (!cpl.getResultado().equals(null)) {
			// una vez obtenidos los datos y comprobado que la respuesta no es nula
			// se debe inluir el xml con los datos en la base de datos zipped
			// y se pinta el pdf.
				//Debería haber recuperado el resultado.
				try {
					Document docRespuesta = (Document) XMLUtils.compilaXMLObject(cpl.getResultado(), null);
					//Comprobamos si hay error. En ese caso, no habrá documento.
					Element[] rsError = XMLUtils.selectNodes(docRespuesta, "//estructuras/error");
					try {
						if (rsError.length==0) //OK
						{   
							//Recuperamos el documento.
							//Si no termina como "00" o "01", es un error.
							String codError=XMLUtils.selectSingleNode(docRespuesta, "//estruc[@nombre='CADE_CADENA']/fila[1]/STRING_CADE").getTextContent();
							if (!codError.equals("00") && !codError.equals("01"))
							{
								//Error al recuperar el documento.
								log.error("La recuperación del documento de la base de datos ha terminado con error error en SQL");
								throw new Exception ("Imposible comprobar si el documento ya estaba generado");
								
							}
							else if (codError.equals("01"))
							{
								//No existe, se vuelve.
								this.generado=false;
							}
							else
							{
								Element[] rsDoc = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='PDF_PDF']/fila");
								String doc=XMLUtils.selectSingleNode(rsDoc[0], "pdf").getTextContent();//Sólo esperamos uno, si hay más no importan.
								//Se decodifica el documento. 
								if (necesitaDecodificacion(tipoDoc))
								{
									String documentzippeado = doc;
									ByteArrayOutputStream resulByteArray = new ByteArrayOutputStream();
									resulByteArray = PdfComprimidoUtils.descomprimirPDF(documentzippeado);
									docRetorna = new String(Base64.encode(resulByteArray.toByteArray()));
								}
								else
								{
									docRetorna=doc;
								}
								this.generado=true;
							}
						}
						else // Parece que hay errores
						{
							//Si hay errores, logeamos el resultado y lanzamos la excepción.
							throw new Exception ("La recuperación del documento de la base de datos ha terminado con error:" + XMLUtils.selectSingleNode(docRespuesta, "//estructuras/error").getTextContent());
						}
					} catch (DOMException e) {
						throw new Exception ("Error en XML al recuperar el documento de la base de datos:" + e.getMessage(),e);
						
					} catch (IOException e) {
						throw new Exception ("Error de Entrada/salida al recuperar el documento de la base de datos:" + e.getMessage(),e);
					}
				} catch (RemoteException e) {
					//Esto puede ser por varios motivos, por ejemplo que no haya devuelto XML, como en
					//"No ejecutado ok".
					//En este caso, se genera un nuevo error. 
					throw new Exception ("Error general al recuperar el documento de la base de datos:" + e.getMessage());
					
				}
		}
		this.docRecuperado=docRetorna;
	}
	
	private void recuperarDocumentoInsertado (TipoDoc tipoDoc,String numAutoliquidacion) throws Exception{
		String tipo="";
		String extension="";
		switch (tipoDoc)
		{
		case PAGO:
			tipo="P";
			extension="PDF";
			break;
		case XML:
			tipo="M";
			extension="XML";
			break;
		case COMPARECENCIA:
			tipo="J";
			extension="PDF";
			break;
		case PRESENTACION:
			tipo="C";
			extension="PDF";
			break;
		case MODELO:
			tipo="M";
			extension="PDF";
			break;
		}
		// llamar al servicio PXLanzador para ejecutar el procedimiento almacenado de recuperación
		// de documento ya insertado.
		cpl = new ConversorParametrosLanzador();

		cpl.setProcedimientoAlmacenado(pref.getPAConsultaDocumento());
        // Nombre
        cpl.setParametro(numAutoliquidacion,ConversorParametrosLanzador.TIPOS.String);
        // Tipo 
        cpl.setParametro(tipo,ConversorParametrosLanzador.TIPOS.String);
        // Extensión. 
        cpl.setParametro(extension,ConversorParametrosLanzador.TIPOS.String);
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
    	respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");
    	cpl.setResultado(respuesta);
	}
	private boolean necesitaDecodificacion(TipoDoc tipoDoc)
	{
		boolean decodifica=false;
		switch (tipoDoc)
		{
		case PAGO:
			decodifica=true;
			break;
		case XML:
			decodifica=false;
			break;
		case COMPARECENCIA:
			decodifica=true;
			break;
		case PRESENTACION:
			decodifica=true;
			break;
		case MODELO:
			decodifica=true;
			break;
		}
		return decodifica;
	}
	@Override
	public void setLogger(LogHelper log)
	{
		this.log = log;
	}
	@Override 
	public LogHelper getLogger()
	{
		return log;
	}
}
