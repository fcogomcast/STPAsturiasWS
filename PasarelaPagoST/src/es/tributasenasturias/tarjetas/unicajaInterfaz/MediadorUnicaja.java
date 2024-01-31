package es.tributasenasturias.tarjetas.unicajaInterfaz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.GsonBuilder;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.tarjetas.unicajaInterfaz.consultaTarjeta.ConsultaPagoTarjetaJsonRequest;
import es.tributasenasturias.tarjetas.unicajaInterfaz.consultaTarjeta.ConsultaPagoTarjetaJsonResponse;
import es.tributasenasturias.tarjetas.unicajaInterfaz.consultaTarjeta.SalidaOpConsultaPagoUnicaja;
import es.tributasenasturias.tarjetas.unicajaInterfaz.token.SalidaOpGenerarTokenUnicaja;
import es.tributasenasturias.tarjetas.unicajaInterfaz.token.ValidacionPagoReciboJSonResponse;
import es.tributasenasturias.tarjetas.unicajaInterfaz.token.ValidacionPagoReciboJsonRequest;

public class MediadorUnicaja {

	private String endpointUnicajaValidacion;
	private String endpointUnicajaConsulta;
	private String idSesion;
	public MediadorUnicaja(String endpointUnicajaValidacion,
							String endpointUnicajaConsulta,
							String idSesion
							)
	{
		this.endpointUnicajaValidacion= endpointUnicajaValidacion;
		this.endpointUnicajaConsulta= endpointUnicajaConsulta;
		this.idSesion= idSesion;
	}
	
	//Clase para modelar la respuesta de una petición HTTP
	private static class HttpResult{
		private int responseCode;
		private String responseMessage;
		private String text;
		public final int getResponseCode() {
			return responseCode;
		}
		public final void setResponseCode(int responseCode) {
			this.responseCode = responseCode;
		}
		public final String getText() {
			return text;
		}
		public final void setText(String text) {
			this.text = text;
		}
		public final String getResponseMessage() {
			return responseMessage;
		}
		public final void setResponseMessage(String responseMessage) {
			this.responseMessage = responseMessage;
		}
		
		
	}
	/**
	 * Dado que los importes hasta llegar a este punto se expresan en céntimos,
	 * tenemos que convertirlos en céntimos.
	 * @param importeCentimos
	 * @return
	 */
	private double centimosToEuro(String importeCentimos){
		if (importeCentimos==null){
			return 0;
		}
		return Double.parseDouble(importeCentimos)/100;
	}
	/**
	 * Formate la fecha de devengo del formato de petición a esta función,
	 * DDMMYYYY a YYYY-MM-DD
	 * @param fechaDevengo
	 * @return
	 */
	private String fechaDevengoFormateada(String fechaDevengo) throws ParseException{
		SimpleDateFormat sd1 = new SimpleDateFormat("ddMMyyyy");
		Date fecha1=sd1.parse(fechaDevengo);
		return new SimpleDateFormat ("yyyy-MM-dd").format(fecha1);
	}
	
	
	/**
	 * Escribe log de Json
	 * @param nombreFichero
	 * @param datos
	 * @throws IOException
	 */
	public void escribirLogJson(String datos, boolean salida) throws IOException{
		String LOG_FILE = "SOAP_CLIENT.log";
		String LOG_DIR = "proyectos/PasarelaPagoST";
		String log;
		String direccion=(salida)?"Envío":"Recepción"; 
		Date today = new Date();
		BufferedWriter writer=null;
		log = "Pasarela Pago::SOAP_CLIENT ::" +"::"+direccion+" :: " + today + " :: " + datos+" :: "+ idSesion +"\n";
		try {
			writer= new BufferedWriter( new FileWriter(LOG_DIR+"/"+LOG_FILE,true));
			writer.write(log);
		} finally {
			if (writer!=null){
				try {
					writer.close();
				} catch (Exception e){
				}
			}
		}
	}
	/**
	 * Envía una petición Post
	 * @param url
	 * @param texto
	 * @return
	 * @throws PasarelaPagoException
	 */
	private HttpResult enviarPeticionPost(String url, String texto) throws MediacionUnicajaException{
		URL dir=null;
		HttpURLConnection conn=null;
		BufferedReader br=null;
		HttpResult result= new HttpResult();
		try {
			dir= new URL(url);
			conn = (HttpURLConnection)dir.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type","application/json;utf-8");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			OutputStream out= conn.getOutputStream();
			//Escribo en SOAPClient, a la ida y a la vuelta
			escribirLogJson(texto,true);
			
			byte[] input= texto.getBytes("utf-8");
			out.write(input,0,input.length);
			int code= conn.getResponseCode();
			result.setResponseCode(code);
			if (code==200){
				br= new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
				StringBuilder response= new StringBuilder();
				String line=null;
				while ((line=br.readLine())!=null){
					response.append(line.trim());
				}
				escribirLogJson(response.toString(),false);
				result.setText(response.toString());
			} else {
				result.setResponseMessage(conn.getResponseMessage());
			}
			return result;
		} catch(Exception e){
			throw new MediacionUnicajaException("Error en método "+ MediadorUnicaja.class.getName()+".enviarPeticionPost:"+e.getMessage(),e);
		} finally{
			if (dir!=null){
				try { conn.disconnect();}
				catch(Exception e){}
			}
			if (br!=null){
				try {
					br.close();
				} catch(Exception e){
				}
			}
		}
	}
	
	
	/**
	 * 
	 * @param modalidad
	 * @param emisora
	 * @param identificacion
	 * @param referencia
	 * @param importeCentimos
	 * @param nifContribuyente
	 * @param fechaDevengo
	 * @param datoEspecifico
	 * @param expediente
	 * @param referenciaExterna
	 */
	public SalidaOpGenerarTokenUnicaja generaToken(String modalidad,
						   String emisora,
						   String identificacion,
						   String referencia,
						   String importeCentimos,
						   String nifContribuyente,
						   String fechaDevengo,
						   String datoEspecifico,
						   String expediente,
						   String referenciaExterna
						   ) throws MediacionUnicajaException
	{
		ValidacionPagoReciboJsonRequest request= new ValidacionPagoReciboJsonRequest();
		request.setTipoPantalla("3".equals(modalidad)?"C603":"C602");
		StringBuilder relleno=new StringBuilder(8);
		if (emisora.length()<8){
			for (int i=8; i>emisora.length();i--){
				relleno.append('0');
			}
		}
		request.setEmisora(relleno.toString() + emisora);
		if ("2".equals(modalidad)){
			request.setIdentificacion(identificacion);
		}
		request.setReferencia(referencia);
		request.setReferenciaExterna(referenciaExterna);
		request.setImporte(centimosToEuro(importeCentimos));
		if (!"".equals(datoEspecifico)){
			request.setDatoEspecifico(datoEspecifico);
		}

		request.setNif(nifContribuyente);
		if (!"".equals(fechaDevengo)){
			try {
				request.setFechaDevengo(fechaDevengoFormateada(fechaDevengo));
			} catch (ParseException pe){
				throw new MediacionUnicajaException("Error al formatear la fecha de devengo para envío a Unicaja:"+ pe.getMessage(),pe);
			}
		}
		if (!"".equals(expediente)){
			request.setExpediente(expediente);
		}
		String jsonRequest= new GsonBuilder().create().toJson(request,ValidacionPagoReciboJsonRequest.class);
		HttpResult http= enviarPeticionPost(this.endpointUnicajaValidacion,jsonRequest);
		
		switch (http.getResponseCode()){
		case 200:
		case 404:
			String jsonResponse= http.getText(); 
			ValidacionPagoReciboJSonResponse validacionResponse= new GsonBuilder().create().fromJson(jsonResponse, ValidacionPagoReciboJSonResponse.class);
			return new SalidaOpGenerarTokenUnicaja(validacionResponse.getNumPedido(),
																				 validacionResponse.getMerchantId(), 
																				 validacionResponse.getIdTerminal(), 
																				 validacionResponse.getAdquirerBin(),
																				 validacionResponse.getImporte(),
																				 validacionResponse.getMoneda(), 
																				 validacionResponse.getExponente(), 
																				 validacionResponse.getCifrado(), 
																				 validacionResponse.getPagoSoportado(), 
																				 validacionResponse.getExencionSCA(), 
																				 validacionResponse.getUrlTpvOk(), 
																				 validacionResponse.getUrlTpvKO(), 
																				 validacionResponse.getFirma(), 
																				 validacionResponse.getUrlTpvCeca(), 
																				 validacionResponse.getHttpCode(), 
																				 validacionResponse.getErrorCode(), 
																				 validacionResponse.getMoreInformation(), 
																				 validacionResponse.getHttpMessage(),
																				 http.getText());
		default:
			throw new MediacionUnicajaException("Error en conexión HTTP con servidor. Respuesta HTTP:"+http.getResponseCode() + ":"+http.getResponseMessage());
		}
		
	}
	
	/**
	 * Consulta de pago por tarjeta contra Unicaja banco
	 * @param operacionEpst
	 * @return
	 * @throws MediacionUnicajaException
	 */
	public SalidaOpConsultaPagoUnicaja consultarPago(String operacionEpst) throws MediacionUnicajaException{
		ConsultaPagoTarjetaJsonRequest request= new ConsultaPagoTarjetaJsonRequest();
		request.setReferenciaExt(operacionEpst);
		
		String jsonRequest= new GsonBuilder().create().toJson(request,ConsultaPagoTarjetaJsonRequest.class);
		HttpResult http= enviarPeticionPost(this.endpointUnicajaConsulta,jsonRequest);
		switch (http.getResponseCode()){
		case 200:
		case 404:
			String jsonRecibido=http.getText();
			ConsultaPagoTarjetaJsonResponse response= new GsonBuilder().create().fromJson(jsonRecibido, ConsultaPagoTarjetaJsonResponse.class);
			return new SalidaOpConsultaPagoUnicaja(response.getEstado()==0?true:false, //0= Pagado
												   response.getReferenciaMunicipal(),
												   response.getEstado(),
												   response.getReferenciaUnica(),
												   response.getFechaHora(),
												   response.getNrc(),
												   jsonRecibido);
		default: 
			throw new MediacionUnicajaException("Error en conexión HTTP con servidor. Respuesta HTTP:"+http.getResponseCode() + ":"+http.getResponseMessage());
		}
	}
}
