package es.tributasenasturias.pasarelas.comunicacion;
import es.infocaja.schemas.consulta_cobros.ArrayOfCONSULTACOBROSOutputSalidaNANC0061;
import es.infocaja.schemas.consulta_cobros.CONSULTACOBROSOUT;
import es.infocaja.schemas.consulta_cobros.CONSULTACOBROSOutputSalidaNANC0061;
import es.infocaja.schemas.pag_c3po.PAGC3POOUT;
import es.tributasenasturias.conversor.ConversorRespuestaBBVA;
import es.tributasenasturias.conversor.ConversorRespuestaCaixa;
import es.tributasenasturias.conversor.ConversorRespuestaCaja;
import es.tributasenasturias.conversor.ConversorRespuestaCajaRural;
import es.tributasenasturias.conversor.ConversorRespuestaUnicaja;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.EstadosPago;
import es.tributasenasturias.utils.Varios;

public class DatosComunicacion {
	
	private String idSujetoPasivo;
	private String modalidad;
	private String emisora;
	private String numeroOperacion;
	private String nrc;
	private String referencia;
	private String fechaPago;
	private String fechaAnulacion;
	private String importe;
	private String tarjeta;
	private String cuenta;
	private String identificacion;
	private String numeroExpediente;
	private String fechaDevengo;
	private String timestamp;
	private String resultadoRemoto;
	private String resultadoDescripcionRemoto;
	private String stamp;
	private String estadoCobroRemoto;
	private boolean error;
	private String codigoError;
	private String textoError;
	private String resultadoComunicacion;
	private EstadosPago estadoPago;
	/**
	 * @return the estadoPago
	 */
	public final EstadosPago getEstadoPago() {
		return estadoPago;
	}
	/**
	 * @return the error
	 */
	public final boolean isError() {
		return error;
	}
	/**
	 * @return the idSujetoPasivo
	 */
	public final String getIdSujetoPasivo() {
		return idSujetoPasivo;
	}
	/**
	 * @return the modalidad
	 */
	public final String getModalidad() {
		return modalidad;
	}
	/**
	 * @return the emisora
	 */
	public final String getEmisora() {
		return emisora;
	}
	/**
	 * @return the numeroOperacion
	 */
	public final String getNumeroOperacion() {
		return numeroOperacion;
	}
	/**
	 * @return the nrc
	 */
	public final String getNrc() {
		return nrc;
	}
	/**
	 * @return the referencia
	 */
	public final String getReferencia() {
		return referencia;
	}
	/**
	 * @return the fechaPago
	 */
	public final String getFechaPago() {
		return fechaPago;
	}
	/**
	 * @return the fechaAnulacion
	 */
	public final String getFechaAnulacion() {
		return fechaAnulacion;
	}
	/**
	 * @return the importe
	 */
	public final String getImporte() {
		return importe;
	}
	/**
	 * @return the tarjeta
	 */
	public final String getTarjeta() {
		return tarjeta;
	}
	/**
	 * @return the cuenta
	 */
	public final String getCuenta() {
		return cuenta;
	}
	/**
	 * @return the identificacion
	 */
	public final String getIdentificacion() {
		return identificacion;
	}
	/**
	 * @return the numeroExpediente
	 */
	public final String getNumeroExpediente() {
		return numeroExpediente;
	}
	/**
	 * @return the fechaDevengo
	 */
	public final String getFechaDevengo() {
		return fechaDevengo;
	}
	/**
	 * @return the timestamp
	 */
	public final String getTimestamp() {
		return timestamp;
	}
	/**
	 * @return the resultadoRemoto
	 */
	public final String getResultadoRemoto() {
		return resultadoRemoto;
	}
	/**
	 * @return the resultadoDescripcionRemoto
	 */
	public final String getResultadoDescripcionRemoto() {
		return resultadoDescripcionRemoto;
	}
	
	public DatosComunicacion()
	{
		
	}
	/**
	 * Constructor de resultadoRemoto de pasarela, en petición y anulación.
	 * @param out {@link PAGC3POOUT} que devuelve el servicio de Cajastur
	 */
	public DatosComunicacion(PAGC3POOUT out)
	{
		numeroOperacion = Varios.normalizeNull(out.getNUMOPER());
		resultadoRemoto = Varios.normalizeNull(out.getRESULTADO());
		resultadoDescripcionRemoto = Varios.normalizeNull(out.getRESULTADODESCRIPCION());
		ConversorRespuestaCaja cons = new ConversorRespuestaCaja();
		String resCajastur=cons.getResultadoCaja(out.getRESULTADODESCRIPCION());
		resultadoComunicacion = resCajastur; // En este caso lo que nos interesa es el resultadoRemoto traducido.
		if (resultadoRemoto.equals("KO") || (resultadoRemoto.equals("OK") && numeroOperacion==null))
		{
			error = true;
			this.estadoPago=EstadosPago.ERROR;
		}
		emisora = Varios.normalizeNull(out.getEMISOR());
		nrc = Varios.normalizeNull(out.getNRC());
		referencia = Varios.normalizeNull(out.getREFERENCIA());
		fechaPago = Varios.normalizeNull(out.getFECOPERA());
		fechaAnulacion = Varios.normalizeNull(out.getFECANULACION());
		importe = Varios.normalizeNull(out.getIMPORTE());
		tarjeta = Varios.normalizeNull(out.getCTACARGO());
		cuenta = Varios.normalizeNull(out.getCTACARGO());
		identificacion = Varios.normalizeNull(out.getIDENTIFICATIVO());
	}
	
	/**
	 * Constructor de resultadoRemoto de pasarela, en consulta.
	 * @param out {@link CONSULTACOBROSOUT} que devuelve el servicio de Cajastur
	 */
	public DatosComunicacion(CONSULTACOBROSOUT out)
	{
		ArrayOfCONSULTACOBROSOutputSalidaNANC0061 arrOut;
		CONSULTACOBROSOutputSalidaNANC0061 outData=null;
		this.resultadoRemoto = Varios.normalizeNull(out.getRESULTADO());
		this.resultadoDescripcionRemoto = Varios.normalizeNull(out.getRESULTADODESCRIPCION());
		ConversorRespuestaCaja cons = new ConversorRespuestaCaja();
		String resLiberbank=cons.getResultadoCaja(out.getRESULTADODESCRIPCION());
		resultadoComunicacion = resLiberbank;
		if (resLiberbank.equalsIgnoreCase(Constantes.getNoPagadoLiberbank()))
		{
			this.estadoPago = EstadosPago.NO_PAGADO;
		}
		else if (out.getRESULTADO().equals("OK"))
		{
			arrOut = out.getCONSULTACOBROSOutputSalidaNANC0061();
			outData = arrOut.getArrayOfCONSULTACOBROSOutputSalidaNANC0061().get(0); //Se recupera el único que debería haber, y sólo si es OK.
			if (out.getRESULTADO().trim().equals("OK") && outData.getNIO()!=null )
			{
				if (outData.getESTADO().equals(Constantes.EstadoAnulacion.A.toString()) || 
						outData.getESTADO().equals(Constantes.EstadoAnulacion.C.toString()))
					{
						this.estadoPago= EstadosPago.PAGADO;
					}
				else if (outData.getESTADO().equals(Constantes.EstadoAnulacion.B.toString()))
				{
						this.estadoPago= EstadosPago.ANULADO;
				}
				this.nrc=Varios.normalizeNull(outData.getNRC());
				this.fechaPago = Varios.normalizeNull(outData.getFECOPER());
				this.numeroOperacion = Varios.normalizeNull(outData.getNIO());
				
			}
			else
			{
				this.estadoPago=EstadosPago.ERROR;
				this.error=true;
			}
		}
		else
		{
			this.estadoPago=EstadosPago.ERROR;
			this.error=true;
		}
	}
	/**
	 * Constructor que acepta un objeto de tipo PAGOSIN del BBVA
	 * TODO: Hacer subclasses de esto, para no mezclar la generación de cada una de las pasarelas.
	 * @param pago
	 */
	public DatosComunicacion (es.tributasenasturias.client.pasarela.bbva.PAGOOUT pago)
	{
		numeroOperacion = Varios.normalizeNull(pago.getNUMOPER());
		resultadoRemoto = Varios.normalizeNull(pago.getRESULTADO());
		resultadoDescripcionRemoto = Varios.normalizeNull(pago.getRESULTADODESCRIPCION());
		ConversorRespuestaBBVA cons = new ConversorRespuestaBBVA();
		//Caso especial. Si el código es el de "BBVA ha devuelto SOAPFAULT ". Es una chapuza, pero errores esperados de formato
		//los devuelven en SOAP Fault.
		String resBBVA;
		if (!Constantes.ERROR_SOAP_FAULT_BBVA.equals(pago.getRESULTADO()))
		{
			resBBVA=cons.getResultadoBBVA(pago.getRESULTADO());
		}
		else
		{
			resBBVA="";
		}
		resultadoComunicacion = resBBVA; // En este caso lo que nos interesa es el resultadoRemoto traducido.
		//TODO: parametrizar este resultado.
		if (!resultadoRemoto.equals(Constantes.OK_BBVA) || (resultadoRemoto.equals(Constantes.OK_BBVA) && numeroOperacion==null))
		{
			error = true;
			this.estadoPago=EstadosPago.ERROR;
		}
		emisora = Varios.normalizeNull(pago.getEMISORA());
		nrc = Varios.normalizeNull(pago.getNRC());
		referencia = Varios.normalizeNull(pago.getREFERENCIA());
		fechaPago = Varios.normalizeNull(pago.getFECOPERA());
		importe = Varios.normalizeNull(pago.getIMPORTERECIBO());
		tarjeta = Varios.normalizeNull(pago.getTARJETA());
		cuenta = Varios.normalizeNull(pago.getCUENTA());
		identificacion = Varios.normalizeNull(pago.getIDENTIFICACION());
	}
	
	/**
	 * Traducción de la salida de anulación de BBVA a datos de servicio
	 * @param anulacion
	 */
	public DatosComunicacion (es.tributasenasturias.client.pasarela.bbva.ANULACIONOUT anulacion)
	{
		numeroOperacion = Varios.normalizeNull(anulacion.getNUMOPER());
		resultadoRemoto = Varios.normalizeNull(anulacion.getRESULTADO());
		resultadoDescripcionRemoto = Varios.normalizeNull(anulacion.getRESULTADODESCRIPCION());
		ConversorRespuestaBBVA cons = new ConversorRespuestaBBVA();
		String resBBVA;
		if (!Constantes.ERROR_SOAP_FAULT_BBVA.equals(anulacion.getRESULTADO()))
		{
			resBBVA=cons.getResultadoBBVA(anulacion.getRESULTADO());
		}
		else
		{
			resBBVA="";
		}
		resultadoComunicacion = resBBVA; // En este caso lo que nos interesa es el resultadoRemoto traducido.
		//TODO: parametrizar este resultado.
		if (!resultadoRemoto.equals(Constantes.OK_BBVA) || (resultadoRemoto.equals(Constantes.OK_BBVA) && numeroOperacion==null))
		{
			error = true;
			this.estadoPago=EstadosPago.ERROR;
		}
		emisora = Varios.normalizeNull(anulacion.getEMISORA());
		nrc = Varios.normalizeNull(anulacion.getNRC());
		referencia = Varios.normalizeNull(anulacion.getREFERENCIA());
		fechaPago = Varios.normalizeNull(anulacion.getFECOPERA());
		importe = Varios.normalizeNull(anulacion.getIMPORTERECIBO());
		tarjeta = Varios.normalizeNull(anulacion.getTARJETA());
		cuenta = Varios.normalizeNull(anulacion.getCUENTA());
		fechaAnulacion = Varios.normalizeNull(anulacion.getFECANULACION());
		identificacion = Varios.normalizeNull(anulacion.getIDENTIFICACION());
	}
	/**
	 * Construcción del objeto que traducirá la salida  de  la consulta de cobros de BBVA a lo que puede entender nuestro servicio.
	 * @param consulta
	 */
	public DatosComunicacion (es.tributasenasturias.client.pasarela.bbva.CONSULTACOBROSOUT consulta)
	{
		this (false, consulta);
	}
	/**
	 * Construcción del objeto que traducirá la salida  de  la consulta de cobros de BBVA a lo que puede entender nuestro servicio.
	 * @param consultaAnulacion Indica si los datos de la comunicacion se han de tratar como los datos de una consulta de anulacióin
	 * @param consulta
	 */
	public DatosComunicacion (boolean consultaAnulacion,es.tributasenasturias.client.pasarela.bbva.CONSULTACOBROSOUT consulta)
	{
		numeroOperacion = Varios.normalizeNull(consulta.getNUMOPER());
		resultadoRemoto = Varios.normalizeNull(consulta.getRESULTADO());
		resultadoDescripcionRemoto = Varios.normalizeNull(consulta.getRESULTADODESCRIPCION());
		ConversorRespuestaBBVA cons = new ConversorRespuestaBBVA();
		String resBBVA;
		if (!Constantes.ERROR_SOAP_FAULT_BBVA.equals(consulta.getRESULTADO()))
		{
			resBBVA=cons.getResultadoBBVA(consulta.getRESULTADO());
		}
		else
		{
			resBBVA="";
		}
		resultadoComunicacion = resBBVA; // En este caso lo que nos interesa es el resultadoRemoto traducido.
		//TODO: Parametrizar este estado.
		if (resBBVA.equalsIgnoreCase(Constantes.CODIGO_NO_PAGADO_BBVA))
		{
			if (!consultaAnulacion)
			{
				this.estadoPago = EstadosPago.NO_PAGADO;
			}
			else
			{
				//En consulta de anulación, no encontrado es anulado (al anular ya no se puede consultar el tributo).
				this.estadoPago= EstadosPago.ANULADO;
			}
		}
		//TODO: Parametrizar este estado.
		else if (consulta.getRESULTADO().equals(Constantes.OK_BBVA))
		{
			if (consulta.getRESULTADO().trim().equals(Constantes.OK_BBVA) && consulta.getNUMOPER()!=null )
			{
				if (consulta.getESTADO().equals(Constantes.EstadoAnulacion.A.toString()) || 
						consulta.getESTADO().equals(Constantes.EstadoAnulacion.C.toString()))
					{
						this.estadoPago= EstadosPago.PAGADO;
					}
				else if (consulta.getESTADO().equals(Constantes.EstadoAnulacion.B.toString()))
				{
						this.estadoPago= EstadosPago.ANULADO;
				}
				this.nrc=Varios.normalizeNull(consulta.getNRC());
				this.fechaPago = Varios.normalizeNull(consulta.getFECPAGO());
				this.numeroOperacion = Varios.normalizeNull(consulta.getNUMOPER());
				
			}
			else
			{
				this.estadoPago=EstadosPago.ERROR;
				this.error=true;
			}
		}
		else
		{
			this.estadoPago=EstadosPago.ERROR;
			this.error=true;
		}
	}
	/**
	 * Constructor que acepta un objeto de tipo PAGOSOUT de Caja Rural
	 * TODO: Hacer subclasses de esto, para no mezclar la generación de cada una de las pasarelas.
	 * @param pago
	 */
	public DatosComunicacion (com.ruralserviciosinformaticos.empresa.se_tributosasturiaspago.PAGOOUT pago)
	{
		numeroOperacion = Varios.normalizeNull(pago.getNUMOPER());
		resultadoRemoto = Varios.normalizeNull(pago.getRESULTADO());
		resultadoDescripcionRemoto = Varios.normalizeNull(pago.getRESULTADODESCRIPCION());
		ConversorRespuestaCajaRural cons = new ConversorRespuestaCajaRural();
		
		resultadoComunicacion = cons.getResultadoCajaRural(pago.getRESULTADODESCRIPCION()); // En este caso lo que nos interesa es el resultadoRemoto traducido.
		//TODO: parametrizar este resultado.
		if (!Constantes.OK_CRURAL.equalsIgnoreCase(resultadoComunicacion) || (Constantes.OK_CRURAL.equalsIgnoreCase(resultadoComunicacion) && numeroOperacion==null))
		{
			error = true;
			this.estadoPago=EstadosPago.ERROR;
		}
		emisora = Varios.normalizeNull(pago.getEMISORA());
		nrc = Varios.normalizeNull(pago.getNRC());
		referencia = Varios.normalizeNull(pago.getREFERENCIA());
		fechaPago = Varios.normalizeNull(pago.getFECOPERA());
		importe = Varios.normalizeNull(pago.getIMPORTERECIBO());
		tarjeta = Varios.normalizeNull(pago.getTARJETA());
		cuenta = Varios.normalizeNull(pago.getCUENTA());
		identificacion = Varios.normalizeNull(pago.getIDENTIFICACION());
	}
	/**
	 * Traducción de la salida de anulación de CajaRural a datos de servicio
	 * @param anulacion
	 */
	public DatosComunicacion (com.ruralserviciosinformaticos.empresa.se_tributosasturiasanulacion.ANULACIONOUT anulacion)
	{
		numeroOperacion = Varios.normalizeNull(anulacion.getNUMOPER());
		resultadoRemoto = Varios.normalizeNull(anulacion.getRESULTADO());
		resultadoDescripcionRemoto = Varios.normalizeNull(anulacion.getRESULTADODESCRIPCION());
		ConversorRespuestaCajaRural cons = new ConversorRespuestaCajaRural();
		resultadoComunicacion = cons.getResultadoCajaRural(anulacion.getRESULTADODESCRIPCION()); // En este caso lo que nos interesa es el resultadoRemoto traducido.
		//TODO: parametrizar este resultado.
		if (!Constantes.ANULACION_OK_CRURAL.equals(resultadoRemoto) || (Constantes.ANULACION_OK_CRURAL.equals(resultadoRemoto) && numeroOperacion==null))
		{
			error = true;
			this.estadoPago=EstadosPago.ERROR;
		}
		emisora = Varios.normalizeNull(anulacion.getEMISORA());
		nrc = Varios.normalizeNull(anulacion.getNRC());
		referencia = Varios.normalizeNull(anulacion.getREFERENCIA());
		fechaPago = Varios.normalizeNull(anulacion.getFECOPERA());
		importe = Varios.normalizeNull(anulacion.getIMPORTERECIBO());
		tarjeta = Varios.normalizeNull(anulacion.getTARJETA());
		cuenta = Varios.normalizeNull(anulacion.getCUENTA());
		fechaAnulacion = Varios.normalizeNull(anulacion.getFECANULACION());
		identificacion = Varios.normalizeNull(anulacion.getIDENTIFICACION());
	}
	/**
	 * Construcción del objeto que traducirá la salida  de  la consulta de cobros de Caja Rural a lo que puede entender nuestro servicio.
	 * @param consulta
	 */
	public DatosComunicacion (com.ruralserviciosinformaticos.empresa.se_tributosasturiasconsulta.CONSULTACOBROSOUT consulta)
	{
		this (false, consulta);
	}
	/**
	 * Construcción del objeto que traducirá la salida  de  la consulta de cobros de Caja Rural a lo que puede entender nuestro servicio.
	 * @param consultaAnulacion Indica si los datos de la comunicacion se han de tratar como los datos de una consulta de anulacióin.
	 *        Después de anular, las consultas indican que el pago no existe.
	 * @param consulta
	 */
	public DatosComunicacion (boolean consultaAnulacion,com.ruralserviciosinformaticos.empresa.se_tributosasturiasconsulta.CONSULTACOBROSOUT consulta)
	{
		numeroOperacion = Varios.normalizeNull(consulta.getNUMOPER());
		resultadoRemoto = Varios.normalizeNull(consulta.getRESULTADO());
		resultadoDescripcionRemoto = Varios.normalizeNull(consulta.getRESULTADODESCRIPCION());
		ConversorRespuestaCajaRural cons = new ConversorRespuestaCajaRural();
		resultadoComunicacion = cons.getResultadoCajaRural(consulta.getRESULTADODESCRIPCION()); // En este caso lo que nos interesa es el resultadoRemoto traducido.
		//TODO: Parametrizar este estado.
		if (Constantes.CODIGO_NO_PAGADO_CRURAL.equalsIgnoreCase(resultadoComunicacion))
		{
			if (!consultaAnulacion)
			{
				this.estadoPago = EstadosPago.NO_PAGADO;
			}
			else
			{
				//En consulta de anulación, no encontrado es anulado (al anular ya no se puede consultar el tributo).
				this.estadoPago= EstadosPago.ANULADO;
			}
		}
		//TODO: Parametrizar este estado.
		else if (Constantes.CONSULTA_OK_CRURAL.equalsIgnoreCase(consulta.getRESULTADO().trim()))
		{
			if (Constantes.CONSULTA_OK_CRURAL.equalsIgnoreCase(consulta.getRESULTADO().trim()) && consulta.getNUMOPER()!=null )
			{
				if (Constantes.EstadoAnulacion.A.toString().equals(consulta.getESTADO()) || 
						Constantes.EstadoAnulacion.C.toString().equals(consulta.getESTADO()))
					{
						this.estadoPago= EstadosPago.PAGADO;
					}
				else if (Constantes.EstadoAnulacion.B.toString().equals(consulta.getESTADO()))
				{
						this.estadoPago= EstadosPago.ANULADO;
				}
				this.nrc=Varios.normalizeNull(consulta.getNRC());
				this.fechaPago = Varios.normalizeNull(consulta.getFECPAGO());
				this.numeroOperacion = Varios.normalizeNull(consulta.getNUMOPER());
				
			}
			else
			{
				this.estadoPago=EstadosPago.ERROR;
				this.error=true;
			}
		}
		else
		{
			this.estadoPago=EstadosPago.ERROR;
			this.error=true;
		}
	}
	
	/**
	 * Constructor que acepta un objeto de tipo PAGOIN de la Caixa
	 * TODO: Hacer subclasses de esto, para no mezclar la generación de cada una de las pasarelas.
	 * @param pago
	 */
	public DatosComunicacion (org.ejemplo.PAGOOUT pago)
	{
		numeroOperacion = Varios.normalizeNull(pago.getNUMOPER());
		resultadoRemoto = Varios.normalizeNull(pago.getRESULTADO());
		resultadoDescripcionRemoto = Varios.normalizeNull(pago.getRESULTADODESCRIPCION());

		ConversorRespuestaCaixa cons = new ConversorRespuestaCaixa();

		resultadoComunicacion = cons.getResultadoCaixa(pago.getRESULTADODESCRIPCION()); 
		//TODO: parametrizar este resultado.
		if (!Constantes.OK_CAIXA.equalsIgnoreCase(resultadoRemoto) || (Constantes.OK_CAIXA.equalsIgnoreCase(resultadoRemoto) && numeroOperacion==null))
		{
			error = true;
			this.estadoPago=EstadosPago.ERROR;
		}
		emisora = Varios.normalizeNull(pago.getEMISORA());
		nrc = Varios.normalizeNull(pago.getNRC());
		referencia = Varios.normalizeNull(pago.getREFERENCIA());
		fechaPago = Varios.normalizeNull(pago.getFECOPERA());
		importe = Varios.normalizeNull(pago.getIMPORTERECIBO());
		tarjeta = Varios.normalizeNull(pago.getTARJETA());
		cuenta = Varios.normalizeNull(pago.getCUENTA());
		identificacion = Varios.normalizeNull(pago.getIDENTIFICACION());
	}
	
	/**
	 * Traducción de la salida de anulación de LaCaixa a datos de servicio
	 * @param anulacion
	 */
	public DatosComunicacion (org.ejemplo.ANULACIONOUT anulacion)
	{
		numeroOperacion = Varios.normalizeNull(anulacion.getNUMOPER());
		resultadoRemoto = Varios.normalizeNull(anulacion.getRESULTADO());
		resultadoDescripcionRemoto = Varios.normalizeNull(anulacion.getRESULTADODESCRIPCION());
		
		ConversorRespuestaCaixa cons = new ConversorRespuestaCaixa();

		resultadoComunicacion = cons.getResultadoCaixa(anulacion.getRESULTADODESCRIPCION()); // En este caso lo que nos interesa es el resultadoRemoto traducido.
		//TODO: parametrizar este resultado.
		if (!Constantes.OK_CAIXA.equalsIgnoreCase(resultadoRemoto) || (Constantes.OK_CAIXA.equalsIgnoreCase(resultadoRemoto) && numeroOperacion==null))
		{
			error = true;
			this.estadoPago=EstadosPago.ERROR;
		}
		emisora = Varios.normalizeNull(anulacion.getEMISORA());
		nrc = Varios.normalizeNull(anulacion.getNRC());
		referencia = Varios.normalizeNull(anulacion.getREFERENCIA());
		fechaPago = Varios.normalizeNull(anulacion.getFECOPERA());
		importe = Varios.normalizeNull(anulacion.getIMPORTERECIBO());
		tarjeta = Varios.normalizeNull(anulacion.getTARJETA());
		cuenta = Varios.normalizeNull(anulacion.getCUENTA());
		fechaAnulacion = Varios.normalizeNull(anulacion.getFECANULACION());
		identificacion = Varios.normalizeNull(anulacion.getIDENTIFICACION());
	}
	/**
	 * Construcción del objeto que traducirá la salida  de  la consulta de cobros de LaCaixa a lo que puede entender nuestro servicio.
	 * @param consulta
	 */
	public DatosComunicacion (org.ejemplo.CONSULTACOBROSOUT consulta)
	{
		this (false, consulta);
	}
	/**
	 * Construcción del objeto que traducirá la salida  de  la consulta de cobros de LaCaixa a lo que puede entender nuestro servicio.
	 * @param consultaAnulacion Indica si los datos de la comunicacion se han de tratar como los datos de una consulta de anulacióin
	 * @param consulta
	 */
	public DatosComunicacion (boolean consultaAnulacion,org.ejemplo.CONSULTACOBROSOUT consulta)
	{
		numeroOperacion = Varios.normalizeNull(consulta.getNUMOPER());
		resultadoRemoto = Varios.normalizeNull(consulta.getRESULTADO());
		resultadoDescripcionRemoto = Varios.normalizeNull(consulta.getRESULTADODESCRIPCION());
		ConversorRespuestaCaixa cons = new ConversorRespuestaCaixa();

		resultadoComunicacion = cons.getResultadoCaixa(consulta.getRESULTADODESCRIPCION()); // En este caso lo que nos interesa es el resultadoRemoto traducido.
		//TODO: Parametrizar este estado.
		if (Constantes.CODIGO_NO_PAGADO_CAIXA.equalsIgnoreCase(resultadoComunicacion))
		{
			if (!consultaAnulacion)
			{
				this.estadoPago = EstadosPago.NO_PAGADO;
			}
			else
			{
				//En consulta de anulación, no encontrado es anulado (al anular desaparece el pago).
				this.estadoPago= EstadosPago.ANULADO;
			}
		}
		//TODO: Parametrizar este estado.
		else if (Constantes.OK_CAIXA.equalsIgnoreCase(consulta.getRESULTADO()))
		{
			if (Constantes.OK_CAIXA.equalsIgnoreCase(consulta.getRESULTADO().trim()) && consulta.getNRC()!=null )
			{
				//Para anulado nunca se devuelven datos, así que si se devuelven es que está pagado
				this.nrc=Varios.normalizeNull(consulta.getNRC());
				this.fechaPago = Varios.normalizeNull(consulta.getFECPAGO());
				this.numeroOperacion = Varios.normalizeNull(consulta.getNRC()); // Para LaCaixa el número de operación y el NRC son lo mismo.
				this.estadoPago= EstadosPago.PAGADO;
			}
			else
			{
				this.estadoPago=EstadosPago.ERROR;
				this.error=true;
			}
		}
		else
		{
			this.estadoPago=EstadosPago.ERROR;
			this.error=true;
		}
	}
	
	// CRUBENCVS 44613 08/04/2022. Se han generado tipos específicos para Unicaja, 
	// y se han de tratar.
	/**
	 * Constructor de resultadoRemoto de pasarela, en petición y anulación.
	 * @param out {@link es.types.unicaja.pago.PAGC3POOUT} que devuelve el servicio de Unicaja
	 */
	public DatosComunicacion(es.types.unicaja.pago.PAGC3POOUT out)
	{
		numeroOperacion = Varios.normalizeNull(out.getNUMOPER());
		resultadoRemoto = Varios.normalizeNull(out.getRESULTADO());
		resultadoDescripcionRemoto = Varios.normalizeNull(out.getRESULTADODESCRIPCION());
		ConversorRespuestaUnicaja cons = new ConversorRespuestaUnicaja();
		String resUnicaja=cons.getResultadoUnicaja(out.getRESULTADODESCRIPCION());
		resultadoComunicacion = resUnicaja; // En este caso lo que nos interesa es el resultadoRemoto traducido.
		if (resultadoRemoto.equals("KO") || (resultadoRemoto.equals("OK") && numeroOperacion==null))
		{
			error = true;
			this.estadoPago=EstadosPago.ERROR;
		}
		emisora = Varios.normalizeNull(out.getEMISOR());
		nrc = Varios.normalizeNull(out.getNRC());
		referencia = Varios.normalizeNull(out.getREFERENCIA());
		fechaPago = Varios.normalizeNull(out.getFECOPERA());
		fechaAnulacion = Varios.normalizeNull(out.getFECANULACION());
		importe = Varios.normalizeNull(out.getIMPORTE());
		tarjeta = Varios.normalizeNull(out.getCTACARGO());
		cuenta = Varios.normalizeNull(out.getCTACARGO());
		identificacion = Varios.normalizeNull(out.getIDENTIFICATIVO());
	}
	
	/**
	 * Constructor de resultadoRemoto de pasarela, en consulta.
	 * @param out {@link es.types.unicaja.consulta.CONSULTACOBROSOUT} que devuelve el servicio de Unicaja
	 */
	public DatosComunicacion(es.types.unicaja.consulta.CONSULTACOBROSOUT out)
	{
		es.types.unicaja.consulta.ArrayOfCONSULTACOBROSOutputSalidaNANC0061 arrOut;
		es.types.unicaja.consulta.CONSULTACOBROSOutputSalidaNANC0061 outData=null;
		this.resultadoRemoto = Varios.normalizeNull(out.getRESULTADO());
		this.resultadoDescripcionRemoto = Varios.normalizeNull(out.getRESULTADODESCRIPCION());
		ConversorRespuestaUnicaja cons = new ConversorRespuestaUnicaja();
		String resUnicaja=cons.getResultadoUnicaja(out.getRESULTADODESCRIPCION());
		resultadoComunicacion = resUnicaja;
		if (resUnicaja.equalsIgnoreCase(Constantes.getNoDatosUnicaja()))
		{
			this.estadoPago = EstadosPago.NO_PAGADO;
		}
		else if (out.getRESULTADO().equals("OK"))
		{
			arrOut = out.getCONSULTACOBROSOutputSalidaNANC0061();
			outData = arrOut.getArrayOfCONSULTACOBROSOutputSalidaNANC0061().get(0); //Se recupera el único que debería haber, y sólo si es OK.
			if (out.getRESULTADO().trim().equals("OK") && outData.getNIO()!=null )
			{
				//En realidad, creo que nunca devuelven "C",  pero si lo devolviesen,
				//sería "Pagado"
				if (outData.getESTADO().equals(Constantes.EstadoAnulacion.A.toString()) || 
						outData.getESTADO().equals(Constantes.EstadoAnulacion.C.toString()))
					{
						this.estadoPago= EstadosPago.PAGADO;
					}
				else if (outData.getESTADO().equals(Constantes.EstadoAnulacion.B.toString()))
				{
						this.estadoPago= EstadosPago.ANULADO;
				}
				this.nrc=Varios.normalizeNull(outData.getNRC());
				this.fechaPago = Varios.normalizeNull(outData.getFECOPER());
				this.numeroOperacion = Varios.normalizeNull(outData.getNIO());
				
			}
			else
			{
				this.estadoPago=EstadosPago.ERROR;
				this.error=true;
			}
		}
		else
		{
			this.estadoPago=EstadosPago.ERROR;
			this.error=true;
		}
	}
	/**
	 * @return the codigoRetorno
	 */
	public final String getCodigoError() {
		return codigoError;
	}
	/**
	 * @return the textoError
	 */
	public final String getTextoError() {
		return textoError;
	}
	/**
	 * @param error the error to set
	 */
	public final void setError(boolean error) {
		this.error = error;
	}
	/**
	 * @param codigoError the codigoError to set
	 */
	public final void setCodigoError(String codigoError) {
		this.codigoError = codigoError;
	}
	/**
	 * @param textoError the textoError to set
	 */
	public final void setTextoError(String textoError) {
		this.textoError = textoError;
	}
	/**
	 * @return the resultadoComunicacion
	 */
	public final String getResultadoComunicacion() {
		return resultadoComunicacion;
	}
	/**
	 * @return the stamp
	 */
	public final String getStamp() {
		return stamp;
	}
	/**
	 * @return the estadoCobroRemoto
	 */
	public final String getEstadoCobroRemoto() {
		return estadoCobroRemoto;
	}
	
}
