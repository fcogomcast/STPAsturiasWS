package es.tributasenasturias.documentopagoutils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.tributasenasturias.documentopagoutils.StpawsException;
import es.tributasenasturias.documentopagoutils.CampoRecuperarWS;
import es.tributasenasturias.documentopagoutils.CampoWS;
import es.tributasenasturias.documentopagoutils.Constantes;
import es.tributasenasturias.documentopagoutils.InorInfoOrga;
import es.tributasenasturias.documentopagoutils.ObtenerListadosWS;
import es.tributasenasturias.documentopagoutils.Organismo;

public class ListadoUtils {

	public static List<Oficina> obtenerOficinas() {
		List<Oficina> oficinas = null;
		
		String procedimiento = Constantes.FUNCION_OFICINAS;
		
		List<CampoRecuperarWS> camposRecuperar = new ArrayList<CampoRecuperarWS>();
		
		CampoRecuperarWS campoRecuperar1 = new CampoRecuperarWS (Constantes.OFICINAS_ESTRUCTURA, Constantes.OFICINAS_CAMPO_id_ofic); 
		camposRecuperar.add(campoRecuperar1);
		CampoRecuperarWS campoRecuperar2 = new CampoRecuperarWS (Constantes.OFICINAS_ESTRUCTURA, Constantes.OFICINAS_CAMPO_nombre_ofic);
		camposRecuperar.add(campoRecuperar2);
		CampoRecuperarWS campoRecuperar3 = new CampoRecuperarWS (Constantes.OFICINAS_ESTRUCTURA, Constantes.OFICINAS_CAMPO_of_liq_ofic);
		camposRecuperar.add(campoRecuperar3);
		CampoRecuperarWS campoRecuperar4 = new CampoRecuperarWS (Constantes.OFICINAS_ESTRUCTURA, Constantes.OFICINAS_CAMPO_sale_recibo_ofic);
		camposRecuperar.add(campoRecuperar4);
		CampoRecuperarWS campoRecuperar5 = new CampoRecuperarWS (Constantes.OFICINAS_ESTRUCTURA, Constantes.OFICINAS_CAMPO_telefono_ofic); 
		camposRecuperar.add(campoRecuperar5);
		CampoRecuperarWS campoRecuperar6 = new CampoRecuperarWS (Constantes.OFICINAS_ESTRUCTURA, Constantes.OFICINAS_CAMPO_dir_corta_ofi);
		camposRecuperar.add(campoRecuperar6);
			
		try {
			//es.tributasenasturias.documentopagoutils.Logger.info("antes de obtener listado",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			List<Map<String, String>> resultados = ObtenerListadosWS.obtenerListado(procedimiento, null, camposRecuperar, null);
			//es.tributasenasturias.documentopagoutils.Logger.info("despues de obtener listado",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			
			if(resultados != null){
				oficinas = new ArrayList<Oficina>();
				for(int i=0; i < resultados.size(); i++){
					Map<String, String> item = resultados.get(i);
					Oficina ofic = new Oficina(item);
					
					oficinas.add(ofic);
				}							
			}			
			
		} catch (StpawsException e) {
			es.tributasenasturias.documentopagoutils.Logger.error("ListadoUtils.obtenerOficinas|"+e.getMessage(),e,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}		
		return oficinas;		
	}

	public static List<Oficina> obtenerOficinasBySaleRecibo(String saleRecibo) {
		List<Oficina> oficinas = null;
		List<Oficina> todasOficinas = obtenerOficinas();
		if(todasOficinas != null && saleRecibo != null){
			oficinas = new ArrayList<Oficina>();
			for(int i = 0; i < todasOficinas.size(); i++){
				Oficina ofic = (Oficina)todasOficinas.get(i);
				if(ofic != null && saleRecibo.equals(ofic.getSaleReciboOfic())){
					oficinas.add(ofic);
				}
			}
		}
		else {
			oficinas = todasOficinas;
		}
		return oficinas;
	}
	
	/**
	 * "select o.id_ofic, o.nombre_corto_ofic,o.telefono_ofic,o.dir_corta_ofic from ofic_oficinas o,orga_ofic oo 
	 *  where oo.id_orga = "+ (idOrga)+ " 
	 *  and o.id_ofic=oo.id_ofic 
	 *  and oo.id_ofic not in (35,38) 
	 *  and o.of_liq_ofic='S' 
	 *  order by oo.id_ofic asc"
	 * 
	 * 
	 * @param saleRecibo
	 * @return
	 */
	public static List<Oficina> obtenerOficinasLiquidadoras() {
		List<Oficina> oficinas = null;
		List<Oficina> todasOficinas = obtenerOficinas();
		if(todasOficinas != null){
			oficinas = new ArrayList<Oficina>();
			for(int i = 0; i < todasOficinas.size(); i++){
				Oficina ofic = (Oficina)todasOficinas.get(i);
				if(ofic != null && "S".equals(ofic.getOfLiqOfic())
						&& (!"35".equals(ofic.getIdOfic()) && !"38".equals(ofic.getIdOfic()))){
					oficinas.add(ofic);
				}
			}
		}
		else {
			oficinas = todasOficinas;
		}
		return oficinas;
	}
	
	public static List<Organismo> obtenerOrganismos(String nOrganismo) {
		List<Organismo> organismos = null;
		
		String procedimiento = Constantes.FUNCION_ORGANISMO;
		
		List<CampoWS> camposEntrada = new ArrayList<CampoWS>(); 
		CampoWS campo1 = new CampoWS("0","p_in_norga", "NUMERO", nOrganismo);
		
		camposEntrada.add(campo1);
				
		List<CampoRecuperarWS> camposRecuperar = new ArrayList<CampoRecuperarWS>();

		
		CampoRecuperarWS campoRecuperar1 = new CampoRecuperarWS (Constantes.ORGANISMO_ESTRUCTURA, Constantes.ORGANISMO_CAMPO_nombre_orga); 
		camposRecuperar.add(campoRecuperar1);
		CampoRecuperarWS campoRecuperar2 = new CampoRecuperarWS (Constantes.ORGANISMO_ESTRUCTURA, Constantes.ORGANISMO_CAMPO_nombre_l_orga);
		camposRecuperar.add(campoRecuperar2);
		CampoRecuperarWS campoRecuperar3 = new CampoRecuperarWS (Constantes.ORGANISMO_ESTRUCTURA, Constantes.ORGANISMO_CAMPO_nif_orga);
		camposRecuperar.add(campoRecuperar3);
		CampoRecuperarWS campoRecuperar4 = new CampoRecuperarWS (Constantes.ORGANISMO_ESTRUCTURA, Constantes.ORGANISMO_CAMPO_cod_eemi);
		camposRecuperar.add(campoRecuperar4);
		CampoRecuperarWS campoRecuperar5 = new CampoRecuperarWS (Constantes.ORGANISMO_ESTRUCTURA, Constantes.ORGANISMO_CAMPO_director_orgaa); 
		camposRecuperar.add(campoRecuperar5);
		CampoRecuperarWS campoRecuperar6 = new CampoRecuperarWS (Constantes.ORGANISMO_ESTRUCTURA, Constantes.ORGANISMO_CAMPO_max_fracc_orga);
		camposRecuperar.add(campoRecuperar6);
		CampoRecuperarWS campoRecuperar7 = new CampoRecuperarWS (Constantes.ORGANISMO_ESTRUCTURA, Constantes.ORGANISMO_CAMPO_min_fraccionar_orga);
		camposRecuperar.add(campoRecuperar7);
		CampoRecuperarWS campoRecuperar8 = new CampoRecuperarWS (Constantes.ORGANISMO_ESTRUCTURA, Constantes.ORGANISMO_CAMPO_min_fraccion_orga);
		camposRecuperar.add(campoRecuperar8);
		
		CampoRecuperarWS campoError = new CampoRecuperarWS (Constantes.ESTRUCTURA_CADE_CADENA, Constantes.CAMPO_STRING_CADE, "00");
		
		
		try {
			
			List<Map<String, String>> resultados = ObtenerListadosWS.obtenerListado(procedimiento, camposEntrada, camposRecuperar, campoError);
			
			if(resultados != null){
				organismos = new ArrayList<Organismo>();
				for(int i=0; i < resultados.size(); i++){
					Map<String, String> item = resultados.get(i);
					Organismo orga = new Organismo(item);
					
					organismos.add(orga);
				}							
			}			
			
		} catch (StpawsException e) {
			es.tributasenasturias.documentopagoutils.Logger.error("ListadoUtils.obtenerOrganismos|"+e.getMessage(),e,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}		
		return organismos;		
	}
	
	
	public static Organismo obtenerOrganismo(String nOrganismo) {
		List<Organismo> organismos = obtenerOrganismos(nOrganismo);
		Organismo organismo = null; 
		
		if(organismos != null && organismos.size() > 0){
			organismo = (Organismo)organismos.get(0);
		}
		return organismo;
	}
	
	public static List<InorInfoOrga> obtenerListaInorInfoOrga(String organismo, String tiio) {
		List<InorInfoOrga> inorInfoOrgaList = null;
		
		String procedimiento = Constantes.FUNCION_INOR_INFO_ORGA;
		
		List<CampoWS> camposEntrada = new ArrayList<CampoWS>(); 
		CampoWS campo1 = new CampoWS("0","p_in_idOrga", "NUMERO", organismo);
		CampoWS campo2 = new CampoWS("1","p_in_codTiio", "CADENA", tiio);
		
		camposEntrada.add(campo1);
		camposEntrada.add(campo2);		
				
		List<CampoRecuperarWS> camposRecuperar = new ArrayList<CampoRecuperarWS>();

		
		CampoRecuperarWS campoRecuperar1 = new CampoRecuperarWS (Constantes.INOR_INFO_ORGA_ESTRUCTURA, Constantes.INOR_INFO_ORGA_CAMPO_descr_inor); 
		camposRecuperar.add(campoRecuperar1);
		CampoRecuperarWS campoRecuperar2 = new CampoRecuperarWS (Constantes.INOR_INFO_ORGA_ESTRUCTURA, Constantes.INOR_INFO_ORGA_CAMPO_cod_inor);
		camposRecuperar.add(campoRecuperar2);
		CampoRecuperarWS campoRecuperar3 = new CampoRecuperarWS (Constantes.INOR_INFO_ORGA_ESTRUCTURA, Constantes.INOR_INFO_ORGA_CAMPO_bool_inor);
		camposRecuperar.add(campoRecuperar3);
		CampoRecuperarWS campoRecuperar4 = new CampoRecuperarWS (Constantes.INOR_INFO_ORGA_ESTRUCTURA, Constantes.INOR_INFO_ORGA_CAMPO_fecha_inor);
		camposRecuperar.add(campoRecuperar4);
		CampoRecuperarWS campoRecuperar5 = new CampoRecuperarWS (Constantes.INOR_INFO_ORGA_ESTRUCTURA, Constantes.INOR_INFO_ORGA_CAMPO_num_inor); 
		camposRecuperar.add(campoRecuperar5);
		
		
		CampoRecuperarWS campoError = new CampoRecuperarWS (Constantes.ESTRUCTURA_CADE_CADENA, Constantes.CAMPO_STRING_CADE, "00");
		
		
		try {
			
			List<Map<String, String>> resultados = ObtenerListadosWS.obtenerListado(procedimiento, camposEntrada, camposRecuperar, campoError);
			
			if(resultados != null){
				inorInfoOrgaList = new ArrayList<InorInfoOrga>();
				for(int i=0; i < resultados.size(); i++){
					Map<String, String> item = resultados.get(i);
					InorInfoOrga inorInfoOrga = new InorInfoOrga(item);
					inorInfoOrgaList.add(inorInfoOrga);
				}							
			}			
			
		} catch (Exception e) {
			es.tributasenasturias.documentopagoutils.Logger.error("ListadoUtils.obtenerOrganismos|"+e.getMessage(),e,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}		
		return inorInfoOrgaList;		
	}
	public static InorInfoOrga obtenerInorInfoOrga(String nOrganismo, String tiio) {
		List<InorInfoOrga> inorInfoOrgaList = obtenerListaInorInfoOrga(nOrganismo, tiio);
		InorInfoOrga inorInfoOrga = null; 
		
		if(inorInfoOrgaList != null && inorInfoOrgaList.size() > 0){
			inorInfoOrga = (InorInfoOrga)inorInfoOrgaList.get(0);
		}
		return inorInfoOrga;
	}
	
	public static List<String> obtenerNormativa(String tdoc, String sdoc, String tact) {
		List<String> literalesNormativa = null;
		
		String procedimiento = Constantes.FUNCION_NORMATIVA_NORM;
		
		List<CampoWS> camposEntrada = new ArrayList<CampoWS>(); 
		CampoWS campo1 = new CampoWS("0","p_in_tdoc", "NUMERO", tdoc);
		camposEntrada.add(campo1);
		CampoWS campo2 = new CampoWS("1","p_in_tdoc", "CADENA", sdoc);	
		camposEntrada.add(campo2);
		CampoWS campo3 = new CampoWS("2","p_in_tact", "CADENA", tact);	
		camposEntrada.add(campo3);
		CampoWS campo4 = new CampoWS("3","p_in_internet", "CADENA", "S");	
		camposEntrada.add(campo4);
		
		
		List<CampoRecuperarWS> camposRecuperar = new ArrayList<CampoRecuperarWS>();
		CampoRecuperarWS campoLiteral = new CampoRecuperarWS (Constantes.NORMATIVA_ESTRUCTURA, Constantes.NORMATIVA_CAMPO);
		camposRecuperar.add(campoLiteral);
		
		CampoRecuperarWS campoError = new CampoRecuperarWS (Constantes.ESTRUCTURA_CADE_CADENA, Constantes.CAMPO_STRING_CADE, "00");
		
		try {
			
			List<Map<String, String>> resultados = ObtenerListadosWS.obtenerListado(procedimiento, camposEntrada, camposRecuperar, campoError);
			
			if(resultados != null){
				literalesNormativa = new ArrayList<String>();
				for(int i=0; i < resultados.size(); i++){
					Map<String, String> item = resultados.get(i);
					literalesNormativa.add(item.get(Constantes.NORMATIVA_ESTRUCTURA + "." + Constantes.NORMATIVA_CAMPO));
				}	
			}
			
			
		} catch (Exception e) {
			es.tributasenasturias.documentopagoutils.Logger.error("LiteralesUtils.obtenerLiteralNormativa|"+tdoc+"|"+sdoc+"|"+tact,e,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}		
		return literalesNormativa;		
	}
}
