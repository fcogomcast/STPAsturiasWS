package es.tributasenasturias.apremios.bd;

import es.tributasenasturias.apremios.preferencias.Preferencias;
import es.tributasenasturias.apremios.soap.SoapClientHandler;
import es.tributasenasturias.lanzador.LanzadorException;
import es.tributasenasturias.lanzador.LanzadorFactory;
import es.tributasenasturias.lanzador.ParamType;
import es.tributasenasturias.lanzador.ProcedimientoAlmacenado;
import es.tributasenasturias.lanzador.TLanzador;
import es.tributasenasturias.lanzador.response.RespuestaLanzador;

/**
 * Clase de acceso a la base de datos
 * @author crubencvs
 *
 */
public class GestorBD {
	
	private Preferencias pref;
	private String idLlamada;
	public GestorBD(Preferencias pref, String idLlamada) {
		this.idLlamada= idLlamada;
		this.pref= pref;
	}
	
	/**
	 * Recupera el identificador de archivo asociado con la clave de liquidación y valor
	 * @param claveLiquidacion Clave de liquidación
	 * @param idEperValor Identificador de valor
	 * @return Identificador de archivo o 0 si no se ha podido recuperar.
	 * @throws DatosException 
	 */
	public int getIdArchivo(String claveLiquidacion, int idEperValor) throws DatosException
	{
		try{
			TLanzador lanzador = LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(idLlamada));
			ProcedimientoAlmacenado p = new ProcedimientoAlmacenado(pref.getProcAlmacenadoObtenerIdArchivo(), pref.getEsquemaBD());
			p.param(claveLiquidacion, ParamType.CADENA);
			p.param(String.valueOf(idEperValor), ParamType.CADENA);
			p.param("P", ParamType.CADENA); //Conexión oracle
			RespuestaLanzador res= new RespuestaLanzador(lanzador.ejecutar(p));
			if (res.esErronea())
			{
				throw new DatosException ("Error al recuperar el identificador de archivo en base de datos:" + res.getTextoError());
			}
			String resultado=res.getValue("CANU_CADENAS_NUMEROS", 1, "STRING1_CANU");
			String idArchivo= res.getValue("CANU_CADENAS_NUMEROS", 1, "NUME1_CANU");
			if ("OK".equalsIgnoreCase(resultado))
			{
				int id;
				try {
					id= Integer.valueOf(idArchivo);
				} catch (NumberFormatException nfe) {
					return 0;
				}
				return id;
			} else {
				return 0;
			}
		}catch (LanzadorException le)
		{
			throw new DatosException ("Error al recuperar el identificador de archivo en base de datos:" + le.getMessage(), le); 
		}
	}
}
