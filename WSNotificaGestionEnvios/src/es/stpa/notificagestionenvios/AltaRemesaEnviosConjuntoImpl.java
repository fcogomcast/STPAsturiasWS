package es.stpa.notificagestionenvios;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


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
public class AltaRemesaEnviosConjuntoImpl {

	private Preferencias pref;
	private String idLlamada;
	private ILog log;
	
	public AltaRemesaEnviosConjuntoImpl(Preferencias pref, ILog logger, String idLlamada){
		this.log=logger;
		this.pref= pref;
		this.idLlamada= idLlamada;
	}
	/**
	 * Datos de cada entrada del archivo Zip
	 * @author crubencvs
	 *
	 */
	private static class EntradaZip{
		private String idAdar;
		private byte[] contenido;
		private String nombreEntrada;
		public String getIdAdar() {
			return idAdar;
		}
		public void setIdAdar(String idAdar) {
			this.idAdar = idAdar;
		}
		public byte[] getContenido() {
			return contenido;
		}
		public void setContenido(byte[] contenido) {
			this.contenido = contenido;
		}
		public String getNombreEntrada() {
			return nombreEntrada;
		}
		public void setNombreEntrada(String nombreEntrada) {
			this.nombreEntrada = nombreEntrada;
		}
	}
	
	
	public RespuestaAltaRemesaEnvios altaRemesaEnviosConjunto(
			String codOrganismoEmisor, 
			ArchivosType archivos,
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
		
		log.info("Se recuperan los documentos a enviar");
		byte[] zip= generarZipEnvio(archivos);
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
				zip);
		
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
	 * Genera un zip con todas las entradas del envío.
	 * @param archivos Lista de archivos (Id_Adar y nombre del archivo en el ZIP) a enviar
	 * @return Contenido en bytes del zip
	 * @throws AltaRemesaException
	 */
	private byte[] generarZipEnvio(ArchivosType archivos) throws AltaRemesaException{
		if (archivos==null || archivos.getArchivo().size()==0){
			throw new AltaRemesaException ("No se han recibido archivos a incluir en el envío");
		}
		ZipOutputStream zip=null;
		ByteArrayOutputStream bytesZip= new ByteArrayOutputStream();
		try {
			List<EntradaZip> entradasZip= new ArrayList<EntradaZip>();
			
			MediadorArchivoDigital md = new MediadorArchivoDigital(this.idLlamada);
			for (ArchivoType archivo: archivos.getArchivo()){
				if (archivo.getNombreFichero()==null || "".equals(archivo.getNombreFichero())){
					throw new AltaRemesaException ("El fichero con id_adar " + archivo.getIdAdar()+ " no tiene nombre de fichero ");
				}
				EntradaZip entradaZip= new EntradaZip();
				entradaZip.setIdAdar(archivo.getIdAdar());
				entradaZip.setNombreEntrada(archivo.getNombreFichero());
				entradaZip.setContenido(md.obtenerDocumentoPorId(archivo.getIdAdar()));
				entradasZip.add(entradaZip);
			}
			zip = new ZipOutputStream(bytesZip);
			for (EntradaZip entrada: entradasZip){
				zip.putNextEntry(new ZipEntry(entrada.getNombreEntrada()));
				zip.write(entrada.getContenido());
				zip.closeEntry();
			}
		}  catch (Exception e){
			throw new AltaRemesaException("Error al generar el zip con los documentos del envío:"+e.getMessage(),e);
		} finally {
			if (zip!=null){
				try{ zip.close();} catch(Exception ex){}
			}
		}
		return bytesZip.toByteArray();
	}
	
	
}
