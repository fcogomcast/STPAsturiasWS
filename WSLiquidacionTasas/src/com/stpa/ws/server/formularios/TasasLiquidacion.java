
package com.stpa.ws.server.formularios;

import java.io.OutputStream;
import java.util.Map;

import com.stpa.ws.server.constantes.LiquidacionTasasConstantes;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.formularios.DatosSalidaImpresa;
import com.stpa.ws.server.util.PropertiesUtils;

public class TasasLiquidacion extends PdfBase{
	
	public String referencia;
	
	public TasasLiquidacion() {
		Session.put("cgestor", "");
		//plantilla = "tasasliquidacion";
		plantilla = "recursos//impresos//xml//tasasliquidacion.xml";
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void compila(String id, String xml, String xsl, OutputStream output) {//throws StpawsException {
//		try {
			tasasliquidacion(id, xml, xsl, output);
//		} catch (Exception e) {
//			throw new StpawsException("Error en la creacion del pdf", e);
//		}
		
	}

	@SuppressWarnings("static-access")
	public void tasasliquidacion(String id, String xml, String xsl, OutputStream output) {
		
		//com.stpa.ws.server.util.Logger.debug("DATOSALIDAIMPRESA :: ",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);		
		DatosSalidaImpresa s = new DatosSalidaImpresa(xml, xsl, output);			
				
		Map<Integer, Map<String, String>> rs = s.findWs(new String[] { "rica_recibo_inter_cabecera", "cade_cadena" },
				null, "SELECT v_rica_recibo_inter_cabecera.*, " + "LPAD(referencia_rica,12,'0') AS referencia, "
						+ "v_cade_cadena.string_cade " + "FROM v_rica_recibo_inter_cabecera, v_cade_cadena "
						+ "WHERE v_rica_recibo_inter_cabecera.id_respuesta = " + id
						+ "AND v_rica_recibo_inter_cabecera.id_respuesta = v_cade_cadena.id_respuesta "
						+ "AND v_rica_recibo_inter_cabecera.id_orden = v_cade_cadena.id_orden");

		Map<Integer, Map<String, String>> rs2 = s.findWs(new String[] { "tdco_tex_depen_concepto" }, null,
				"select * from v_tdco_tex_depen_concepto where id_respuesta=" + id + " order by id_orden");

		Map<Integer, Map<String, String>> rs3 = s.findWs(new String[] { "CANU_CADENAS_NUMEROS" }, null,
				"select string1_canu "
						+ " as nombreConserjeria, string2_canu as nombreCentro, string3_canu as nombreTasa, "
						+ "string4_canu as codTCon, nume1_canu as importeIva,nume2_canu as importeRec, "
						+ "nume3_canu as importeInt from v_CANU_CADENAS_NUMEROS where id_respuesta=" + id);
		
		
		int linActual = 0;
		Map<String, String> rsLinea = rs.get(new Integer(linActual));
		int linActual2 = 0;
		Map<String, String> rsLinea2 = rs2.get(new Integer(linActual2));
		int linActual3 = 0;
		Map<String, String> rsLinea3 = rs3.get(new Integer(linActual3));
		String consejeria = s.campo(rsLinea3, "string1_canu");
		String tipoTasa   = s.campo(rsLinea3, "string4_canu");
		String nombreCentro = "";			
		
		if (tipoTasa.equals("MP"))
			nombreCentro = "CENTRO GESTOR " + s.campo(rsLinea3, "string2_canu");
		else
			nombreCentro = s.campo(rsLinea3, "string2_canu");

		String nombreTasa = s.campo(rsLinea3, "string3_canu");
		String idioma=Session.get("idioma");
			
		// PRIMERA HOJA, EJEMPLAR PARA EL CONTRIBUYENTE	
		this.referencia = s.Right("000000000000" + s.Formato(s.campo(rsLinea, "referencia_rica")), 12);
		
		s.Campo("Texto279", s.campo(rsLinea, "identificacion_rica"));
		s.Campo("Texto233", s.Left(s.campo(rsLinea, "nombre_sp_valo"), 35));
		s.Campo("Texto234", s.campo(rsLinea, "nif_sp_valo"));
									
		s.Campo("Texto577", s.rellenaTexto(24,33));
		s.Campo("Texto578", s.rellenaTexto(25,33));
		
//if ((Session.get("cgestor") + "").equals("")) {
if (1==1) {	
	s.Campo("textOfic1", s.rellenaOficinas(""
					+ s.Val(s.Left(s.Right(s.campo(rsLinea, "numero_valo"),
							(s.campo(rsLinea, "numero_valo")).length() - 4), 2)), 1));
								
	s.Campo("Texto576", s.rellenaOficinas(""
			+ s.Val(s.Left(s.Right(s.campo(rsLinea, "numero_valo"),
					(s.campo(rsLinea, "numero_valo")).length() - 4), 2)), 2));
		
} else {								
			s.BorrarCampo("Etiqueta568");
			s.BorrarCampo("Etiqueta569");
			s.BorrarCampo("Etiqueta570");
			s.BorrarCampo("Etiqueta568");
			s.BorrarCampo("Etiqueta569");
			s.BorrarCampo("Etiqueta570");
			s.BorrarCampo("Linea2");
			s.BorrarCampo("Linea3");
			
//com.stpa.ws.server.util.Logger.debug("DATOSALIDAIMPRESA:: DESPUES DE BORRAR:" , com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);			
	
}

		/* NIF Y NOMBRE DEL REPRESENTANTE */					 		
		s.Campo("Texto336",s.Left(s.campo(rsLinea, "nombre_repr_rica"),35));
		s.Campo("Texto337",s.campo(rsLinea, "nif_repr_rica")); 
		s.Campo("Texto338",s.Left(s.campo(rsLinea, "nombre_repr_rica"),35));
		s.Campo("Texto339",s.campo(rsLinea, "nif_repr_rica"));	

		s.Campo("Texto275", s.Left(s.campo(rsLinea, "nombre_contrib_rica"), 38));
		s.Campo("Texto578", s.rellenaTexto(25, s.Val(s.Left(s.Right(s.campo(rsLinea, "numero_valo"), (s.campo(rsLinea,
				"numero_valo")).length() - 4), 2))));
		s.Campo("CodBarras2", s.Right("000000000000" + s.Formato(s.campo(rsLinea, "referencia_rica")), 12));
		
		if ((s.campo(rsLinea3, "string4_canu") + "").equals("TP")) {
			s.Campo("Texto332", s.traduc("Liquidación de la tasa", idioma) + " ");
			s.Campo("Texto542", s.IIf(s.campo(rsLinea, "string_cade").equals("A"), s.traduc("Recibo del Ayuntamiento de",
					idioma)
					+ " ", s.traduc("Valor de", idioma) + " "));
		}
		if (s.campo(rsLinea3, "string4_canu").equals("MP")) {
			s.Campo("Texto332", s.traduc("Liquidación Global", idioma) + " ");
			s.Campo("Texto542", s.IIf(s.campo(rsLinea, "string_cade").equals("A"), s.traduc(
					"Recibo del Ayuntamiento de", idioma)
					+ " ", ""));

		}

		s.Campo("MODO_EMIS_RICA", s.campo(rsLinea, "MODO_EMIS_RICA"));
		// Escribe objeto tributario
		if (s.campo(rsLinea, "activ_conc").equals("Objeto tributario"))
			if (s.campo(rsLinea3, "string4_canu").equals("TP"))
				s.Campo("Texto353", "Descripción de la Tasa");
			else
				s.Campo("Texto353", "Descripción de la Multa");

		s.Campo("Texto501", s.Left(s.campo(rsLinea, "nombre_sp_valo"), 37));
		s.Campo("Texto502", s.campo(rsLinea, "nif_sp_valo"));
		
		if (s.campo(rsLinea, "calle_dire").length() > 52)
			s.Campo("Texto666", s.campo(rsLinea, "calle_dire"));
		else
			s.Campo("Texto347", s.campo(rsLinea, "calle_dire"));

		s.Campo("Texto346", s.IIf(s.validString(s.Trim(s.campo(rsLinea, "calle_ampliacion_dire"))), s.Trim(s.campo(
				rsLinea, "calle_ampliacion_dire"))
				+ "\n", " ")
				+ s.Trim(s.Formato(s.campo(rsLinea, "distrito_dire"), 5))
				+ " "
				+ s.Trim(s.campo(rsLinea, "municipio_rica"))
				+ s.IIf(s.validString(s.Trim(s.campo(rsLinea, "calle_ampliacion_dire"))), " ", "\n")
				+ s.Trim(s.campo(rsLinea, "provincia_rica")));

		s.Campo("Etiqueta366", s.traduc(s.rellenaTexto(55, s.Val(s.Left(s.Right(s.campo(rsLinea, "numero_valo"), (s
				.campo(rsLinea, "numero_valo")).length() - 4), 2))), idioma));	

//===================
//PLAZOS PARA EL PAGO
//===================
		
		s.Campo("Etiqueta357", s.traduc(s.rellenaTexto(Integer.parseInt(s.campo(rsLinea2, "cod_text_textos")),33),idioma));	
		linActual2++;			
		rsLinea2 = rs2.get(new Integer(linActual2));	
	
		//Plazos para el pago apartado b)
		s.Campo("Etiqueta358", s.traduc(s.rellenaTexto(Integer.parseInt(s.campo(rsLinea2, "cod_text_textos")),33),idioma));		
		linActual2++;		
		rsLinea2 = rs2.get(new Integer(linActual2));
					
												
//============================
//RECURSOS 
//============================															
		String texto1 = s.rellenaTexto(85, 33);															
		linActual2++;
		rsLinea2 = rs2.get(new Integer(linActual2));
		
		String texto2 =s.traduc(s.rellenaTexto(Integer.parseInt(s.campo(rsLinea2, "cod_text_textos")),33),idioma); 	
		//com.stpa.ws.server.util.Logger.debug("TEXTO 2::" + s.campo(rsLinea2, "cod_text_textos") , com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		linActual2++;
		rsLinea2 = rs2.get(new Integer(linActual2));
				
		String texto3 =s.traduc(s.rellenaTexto(Integer.parseInt(s.campo(rsLinea2, "cod_text_textos")),33),idioma);				
		//com.stpa.ws.server.util.Logger.debug("TEXTO 3::" + s.campo(rsLinea2, "cod_text_textos") , com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);

		linActual2++;		
		rsLinea2 = rs2.get(new Integer(linActual2));
				
		s.Campo("Etiqueta364", s.traduc(texto1, idioma) + "\n" + s.traduc(texto2, idioma)
				+ "\n" + s.traduc(texto3, idioma));
										
		s.Campo("punto4texto", s.traduc(s.rellenaTexto(87, s.Val(s.Left(s.Right(s.campo(rsLinea, "numero_valo"), (s
				.campo(rsLinea, "numero_valo")).length() - 4), 2))), idioma));
		
		
		if (!s.campo(rsLinea3, "string4_canu").equals("MP")) {
			
			s.Campo("validacion2", " ");			
			
			s.Campo("Texto300", nombreCentro + s.IIf(s.campo(rsLinea, "string_cade").equals("M"), "-" + nombreCentro, ""));

			String campoPeriodoValo = "";
			if (s.validString(s.campo(rsLinea, "periodo_valo")))
				campoPeriodoValo = s.campo(rsLinea, "periodo_valo").toUpperCase();
			else
				campoPeriodoValo = "NA";

			if (campoPeriodoValo.equals("ANUAL") || campoPeriodoValo.equals("NA") || campoPeriodoValo.equals("NO APLICABLE")) {
				s
						.Campo("Texto333", s.traduc("Año ", idioma) + " "
								+ s.campo(rsLinea, "anyo_cargo_carg"));
				s.Campo("Texto22", s.traduc("Año", idioma) + " " + s.campo(rsLinea, "anyo_cargo_carg"));
			} else {
				s.Campo("Texto333", s.IIf(!s.campo(rsLinea, "periodo_valo").equals("ANUAL"), s.campo(rsLinea, "periodo_valo")
						+ s.traduc(" del año ", idioma) + " " + s.campo(rsLinea, "anyo_cargo_carg"), s
						.traduc("Año ", idioma)
						+ " " + s.campo(rsLinea, "anyo_cargo_carg")));
				s.Campo("Texto22", s.IIf(!s.campo(rsLinea, "periodo_valo").equals("ANUAL"), s.campo(rsLinea, "periodo_valo")
						+ s.traduc(" del año", idioma) + " " + s.campo(rsLinea, "anyo_cargo_carg"), s
						.traduc("Año", idioma)
						+ " " + s.campo(rsLinea, "anyo_cargo_carg")));
			}
			
		} else {
			s.Campo("Texto22", s.traduc("Año", idioma) + " " + s.campo(rsLinea, "anyo_cargo_carg"));
			s.Campo("Texto333", s.traduc("Año ", idioma) + " " + s.campo(rsLinea, "anyo_cargo_carg"));
		}
		s.Campo("Texto276", s.Formato(s.campo(rsLinea, "referencia_rica"), 12));
		s.Campo("Texto331", s.IIf(s.campo(rsLinea, "string_cade").equals("A"), s.traduc("del Ayuntamiento de", Session
				.get("idioma"))
				+ " ", s.IIf(s.campo(rsLinea, "string_cade").equals("C"), "de la ", s.IIf(
				s.campo(rsLinea, "string_cade").equals("M"), "de la ", "")))
				+ nombreCentro + s.IIf(s.campo(rsLinea, "string_cade").equals("M"), "-" + nombreCentro, ""));

		s.Campo("Texto330", s.campo(rsLinea, "nif_contrib_rica"));
		s.Campo("Texto329", s.Left(s.campo(rsLinea, "nombre_contrib_rica"), 37));
		s.Campo("Texto303", s.campo(rsLinea, "numero_valo"));
		s.Campo("Texto301", nombreTasa);
		s.Campo("Texto281", s.Formato(s.campo(rsLinea, "referencia_rica"), 12));
		s.Campo("Texto280", s.toEuro(s.campo(rsLinea, "importe_valo") + ""));
		s.Campo("Texto278", s.Right("000000" + s.campo(rsLinea, "entidad_volu"), 6));		

		s.Campo("Etiqueta360", s.traduc(s.rellenaTexto(84, s.Val(s.Left(s.Right(s.campo(rsLinea, "numero_valo"), (s
				.campo(rsLinea, "numero_valo")).length() - 4), 2))), idioma));
		s.Campo("CodBarras3", s.campo(rsLinea, "USCO_RICA"));
		s.Campo("Texto151", s.campo(rsLinea, "detalle_7_rica"));
		s.Campo("Texto152", s.campo(rsLinea, "detalle_8_rica"));
		s.Campo("Texto153", s.campo(rsLinea, "detalle_9_rica"));
		s.Campo("Texto154", s.campo(rsLinea, "detalle_10_rica"));
		s.Campo("Texto155", s.campo(rsLinea, "detalle_11_rica"));
		s.Campo("Texto6", s.campo(rsLinea, "nif_contrib_rica"));
		s.Campo("Texto157", s.campo(rsLinea, "detalle_13_rica"));
		s.Campo("Texto158", s.campo(rsLinea, "detalle_14_rica"));
		s.Campo("Texto159", s.campo(rsLinea, "detalle_15_rica"));
		s.Campo("Texto2", s.campo(rsLinea, "nombre_contrib_rica"));
		s.Campo("Texto156", s.campo(rsLinea, "detalle_12_rica"));
		s.Campo("Texto149", s.campo(rsLinea, "detalle_5_rica"));
		s.Campo("Texto148", s.campo(rsLinea, "detalle_4_rica"));
		s.Campo("Texto147", s.campo(rsLinea, "detalle_3_rica"));
		s.Campo("Texto146", s.campo(rsLinea, "detalle_2_rica"));
		s.Campo("Texto145", s.campo(rsLinea, "detalle_1_rica"));
		if (s.campo(rsLinea, "calle_dire").length() > 52)
			s.Campo("Texto667", s.campo(rsLinea, "calle_dire"));
		else
			s.Campo("Texto427", s.campo(rsLinea, "calle_dire"));
		s.Campo("Texto454", s.rellenaTexto(24, s.Val(s.Left(s.Right(s.campo(rsLinea, "numero_valo"), (s.campo(rsLinea,
				"numero_valo")).length() - 4), 2))));
		s.Campo("Texto190", s.ConvEuro(s.campo(rsLinea, "importe_valo"), 166.386, 2));
		String importeMultasRepetido = "";
		if (s.campo(rsLinea3, "string4_canu").equals("MP")) {
			s.Campo("Texto185M", s.toEuro(s.campo(rsLinea, "importe_valo") + ""));
			importeMultasRepetido = s.toEuro(s.campo(rsLinea, "importe_valo") + "");
		} else
			s.Campo("Texto185M", s.toEuro(s.campo(rsLinea, "importe_valo") + ""));
		s.Campo("Texto182", s.traduc("Referencia de cobro", idioma) + ": "
				+ s.Formato(s.campo(rsLinea, "referencia_rica"), 12));
		s.Campo("Texto455", s.rellenaTexto(25, s.Val(s.Left(s.Right(s.campo(rsLinea, "numero_valo"), (s.campo(rsLinea,
				"numero_valo")).length() - 4), 2))));
		if (s.campo(rsLinea, "activ_conc").equals("Objeto tributario"))
			if (s.campo(rsLinea3, "string4_canu").equals("TP"))
				s.Campo("Texto430", "Descripción de la Tasa");
			else
				s.Campo("Texto430", "Descripción de la Multa");
		s.Campo("Texto150", s.campo(rsLinea, "detalle_6_rica"));
		s.Campo("Texto405", s.Left(s.campo(rsLinea, "nombre_contrib_rica"), 38));
		s.Campo("Texto406", s.campo(rsLinea, "USCO_RICA"));
		//s.Campo("Texto407", s.campoFecha(rsLinea, "fecha_fin_volu"));
		//s.Campo("Texto447", s.campoFecha(rsLinea, "fecha_fin_volu"));
		if (s.campo(rsLinea3, "string4_canu").equals("TP"))
			s.Campo("Texto448", s.IIf(s.campo(rsLinea, "string_cade").equals("A"), s.traduc("Recibo del Ayuntamiento de",
					idioma)
					+ " ", s.traduc("Valor de", idioma) + " "));
		if (s.campo(rsLinea3, "string4_canu").equals("MP"))
			s.Campo("Texto448", s.IIf(s.campo(rsLinea, "string_cade").equals("A"), s.traduc(
					"Recibo del Ayuntamiento de", idioma)
					+ " ", ""));
		s.Campo("Texto408", s.Right("000000" + s.campo(rsLinea, "entidad_volu"), 6));
		s.Campo("Texto451", s.IIf(s.campo(rsLinea, "totaln_fracciones").equals(""), "", s.traduc("Fracción", Session
				.get("idioma"))
				+ " "
				+ s.campo(rsLinea, "N_FRACCION_valo")
				+ "/"
				+ s.campo(rsLinea, "totaln_fracciones")
				+ s.traduc(" del recibo con identificación ", idioma)
				+ s.campo(rsLinea, "ID_PADRE_valo")));
		s.Campo("Texto409", s.campo(rsLinea, "identificacion_rica"));
		s.Campo("Texto9", s.IIf(s.campo(rsLinea, "string_cade").equals("A"), s.traduc("de ", idioma) + " ", s
				.IIf(s.campo(rsLinea, "string_cade").equals("C"), "de la ", s.IIf(s.campo(rsLinea, "string_cade").equals("M"),
						"de la ", "")))
				+ nombreCentro + s.IIf(s.campo(rsLinea, "string_cade").equals("M"), "-" + nombreCentro, ""));
		
		s.Campo("Texto411", s.Formato(s.campo(rsLinea, "referencia_rica"), 12));
		if (s.campo(rsLinea, "activ_conc").equals("Objeto tributario"))
			if (s.campo(rsLinea3, "string4_canu").equals("TP"))
				s.Campo("Texto229", "Descripción de la Tasa");
			else
				s.Campo("Texto229", "Descripción de la Multa");

		s.Campo("Texto416", nombreCentro + s.IIf(s.campo(rsLinea, "string_cade").equals("M"), "-" + nombreCentro, ""));
				s.Campo("Texto417", nombreTasa);
		s.Campo("Texto385", s.campo(rsLinea, "nif_sp_valo"));
		s.Campo("Texto419", s.campo(rsLinea, "numero_valo"));
		s.Campo("Texto453", s.campo(rsLinea, "MODO_EMIS_RICA"));
		s.Campo("Etiqueta506", s.traduc(s.rellenaTexto(84, s.Val(s.Left(s.Right(s.campo(rsLinea, "numero_valo"), (s
				.campo(rsLinea, "numero_valo")).length() - 4), 2))), idioma));
		s.Campo("Texto27", nombreTasa);
		s.Campo("Texto38", s.campo(rsLinea, "numero_valo"));
		s.Campo("Texto426", s.IIf(s.validString(s.Trim(s.campo(rsLinea, "calle_ampliacion_dire"))), s.Trim(s.campo(
				rsLinea, "calle_ampliacion_dire")
				+ "\n"), " ")
				+ s.Trim(s.Formato(s.campo(rsLinea, "distrito_dire"), 5))
				+ " "
				+ s.Trim(s.campo(rsLinea, "municipio_rica"))
				+ s.IIf(s.validString(s.Trim(s.campo(rsLinea, "calle_ampliacion_dire"))), " ", "\n")
				+ s.Trim(s.campo(rsLinea, "provincia_rica")));
		s.Campo("Texto28", s.campo(rsLinea, "num_fijo_valo"));
		if (s.campo(rsLinea3, "string4_canu").equals("TP")) {
			s.Campo("Texto20", s.traduc("Liquidación de la tasa", idioma) + " ");

		}
		if (s.campo(rsLinea3, "string4_canu").equals("MP")) {
			s.Campo("Texto20", s.traduc("Liquidación Global", idioma) + " ");
		}

		s.Campo("Texto410", s.toEuro(s.campo(rsLinea, "importe_valo") + ""));
		s.Campo("Texto384", s.Left(s.campo(rsLinea, "nombre_sp_valo"), 35));
		s.Campo("Texto503", s.Left(s.campo(rsLinea, "nombre_sp_valo"), 37));
		s.Campo("Texto504", s.campo(rsLinea, "nif_sp_valo"));
		if (s.euro()) {
			s.Campo("Texto211", "euros"); // Cambio el literal del importe
			s.Campo("Texto211M", "euros"); // Cambio el literal del importe
			s.BorrarCampo("Texto212"); // literal "euros" que va tras la conversión en la información
			s.BorrarCampo("Euro"); // Simbolo del Euro de la información
			s.BorrarCampo("Etiqueta187"); // Etiqueta con información referente al Euro
			s.BorrarCampo("Texto190"); // Importe convertido al Euro de la información
		} else {
			s.Campo("Texto211", "ptas."); // Cambio el literal del importe
		}

		if (s.campo(rsLinea3, "string4_canu").equals("MP")) {
			if (s.euro()) {
				s.Campo("Texto211M", "euros");
			} else {
				s.Campo("Texto211M", "ptas.");
				s.Campo("Texto190", s.ConvEuro(s.campo(rsLinea, "importe_valo"), 166.386, 2));
			}
			s.BorrarCampo("Etiqueta160");
			s.BorrarCampo("Etiqueta192");
			s.BorrarCampo("Texto185");
			s.BorrarCampo("Texto211");
			s.BorrarCampo("linea4");
			s.BorrarCampo("Etiqueta180");
			s.BorrarCampo("Linea13");
			s.BorrarCampo("Etiqueta296");
			s.BorrarCampo("Cuadro213bis");
			s.BorrarCampo("Cuadro164bis");
		} else {
			s.BorrarCampo("Etiqueta160M");
			s.BorrarCampo("Etiqueta192M");
			s.BorrarCampo("Texto185");
			s.BorrarCampo("Texto211");
			s.BorrarCampo("linea4M");
			s.BorrarCampo("Etiqueta180");
			s.BorrarCampo("linea701");
			s.BorrarCampo("Linea13M");
			s.BorrarCampo("Etiqueta296M");
		}
		linActual3++;
		rsLinea3 = rs3.get(new Integer(linActual3));

		if (tipoTasa.equals("MP")) {
			s.Campo("TEPORCENTAJEIVAM", s.campo(rsLinea3, "string2_canu"));
			s.Campo("TEIMPORTEIVAM", s.ConvEuro(s.campo(rsLinea3, "nume1_canu"), 100, 2));
			s.Campo("TEPORCENTAJERECM", s.campo(rsLinea3, "string3_canu"));
			s.Campo("TEIMPORTERECM", s.ConvEuro(s.campo(rsLinea3, "nume2_canu"), 100, 2));
			s.Campo("TEIMPORTEINTM", s.ConvEuro(s.campo(rsLinea3, "nume3_canu"), 100, 2));
			s.Campo("Texto417", s.campo(rsLinea3, "string4_canu"));
			s.Campo("TEIMPORTETOTM", importeMultasRepetido + " euros");

		} else {
			s.BorrarCampo("IVAM");
			s.BorrarCampo("PORCENTAJEIVAM");
			s.BorrarCampo("IMPORTEIVAM");
			s.BorrarCampo("CuadroIMPORIVAM");
			s.BorrarCampo("CuadroIMPORIVAM2");
			s.BorrarCampo("TEPORCENTAJEIVAM");
			s.BorrarCampo("TEIMPORTEIVAM");
			s.BorrarCampo("lineaIVAREC");
			s.BorrarCampo("RECM");
			s.BorrarCampo("PORCENTAJERECM");
			s.BorrarCampo("IMPORTERECM");
			s.BorrarCampo("CuadroIMPORRECM");
			s.BorrarCampo("CuadroIMPORRECM2");
			s.BorrarCampo("TEPORCENTAJERECM");
			s.BorrarCampo("TEIMPORTERECM");
			s.BorrarCampo("lineaRECINT");
			s.BorrarCampo("INTM");
			s.BorrarCampo("IMPORTEINTM");
			s.BorrarCampo("TEIMPORTEINTM");
			s.BorrarCampo("CuadroIMPORINTM");
			s.BorrarCampo("TEIMPORTETOTM");
			s.BorrarCampo("TEXTOEUROM");
			s.BorrarCampo("Linea12");
			s.BorrarCampo("Linea13");
			s.BorrarCampo("linea700");
			s.BorrarCampo("TEXTOEUROM");
			s.BorrarCampo("Cuadro164");
			s.BorrarCampo("Cuadro213");
		}
		if (s.parseInt(s.campo(rsLinea, "anyo_cargo_carg")) < 2002) {
			s.Campo("infoEuros", "Información en euros: 1 Euro = 166,386 ptas.");
		} else {
			s.BorrarCampo("infoEuros");
		}
		s.Campo("nliquidacion", s.campo(rsLinea3, "string1_canu")); // Numero de liquidacion
		
		s.Campo("consejeria", consejeria);
		s.Campo("consejeria2", consejeria);
		
		s.Mostrar();
	}
}
