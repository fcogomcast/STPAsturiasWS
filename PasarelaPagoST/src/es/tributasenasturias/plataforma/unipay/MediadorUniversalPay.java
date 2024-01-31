package es.tributasenasturias.plataforma.unipay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.google.gson.GsonBuilder;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.Varios;
import es.tributasenasturias.webservices.context.CallContext;

/**
 * Mediador con la plataforma de pago UniversalPay
 * @author crubencvs
 *
 */
public class MediadorUniversalPay {
	
	
	private String currency;
	private String locale;
	private String merchantIdentifier;
	private String descripcionAutoliquidacion;
	private String descripcionLiquidacion;
	private String paymentOperation;
	private String paymentChannel;
	private String entidadEquivalente;
	private String urlOk;
	private String urlKo;
	private String urlResponse;
	private String endpointQuery;
	private String endpointTokenRequest;
	private String passwordSignature;
	private String endpointRefund;
	private String styleColorLabel;
	private String styleBackBoton;
	private String styleBackSecondBoton;
	private String styleBackFrame;
	private String styleColorSecondBoton;
	private String styleColorBoton;
	private String styleColorBordeSecondBoton;
	private String styleColorBordeBoton;
	
	
	private CallContext context;
	/**
	 * Clase para modelar la respuesta de la operación de solitud de token
	 * @author crubencvs
	 *
	 */
	public static class TokenResponse{
		private String token;
		private String code;
		private String description;
		public final String getToken() {
			return token;
		}
		public final void setToken(String token) {
			this.token = token;
		}
		public final String getCode() {
			return code;
		}
		public final void setCode(String code) {
			this.code = code;
		}
		public final String getDescription() {
			return description;
		}
		public final void setDescription(String description) {
			this.description = description;
		}
		
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
	
	public static class QueryResponse{
		//No pongo todas las propiedades, sólo las que nos interesan
		private String errorCode;
		private String paymentCode;
		private String amount;
		private String bankOperation;
		private String paymentOperation;
		private String nrc; //Por ahora no se devuelve
		private String fechaPago;
		//CRUBENCVS 02/06/2021. Datos de las devoluciones
		private RefundData[] refundData;
		/**
		 * Datos de las devoluciones, sólo nos interesa mantener la cantidad y la fecha. 
		 * Por abreviar, se incluirán las correctas, por eso no se guarda el estado de 
		 * devolución
		 */
		public static class RefundData{
			private String amount;
			private String fechaDevolucion;
			
			public RefundData(String amount, String fechaDevolucion){
				this.amount= amount;
				this.fechaDevolucion= fechaDevolucion;
			}
			
			/**
			 * @return the amount
			 */
			public final String getAmount() {
				return amount;
			}
			/**
			 * @param amount the amount to set
			 */
			public final void setAmount(String amount) {
				this.amount = amount;
			}
			/**
			 * @return the fechaDevolucion
			 */
			public final String getFechaDevolucion() {
				return fechaDevolucion;
			}
			/**
			 * @param fechaDevolucion the fechaDevolucion to set
			 */
			public final void setFechaDevolucion(String fechaDevolucion) {
				this.fechaDevolucion = fechaDevolucion;
			}
			
		}
		
		public final String getErrorCode() {
			return errorCode;
		}
		public final void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}
		public final String getPaymentCode() {
			return paymentCode;
		}
		public final void setPaymentCode(String paymentCode) {
			this.paymentCode = paymentCode;
		}
		public final String getBankOperation() {
			return bankOperation;
		}
		public final void setBankOperation(String bankOperation) {
			this.bankOperation = bankOperation;
		}
		
		public final String getPaymentOperation() {
			return paymentOperation;
		}
		public final void setPaymentOperation(String paymentOperation) {
			this.paymentOperation = paymentOperation;
		}
		public final String getNrc() {
			return nrc;
		}
		public final void setNrc(String nrc) {
			this.nrc = nrc;
		}
		public final String getFechaPago() {
			return fechaPago;
		}
		public final void setFechaPago(String fechaPago) {
			this.fechaPago = fechaPago;
		}
		/**
		 * @return the refundData
		 */
		public final RefundData[] getRefundData() {
			return refundData;
		}
		/**
		 * @param refundData the refundData to set
		 */
		public final void setRefundData(RefundData[] refundData) {
			this.refundData = refundData;
		}
		/**
		 * @return the amount
		 */
		public final String getAmount() {
			return amount;
		}
		/**
		 * @param amount the amount to set
		 */
		public final void setAmount(String amount) {
			this.amount = amount;
		}
		
	}
	
	public static class RefundResponse{
		// Sólo nos interesan estas dos, por el momento.
		//El número de operación de devolución, y el código de authorización, no.
		private String errorCode;
		private String refundCode;
		public final String getErrorCode() {
			return errorCode;
		}
		public final void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}
		public final String getRefundCode() {
			return refundCode;
		}
		public final void setRefundCode(String refundCode) {
			this.refundCode = refundCode;
		}
		
	}
	
	/**
	 * 
	 * @param ficheroPropiedades
	 * @throws Exception
	 */
	public MediadorUniversalPay(Properties prop, CallContext context) throws PasarelaPagoException{
		try {
			//Cargamos las propiedades necesarias
			this.currency=prop.getProperty("CURRENCY");
			this.locale=prop.getProperty("LOCALE");
			this.merchantIdentifier=prop.getProperty("MERCHANT_IDENTIFIER");
			this.descripcionAutoliquidacion=prop.getProperty("DESCRIPCION_AUTOLIQUIDACION");
			this.descripcionLiquidacion=prop.getProperty("DESCRIPCION_LIQUIDACION");
			this.paymentOperation=prop.getProperty("PAYMENT_OPERATION");
			this.paymentChannel=prop.getProperty("PAYMENT_CHANNEL");
			this.urlOk=prop.getProperty("URL_OK");
			this.urlKo=prop.getProperty("URL_KO");
			this.urlResponse=prop.getProperty("URL_RESPONSE");
			this.endpointTokenRequest=prop.getProperty("ENDPOINT_TOKEN_REQUEST");
			this.passwordSignature=prop.getProperty("PASSWORD_SIGNATURE");
			this.endpointQuery=prop.getProperty("ENDPOINT_QUERY");
			this.entidadEquivalente=prop.getProperty("ENTIDAD_EQUIVALENTE");
			this.endpointRefund=prop.getProperty("ENDPOINT_REFUND");
			//Estilos
			this.styleColorLabel=prop.getProperty("STYLE_COLOR_LABEL");
			this.styleBackBoton=prop.getProperty("STYLE_BACK_BOTON");
			this.styleBackSecondBoton=prop.getProperty("STYLE_BACK_SECOND_BOTON");
			this.styleBackFrame=prop.getProperty("STYLE_BACK_FRAME");
			this.styleColorSecondBoton=prop.getProperty("STYLE_COLOR_SECOND_BOTON");
			this.styleColorBoton=prop.getProperty("STYLE_COLOR_BOTON");
			this.styleColorBordeSecondBoton=prop.getProperty("STYLE_COLOR_BORDE_SECOND_BOTON");
			this.styleColorBordeBoton=prop.getProperty("STYLE_COLOR_BORDE_BOTON");
			
			
			this.context= context;
		} catch (Exception e){
			throw new PasarelaPagoException("Error en creación de objeto "+MediadorUniversalPay.class.getName()+":"+e.getMessage(),e);
		}
	}
	// En principio lo único que se necesita desde fuera es la entidad de pago equivalente a la plataforma
	public final String getEntidadEquivalente() {
		return entidadEquivalente;
	}
	
	/**
	 * Construye los metadatos a enviar en la petición de Token
	 * @param operacion
	 * @param emisora
	 * @param justificante
	 * @param identificacion
	 * @param referencia
	 * @param importe
	 * @param fechaDevengo
	 * @param datoEspecifico
	 * @param nifOperante
	 * @param codIdentif
	 * @param numExpediente
	 * @return
	 */
	private String construirMetadatosTokenRequest(String operacion,
									 String emisora,
									 String justificante,
									 String identificacion,
									 String referencia,
									 String importe,
									 String fechaDevengo,
									 String datoEspecifico,
									 String nifOperante,
									 String codIdentif,
									 String numExpediente){
		StringBuilder metadata=new StringBuilder();
		Varios util = new Varios(); //11/05/2021. Se incluye para formatear los NIF de salida
		if (justificante!=null && !"".equals(justificante)){
			metadata.append("EMISORA:"+emisora+";");
			metadata.append("JUSTIFICANTE:"+justificante+";");
			if (fechaDevengo!=null && !"".equals(fechaDevengo)){
				metadata.append("FEC_DEVENGO:"+fechaDevengo+";");
			}
			if (datoEspecifico!=null && !"".equals(datoEspecifico)){
				metadata.append("DATO_ESPECIFICO:"+ datoEspecifico+";");
			}
			metadata.append("NIF_OPERANTE:"+util.formateaNIF(nifOperante)+";");
			metadata.append("COD_IDENTIF:"+util.formateaNIF(codIdentif)+";");
			if (numExpediente!=null && !"".equals(numExpediente)){
				metadata.append("NUM_EXPEDIENTE:"+numExpediente+";");
			}
		} else if (referencia!=null && !"".equals(referencia)){
			metadata.append("EMISORA:"+emisora+";");
			metadata.append("REFERENCIA:"+referencia+";");
			metadata.append("IDENTIFICATIVO:"+identificacion+";");
			metadata.append("NIF_OPERANTE:"+util.formateaNIF(nifOperante)+";");
			metadata.append("COD_IDENTIF:"+util.formateaNIF(codIdentif)+";");
		}
		return metadata.toString();
	}
	/**
	 * Construye la cadena JSON de petición
	 * @param operacion
	 * @param emisora
	 * @param justificante
	 * @param identificacion
	 * @param referencia
	 * @param importe
	 * @param fechaDevengo
	 * @param datoEspecifico
	 * @param nifOperante
	 * @param codIdentif
	 * @param numExpediente
	 * @return
	 */
	private String construirPeticionTokenRequest(String operacion,
									 String emisora,
									 String justificante,
									 String identificacion,
									 String referencia,
									 String importe,
									 String fechaDevengo,
									 String datoEspecifico,
									 String nifOperante,
									 String codIdentif,
									 String numExpediente)
	    throws Exception
	{
		TokenRequestJSonObject req = new TokenRequestJSonObject();
		req.setCurrency(this.currency);
		req.setLocale(this.locale);
		req.setMerchantIdentifier(this.merchantIdentifier);
		Long amount= Long.parseLong(importe);
		req.setAmount(amount);
		req.setOperation(operacion);
		if (justificante!=null && !"".equals(justificante)){
			req.setDescription(this.descripcionAutoliquidacion);
		} else if (referencia!=null && !"".equals(referencia)) {
			req.setDescription(this.descripcionLiquidacion);
		}
		req.setURLOk(this.urlOk+operacion);
		Params params= new Params();
		params.setPaymentOperation(this.paymentOperation);
		params.setPaymentChannel(this.paymentChannel);
		params.setMetadata(construirMetadatosTokenRequest(operacion, 
										      emisora, 
										      justificante, 
										      identificacion,
										      referencia,
										      importe,
										      fechaDevengo,
										      datoEspecifico,
										      nifOperante,
										      codIdentif,
										      numExpediente)
										     );
		
		//Estilos
		params.setStyleColorLabel(styleColorLabel);
		params.setStyleBackBoton(styleBackBoton);
		params.setStyleBackSecondBoton(styleBackSecondBoton);
		params.setStyleBackFrame(styleBackFrame);
		params.setStyleColorSecondBoton(styleColorSecondBoton);
		params.setStyleColorBoton(styleColorBoton);
		params.setStyleColorBordeSecondBoton(styleColorBordeSecondBoton);
		params.setStyleColorBordeBoton(styleColorBordeBoton);
		//14/09/2021. Si se indica amount_min y amount_max,
		//la plataforma no permite  modificar el importe
		params.setAmount_min(amount);
		params.setAmount_max(amount);
		//FIN 14/09/2021
		req.setParams(params);
		req.setSignature(generaSignature(this.merchantIdentifier + Long.parseLong(importe) + operacion + 
				 						(this.urlResponse+operacion)
				 						+ (this.urlOk+operacion)
				 						+(this.urlKo+operacion)));
		req.setURLResponse(this.urlResponse+operacion);
		req.setURLKo(this.urlKo+operacion);
		
		return new GsonBuilder().create().toJson(req,TokenRequestJSonObject.class);
	}
	
	/**
	 * Construye la cadena Json de petición de Consulta
	 * @param operacionEpst
	 * @return
	 * @throws Exception
	 */
	private String construirPeticionConsulta(String operacionEpst) throws Exception{
		QueryRequestJSonObject q= new QueryRequestJSonObject();
		q.setMerchantIdentifier(this.merchantIdentifier);
		q.setMerchantOperation(operacionEpst);
		q.setSignature(generaSignature(this.merchantIdentifier+operacionEpst));
		return new GsonBuilder().create().toJson(q, QueryRequestJSonObject.class);
	}
	
	/**
	 * Construye la cadena Json de petición de devolución
	 * @param importe
	 * @param paymentOperation
	 * @return
	 * @throws Exception
	 */
	private String construirPeticionDevolucion(String importe,
											   String paymentOperation) throws Exception{
		RefundRequestJsonObject r= new RefundRequestJsonObject();
		r.setMerchantIdentifier(this.merchantIdentifier);
		r.setAmount(Long.parseLong(importe));
		r.setPaymentOperation(paymentOperation);
		r.setSignature(generaSignature(r.getMerchantIdentifier()+r.getAmount()+r.getPaymentOperation()));
		return new GsonBuilder().create().toJson(r, RefundRequestJsonObject.class);
	}
	/**
	 * Devuelve el objeto equivalente a una respuesta de  solicitud de token
	 * @param json
	 * @return
	 */
	private TokenResponseJsonObject leerTokenResponse(String json){
		return new GsonBuilder().create().fromJson(json, TokenResponseJsonObject.class);
	}
	
	/**
	 * Devuelve el objeto equivalente a una respuesta de consulta
	 * @param json
	 * @return
	 */
	private QueryResponseJSonObject leerQueryResponse(String json){
		return new GsonBuilder().create().fromJson(json, QueryResponseJSonObject.class);
	}
	
	/**
	 * Devuelve el objeto equivalente a una respuesta de pago.
	 * No se necesitaría aquí, pero ya que la lectura de los otros mensajes se hace
	 * aquí, hago esta también.
	 * @param json
	 * @return
	 */
	public PaymentResponseJsonObject leerRespuestaPagoResponse(String json){
		return new GsonBuilder().create().fromJson(json, PaymentResponseJsonObject.class);
	}
	
	/**
	 * Devuelve el objeto equivalente a una respuesta de devolución
	 * @param json
	 * @return
	 */
	public RefundResponseJsonObject leerRespuestaDevolucion(String json){
		return new GsonBuilder().create().fromJson(json, RefundResponseJsonObject.class);
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
		log = "Pasarela Pago::SOAP_CLIENT ::" +"::"+direccion+" :: " + today + " :: " + datos+" :: "+ (String)this.context.get(Constantes.ID_SESION)+"\n";
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
	private HttpResult enviarPeticionPost(String url, String texto) throws PasarelaPagoException{
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
			throw new PasarelaPagoException("Error en método "+ MediadorUniversalPay.class.getName()+".enviarPeticionPost:"+e.getMessage(),e);
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
	 * Realiza la petición de un token de operación  contra UniversalPay
	 * @param operacion
	 * @param emisora
	 * @param justificante
	 * @param identificacion
	 * @param referencia
	 * @param importe
	 * @param fechaDevengo
	 * @param datoEspecifico
	 * @param nifOperante
	 * @param codIdentif
	 * @param numExpediente
	 * @return {@link TokenResponse} con los datos del token
	 * @throws Exception
	 */
	public TokenResponse tokenRequest(String operacion,
							  String emisora,
							  String justificante,
						      String identificacion,
							  String referencia,
							  String importe,
							  String fechaDevengo,
							  String datoEspecifico,
							  String nifOperante,
							  String codIdentif,
							  String numExpediente)
	 throws PasarelaPagoException
	{
		String fecha="";
		try {
			try {
				if (!"".equals(fechaDevengo)){
					SimpleDateFormat sdf= new SimpleDateFormat("ddMMyyyy");
					Date f=sdf.parse(fechaDevengo);
					sdf= new SimpleDateFormat("dd/MM/yyyy");
					fecha=sdf.format(f);
				}
			} catch (ParseException pe){
				//Debería ser imposible, porque se valida antes
				throw new Exception ("La fecha de devengo no se corresponde con el formato DDMMYYYY");
			}
			String json= construirPeticionTokenRequest(operacion,
													   emisora,
													   justificante,
													   identificacion,
													   referencia,
													   importe,
													   fecha,
													   datoEspecifico,
													   nifOperante,
													   codIdentif,
													   numExpediente);
			HttpResult http= enviarPeticionPost(this.endpointTokenRequest,json);
			if (http.getResponseCode()!=200){
				throw new Exception("Error en conexión HTTP con servidor. Respuesta HTTP:"+http.getResponseCode() + ":"+http.getResponseMessage());
			}
			
			TokenResponseJsonObject jsonObj= leerTokenResponse(http.getText());
			TokenResponse tokenResponse= new TokenResponse();
			if (jsonObj.getTokenResult()!=null){
				tokenResponse.setCode(jsonObj.getTokenResult().getCode());
				tokenResponse.setDescription(jsonObj.getTokenResult().getDescription());
			} else {
				throw new Exception("Error en la respuesta de solicitud de token, no ha llegado elemento 'TOKEN_RESULT'");
			}
			tokenResponse.setToken(jsonObj.getToken());
			return tokenResponse;
		} catch(Exception e){
			throw new PasarelaPagoException("Error en clase "+ MediadorUniversalPay.class.getName()+":"+e.getMessage(),e);
		} 
		
	}
	/**
	 * Consulta del estado de pago en la plataforma  UniversalPay
	 */
	public QueryResponse consultaEstadoPago(String operacionEpst) throws PasarelaPagoException{
		try {
			String json= construirPeticionConsulta(operacionEpst);
			HttpResult http= enviarPeticionPost(this.endpointQuery,json);
			if (http.getResponseCode()!=200){
				throw new Exception("Error en conexión HTTP con servidor. Respuesta HTTP:"+http.getResponseCode() + ":"+http.getResponseMessage());
			}
			QueryResponseJSonObject jsonObj= leerQueryResponse(http.getText());
			QueryResponse response= new QueryResponse();
			response.setErrorCode(jsonObj.getErrorCode());
			response.setPaymentCode(jsonObj.getPaymentCode());
			response.setBankOperation(jsonObj.getBankOperation());
			response.setPaymentOperation(jsonObj.getPaymentOperation());
			response.setAmount(jsonObj.getPaymentAmount());
			if (jsonObj.getParams()!=null){
				response.setNrc(jsonObj.getParams().getNrc());
			}
			response.setFechaPago(jsonObj.getPaymentDate());
			//CRUBENCVS 02/06/2021. Devuelvo datos de las devoluciones correctas.
			if (jsonObj.getRefunds()!=null && jsonObj.getRefunds().length >0){
				List<QueryResponse.RefundData> devoluciones= new ArrayList<QueryResponse.RefundData>();
				for (QueryResponseJSonObject.Refund r: jsonObj.getRefunds()){
					if ("000".equals(r.getRefundcode())){
						devoluciones.add(new QueryResponse.RefundData(r.getAmount(), r.getRefunddate()));
					}
				}
				response.setRefundData(devoluciones.toArray(new QueryResponse.RefundData[1])); //Dado que lo normal será una devolución, es probable que la llamada no genere nuevos objetos
			}
			// FIN CRUBENCVS 02/06/2021. Devoluciones
			return response;
			
		} catch(Exception e){
			throw new PasarelaPagoException("Error en método "+ MediadorUniversalPay.class.getName()+".consultaEstadoPago:"+e.getMessage(),e);
		}
		
	}
	/**
	 * Realiza la devolución de una operación de pago
	 * @param importe
	 * @param paymentOperation
	 * @return {@link RefundResponse} con ErrorCode y RefundCode. El resto de campos no se muestran.
	 * @throws PasarelaPagoException
	 */
	public RefundResponse realizaDevolucion(String importe, String paymentOperation) throws PasarelaPagoException{
		try {
			String json= construirPeticionDevolucion (importe, paymentOperation);
			HttpResult http= enviarPeticionPost(this.endpointRefund,json);
			if (http.getResponseCode()!=200){
				throw new Exception("Error en conexión HTTP con servidor. Respuesta HTTP:"+http.getResponseCode() + ":"+http.getResponseMessage());
			}
			RefundResponse response= new RefundResponse();
			RefundResponseJsonObject jsonObj= leerRespuestaDevolucion(http.getText());
			/*if (!validarSignature()){
				
			}*/
			response.setErrorCode(jsonObj.getErrorCode());
			response.setRefundCode(jsonObj.getRefundCode());
			return response;
			
		} catch(Exception e){
			throw new PasarelaPagoException("Error en método "+ MediadorUniversalPay.class.getName()+".realizaDevolucion:"+e.getMessage(),e);
		}
		
	}
	/**
	 * Genera la firma como hash SHA-256
	 * @param datos Cadena con los datos sobre lo que se creará la firma.
	 * @return
	 * @throws Exception
	 */
	private String generaSignature(String datos) throws Exception
	{
		MessageDigest md= MessageDigest.getInstance("SHA-256");
		byte[] sha=md.digest( (datos+this.passwordSignature).getBytes("UTF-8"));
		String hexChar="";
		StringBuffer hash=new StringBuffer();
		for(byte aux : sha) {
	           int b = aux & 0xff; // Convierte de unsigned a integer, respetando signo (integer tiene signo, y podría considerar que un byte es negativo)
	           hexChar=Integer.toHexString(b);
	           hash.append(hexChar.length()==1?("0"+hexChar):hexChar); //Left Padding, así se rellena correctamente.
	    }
		return hash.toString();
	}
	/**
	 * Valida la firma recibida de UniversalPay
	 * @param datos Datos con los que construir la firma de verificación
	 * @param signatureRecibida Firma recibida
	 * @return
	 * @throws Exception
	 */
	public boolean validarSignature(String datos, String signatureRecibida) throws Exception{
		String construida= generaSignature(datos);
		if (signatureRecibida.equalsIgnoreCase(construida)){
			return true;
		}
		return false;
	}
	
}
