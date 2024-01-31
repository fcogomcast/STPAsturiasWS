package es.tributasenasturias.dao;

import es.tributasenasturias.utils.Constantes;


/**
 * Modela los datos de proceso genérico, las clases heredarán de ella para pago, consulta o anulación a nivel de STPA.
 * Contendrá la petición tal cual entra al servicio, y cualquier dato recuperado o 
 * calculado que se necesite para identificar cada operación.
 * Será el parámetro a varias de las funciones de comunicación con los proxies de pasarelas remotas.
 * @author crubencvs
 *
 */
public class DatosProceso {
	
	private DatosEntradaServicio peticionServicio;
	private String fechaOperacion;
	private String numeroOperacion;
	private String nrc;
	private String estado;
	private String justificante;
	private String fechaPago;
	private String aplicacion;
	private String numeroUnico;
	private String pasarelaPagoPeticion;
	private String nifContribuyente;
	private String fechaDevengo;
	private String datoEspecifico;
	private String expediente;
	private String nifOperante;
	private String importe;
	private TipoLlamada tipoLlamada;
	
	
	/**
	 * @return the tipoLlamada
	 */
	public final TipoLlamada getTipoLlamada() {
		return tipoLlamada;
	}
	/**
	 * @param tipoLlamada the tipoLlamada to set
	 */
	public final void setTipoLlamada(TipoLlamada tipoLlamada) {
		this.tipoLlamada = tipoLlamada;
	}
	/**
	 * @return the pasarelaPago
	 */
	public final String getPasarelaPagoPeticion() {
		return pasarelaPagoPeticion;
	}
	/**
	 * @param pasarelaPagoPeticion the pasarelaPago to set
	 */
	public final void setPasarelaPagoPeticion(String pasarelaPagoPeticion) {
		this.pasarelaPagoPeticion = pasarelaPagoPeticion;
	}
	/**
	 * @return the aplicacion
	 */
	public String getAplicacion() {
		return aplicacion;
	}
	/**
	 * @param aplicacion the aplicacion to set
	 */
	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}
	/**
	 * @return the numeroUnico
	 */
	public String getNumeroUnico() {
		return numeroUnico;
	}
	/**
	 * @param numeroUnico the numeroUnico to set
	 */
	public void setNumeroUnico(String numeroUnico) {
		this.numeroUnico = numeroUnico;
	}
	/**
	 * @return the fechaPago
	 */
	public String getFechaPago() {
		return fechaPago;
	}
	/**
	 * @param fechaPago the fechaPago to set
	 */
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}
	/**
	 * @return the justificante
	 */
	public String getJustificante() {
		return justificante;
	}
	/**
	 * @param justificante the justificante to set
	 */
	public void setJustificante(String justificante) {
		this.justificante = justificante;
	}
	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return the peticionServicio
	 */
	public DatosEntradaServicio getPeticionServicio() {
		return peticionServicio;
	}
	/**
	 * @param peticionServicio the peticionServicio to set
	 */
	public void setPeticionServicio(DatosEntradaServicio peticionServicio) {
		this.peticionServicio = peticionServicio;
	}
	/**
	 * @return the fechaOperacion
	 */
	public String getFechaOperacion() {
		return fechaOperacion;
	}
	/**
	 * @param fechaOperacion the fechaOperacion to set
	 */
	public void setFechaOperacion(String fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}
	/**
	 * @return the numeroOperacion
	 */
	public String getNumeroOperacion() {
		return numeroOperacion;
	}
	/**
	 * @param numeroOperacion the numeroOperacion to set
	 */
	public void setNumeroOperacion(String numeroOperacion) {
		this.numeroOperacion = numeroOperacion;
	}
	/**
	 * @return the nrc
	 */
	public String getNrc() {
		return nrc;
	}
	/**
	 * @param nrc the nrc to set
	 */
	public void setNrc(String nrc) {
		this.nrc = nrc;
	}
	public DatosProceso (DatosEntradaServicio peticion)
	{
		this.peticionServicio=peticion;
		if(peticionServicio.getOrigen().equals(Constantes.getOrigenServicioWeb()) && peticionServicio.getModalidad().equals(Constantes.getModalidadAutoliquidacion()))
		{
			this.tipoLlamada=TipoLlamada.SERVICIO_WEB_AUTOLIQUIDACION;
		}
		else if ((peticionServicio.getOrigen().equals(Constantes.getOrigenPortal()) && 
				  peticionServicio.getModalidad().equals(Constantes.getModalidadAutoliquidacion())))
		{
			this.tipoLlamada=TipoLlamada.PORTAL_AUTOLIQUIDACION; 
		}
		else if (peticionServicio.getOrigen().equals(Constantes.getOrigenS1()) && 
				  peticionServicio.getModalidad().equals(Constantes.getModalidadAutoliquidacion()))
		{
			this.tipoLlamada=TipoLlamada.S1_AUTOLIQUIDACION; 
		}
		else if ((peticionServicio.getOrigen().equals(Constantes.getOrigenPortal()) && 
				  peticionServicio.getModalidad().equals(Constantes.getModalidadLiquidacion())))
		{
			this.tipoLlamada=TipoLlamada.PORTAL_LIQUIDACION; 
		}
		else if (peticionServicio.getOrigen().equals(Constantes.getOrigenS1()) && 
				  peticionServicio.getModalidad().equals(Constantes.getModalidadLiquidacion()))
		{
			this.tipoLlamada=TipoLlamada.S1_LIQUIDACION; 
		}
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
	
}
