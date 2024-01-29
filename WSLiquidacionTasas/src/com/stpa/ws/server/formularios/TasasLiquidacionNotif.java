package com.stpa.ws.server.formularios;

import java.io.OutputStream;
import java.util.Map;

import com.stpa.ws.server.exception.StpawsException;

public class TasasLiquidacionNotif extends PdfBase{

	public TasasLiquidacionNotif() {
		Session.put("cgestor", "");
		plantilla = "recursos//impresos//xml//tasasliquidacionnotif.xml";
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void compila(String id, String xml, String xsl, OutputStream output) throws StpawsException {
		try {
			tasasliquidacionnotificada(id, xml, xsl, output);
		} catch (Exception e) {
			throw new StpawsException("Error en la creacion del pdf", e);
		}
		
	}

	@SuppressWarnings("static-access")
	public void tasasliquidacionnotificada(String id, String xml, String xsl, OutputStream output) {
		
		DatosSalidaImpresa s = new DatosSalidaImpresa(xml, xsl, output);
		
		Map<Integer, Map<String, String>> rs = s.findWs(new String[] { "rica_recibo_inter_cabecera", "cade_cadena" },
				null, "SELECT v_rica_recibo_inter_cabecera.*, " + "LPAD(referencia_rica,12,'0') AS referencia, "
						+ "v_cade_cadena.string_cade " + "FROM v_rica_recibo_inter_cabecera, v_cade_cadena "
						+ "WHERE v_rica_recibo_inter_cabecera.id_respuesta = " + id
						+ "AND v_rica_recibo_inter_cabecera.id_respuesta = v_cade_cadena.id_respuesta "
						+ "AND v_rica_recibo_inter_cabecera.id_orden = v_cade_cadena.id_orden");

		Map<Integer, Map<String, String>> rs2 = s.findWs(new String[] { "rica_recibo_inter_cabecera" }, null,
				"select count(*) as numero_registros FROM v_rica_recibo_inter_cabecera "
						+ " where v_rica_recibo_inter_cabecera.id_respuesta = " + id);

		Map<Integer, Map<String, String>> rs3 = s.findWs(new String[] { "CANU_CADENAS_NUMEROS" }, null,
				"select string1_canu  as nombreConserjeria, string2_canu as nombreCentro, "
						+ "string3_canu as nombreTasa, string4_canu as codTCon, "
						+ "nume1_canu as importeIva,nume2_canu as importeRec, nume3_canu as importeInt "
						+ "from v_CANU_CADENAS_NUMEROS where id_respuesta=" + id);
		
		Map<Integer, Map<String, String>> rs4 = s.findWs(new String[] { "CAOR_CADENA_ORDEN" }, null,
				"select string_caor as refExterna   from v_CAOR_CADENA_ORDEN where id_respuesta="+id);

		int linActual = 0;
		Map<String, String> rsLinea = rs.get(new Integer(linActual));
		int linActual3 = 0;
		Map<String, String> rsLinea3 = rs3.get(new Integer(linActual3));
		int num_reg = rs2.size();//s.parseInt(s.campo(rsLinea2, "numero_registros"));
		int linActual4 = 0;
		Map<String, String> rsLinea4 = rs4.get(new Integer(linActual4));

		String consejeria = s.campo(rsLinea3, "string1_canu");
		String tipoTasa = s.campo(rsLinea3, "string4_canu");
		String nombreCentro = "";
		if (tipoTasa.equals("MP"))
			nombreCentro = "CENTRO GESTOR " + s.campo(rsLinea3, "string2_canu");
		else
			nombreCentro = s.campo(rsLinea3, "string2_canu");
		int primerapasada = 1;
		String nombreTasa = s.campo(rsLinea3, "string3_canu");

		int i = 0;
		int maxparam = 0;

		String idioma=Session.get("idioma");
		
		while (rsLinea != null && !rsLinea.isEmpty() && maxparam <= rsLinea.size()) {
			maxparam = rsLinea.size();
			if (i != 0 && num_reg > 1)
				s.NuevaPagina("");
			i = i + 1;
			s.Campo("Texto6", s.campo(rsLinea, "nif_contrib_rica"));
			s.Campo("Texto2", s.Left(s.campo(rsLinea, "nombre_contrib_rica"), 37));
			s.Campo("Texto159", s.campo(rsLinea, "detalle_15_rica"));
			s.Campo("Texto158", s.campo(rsLinea, "detalle_14_rica"));
			s.Campo("Texto157", s.campo(rsLinea, "detalle_13_rica"));
			s.Campo("Texto559", s.rellenaTexto(25, s.Val(s.Left(s.Right(s.campo(rsLinea, "numero_valo"), (s.campo(rsLinea,
					"numero_valo")).length() - 4), 2))));
			s.Campo("Texto156", s.campo(rsLinea, "detalle_12_rica"));
			s.Campo("Texto182", s.traduc("Referencia de cobro", idioma) + ": "
					+ s.Formato(s.campo(rsLinea, "referencia_rica"), 12));

			if (s.euro()) {
				s.Campo("Texto211", "euros");
				s.Campo("Texto211M", "euros"); // Cambio el literal del importe
				s.BorrarCampo("Texto212");
				s.BorrarCampo("Etiqueta187");
				s.BorrarCampo("Texto190");
				s.BorrarCampo("Euro");
			} else {
				s.Campo("Texto211", "ptas.");
				s.Campo("Texto190", s.ConvEuro(s.campo(rsLinea, "importe_valo"), 166.386, 2));
			}

			if (tipoTasa.equals("MP")) {
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
				s.BorrarCampo("Cuadro213bis");
				s.BorrarCampo("linea4");
			} else {
				s.BorrarCampo("Etiqueta160M");
				s.BorrarCampo("Etiqueta192M");
				s.BorrarCampo("Texto185");
				s.BorrarCampo("Texto211");
				s.BorrarCampo("linea4");
				s.BorrarCampo("linea4M");
				s.BorrarCampo("Etiqueta180");
				s.BorrarCampo("linea701");
			}

			s.Campo("Texto148", s.campo(rsLinea, "detalle_4_rica"));
			s.Campo("Texto155", s.campo(rsLinea, "detalle_11_rica"));
			s.Campo("Texto154", s.campo(rsLinea, "detalle_10_rica"));
			s.Campo("Texto153", s.campo(rsLinea, "detalle_9_rica"));
			s.Campo("Texto152", s.campo(rsLinea, "detalle_8_rica"));
			s.Campo("Texto558", s.rellenaTexto(24, s.Val(s.Left(s.Right(s.campo(rsLinea, "numero_valo"), (s.campo(rsLinea,
					"numero_valo")).length() - 4), 2))));
			s.Campo("Texto151", s.campo(rsLinea, "detalle_7_rica"));
			s.Campo("Texto150", s.campo(rsLinea, "detalle_6_rica"));
			s.Campo("Texto9", s.IIf(s.campo(rsLinea, "string_cade").equals("A"), s
					.traduc("Valor de ", idioma)
					+ " ", s.IIf(s.campo(rsLinea, "string_cade").equals("C"), "de la ", s.IIf(s
					.campo(rsLinea, "string_cade").equals("M"), "de la ", "")))
					+ nombreCentro + s.IIf(s.campo(rsLinea, "string_cade").equals("M"), "-" + nombreCentro, ""));
			s.Campo("Texto145", s.campo(rsLinea, "detalle_1_rica"));
			s.Campo("Texto146", s.campo(rsLinea, "detalle_2_rica"));
			s.Campo("Texto147", s.campo(rsLinea, "detalle_3_rica"));
			s.Campo("Texto149", s.campo(rsLinea, "detalle_5_rica"));
			String importeMultasRepetido = "";
			if (tipoTasa.equals("MP")) {
				s.Campo("Texto185M", s.toEuro(s.campo(rsLinea, "importe_valo") + ""));
				importeMultasRepetido = s.toEuro(s.campo(rsLinea, "importe_valo") + "");
			} else
				s.Campo("Texto185M", s.toEuro(s.campo(rsLinea, "importe_valo") + ""));
			if ((tipoTasa + "").equals("TP")) {
				s.Campo("Texto448", s.IIf(s.campo(rsLinea, "string_cade").equals("A"), s.traduc("Valor de", Session
						.get("idioma")), s.traduc("Valor de", idioma)));
			}

			if (s.campo(rsLinea, "activ_conc").equals("Objeto tributario"))
				if (tipoTasa.equals("TP"))
					s.Campo("Texto430", "Descripción de la Tasa");
				else
					s.Campo("Texto430", "Descripción de la Multa");

			if (s.campo(rsLinea, "calle_dire").length() > 52)
				s.Campo("Texto666", s.campo(rsLinea, "calle_dire"));
			else
				s.Campo("Texto427", s.campo(rsLinea, "calle_dire"));
			s.Campo("Texto426", s.IIf(s.validString(s.Trim(s.campo(rsLinea, "calle_ampliacion_dire"))), s.Trim(s.campo(
					rsLinea, "calle_ampliacion_dire"))
					+ "\n", " ")
					+ s.Trim(s.Formato(s.campo(rsLinea, "distrito_dire"), 5))
					+ " "
					+ s.Trim(s.campo(rsLinea, "municipio_rica"))
					+ s.IIf(s.validString(s.Trim(s.campo(rsLinea, "calle_ampliacion_dire"))), " ", "\n")
					+ s.Trim(s.campo(rsLinea, "provincia_rica")));
			s.Campo("Texto419", s.campo(rsLinea, "numero_valo"));
			s.Campo("Texto417", nombreTasa);
			s.Campo("Texto416", nombreCentro + s.IIf(s.campo(rsLinea, "string_cade").equals("M"), "-" + nombreCentro, ""));
			s.Campo("Texto411", s.Formato(s.campo(rsLinea, "referencia_rica"), 12));
			s.Campo("Texto410", s.toEuro(s.campo(rsLinea, "importe_valo") + ""));
			s.Campo("Texto408", s.Right("000000" + s.campo(rsLinea, "entidad_volu"), 6));
			s.Campo("MODO_EMIS_RICA", s.campo(rsLinea, "MODO_EMIS_RICA"));
			String persNotif  = s.campo(rsLinea3,"persNotif");
			if (persNotif=="RP")
				s.Campo("Texto405",s.Left(s.campo(rsLinea, "NOMBRE_REPR_RICA"),38));
			else
				s.Campo("Texto405",s.Left(s.campo(rsLinea, "nombre_contrib_rica"),38));
			
			s.Campo("Texto336",s.Left(s.campo(rsLinea, "NOMBRE_REPR_RICA"),35));
			s.Campo("Texto337",s.campo(rsLinea, "NIF_REPR_RICA")); 	 		

			if (tipoTasa.equals("TP")) {
				s.Campo("Texto20", s.traduc("Liquidación de la tasa", idioma) + " ");
				s.BorrarCampo("linea700");
			}
			if (tipoTasa.equals("MP"))
				s.Campo("Texto20", s.traduc("Liquidación Global", idioma) + " ");
			if (!tipoTasa.equals("MP"))
				s.Campo("validacion2", " ");
			s.Campo("Texto451", s.IIf(!s.validString(s.campo(rsLinea, "totaln_fracciones")), "", s.traduc("Fracción",
					idioma)
					+ " "
					+ (s.campo(rsLinea, "N_FRACCION_valo"))
					+ "/"
					+ (s.campo(rsLinea, "totaln_fracciones"))
					+ " "
					+ s.traduc("del recibo con identificación", idioma)
					+ " "
					+ s.campo(rsLinea, "ID_PADRE_valo")));
			s.Campo("Texto409", s.campo(rsLinea, "identificacion_rica"));
			s.Campo("Texto385", s.campo(rsLinea, "nif_sp_valo"));
			s.Campo("Texto384", s.Left(s.campo(rsLinea, "nombre_sp_valo"), 35));
			if (s.campo(rsLinea, "activ_conc").equals("Objeto tributario"))
				if (tipoTasa.equals("TP"))
					s.Campo("Texto229", "Descripción de la Tasa");
				else
					s.Campo("Texto229", "Descripción de la Multa");

			if (primerapasada == 1) {
				linActual3++;
				rsLinea3 = rs3.get(new Integer(linActual3));// rs3.movenext();
			}

			if (primerapasada == 1) {
				primerapasada = 0;

				if (tipoTasa.equals("MP")) {

				    if (rsLinea3!=null && !rsLinea3.isEmpty())
				    {
					s.Campo("TEPORCENTAJEIVAM",s.campo(rsLinea3,"string2_canu"));
					s.Campo("TEIMPORTEIVAM",s.ConvEuro(s.campo(rsLinea3,"nume1_canu"),100,2));
					s.Campo("TEPORCENTAJERECM",s.campo(rsLinea3,"string3_canu"));
					s.Campo("TEIMPORTERECM",s.ConvEuro(s.campo(rsLinea3,"nume2_canu"),100,2));
					s.Campo("TEIMPORTEINTM",s.ConvEuro(s.campo(rsLinea3,"nume3_canu"),100,2));
					s.Campo("Texto417",s.campo(rsLinea3,"string4_canu"));	
					s.Campo("TEIMPORTETOTM",importeMultasRepetido+" euros");
				    }	
				    else
				    {
					s.BorrarCampo("PORCENTAJEIVAM");
					s.BorrarCampo("TEIMPORTEIVAM");
					s.BorrarCampo("TEPORCENTAJERECM");
					s.BorrarCampo("TEIMPORTERECM");
					s.BorrarCampo("TEIMPORTEINTM");
					s.BorrarCampo("TEIMPORTETOTM");
				    }	
					s.BorrarCampo("linea3bis");
				
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
					s.BorrarCampo("linea700");
					s.BorrarCampo("Cuadro164");
					s.BorrarCampo("Cuadro213");
					s.BorrarCampo("linea3");
				}

				s.Campo("nliquidacion", s.campo(rsLinea3, "string1_canu")); // Numero de liquidacion
			}

			s.Campo("Etiqueta506", s.traduc(s.rellenaTexto(84, s.Val(s.Left(s.Right(s.campo(rsLinea, "numero_valo"), (s
					.campo(rsLinea, "numero_valo")).length() - 4), 2))), idioma));
			s.Campo("Texto27", nombreTasa);
			s.Campo("Texto38", s.campo(rsLinea, "numero_valo"));
			s.Campo("Texto28", s.campo(rsLinea, "num_fijo_valo"));
			s.Campo("Texto29",s.campo(rsLinea4, "string_caor"));//refExterna"));
			if (s.parseInt(s.campo(rsLinea, "anyo_cargo_carg")) < 2002) {
				s.Campo("infoEuros", "Información en euros: 1 Euro = 166,386 ptas.");
			} else {
				s.BorrarCampo("infoEuros");
			}

			if (tipoTasa.equals("MP")) {
				s
						.Campo("Texto22", s.traduc("Año", idioma) + " "
								+ (s.campo(rsLinea, "anyo_cargo_carg")));
			} else {
				String campoPeriodoValo = "";
				if (s.validString(s.campo(rsLinea, "periodo_valo")))
					campoPeriodoValo = s.campo(rsLinea, "periodo_valo").toUpperCase();
				else
					campoPeriodoValo = "NA";

				if (campoPeriodoValo.equals("ANUAL") || campoPeriodoValo.equals("NA") || campoPeriodoValo.equals("NO APLICABLE")) {
					s.Campo("Texto22", s.traduc("Año", idioma) + " "
							+ s.campo(rsLinea, "anyo_cargo_carg"));
				} else {
					s.Campo("Texto22", s.IIf(!s.campo(rsLinea, "periodo_valo").equals("ANUAL"), s.campo(rsLinea,
							"periodo_valo")
							+ s.traduc(" del año", idioma) + " " + s.campo(rsLinea, "anyo_cargo_carg"),
							s.traduc("Año", idioma) + " " + s.campo(rsLinea, "anyo_cargo_carg")));
				}
			}
			s.Campo("Texto406", s.campo(rsLinea, "USCO_RICA"));
			s.Campo("CodBarras2", s.campo(rsLinea, "USCO_RICA"));

			s.Campo("Texto501", s.Left(s.campo(rsLinea, "nombre_sp_valo"), 37));
			s.Campo("Texto502", s.campo(rsLinea, "nif_sp_valo"));

			if (num_reg > 1) {
				linActual++;
				rsLinea = rs.get(new Integer(linActual));
			}
			linActual++;
			rsLinea = rs.get(new Integer(linActual));
		}
		s.Campo("consejeria", consejeria);
		s.Mostrar();
	}
}