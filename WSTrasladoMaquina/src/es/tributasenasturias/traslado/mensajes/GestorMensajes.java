package es.tributasenasturias.traslado.mensajes;

import javax.naming.ConfigurationException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import es.tributasenasturias.traslado.util.Constantes;

/**
 * Gesti�n de los mensajes de aplicaci�n. Los cargar� del fichero que los contenga, y devolver� el
 * mapeo de los mensajes externos o bien el mensaje que se solicite.
 * En caso de querer crear una secci�n con mapeo (lo normal ser� la de base de datos) la sintaxis ser�
 * <pre> 
 * &lt;grupo id=&quot;base.datos&quot;&gt;
 *	
	 &lt;mensaje id=&quot;error.mostrado.aplicacion&quot;&gt;
		&lt;codigo&gt;0010&lt;/codigo&gt;
		&lt;descripcion&gt;Descripcion del error&lt;/descripcion&gt;
	&lt;/mensaje&gt;	
	&lt;!--mapeos--&gt;
		&lt;mapeo&gt;
			&lt;codigo&gt;35&lt;/codigo&gt;
			&lt;mensaje&gt;autoliquidacion.usada&lt;/mensaje&gt;
		&lt;/mapeo&gt;
	&lt;/grupo&gt;
	</pre>
 * En este caso se mapea el error 35 de base de datos al 0010.
 * @author crubencvs
 *
 */
public final class GestorMensajes {

	private Mensajes mensajes;
	
	/**
	 * Lee el fichero de configuraci�n y recupera los mensajes de aplicaci�n
	 * @param fichero
	 * @throws ConfigurationException
	 */
	public GestorMensajes(String fichero) throws ConfigurationException{
		try {
			JAXBContext j= JAXBContext.newInstance(Mensajes.class);
			StreamSource source = new StreamSource(fichero);
			Unmarshaller ums= j.createUnmarshaller();
			JAXBElement<Mensajes> men = ums.unmarshal(source,Mensajes.class);
			this.mensajes= men.getValue();
		}catch (JAXBException je){
			throw new ConfigurationException("Error en configuraci�n de mensajes:" + je.getMessage());
		}
	}
	/**
	 * 
	 * @return mensajes de aplicaci�n
	 */
	public Mensajes getMensajes() {
		return mensajes;
	}
	/**
	 * Recupera un grupo de mensjes por identificador
	 * @param id Identificador del grupo a buscar
	 * @return {@link Grupo} o null si no existe ninguno con ese Id o el par�metro es nulo
	 */
	public Grupo getGrupoPorId(String id)
	{
		if (id==null)
		{
			return null;
		}
		Grupo g = null;
		for (Grupo group: mensajes.getGrupos())
		{
			if (group.getId().equals(id))
			{
				g= group;
				break;
			}
		}
		return g;
	}
	/**
	 * Devuelve un mensaje concreto indicando su grupo y su id
	 * @param idGrupo Identificador del grupo donde se encuentra el mensaje
	 * @param idMensaje Identificador del mensaje
	 * @return Mensaje o null si no se encuentra o cualquiera de los par�metros son nulos
	 */
	public Mensaje getMensajePorId(String idGrupo, String idMensaje)
	{
		if (idGrupo==null || idMensaje==null)
		{
			return null;
		}
		Mensaje mensaje= null;
		Grupo gr= getGrupoPorId(idGrupo);
		if (gr!=null)
		{
			
			for (Mensaje m: gr.getMensajes())
			{
				if (m.getId().equals(idMensaje))
				{
					mensaje=m;
					break;
				}
			}
		}
		return mensaje;
	}
	/**
	 * Recupera un mensaje en base a un grupo y un c�digo de mapeo
	 * @param idGrupo Identificador del grupo donde se encontrar� el mensaje y su mapeo
	 * @param codigo C�digo de mapeo del mensaje
	 * @param mensajeOriginal mensaje original recibido, por si el mapeo tiene el flag mostrar_original
	 * @return Mensaje o null si no se ha podido encontrar el mensaje o cualquier a de sus par�metros es nulo
	 */
	public Mensaje mapear(String idGrupo,String codigo, String mensajeOriginal) {
		Mensaje m=null; //Por defecto devolvemos nulo
		Grupo gr = getGrupoPorId(idGrupo);
		if (gr!=null)
		{
			for (Mapeo map:gr.getMapeos())
			{
				if (map.getCodigo().equals(codigo))
				{
					if ("S".equalsIgnoreCase(map.getMostrarOriginal()))
					{
						m = new Mensaje();
						m.setCodigo(formateaCodigo(codigo));
						m.setDescripcion(mensajeOriginal);
					}
					else
					{
						String idMensaje= map.getMensaje();
						m= getMensajePorId(idGrupo, idMensaje);
					}
				}
			}
		}
		return m;
	}
	/**
	 * Recupera el mensaje de error por defecto.
	 * @return Mensaje de error por defecto. 
	 */
	public Mensaje errorPorDefecto()
	{
		Mensaje mensaje=getMensajePorId("aplicacion","error.default");
		if (mensaje==null)
		{
			mensaje= new Mensaje();
			mensaje.setCodigo(Constantes.CODIGO_ERROR_DEFECTO);
			mensaje.setDescripcion(Constantes.MENSAJE_ERROR_DEFECTO);
		}
		return mensaje;
	}
	/**
	 * Recupera el mensaje de terminaci�n correcta por defecto
	 * @return Mensaje de terminaci�n por defecto
	 */
	public Mensaje terminacionOK()
	{
		Mensaje mensaje=getMensajePorId("aplicacion","ok");
		if (mensaje==null)
		{
			mensaje= new Mensaje();
			mensaje.setCodigo(Constantes.CODIGO_ERROR_DEFECTO);
			mensaje.setDescripcion(Constantes.MENSAJE_ERROR_DEFECTO);
		}
		return mensaje;
	}
	/**
	 * Recupera el mensaje de terminaci�n cuando el traslado ya estaba realizado
	 * @return Mensaje de terminaci�n por defecto
	 */
	public Mensaje terminacionYaTrasladado()
	{
		Mensaje mensaje=getMensajePorId("aplicacion","ya.trasladado");
		if (mensaje==null)
		{
			mensaje= new Mensaje();
			mensaje.setCodigo(Constantes.CODIGO_ERROR_DEFECTO);
			mensaje.setDescripcion(Constantes.MENSAJE_ERROR_DEFECTO);
		}
		return mensaje;
	}
	/**
	 * Devuelve el c�digo de mensaje equivalente a uno recibido desde base de datos o el c�digo de error
	 * por defecto, si no encuentra nada.
	 * En caso de no existir secci�n de base de datos, que ser� lo habitual, devolver� lo que indique la base.
	 * @param codigoBD C�digo recibido de la base de datos
	 * @param mensajeBD Mensaje recibido de la base de datos
	 * @return
	 */
	public Mensaje getErrorBD(String codigoBD, String mensajeBD)
	{
		Mensaje mn= mapear("base.datos", codigoBD, mensajeBD);
		if (mn==null)
		{
			//Si no hay mapeo de error, pasamos directamente lo que ha vuelto de base de datos
			if (codigoBD!=null) {
				mn= new Mensaje();
				mn.setCodigo(formateaCodigo(codigoBD));
				mn.setDescripcion(mensajeBD);
			}
			else {
				mn= errorPorDefecto();
			}
		}
		return mn;
	}
	
	/**
	 * Devuelve el c�digo de mensaje de aplicaci�n que se indica.
	 * En caso de que el mapeo indique que el mensaje de base de datos ha de pasarse sin tratar
	 * se pasa el que se indica en el par�metro mensajeBD
	 * @param codigoBD C�digo recibido de la base de datos
	 * @param mensajeBD Mensaje recibido de la base de datos
	 * @return
	 */
	public Mensaje getErrorAplicacion(String codigo)
	{
		Mensaje mn= getMensajePorId("aplicacion", codigo);
		return mn;
	}
	/**
	 * Rellena por la izquierda una cadena hasta una longitud indicada, con un caracter de relleno
	 * @param cadena Cadena a rellenar 
	 * @param longitud Longitud de la cadena resultante
	 * @param caracter Caracter de relleno
	 * @return Cadena original, rellena con el caracter de relleno por la izquierda hasta tener una cadena de la longitud indicada en par�metro.
	 */
	private String lpad(String cadena, int longitud, char caracter) {
		if (cadena==null){
			return cadena;
		}
		if (cadena.length()>=longitud){
			return cadena;
		}
		StringBuilder sb = new StringBuilder(longitud);
	    for (int i = cadena.length(); i < longitud; i++) {
	      sb.append(caracter);
	    }
	    sb.append(cadena);
	    return sb.toString();
	}
	
	private String formateaCodigo (String codigo) {
		return lpad(codigo, 4, '0');
	}
}
