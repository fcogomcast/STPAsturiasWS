package es.tributasenasturias.modelo600utils;

import java.util.ArrayList;
import java.util.HashMap;

import es.tributasenasturias.validacion.XMLDOMUtils;



/**
 * Clase de utilidad para manejar par�metros a procedimientos de base de datos.
 */
public class ConversorParametrosLanzador{

	private HashMap<TIPOS,String> m_tipos = new HashMap<TIPOS, String>();
	private ArrayList<String> m_valorParametros = new ArrayList<String>();
	private ArrayList<String> m_formatoParametros = new ArrayList<String>();
	private ArrayList<TIPOS> m_tipoParametros = new ArrayList<TIPOS>();
	private String m_procedimientoAlmacenado = new String();
	private String m_resultado = new String();
	private org.w3c.dom.Document m_resultadoXML = null;
	
	// Objeto para manejar �rbol DOM.
	javax.xml.parsers.DocumentBuilderFactory fact;
	javax.xml.parsers.DocumentBuilder db;

	/**
	 * Enumeraci�n de tipos de par�metro.
	 */
	public static enum TIPOS {String, Integer, Date, Clob}

	/**
	 * Constructor p�blico.
	 * @throws javax.xml.parsers.ParserConfigurationException
	 */
	public ConversorParametrosLanzador() throws javax.xml.parsers.ParserConfigurationException{
		//Inicializamos la correspondencia de tipos
		m_tipos.put(TIPOS.String, 	"1");
		m_tipos.put(TIPOS.Integer, 	"2");
		m_tipos.put(TIPOS.Date, 	"3");
		m_tipos.put(TIPOS.Clob, 	"4");
		//Inicializamos  los objetos que nos servir�n para parsear los resultados.
		fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		db= fact.newDocumentBuilder(); // Lanza javax.xml.parsers.ParserConfigurationException
	}

	/**
	 * Devuelve el error en la llamada.
	 * @return Cadena con el error.
	 */
	public String getError()
	{
		return getNodoResultado("error");
	}
	/**
	 * M�todo que devuelve el resultado de una llamda.
	 * @return Cadena de resultado de una llamada.
	 */
	public String getResultado(){
		return m_resultado;
	}
	
	/**
	 * Obtiene el primer valor asociado al nombre del nodo dado
	 * @param nombreNodo
	 * @return
	 */
	public String getNodoResultado(String nombreNodo){
		String toReturn = new String();
		
		String aux = new String(m_resultado);
		String nombreNodoMayus = new String(nombreNodo);
		nombreNodoMayus = nombreNodoMayus.toUpperCase();
		aux = aux.toUpperCase();
		int initialIndex = aux.indexOf("<"+nombreNodoMayus+"/>");
		if (initialIndex ==-1){
			initialIndex = aux.indexOf("<"+nombreNodoMayus);
			if ( initialIndex >= 0){
				int inicioValor = aux.indexOf('>', initialIndex) + 1;
				int finValor = aux.indexOf("</"+nombreNodoMayus,inicioValor);
				toReturn = m_resultado.substring(inicioValor,finValor);
			}
		}
		else
		{
			toReturn="";
		}	
		return toReturn;
	}

	/**
	 * Obtiene el primer valor asociado al nombre del nodo dado
	 * @param nombreNodo
	 * @return
	 */
	public String getNodoResultadoX(String nombreNodo){
		if (this.m_resultadoXML==null){
			return getNodoResultado(nombreNodo);
		}
		String toReturn = new String();
		//org.w3c.dom.NodeList nodos = this.m_resultadoXML.getElementsByTagName(nombreNodo);
		org.w3c.dom.NodeList nodos = XMLDOMUtils.getAllNodes(m_resultadoXML, nombreNodo);
		if (nodos.getLength()>0){
			
			toReturn = XMLDOMUtils.getNodeText(nodos.item(0));
		}		
		return toReturn;
	}
	/**
	 * Parsea el resultado como XML. En caso de no poder hacerlo, simplemente lo ignora.
	 * @param nombreNodo
	 * @return
	 */
	private void interpretaResultadoXML(){
		if (this.m_resultado !=null && this.m_resultado !=""){
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(m_resultado.toString()));// Ya es un string, pero para prevenir futuros cambios.
			try{
				this.m_resultadoXML = db.parse(inStr);
			}catch (java.io.IOException ex){
				this.m_resultadoXML =null;
			}catch (org.xml.sax.SAXException ex){
				this.m_resultadoXML=null;
			}
		}
	}

	/**
	 * M�todo para pasar un par�metro a la lista de un procedimiento  almacenado que se va a ejecutar.
	 * @param valor Valor del par�metro. Los par�metros son posicionales.
	 * @param tipo Tipo del par�metro.
	 */
	public void setParametro(String valor, TIPOS tipo){
		if (valor==null) // Si no se hace as�, introduce en la base de datos "null", al incluirlo en el ArrayList.
			valor="";
		m_valorParametros.add(valor);
		m_tipoParametros.add(tipo);
		m_formatoParametros.add(null);
	}

	/**
	 * M�todo para pasar un par�metro con formato a la lista de un procedimiento almacenado que se va a ejecutar.
	 * @param valor Valor del par�metro. Los par�metros son posicionales.
	 * @param tipo Tipo del par�metro.
	 * @param formato Formato del par�metro.
	 */
	public void setParametro(String valor, TIPOS tipo, String formato){
		if (valor==null)
			valor="";
		m_valorParametros.add(valor);
		m_tipoParametros.add(tipo);
		m_formatoParametros.add(formato);
	}

	/**
	 * M�todo que indica el procedimiento almacenado que se va a ejecutar.
	 * @param nombre Nombre del procedimiento almacenado.
	 */
	public void setProcedimientoAlmacenado(String nombre){
		m_procedimientoAlmacenado = nombre;
		m_valorParametros.clear();
		m_tipoParametros.clear();
		m_formatoParametros.clear();
	}

	public String getProcedimientoAlmacenado(){
		return m_procedimientoAlmacenado;
	}

	public String Codifica(){
		String textoProcedimiento = "<proc nombre=\"" + m_procedimientoAlmacenado + "\">";
		String textoParametros = new String();

		for(int i=0;i<m_valorParametros.size();i++){
			String formato = new String();
			if(m_formatoParametros.get(i) != null && m_formatoParametros.get(i).length()>0){
				formato = "<formato>"+m_formatoParametros.get(i)+"</formato>";
			}else{
				formato = "<formato/>";
			}
	       	textoParametros += "<param id=\""+(i+1)+"\"><valor>"+m_valorParametros.get(i) +
	       	"</valor><tipo>"+m_tipos.get(m_tipoParametros.get(i))+"</tipo>"+formato+"</param>";
		}
		return "<peti>"+textoProcedimiento+textoParametros+"</proc></peti>";
	}

	public ArrayList<HashMap<String,String>> Decodifica(String mensaje){
		ArrayList<HashMap<String,String>> listaFilas = new ArrayList<HashMap<String,String>>();
		int i=0;
		while(i<mensaje.length()){
			int initialIndex = mensaje.indexOf("<fila>",i);
			if(initialIndex > 0){
				int inicioFila = mensaje.indexOf('>', initialIndex) + 1;
				int finFila = mensaje.indexOf("</fila>",inicioFila);
				String fila = mensaje.substring(inicioFila, finFila);

				int j=0;
				HashMap<String,String> columnasFila = new HashMap<String, String>();
				while(j<fila.length()){
					int inicioColumna = fila.indexOf('<',j)+1;
					if(inicioColumna > 0){
						int finColumna = fila.indexOf('>',inicioColumna);
						String nombreColumna = fila.substring(inicioColumna,finColumna);
						int finValor = fila.indexOf("</",finColumna);
						String valorColumna = fila.substring(finColumna+1,finValor);
						j = fila.indexOf('>',finValor);
						columnasFila.put(nombreColumna, valorColumna);
					}else{
						break;
					}
				}
				i = mensaje.indexOf('>',finFila);
				listaFilas.add(columnasFila);
			}else{
				break;
			}
		}
		return listaFilas;
	}

	public void setResultado(String resultado) {
		this.m_resultado = resultado;
		interpretaResultadoXML();
	}
}