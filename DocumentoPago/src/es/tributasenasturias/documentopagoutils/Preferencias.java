package es.tributasenasturias.documentopagoutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.prefs.Preferences;

public final class Preferencias {

	private Preferences m_preferencias;
	private String m_debug;
	private String m_entorno;

	private String m_endpoint_lanzador;
	private String m_namespace_lanzador;
	private String m_servicename_lanzador;
	private String m_pa_get_reimprimible;
	private String m_dir_imagenes;
	private String m_pa_impresion;
	private String m_endpoint_documentos;
	private String m_pa_justificante_autoliquidacion;
	private String m_pa_justificante_ref_grupo;
    // 32484
	private String m_pa_justificante_pago;
	private String m_pa_datos_doin_justificante;
	// 30849
	private String m_pa_cartaPagoExpediente;
	private String m_plantilla_cartaPagoExpediente;
	//41133
	private String m_pa_datos_justificante_ad;
	private String m_endpoint_archivo_digital;
	//41947
	private String m_endpoint_cod_barras;
	private String m_qr_pago_refvol_x;
	private String m_qr_pago_refvol_y;
	private String m_qr_pago_refvol_ancho;
	private String m_qr_pago_refvol_alto;
	private String m_qr_pago_refvol_tipo;
	private String m_qr_pago_refvol_formato;
	private String m_qr_pago_refvol_pagina;
	// Variables relacionads con la firma.
	
	// CRUBENCVS 46542 17/11/2022
	private String m_pa_certificado_pago_autoliq;
	//FIN CRUBENCVS 46542
	
	// constantes para trabajar con las preferencias
	private final String FICHERO_PREFERENCIAS = "prefsDocumentoPago.xml";
	private final String DIRECTORIO_PREFERENCIAS = "proyectos//WSDocumentoPago";
	private final String NOMBRE_PREF_DEBUG = "Debug";
	private final String NOMBRE_PREF_ENTORNO = "Entorno";
	private final String NOMBRE_PREF_ENDPOINT_LANZADOR = "EndPointLanzador";
	private final String NOMBRE_PREF_PA_GET_REIMPRIMIBLE = "pAGetReimprimible";
	private final String NOMBRE_PREF_DIR_IMAGENES = "dirImagenes";
	private final String NOMBRE_PREF_WSLANZADOR_NAMESPACE = "NameSpaceLanzador";
	private final String NOMBRE_PREF_WSLANZADOR_SERVICE_NAME = "ServiceNameLanzador";				
	private final String NOMBRE_PREF_PAIMPRESION = "pAImpresionGD";
	private final String NOMBRE_PREF_ENDPOINT_DOCUMENTOS = "EndpointDocumentos";
	private final String NOMBRE_PREF_PA_JUSTIFICANTE_AUTOLIQUIDACION = "pAJustificanteAutoliquidacion";
	private final String NOMBRE_PREF_PA_JUSTIFICANTE_REFERENCIA = "pAJustificanteReferencia";
	// 32484 20/12/2017
	private final String NOMBRE_PREF_PA_JUSTIFICANTE_PAGO="pAJustificantePago"; 
	private final String NOMBRE_PREF_PA_DATOS_DOIN_JUSTIFICANTE="pADatosDoinJustificante";
	// 30849
	private final String NOMBRE_PREF_PA_CARTA_PAGO_EXPEDIENTE = "pACartaPagoExpedienteEjecutiva";
	private final String NOMBRE_PREF_PLANTILLA_CARTA_PAGO_EXPEDIENTE= "plantillaCartaPagoExpedienteEjecutiva";
	//41133
	private final String NOMBRE_PREF_PA_DATOS_JUSTIFICANTE_AD="pADatosJustificanteAD";
	private final String NOMBRE_PREF_ENDPOINT_ARCHIVO_DIGITAL = "EndpointArchivoDigital";
	//FIN 41133
	// 41947
	private final String NOMBRE_PREF_ENDPOINT_COD_BARRAS="EndpointCodigoBarras";
	private final String NOMBRE_PREF_QR_PAGO_VOLUNTARIA_TIPO="QR_Pago_Voluntaria_Tipo";
	private final String NOMBRE_PREF_QR_PAGO_VOLUNTARIA_PAGINA="QR_Pago_Voluntaria_Pagina";
	private final String NOMBRE_PREF_QR_PAGO_VOLUNTARIA_X="QR_Pago_Voluntaria_X";
	private final String NOMBRE_PREF_QR_PAGO_VOLUNTARIA_Y="QR_Pago_Voluntaria_Y";
	private final String NOMBRE_PREF_QR_PAGO_VOLUNTARIA_ANCHO="QR_Pago_Voluntaria_Ancho";
	private final String NOMBRE_PREF_QR_PAGO_VOLUNTARIA_ALTO="QR_Pago_Voluntaria_Alto";
	private final String NOMBRE_PREF_QR_PAGO_VOLUNTARIA_FORMATO="QR_Pago_Voluntaria_Formato_Imagen";
	//FIN 41947
	// CRUBENCVS 46542 17/11/2022
	private final String NOMBRE_PREF_PA_CERTIFICADO_AUTOLIQUIDACION = "pACertificadoPagoAutoliquidacion";
	// FIN CRUBENCVS 46542
	private final String VALOR_INICIAL_WSLANZADOR_NAMESPACE = "http://stpa/services";
	private final String VALOR_INICIAL_WSLANZADOR_SERVICE_NAME = "lanzaPLService";
	private final String VALOR_INICIAL_PA_GET_REIMPRIMIBLE = "ReimpresionSWGD.getXMLReimprimible";
	//41133 
	private final String VALOR_INICIAL_PA_DATOS_JUSTIFICANTE_AD = "VALOR.DATOS_JUSTIFICANTE_COBRO_AD";
	private final String VALOR_INICIAL_PREF_ENDPOINT_ARCHIVO_DIGITAL = "http://bus:7101/WSInternos/ProxyServices/PXArchivoDigital";
	// Escribe aqui el valor inicial del debug (0 = no existe debug, 1 = existe
	// debug)
	private final String VALOR_INICIAL_PREF_DEBUG = "0";
	private final String VALOR_INICIAL_PREF_ENTORNO = "EXPLOTACION";
	private final String VALOR_INICIAL_PREF_ENDPOINT_LANZADOR = "http://bus:7101/WSInternos/ProxyServices/PXLanzador";
	private final String VALOR_INICIAL_DIR_IMAGENES = "imagenes";
	private final String VALOR_INICIAL_PREF_PAIMPRESION = "impresionGDSW.getXMLImpresion";
	private final String VALOR_INICIAL_ENDPOINT_DOCUMENTOS = "http://bus:7101/WSInternos/ProxyServices/PXDocumentos";
	private final String VALOR_INICIAL_PA_JUSTIFICANTE_AUTOLIQUIDACION = "GD_DOCUMENTOS_PAGO.JUST_PAGO_AUTOLIQ_ALTA";
	private final String VALOR_INICIAL_PA_JUSTIFICANTE_REFERENCIA = "GD_DOCUMENTOS_PAGO.JUST_PAGO_REFERENCIA_ALTA";
    private final String VALOR_INICIAL_PA_JUSTIFICANTE_PAGO= "ACCESS_INFORMES.IMPRESIONJUSTIFICANTE";
    private final String VALOR_INICIAL_PA_DATOS_DOIN_JUSTIFICANTE= "INTERNET.DATOS_DOIN_JUSTIFICANTE_PAGO";
    private final String VALOR_INICIAL_PA_CARTA_PAGO_EXPEDIENTE = "EXPEDIENTEEJECUTIVA.emitirexpedienteejecutivaws";
    private final String VALOR_INICIAL_PLANTILLA_CARTA_PAGO_EXPEDIENTE="recursos/impresos/xml/imprimirICEjecutiva.xml";
    //41947
    private final String VALOR_INICIAL_ENDPOINT_COD_BARRAS="http://bus:7101/WSInternos/ProxyServices/PXCodigoBarras";
    private final String VALOR_INICIAL_QR_PAGO_VOLUNTARIA_TIPO="PAGO_REF";
	private final String VALOR_INICIAL_QR_PAGO_VOLUNTARIA_PAGINA="1";
	private final String VALOR_INICIAL_QR_PAGO_VOLUNTARIA_X="50";
	private final String VALOR_INICIAL_QR_PAGO_VOLUNTARIA_Y="25";
	private final String VALOR_INICIAL_QR_PAGO_VOLUNTARIA_ANCHO="80";
	private final String VALOR_INICIAL_QR_PAGO_VOLUNTARIA_ALTO="80";
	private final String VALOR_INICIAL_QR_PAGO_VOLUNTARIA_FORMATO="JPG";
    //FIN 41947
	// CRUBENCVS 46542 17/11/2022
	private final String VALOR_INICIAL_PA_CERTIFICADO_PAGO_AUTOLIQUIDACION = "GD_DOCUMENTOS_PAGO.CERT_PAGO_AUTOLIQ";
	// FIN CRUBENCVS 46542  17/11/2022
	public Preferencias() {
		try {
			CargarPreferencias();
		} catch (Exception e) {		
			e.printStackTrace();
		} 
	}

	public void CompruebaFicheroPreferencias() {

		File f = new File(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
		if (f.exists() == false) {
			CrearFicheroPreferencias();
		}
	}

	/***************************************************************************
	 * 
	 * Creamos el fichero de preferencias con los valores por defecto
	 * 
	 **************************************************************************/
	private void CrearFicheroPreferencias() {
		// preferencias por defecto
		m_preferencias = Preferences.systemNodeForPackage(this.getClass());
		m_preferencias.put(NOMBRE_PREF_DEBUG, VALOR_INICIAL_PREF_DEBUG);
		m_preferencias.put(NOMBRE_PREF_ENTORNO, VALOR_INICIAL_PREF_ENTORNO);

		m_preferencias.put(NOMBRE_PREF_ENDPOINT_LANZADOR,VALOR_INICIAL_PREF_ENDPOINT_LANZADOR);					
		m_preferencias.put(NOMBRE_PREF_WSLANZADOR_NAMESPACE, VALOR_INICIAL_WSLANZADOR_NAMESPACE);
		m_preferencias.put(NOMBRE_PREF_WSLANZADOR_SERVICE_NAME, VALOR_INICIAL_WSLANZADOR_SERVICE_NAME);
		m_preferencias.put(NOMBRE_PREF_WSLANZADOR_SERVICE_NAME, VALOR_INICIAL_WSLANZADOR_SERVICE_NAME);
		m_preferencias.put(NOMBRE_PREF_PA_GET_REIMPRIMIBLE, VALOR_INICIAL_PA_GET_REIMPRIMIBLE);
		m_preferencias.put(NOMBRE_PREF_DIR_IMAGENES, VALOR_INICIAL_DIR_IMAGENES);
		m_preferencias.put(NOMBRE_PREF_DIR_IMAGENES, VALOR_INICIAL_DIR_IMAGENES);
		m_preferencias.put(NOMBRE_PREF_PAIMPRESION, VALOR_INICIAL_PREF_PAIMPRESION);
		m_preferencias.put(NOMBRE_PREF_ENDPOINT_DOCUMENTOS, VALOR_INICIAL_ENDPOINT_DOCUMENTOS);
		m_preferencias.put(NOMBRE_PREF_PA_JUSTIFICANTE_AUTOLIQUIDACION, VALOR_INICIAL_PA_JUSTIFICANTE_AUTOLIQUIDACION);
		m_preferencias.put(NOMBRE_PREF_PA_JUSTIFICANTE_REFERENCIA, VALOR_INICIAL_PA_JUSTIFICANTE_REFERENCIA);
		m_preferencias.put(NOMBRE_PREF_PA_JUSTIFICANTE_PAGO, VALOR_INICIAL_PA_JUSTIFICANTE_PAGO);
		m_preferencias.put(NOMBRE_PREF_PA_DATOS_DOIN_JUSTIFICANTE, VALOR_INICIAL_PA_DATOS_DOIN_JUSTIFICANTE);
		m_preferencias.put(NOMBRE_PREF_PA_CARTA_PAGO_EXPEDIENTE, VALOR_INICIAL_PA_CARTA_PAGO_EXPEDIENTE);
		m_preferencias.put(NOMBRE_PREF_PLANTILLA_CARTA_PAGO_EXPEDIENTE, VALOR_INICIAL_PLANTILLA_CARTA_PAGO_EXPEDIENTE);
		//41133
		m_preferencias.put(NOMBRE_PREF_PA_DATOS_JUSTIFICANTE_AD, VALOR_INICIAL_PA_DATOS_JUSTIFICANTE_AD);
		m_preferencias.put(NOMBRE_PREF_ENDPOINT_ARCHIVO_DIGITAL, VALOR_INICIAL_PREF_ENDPOINT_ARCHIVO_DIGITAL);
		//41947
		m_preferencias.put(NOMBRE_PREF_ENDPOINT_COD_BARRAS, VALOR_INICIAL_ENDPOINT_COD_BARRAS);
		m_preferencias.put(NOMBRE_PREF_QR_PAGO_VOLUNTARIA_TIPO, VALOR_INICIAL_QR_PAGO_VOLUNTARIA_TIPO);
		m_preferencias.put(NOMBRE_PREF_QR_PAGO_VOLUNTARIA_PAGINA, VALOR_INICIAL_QR_PAGO_VOLUNTARIA_PAGINA);
		m_preferencias.put(NOMBRE_PREF_QR_PAGO_VOLUNTARIA_X, VALOR_INICIAL_QR_PAGO_VOLUNTARIA_X);
		m_preferencias.put(NOMBRE_PREF_QR_PAGO_VOLUNTARIA_Y, VALOR_INICIAL_QR_PAGO_VOLUNTARIA_Y);
		m_preferencias.put(NOMBRE_PREF_QR_PAGO_VOLUNTARIA_ANCHO, VALOR_INICIAL_QR_PAGO_VOLUNTARIA_ANCHO);
		m_preferencias.put(NOMBRE_PREF_QR_PAGO_VOLUNTARIA_ALTO, VALOR_INICIAL_QR_PAGO_VOLUNTARIA_ALTO);
		m_preferencias.put(NOMBRE_PREF_QR_PAGO_VOLUNTARIA_FORMATO, VALOR_INICIAL_QR_PAGO_VOLUNTARIA_FORMATO);
		//FIN CRUBENCVS 41947
		//CRUBENCVS 46542
		m_preferencias.put(NOMBRE_PREF_PA_CERTIFICADO_AUTOLIQUIDACION, VALOR_INICIAL_PA_CERTIFICADO_PAGO_AUTOLIQUIDACION);
		//FIN CRUBENCVS 46542
		FileOutputStream outputStream = null;
		File fichero;
		try {
			fichero = new File(DIRECTORIO_PREFERENCIAS);
			if (fichero.exists() == false)
				fichero.mkdirs();

			outputStream = new FileOutputStream(DIRECTORIO_PREFERENCIAS + "//"
					+ FICHERO_PREFERENCIAS);
			m_preferencias.exportNode(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (Exception e) {
				System.out.println("Error cerrando fichero de preferencias -> "
						+ e.getMessage());
				e.printStackTrace();
			}
		}
	}

	// Obtencion de las preferencias que especificaran el almacen y su
	// contraseña
	public void CargarPreferencias() throws Exception {
		File f = new File(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
		if (f.exists()) {
			// si existe el fichero de preferencias lo cargamos
			FileInputStream inputStream = new FileInputStream(
					DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
			Preferences.importPreferences(inputStream);
			inputStream.close();

			m_preferencias = Preferences.systemNodeForPackage(this.getClass());
			// obtenemos las preferencias
			
			m_debug = m_preferencias.get(NOMBRE_PREF_DEBUG, "");
			
			m_entorno = m_preferencias.get(NOMBRE_PREF_ENTORNO, "");
			m_endpoint_lanzador = m_preferencias.get(NOMBRE_PREF_ENDPOINT_LANZADOR, "");
			m_pa_get_reimprimible = m_preferencias.get(NOMBRE_PREF_PA_GET_REIMPRIMIBLE, "");
			m_namespace_lanzador = m_preferencias.get(NOMBRE_PREF_WSLANZADOR_NAMESPACE, "");
			m_servicename_lanzador= m_preferencias.get(NOMBRE_PREF_WSLANZADOR_SERVICE_NAME, "");
			m_dir_imagenes= m_preferencias.get(NOMBRE_PREF_DIR_IMAGENES, "");
			m_pa_impresion= m_preferencias.get(NOMBRE_PREF_PAIMPRESION, "");
			m_endpoint_documentos= m_preferencias.get(NOMBRE_PREF_ENDPOINT_DOCUMENTOS, "");
			m_pa_justificante_autoliquidacion= m_preferencias.get(NOMBRE_PREF_PA_JUSTIFICANTE_AUTOLIQUIDACION, "");
			m_pa_justificante_ref_grupo= m_preferencias.get(NOMBRE_PREF_PA_JUSTIFICANTE_REFERENCIA, "");
			m_pa_justificante_pago= m_preferencias.get(NOMBRE_PREF_PA_JUSTIFICANTE_PAGO,"");
			m_pa_datos_doin_justificante= m_preferencias.get(NOMBRE_PREF_PA_DATOS_DOIN_JUSTIFICANTE, "");
			m_pa_cartaPagoExpediente = m_preferencias.get(NOMBRE_PREF_PA_CARTA_PAGO_EXPEDIENTE, "");
			m_plantilla_cartaPagoExpediente= m_preferencias.get(NOMBRE_PREF_PLANTILLA_CARTA_PAGO_EXPEDIENTE,"");
			m_pa_datos_justificante_ad = m_preferencias.get(NOMBRE_PREF_PA_DATOS_JUSTIFICANTE_AD, "");
			m_endpoint_archivo_digital= m_preferencias.get(NOMBRE_PREF_ENDPOINT_ARCHIVO_DIGITAL, "");
			//CRUBENCVS 41947
			m_endpoint_cod_barras= m_preferencias.get(NOMBRE_PREF_ENDPOINT_COD_BARRAS, "");
			m_qr_pago_refvol_tipo= m_preferencias.get(NOMBRE_PREF_QR_PAGO_VOLUNTARIA_TIPO, "");
			m_qr_pago_refvol_pagina= m_preferencias.get(NOMBRE_PREF_QR_PAGO_VOLUNTARIA_PAGINA, "");
			m_qr_pago_refvol_x= m_preferencias.get(NOMBRE_PREF_QR_PAGO_VOLUNTARIA_X, "");
			m_qr_pago_refvol_y= m_preferencias.get(NOMBRE_PREF_QR_PAGO_VOLUNTARIA_Y, "");
			m_qr_pago_refvol_ancho= m_preferencias.get(NOMBRE_PREF_QR_PAGO_VOLUNTARIA_ANCHO, "");
			m_qr_pago_refvol_alto= m_preferencias.get(NOMBRE_PREF_QR_PAGO_VOLUNTARIA_ALTO, "");
			m_qr_pago_refvol_formato= m_preferencias.get(NOMBRE_PREF_QR_PAGO_VOLUNTARIA_FORMATO, "");
			//INI CRUBENCVS 46542 17/11/2022
			m_pa_certificado_pago_autoliq = m_preferencias.get(NOMBRE_PREF_PA_CERTIFICADO_AUTOLIQUIDACION, "");
			//FIN CRUBENCVS 46542 17/11/2022
		} else {
			// si no existe el fichero de preferencias lo crearemos
			CrearFicheroPreferencias();

			throw new Exception(
					"Debe especificar primero las preferencias en el fichero: "
							+ f.getAbsolutePath() + " (parar el servicio)");
		}
	}

	public String getDebug() {
		return this.m_debug;
	};

	public String getPaGetReimprimible() {
		return this.m_pa_get_reimprimible;
	};
	
	public String getEntorno() {
		return this.m_entorno;
	};

	public String getEndpointLanzador() {
		return this.m_endpoint_lanzador;
	};
	
	public String getNameSpaceLanzador() {
		return this.m_namespace_lanzador;
	};
	
	public String getServiceNameLanzador () {
		return this.m_servicename_lanzador;
	};
	public String getDirImagenes() {
		return this.m_dir_imagenes;
	};
	public String getPaImpresion() {
		return this.m_pa_impresion;
	};
	
	public String getEndpointDocumentos() {
		return this.m_endpoint_documentos;
	};
	
	public String getPaJustificanteAutoliquidacion() {
		return this.m_pa_justificante_autoliquidacion;
	};
	public String getPaJustificanteReferencia() {
		return this.m_pa_justificante_ref_grupo;
	};
	
	public String getPaJustificantePago() {
		return this.m_pa_justificante_pago;
	};
	
	public String getPaDatosDoinJustificante() {
		return this.m_pa_datos_doin_justificante;
	};
	
	public String getPaCartaPagoExpediente() {
		return this.m_pa_cartaPagoExpediente;
	};
	
	public String getPlantillaCartaPagoExpediente() {
		return this.m_plantilla_cartaPagoExpediente;
	};
	
	public String getPaDatosJustificanteAD() {
		return this.m_pa_datos_justificante_ad;
	};

	public String getEndpointArchivoDigital() {
		return this.m_endpoint_archivo_digital;
	};
	
	public String getEndpointCodBarras() {
		return this.m_endpoint_cod_barras;
	};	
	
	public String getQRPagoVoluntariaTipo() {
		return this.m_qr_pago_refvol_tipo;
	}
	
	public String getQRPagoVoluntariaAncho() {
		return this.m_qr_pago_refvol_ancho;
	}
	
	public String getQRPagoVoluntariaAlto() {
		return this.m_qr_pago_refvol_alto;
	};
	
	public String getQRPagoVoluntariaPagina() {
		return this.m_qr_pago_refvol_pagina;
	};
	
	public String getQRPagoVoluntariaX() {
		return this.m_qr_pago_refvol_x;
	};
	
	public String getQRPagoVoluntariaY() {
		return this.m_qr_pago_refvol_y;
	};
	
	public String getQRPagoVoluntariaFormatoImagen() {
		return this.m_qr_pago_refvol_formato;
	};
	
	//INI CRUBENCVS 17/11/2022
	public String getPaCertificadoPagoAutoliq() {
		return this.m_pa_certificado_pago_autoliq;
	};
	// FIN CRUBENCVS 17/11/2022
}
