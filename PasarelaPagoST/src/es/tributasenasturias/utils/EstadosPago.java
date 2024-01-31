package es.tributasenasturias.utils;

/**
 * INICIADO -> Iniciado
 * PAGADO -> Pagado
 * ERROR -> Error
 * DIFERENTE -> Diferente (se obtiene este resultado de PeticionPate cuando este PL encuentra que los datos recibidos son difernetes que los de bbdd)
 * ANULADO -> Anulado 
 * REGISTRADO -> Registrado. Este estado no aparece en datos, se utiliza para marcar que el registro ha sido dado de alta.
 * ANULACION_COMENZADA -> Tratamiento. Este estado marca que se ha comenzado la anulación del registro, para que si esta falla la siguiente operación lo tenga en cuenta.
 * NO_PAGADO -> No se utiliza en Pate, marca que no está pagado.
 */
public enum EstadosPago {
	INICIADO("I"),PAGADO("P"),ERROR("E"),DIFERENTE("D"),ANULADO("A"),REGISTRADO("R"),ANULACION_COMENZADA("T"),NO_PAGADO("NP"), GENERADO_HASH("G");
	private String valor;
		private EstadosPago(String valor)
		{
			this.valor = valor;
		}
		public String getValor()
		{
			return this.valor; 
		}
};