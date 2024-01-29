package com.stpa.ws.server.bean;

public class LiquidacionTasasLiquidacion {
	private String notificacion = "";
	private String centroGestor = "";
	private String codTasa = "";
	private String fechaDevengo = "";
	private LiquidacionTasasDetalleLiquidacion[] detalleLiquidacion=null;//new LiquidacionTasasDetalleLiquidacion[5];
	private String porcBonificacion = "";
	private String porcRecargo = "";
	private String impRecargo = "";
	private String impIntereses = "";
	private LiquidacionTasasDatosSP datosSP = new LiquidacionTasasDatosSP();
	private LiquidacionTasasDatosPresentador datosPresentador = new LiquidacionTasasDatosPresentador();
	private String objLiquidacion = "";
	private String expExterno = "";
	private String expGestion = "";
	private String periodo = "";
	private String fechaAcuerdo = "";
	
	public String getNotificacion() {
		return notificacion;
	}
	public void setNotificacion(String notificacion) {
		this.notificacion = notificacion;
	}
	public String getCentroGestor() {
		return centroGestor;
	}
	public void setCentroGestor(String centroGestor) {
		this.centroGestor = centroGestor;
	}
	public String getCodTasa() {
		return codTasa;
	}
	public void setCodTasa(String codTasa) {
		this.codTasa = codTasa;
	}
	public String getFechaDevengo() {
		return fechaDevengo;
	}
	public void setFechaDevengo(String fechaDevengo) {
		this.fechaDevengo = fechaDevengo;
	}
	public LiquidacionTasasDetalleLiquidacion[] getDetalleLiquidacion() {
		return detalleLiquidacion;
	}
	public void setDetalleLiquidacion(
			LiquidacionTasasDetalleLiquidacion[] detalleLiquidacion) {
		this.detalleLiquidacion = detalleLiquidacion;
	}
	public String getPorcBonificacion() {
		return porcBonificacion;
	}
	public void setPorcBonificacion(String porcBonificacion) {
		this.porcBonificacion = porcBonificacion;
	}
	public String getPorcRecargo() {
		return porcRecargo;
	}
	public void setPorcRecargo(String porcRecargo) {
		this.porcRecargo = porcRecargo;
	}
	public String getImpRecargo() {
		return impRecargo;
	}
	public void setImpRecargo(String impRecargo) {
		this.impRecargo = impRecargo;
	}
	public String getImpIntereses() {
		return impIntereses;
	}
	public void setImpIntereses(String impIntereses) {
		this.impIntereses = impIntereses;
	}
	public LiquidacionTasasDatosSP getDatosSP() {
		return datosSP;
	}
	public void setDatosSP(LiquidacionTasasDatosSP datosSP) {
		this.datosSP = datosSP;
	}
	public LiquidacionTasasDatosPresentador getDatosPresentador() {
		return datosPresentador;
	}
	public void setDatosPresentador(
			LiquidacionTasasDatosPresentador datosPresentador) {
		this.datosPresentador = datosPresentador;
	}
	public String getObjLiquidacion() {
		return objLiquidacion;
	}
	public void setObjLiquidacion(String objLiquidacion) {
		this.objLiquidacion = objLiquidacion;
	}
	public String getExpExterno() {
		return expExterno;
	}
	public void setExpExterno(String expExterno) {
		this.expExterno = expExterno;
	}
	public String getExpGestion() {
		return expGestion;
	}
	public void setExpGestion(String expGestion) {
		this.expGestion = expGestion;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public String getFechaAcuerdo() {
		return fechaAcuerdo;
	}
	public void setFechaAcuerdo(String fechaAcuerdo) {
		this.fechaAcuerdo = fechaAcuerdo;
	}
	
	
}
