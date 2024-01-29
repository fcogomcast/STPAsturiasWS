package es.tributasenasturias.webservices.Certificados.impl;

import es.tributasenasturias.webservices.Certificados.Exceptions.DatosException;
import es.tributasenasturias.webservices.Certificados.General.InfoResultado;
import es.tributasenasturias.webservices.Certificados.SERVICIOWEB.RESPUESTA;
import es.tributasenasturias.webservices.Certificados.bd.Datos;
import es.tributasenasturias.webservices.Certificados.utils.Log.GenericAppLogger;
import es.tributasenasturias.webservices.Certificados.utils.Log.TributasLogger;
import es.tributasenasturias.webservices.Certificados.SERVICIOWEB;
/**Validador de permiso de certificado.
 * 
 * @author CarlosRuben
 *
 */
public class ConsultaCertificadoAction{
	InfoResultado res;
	private RESPUESTA respuesta=null;
	public ConsultaCertificadoAction()
	{
		res = new InfoResultado();
	}
	public InfoResultado getResultado() {
		return res;
	}
	/**
	 * Realiza la consulta de certificados
	 * @param entrada Entrada del servicio web
	 * @param flags Flags que puedan interesar para el proceso de ficheros
	 * @return
	 */
	public boolean doConsulta(SERVICIOWEB entrada, Flags flags) {
		boolean valido=false;
		Datos bd = null;
		java.util.Map<String,String> resBD= null;
		try
		{
			bd=new Datos();
			resBD=bd.consultaCertificado(entrada.getPETICION().getCERTIFICADO().getNIF(),
					entrada.getPETICION().getCERTIFICADO().getNOMBRE(),
					entrada.getPETICION().getCERTIFICADO().getTIPO(), 
					entrada.getPETICION().getCERTIFICADO().getMOTIVO(),
					flags);
			String resultado=resBD.get("RESULTADO");
			String identificacion = resBD.get("IDENTIFICACION");
			String nif= resBD.get("NIF");
			String nombre = resBD.get("NOMBRE");
			String fechaGeneracion= resBD.get("FECHA_GENERACION");
			String fechaValidez=resBD.get("FECHA_VALIDEZ");
			//30183
			if (fechaValidez==null) {
				fechaValidez="";
			}
			//Rellenamos el objeto respuesta, por si el llamador necesita estos resultados.
			respuesta=new RESPUESTA();
			respuesta.setRESULTADO(resultado);
			respuesta.setIDENTIFICACION(identificacion);
			respuesta.setNIF(nif);
			respuesta.setNOMBRE(nombre);
			respuesta.setFECHAVALIDEZ(fechaValidez);
			respuesta.setFECHAGENERACION(fechaGeneracion);
			valido=true;
		}
		catch (DatosException ex)
		{
			res.addMessage("Error inesperado al consultar el permiso en el sistema.");
			GenericAppLogger log = new TributasLogger();
			log.error("Error en la llamada remota a la base de datos:" + ex.getError() + "-" + ex.getMessage());
			log.trace(ex.getStackTrace());
			valido=false;
		}
		return valido;
	}
	public RESPUESTA getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(RESPUESTA respuesta) {
		this.respuesta = respuesta;
	}

}
