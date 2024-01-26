package es.tributasenasturias.bd;

import java.rmi.RemoteException;
import java.util.Map;

import es.tributasenasturias.excepciones.RecepcionDocumentosException;
import es.tributasenasturias.utils.GestorIdLlamada;

/**
 * Realiza la comunicación con el alta de escritura, indicando cómo ha finalizado.
 * @author crubencvs
 *
 */
public class AltaEscritura {
	/**
	 * Estados de finalización de un alta de escritura.
	 * @author crubencvs
	 *
	 */
	public enum Estados {OK, DUPLICADO_POR_NOTARIO, DUPLICADO_POR_GESTOR, ERROR_DATOS,ERROR};
	private Estados estadoAlta;
	private String descEstado;
	public Estados getEstadoAlta() {
		return estadoAlta;
	}
	public void setEstadoAlta(Estados estadoAlta) {
		this.estadoAlta = estadoAlta;
	}
	public String getDescEstado() {
		return descEstado;
	}
	public void setDescEstado(String descEstado) {
		this.descEstado = descEstado;
	}
	/**
	 * Realiza el alta de una escritura. En finalización, establece el estado, que se podrá consultar en el objeto.
	 * @param codNotario Código de notario de la escritura.
	 * @param codNotaria Código de notaría en la fecha de autorización.
	 * @param numProtocolo Número de protocolo.
	 * @param numProtocoloBis Número de protocolo bis.
	 * @param fechaAutorizacion Fecha de autorización.
	 * @param docEscritura Documento de escritura
	 * @param firmaEscritura Firma de escritura.
	 * @throws RecepcionDocumentosException
	 */
	public void realizaAlta(String codNotario, String codNotaria, String numProtocolo, String numProtocoloBis,
							String fechaAutorizacion, String docEscritura, String firmaEscritura) 
	throws RecepcionDocumentosException
	{
		Datos dat = new Datos(GestorIdLlamada.getIdLlamada());
		try {
			Map<String,String> res=dat.insertarEscritura(codNotario, codNotaria, numProtocolo, numProtocoloBis, fechaAutorizacion, docEscritura, firmaEscritura, "G");
			String estado=res.get(Datos.C_ESTADO_ALTA);
			if ("OK".equalsIgnoreCase(estado))
			{
				this.estadoAlta=Estados.OK;
			}
			else if ("OKYN".equalsIgnoreCase(estado))
			{
				this.estadoAlta=Estados.DUPLICADO_POR_NOTARIO;
			}
			else if ("OKYG".equalsIgnoreCase(estado))
			{
				this.estadoAlta=Estados.DUPLICADO_POR_GESTOR;
			}
			else if ("DATOSKO".equalsIgnoreCase(estado))
			{
				this.estadoAlta=Estados.ERROR_DATOS;
				this.descEstado=res.get(Datos.C_DESC_ALTA);
			}
			else if ("KO".equalsIgnoreCase(estado))
			{
				this.estadoAlta=Estados.ERROR;
				res.get(Datos.C_DESC_ALTA);
				this.descEstado=res.get(Datos.C_DESC_ALTA);
			}
			
		} catch (RemoteException e) {
			throw new RecepcionDocumentosException("Imposible dar de alta la escritura:"+ e.getMessage(),e);
		}
		
	}
}
