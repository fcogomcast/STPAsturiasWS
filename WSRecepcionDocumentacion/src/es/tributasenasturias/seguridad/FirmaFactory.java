package es.tributasenasturias.seguridad;

import es.tributasenasturias.excepciones.RecepcionDocumentosException;


/**
 * Instancia clases en relaci�n con la firma
 * @author crubencvs
 *
 */
public class FirmaFactory {
	/**
	 * Instancia la clase de gesti�n de firma.
	 * @return Instancia de la clase de gesti�n de firma FirmaHelper
	 * @throws RecepcionDocumentosException En caso de no poder instanciar.
	 */
	public FirmaHelper newFirmaHelper() throws RecepcionDocumentosException{
		FirmaHelper firma;
		try{
			firma=new FirmaHelper();
		}catch (Exception ex){
			throw new RecepcionDocumentosException (this.getClass().getName()+"::Error al instanciar el objeto de firma:"+ ex.getMessage(),ex);
		}
		return firma;
	}
}
