package es.tributasenasturias.documentopagoutils;

public class SelectorCartaPago {

	public static ProcesadorCartaPagoInterface seleccionarCartaPago (String idEper, String tipoNoti, String comprimido, String origen)
	{
		boolean discriminador = Integer.parseInt(idEper)>0;
		if (discriminador)
		{
			return new ProcesadorCartaPagoIndividual();
		}
		else 
		{
			return new ProcesadorCartaPagoReferenciaGrupo ();
		}
	}
}
