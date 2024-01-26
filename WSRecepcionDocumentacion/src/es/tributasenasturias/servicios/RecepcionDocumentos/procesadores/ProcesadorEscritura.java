package es.tributasenasturias.servicios.RecepcionDocumentos.procesadores;


import es.tributasenasturias.bd.AltaEscritura;
import es.tributasenasturias.excepciones.RecepcionDocumentosException;
import es.tributasenasturias.servicios.RecepcionDocumentos.EscrituraType;

/**
 * Procesa una escritura.
 * @author crubencvs
 *
 */
public class ProcesadorEscritura {

	public enum CodigosInfo {OK,DUPLICADO_POR_NOTARIO, DUPLICADO_POR_GESTOR, ERROR_DATOS, ERROR, ESCRITURA_NULA};
	/**
	 * Información del proceso de la escritura.
	 * @author crubencvs
	 *
	 */
	public static class InfoProceso
	{
		private CodigosInfo codigo;
		private String descripcion;
		public CodigosInfo getCodigo() {
			return codigo;
		}
		public void setCodigo(CodigosInfo codigo) {
			this.codigo = codigo;
		}
		public String getDescripcion() {
			return descripcion;
		}
		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
	}
	/**
	 * Procesa la escritura. En nuestro caso significa darla de alta.
	 * @param escritura Datos de la escritura tal como llegan en el mensaje.
	 * @throws RecepcionDocumentosException
	 */
	public InfoProceso procesar(EscrituraType escritura) throws RecepcionDocumentosException
	{
		InfoProceso info=new InfoProceso();
		if (escritura==null)
		{
			info.setCodigo(CodigosInfo.ESCRITURA_NULA);
			info.setDescripcion("La escritura recibida está vacía.");
			return info;
		}
		
		AltaEscritura alta= new AltaEscritura();
		alta.realizaAlta(escritura.getCodNotario(), escritura.getCodNotaria(), escritura.getNumProtocolo(), escritura.getNumProtocoloBis(), escritura.getFechaAutorizacion(), escritura.getDocumentoEscritura(), escritura.getFirmaEscritura());
		switch(alta.getEstadoAlta()){
		case OK:
		case DUPLICADO_POR_GESTOR:
		{
			info.setCodigo(CodigosInfo.OK);
			break;
		}
		case DUPLICADO_POR_NOTARIO:
		{
			info.setCodigo(CodigosInfo.DUPLICADO_POR_NOTARIO);
			break;
		}
		case ERROR_DATOS:
		{
			info.setCodigo(CodigosInfo.ERROR_DATOS);
			info.setDescripcion(alta.getDescEstado());
			break;
		}
		case ERROR:
		{
			info.setCodigo(CodigosInfo.ERROR);
			info.setDescripcion(alta.getDescEstado());
			break;
		}
		default:
		{
			info.setCodigo(CodigosInfo.ERROR);
			info.setDescripcion(alta.getDescEstado());
			break;
		}
		}
		return info;
	}
}
