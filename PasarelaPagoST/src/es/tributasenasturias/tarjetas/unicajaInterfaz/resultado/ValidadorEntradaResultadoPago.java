package es.tributasenasturias.tarjetas.unicajaInterfaz.resultado;

public class ValidadorEntradaResultadoPago {

	public static class ValidarResultadoPagoException extends Exception{

		private final String errorValidacion;
		/**
		 * 
		 */
		private static final long serialVersionUID = 7917254236068203167L;

		/**
		 * @param errorValidacion
		 */
		public ValidarResultadoPagoException(String errorValidacion) {
			super();
			this.errorValidacion=errorValidacion;
		}

		/**
		 * @return the errorValidacion
		 */
		public final String getErrorValidacion() {
			return errorValidacion;
		}

		
		
	}
	
	private static boolean isNullOrEmpty(String dato){
		return dato==null || "".equals(dato);
	}
	/**
	 * Validación de la recepción
	 * @param request
	 * @throws ValidarResultadoPagoException
	 */
	public static void validar(RecepcionResultadoPagoJsonRequest request) throws ValidarResultadoPagoException{
		//Podría no basar la validación en excepciones, pero ya que se saldrá en el mismo
		//instante, es lo más cómodo.
		
		if (isNullOrEmpty(request.getResultado())){
			throw new ValidarResultadoPagoException ("Campo resultado Obligatorio");
		}
		
		if (isNullOrEmpty(request.getResultadoDescripcion())){
			throw new ValidarResultadoPagoException ("Campo resultadoDescripcion Obligatorio");
		}
		
		if (isNullOrEmpty(request.getNumeroPedido())){
			throw new ValidarResultadoPagoException ("Campo numPedido Obligatorio");
		}
		
		if (isNullOrEmpty(request.getReferencia())){
			throw new ValidarResultadoPagoException ("Campo referencia Obligatorio");
		}
		
		if (isNullOrEmpty(request.getImporte())){
			throw new ValidarResultadoPagoException ("Campo importe Obligatorio");
		}
		
		if (isNullOrEmpty(request.getMoneda())){
			throw new ValidarResultadoPagoException ("Campo moneda Obligatorio");
		}
		
		if (isNullOrEmpty(request.getFechaPago())){
			throw new ValidarResultadoPagoException ("Campo fechaPago Obligatorio");
		}
		
		if (isNullOrEmpty(request.getNrc())){
			throw new ValidarResultadoPagoException ("Campo NRC Obligatorio");
		}
		
		if (isNullOrEmpty(request.getNumeroOperacion())){
			throw new ValidarResultadoPagoException ("Campo numOper Obligatorio");
		}
		
		if (isNullOrEmpty(request.getTarjeta())){
			throw new ValidarResultadoPagoException ("Campo tarjeta Obligatorio");
		}
		
		if (request.getResultado().length()!=4){
			throw new ValidarResultadoPagoException ("El campo resultado no tiene la longitud adecuada");
		}
		
		if (request.getResultadoDescripcion().length()>50){
			throw new ValidarResultadoPagoException ("El campo resultadoDescripcion no tiene la longitud adecuada");
		}
		
		if (request.getNumeroPedido().length()>22){
			throw new ValidarResultadoPagoException ("El campo numeroPedido no tiene la longitud adecuada");
		}
		
		if (request.getReferencia().length() > 13){
			throw new ValidarResultadoPagoException ("El campo referencia no tiene la longitud adecuada");
		}
		
		if (request.getImporte().length()>17){
			throw new ValidarResultadoPagoException ("El campo importe no tiene la longitud adecuada");
		}
		
		if (request.getMoneda().length()!=3){
			throw new ValidarResultadoPagoException ("El campo moneda no tiene la longitud adecuada");
		}
		
		if (request.getFechaPago().length()!=19){
			throw new ValidarResultadoPagoException ("El campo fechaPago no tiene la longitud adecuada");
		}
		
		if (request.getNrc().length()>22){
			throw new ValidarResultadoPagoException ("El campo NRC no tiene la longitud adecuada");
		}
		
		if (request.getNumeroOperacion().length()>22){
			throw new ValidarResultadoPagoException ("El campo numeroOperacion no tiene la longitud adecuada");
		}
		
		//La tarjeta no nos importa

	}
}
