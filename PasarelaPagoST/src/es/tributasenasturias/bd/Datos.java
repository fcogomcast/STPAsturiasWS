package es.tributasenasturias.bd;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.Binding;
import javax.xml.ws.handler.Handler;


import es.tributasenasturias.conversor.ConversorRespuestaBD;
import es.tributasenasturias.conversor.MensajesBD;
import es.tributasenasturias.utils.ConversorParametrosLanzador;
import es.tributasenasturias.utils.Preferencias;
/**
 * Clase de acceso a datos.
 */
public class Datos 
{
	private final static String STRING_CADE = "STRING_CADE";
	private final static String FECHA1_CANU = "FECHA1_CANU";
	private final static String FECHA2_CANU = "FECHA2_CANU";
	private final static String STRING1_CANU = "STRING1_CANU";
	private final static String STRING2_CANU = "STRING2_CANU";
	private final static String STRING3_CANU = "STRING3_CANU";
	private final static String STRING4_CANU = "STRING4_CANU";
	private final static String STRING5_CANU = "STRING5_CANU";
	private final static String ESUN_C1 = "C1";
	private final static String ESUN_C2 = "C2";
	private final static String ESUN_C3 = "C3";
	private final static String ESUN_C4 = "C4";
	private final static String ESUN_C5 = "C5";
	private final static String ESUN_C6 = "C6";
	private final static String ESUN_C7 = "C7";
	private final static String ESUN_C8 = "C8";
	private final static String ESUN_C9 = "C9";
	private final static String ESUN_C10 = "C10";
	private final static String ESUN_C11 = "C11";
	private final static String ESUN_C12 = "C12";
	private final static String ESUN_C13 = "C13";
	private final static String ESUN_C14 = "C14";
	private final static String ESUN_C15 = "C15";
	private final static String ESUN_C16= "C16";
	private final static String ESUN_C17= "C17";
	private final static String ESUN_C18= "C18";
	private final static String ESUN_C19= "C19";
	private final static String ESUN_C20= "C20";
	private final static String ESUN_C21= "C21";
	private final static String ESUN_C22= "C22";
	private final static String ESUN_C23= "C23";
	private final static String ESUN_C24= "C24";
	private final static String ESUN_C25= "C25";
	private final static String ESUN_C26= "C26";
	
	private final static String ESUN_N0= "N0";
	
	
	private final static String ERRORNODE = "error";
	private final static String ORDEN_CAOR  = "ORDEN_CAOR";
	private final static String STRING_CAOR = "STRING_CAOR";
	
	private stpa.services.LanzaPLService lanzaderaWS; // Servicio Web
	private stpa.services.LanzaPL lanzaderaPort; // Port (operaciones) a las que se llamas
	private ConversorParametrosLanzador conversor;
	private Preferencias preferencias;
	private String errorLlamada;
	//Constantes para utilizar fuera de la clase.
	public static final String DES_RESULTADO_LLAMADA_BD = "DesResultadoBD";
	public static final String COD_RESULTADO_LLAMADA_BD = "CodResultadoBD";
	public static final String C_JUSTIFICANTE_PETICION_PATE = "Justificante";
	public static final String C_ESTADO_PETICION_PATE = "Estado";
	public static final String C_NRC = "NRC";
	public static final String C_NUM_OPERACION = "Nume_Oper";
	public static final String C_NUM_UNICO = "Num_Unico";
	public static final String C_APLICACION = "Servicio";
	public static final String C_FECHA_PAGO = "Fecha_Pago";
	public static final String C_FECHA_OPERACION = "Fecha_Operacion";
	public static final String C_PASARELA_PAGO_BD = "Pasarela_Pago";
	public static final String C_NIF_CONTRIBUYENTE = "Nif_Contribuyente";
	public static final String C_FECHA_DEVENGO = "Fecha_Devengo";
	public static final String C_DATO_ESPECIFICO = "Dato_Especifico";
	public static final String C_EXPEDIENTE = "Expediente";
	public static final String C_NIF_OPERANTE = "Nif_Operante";
	public static final String C_IMPORTE = "Importe";
	//CRUBENCVS 04/03/2021
	public static final String C_OPERACION_EPST = "Operacion_Epst";
	public static final String C_HASH_DATOS = "Hash_Datos";
	public static final String C_MEDIO_PAGO= "Medio_Pago";
	public static final String C_DATOS_CONSISTENTES= "DATOS_INCONSISTENTES";
	// FIN CRUBENCVS 04/03/2021
	public static final String C_RES_LLAMADA_OK="0";
	
	//Clase interna para comprobar errores en la llamada.
	//Cada llamada dará valor a esta clase, y así se podrá verificar si una llamada ha respondido
	//con un error o un código de mensaje correcto.
	public static class InfoLlamada
	{
		private boolean error;
		private String codErrorBD;
		private String codErrorServicio;
		private String desErrorServicio;
		/**
		 * @return the error
		 */
		public boolean isError() {
			return error;
		}
		/**
		 * @param error the error to set
		 */
		public void setError(boolean error) {
			this.error = error;
		}
		/**
		 * @return the codErrorBD
		 */
		public String getCodErrorBD() {
			return codErrorBD;
		}
		/**
		 * @param codErrorBD the codErrorBD to set
		 */
		public void setCodErrorBD(String codErrorBD) {
			this.codErrorBD = codErrorBD;
		}
		/**
		 * @return the codErrorServicio
		 */
		public String getCodErrorServicio() {
			return codErrorServicio;
		}
		/**
		 * @param codErrorServicio the codErrorServicio to set
		 */
		public void setCodErrorServicio(String codErrorServicio) {
			this.codErrorServicio = codErrorServicio;
		}
		/**
		 * @return the desErrorServicio
		 */
		public String getDesErrorServicio() {
			return desErrorServicio;
		}
		/**
		 * @param desErrorServicio the desErrorServicio to set
		 */
		public void setDesErrorServicio(String desErrorServicio) {
			this.desErrorServicio = desErrorServicio;
		}
		@SuppressWarnings("unused")
		private InfoLlamada() {
			
		}
		public InfoLlamada(String codigoResultadoBD)
		{
			MensajesBD menBD;
			if (codigoResultadoBD==null)
			{
				this.codErrorServicio="";
				this.desErrorServicio="";
				this.error=true; //No sabemos si es o no error, así que consideramos que sí, ya que es error de programación.
			}
			else if (!codigoResultadoBD.equalsIgnoreCase(Datos.C_RES_LLAMADA_OK))
				{
					this.error=true;
					ConversorRespuestaBD convBD = new ConversorRespuestaBD();
					menBD=convBD.getResultadoBD(codigoResultadoBD);
					if (menBD!=null)
					{
						this.codErrorServicio = menBD.getCodigoServicio();
						this.desErrorServicio= menBD.getTextoServicio();
					}
					else
					{
						this.codErrorServicio="";
						this.desErrorServicio="";
					}
				}
				else
				{
					this.error=false;
				}
		}
	}
	
	//Tipo de infoLlamada, para guardar la información de la última llamada a base de datos realizada.
	InfoLlamada infoUltimaLlamada;
	
	//CRUBENCVS 01/12/2021
	//Clase para almacenar el resultado de una consulta de pago por tarjeta, aunque
	//podría almacenar cualquier resultado a PATE.
	public static class ResultadoConsultaPagoTarjetaBD {
		private String estado;
		private String aplicacion;
		private String numeroUnico;
		private String justificante;
		private String identificacion;
		private String referencia;
		private String numeroOperacion;
		private String nrc;
		private String fechaOperacion; //Formateada a YYYYMMDD
		private String fechaPago; //Formateada a YYYYMMDD
		private String pasarelaPago;
		private String nifContribuyente;
		private String fechaDevengo; //Formateada a DD/MM/YYYY ???
		private String datoEspecifico;
		private String expediente;
		private String nifOperante;
		private String importe;
		private String operacionEpst;
		private String medioPago;
		private String hashDatos;
		private String resultado;
		//CRUBENCVS 31/03/2023 47535
		private String origen;
		private String modalidad;
		private String fechaAnulacion;
		private String emisora;
		private String nombreContribuyente;
		//FIN CRUBECNVS 31/03/2023
		private String[] numerosOperacionEpst;
		private boolean error;
		private boolean hayDatos;
		/**
		 * @return the estado
		 */
		public final String getEstado() {
			return estado;
		}
		/**
		 * @param estado the estado to set
		 */
		public final void setEstado(String estado) {
			this.estado = estado;
		}
		/**
		 * @return the aplicacion
		 */
		public final String getAplicacion() {
			return aplicacion;
		}
		/**
		 * @param aplicacion the aplicacion to set
		 */
		public final void setAplicacion(String aplicacion) {
			this.aplicacion = aplicacion;
		}
		/**
		 * @return the numeroUnico
		 */
		public final String getNumeroUnico() {
			return numeroUnico;
		}
		/**
		 * @param numeroUnico the numeroUnico to set
		 */
		public final void setNumeroUnico(String numeroUnico) {
			this.numeroUnico = numeroUnico;
		}
		/**
		 * @return the justificante
		 */
		public final String getJustificante() {
			return justificante;
		}
		/**
		 * @param justificante the justificante to set
		 */
		public final void setJustificante(String justificante) {
			this.justificante = justificante;
		}
		/**
		 * @return the identificacion
		 */
		public final String getIdentificacion() {
			return identificacion;
		}
		/**
		 * @param identificacion the identificacion to set
		 */
		public final void setIdentificacion(String identificacion) {
			this.identificacion = identificacion;
		}
		/**
		 * @return the referencia
		 */
		public final String getReferencia() {
			return referencia;
		}
		/**
		 * @param referencia the referencia to set
		 */
		public final void setReferencia(String referencia) {
			this.referencia = referencia;
		}
		/**
		 * @return the numeroOperacion
		 */
		public final String getNumeroOperacion() {
			return numeroOperacion;
		}
		/**
		 * @param numeroOperacion the numeroOperacion to set
		 */
		public final void setNumeroOperacion(String numeroOperacion) {
			this.numeroOperacion = numeroOperacion;
		}
		/**
		 * @return the nrc
		 */
		public final String getNrc() {
			return nrc;
		}
		/**
		 * @param nrc the nrc to set
		 */
		public final void setNrc(String nrc) {
			this.nrc = nrc;
		}
		/**
		 * @return the fechaOperacion
		 */
		public final String getFechaOperacion() {
			return fechaOperacion;
		}
		/**
		 * @param fechaOperacion the fechaOperacion to set
		 */
		public final void setFechaOperacion(String fechaOperacion) {
			this.fechaOperacion = fechaOperacion;
		}
		/**
		 * @return the fechaPago
		 */
		public final String getFechaPago() {
			return fechaPago;
		}
		/**
		 * @param fechaPago the fechaPago to set
		 */
		public final void setFechaPago(String fechaPago) {
			this.fechaPago = fechaPago;
		}
		/**
		 * @return the pasarelaPago
		 */
		public final String getPasarelaPago() {
			return pasarelaPago;
		}
		/**
		 * @param pasarelaPago the pasarelaPago to set
		 */
		public final void setPasarelaPago(String pasarelaPago) {
			this.pasarelaPago = pasarelaPago;
		}
		/**
		 * @return the nifContribuyente
		 */
		public final String getNifContribuyente() {
			return nifContribuyente;
		}
		/**
		 * @param nifContribuyente the nifContribuyente to set
		 */
		public final void setNifContribuyente(String nifContribuyente) {
			this.nifContribuyente = nifContribuyente;
		}
		/**
		 * @return the fechaDevengo
		 */
		public final String getFechaDevengo() {
			return fechaDevengo;
		}
		/**
		 * @param fechaDevengo the fechaDevengo to set
		 */
		public final void setFechaDevengo(String fechaDevengo) {
			this.fechaDevengo = fechaDevengo;
		}
		/**
		 * @return the datoEspecifico
		 */
		public final String getDatoEspecifico() {
			return datoEspecifico;
		}
		/**
		 * @param datoEspecifico the datoEspecifico to set
		 */
		public final void setDatoEspecifico(String datoEspecifico) {
			this.datoEspecifico = datoEspecifico;
		}
		/**
		 * @return the expediente
		 */
		public final String getExpediente() {
			return expediente;
		}
		/**
		 * @param expediente the expediente to set
		 */
		public final void setExpediente(String expediente) {
			this.expediente = expediente;
		}
		/**
		 * @return the nifOperante
		 */
		public final String getNifOperante() {
			return nifOperante;
		}
		/**
		 * @param nifOperante the nifOperante to set
		 */
		public final void setNifOperante(String nifOperante) {
			this.nifOperante = nifOperante;
		}
		/**
		 * @return the importe
		 */
		public final String getImporte() {
			return importe;
		}
		/**
		 * @param importe the importe to set
		 */
		public final void setImporte(String importe) {
			this.importe = importe;
		}
		/**
		 * @return the operacionEpst
		 */
		public final String getOperacionEpst() {
			return operacionEpst;
		}
		/**
		 * @param operacionEpst the operacionEpst to set
		 */
		public final void setOperacionEpst(String operacionEpst) {
			this.operacionEpst = operacionEpst;
		}
		/**
		 * @return the medioPago
		 */
		public final String getMedioPago() {
			return medioPago;
		}
		/**
		 * @param medioPago the medioPago to set
		 */
		public final void setMedioPago(String medioPago) {
			this.medioPago = medioPago;
		}
		/**
		 * @return the hashDatos
		 */
		public final String getHashDatos() {
			return hashDatos;
		}
		/**
		 * @param hashDatos the hashDatos to set
		 */
		public final void setHashDatos(String hashDatos) {
			this.hashDatos = hashDatos;
		}
		/**
		 * @return the resultado
		 */
		public final String getResultado() {
			return resultado;
		}
		/**
		 * @param resultado the resultado to set
		 */
		public final void setResultado(String resultado) {
			this.resultado = resultado;
		}
		/**
		 * @return the numerosOperacionEpst
		 */
		public final String[] getNumerosOperacionEpst() {
			return numerosOperacionEpst;
		}
		/**
		 * @param numerosOperacionEpst the numerosOperacionEpst to set
		 */
		public final void setNumerosOperacionEpst(String[] numerosOperacionEpst) {
			this.numerosOperacionEpst = numerosOperacionEpst;
		}
		/**
		 * @return the error
		 */
		public final boolean isError() {
			return error;
		}
		/**
		 * @param error the error to set
		 */
		public final void setError(boolean error) {
			this.error = error;
		}
		/**
		 * @return the hayDatos
		 */
		public final boolean hayDatos() {
			return hayDatos;
		}
		/**
		 * @param hayDatos the hayDatos to set
		 */
		public final void setHayDatos(boolean hayDatos) {
			this.hayDatos = hayDatos;
		}
		/**
		 * @return the origen
		 */
		public final String getOrigen() {
			return origen;
		}
		/**
		 * @param origen the origen to set
		 */
		public final void setOrigen(String origen) {
			this.origen = origen;
		}
		/**
		 * @return the modalidad
		 */
		public final String getModalidad() {
			return modalidad;
		}
		/**
		 * @param modalidad the modalidad to set
		 */
		public final void setModalidad(String modalidad) {
			this.modalidad = modalidad;
		}
		/**
		 * @return the fechaAnulacion
		 */
		public final String getFechaAnulacion() {
			return fechaAnulacion;
		}
		/**
		 * @param fechaAnulacion the fechaAnulacion to set
		 */
		public final void setFechaAnulacion(String fechaAnulacion) {
			this.fechaAnulacion = fechaAnulacion;
		}
		/**
		 * @return the emisora
		 */
		public final String getEmisora() {
			return emisora;
		}
		/**
		 * @param emisora the emisora to set
		 */
		public final void setEmisora(String emisora) {
			this.emisora = emisora;
		}
		/**
		 * @return the nombreContribuyente
		 */
		public final String getNombreContribuyente() {
			return nombreContribuyente;
		}
		/**
		 * @param nombreContribuyente the nombreContribuyente to set
		 */
		public final void setNombreContribuyente(String nombreContribuyente) {
			this.nombreContribuyente = nombreContribuyente;
		}
		
	}
	/**
	 * Constructor
	 * @param idSesion Identificador de la sesión en la que se instancia este objeto.
	 */
	public Datos(String idSesion) 
	{
		try
		{
			preferencias = Preferencias.getPreferencias();//new Preferencias();
			String endPointLanzador=preferencias.getEndPointLanzador();
			lanzaderaWS = new stpa.services.LanzaPLService();
			if (!"".equals(endPointLanzador))
			{
				lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
				javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort; // enlazador de protocolo para el servicio.
				bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endPointLanzador); // Cambiamos el endpoint
				//Asociamos el log con este port.

				Binding bi = bpr.getBinding();
				List <Handler> handlerList = bi.getHandlerChain();
				if (handlerList == null)
				{
				   handlerList = new ArrayList<Handler>();
				}
				handlerList.add(new es.tributasenasturias.webservices.soap.LogMessageHandlerClient(idSesion));
				bi.setHandlerChain(handlerList);
			}
			else
			{
				lanzaderaPort =lanzaderaWS.getLanzaPLSoapPort();
				javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort; // enlazador de protocolo para el servicio.
				//Asociamos el log con este port.
				Binding bi = bpr.getBinding();
				List <Handler> handlerList = bi.getHandlerChain();
				if (handlerList == null)
				{
				   handlerList = new ArrayList<Handler>();
				}
				handlerList.add(new es.tributasenasturias.webservices.soap.LogMessageHandlerClient(idSesion));
				bi.setHandlerChain(handlerList);
			}
			conversor = new ConversorParametrosLanzador();
		}
		catch (Exception ex)
		{
			//Se ignora. En la recuperación de la instancia se comprobará si se creó bien.
		}
	}
	/**
	 * Método que recupera la instancia de la clase. 
	 * Será el interfaz que utilizarán los clientes de esta clase para acceder a la instancia de la misma.
	 * @return Instancia de la clase.
	 * @throws Exception
	 */
	/*public static Datos getDatos () throws Exception
	{
		if (_datos==null)
		{
			throw new Exception ("No se ha podido recuperar la instancia de datos.");
		}
		return _datos;
	}
	*/
	/**
	 * Método que devuelve la clave de consejerías para generación del MAC.
	 * @return Clave de consejerías
	 * @throws RemoteException
	 */
	public String obtenerClaveConsejerias() throws RemoteException
	{
		conversor.setProcedimientoAlmacenado("internet.obtenerclaveconsejerias");
		String resultadoEjecutarPL = Ejecuta();
		
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		return conversor.getNodoResultado(STRING_CADE);
	}
	/**
	 * Método que devuelve la clave de un cliente concreto para generación del MAC.
	 * @param Cliente del que se desea recuperar clave
	 * @return Clave de cliente que se pasa por parámetro
	 * @throws RemoteException
	 */
	public Map<String,String> obtenerClaveCliente(String cliente) throws RemoteException
	{
		Map<String,String> resultado= new HashMap<String,String>();
		conversor.setProcedimientoAlmacenado("SW_Pasarela2.recuperarClave");
		conversor.setParametro (cliente,ConversorParametrosLanzador.TIPOS.String);
		String resultadoEjecutarPL = Ejecuta();
		
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		resultado.put("estado",conversor.getNodoResultado(STRING_CADE));
		resultado.put("clave",conversor.getNodoResultado(STRING1_CANU));
		return resultado;
	}
	/**
	 * @deprecated
	 * @param aplicacion
	 * @param numeroUnico
	 * @return
	 * @throws RemoteException
	 */
	public boolean existePate(String aplicacion, String numeroUnico) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado("SW_Pasarela2.ExistePate");
		conversor.setParametro(aplicacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(numeroUnico, ConversorParametrosLanzador.TIPOS.String);
		
		String resultadoEjecutarPL = Ejecuta();
		
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		return conversor.getNodoResultado(STRING_CADE).equalsIgnoreCase("TRUE") ? true : false;
	}
	/**
	 * @deprecated
	 * @param emisora
	 * @param justificante
	 * @param nif
	 * @param fechaDevengo
	 * @param datoEspecifico
	 * @param expediente
	 * @param servicio
	 * @param numeroUnico
	 * @param nifOperante
	 * @param tarjeta
	 * @param fechaCaducidad
	 * @throws RemoteException
	 */
	public void insertarPate(String emisora, String justificante, String nif, String fechaDevengo,
			String datoEspecifico, String expediente, String servicio, String numeroUnico, String nifOperante,
			String tarjeta, String fechaCaducidad) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado("SW_Pasarela2.InsertarPate");
		conversor.setParametro(emisora, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(justificante, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(nif, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(fechaDevengo, ConversorParametrosLanzador.TIPOS.Date);
		conversor.setParametro(datoEspecifico, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(expediente, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(servicio, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(numeroUnico, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(nifOperante, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(tarjeta, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(fechaCaducidad, ConversorParametrosLanzador.TIPOS.String);
		
		Ejecuta();
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
	}
	/**
	 * Método que llama al procedimiento de petición de pago telemático en la base de datos.
	 * Este método comprueba el estado del pago si existe, y si no, lo crea.
	 * @param emisora
	 * @param justificante
	 * @param nif
	 * @param fechaDevengo
	 * @param datoEspecifico
	 * @param expediente
	 * @param importe
	 * @param identificacion
	 * @param referencia
	 * @param servicio
	 * @param numeroUnico
	 * @param nif_operante
	 * @param tarjeta
	 * @param fechaCaducidad
	 * @return Estado del pago en la base de datos.
	 * @throws RemoteException
	 */
	public Map<String,String> peticionPate(String origen,String modalidad, String emisora, String nif, String nombreContribuyente,
			String fechaDevengo,
			String justificante,String datoEspecifico, String expediente, 
			String importe, String identificacion, String referencia,String servicio, 
			String numeroUnico, String nif_operante,
			String tarjeta, String fechaCaducidad, String ccc,String modelo, String pasarelaPago) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado("SW_Pasarela2.PeticionPate");
		conversor.setParametro(origen, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(modalidad, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(emisora, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(importe, ConversorParametrosLanzador.TIPOS.Integer);
		conversor.setParametro(justificante, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(nif, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(fechaDevengo, ConversorParametrosLanzador.TIPOS.Date,"DDMMYYYY");
		conversor.setParametro(datoEspecifico, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(expediente, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(identificacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(referencia, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(servicio, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(numeroUnico, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(nif_operante, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(tarjeta, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(fechaCaducidad, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(ccc, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(modelo, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(nombreContribuyente, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(pasarelaPago, ConversorParametrosLanzador.TIPOS.String);
		
		String resultadoEjecutarPL = Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		//return conversor.getNodoResultado(STRING_CADE);
		String estado = conversor.getNodoResultado(STRING1_CANU);
		String rjustificante = conversor.getNodoResultado(STRING2_CANU);
		String pasarela= conversor.getNodoResultado(STRING3_CANU);
		Map<String, String> resultado = new HashMap<String, String>();
		resultado.put(C_ESTADO_PETICION_PATE, estado);
		resultado.put(C_JUSTIFICANTE_PETICION_PATE, rjustificante);
		resultado.put(C_PASARELA_PAGO_BD, pasarela);
		//Para devolver el resultado de la llamada.
		//En CAOR se devuelve el código de resultado de la llamada y una descripción opcional.
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
		return resultado;
	}
	/**
	 * Método que actualiza el estado de un pago telemático en la base de datos.
	 * @param servicio
	 * @param numeroUnico
	 * @param estado
	 * @param resultado
	 * @param nrc
	 * @param numOperacion
	 * @param fecOperacion
	 * @param pasarelaPago
	 * @throws RemoteException
	 */
	@Deprecated
	public void actualizarPate(String servicio, String numeroUnico, String estado, String resultado, String nrc, String numOperacion, String fecOperacion, String pasarelaPago) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado("SW_Pasarela2.ActualizarPate");
		conversor.setParametro(servicio, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(numeroUnico, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(estado, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(resultado, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(nrc, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(numOperacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro (fecOperacion, ConversorParametrosLanzador.TIPOS.Date,"YYYY-MM-DD");
		conversor.setParametro (pasarelaPago, ConversorParametrosLanzador.TIPOS.String);
		
		String resultadoEjecutarPL =Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		//Para devolver el resultado de la llamada.
		//En CAOR se devuelve el código de resultado de la llamada y una descripción opcional.
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
	}
	/**
	 * Método que actualiza el estado de un pago telemático en la base de datos.
	 * @param aplicacion
	 * @param numero_unico
	 * @param estado
	 * @param resultado
	 * @param nrc
	 * @param tarjeta
	 * @param fechaCaducidad
	 * @param ccc
	 * @param numOperacion
	 * @param fecOperacion
	 * @param operante
	 * @param fechaDevengo
	 * @param fechaAnulacion
	 * @param operacionEpst
	 * @throws RemoteException
	 */
	public void actualizarPate(String pasarela,
							   String justificante,
							   String identificacion, 
							   String referencia, 
							   String estado, 
							   String resultado, 
							   String nrc, 
							   String tarjeta, 
							   String fechaCaducidad, 
							   String ccc, 
							   String numOperacion, 
							   String fecOperacion, 
							   String operante, 
							   String fechaDevengo, 
							   String fechaAnulacion,
							   String operacionEpst) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado("SW_Pasarela2.ActualizarPate");
		conversor.setParametro(justificante, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(identificacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(referencia, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(estado, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(resultado, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(nrc, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(tarjeta, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(fechaCaducidad, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(ccc, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(numOperacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro (fecOperacion, ConversorParametrosLanzador.TIPOS.Date,"YYYY-MM-DD");
		conversor.setParametro(pasarela, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(operante, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(fechaDevengo, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(fechaAnulacion, ConversorParametrosLanzador.TIPOS.Date, "DDMMYYYYHH24MISS");
		//02/12/2021. Se añade también la opción de modificar el número de operación contra la plataforma de pago remota.
		conversor.setParametro(operacionEpst, ConversorParametrosLanzador.TIPOS.String);
		String resultadoEjecutarPL =Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));	
		//Para devolver el resultado de la llamada.
		//En CAOR se devuelve el código de resultado de la llamada y una descripción opcional.
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
	}
	/*******************************************************************
	 * Metodo que obtiene el resultado de una consulta a PATE
	 * Devuelve una hash con columna - valor
	 * En concreto seran las columnas fecha de operación  y estado.
	 * 
	 * @param emisora
	 * @param numeroAutoliquidacion
	 * @return
	 * @throws RemoteException
	 */
	public Map<String, String> consultaPate(String emisora, String numeroAutoliquidacion) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado("SW_Pasarela2.ConsultaPate");
		conversor.setParametro(emisora, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(numeroAutoliquidacion, ConversorParametrosLanzador.TIPOS.String);
		
		String resultadoEjecutarPL = Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		String fecha_operacion = conversor.getNodoResultadoX(ESUN_C6);
		String fecha_pago = conversor.getNodoResultadoX(ESUN_C7);
		String estado = conversor.getNodoResultadoX(ESUN_C1);
		String servicio = conversor.getNodoResultadoX(ESUN_C2);
		String num_unico = conversor.getNodoResultadoX(ESUN_C3);
		String nume_oper = conversor.getNodoResultadoX(ESUN_C4);
		String nrc = conversor.getNodoResultadoX(ESUN_C5);
		String pasarela_pago = conversor.getNodoResultadoX(ESUN_C8);
		String nifContribuyente = conversor.getNodoResultadoX(ESUN_C9);
		String fechaDevengo = conversor.getNodoResultadoX(ESUN_C10);
		String datoEspecifico = conversor.getNodoResultadoX(ESUN_C11);
		String expediente = conversor.getNodoResultadoX(ESUN_C12);
		String nifOperante = conversor.getNodoResultadoX(ESUN_C13);
		String importe = conversor.getNodoResultadoX(ESUN_C14);
		// CRUBENCVS 04/03/2021. Para los datos extra 
		// que se tienen desde que se introduce el 
		// pago con tarjeta por plataformas de pago
		String operacion= conversor.getNodoResultadoX(ESUN_C15);
		String medioPago= conversor.getNodoResultadoX(ESUN_C16);
		String hashDatos= conversor.getNodoResultadoX(ESUN_C17);
		// FIN CRUBENCVS 04/03/2021
		Map<String, String> resultado = new HashMap<String, String>();
		resultado.put(C_FECHA_OPERACION, fecha_operacion);
		resultado.put(C_FECHA_PAGO, fecha_pago);
		resultado.put(C_ESTADO_PETICION_PATE, estado);
		resultado.put(C_APLICACION, servicio);
		resultado.put(C_NUM_UNICO, num_unico);
		resultado.put(C_NUM_OPERACION, nume_oper);
		resultado.put(C_PASARELA_PAGO_BD, pasarela_pago);
		resultado.put(C_NRC, nrc);
		resultado.put(C_NIF_CONTRIBUYENTE, nifContribuyente);
		resultado.put(C_FECHA_DEVENGO, fechaDevengo);
		resultado.put(C_DATO_ESPECIFICO, datoEspecifico);
		resultado.put(C_EXPEDIENTE, expediente);
		resultado.put(C_NIF_OPERANTE, nifOperante);
		resultado.put(C_IMPORTE, importe);
		//CRUBENCVS 04/03/2021
		resultado.put(C_OPERACION_EPST, operacion);
		resultado.put(C_MEDIO_PAGO,medioPago);
		resultado.put(C_HASH_DATOS,hashDatos);
		//FIN CRUBENCVS 04/03/2021
		//Para devolver el resultado de la llamada.
		//En CAOR se devuelve el código de resultado de la llamada y una descripción opcional.
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
		return resultado;
	}
	
	public Map<String, String> consultaPate(String emisora, String identificacion,String referencia) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado("SW_Pasarela2.ConsultaPate");
		conversor.setParametro(emisora, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(identificacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(referencia, ConversorParametrosLanzador.TIPOS.String);
		
		String resultadoEjecutarPL = Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		String fecha_operacion = conversor.getNodoResultadoX(ESUN_C6);
		String fecha_pago = conversor.getNodoResultadoX(ESUN_C7);
		String estado = conversor.getNodoResultadoX(ESUN_C1);
		String servicio = conversor.getNodoResultadoX(ESUN_C2);
		String num_unico = conversor.getNodoResultadoX(ESUN_C3);
		String nume_oper = conversor.getNodoResultadoX(ESUN_C4);
		String nrc = conversor.getNodoResultadoX(ESUN_C5);
		String pasarela_pago = conversor.getNodoResultadoX(ESUN_C8);
		String nifContribuyente = conversor.getNodoResultadoX(ESUN_C9);
		String fechaDevengo = conversor.getNodoResultadoX(ESUN_C10);
		String datoEspecifico = conversor.getNodoResultadoX(ESUN_C11);
		String expediente = conversor.getNodoResultadoX(ESUN_C12);
		String nifOperante = conversor.getNodoResultadoX(ESUN_C13);
		String importe = conversor.getNodoResultadoX(ESUN_C14);
		// CRUBENCVS 04/03/2021. Para los datos extra 
		// que se tienen desde que se introduce el 
		// pago con tarjeta por plataformas de pago
		String operacion= conversor.getNodoResultadoX(ESUN_C15);
		String medioPago= conversor.getNodoResultadoX(ESUN_C16);
		String hashDatos= conversor.getNodoResultadoX(ESUN_C17);
		// FIN CRUBENCVS 04/03/2021
		Map<String, String> resultado = new HashMap<String, String>();
		resultado.put(C_FECHA_OPERACION, fecha_operacion);
		resultado.put(C_FECHA_PAGO, fecha_pago);
		resultado.put(C_ESTADO_PETICION_PATE, estado);
		resultado.put(C_APLICACION, servicio);
		resultado.put(C_NUM_UNICO, num_unico);
		resultado.put(C_NUM_OPERACION, nume_oper);
		resultado.put(C_NRC, nrc);
		resultado.put(C_PASARELA_PAGO_BD, pasarela_pago);
		resultado.put(C_NIF_CONTRIBUYENTE, nifContribuyente);
		resultado.put(C_FECHA_DEVENGO, fechaDevengo);
		resultado.put(C_DATO_ESPECIFICO, datoEspecifico);
		resultado.put(C_EXPEDIENTE, expediente);
		resultado.put(C_NIF_OPERANTE, nifOperante);
		resultado.put(C_IMPORTE, importe);
		//CRUBENCVS 04/03/2021
		resultado.put(C_OPERACION_EPST, operacion);
		resultado.put(C_MEDIO_PAGO, medioPago);
		resultado.put(C_HASH_DATOS, hashDatos);
		//FIN CRUBENCVS 04/03/2021
		//Para devolver el resultado de la llamada.
		//En CAOR se devuelve el código de resultado de la llamada y una descripción opcional.
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
		return resultado;
	}
	
	/*******************************************************************
	 * Metodo que obtiene el justificante asociado a un número único.
	 * Devuelve una hash con columna - valor
	 * 
	 * @param aplicacion
	 * @param numeroUnico
	 * @return
	 * @throws RemoteException
	 */
	public Map<String, String> consultaJustificante(String aplicacion, String numeroUnico) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado("SW_Pasarela2.ConsultaNumeroUnico");
		conversor.setParametro(aplicacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(numeroUnico, ConversorParametrosLanzador.TIPOS.String);
		
		String resultadoEjecutarPL = Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		String fecha_operacion = conversor.getNodoResultadoX(ESUN_C6);
		String fecha_pago = conversor.getNodoResultadoX(ESUN_C7);
		String estado = conversor.getNodoResultadoX(ESUN_C1);
		String servicio = conversor.getNodoResultadoX(ESUN_C2);
		String num_unico = conversor.getNodoResultadoX(ESUN_C3);
		String nume_oper = conversor.getNodoResultadoX(ESUN_C4);
		String nrc = conversor.getNodoResultadoX(ESUN_C5);
		String pasarela_pago = conversor.getNodoResultadoX(ESUN_C8);
		String nifContribuyente = conversor.getNodoResultadoX(ESUN_C9);
		String fechaDevengo = conversor.getNodoResultadoX(ESUN_C10);
		String datoEspecifico = conversor.getNodoResultadoX(ESUN_C11);
		String expediente = conversor.getNodoResultadoX(ESUN_C12);
		String nifOperante = conversor.getNodoResultadoX(ESUN_C13);
		String importe = conversor.getNodoResultadoX(ESUN_C14);
		String justificante = conversor.getNodoResultadoX(ESUN_C15);
		//CRUBENCVS 04/03/2021
		String operacion= conversor.getNodoResultadoX(ESUN_C16);
		String medioPago= conversor.getNodoResultadoX(ESUN_C17);
		String hashDatos= conversor.getNodoResultadoX(ESUN_C18);
		//FIN CRUBENCVS 04/03/2021
		Map<String, String> resultado = new HashMap<String, String>();
		resultado.put(C_FECHA_OPERACION, fecha_operacion);
		resultado.put(C_FECHA_PAGO, fecha_pago);
		resultado.put(C_ESTADO_PETICION_PATE, estado);
		resultado.put(C_APLICACION, servicio);
		resultado.put(C_NUM_UNICO, num_unico);
		resultado.put(C_NUM_OPERACION, nume_oper);
		resultado.put(C_NRC, nrc);
		resultado.put(C_PASARELA_PAGO_BD, pasarela_pago);
		resultado.put(C_NIF_CONTRIBUYENTE, nifContribuyente);
		resultado.put(C_FECHA_DEVENGO, fechaDevengo);
		resultado.put(C_DATO_ESPECIFICO, datoEspecifico);
		resultado.put(C_EXPEDIENTE, expediente);
		resultado.put(C_NIF_OPERANTE, nifOperante);
		resultado.put(C_IMPORTE, importe);
		resultado.put(C_JUSTIFICANTE_PETICION_PATE, justificante);
		// CRUBENCVS 04/03/2021
		resultado.put(C_OPERACION_EPST,operacion);
		resultado.put(C_MEDIO_PAGO,medioPago);
		resultado.put(C_HASH_DATOS, hashDatos);
		//FIN CRUBENCVS 04/03/2021
		//Para devolver el resultado de la llamada.
		//En CAOR se devuelve el código de resultado de la llamada y una descripción opcional.
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
		return resultado;
	}
	public Map<String, String> inicioAnulacion(String emisora, String numeroAutoliquidacion) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado("SW_Pasarela2.InicioAnulacion");
		conversor.setParametro(emisora, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(numeroAutoliquidacion, ConversorParametrosLanzador.TIPOS.String);
		
		String resultadoEjecutarPL = Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		/*String fecha_operacion = conversor.getNodoResultado(FECHA1_CANU);
		String fecha_pago = conversor.getNodoResultado(FECHA2_CANU);
		String estado = conversor.getNodoResultado(STRING1_CANU);
		String servicio = conversor.getNodoResultado(STRING2_CANU);
		String num_unico = conversor.getNodoResultado(STRING3_CANU);
		String nume_oper = conversor.getNodoResultado(STRING4_CANU);
		String nrc = conversor.getNodoResultado(STRING5_CANU);
		*/
		String fecha_operacion = conversor.getNodoResultadoX(ESUN_C6);
		String fecha_pago = conversor.getNodoResultadoX(ESUN_C7);
		String estado = conversor.getNodoResultadoX(ESUN_C1);
		String servicio = conversor.getNodoResultadoX(ESUN_C2);
		String num_unico = conversor.getNodoResultadoX(ESUN_C3);
		String nume_oper = conversor.getNodoResultadoX(ESUN_C4);
		String nrc = conversor.getNodoResultadoX(ESUN_C5);
		String pasarelaPago = conversor.getNodoResultadoX(ESUN_C8);
		String nifContribuyente = conversor.getNodoResultadoX(ESUN_C9);
		String fechaDevengo = conversor.getNodoResultadoX(ESUN_C10);
		String datoEspecifico = conversor.getNodoResultadoX(ESUN_C11);
		String expediente = conversor.getNodoResultadoX(ESUN_C12);
		String nifOperante = conversor.getNodoResultadoX(ESUN_C13);
		String importe = conversor.getNodoResultadoX(ESUN_C14);
		Map<String, String> resultado = new HashMap<String, String>();
		resultado.put(C_FECHA_OPERACION, fecha_operacion);
		resultado.put(C_FECHA_PAGO, fecha_pago);
		resultado.put(C_ESTADO_PETICION_PATE, estado);
		resultado.put(C_APLICACION, servicio);
		resultado.put(C_NUM_UNICO, num_unico);
		resultado.put(C_NUM_OPERACION, nume_oper);
		resultado.put(C_NRC, nrc);
		resultado.put (C_PASARELA_PAGO_BD, pasarelaPago);
		resultado.put(C_NIF_CONTRIBUYENTE, nifContribuyente);
		resultado.put(C_FECHA_DEVENGO, fechaDevengo);
		resultado.put(C_DATO_ESPECIFICO, datoEspecifico);
		resultado.put(C_EXPEDIENTE, expediente);
		resultado.put(C_NIF_OPERANTE, nifOperante);
		resultado.put(C_IMPORTE, importe);
		//Para devolver el resultado de la llamada.
		//En CAOR se devuelve el código de resultado de la llamada y una descripción opcional.
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
		return resultado;
	}
	public Map<String, String> inicioAnulacion(String emisora, String identificacion,String referencia) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado("SW_Pasarela2.InicioAnulacion");
		conversor.setParametro(emisora, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(identificacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(referencia, ConversorParametrosLanzador.TIPOS.String);
		
		String resultadoEjecutarPL = Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		/*String fecha_operacion = conversor.getNodoResultado(FECHA1_CANU);
		String fecha_pago = conversor.getNodoResultado(FECHA2_CANU);
		String estado = conversor.getNodoResultado(STRING1_CANU);
		String servicio = conversor.getNodoResultado(STRING2_CANU);
		String num_unico = conversor.getNodoResultado(STRING3_CANU);
		String nume_oper = conversor.getNodoResultado(STRING4_CANU);
		String nrc = conversor.getNodoResultado(STRING5_CANU);
		*/
		String fecha_operacion = conversor.getNodoResultadoX(ESUN_C6);
		String fecha_pago = conversor.getNodoResultadoX(ESUN_C7);
		String estado = conversor.getNodoResultadoX(ESUN_C1);
		String servicio = conversor.getNodoResultadoX(ESUN_C2);
		String num_unico = conversor.getNodoResultadoX(ESUN_C3);
		String nume_oper = conversor.getNodoResultadoX(ESUN_C4);
		String nrc = conversor.getNodoResultadoX(ESUN_C5);
		String pasarelaPago = conversor.getNodoResultadoX(ESUN_C8);
		String nifContribuyente = conversor.getNodoResultadoX(ESUN_C9);
		String fechaDevengo = conversor.getNodoResultadoX(ESUN_C10);
		String datoEspecifico = conversor.getNodoResultadoX(ESUN_C11);
		String expediente = conversor.getNodoResultadoX(ESUN_C12);
		String nifOperante = conversor.getNodoResultadoX(ESUN_C13);
		String importe = conversor.getNodoResultadoX(ESUN_C14);
		Map<String, String> resultado = new HashMap<String, String>();
		resultado.put(C_FECHA_OPERACION, fecha_operacion);
		resultado.put(C_FECHA_PAGO, fecha_pago);
		resultado.put(C_ESTADO_PETICION_PATE, estado);
		resultado.put(C_APLICACION, servicio);
		resultado.put(C_NUM_UNICO, num_unico);
		resultado.put(C_NUM_OPERACION, nume_oper);
		resultado.put(C_NRC, nrc);
		resultado.put (C_PASARELA_PAGO_BD,pasarelaPago);
		resultado.put (C_PASARELA_PAGO_BD, pasarelaPago);
		resultado.put(C_NIF_CONTRIBUYENTE, nifContribuyente);
		resultado.put(C_FECHA_DEVENGO, fechaDevengo);
		resultado.put(C_DATO_ESPECIFICO, datoEspecifico);
		resultado.put(C_EXPEDIENTE, expediente);
		resultado.put(C_NIF_OPERANTE, nifOperante);
		resultado.put(C_IMPORTE, importe);
		//Para devolver el resultado de la llamada.
		//En CAOR se devuelve el código de resultado de la llamada y una descripción opcional.
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
		return resultado;
	}
	/*******************************************************************
	 * Metodo que comprueba si los datos que se pasan por parámetro
	 * corresponden con los datos guardados en la base de datos de
	 * un pago.
	 * 
	 * @param emisora           
	 * @param	justificante      
     * @param nif               
	 * @param fechaDevengo     
	 * @param datoEspecifico   
	 * @param expediente
	 * @param importe        
	 * @param servicio          
	 * @param numeroUnico      
	 * @param nifOperante      
	 * @param tarjeta           
	 * @param fechaCaducidad   
	 * @return Estado del pago.
	 * @throws RemoteException
	 */
	public String datosCoherentes(String origen, String emisora, String justificante, String nif, String nombreContribuyente, String fechaDevengo,
			String datoEspecifico, String expediente, String identificacion, String referencia,String importe, String servicio, String numeroUnico, String nifOperante,
			String tarjeta, String fechaCaducidad) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado("SW_Pasarela2.datosCoherentes");
		conversor.setParametro(origen, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(emisora, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(importe, ConversorParametrosLanzador.TIPOS.Integer);
		conversor.setParametro(justificante, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(nif, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(fechaDevengo, ConversorParametrosLanzador.TIPOS.Date,"DDMMYYYY");
		conversor.setParametro(datoEspecifico, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(expediente, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(identificacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(referencia, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(servicio, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(numeroUnico, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(nifOperante, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(tarjeta, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(fechaCaducidad, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro("PAGADO", ConversorParametrosLanzador.TIPOS.String); //En este caso se pasará siempre 'PAGADO', para que devuelva datos.
		conversor.setParametro(nombreContribuyente, ConversorParametrosLanzador.TIPOS.String);
		
		
		String resultadoEjecutarPL = Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		//Para devolver el resultado de la llamada.
		//En CAOR se devuelve el código de resultado de la llamada y una descripción opcional.
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
		return conversor.getNodoResultado(STRING_CADE);
	}
	/*******************************************************************
	 * Metodo que obtiene el resultado de una datosProceso telematica
	 * Devuelve una hash con columna - valor
	 * En concreto seran las columnas Resultado y NRC
	 * 
	 * @param aplicacion
	 * @param numero_unico
	 * @return
	 * @throws RemoteException
	 */
	public Map<String, String> obtenerResultadoPate(String justificante) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado("SW_Pasarela2.ObtenerResultadoPate");
		conversor.setParametro(justificante, ConversorParametrosLanzador.TIPOS.String);
		
		String resultadoEjecutarPL = Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		//ArrayList<HashMap<String, String>> lista = conversor.Decodifica(resultadoEjecutarPL);

		Map<String, String> resultado = new HashMap<String, String>();
		resultado.put("Resultado",conversor.getNodoResultado(STRING1_CANU));
		resultado.put (C_NRC, conversor.getNodoResultado(STRING2_CANU));
		//Para devolver el resultado de la llamada.
		//En CAOR se devuelve el código de resultado de la llamada y una descripción opcional.
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
		return resultado;
	}
	public Map<String, String> obtenerResultadoPate(String identificacion, String referencia) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado("SW_Pasarela2.ObtenerResultadoPate");
		conversor.setParametro(identificacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(referencia, ConversorParametrosLanzador.TIPOS.String);
		
		String resultadoEjecutarPL = Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		//ArrayList<HashMap<String, String>> lista = conversor.Decodifica(resultadoEjecutarPL);

		Map<String, String> resultado = new HashMap<String, String>();
		resultado.put("Resultado",conversor.getNodoResultado(STRING1_CANU));
		resultado.put (C_NRC, conversor.getNodoResultado(STRING2_CANU));
		//Para devolver el resultado de la llamada.
		//En CAOR se devuelve el código de resultado de la llamada y una descripción opcional.
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
		return resultado;
	}
	/**
	 * Realiza el cambio de pasarela de pago en base de datos. De esta forma, la nueva pasarela de pago
	 * por la que se emitirán las peticiones remotas será la indicada.
	 * @param origen Origen de la petición.
	 * @param modalidad Modalidad de la petición. 
	 * @param emisora Emisora de la petición.
	 * @param justificante Número de justificante para modalidad autoliquidaciones.
	 * @param identificacion Número de identificación para modalidad
	 * @param referencia Número de identificación para modalidad liquidación.
	 * @param pasarelaPago Identificador de la nueva pasarela de pago.
	 * @throws RemoteException
	 */
	public void cambioPasarelaPago(String origen,String modalidad, String emisora, 
			String justificante,String identificacion, String referencia, String pasarelaPago) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado("SW_Pasarela2.cambioPasarelaPago");
		conversor.setParametro(origen, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(modalidad, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(emisora, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(justificante, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(identificacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(referencia, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(pasarelaPago, ConversorParametrosLanzador.TIPOS.String);
		
		String resultadoEjecutarPL = Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		//Para devolver el resultado de la llamada.
		//En CAOR se devuelve el código de resultado de la llamada y una descripción opcional.
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
		return;
	}
	
	/**
	 * Método que inicia el pago con tarjeta en base de datos, para la plataforma UniversalPay
	 * @param origen
	 * @param modalidad
	 * @param emisora
	 * @param nifContribuyente
	 * @param fechaDevengo
	 * @param justificante
	 * @param datoEspecifico
	 * @param expediente
	 * @param importe
	 * @param identificacion
	 * @param referencia
	 * @param nif_operante
	 * @param modelo
	 * @param plataformaPago
	 * @param procAlmacenado
	 * @return
	 * @throws RemoteException
	 */
	public Map<String,String> inicioPagoTarjetaUniversalPay(
					String origen,
					String modalidad, 
					String emisora, 
					String nifContribuyente, 
					String fechaDevengo,
					String justificante,
					String datoEspecifico, 
					String expediente, 
					String importe, 
					String identificacion, 
					String referencia,
					String nif_operante,
					String modelo, 
					String plataformaPago,
					String procAlmacenado) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado(procAlmacenado);
		conversor.setParametro(origen, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(modalidad, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(emisora, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(importe, ConversorParametrosLanzador.TIPOS.Integer);
		conversor.setParametro(justificante, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(nifContribuyente, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(fechaDevengo, ConversorParametrosLanzador.TIPOS.Date,"DDMMYYYY");
		conversor.setParametro(datoEspecifico, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(expediente, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(identificacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(referencia, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(nif_operante, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(modelo, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(plataformaPago, ConversorParametrosLanzador.TIPOS.String);
		
		String resultadoEjecutarPL = Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		String estado = conversor.getNodoResultado(STRING1_CANU);
		String operacion= conversor.getNodoResultado(STRING2_CANU);
		String pasarela= conversor.getNodoResultado(STRING3_CANU);
		Map<String, String> resultado = new HashMap<String, String>();
		resultado.put(C_ESTADO_PETICION_PATE, estado);
		resultado.put(C_OPERACION_EPST, operacion);
		resultado.put(C_PASARELA_PAGO_BD, pasarela);
		//Para devolver el resultado de la llamada.
		//En CAOR se devuelve el código de resultado de la llamada y una descripción opcional.
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
		return resultado;
	}
	
	/**
	 * Finalización de un pago en base de datos
	 * @param operacionEpst
	 * @param estado
	 * @param resultado
	 * @param numOperacion
	 * @param nrc
	 * @param fechaPago
	 * @param procAlmacenado
	 * @throws RemoteException
	 */
	public void finalizarPagoTarjeta(
								String operacionEpst,
								String estado,
								String resultado,
								String numOperacion,
								String nrc,
								String fechaPago,
								// CRUBENCVS 11/11/2021
								String emisora,
								String justificante,
								String identificacion,
								String referencia,
								// FIN CRUBENCVS 11/11/2021
								String procAlmacenado
						 ) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado(procAlmacenado);
		conversor.setParametro(operacionEpst, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(estado, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(resultado, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(numOperacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(nrc, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro (fechaPago, ConversorParametrosLanzador.TIPOS.Date,"YYYY-MM-DD HH24:MI:SS");
		//CRUBENCVS 11/11/2021
		conversor.setParametro (emisora, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro (justificante, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro (identificacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro (referencia, ConversorParametrosLanzador.TIPOS.String);
		//FIN  CRUBENCVS 11/11/2021
		String resultadoEjecutarPL =Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));	
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
	}
	/**
	 * Inicio de la anulación de pago por tarjeta en plataforma de pago
	 * @param emisora
	 * @param numeroAutoliquidacion
	 * @param identificacion
	 * @param referencia
	 * @param procAlmacenado
	 * @throws RemoteException
	 */
	public void inicioAnulacionPagoTarjeta(
			String emisora,
			String numeroAutoliquidacion,
			String identificacion,
			String referencia,
			String procAlmacenado
	 ) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado(procAlmacenado);
		conversor.setParametro(emisora, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(numeroAutoliquidacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(identificacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(referencia, ConversorParametrosLanzador.TIPOS.String);
		String resultadoEjecutarPL =Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));	
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
	}
	/**
	 * Finaliza la anulación de pago con tarjeta, poniendo el estado que se indique.
	 * @param emisora
	 * @param numeroAutoliquidacion
	 * @param identificacion
	 * @param referencia
	 * @param procAlmacenado
	 * @throws RemoteException
	 */
	public void finAnulacionPagoTarjeta(
			String emisora,
			String numeroAutoliquidacion,
			String identificacion,
			String referencia,
			String estado,
			String codResultado,
			String procAlmacenado
	 ) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado(procAlmacenado);
		conversor.setParametro(emisora, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(numeroAutoliquidacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(identificacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(referencia, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(estado, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(codResultado, ConversorParametrosLanzador.TIPOS.String);
		String resultadoEjecutarPL =Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));	
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
	}
	
	/**
	 * Llama a base de datos para validar una MAC
	 * @param origen
	 * @param datosMac
	 * @param macRecibida
	 * @throws RemoteException
	 */
	public boolean validarMac(
			String origen,
			String datosMac,
			String macRecibida
	 ) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado(preferencias.getPAValidarMac());
		conversor.setParametro(origen, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(datosMac, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(macRecibida, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro("P", ConversorParametrosLanzador.TIPOS.String);
		String resultadoEjecutarPL =Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		String error= conversor.getNodoResultado(ERRORNODE);
		if (error==null || "".equals(error)){
			String valido= conversor.getNodoResultado(STRING_CADE);
			return ("S".equals(valido)?true:false);
		} else {
			return false;
		}
	}
	/**
	 * Generación de una MAC. Utilizado en los pagos por plataform
	 * @param origen Origen de la petición. En función del valor, se utilizará una clave u otra para la generación.
	 * @param datosMac
	 * @param macRecibida
	 * @return
	 * @throws RemoteException
	 */
	public String generarMac(
			String origen,
			String datosMac
	 ) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado(preferencias.getPAGenerarMAC());
		conversor.setParametro(origen, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(datosMac, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro("P", ConversorParametrosLanzador.TIPOS.String);
		String resultadoEjecutarPL =Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		String error= conversor.getNodoResultado(ERRORNODE);
		if (error==null || "".equals(error)){
			String mac= conversor.getNodoResultado(STRING_CADE);
			return mac;
		} else {
			return "";
		}
	}
	/**
	 * Inicio de la operación de pago para PA
	 * @param origen
	 * @param modalidad
	 * @param emisora
	 * @param importe
	 * @param nifContribuyente
	 * @param fechaDevengo
	 * @param datoEspecifico
	 * @param aplicacion
	 * @param numeroUnico
	 * @param nif_operante
	 * @param modelo
	 * @return
	 * @throws RemoteException
	 */
	public Map<String,String> inicioOperacionPago(
			String origen,
			String modalidad, 
			String emisora,
			String importe, 
			String nifContribuyente, 
			String fechaDevengo,
			String datoEspecifico, 
			String aplicacion,
			String numeroUnico,
			String nif_operante,
			String modelo 
     ) throws RemoteException
	{
	conversor.setProcedimientoAlmacenado(preferencias.getPAInicioOperacionPago());
	conversor.setParametro(origen, ConversorParametrosLanzador.TIPOS.String);
	conversor.setParametro(modalidad, ConversorParametrosLanzador.TIPOS.String);
	conversor.setParametro(emisora, ConversorParametrosLanzador.TIPOS.String);
	conversor.setParametro(importe, ConversorParametrosLanzador.TIPOS.Integer);
	conversor.setParametro(nifContribuyente, ConversorParametrosLanzador.TIPOS.String);
	conversor.setParametro(fechaDevengo, ConversorParametrosLanzador.TIPOS.Date,"DDMMYYYY");
	conversor.setParametro(datoEspecifico, ConversorParametrosLanzador.TIPOS.String);
	conversor.setParametro(aplicacion, ConversorParametrosLanzador.TIPOS.String);
	conversor.setParametro(numeroUnico, ConversorParametrosLanzador.TIPOS.String);
	conversor.setParametro(nif_operante, ConversorParametrosLanzador.TIPOS.String);
	conversor.setParametro(modelo, ConversorParametrosLanzador.TIPOS.String);
	
	String resultadoEjecutarPL = Ejecuta();
	conversor.setResultado(resultadoEjecutarPL);
	this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
	String estado = conversor.getNodoResultado(STRING1_CANU);
	String hash_datos= conversor.getNodoResultado(STRING3_CANU);
	Map<String, String> resultado = new HashMap<String, String>();
	resultado.put(C_ESTADO_PETICION_PATE, estado);
	resultado.put(C_HASH_DATOS, hash_datos);
	//Para devolver el resultado de la llamada.
	//En CAOR se devuelve el código de resultado de la llamada y una descripción opcional.
	String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
	this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
	return resultado;
	}
	/**
	 * Consulta al inicio de pago con tarjeta por plataformas nuevas.
	 * Comprobará si ya existe el pago, si los datos de pago han cambiado,
	 * y si es un pago por pasarela antigua.
	 * @param origen
	 * @param modalidad
	 * @param emisora
	 * @param importe
	 * @param justificante
	 * @param nifContribuyente
	 * @param fechaDevengo
	 * @param datoEspecifico
	 * @param expediente
	 * @param identificacion
	 * @param referencia
	 * @param nifOperante
	 * @param modelo
	 * @return
	 * @throws RemoteException
	 */
	public Map<String, String> consultaInicioPagoTajeta(
			String origen, 
			String modalidad, 
			String emisora,
			String importe,
			String justificante, 
			String nifContribuyente,
			String fechaDevengo,
			String datoEspecifico,
			String expediente,
			String identificacion, 
			String referencia,
			String nifOperante,
			String modelo
			) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado(preferencias.getPAConsultaInicioPagoTarjeta());
		conversor.setParametro(origen, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(modalidad, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(emisora, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(importe, ConversorParametrosLanzador.TIPOS.Integer);
		conversor.setParametro(justificante, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(nifContribuyente, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(fechaDevengo, ConversorParametrosLanzador.TIPOS.Date,"DDMMYYYY");
		conversor.setParametro(datoEspecifico, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(expediente, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(identificacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(referencia, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(nifOperante, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(modelo, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro("P", ConversorParametrosLanzador.TIPOS.String);
		
		String resultadoEjecutarPL = Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		Map<String, String> resultado = new HashMap<String, String>();
		resultado.put(C_ESTADO_PETICION_PATE, conversor.getNodoResultadoX(ESUN_C1));
		resultado.put(C_APLICACION, conversor.getNodoResultadoX(ESUN_C2));
		resultado.put(C_NUM_UNICO, conversor.getNodoResultadoX(ESUN_C3));
		resultado.put(C_NUM_OPERACION, conversor.getNodoResultadoX(ESUN_C4));
		resultado.put(C_NRC, conversor.getNodoResultadoX(ESUN_C5));
		resultado.put(C_FECHA_OPERACION, conversor.getNodoResultadoX(ESUN_C6));
		resultado.put(C_FECHA_PAGO, conversor.getNodoResultadoX(ESUN_C7));
		resultado.put(C_PASARELA_PAGO_BD, conversor.getNodoResultadoX(ESUN_C8));
		resultado.put(C_NIF_CONTRIBUYENTE, conversor.getNodoResultadoX(ESUN_C9));
		resultado.put(C_FECHA_DEVENGO, conversor.getNodoResultadoX(ESUN_C10));
		resultado.put(C_DATO_ESPECIFICO, conversor.getNodoResultadoX(ESUN_C11));
		resultado.put(C_EXPEDIENTE, conversor.getNodoResultadoX(ESUN_C12));
		resultado.put(C_NIF_OPERANTE, conversor.getNodoResultadoX(ESUN_C13));
		resultado.put(C_IMPORTE, conversor.getNodoResultadoX(ESUN_C14));
		resultado.put(C_OPERACION_EPST,conversor.getNodoResultadoX(ESUN_C15));
		resultado.put(C_MEDIO_PAGO,conversor.getNodoResultadoX(ESUN_C16));
		resultado.put(C_HASH_DATOS, conversor.getNodoResultadoX(ESUN_C17));
		resultado.put(C_DATOS_CONSISTENTES, conversor.getNodoResultadoX(ESUN_N0));
		//Para devolver el resultado de la llamada.
		//En CAOR se devuelve el código de resultado de la llamada y una descripción opcional.
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
		return resultado;
	}
	
	/**
	 * Consulta que se realiza al inicio de la operación de pago por parte 
	 * del Principado de Asturias, para conocer el estado del registro anterior,
	 * si hubiera
	 * @param origen
	 * @param modalidad
	 * @param emisora
	 * @param modelo
	 * @param importe
	 * @param nifContribuyente
	 * @param fechaDevengo
	 * @param datoEspecifico
	 * @param expediente
	 * @param nifOperante
	 * @param aplicacion
	 * @param numeroUnico
	 * @return
	 * @throws RemoteException
	 */
	public Map<String, String> consultaInicioOperacionPago(
			String origen, 
			String modalidad, 
			String emisora,
			String modelo,
			String importe,
			String nifContribuyente,
			String fechaDevengo,
			String datoEspecifico,
			String nifOperante,
			String aplicacion,
			String numeroUnico
			) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado(preferencias.getPAConsultaInicioOperacionPago());
		conversor.setParametro(origen, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(modalidad, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(emisora, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(modelo, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(importe, ConversorParametrosLanzador.TIPOS.Integer);
		conversor.setParametro(nifContribuyente, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(fechaDevengo, ConversorParametrosLanzador.TIPOS.Date,"DDMMYYYY");
		conversor.setParametro(datoEspecifico, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(nifOperante, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(aplicacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(numeroUnico, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro("P", ConversorParametrosLanzador.TIPOS.String);
		
		String resultadoEjecutarPL = Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		Map<String, String> resultado = new HashMap<String, String>();
		resultado.put(C_ESTADO_PETICION_PATE, conversor.getNodoResultadoX(ESUN_C1));
		resultado.put(C_APLICACION, conversor.getNodoResultadoX(ESUN_C2));
		resultado.put(C_NUM_UNICO, conversor.getNodoResultadoX(ESUN_C3));
		resultado.put(C_NUM_OPERACION, conversor.getNodoResultadoX(ESUN_C4));
		resultado.put(C_NRC, conversor.getNodoResultadoX(ESUN_C5));
		resultado.put(C_FECHA_OPERACION, conversor.getNodoResultadoX(ESUN_C6));
		resultado.put(C_FECHA_PAGO, conversor.getNodoResultadoX(ESUN_C7));
		resultado.put(C_PASARELA_PAGO_BD, conversor.getNodoResultadoX(ESUN_C8));
		resultado.put(C_NIF_CONTRIBUYENTE, conversor.getNodoResultadoX(ESUN_C9));
		resultado.put(C_FECHA_DEVENGO, conversor.getNodoResultadoX(ESUN_C10));
		resultado.put(C_DATO_ESPECIFICO, conversor.getNodoResultadoX(ESUN_C11));
		resultado.put(C_EXPEDIENTE, conversor.getNodoResultadoX(ESUN_C12));
		resultado.put(C_NIF_OPERANTE, conversor.getNodoResultadoX(ESUN_C13));
		resultado.put(C_IMPORTE, conversor.getNodoResultadoX(ESUN_C14));
		resultado.put(C_OPERACION_EPST,conversor.getNodoResultadoX(ESUN_C15));
		resultado.put(C_MEDIO_PAGO,conversor.getNodoResultadoX(ESUN_C16));
		resultado.put(C_HASH_DATOS, conversor.getNodoResultadoX(ESUN_C17));
		resultado.put(C_JUSTIFICANTE_PETICION_PATE, conversor.getNodoResultadoX(ESUN_C19));
		resultado.put(C_DATOS_CONSISTENTES, conversor.getNodoResultadoX(ESUN_N0));
		//Para devolver el resultado de la llamada.
		//En CAOR se devuelve el código de resultado de la llamada y una descripción opcional.
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
		return resultado;
	}
	
	/**
	 * Método que inicia el pago con tarjeta en base de datos, para la plataforma UniversalPay
	 * @param origen
	 * @param modalidad
	 * @param emisora
	 * @param nifContribuyente
	 * @param fechaDevengo
	 * @param justificante
	 * @param datoEspecifico
	 * @param expediente
	 * @param importe
	 * @param identificacion
	 * @param referencia
	 * @param nif_operante
	 * @param modelo
	 * @param plataformaPago
	 * @param procAlmacenado
	 * @return
	 * @throws RemoteException
	 */
	public void convertirPagoAntiguoAUniversalPay(
					String emisora, 
					String justificante,
					String identificacion, 
					String referencia,
					String procAlmacenado) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado(procAlmacenado);
		conversor.setParametro(emisora, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(justificante, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(identificacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(referencia, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro("P", ConversorParametrosLanzador.TIPOS.String);
		
		String resultadoEjecutarPL = Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		//En CAOR se devuelve el código de resultado de la llamada y una descripción opcional.
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
		return;
	}
    /**
     * Consulta de pago por tarjeta en base de datos. Devolverá datos del pago 
     * por tarjeta y la lista de operacion_epst
     * La lista de operaciones nunca es nula, en todo caso devuelve una lista de operaciones vacía.
     * @param origen
     * @param emisora
     * @param justificante
     * @param identificacion
     * @param referencia
     * @param aplicacion
     * @param numeroUnico
     * @return
     * @throws RemoteException
     */
	public ResultadoConsultaPagoTarjetaBD consultaPagoTarjeta(
			String origen, 
			String emisora,
			String justificante, 
			String identificacion, 
			String referencia,
			String aplicacion,
			String numeroUnico
			) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado(preferencias.getPAConsultaPagoTarjeta());
		conversor.setParametro(origen, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(emisora, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(justificante, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(identificacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(referencia, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(aplicacion, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(numeroUnico, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro("P", ConversorParametrosLanzador.TIPOS.String);
		
		String resultadoEjecutarPL = Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		ResultadoConsultaPagoTarjetaBD resultado = new ResultadoConsultaPagoTarjetaBD();
		
		resultado.setEstado(conversor.getNodoResultadoX(ESUN_C1));
		resultado.setAplicacion(conversor.getNodoResultadoX(ESUN_C2));
		resultado.setNumeroUnico(conversor.getNodoResultadoX(ESUN_C3));
		resultado.setNumeroOperacion(conversor.getNodoResultadoX(ESUN_C4));
		resultado.setNrc(conversor.getNodoResultadoX(ESUN_C5));
		resultado.setFechaOperacion(conversor.getNodoResultadoX(ESUN_C6));
		resultado.setFechaPago(conversor.getNodoResultadoX(ESUN_C7));
		resultado.setPasarelaPago(conversor.getNodoResultadoX(ESUN_C8));
		resultado.setNifContribuyente(conversor.getNodoResultadoX(ESUN_C9));
		resultado.setFechaDevengo(conversor.getNodoResultadoX(ESUN_C10));
		resultado.setDatoEspecifico(conversor.getNodoResultadoX(ESUN_C11));
		resultado.setExpediente(conversor.getNodoResultadoX(ESUN_C12));
		resultado.setNifOperante(conversor.getNodoResultadoX(ESUN_C13));
		resultado.setImporte(conversor.getNodoResultadoX(ESUN_C14));
		resultado.setOperacionEpst(conversor.getNodoResultadoX(ESUN_C15));
		resultado.setMedioPago(conversor.getNodoResultadoX(ESUN_C16));
		resultado.setHashDatos(conversor.getNodoResultadoX(ESUN_C17));
		resultado.setJustificante(conversor.getNodoResultadoX(ESUN_C19));
		//CRUBENCVS 47535
		resultado.setIdentificacion(conversor.getNodoResultadoX(ESUN_C20));
		resultado.setReferencia(conversor.getNodoResultadoX(ESUN_C21));
		resultado.setOrigen(conversor.getNodoResultadoX(ESUN_C22));
		resultado.setModalidad(conversor.getNodoResultadoX(ESUN_C23));
		resultado.setFechaAnulacion(conversor.getNodoResultadoX(ESUN_C24));
		resultado.setEmisora(conversor.getNodoResultadoX(ESUN_C25));
		resultado.setNombreContribuyente(conversor.getNodoResultadoX(ESUN_C26));
		//FIN CRUBENCVS 47535
		
		//Devuelvo también la lista de OPERACION_EPST que pueda haber devuelto
		String[] operaciones= conversor.getNodosResultadoX(STRING_CADE);
		if (operaciones!=null){
			resultado.setNumerosOperacionEpst(operaciones);
		} else {
			resultado.setNumerosOperacionEpst(new String[0]);
		}
		//Para devolver el resultado de la llamada.
		//En CAOR se devuelve el código de resultado de la llamada y una descripción opcional.
		String codResultadoBD= conversor.getNodoResultado(ORDEN_CAOR);
		this.infoUltimaLlamada= new InfoLlamada(codResultadoBD);
		if (infoUltimaLlamada.isError())
		{
			if ("9995".equals(infoUltimaLlamada.getCodErrorServicio())){
				resultado.setError(false);
				resultado.setHayDatos(false);
			} else {
				resultado.setError(true);
			}
		} else {
			resultado.setError(false);
			resultado.setHayDatos(true);
		}
		return resultado;
	}
	
	
	
		
	/**
	 * Recupera una estructura InfoLlamada con los datos de la última llamada, para saber si se ha
	 * producido un error controlado y cual, en su caso.
	 * @return {@link InfoLlamada} con los datos de la última llamada.
	 */
	public InfoLlamada getInfoUltimaLlamada()
	{
		return infoUltimaLlamada;
	}
	/**
	 * Ejecuta un procedimiento almacenado en la base de datos mediante el servicio lanzador.
	 * @return String con los datos de XML de la respuesta del servicio.
	 * @throws RemoteException Si se produce un error de conexión.
	 */
	private String Ejecuta() throws RemoteException
	{
		return lanzaderaPort.executePL(
				preferencias.getEsquemaBaseDatos(),
				conversor.codifica(),
				"", "", "", "");
	}
	
	/**
	 * Método que devuelve el error en la ultima llamada a un procedimiento de esta clase.
	 * Los errores en llamada son devueltos en formato XML, este procedimiento devuelve el error
	 * extraído en formato de cadena.
	 * @return
	 */
	public String getErrorLlamada() {
		return errorLlamada;
	}

	private void  setErrorLlamada(String errorLlamada) {
		this.errorLlamada = errorLlamada;
	}
}
