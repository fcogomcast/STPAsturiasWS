
package es.tributasenasturias.webservice;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import es.tributasenasturias.business.Cifrar;
import es.tributasenasturias.business.ComponerRespuesta;
import es.tributasenasturias.business.Descifrar;
import es.tributasenasturias.business.IntegraTributas;
import es.tributasenasturias.soap.handler.NumerosAutoliquidacionHandler;
import es.tributasenasturias.utilsProgramaAyuda.Logger;
import es.tributasenasturias.utilsProgramaAyuda.Preferencias;
import es.tributasenasturias.docs.AltaDocumento;
import es.tributasenasturias.docs.DatosInterviniente;
//(portName = "wsNumerosAutoliquidacion", serviceName = "NumerosAutoliquidacion", targetNamespace = "http://webservices.numerosautoliquidacion.tributasenasturias.es/", wsdlLocation = "/wsdls/pa_wsNAutoliquidacion.wsdl", endpointInterface = "es.tributasenasturias.webservice.NumerosAutoliquidacion")
@WebService 
@HandlerChain (file="HandlerChain.xml")
public class NumerosAutoliquidacionImpl
    implements NumerosAutoliquidacion
{
	
	@Resource
	WebServiceContext ws;
	private Preferencias pref = new Preferencias();
	
    public NumerosAutoliquidacionImpl() {
    	//de este modo, al instalar el webservice, se creara el fichero de preferencias si no existe
        pref.CompruebaFicheroPreferencias();
    }

    /**
     * getNumerosAutoliquidacion realiza la integración en tributas del xml de entrada recibido de los programas de ayuda.
     *
     * @param datos - Requiere el contenido de un XML con los datos de las declaraciones del programa de ayuda
     * @return Devuelve un xml el resultado de la integración de las declaraciones recibidas en tributas.
     */
    @WebMethod
    public String getNumerosAutoliquidacion(@WebParam(name = "datos") String datos) {
    	String v_numAutoliquidacion = "";
    	try
        {
    		//Recuperamos el valor de IP cliente, si existiera. 
    		//Si existe, sabemos además que es una llamada de cita previa
    		String ip = (String)ws.getMessageContext().get(NumerosAutoliquidacionHandler.IP_KEY);
    		//
			//cargamos los datos del almacen de un fichero xml preferencias
			pref.CargarPreferencias();
               	    
            //Se comprueban los datos de entrada
            if(datos == null || datos.length() == 0)
            {
                throw new Exception("El campo xmlData es obligatorio.");
            }
            // Se descifra la entrada
            Descifrar descifrar = new Descifrar(datos);
            String xmlDataDescifrada = descifrar.getResultado();
            
            if (descifrar.getCodResultado() == 1) {
            	Logger.error("Error al descifrar la entrada de datos. "+xmlDataDescifrada);          	
            	return ComponerRespuesta.getRespuestaCifrada(v_numAutoliquidacion, "Error al descifrar la entrada de datos. "+xmlDataDescifrada);
            }
            // Se realiza la llamada al procedimiento almacenado para integrar la petición en las tablas temporales de tributas.
            IntegraTributas it = new IntegraTributas();
            it.setDatosXml(xmlDataDescifrada);
            it.setIp(ip);
            String resultadoIntegracion = new String();
            boolean resultadoOK = it.Ejecuta();
            resultadoIntegracion = it.getResultado();
            //Logger.info(it.getNumAutoliquidacion());
            v_numAutoliquidacion = it.getNumAutoliquidacion();
            
            //Cambiamos el número de autoliquidación en el XML.
            it.setNumAutoliquidacion(v_numAutoliquidacion);
            
            // Si el resultado NO es ok, se retorna el resultado y no se continua con el pago.
            if ((!resultadoOK) || (v_numAutoliquidacion.isEmpty())) {
            	Logger.error("Error al integrar los datos en tributas. "+resultadoIntegracion);
            	return ComponerRespuesta.getRespuestaCifrada(v_numAutoliquidacion, "Error al integrar los datos en tributas. "+it.getError());
            }
        	                                   
            //Se da de alta el XML en Doin.                                  
            try {	                   	
            
            	if (pref.getDebug().equals("1")) {
	            	Logger.debug ("ALTA DE DOCUMENTO XML EN DOIN:");
	            	Logger.debug ("RESPUESTA:"+xmlDataDescifrada);
            	}
            	            	                        	               
            	//Obtenemos los datos de los intervinientes.
            	DatosInterviniente datosInter = new DatosInterviniente (xmlDataDescifrada); 

            	//Damos de alta el documento.
            	AltaDocumento ad = new AltaDocumento();				          

            	//Quitamos retornos de carro y tabuladores.
            	String datosXml = new String(it.getDatosXml().replace("\r\n", "").replace("\n", ""));            	            
            	//Llamamos metodo para dar de alta documento.            	
            	ad.setDocumento("M", v_numAutoliquidacion , null,datosInter.getNifSP() , datosInter.getNifPr(),datosXml ,"XML");
	            
            } catch (Exception e) {
            	Logger.error("Error al almacenar el documento.");            	
            }
                       
            if (pref.getDebug().equals("1")) 
            	Logger.debug ("ESTO ES LO QUE CIFRO:"+ComponerRespuesta.getRespuestaCompuesta(v_numAutoliquidacion, ""));                                                                      
            
            // Se cifra la salida
            Cifrar cifrar = new Cifrar(ComponerRespuesta.getRespuestaCompuesta(v_numAutoliquidacion, ""));            
            String xmlDataCifrada = cifrar.getResultado();
            
            if (pref.getDebug().equals("1")) 
            	Logger.debug ("LOS DATOS CIFRADOS:"+xmlDataCifrada);
                        
            if (cifrar.getCodResultado() == 1) {
            	Logger.error("Error al cifrar la salida. "+xmlDataCifrada);
            	return ComponerRespuesta.getRespuestaCifrada(v_numAutoliquidacion, "Error al cifrar la salida. "+xmlDataCifrada);
            }
                                                                        	                    
            return xmlDataCifrada;
        }
        catch (Exception e)
        {
        	if (pref.getDebug().equals("1")) {
        		Logger.error("error:".concat(e.getMessage()));
        	}
            return ComponerRespuesta.getRespuestaCifrada(v_numAutoliquidacion, "error:".concat(e.getMessage()));
        }
    }

        
    
}
