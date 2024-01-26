package es.tributasenasturias.servicios.codigobarras;


import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;


import es.tributasenasturias.log.ILog;
import es.tributasenasturias.log.TributasLogger;
import es.tributasenasturias.servicios.codigobarras.CodigoQRImpl.DatosQR;
import es.tributasenasturias.servicios.codigobarras.preferencias.Preferencias;
import es.tributasenasturias.servicios.codigobarras.soap.SOAPUtils;

/**
 * This class was generated by the JAX-WS RI. Oracle JAX-WS 2.1.3-06/19/2008
 * 07:03 PM(bt) Generated source version: 2.1
 * 
 */
@WebService(portName = "CodigoBarrasSOAP", serviceName = "CodigoBarras", targetNamespace = "http://codigobarras.servicios.tributasenasturias.es", wsdlLocation = "/wsdls/CodigoBarras.wsdl", endpointInterface = "es.tributasenasturias.servicios.codigobarras.CodigoBarras")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@HandlerChain(file="HandlerChain.xml")
public class CodigoBarras_CodigoBarrasSOAPImpl implements CodigoBarras {

	@Resource
	WebServiceContext context;
	public CodigoBarras_CodigoBarrasSOAPImpl() {
	}

	/**
	 * 
	 * @param altoImagenPixeles
	 * @param imagenQr
	 * @param formatoImagen
	 * @param anchoImagenPixeles
	 * @param contenidoACodificar
	 * @param altoImagenPixeles0
	 */
	public void generaQR(String contenidoACodificar,
			Holder<Integer> anchoImagenPixeles, Integer altoImagenPixeles,
			Holder<String> formatoImagen, Holder<byte[]> imagenQr,
			Holder<Integer> altoImagenPixeles0, Holder<Boolean> esError,
			Holder<String> mensajeError) {
		Preferencias pref;
		String idLlamada;
		ILog log=null;
		try{
			MessageContext mc = context.getMessageContext();
			
			if (mc.containsKey(Constantes.PREFERENCIAS))
			{
				pref = (Preferencias) mc.get(Constantes.PREFERENCIAS);
			}
			else
			{
				pref = new Preferencias();
			}
			if (mc.containsKey(Constantes.IDSESION))
			{
				idLlamada= (String) mc.get(Constantes.IDSESION);
			}
			else
			{
				idLlamada= SOAPUtils.getIdLlamada();
			}
			if (mc.containsKey(Constantes.LOG))
			{
				log = (ILog) mc.get(Constantes.LOG);
			}
			else 
			{
				log = new TributasLogger(pref.getModoLog(),pref.getFicheroLogAplicacion(), idLlamada);
			}
			log.info("Inicio de la generaci�n de c�digo QR de contenido:" +
					contenidoACodificar + " y ancho de imagen " + anchoImagenPixeles.value);
			
			
			DatosQR datosQR= new CodigoQRImpl(pref).generarQR(contenidoACodificar, 
															  anchoImagenPixeles.value, 
														      altoImagenPixeles!=null?altoImagenPixeles.intValue():0, 
														      formatoImagen.value);
			
			esError.value=false;
			imagenQr.value= datosQR.getImagen();
			anchoImagenPixeles.value=datosQR.getAnchoImagen();
			altoImagenPixeles0.value=datosQR.getAltoImagen();
			formatoImagen.value= datosQR.getFormatoImagen();
			
		}  catch (Exception e){
			if (log!=null) {
				log.error ("Error inesperado:" + e.getMessage(),e);
				log.trace(e.getStackTrace());
			}
			esError.value=true;
			mensajeError.value="Error inesperado en el servicio:"+ e.getMessage();
			
		} catch (Throwable e){
			if (log!=null) {
				log.error ("Error inesperado:" + e.getMessage(),e);
				log.trace(e.getStackTrace());
			}
			esError.value=true;
			mensajeError.value="Error inesperado en el servicio:"+ e.getMessage();
			
		} finally{
			if (log!=null) {
				log.info("Fin de la generaci�n de c�digo QR");
			}
		}
		return;
	}

    /**
	 * 
	 * @param datosQr
	 * @param textoEquivalenteQr
	 * @param altoImagenPixeles
	 * @param imagenQr
	 * @param formatoImagen
	 * @param anchoImagenPixeles
	 * @param esError
	 * @param tipoQr
	 * @param mensajeError
	 * @param altoImagenPixeles0
	 */
	public void generaQRPorTipo(String tipoQr, String datosQr,
			Holder<Integer> anchoImagenPixeles, Integer altoImagenPixeles,
			Holder<String> formatoImagen, Holder<byte[]> imagenQr,
			Holder<Integer> altoImagenPixeles0,
			Holder<String> textoEquivalenteQr, Holder<Boolean> esError,
			Holder<String> mensajeError) {
		Preferencias pref;
		String idLlamada;
		ILog log=null;
		try{
			MessageContext mc = context.getMessageContext();
			
			if (mc.containsKey(Constantes.PREFERENCIAS))
			{
				pref = (Preferencias) mc.get(Constantes.PREFERENCIAS);
			}
			else
			{
				pref = new Preferencias();
			}
			if (mc.containsKey(Constantes.IDSESION))
			{
				idLlamada= (String) mc.get(Constantes.IDSESION);
			}
			else
			{
				idLlamada= SOAPUtils.getIdLlamada();
			}
			if (mc.containsKey(Constantes.LOG))
			{
				log = (ILog) mc.get(Constantes.LOG);
			}
			else 
			{
				log = new TributasLogger(pref.getModoLog(),pref.getFicheroLogAplicacion(), idLlamada);
			}
			log.info("Inicio de la generaci�n de c�digo QR para el tipo : " + tipoQr +" con datos:" +
					datosQr + " y ancho de imagen " + anchoImagenPixeles.value);
			
			//Tipos admitidos.
			//Por el momento, s�lo pago con referencia, no merece la pena
			//crear una clase de construcci�n para esto.
			String contenidoACodificar="";
			if ("PAGO_REF".equalsIgnoreCase(tipoQr)){
				String url= pref.getUrlPagoReferenciaQR();
				if (url==null  || "".equals(url)){
					throw new Exception ("Error en par�metros, no se puede encontrar la URL de pago por referencia");
				}
				contenidoACodificar= pref.getUrlPagoReferenciaQR()+datosQr;
			}
			
			DatosQR datosQR= new CodigoQRImpl(pref).generarQR(contenidoACodificar, 
															  anchoImagenPixeles.value, 
														      altoImagenPixeles!=null?altoImagenPixeles.intValue():0, 
														      formatoImagen.value);
			
			esError.value=false;
			imagenQr.value= datosQR.getImagen();
			anchoImagenPixeles.value=datosQR.getAnchoImagen();
			altoImagenPixeles0.value=datosQR.getAltoImagen();
			formatoImagen.value= datosQR.getFormatoImagen();
			textoEquivalenteQr.value=contenidoACodificar;
			
		}  catch (Exception e){
			if (log!=null) {
				log.error ("Error inesperado:" + e.getMessage(),e);
				log.trace(e.getStackTrace());
			}
			esError.value=true;
			mensajeError.value="Error inesperado en el servicio:"+ e.getMessage();
			
		} catch (Throwable e){
			if (log!=null) {
				log.error ("Error inesperado:" + e.getMessage(),e);
				log.trace(e.getStackTrace());
			}
			esError.value=true;
			mensajeError.value="Error inesperado en el servicio:"+ e.getMessage();
			
		} finally{
			if (log!=null) {
				log.info("Fin de la generaci�n de c�digo QR");
			}
		}
		return;
	}
}
