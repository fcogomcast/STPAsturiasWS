package es.tributasenasturias.servicios.RecepcionDocumentos;

import java.util.HashMap;
import java.util.Map;

import es.tributasenasturias.servicios.RecepcionDocumentos.procesadores.ProcesadorEscritura;
import es.tributasenasturias.utils.Mensajes;

/**
 * Genera el nodo resultado de la respuesta.
 * @author crubencvs
 * TODO: Si se van a procesar en el futuro m�s de un tipo de documento, deber�a modificarse esta 
 * clase para soportar m�s de un tipo de resultado.
 * Se har�a una clase base/interfaz, GeneradorResultado, y clases concretas que implementar�an cada
 * uno de los tipos de resultado.
 * Se pasar�a un par�metro que implementar�a un interfaz, InfoProceso, y generar�a el resultado en
 * funci�n de �l.
 * Ser�a necesaria una clase que decidiera qu� tipo de generador de resultado se utilizar� en funci�n
 * de la informaci�n de proceso (preguntar�a si es una informaci�n de proceso de escritura, o de otro tipo
 * de documento para llamar al generador que necesitara).
 */
public class GeneradorResultado {
	
	private static Map<ProcesadorEscritura.CodigosInfo,String[]> mensajes= new HashMap<ProcesadorEscritura.CodigosInfo, String[]>();
	private final static String MSG_0000="Recepci�n Correcta";
	private final static String MSG_0005="La escritura no se ha dado de alta por existir previamente";
	private final static String MSG_0020="Error en la validaci�n de los datos de la petici�n";
	private final static String MSG_0100="No se ha podido completar la operaci�n debido a un error t�cnico.Int�ntelo en unos minutos o contacte con soporte t�cnico.";
	private final static String MSG_0999="Se ha producido un error grave durante la operaci�n.";
	static
	{
		mensajes.put(ProcesadorEscritura.CodigosInfo.OK, 
							new String[]{"0000","Recepci�n Correcta"});
		mensajes.put(ProcesadorEscritura.CodigosInfo.DUPLICADO_POR_GESTOR, 
				new String[]{"0000",MSG_0000});
		mensajes.put(ProcesadorEscritura.CodigosInfo.DUPLICADO_POR_NOTARIO, 
				new String[]{"0005",MSG_0005});
		mensajes.put(ProcesadorEscritura.CodigosInfo.ESCRITURA_NULA, 
				new String[]{"0020",MSG_0020});
		mensajes.put(ProcesadorEscritura.CodigosInfo.ERROR_DATOS, 
				new String[]{"0020",MSG_0020});
		mensajes.put(ProcesadorEscritura.CodigosInfo.ERROR, 
				new String[]{"0100",MSG_0100});
	}
	/**
	 * Genera el resultado en funci�n del alta de escritura.
	 * @param in Informaci�n sobre el proceso del alta de la escritura.
	 * @return
	 */
	public ResultadoTypeOut generaResultadoEscritura(ProcesadorEscritura.InfoProceso in)
	{
		ResultadoTypeOut res = new ResultadoTypeOut();
		String codigo;
		String mensaje;
		if (mensajes.containsKey(in.getCodigo()))
		{
			codigo=mensajes.get(in.getCodigo())[0];
			mensaje= mensajes.get(in.getCodigo())[1];
			if (in.getDescripcion()!=null )
			{
				mensaje+= ":"+in.getDescripcion();
			}
		}
		else
		{
			codigo="0100";
			mensaje= MSG_0100;
		}
		res.setCodigo(codigo);
		res.setDescripcion(mensaje);
		res.setID(Mensajes.RESPUESTA_ID);
		return res;
	}

	public ResultadoTypeOut generaResultadoErrorGenerico()
	{
		ResultadoTypeOut res= new ResultadoTypeOut();
		res.setCodigo("0100");
		res.setDescripcion(MSG_0100);
		res.setID(Mensajes.RESPUESTA_ID);
		return res;
	}
	
	public ResultadoTypeOut generaResultadoFatal ()
	{
		ResultadoTypeOut res=new ResultadoTypeOut();
		res.setCodigo("0999");
		res.setDescripcion(MSG_0999);
		res.setID(Mensajes.RESPUESTA_ID);
		return res;
	}
}
