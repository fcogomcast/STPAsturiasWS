package es.tributasenasturias.tarjetas.unicajaInterfaz.consultaTarjeta;

public class SalidaOpConsultaPagoUnicaja {
	private final boolean pagado;
	private final String operacionEpst;
	private final int estado;
	private final String numeroOperacion;
	private final String fechaHora;
	private final String nrc;
	private final boolean pagadoSinNrc;
	private final String jsonRecibido;
	/**
	 * @param error
	 * @param operacionEpst
	 * @param estado
	 * @param numeroOperacion
	 * @param fechaHora
	 * @param nrc
	 */
	public SalidaOpConsultaPagoUnicaja(boolean pagado, 
									   String operacionEpst,
									   int estado, 
									   String numeroOperacion, 
									   String fechaHora, 
									   String nrc,
									   String jsonRecibido) {
		this.operacionEpst = operacionEpst;
		this.estado = estado;
		this.numeroOperacion = numeroOperacion;
		this.fechaHora = fechaHora;
		this.nrc = nrc;
		this.jsonRecibido= jsonRecibido;
		//CRUBENCVS Sin número 27/11/2023. 
		//La marca de NRC sólo tiene sentido si indica "pagado", si no, no hace falta.
		//Si no tenemos NRC, independientemente del estado de pago, consideramos no pagado.
		if (this.nrc==null || "".equals(this.nrc.trim())){
			this.pagado=false;
			//this.pagadoSinNrc=true;
			this.pagadoSinNrc=pagado?true:false;
		} else {
			this.pagado = pagado;
			this.pagadoSinNrc=false;
		}
	}
	/**
	 * @return the pagado
	 */
	public final boolean isPagado() {
		return pagado;
	}
	/**
	 * @return the operacionEpst
	 */
	public final String getOperacionEpst() {
		return operacionEpst;
	}
	/**
	 * @return the estado
	 */
	public final int getEstado() {
		return estado;
	}
	/**
	 * @return the numeroOperacion
	 */
	public final String getNumeroOperacion() {
		return numeroOperacion;
	}
	/**
	 * @return the fechaHora
	 */
	public final String getFechaHora() {
		return fechaHora;
	}
	/**
	 * @return the nrc
	 */
	public final String getNrc() {
		return nrc;
	}
	/**
	 * @return the pagadoSinNrc
	 */
	public final boolean isPagadoSinNrc() {
		return pagadoSinNrc;
	}
	/**
	 * @return the jsonRecibido
	 */
	public final String getJsonRecibido() {
		return jsonRecibido;
	}
	
	
	
}
