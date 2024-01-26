package es.tributasenasturias.servicios.RecepcionDocumentos.procesadores;

import es.tributasenasturias.excepciones.RecepcionDocumentosException;
import es.tributasenasturias.servicios.RecepcionDocumentos.EscrituraType;
import es.tributasenasturias.servicios.RecepcionDocumentos.GeneradorResultado;
import es.tributasenasturias.servicios.RecepcionDocumentos.ListaDocumentosType;
import es.tributasenasturias.servicios.RecepcionDocumentos.ResultadoTypeOut;

/**
 * Realiza el procesamiento de cada uno de los documentos, enviando la lista de documentos a trav�s de 
 * una serie de procesadores espec�ficos.
 * En este momento, lo �nico que hace es llamar directamente al procesador de escritura, pero 
 * si se a�aden documentos se podr�a mejorar.
 * @author crubencvs
 *
 */
public class ProcesadorDocumentos {

	/**
	 * Procesa la lista de documentos. En el momento de crear este c�digo s�lo procesa la escritura,
	 * que es el �nico documento que puede llegar.
	 * @param lista Lista de documentos, tal como llega en el mensaje de entrada.
	 * @return {@link ResultadoTypeOut}
	 * @throws RecepcionDocumentosException
	 */
	public ResultadoTypeOut procesar(ListaDocumentosType lista) throws RecepcionDocumentosException
	{
		return procesarEscritura(lista.getEscritura());
	}
	/**
	 * Procesa el documento de escritura.
	 * @param escritura Datos de la escritura.
	 * @return {@link ResultadoTypeOut}
	 * @throws RecepcionDocumentosException
	 */
	private ResultadoTypeOut procesarEscritura(EscrituraType escritura) throws RecepcionDocumentosException
	{
		ProcesadorEscritura pe= new ProcesadorEscritura();
		ProcesadorEscritura.InfoProceso in=pe.procesar(escritura);
		GeneradorResultado gn = new GeneradorResultado();
		return gn.generaResultadoEscritura(in);
	}
	
}
