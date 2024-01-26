package es.tributasenasturias.servicios.RecepcionDocumentos;

import es.tributasenasturias.excepciones.RecepcionDocumentosException;
import es.tributasenasturias.servicios.RecepcionDocumentos.procesadores.ProcesadorDocumentos;

/**
 * Recibe la lista de documentos y los procesa, según su tipo.
 * @author crubencvs
 *
 */
public class ReceptorLista {

	public ResultadoTypeOut procesaLista(ListaDocumentosType lista)
	{
		try
		{
			//Procesamos documentos.
			ProcesadorDocumentos prd= new ProcesadorDocumentos();
			return prd.procesar(lista);
		}
		catch (RecepcionDocumentosException e)
		{
			return new GeneradorResultado().generaResultadoErrorGenerico();
		}
	}
}
