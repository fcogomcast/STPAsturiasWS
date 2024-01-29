package com.stpa.ws.server.formularios;

import java.io.Serializable;
import java.util.HashMap;

import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.UtilsComunes;
import com.stpa.ws.server.util.ValidatorUtils;

public class PdfBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6647706866354152802L;

	String XMLDatos = "";// xml con datos a formatear
	String XSLDatos = "";// plantilla para autoliquidacion/etc
	String[] error = new String[0];


	String tipoPdf = "";// A,B
	String modelo = "";// numero de modelo
	String tipoModelo = "";// inicial:J,M,R, P
	String nombreDoc = "";// el nombre: JXXXXX-XXXXX
	// Class claseModelo = null;// clase base para invocar
	String nombreClaseModelo = "";
	String pathDocumentosPlantilla = "";
	String jspOrigen = "";// JSP al que retornar
	/** llama clase FormPDFBase directamente(1-27) */
	String param0 = "0";
	/** llama clase FormPDFBase por parametros (AV,AL,LI,E1,EE,VO,PT,LF,AN,EN,CC,RD,JC) */
	String param1 = "";
	/** llama clase FormPDFBase por parametros (N) */
	String param2 = "";
	/** llama clase FormPDFBase por parametros (1) */
	String param3 = "";
	/** llama clase FormPDFBase para formularios de tributas(TP/MP) */
	String param4 = "";
	/** llama clase FormPDFBase por parametros (TR/TT,DD/SS) */
	String param5 = "";
	HashMap<String, String> Session = null;// contiene fecha, lenguaje, nif, justificantes...
	String cabecera = "\n\n";
	String titulo = "Visualizacion PDFS";
	String mensaje = "Guarde en disco con los datos siguientes";
	public static String formsBase = "forms/";

	public String accionVolver = null;
	public String accion = null;
	
	String authTipo = ""; //Indica si es necesario el certificado al ir a pagos.	
	
	String boton = null;

	public String idDocumento;//Solo se utiliza en el programa de ayuda...
	
	public String[] getError() {
		return error;
	}

	public void addError(String mensaje) {
		String[] temp = new String[error.length + 1];
		temp[error.length] = mensaje;
		for (int i = 0; i < error.length; i++)
			temp[i] = error[i];
		error = temp;
	}

	public void cleanError() {
		error = new String[0];
	}

	public String getXMLDatos() {
		return XMLDatos;
	}

	public void setXMLDatos(String datos) {
		XMLDatos = datos;
	}

	public String getXSLDatos() {
		return XSLDatos;
	}

	public void setXSLDatos(String datos) {
		XSLDatos = datos;
	}

	public String getTipoPdf() {
		return tipoPdf;
	}

	/**
	 * Tipo de PDF, obligatorio. Tipo A = Modelo, Justificante o Registro; Tipo B = todos lo demás (recibo,
	 * autoliquidacion)
	 * 
	 * @param tipoDoc
	 */
	public void setTipoPdf(String tipoDoc) {
		this.tipoPdf = tipoDoc;
	}

	public String getModelo() {
		return modelo;
	}

	/**
	 * numero de modelo (003, 620 ej)
	 * 
	 * @param modelo
	 */
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getTipoModelo() {
		return tipoModelo;
	}

	/**
	 * obligatorio
	 * 
	 * @param tipoModelo
	 */
	public void setTipoModelo(String tipoModelo) {
		this.tipoModelo = tipoModelo;
	}

	
	public String getNombreDoc() {
		return nombreDoc;
	}

	/**
	 * opcional
	 * 
	 * @param nombreDoc
	 */
	public void setNombreDoc(String nombreDoc) {
		this.nombreDoc = nombreDoc;
	}

	public String getNombreClaseModelo() {
		return nombreClaseModelo;
	}

	public void setNombreClaseModelo(String nombreClaseModelo) {
		this.nombreClaseModelo = nombreClaseModelo;
	}

	public String getJspOrigen() {
		return jspOrigen;
	}

	public void setJspOrigen(String jspOrigen) {
		this.jspOrigen = jspOrigen;
	}

	public String getAccionVolver() throws StpawsException {
		if(ValidatorUtils.isEmpty(accionVolver)){
			accionVolver = UtilsComunes.getDestinoVolver();
		}
		
		return accionVolver;
	}

	public void setAccionVolver(String accionVolver) {
		this.accionVolver = accionVolver;
	}


	public HashMap<String, String> getSession() {
		return Session;
	}

	public void setSession(HashMap<String, String> session) {
		Session = session;
	}

	/**
	 * Número que identifica la clase que representa el PDF
	 * Ver método public static String getClaseFormulario(int codigo)
	 * @return
	 */
	public String getParam0() {
		return param0;
	}

	/**
	 * Número que identifica la clase que representa el PDF
	 * Ver método public static String getClaseFormulario(int codigo)
	 */
	public void setParam0(String param0) {
		this.param0 = param0;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}

	public String getParam4() {
		return param4;
	}

	public void setParam4(String param4) {
		this.param4 = param4;
	}

	public String getParam5() {
		return param5;
	}

	public void setParam5(String param5) {
		this.param5 = param5;
	}

	public static String getFormsBase() {
		return formsBase;
	}

	public static void setFormsBase(String formsBase) {
		PdfBean.formsBase = formsBase;
	}

	public String getCabecera() {
		return cabecera;
	}

	public void setCabecera(String cabecera) {
		this.cabecera = cabecera;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public void setError(String[] error) {
		this.error = error;
	}

	public String getPathDocumentosPlantilla() {
		return pathDocumentosPlantilla;
	}

	public void setPathDocumentosPlantilla(String pathDocumentosPlantilla) {
		this.pathDocumentosPlantilla = pathDocumentosPlantilla;
	}

	public String getBoton() {
		return boton;
	}

	public void setBoton(String boton) {
		this.boton = boton;
	}

	public String getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(String idDocumento) {
		this.idDocumento = idDocumento;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getAuthTipo() {
		return authTipo;
	}

	public void setAuthTipo(String authTipo) {
		this.authTipo = authTipo;
	}

	public void setAuthTipo(boolean needCertificado) {
		
		if(needCertificado){
			setAuthTipo("1");
		}
		else {
			setAuthTipo("");
		}
		
	}
	public String toString() {
		return "PDFBEAN:" + tipoPdf + "." + modelo + "." + nombreDoc + ".." + nombreClaseModelo + "."
				+ pathDocumentosPlantilla + "." + jspOrigen + ".." + param0 + "." + param1 + "." + param2 + "."
				+ param3 + "." + param4 + "." + param5 + "--";
	}
}