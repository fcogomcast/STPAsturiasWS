package es.tributasenasturias.tarjetas.core;

import es.tributasenasturias.webservices.types.InicioOperacionPagoRequest;
import es.tributasenasturias.webservices.types.InicioPagoTarjetaRequest;


public class PeticionPagoTarjetaParameters {
		private final String origen;
		private final String modalidad;
		private final String entidad;
		private final String emisora;
		private final String modelo;
		private final String nifContribuyente;
		private final String fechaDevengo;
		private final String datoEspecifico;
		private final String identificacion;
		private final String referencia;
		private final String numeroAutoliquidacion;
		private final String expediente;
		private final String importe;
		private final String nifOperante;
		private final String numeroPeticion;
		private final String aplicacion;
		private final String numeroUnico;
		private final String mac;
		private String plataformaPago;
		
		/**
		 * Creación para la operación de inicio de pago por tarjeta
		 * @param request
		 */
		public PeticionPagoTarjetaParameters(InicioPagoTarjetaRequest request){
			this.origen= request.getOrigen();
			this.modalidad= request.getModalidad();
			this.entidad= request.getEntidad();
			this.emisora= request.getEmisora();
			this.modelo= request.getModelo();
			this.nifContribuyente= request.getNifContribuyente();
			this.fechaDevengo= request.getFechaDevengo();
			this.datoEspecifico= request.getDatoEspecifico();
			this.identificacion= request.getIdentificacion();
			this.referencia= request.getReferencia();
			this.numeroAutoliquidacion= request.getNumeroAutoliquidacion();
			this.expediente= request.getExpediente();
			this.importe= request.getImporte();
			this.nifOperante= request.getNifOperante();
			this.numeroPeticion= request.getNumeroPeticion();
			this.aplicacion= "";
			this.numeroUnico="";
			this.mac="";
			this.plataformaPago=request.getPlataformaPago();
		}
		
		/**
		 * Creación para la operación de inicio de operación de pago del Principado
		 * @param request
		 */
		public PeticionPagoTarjetaParameters(InicioOperacionPagoRequest request){
			this.origen= request.getOrigen();
			this.modalidad= request.getModalidad();
			this.entidad= "";
			this.emisora= request.getEmisora();
			this.modelo= request.getModelo();
			this.nifContribuyente= request.getNifContribuyente();
			this.fechaDevengo= request.getFechaDevengo();
			this.datoEspecifico= request.getDatoEspecifico();
			this.identificacion= "";
			this.referencia= "";
			this.numeroAutoliquidacion= "";
			this.expediente= "";
			this.importe= request.getImporte();
			this.nifOperante= request.getNifOperante();
			this.numeroPeticion= "";
			this.aplicacion= request.getAplicacion();
			this.numeroUnico=request.getNumeroUnico();
			this.mac=request.getMac();
			this.plataformaPago= "";
		}

		/**
		 * @return the plataformaPago
		 */
		public final String getPlataformaPago() {
			return plataformaPago;
		}

		/**
		 * @param plataformaPago the plataformaPago to set
		 */
		public final void setPlataformaPago(String plataformaPago) {
			this.plataformaPago = plataformaPago;
		}

		/**
		 * @return the origen
		 */
		public final String getOrigen() {
			return origen;
		}

		/**
		 * @return the modalidad
		 */
		public final String getModalidad() {
			return modalidad;
		}

		/**
		 * @return the entidad
		 */
		public final String getEntidad() {
			return entidad;
		}

		/**
		 * @return the emisora
		 */
		public final String getEmisora() {
			return emisora;
		}

		/**
		 * @return the modelo
		 */
		public final String getModelo() {
			return modelo;
		}

		/**
		 * @return the nifContribuyente
		 */
		public final String getNifContribuyente() {
			return nifContribuyente;
		}

		/**
		 * @return the fechaDevengo
		 */
		public final String getFechaDevengo() {
			return fechaDevengo;
		}

		/**
		 * @return the datoEspecifico
		 */
		public final String getDatoEspecifico() {
			return datoEspecifico;
		}

		/**
		 * @return the identificacion
		 */
		public final String getIdentificacion() {
			return identificacion;
		}

		/**
		 * @return the referencia
		 */
		public final String getReferencia() {
			return referencia;
		}

		/**
		 * @return the numeroAutoliquidacion
		 */
		public final String getNumeroAutoliquidacion() {
			return numeroAutoliquidacion;
		}

		/**
		 * @return the expediente
		 */
		public final String getExpediente() {
			return expediente;
		}

		/**
		 * @return the importe
		 */
		public final String getImporte() {
			return importe;
		}

		/**
		 * @return the nifOperante
		 */
		public final String getNifOperante() {
			return nifOperante;
		}

		/**
		 * @return the numeroPeticion
		 */
		public final String getNumeroPeticion() {
			return numeroPeticion;
		}

		/**
		 * @return the aplicacion
		 */
		public final String getAplicacion() {
			return aplicacion;
		}

		/**
		 * @return the numeroUnico
		 */
		public final String getNumeroUnico() {
			return numeroUnico;
		}

		/**
		 * @return the mac
		 */
		public final String getMac() {
			return mac;
		}
		
		
		
}
