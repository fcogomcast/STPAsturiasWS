package es.tributasenasturias.autenticacionlocalepst.business;

import java.text.ParseException;
import java.util.Calendar;

import es.tributasenasturias.autenticacionlocalepst.ResultadoType;
import es.tributasenasturias.autenticacionlocalepst.exceptions.CertificadoException;
import es.tributasenasturias.autenticacionlocalepst.prefs.Preferencias;
import es.tributasenasturias.autenticacionlocalepst.soap.SoapClientHandler;
import es.tributasenasturias.autenticacionlocalepst.utils.Constantes;
import es.tributasenasturias.lanzador.LanzadorException;
import es.tributasenasturias.lanzador.LanzadorFactory;
import es.tributasenasturias.lanzador.ParamType;
import es.tributasenasturias.lanzador.ProcedimientoAlmacenado;
import es.tributasenasturias.lanzador.TLanzador;
import es.tributasenasturias.lanzador.response.RespuestaLanzador;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.seguridad.AutenticacionCertificado;
import es.tributasenasturias.seguridad.SeguridadException;
import es.tributasenasturias.seguridad.AutenticacionCertificado.ResultadoAutenticacion;

/**
 * Permite realizar las operaciones sobre el certificado
 * @author crubencvs
 *
 */
public class Certificado {

	private static final String ESTRUCTURA = "ESUN_ESTRUCTURA_UNIVERSAL";
	private Preferencias pref;
	private ILog log;
	private String idLlamada;
	/**
	 * Impedimos la implementación sin datos de llamada
	 */
	@SuppressWarnings("unused")
	private Certificado(){
		
	}
	/**
	 * Se ha de utilizar este constructor
	 * @param pref Preferencias de la llamada
	 * @param logger Objeto de log de la llamada
	 * @param idLlamada Identificador de la llamada
	 */
	public Certificado(Preferencias pref, ILog logger, String idLlamada){
		this.pref= pref;
		this.idLlamada=idLlamada;
		this.log= logger;
		
	}
	/**
	 * Devuelve los datos de un certificado tal como los necesita la salida del servicio
	 * @param certificado Certificado en base64
	 * @return Datos del certificado tal como necesita la interfaz de servicio
	 */
	public ResultadoType getDatosCertificado(String certificado) throws CertificadoException{
		ResultadoType ct= new ResultadoType();
		DatosCertificado d= getDatosLocalesCertificado(certificado);
		if (d.isValido()) {
			ct.setIdFiscal(d.getIdentificadorFiscal());
			ct.setNombre(d.getNombre());
			ct.setRazonSocial(d.getRazonSocial());
			ct.setApellido1(d.getApellido1());
			ct.setApellido2(d.getApellido2());
			ct.setEsValido(true);
			if (DatosCertificado.FISICO.equals(d.getTipo())){
				ct.setTipo("F");
			} else  {
				ct.setTipo("J");
			}
		} else {
			ct.setEsValido(false);
			ct.setIdFiscal("");
			ct.setNombre("");
			ct.setApellido1("");
			ct.setApellido2("");
			ct.setRazonSocial("");
		}
		return ct;
	}
	/**
	 * Calcula la fecha de comprobación para el certificado
	 * @param fechaUltimaComprobacion Fecha en la que se comprobó por última vez
	 * @param periodicidad Periodicidad
	 * @return Fecha de nueva comprobación
	 */
	private Calendar calculaFechaComprobacion (Calendar fechaUltimaComprobacion, int periodicidad){
		Calendar cd= Calendar.getInstance(Constantes.local);
		cd.setTime(fechaUltimaComprobacion.getTime());
		cd.add(Calendar.DAY_OF_YEAR, periodicidad);
		return cd;
	}
	/**
	 * Indica si la fecha indicada ha vencido
	 * @param fecha
	 * @return  
	 */
	private boolean fechaVencida (Calendar fecha) {
		Calendar now= Calendar.getInstance(Constantes.local);
		if (now.compareTo(fecha)>=0) {
			return true;
		}
		return false;
	}
	/**
	 * Recupera los datos del certificado almacenados de manera local
	 * @param certificado Certificado en base64
	 * @return datos del certificado
	 */
	private DatosCertificado getDatosLocalesCertificado(String certificado) throws CertificadoException{
		try{
			DatosCertificado dc= new DatosCertificado();
			log.info("Comprobamos si existe el certificado en base de datos.");
			TLanzador l = LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(idLlamada));
			ProcedimientoAlmacenado proc= new ProcedimientoAlmacenado(pref.getProcAlmacenadoDatosCert(), pref.getEsquemaBD());
			proc.param(certificado,ParamType.CADENA);
			proc.param("P", ParamType.CADENA);
			RespuestaLanzador re= new RespuestaLanzador(l.ejecutar(proc));
			if (!re.esErronea()){
				
				String c= re.getValue("CADE_CADENA", 1, "STRING_CADE");
				//Hay certificado, y parece correcto
				if ("00".equals(c)){
					dc.setIdentificadorFiscal(re.getValue(ESTRUCTURA, 1, "C1"));
					dc.setFechaUltimaComprobacion(re.getValue(ESTRUCTURA, 1, "C3"));
					dc.setFechaCaducidad(re.getValue(ESTRUCTURA, 1, "C2"));
					dc.setNombre(re.getValue(ESTRUCTURA, 1, "C4"));
					dc.setApellido1(re.getValue(ESTRUCTURA, 1, "C5"));
					dc.setApellido2(re.getValue(ESTRUCTURA, 1, "C6"));
					dc.setRazonSocial(re.getValue(ESTRUCTURA, 1, "C7"));
					dc.setTipo(re.getValue(ESTRUCTURA, 1, "C8"));
					dc.setEsValido(re.getValue(ESTRUCTURA, 1, "C9").equals("S")?true:false);
					//CRUBENCVS 47535 Se incluye la periodicidad
					dc.setPeriodicidadComprobacion(re.getValue(ESTRUCTURA, 1, "C10"));
					if (dc.isValido()){
						log.info("Existe y es válido, comprobamos la fecha de vencimiento");
						//Fecha de comprobación, para recuperar los datos en su caso.
						//CRUBENCVS 47535. Si desde base de datos se envía una periodicidad
						//es la que se toma en consideración. Si es cero, se da por buena, significa
						// que no se quiere comprobar la validez.
						String periodicidad;
						if (dc.getPeriodicidadComprobacion()!= null && !"".equals(dc.getPeriodicidadComprobacion().trim())){
							periodicidad= dc.getPeriodicidadComprobacion();
						} else {
							periodicidad= pref.getPeriodicidad();
						}
						if (!"0".equals(periodicidad)){
							Calendar fechaComprobacion = calculaFechaComprobacion(dc.getFechaUltimaComprobacion(),Integer.valueOf(periodicidad));
							if (fechaVencida(fechaComprobacion)){
								log.info("Fecha vencida, vamos a @firma a por sus datos");
								//Vamos a traer los datos reales
								return sincronizarConAFirma(certificado, true);
							}
							else { //La fecha no está vencida, por tanto devolvemos los datos de la base
								log.info("Certificado en base de datos válido y no vencido");
								dc.setEsValido(true);
								return dc;
							}
						} else {
							log.info("Certificado en base de datos válido y con comprobación de validez desactivada (periodicidad 0)");
							dc.setEsValido(true);
							return dc;
						}
						//FIN CRUBENCVS 47535
					} else //Los datos de base no son válidos, tenemos que ir a @firma
					{
						log.info("El certificado en base de datos tiene estado \"inválido\", se va a @firma");
						return sincronizarConAFirma(certificado, true);
					}
				} else if ("03".equals(c)) {
					//Caducado
					//No válido
					log.info("Certificado caducado.");
					dc= new DatosCertificado();
					dc.setEsValido(false);
					return dc;
					
				} else if ("01".equals(c)) {
					log.info("No hay datos de certificado, se va a @firma");
					//No se ha encontrado certificado, vamos a remoto
					return sincronizarConAFirma(certificado, false);
				} else {
					//No sabemos tratarlo
					throw new CertificadoException ("Estado de certificado en la base de datos desconocido");
				}
			}
			else {
				throw new CertificadoException ("Error al recuperar datos del certificado en la base");
			}
		} catch (LanzadorException le){
			throw new CertificadoException ("Error recuperando datos del certificado: "  + le.getMessage(),le);
		}
		catch (ParseException p)  {
			throw new CertificadoException ("Error al interpretar fechas:" + p.getMessage(), p);
		}  
		 catch (NumberFormatException ne) {
			throw new CertificadoException("Error en la periodicidad de comprobación:"+ ne.getMessage(),ne);
		}
	}
	
	/**
	 * Devuelve los datos de certificado consultando el servicio remoto
	 * @param certificado Certificado a consultar
	 * @return Resultado de la consulta
	 * @throws SeguridadException
	 */
	private ResultadoAutenticacion getDatosAFirma(String certificado) throws SeguridadException{
		AutenticacionCertificado aut= new AutenticacionCertificado(pref.getEndpointAutenticacion(), new SoapClientHandler(idLlamada));
		return aut.comprobarCertificado(certificado);
	}
	/**
	 * Sincroniza el estado del certificado con @firma. Esto significa consultar, actualizar el estado en base
	 * de datos y traer los datos
	 * @param certificado Certificado
	 * @param actualizar Indica si se actualizará el estado en base de datos
	 * @return Datos del certificado
	 * @throws CertificadoException
	 */
	private DatosCertificado sincronizarConAFirma(String certificado, boolean actualizar) throws CertificadoException{
		try{
			ResultadoAutenticacion resAut= getDatosAFirma(certificado);
			if (resAut.esValido()){
				if (actualizar) {
					if (actualizarEstadoCertificado(certificado, resAut.esValido())) {
						return convertirRespuestaAFirma(resAut);
					} else {
						throw new CertificadoException ("Error al actualizar validez de certificado. No se ha podido actualizar. Revise log de cliente para ver más errores");
					}
				}
				else {
					return convertirRespuestaAFirma(resAut);
				}
			}
			else { //Según el servicio remoto, el certificado no es válido
				DatosCertificado dc= new DatosCertificado();
				dc.setEsValido(false);
				return dc;
			}
		}catch (SeguridadException se){
			throw new CertificadoException ("Error al comprobar el certificado en @firma:"+ se.getMessage(),se);
		} catch (LanzadorException le){
			throw new CertificadoException ("Error al actualizar el estado de certificado en base de datos:"+ le.getMessage(),le);
		}
	}
	/**
	 * Convierte la respuesta remota a una respuesta que pueda entender la clase
	 * @param res
	 * @return
	 */
	private DatosCertificado convertirRespuestaAFirma(ResultadoAutenticacion res) {
		DatosCertificado dc= new DatosCertificado();
		if ("JURIDICO".equals(res.getTipo())) {
			dc.setIdentificadorFiscal(res.getCif());
			dc.setTipo(DatosCertificado.JURIDICO);
		}else {
			dc.setIdentificadorFiscal(res.getNifnie());
			dc.setTipo(DatosCertificado.FISICO);
		}
		dc.setNombre(res.getNombre());
		dc.setApellido1(res.getApellido1());
		dc.setApellido2(res.getApellido2());
		dc.setRazonSocial(res.getRazonSocial());
		dc.setEsValido(true);
		return dc;
	}
	
	/**
	 * Actualiza el estado de certificado, para indicar si es válido o no
	 * @param certificado certificado a actualizar
	 * @param valido indica si el estado es válido
	 * @return true si ha podido actualizar, false si no
	 */
	private boolean actualizarEstadoCertificado (String certificado, boolean valido) throws LanzadorException{
		TLanzador l= LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(idLlamada));
		ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado(pref.getProcAlmacenadoActualizaValidezCert(), pref.getEsquemaBD());
		proc.param(certificado,ParamType.CADENA);
		if (valido){
			proc.param("S", ParamType.CADENA);
		}
		else {
			proc.param("N", ParamType.CADENA);
		}
		RespuestaLanzador re= new RespuestaLanzador(l.ejecutar(proc));
		//No nos importa el resultado, solo que sea válido
		return !re.esErronea();
	}
	
}
