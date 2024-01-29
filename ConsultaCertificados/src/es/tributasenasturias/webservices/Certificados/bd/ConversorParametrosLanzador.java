package es.tributasenasturias.webservices.Certificados.bd;


import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import es.tributasenasturias.webservices.Certificados.Exceptions.DatosException;
import es.tributasenasturias.webservices.Certificados.tipos.Peti;
import es.tributasenasturias.webservices.Certificados.tipos.Peti.Proc;
import es.tributasenasturias.webservices.Certificados.tipos.Peti.Proc.Param;
import es.tributasenasturias.webservices.Certificados.utils.XMLDOMUtils;



/**
 * Clase de utilidad para manejar parámetros a procedimientos de base de datos.
 * 
 *
 */
public class ConversorParametrosLanzador 
{
	private HashMap<TIPOS,String> m_tipos = new HashMap<TIPOS, String>();
	private String m_procedimientoAlmacenado = "";
	private String m_resultado = "";
	private org.w3c.dom.Document m_resultadoXML = null;
	private Peti peticion;
	// Objeto para manejar árbol DOM.
	javax.xml.parsers.DocumentBuilderFactory fact;
	javax.xml.parsers.DocumentBuilder db;
	/**
	 * Enumeración de tipos de parámetro.
	 * 
	 *
	 */
	public enum TIPOS {String, Integer, Date}
	/**
	 * Constructor público.
	 * @throws javax.xml.parsers.ParserConfigurationException
	 */
	public ConversorParametrosLanzador() throws javax.xml.parsers.ParserConfigurationException
	{
		//inicializamos la correspondencia de tipos
		m_tipos.put(TIPOS.String, 	"1");
		m_tipos.put(TIPOS.Integer, 	"2");
		m_tipos.put(TIPOS.Date, 	"3");
		// Inicializamos  los objetos que nos servirán para parsear los resultados.
		fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		db= fact.newDocumentBuilder(); // Lanza javax.xml.parsers.ParserConfigurationException
		//logger=new SimpleFileLogger();
		peticion = new Peti();
	}
	/**
	 * Método que devuelve el resultado de una llamda.
	 * @return Cadena de resultado de una llamada.
	 */
	public String getResultado()
	{
		return m_resultado;
	}
	
	/**
	 * Obtiene el primer valor asociado al nombre del nodo dado
	 * @param nombreNodo
	 * @return
	 */
	public String getNodoResultado(String nombreNodo)
	{
		String toReturn = "";
		
		int initialIndex = m_resultado.indexOf(nombreNodo);
		if ( initialIndex >= 0)
		{
			int inicioValor = m_resultado.indexOf('>', initialIndex) + 1;
			int finValor = m_resultado.indexOf('<',inicioValor);
			
			toReturn = m_resultado.substring(inicioValor,finValor);
		}
		
		
		return toReturn;
	}
	/**
	 * Obtiene el primer valor asociado al nombre del nodo dado
	 * @param nombreNodo
	 * @return
	 */
	public String getNodoResultadoX(String nombreNodo)
	{
		if (this.m_resultadoXML==null)
		{
			return getNodoResultado(nombreNodo);
		}
		String toReturn = "";
		
		org.w3c.dom.NodeList nodos = this.m_resultadoXML.getElementsByTagName(nombreNodo);
		if (nodos.getLength()>0) 
		{
			//toReturn = nodos.item(0).getFirstChild().getNodeValue();
			toReturn = XMLDOMUtils.getNodeText(nodos.item(0));
		}
		
		return toReturn;
	}
	/**
	 * Parsea el resultado como XML. En caso de no poder hacerlo, simplemente lo ignora.
	 * @param nombreNodo
	 * @return
	 */
	private void interpretaResultadoXML()
	{
		if (this.m_resultado !=null && !this.m_resultado.equalsIgnoreCase(""))
		{
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(m_resultado.toString()));// Ya es un string, pero para prevenir futuros cambios.
			try
			{
				this.m_resultadoXML = db.parse(inStr);
			}
			catch (java.io.IOException ex)
			{
				this.m_resultadoXML =null;
			}
			catch (org.xml.sax.SAXException ex)
			{
				this.m_resultadoXML=null;
			}
		}
	}
	/**
	 * Método para pasar un parámetro a la lista de un procedimiento  almacenado que se va a ejecutar.
	 * @param valor Valor del parámetro. Los parámetros son posicionales.
	 * @param tipo Tipo del parámetro.
	 */
	public void setParametro(String valor, TIPOS tipo)
	{
		String value= valor==null?"":valor;
		Param param = new Param();
		param.setValor(value);
		param.setTipo(Short.valueOf(m_tipos.get(tipo)));
		param.setFormato("");
		peticion.getProc().getParam().add(param);
		
	}
	/**
	 * Método para pasar un parámetro con formato a la lista de un procedimiento almacenado que se va a ejecutar.
	 * @param valor Valor del parámetro. Los parámetros son posicionales.
	 * @param tipo Tipo del parámetro.
	 * @param formato Formato del parámetro.
	 */
	public void setParametro(String valor, TIPOS tipo, String formato)
	{
		
		Param param = new Param();
		param.setValor(valor);
		param.setTipo(Short.valueOf(tipo.toString()));
		param.setFormato(formato);
		peticion.getProc().getParam().add(param);
	}
	/**
	 * Método que indica el procedimiento almacenado que se va a ejecutar.
	 * @param nombre Nombre del procedimiento almacenado.
	 */
	public void setProcedimientoAlmacenado(String nombre)
	{
		Proc proc = new Peti.Proc();
		proc.setNombre(nombre);
		peticion.setProc(proc);
	}
	
	public String getProcedimientoAlmacenado()
	{
		return m_procedimientoAlmacenado;
	}
	
	private String peticionToString(Peti peticion) throws DatosException
	{
		try {
			JAXBContext ctx= JAXBContext.newInstance(this.peticion.getClass().getPackage().getName());
			Marshaller mars = ctx.createMarshaller();
			StringWriter writer = new StringWriter();
			mars.marshal(peticion, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new DatosException  ("Excepción al crear la petición:" + e.getMessage(),e);
		}
	}
	public String Codifica() throws DatosException
	{
		/*String textoProcedimiento = "<proc nombre=\"" + m_procedimientoAlmacenado + "\">";
		String textoParametros = "";
		
		for(int i=0;i<m_valorParametros.size();i++)
		{
			String formato = "";
			if(m_formatoParametros.get(i) != null && m_formatoParametros.get(i).length()>0)
			{
				formato = "<formato>"+m_formatoParametros.get(i)+"</formato>";
			}
			else
			{
				formato = "<formato/>";
			}
	       	textoParametros += "<param id=\""+(i+1)+"\"><valor>"+m_valorParametros.get(i) +
	       	"</valor><tipo>"+m_tipos.get(m_tipoParametros.get(i))+"</tipo>"+formato+"</param>";
		}
		*/
		return peticionToString(this.peticion);	
	}
	public void setResultado(String resultado) {
		this.m_resultado = resultado;
		interpretaResultadoXML();
	}
}