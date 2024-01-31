package es.tributasenasturias.webservices.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
	    "peticion",
	    "respuesta",
	    "operaciones",
	    "mac"
	})
public class ResultadoConsultaPagoTarjeta  {
	protected PeticionConsultaPagoTarjeta peticion;
	protected RespuestaConsultaPagoTarjeta respuesta;
	protected HistoricoOperacionesPago operaciones;
	protected String mac;

	public ResultadoConsultaPagoTarjeta() {
		peticion = new PeticionConsultaPagoTarjeta();
		respuesta = new RespuestaConsultaPagoTarjeta();
		operaciones = new HistoricoOperacionesPago();
	}
	public PeticionConsultaPagoTarjeta getPeticion() {
		return peticion;
	}

	public void setPeticion(PeticionConsultaPagoTarjeta peticion) {
		this.peticion = peticion;
	}
	
	public RespuestaConsultaPagoTarjeta getRespuesta() {
		return respuesta;
	}
	
	public void setRespuesta(RespuestaConsultaPagoTarjeta respuesta) {
		this.respuesta = respuesta;
	}
	
	
	public String getMac() {
		return mac;
	}

	/**
	 * Para inicializar basado en una petición previa. 
	 * Copia los campos de petición de la entrada a la salida
	 */
	public ResultadoConsultaPagoTarjeta(PeticionConsultaPagoTarjeta peticion) {
		this.peticion = peticion;
		respuesta = new RespuestaConsultaPagoTarjeta();
		operaciones = new HistoricoOperacionesPago();
	}
	
	
	public void setMac(String mac){
		this.mac= mac;
	}
	
	public final HistoricoOperacionesPago getOperaciones() {
		return operaciones;
	}
	

	public final void setOperaciones(HistoricoOperacionesPago operaciones) {
		this.operaciones = operaciones;
	}
	
	
	
}