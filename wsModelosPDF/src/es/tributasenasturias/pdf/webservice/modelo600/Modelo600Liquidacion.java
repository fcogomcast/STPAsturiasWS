package es.tributasenasturias.pdf.webservice.modelo600;

import org.w3c.dom.Node;

import es.tributasenasturias.pdf.utils.XMLUtils;

public class Modelo600Liquidacion {

	private Node liquidacion;

	private String VALOR_DECLARADO="";
	private static String VALOR_DECLARADO_NOTARIO="ValorDeclarado";
	private static String VALOR_DECLARADO_GENERICO="valorDeclarado";
	private String TIPOLIQ_EXENTO="";
	private static String TIPOLIQ_EXENTO_NOTARIO="Exento";
	private static String TIPOLIQ_EXENTO_GENERICO="exento";
	private String TIPOLIQ_EXENTOPROVISIONAL="";
	private static String TIPOLIQ_EXENTOPROVISIONAL_NOTARIO="ExentoProvisional";
	private static String TIPOLIQ_EXENTOPROVISIONAL_GENERICO="exentoProvisional";
	private String TIPOLIQ_NOSUJETO="";
	private static String TIPOLIQ_NOSUJETO_NOTARIO="NoSujeto";
	private static String TIPOLIQ_NOSUJETO_GENERICO="noSujeto";
	private String TIPOLIQ_PRESCRITO="";
	private static String TIPOLIQ_PRESCRITO_NOTARIO="Prescrito";
	private static String TIPOLIQ_PRESCRITO_GENERICO="prescrito";
	private String BASE_IMPONIBLE="";
	private static String BASE_IMPONIBLE_NOTARIO="BaseImponible";
	private static String BASE_IMPONIBLE_GENERICO="baseImponible";
	private String BASE_LIQUIDABLE="";
	private static String BASE_LIQUIDABLE_NOTARIO="BaseLiquidable";
	private static String BASE_LIQUIDABLE_GENERICO="baseLiquidable";
	private String REDUCCION_PORCENTAJE="";
	private static String REDUCCION_PORCENTAJE_NOTARIO="Reduccion";
	private static String REDUCCION_PORCENTAJE_GENERICO="reduccion";
	private String REDUCCION_IMPORTE="";
	private static String REDUCCION_IMPORTE_NOTARIO="ReduccImporte";
	private static String REDUCCION_IMPORTE_GENERICO="reduccImporte";
	private String TIPO_IMPOSITIVO="";
	private static String TIPO_IMPOSITIVO_NOTARIO="Tipo";
	private static String TIPO_IMPOSITIVO_GENERICO="tipo";
	private String CUOTA="";
	private static String CUOTA_NOTARIO="Cuota";
	private static String CUOTA_GENERICO="cuota";
	private String BONIFICACION_CUOTA_PORCENTAJE="";
	private static String BONIFICACION_CUOTA_PORCENTAJE_NOTARIO="BonificacionCuota";
	private static String BONIFICACION_CUOTA_PORCENTAJE_GENERICO="bonificacionCuota";
	private String BONIFICACION_CUOTA_IMPORTE="";
	private static String BONIFICACION_CUOTA_IMPORTE_NOTARIO="BonificacionCuotaImporte";
	private static String BONIFICACION_CUOTA_IMPORTE_GENERICO="bonificacionCuotaImporte";
	private String A_INGRESAR="";
	private static String A_INGRESAR_NOTARIO="Aingresar";
	private static String A_INGRESAR_GENERICO="aIngresar";
	private String RECARGO="";
	private static String RECARGO_NOTARIO="Recargo";
	private static String RECARGO_GENERICO="recargo";
	private String INTERESES_DEMORA="";
	private static String INTERESES_DEMORA_NOTARIO="Intereses";
	private static String INTERESES_DEMORA_GENERICO="intereses";
	private String IMPORTE_LIQUIDACION_ANTERIOR="";
	private static String IMPORTE_LIQUIDACION_ANTERIOR_NOTARIO="ImporteLiquidacionAnterior";
	private static String IMPORTE_LIQUIDACION_ANTERIOR_GENERICO="importeLiquidacionAnterior";
	private String TOTAL_INGRESAR="";
	private static String TOTAL_INGRESAR_NOTARIO="TotalIngresar";
	private static String TOTAL_INGRESAR_GENERICO="totalIngresar";
	private String FUNDAMENTO_LEGAL="";
	private static String FUNDAMENTO_LEGAL_NOTARIO="FundamentoLegalDesc";
	private static String FUNDAMENTO_LEGAL_GENERICO="fundamentoLegalDesc";
	private String LIQUIDACION_COMPLEMENTARIA="";
	private static String LIQUIDACION_COMPLEMENTARIA_NOTARIO="LiquidacionComplementaria";
	private static String LIQUIDACION_COMPLEMENTARIA_GENERICO="liquidacionComplementaria";
	private String FECHA_PRIMERA_LIQUIDACION="";
	private static String FECHA_PRIMERA_LIQUIDACION_NOTARIO="FechaPrimeraLiquidacion";
	private static String FECHA_PRIMERA_LIQUIDACION_GENERICO="fechaPrimeraLiquidacion";
	private String NUMERO_PRIMERA_LIQUIDACION="";
	private static String NUMERO_PRIMERA_LIQUIDACION_NOTARIO="NumJustificantePrimeraLiquidacion";
	private static String NUMERO_PRIMERA_LIQUIDACION_GENERICO="numJustificantePrimeraLiquidacion";
	private String REDUCCION_EXCESO_CUOTA="";
	private static String REDUCCION_EXCESO_CUOTA_NOTARIO="ReduccionExcesoCuota";
	private static String REDUCCION_EXCESO_CUOTA_GENERICO="reduccionExcesoCuota";
	private String CUOTA_INT_AJUSTADA="";
	private static String CUOTA_INT_AJUSTADA_NOTARIO="CuotaIntAjustada";
	private static String CUOTA_INT_AJUSTADA_GENERICO="cuotaIntAjustada";

	public Modelo600Liquidacion () {}

	public Node getNodeLiquidacion(){
		return liquidacion;
	}

	public Modelo600Liquidacion (Node liq,boolean notarios) {
		if (notarios){
			VALOR_DECLARADO=VALOR_DECLARADO_NOTARIO;
			TIPOLIQ_EXENTO=TIPOLIQ_EXENTO_NOTARIO;
			TIPOLIQ_EXENTOPROVISIONAL=TIPOLIQ_EXENTOPROVISIONAL_NOTARIO;
			TIPOLIQ_NOSUJETO=TIPOLIQ_NOSUJETO_NOTARIO;
			TIPOLIQ_PRESCRITO=TIPOLIQ_PRESCRITO_NOTARIO;
			BASE_IMPONIBLE=BASE_IMPONIBLE_NOTARIO;
			BASE_LIQUIDABLE=BASE_LIQUIDABLE_NOTARIO;
			REDUCCION_PORCENTAJE=REDUCCION_PORCENTAJE_NOTARIO;
			REDUCCION_IMPORTE=REDUCCION_IMPORTE_NOTARIO;
			TIPO_IMPOSITIVO=TIPO_IMPOSITIVO_NOTARIO;
			CUOTA=CUOTA_NOTARIO;
			BONIFICACION_CUOTA_PORCENTAJE=BONIFICACION_CUOTA_PORCENTAJE_NOTARIO;
			BONIFICACION_CUOTA_IMPORTE=BONIFICACION_CUOTA_IMPORTE_NOTARIO;
			A_INGRESAR=A_INGRESAR_NOTARIO;
			RECARGO=RECARGO_NOTARIO;
			INTERESES_DEMORA=INTERESES_DEMORA_NOTARIO;
			IMPORTE_LIQUIDACION_ANTERIOR=IMPORTE_LIQUIDACION_ANTERIOR_NOTARIO;
			TOTAL_INGRESAR=TOTAL_INGRESAR_NOTARIO;
			FUNDAMENTO_LEGAL=FUNDAMENTO_LEGAL_NOTARIO;
			LIQUIDACION_COMPLEMENTARIA=LIQUIDACION_COMPLEMENTARIA_NOTARIO;
			FECHA_PRIMERA_LIQUIDACION=FECHA_PRIMERA_LIQUIDACION_NOTARIO;
			NUMERO_PRIMERA_LIQUIDACION=NUMERO_PRIMERA_LIQUIDACION_NOTARIO;
			REDUCCION_EXCESO_CUOTA=REDUCCION_EXCESO_CUOTA_NOTARIO;
			CUOTA_INT_AJUSTADA=CUOTA_INT_AJUSTADA_NOTARIO;
		}else{
			VALOR_DECLARADO=VALOR_DECLARADO_GENERICO;
			TIPOLIQ_EXENTO=TIPOLIQ_EXENTO_GENERICO;
			TIPOLIQ_EXENTOPROVISIONAL=TIPOLIQ_EXENTOPROVISIONAL_GENERICO;
			TIPOLIQ_NOSUJETO=TIPOLIQ_NOSUJETO_GENERICO;
			TIPOLIQ_PRESCRITO=TIPOLIQ_PRESCRITO_GENERICO;
			BASE_IMPONIBLE=BASE_IMPONIBLE_GENERICO;
			BASE_LIQUIDABLE=BASE_LIQUIDABLE_GENERICO;
			REDUCCION_PORCENTAJE=REDUCCION_PORCENTAJE_GENERICO;
			REDUCCION_IMPORTE=REDUCCION_IMPORTE_GENERICO;
			TIPO_IMPOSITIVO=TIPO_IMPOSITIVO_GENERICO;
			CUOTA=CUOTA_GENERICO;
			BONIFICACION_CUOTA_PORCENTAJE=BONIFICACION_CUOTA_PORCENTAJE_GENERICO;
			BONIFICACION_CUOTA_IMPORTE=BONIFICACION_CUOTA_IMPORTE_GENERICO;
			A_INGRESAR=A_INGRESAR_GENERICO;
			RECARGO=RECARGO_GENERICO;
			INTERESES_DEMORA=INTERESES_DEMORA_GENERICO;
			IMPORTE_LIQUIDACION_ANTERIOR=IMPORTE_LIQUIDACION_ANTERIOR_GENERICO;
			TOTAL_INGRESAR=TOTAL_INGRESAR_GENERICO;
			FUNDAMENTO_LEGAL=FUNDAMENTO_LEGAL_GENERICO;
			LIQUIDACION_COMPLEMENTARIA=LIQUIDACION_COMPLEMENTARIA_GENERICO;
			FECHA_PRIMERA_LIQUIDACION=FECHA_PRIMERA_LIQUIDACION_GENERICO;
			NUMERO_PRIMERA_LIQUIDACION=NUMERO_PRIMERA_LIQUIDACION_GENERICO;
			REDUCCION_EXCESO_CUOTA=REDUCCION_EXCESO_CUOTA_GENERICO;
			CUOTA_INT_AJUSTADA=CUOTA_INT_AJUSTADA_GENERICO;
		}
		this.liquidacion = liq;
	}

	public String getValorDeclarado() {
		return XMLUtils.getItemSimple(this.liquidacion,VALOR_DECLARADO,null,null,false);
	}

	public String getTipoLiquidacion() {
		return XMLUtils.getItemIterador(this.liquidacion,new String[]{TIPOLIQ_EXENTO,TIPOLIQ_EXENTOPROVISIONAL,TIPOLIQ_NOSUJETO,TIPOLIQ_PRESCRITO},
														 new String[]{"S","S","S","S"},new String[]{"EF","EP","NS","P"},false,false);
	}	

	public String getBaseImponible() {
		return XMLUtils.getItemSimple(this.liquidacion,BASE_IMPONIBLE,null,null,false);
	}

	public String getPorcentajeReduccion() {
		return XMLUtils.getItemSimple(this.liquidacion,REDUCCION_PORCENTAJE,null,null,false);
	}

	public String getReduccion() {
		return XMLUtils.getItemSimple(this.liquidacion,REDUCCION_IMPORTE,null,null,false);
	}

	public String getBaseLiquidable() {
		return XMLUtils.getItemSimple(this.liquidacion,BASE_LIQUIDABLE,null,null,false);
	}

	public String getTipoImpositivo() {
		return XMLUtils.getItemSimple(this.liquidacion,TIPO_IMPOSITIVO,null,null,false);
	}

	public String getCuota() {
		return XMLUtils.getItemSimple(this.liquidacion,CUOTA,null,null,false);
	}

	public String getPorcentajeBonificacionCuota() {
		return XMLUtils.getItemSimple(this.liquidacion,BONIFICACION_CUOTA_PORCENTAJE,null,null,false);
	}

	public String getBonificacionCuota() {
		return XMLUtils.getItemSimple(this.liquidacion,BONIFICACION_CUOTA_IMPORTE,null,null,false);
	}

	public String getAIngresar() {
		return XMLUtils.getItemSimple(this.liquidacion,A_INGRESAR,null,null,false);
	}
	
	public String getRecargo() {
		return XMLUtils.getItemSimple(this.liquidacion,RECARGO,null,null,false);
	}
	
	public String getInteresesDemora() {
		return XMLUtils.getItemSimple(this.liquidacion,INTERESES_DEMORA,null,null,false);
	}

	public String getImporteIngresadoLiquidacionAnterior() {
		return XMLUtils.getItemSimple(this.liquidacion,IMPORTE_LIQUIDACION_ANTERIOR,null,null,false);
	}

	public String getTotalAIngresar() {
		return XMLUtils.getItemSimple(this.liquidacion,TOTAL_INGRESAR,null,null,false);
	}

	public String getFundamento() {
		return XMLUtils.getItemSimple(this.liquidacion,FUNDAMENTO_LEGAL,null,null,false);
	}	

	public String getAutoliquidacionComplementaria() {
		return XMLUtils.getItemSimple(this.liquidacion,LIQUIDACION_COMPLEMENTARIA,"S","X",false);
	}

	public String getFechaPresentacionLiquidacionAnterior()
	{
		return XMLUtils.getItemSimple(this.liquidacion,FECHA_PRIMERA_LIQUIDACION,null,null,false);
	}
	public String getNumeroPrimeraAutoliquidacion() {
		return XMLUtils.getItemSimple(this.liquidacion,NUMERO_PRIMERA_LIQUIDACION,null,null,false);
	}

	//**INI**CRUBENCVS**27/09/2010**Sin número. Incluir la reducción por exceso de cuota y la cuota íntegra ajustada.
	public String getReducExcesoCuota() {
		return XMLUtils.getItemSimple(this.liquidacion,REDUCCION_EXCESO_CUOTA,null,null,false);
	}

	public String getCuotaIntAjustada() {
		return XMLUtils.getItemSimple(this.liquidacion,CUOTA_INT_AJUSTADA,null,null,false);
	}
	//**FIN**CRUBENCVS**27/09/2010**Sin número. Incluir la reducción por exceso de cuota y la cuota íntegra ajustada.
}
