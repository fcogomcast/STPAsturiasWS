package es.stpa.notificagestionenvios;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;

import es.stpa.notificagestionenvios.MediadorNotifica.RespuestaAlta;
import es.stpa.notificagestionenvios.MediadorNotifica.IdentificadoresRemesa;
import es.stpa.notificagestionenvios.docs.MediadorArchivoDigital;
import es.stpa.notificagestionenvios.exceptions.AltaRemesaException;
import es.stpa.notificagestionenvios.preferencias.Preferencias;
import es.tributasenasturias.log.ILog;

/**
 * Implementación de la operación de alta de remesa de envío
 * @author crubencvs
 *
 */
public class AltaRemesaEnviosImpl {

	private Preferencias pref;
	private String idLlamada;
	private ILog log;
	
	public AltaRemesaEnviosImpl(Preferencias pref, ILog logger, String idLlamada){
		this.log=logger;
		this.pref= pref;
		this.idLlamada= idLlamada;
	}
	
	public RespuestaAltaRemesaEnvios altaRemesaEnvios(
			String codOrganismoEmisor, 
			String idAdar,
			String tipoEnvio, 
			String concepto, 
			String titNif, 
			String titNombre,
			String titApellidos, 
			String titRazonSocial,
			String titTelefono, 
			String titEmail,
			String destNif, 
			String destNombre, 
			String destApellidos,
			String destRazonSocial,
			String destTelefono, 
			String destEmail, 
			String destRefEmisor,
			String destCaducidad, 
			String codSia) throws AltaRemesaException{
		
		log.info("Se recupera el/los documento(s) a enviar");
		byte[] pdf= recuperaDocumentoEnvio(idAdar);
		log.info("Se realiza la llamada a Notifica");
		MediadorNotifica mn= new MediadorNotifica(pref, log, idLlamada);
		RespuestaAlta r= mn.altaRemesaEnvios(
				codOrganismoEmisor, 
				tipoEnvio, 
				concepto, 
				titNif, 
				titNombre, 
				titApellidos, 
				titRazonSocial,
				titTelefono, 
				titEmail, 
				destNif, 
				destNombre, 
				destApellidos, 
				destRazonSocial,
				destTelefono, 
				destEmail, 
				destRefEmisor, 
				destCaducidad, 
				codSia, 
				pdf);
		
		RespuestaAltaRemesaEnvios respuesta= new RespuestaAltaRemesaEnvios();
		if (!r.isEsError()){
			ListaIdentificadoresType l = new ListaIdentificadoresType();
			for (IdentificadoresRemesa id: r.getIdentificadores()){
				IdentificadorType i= new IdentificadorType();
				i.setIdentificador(id.getIdentificador());
				i.setNifTitular(id.getNif());
				l.getIdentificador().add(i);
			}
			respuesta.setListaIdentificadores(l);
		} 
		respuesta.setCodigoRespuesta(r.getCodRespuesta());
		respuesta.setDescripcionRespuesta(r.getDescripcionRespuesta());
		return respuesta;
	}
	/**
	 * Recupera la lista de los id adar pasados, considerando que pueden ser varios
	 * separados por comas
	 * @param idAdar Cadena con un id_adar o varios, separados por comas.
	 * @return
	 * @throws AltaRemesaException
	 */
	private String[] getListaIdAdar(String idAdar) throws AltaRemesaException{
		if (idAdar==null || "".equals(idAdar)){
			throw new AltaRemesaException("Parámetro IdAdar vacío");
		}
		return idAdar.split(",");
		
	}
	
	/**
	 * Une varios pdfs en uno. 
	 * @param pdfs Lista con los contenidos de los pdf como array de bytes
	 * @param dest {@link OutputStream} donde se dejará el 
	 * @throws AltaRemesaException
	 */
	private void unirPdfs(List<byte[]> pdfs, OutputStream dest) throws AltaRemesaException{
		if (pdfs==null || pdfs.size()==0){
			throw new AltaRemesaException("Fallo en "+AltaRemesaEnviosImpl.class.getName()+".unirPdfs. No se ha recibido contenido de los pdfs");
		}
		if (dest==null){
			throw new AltaRemesaException("Fallo en "+AltaRemesaEnviosImpl.class.getName()+".unirPdfs. No se ha recibido destino de la copia");
		}
		try {
			Document doc = new Document();
			PdfCopy copy = new PdfCopy(doc, dest);
			doc.open();
			for (byte[] pdf: pdfs){
				PdfReader reader= new PdfReader(pdf);
				for (int i= 0;i<reader.getNumberOfPages();i++){
					copy.addPage(copy.getImportedPage(reader, (i+1)));
				}
				copy.freeReader(reader);
				reader.close();
			}
			doc.close();
		} catch (Exception e){
			throw new AltaRemesaException ("Error al unir Pdfs:"+e.getMessage(),e);
		}
		
	}
	/**
	 * Recupera el documento a enviar. Si se trata de varios Pdfs, se unen todos en uno.
	 * @param idAdar Identificador o identificadores de los archivos a enviar. Si son varios, estarán separados por comas
	 * @return Contenido del pdf a enviar, bien porque era uno o  porque se han unido varios.
	 * @throws AltaRemesaException
	 */
	private byte[] recuperaDocumentoEnvio(String idAdar) throws AltaRemesaException{
		try {
			String[] idArchivos= getListaIdAdar(idAdar);
			MediadorArchivoDigital md = new MediadorArchivoDigital(this.idLlamada);
			List<byte[]> contenidoPdfs= new ArrayList<byte[]>();
			ByteArrayOutputStream destPdf= new ByteArrayOutputStream();
			for (int i=0;i<idArchivos.length;i++){
				contenidoPdfs.add(md.obtenerDocumentoPorId(idArchivos[i]));
			}
			if (contenidoPdfs.size()>1) {
				log.debug("Número de archivos a enviar/unir:"+contenidoPdfs.size());
				//Por el momento lo guardo en memoria. Si vemos que es demasiado,
				//se deberá guardar en disco.
				unirPdfs(contenidoPdfs, destPdf);
			} else if (contenidoPdfs.size()==1){
				int leido=0;
				byte[] buffer= new byte[8192];
				ByteArrayInputStream bais = new ByteArrayInputStream(contenidoPdfs.get(0));
				while ((leido=bais.read(buffer))!= -1){
					destPdf.write(buffer,0, leido);
				}
			} else {
				throw new Exception ("Error, no se ha podido recuperar el contenido de ningún documento de la lista:"+ idAdar);
			}
			return destPdf.toByteArray();
		}catch (Exception e){
			throw new AltaRemesaException("Error al recuperar el documento de envío:"+e.getMessage(),e);
		}
		
	}
	
	
	
}
