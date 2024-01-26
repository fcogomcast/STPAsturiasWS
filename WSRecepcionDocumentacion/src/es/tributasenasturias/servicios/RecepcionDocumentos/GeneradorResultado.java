package es.tributasenasturias.servicios.RecepcionDocumentos;

import java.util.HashMap;
import java.util.Map;

import es.tributasenasturias.servicios.RecepcionDocumentos.procesadores.ProcesadorEscritura;
import es.tributasenasturias.utils.Mensajes;

/**
 * Genera el nodo resultado de la respuesta.
 * @author crubencvs
 * TODO: Si se van a procesar en el futuro más de un tipo de documento, debería modificarse esta 
 * clase para soportar más de un tipo de resultado.
 * Se haría una clase base/interfaz, GeneradorResultado, y clases concretas que implementarían cada
 * uno de los tipos de resultado.
 * Se pasaría un parámetro que implementaría un interfaz, InfoProceso, y generaría el resultado en
 * función de él.
 * Sería necesaria una clase que decidiera qué tipo de generador de resultado se utilizará en función
 * de la información de proceso (preguntaría si es una información de proceso de escritura, o de otro tipo
 * de documento para llamar al generador que necesitara).
 */
public class GeneradorResultado {
	
	private static Map<ProcesadorEscritura.CodigosInfo,String[]> mensajes= new HashMap<ProcesadorEscritura.CodigosInfo, String[]>();
	private final static String MSG_0000="Recepción Correcta";
	private final static String MSG_0005="La escritura no se ha dado de alta por existir previamente";
	private final static String MSG_0020="Error en la validación de los datos de la petición";
	private final static String MSG_0100="No se ha podido completar la operación debido a un error técnico.Inténtelo en unos minutos o contacte con soporte técnico.";
	private final static String MSG_0999="Se ha producido un error grave durante la operación.";
	static
	{
		mensajes.put(ProcesadorEscritura.CodigosInfo.OK, 
							new String[]{"0000","Recepción Correcta"});
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
	 * Genera el resultado en función del alta de escritura.
	 * @param in Información sobre el proceso del alta de la escritura.
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
