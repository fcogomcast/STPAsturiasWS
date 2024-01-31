package es.tributasenasturias.validacion;

import es.tributasenasturias.utils.Mensajes;

public interface CodigosResultadoValidacion {
	String ORIGEN_INVALIDO=Mensajes.getOrigenInvalido();
	String ORIGEN_VACIO=Mensajes.getOrigenVacio();
	String MODALIDAD_VACIA = Mensajes.getModalidadVacia();
	String MODALIDAD_ERRONEA = Mensajes.getErrorModalidadErronea();
	String MODALIDAD_AUTOLIQ_SW = Mensajes.getErrorModalidadSW();
	String JUSTIFICANTE_VACIO = Mensajes.getJustificanteVacio();
	String IDENTIF_REFER_VACIO = Mensajes.getIdenRefVacio();
	String CLIENTE_VACIO = Mensajes.getClienteVacio();
	String FECHA_DEVENGO_ESPECIFICO_VACIO = Mensajes.getErrorDevengoEspecificoVacio();
	String MODELO_NO_046 = Mensajes.getErrorModelo046();
	String MAC_VACIA = Mensajes.getErrorMacVacia();
	String APLICACION_NUMERO_UNICO_VACIO = Mensajes.getAplicacionNumVacio();
	String DATOS_TARJETA_INCOMPLETOS = Mensajes.getErrorTarjetaFechaVacia();
	String DATOS_PAGO_INCOMPLETOS = Mensajes.getErrorTarjetaCCC();
	String APLICACION_NUM_UNICO_INNECESARIO = Mensajes.getAplNumUnicoInnecesario();
	String JUSTIFICANTE_INNECESARIO = Mensajes.getJustificanteInnecesario();
	String FECHA_DEVENGO_ESPECIFICO_INNECESARIO = Mensajes.getDevengoEspecificoInnecesario();
	String EMISORA_VACIA = Mensajes.getErrorEmisoraVacia();
	String NIF_OPERANTE_VACIO = Mensajes.getErrorNifOperanteVacio();
	String NIF_O_NOMBRE = Mensajes.getErrorNifNombre();
	String CCC_INNECESARIO = Mensajes.getErrorCccSw();
	String IMPORTE_ERRONEO= Mensajes.getErrorImporte();
	String MAC_INVALIDO = Mensajes.getFatalError();
	String NIF_INVALIDO = Mensajes.getNIFInvalido();
	String ENTIDAD_VACIA= Mensajes.getEntidadVacia();
	String ERROR_CRITERIO_CONSULTA= Mensajes.getErrorCriterioConsulta();
	String ERROR_TARJETA_PAGO= Mensajes.getErrorTarjetaPago();
	String ERROR_FORMATO_FECHA_DEVENGO=Mensajes.getErrorFormatoFechaDevengo();
}
