package es.tributasenasturias.documentopagoutils;


import org.w3c.dom.Document;
import org.w3c.dom.Node;



/**
 * Parser para los xml de reimpresi�n de Gestor Documental
 * @author crubencvs
 *
 */
public class PDFParserHandlerGD {
	
	String dataCadena = "";
	String elemActual = "";
	DocumentoGD dc;
	Document doc;

	/**
	 * Constructor
	 * @param datos DocumentoGD
	 * @param documento xml de reimpresi�n a tratar.
	 */
	protected PDFParserHandlerGD(DocumentoGD datos, 
			Document documento) throws Exception{
		dc = datos;
		doc = documento;
		if (dc == null)
			throw new Exception ("Documento vac�o pasado a " + PDFParserHandlerGD.class.getName());
	}

	/**
	 * Escribe a PDF el Documento informado.
	 */
	public void parse() {
		if (doc == null)
			return;
		Node nodeActual = doc;
		boolean devuelta = false;
		while (nodeActual != null) {
			String nombre = nodeActual.getNodeName();
			boolean noesTextoComment = nodeActual.getNodeType() != Node.TEXT_NODE
					&& nodeActual.getNodeType() != Node.COMMENT_NODE;
			// para cada nodo con informacion, llama los procedimientos de
			// inicio(pdf_open_page) y modificacion, pasa a
			// sus hijos y llama los procedimientos de cerrado(pdf_close_page)
			if (!devuelta) {
				if (noesTextoComment) {
					if (dc != null)
						startElementDSI(nodeActual, nombre);
				}
			}
			if (nodeActual.hasChildNodes() && !devuelta) {
				nodeActual = nodeActual.getFirstChild();
			} else if (nodeActual.getNextSibling() != null) {
				if (noesTextoComment) {
					if (dc != null)
						endElementDSI(nodeActual, nombre);
				}
				devuelta = false;
				nodeActual = nodeActual.getNextSibling();
			} else {
				if (noesTextoComment) {
					if (dc != null)
						endElementDSI(nodeActual, nombre);
				}
				nodeActual = nodeActual.getParentNode();
				devuelta = true;
			}
			if (nodeActual.equals(doc))
				break;
		}
	}

	/**
	 * Realiza las tareas relacionadas con la inicializacion del nodo
	 * 
	 * @param node
	 * @param nombre
	 */
	public void startElementDSI(Node node, String nombre) {

		if (nombre.equalsIgnoreCase("Documento")) {
			dc.pdf_gd_open_file(node);
			dc.pdf_gd_get_version_documento(node);
			dc.pdf_gd_cargar_imagenes(node);
		} else if (nombre.equalsIgnoreCase("Pagina")) {
			dc.pdf_gd_iniciar_variables();
			dc.pdf_gd_begin_page(node);
		} else if (nombre.equalsIgnoreCase("Bloque")) {
			dc.pdf_gd_dibuja_bloque(node);
		} else if (nombre.equalsIgnoreCase("Imagen")) {
			dc.pdf_gd_place_image_file(node);
		} else if (nombre.equalsIgnoreCase("Cabecera")) {
			dc.pdf_gd_open_cabecera(node);
		} else if (nombre.equalsIgnoreCase("Pie")) {
			dc.pdf_gd_open_pie(node);
		} else if (nombre.equalsIgnoreCase("Cuadro")) {
			dc.pdf_gd_rect(node);
		} else if (nombre.equalsIgnoreCase("Linea")) {
			dc.pdf_gd_linea(node);
		}  else if (nombre.equalsIgnoreCase("Campo")) {
			dc.pdf_gd_show_texto(node);
		}
		
	}

	/**
	 * Realiza las tareas relacionadas con la finalizacion del nodo
	 * 
	 * @param node
	 * @param nombre
	 */
	public void endElementDSI(Node node, String nombre) {

		if (nombre.equalsIgnoreCase("Documento")) {
			dc.pdf_gd_close();
		} else if (nombre.equalsIgnoreCase("Pagina")) {
			dc.pdf_gd_end_page();
			dc.pdf_gd_save_numpag();
		} else if (nombre.equalsIgnoreCase("Cabecera")) {
			dc.pdf_gd_close_cabecera();
		}else if (nombre.equalsIgnoreCase("Pie")) {
			dc.pdf_gd_close_pie();
		}
	}



}
