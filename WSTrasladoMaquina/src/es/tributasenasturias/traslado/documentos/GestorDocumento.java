package es.tributasenasturias.traslado.documentos;

import es.tributasenasturias.traslado.exceptions.DocumentoException;
import es.tributasenasturias.traslado.preferencias.Preferencias;
import es.tributasenasturias.traslado.util.Base64;

/**
 * Gestiona las operaciones sobre el documento de emisión.
 * @author crubencvs
 *
 */
public class GestorDocumento {

	private static final String TIPO_DOCUMENTO = "J";
	private String idReimprimible;
	private String tipoReimprimible;
	private Preferencias pref;
	private String idSesion;
	private String autoliquidacion;
	private String hashVerificacion;
	private String codigoVerificacion;
	public GestorDocumento(String idReimprimible, String tipoReimprimible, String autoliquidacion, String hashVerificacion,Preferencias p, String idSesion)
	{
		this.idReimprimible= idReimprimible;
		this.tipoReimprimible= tipoReimprimible;
		this.hashVerificacion= hashVerificacion;
		this.pref= p;
		this.idSesion= idSesion;
		this.autoliquidacion= autoliquidacion;
		this.codigoVerificacion= TIPO_DOCUMENTO+autoliquidacion+"-"+hashVerificacion;
	}
	/**
	 * Reimprime el documento
	 * @return Contenido del documento, expresado en base64
	 * @throws DocumentoException
	 */
	public String reimprimirDocumento() throws DocumentoException{
		ReimpresionDocumento r = new ReimpresionDocumento(this.pref, this.idSesion);
		String b64d=r.getReimpresion(this.idReimprimible, this.tipoReimprimible, this.codigoVerificacion);
		return b64d;
	}
	public String recuperarDocumento() throws DocumentoException {
		DocumentoDoin doin = new DocumentoDoin (this.pref, this.idSesion);
		String resultado= doin.getDocumentoDoin(autoliquidacion, TIPO_DOCUMENTO, true);
		if (!"0000".equals(resultado)){
			throw new DocumentoException ("Error, no se ha podido recuperar el documento");
		}
		return doin.getDocumento();
		
	}
	/**
	 * Guarda el documento en base de datos
	 * @param doc Documento PDF en base64
	 * @param nifSujetoPasivo Nif del sujeto pasivo de la autoliquidación  
	 * @param nifPresentador Nif del presentador
	 * @throws DocumentoException Si no se puede dar de alta
	 */
	public void guardarDocumento(String doc, String nifSujetoPasivo, String nifPresentador) throws DocumentoException
	{
		DocumentoDoin doin= new DocumentoDoin(this.pref,this.idSesion);
		String resultado=doin.altaDocumento(autoliquidacion, TIPO_DOCUMENTO, this.hashVerificacion,nifSujetoPasivo, nifPresentador, doc);
		if (!"0000".equals(resultado)) {
			throw new DocumentoException("Error, no se ha podido terminar el alta de documento");
		}
	}
	/**
	 * Recupera el contenido binario del documento Base64
	 * @param base64
	 * @return
	 */
	public byte[] recuperarBinario(String base64){
		return Base64.decode(base64.toCharArray());
	}
	  
}
