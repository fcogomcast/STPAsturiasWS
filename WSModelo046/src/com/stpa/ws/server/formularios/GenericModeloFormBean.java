package com.stpa.ws.server.formularios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.stpa.ws.server.constantes.Modelo046Constantes;
import com.stpa.ws.server.util.NumberUtil;
import com.stpa.ws.server.util.PropertiesUtils;
import com.stpa.ws.server.util.ValidatorUtils;

public class GenericModeloFormBean {
	
	public static final String paisEspana = "ESPA�A";
	public static final String provinciaDefecto = "ASTURIAS";
	public static final int MAX_DECIMALES=2;
	public static final int MAX_ENTEROS=15;
	
	public String[] noValidateMessagesKeys;

	public String nextJsp = "";
	public boolean isPresentacionTelematica = false;

	public String modelo = "";
	public String tipoDocumento = "";
	public String nifSujetoPasivo = "";
	public String nombreSujetoPasivo = "";
	public String nifPresentador = "";
	public String nombrePresentador = "";
	public String numerodeserie = "";
	public String codigoVerificacion = "";
	public String expediente = "";
	public String emisora = "";

	public String importe = "";

	public boolean recuperarEmisora = true;	
	public boolean incluirRegistro = true;
	public boolean necesitaCertificadoParaPagos = false;

	public boolean pagado = false;
	
	
	/**
	 * Permite indicar qué etiqueta hay que mostrar en la pantalla de
	 * confirmacion_view.jsp Por defecto sera Obligado Tributario, pero hay
	 * casos en los que nos piden que se muestre Sujeto Pasivo en su lugar.
	 */
	public String individuoConfirmacion = "confirmacion.obligadoTributario";

	// Datos obtenidos del webservice INTERNET.obtenerDatosPersona
	public String pers;

	public String xml = null;
	public String xmlBase64 = null;
	public String firma = null;

	public String pagosTimestamp = null;
	public String presentacionTimestamp = null;
	public String presentadorCertificado = null;

	public List<PdfBean> pdfBeanList = null;
	/**
	 * Titulo que se va a mostrar en la pàgina de visualización de PDF's cuando
	 * se visualize un PDF de la bean indicada. Si no esta informado se monta el
	 * título que haya definido por defecto.
	 */
	public String titulo_pdf = "";
	/**
	 * Mensaje debajo del título que se va a mostrar en la pàgina de
	 * visualización de PDF's cuando se visualize un PDF de la bean indicada. Si
	 * no esta informado se monta el mensaje que haya definido por defecto.
	 */
	public String mensaje_pdf = "";

	/**
	 * * Titulo que se va a mostrar en la pàgina de visualización de PDF's
	 * cuando se visualize un PDF de la bean indicada. Si no esta informado se
	 * monta el título que haya definido por defecto.
	 * 
	 * @return
	 */
	public String getTitulo_pdf() {
		return titulo_pdf;
	}

	/**
	 * * Titulo que se va a mostrar en la pàgina de visualización de PDF's
	 * cuando se visualize un PDF de la bean indicada. Si no esta informado se
	 * monta el título que haya definido por defecto.
	 * 
	 * @param titulo_pdf
	 */
	public void setTitulo_pdf(String titulo_pdf) {
		this.titulo_pdf = titulo_pdf;
	}

	/**
	 * * Mensaje debajo del título que se va a mostrar en la pàgina de
	 * visualización de PDF's cuando se visualize un PDF de la bean indicada. Si
	 * no esta informado se monta el mensaje que haya definido por defecto.
	 * 
	 * @return
	 */
	public String getMensaje_pdf() {
		return mensaje_pdf;
	}

	/**
	 * * Mensaje debajo del título que se va a mostrar en la pàgina de
	 * visualización de PDF's cuando se visualize un PDF de la bean indicada. Si
	 * no esta informado se monta el mensaje que haya definido por defecto.
	 * 
	 * @param notas_pdf
	 */
	public void setMensaje_pdf(String notas_pdf) {
		this.mensaje_pdf = notas_pdf;
	}

	public String[] getNoValidateMessagesKeys() {
		return noValidateMessagesKeys;
	}

	public void setNoValidateMessagesKeys(String[] noValidateMessagesKeys) {
		this.noValidateMessagesKeys = noValidateMessagesKeys;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNifSujetoPasivo() {
		return nifSujetoPasivo;
	}

	public void setNifSujetoPasivo(String nifSujetoPasivo) {
		this.nifSujetoPasivo = nifSujetoPasivo;
	}

	public String getNombreSujetoPasivo() {
		return nombreSujetoPasivo;
	}

	public void setNombreSujetoPasivo(String nombreSujetoPasivo) {
		this.nombreSujetoPasivo = nombreSujetoPasivo;
	}

	public String getNifPresentador() {
		return nifPresentador;
	}

	public void setNifPresentador(String nifPresentador) {
		this.nifPresentador = nifPresentador;
	}

	public String getNombrePresentador() {
		return nombrePresentador;
	}

	public void setNombrePresentador(String nombrePresentador) {
		this.nombrePresentador = nombrePresentador;
	}

	public String getNumerodeserie() {
		return numerodeserie;
	}

	public void setNumerodeserie(String numerodeserie) {
		this.numerodeserie = numerodeserie;
	}

	public String getExpediente() {
		return expediente;
	}

	public void setExpediente(String expediente) {
		this.expediente = expediente;
	}

	public String getEmisora() {
		return emisora;
	}

	public void setEmisora(String emisora) {
		this.emisora = emisora;
	}

	public String getImporte() {
		return importe;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}

	public String getCodigoVerificacion() {
		return codigoVerificacion;
	}

	public void setCodigoVerificacion(String codigoVerificacion) {
		this.codigoVerificacion = codigoVerificacion;
	}

	public String getNextJsp() {
		return nextJsp;
	}

	public void setNextJsp(String nextJsp) {
		this.nextJsp = nextJsp;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getXmlBase64() {
		return xmlBase64;
	}

	public void setXmlBase64(String xmlBase64) {
		this.xmlBase64 = xmlBase64;
	}

	public String getPers() {
		return pers;
	}

	public void setPers(String idPersona) {
		this.pers = idPersona;
	}

	public String getNombreModelo() {
		String nombre = "";
		try {
			String clave = "modelo" + getModelo() + ".nombre";
			nombre = PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP, clave);
		} catch (Exception e) {
			nombre = getModelo();
		}

		return nombre;
	}

	public String getMensajeConfirmacion() {
		String mensajeConfirmacion = "";
		try {
			String clave = "confirmacion.mensaje.modelo." + getModelo();
			mensajeConfirmacion = PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP, clave, new Locale("es"));
		} catch (Exception e) {
			mensajeConfirmacion = "";
		}

		if (ValidatorUtils.isEmpty(mensajeConfirmacion)) {
			try {
				String clave = "confirmacion.mensaje.defecto";
				mensajeConfirmacion = PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP, clave, new Locale("es"));
			} catch (Exception e) {
				mensajeConfirmacion = "";
			}
		}
		return mensajeConfirmacion;
	}

	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	public boolean isNecesitaPagos() {
		boolean needsPagos = false;
		String importe = getImporte();
		com.stpa.ws.server.util.Logger.debug("GenericModeloFormBean.isNecesitaPagos - IMPORTE:"+importe,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		if (!ValidatorUtils.isEmpty(importe) && NumberUtil.getDoubleFromBaseString(importe) > 0) {
			setImporte(importe);
			needsPagos = true;
		}
		com.stpa.ws.server.util.Logger.debug("GenericModeloFormBean.isNecesitaPagos:"+needsPagos,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return needsPagos;
	}

	public List<PdfBean> getPdfBeanList() {
		return pdfBeanList;
	}

	public void setPdfBeanList(List<PdfBean> pdfBeanList) {
		this.pdfBeanList = pdfBeanList;
	}

	public void addPdfBeanToList(PdfBean bean) {
		if (this.pdfBeanList == null) {
			this.pdfBeanList = new ArrayList<PdfBean>();
		}
		this.pdfBeanList.add(bean);
	}

	public String getPagosTimestamp() {
		return pagosTimestamp;
	}

	public void setPagosTimestamp(String pagosTimestamp) {
		this.pagosTimestamp = pagosTimestamp;
	}

	public String getPresentacionTimestamp() {
		return presentacionTimestamp;
	}

	public void setPresentacionTimestamp(String presentacionTimestamp) {
		this.presentacionTimestamp = presentacionTimestamp;
	}

	public String getPresentadorCertificado() {
		return presentadorCertificado;
	}

	public void setPresentadorCertificado(String presentadorCertificado) {
		this.presentadorCertificado = presentadorCertificado;
	}

	public boolean isRecuperarEmisora() {
		return recuperarEmisora;
	}

	public void setRecuperarEmisora(boolean recuperarEmisora) {
		this.recuperarEmisora = recuperarEmisora;
	}

	public String getIndividuoConfirmacion() {
		return individuoConfirmacion;
	}

	public void setIndividuoConfirmacion(String individuoConfirmacion) {
		this.individuoConfirmacion = individuoConfirmacion;
	}

	public Map<String, List<String>> generarSalidaParaPdfCamposComunes() {
		Map<String, List<String>> salida = new HashMap<String, List<String>>();
		// del map superior cuelgan estas dos listas
		List<String> pdf = new ArrayList<String>();
		List<String> map = new ArrayList<String>();

		// ahora lleno cada lista
		map.add(modelo);
		pdf.add("modelo");
		map.add(numerodeserie);
		pdf.add("numerodeserie");
		map.add(emisora);
		pdf.add("emisora");
		map.add(pers);
		pdf.add("pers");
		map.add(codigoVerificacion);
		pdf.add("codigoVerificacion");
		
		salida.put("MAP", map);
		salida.put("PDF", pdf);
		return salida;
	}

	public Map<String, Object> getCamposXmlPresentacion()
	{
		
		Map<String, Object> mapParam = null;
			
		if(getFirma() != null || getPresentacionTimestamp() != null || getPresentadorCertificado() != null){
			mapParam = new HashMap<String, Object>();			
			
			if(getFirma() != null){
				mapParam.put("firma", getFirma());
			}
				
			if(getPresentacionTimestamp() != null){
				mapParam.put("timestamp", getPresentacionTimestamp());
			}
			
			if(getPresentadorCertificado() != null){
				mapParam.put("presentadorcertificado|certificado", getPresentadorCertificado());
			}
			
			if(getNombrePresentador() != null){
				mapParam.put("presentadorusuario|nombre", getNombrePresentador());
			}
			
			if(getNifPresentador() != null){
				mapParam.put("presentadorusuario|nif", getNifPresentador());			
			}
		}
		
		return mapParam; 
	}
	
	public Map<String, Object> getCamposXmlPagos() {
		Map<String, Object> mapParam = null;
		
		if(getPagosTimestamp() != null){
			mapParam = new HashMap<String, Object>();
			mapParam.put("timestamp", getPagosTimestamp());
		}
		
		return mapParam; 
	}

	public boolean isPagado() {
		return pagado;
	}

	public void setPagado(boolean pagado) {
		this.pagado = pagado;
	}

	public boolean getIncluirRegistro() {
		return incluirRegistro;
	}

	public void setIncluirRegistro(boolean incluirRegistro) {
		this.incluirRegistro = incluirRegistro;
	}

	public boolean isNecesitaCertificadoParaPagos() {
		return necesitaCertificadoParaPagos;
	}

	public void setNecesitaCertificadoParaPagos(boolean necesitaCertificadoParaPagos) {
		this.necesitaCertificadoParaPagos = necesitaCertificadoParaPagos;
	}

	public boolean getIsPresentacionTelematica() {
		return isPresentacionTelematica;
	}

	public void setPresentacionTelematica(boolean isPresentacionTelematica) {
		this.isPresentacionTelematica = isPresentacionTelematica;
	}
}