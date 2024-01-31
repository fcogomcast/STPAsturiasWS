package es.tributasenasturias.business.vo;

public class PagoVO {

	private String m_origen = new String();
	private String m_modalidad = new String();
	private String m_emisora = new String();
	private String m_nifsp = new String();
	private String m_fechaDevengo = new String();
	private String m_datoEspecifico = new String();
	private String m_numeroAutoliquidacion = new String();
	private String m_importe = new String();
	private String m_numTarjeta = new String();
	private String m_fechaCaducidadTarjeta = new String();
	private String m_nifOperante = new String();
	private String m_ccc = new String();

	// Setters
	public void setOrigen(String s) {
		this.m_origen = s;
	}
	
	public void setModalidad(String s) {
		this.m_modalidad = s;
	}
	
	public void setEmisora(String s) {
		this.m_emisora = s;
	}
	
	public void setNifSp(String s) {
		this.m_nifsp = s;
	}
	
	public void setFechaDevengo(String s) {
		this.m_fechaDevengo = s;
	}
	
	public void setDatoEspecifico(String s) {
		this.m_datoEspecifico = s;
	}
	
	public void setNumeroAutoliquidacion(String s) {
		this.m_numeroAutoliquidacion = s;
	}
	
	public void setImporte(String s) {
		this.m_importe = s;
	}
	
	public void setNumTarjeta(String s) {
		this.m_numTarjeta = s;
	}
	
	public void setFechaCaducidadTarjeta(String s) {
		this.m_fechaCaducidadTarjeta = s;
	}
	
	public void setNifOperante(String s) {
		this.m_nifOperante = s;
	}
	
	public void setCCC(String s) {
		this.m_ccc = s;
	}
	
	// Getters
	public String getOrigen() {
		return this.m_origen;
	}
	
	public String getModalidad() {
		return this.m_modalidad;
	}
	
	public String getEmisora() {
		return this.m_emisora;
	}
	
	public String getNifSp() {
		return this.m_nifsp;
	}
	
	public String getFechaDevengo() {
		return this.m_fechaDevengo;
	}
		
	public String getDatoEspecifico() {
		return this.m_datoEspecifico;
	}
	
	public String getNumeroAutoliquidacion() {
		return this.m_numeroAutoliquidacion;
	}
	
	public String getImporte() {
		return this.m_importe;
	}
	
	public String getNumTarjeta() {
		return this.m_numTarjeta;
	}
	
	public String getFechaCaducidadTarjeta() {
		return this.m_fechaCaducidadTarjeta;
	}
	
	public String getNifOperante() {
		return this.m_nifOperante;
	}
	
	public String getCCC() {
		return this.m_ccc;
	}
}
