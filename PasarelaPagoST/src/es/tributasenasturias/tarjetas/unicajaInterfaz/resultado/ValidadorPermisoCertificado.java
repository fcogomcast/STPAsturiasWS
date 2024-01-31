package es.tributasenasturias.tarjetas.unicajaInterfaz.resultado;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.seguridad.servicio.InfoCertificado;
import es.tributasenasturias.seguridad.servicio.PropertyConfigurator;
import es.tributasenasturias.seguridad.servicio.SeguridadException;
import es.tributasenasturias.seguridad.servicio.SeguridadFactory;
import es.tributasenasturias.seguridad.servicio.VerificadorCertificado;
import es.tributasenasturias.seguridad.servicio.VerificadorPermisoServicio;
import es.tributasenasturias.utils.Preferencias;

public class ValidadorPermisoCertificado {

	private Preferencias pref;
	private String idSesion;
	public ValidadorPermisoCertificado(Preferencias pref, String idSesion){
		this.pref= pref;
		this.idSesion= idSesion;
	}
	/**
	 * Comprueba si un certificado concreto tiene establecido un permiso
	 * @param certificado Certificado en base64
	 * @param permiso Permiso a comprobar. Es el PESE_PERMISOS_SERVICOSWEB.COD_PERMISO
	 * @return true si tiene permiso, false si no
	 * @throws PasarelaPagoException
	 */
	public boolean tienePermiso(String certificado, String permiso) throws PasarelaPagoException{
		try {
			VerificadorCertificado ver = SeguridadFactory.newVerificadorCertificado(pref.getEndpointAutenticacionCert() , new es.tributasenasturias.webservices.soap.LogMessageHandlerClient(idSesion));
			InfoCertificado infoCert= ver.login(certificado);
			String nif="";
			if (infoCert.getCif()!=null && !"".equals(infoCert.getCif())){
				nif=infoCert.getCif();
			}
			if (infoCert.getNifNie()!=null && !"".equals(infoCert.getNifNie())){
				nif= infoCert.getNifNie();
			}
			VerificadorPermisoServicio per = SeguridadFactory
			.newVerificadorPermisoServicio(new PropertyConfigurator(
						"", pref.getEndPointLanzador(), 
						pref.getPAPermisoServicio(), pref.getEsquemaBaseDatos()));
			return per.tienePermisosCIF(nif, permiso);
		} catch (SeguridadException s){
			throw new PasarelaPagoException("Error al comprobar el permiso del certificado:" + s.getMessage(), s);
		}
	}
}
