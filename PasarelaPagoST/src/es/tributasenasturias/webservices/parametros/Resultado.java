package es.tributasenasturias.webservices.parametros;

import es.tributasenasturias.webservices.parametros.componentes.EntradaConsulta;
import es.tributasenasturias.webservices.parametros.componentes.SalidaConsulta;

public class Resultado {
	protected EntradaConsulta peticion;
	protected SalidaConsulta respuesta;
	protected String mac;

	public Resultado() {
		peticion = new EntradaConsulta();
		respuesta = new SalidaConsulta();
	}
	public EntradaConsulta getPeticion() {
		return peticion;
	}
	public void setPeticion(EntradaConsulta peticion) {
		this.peticion = peticion;
	}
	public SalidaConsulta getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(SalidaConsulta respuesta) {
		this.respuesta = respuesta;
	}
	public String getMac() {
		return mac;
	}

	/**
	 * Metodo que establece una MAC de los parámetros que se devuelven en la respuesta. Cada cliente tendrá una codificación diferente.
	 * @param cliente contra el que se genera la MAC. 
	 * 
	 */
	public void setMac(String cliente) throws Exception
	{
		String datos="";
		//Se genera la MAC en función de los parámetros de resultado. 
		datos= peticion.getCliente() + peticion.getEmisora()+ peticion.getModelo() +
			  peticion.getNumero_autoliquidacion() + peticion.getNif() + peticion.getNombreContribuyente()+
			  peticion.getFecha_devengo() + peticion.getDato_especifico()+ 
			   peticion.getExpediente() + peticion.getImporte() + 
			   peticion.getTarjeta() + peticion.getFecha_caducidad() + 
			  peticion.getNif_operante() + peticion.getAplicacion() +
			  peticion.getNumero_unico() + peticion.getNumero_peticion() + 
			  peticion.getLibre() + respuesta.getError() +
			  respuesta.getResultado()+ respuesta.getOperacion();
		this.mac= es.tributasenasturias.utils.GenerateMac.calculateHex(datos, cliente);
	}
	
	/**
	 * Establece el valor de la MAC a un literal que se la pasa, para poder generar la MAC externamente
	 * @param mac MAC generada
	 */
	public void setMacLiteral(String mac){
		this.mac= mac;
	}
}
