package com.stpa.ws.server.formularios;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Node;



/**
 * Parser para los xml procesados por DatosSalidaImpresa y DatosSalidaImpresa
 * 
 * @author david.gimenez.banos
 * 
 */
public class PDFParserHandler {
	/**
	 * Mapa con las propiedades a informar en el PDF: "creador","autor","titulo"
	 */
	HashMap<String, String> mapNames = new HashMap<String, String>();
	String dataCadena = "";
	String elemActual = "";
	DatosSalidaImpresa dsi;
	Document doc;

	public PDFParserHandler(DatosSalidaImpresa datos, Document documento) {
		this(datos, documento, null);
	}

	/**
	 * @param datos
	 * @param datos2
	 * @param documento
	 * @param mapNombres
	 */
	protected PDFParserHandler(DatosSalidaImpresa datos, 
			Document documento, HashMap<String, String> mapNombres) {
		dsi = datos;
		doc = documento;
		if (dsi == null)
			dsi = new DatosSalidaImpresa();
		mapNames = mapNombres;
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
					if (dsi != null)
						startElementDSI(nodeActual, nombre);
				}
			}
			if (nodeActual.hasChildNodes() && !devuelta) {
				nodeActual = nodeActual.getFirstChild();
			} else if (nodeActual.getNextSibling() != null) {
				if (noesTextoComment) {
					if (dsi != null)
						endElementDSI(nodeActual, nombre);
				}
				devuelta = false;
				nodeActual = nodeActual.getNextSibling();
			} else {
				if (noesTextoComment) {
					if (dsi != null)
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

		if (nombre.equalsIgnoreCase("Paginas")) {
			dsi.pdf_open_file(node);
			String creador = "";
			String autor = "autor";
			String titulo = "titulo";
			if (mapNames != null) {
				creador = mapNames.get("creador");
				autor = mapNames.get("autor");
				titulo = mapNames.get("titulo");
				creador = (creador == null) ? "" : creador;
				autor = (autor == null || autor.equals("")) ? "autor" : creador;
				titulo = (titulo == null || titulo.equals("")) ? "titulo"
						: titulo;
				dsi.pdf_creador(creador, autor, titulo);
			}
		} else if (nombre.equalsIgnoreCase("Pagina")) {
			dsi.pdf_begin_page(node);
		} else if (nombre.equalsIgnoreCase("texto")) {
			dsi.pdf_setfont(node);
			dsi.pdf_show_text(node);
		} else if (nombre.equalsIgnoreCase("textobox")) {
			dsi.pdf_setfont(node);
			dsi.pdf_show_boxed(node);
		} else if (nombre.equalsIgnoreCase("cuadro")) {
			dsi.pdf_rect(node);
		} else if (nombre.equalsIgnoreCase("imagen")) {
			dsi.pdf_open_image_file(node);
		} else if (nombre.equalsIgnoreCase("barras")) {
			dsi.pdf_barras(node);
		} else if (nombre.equalsIgnoreCase("linea")) {
			dsi.pdf_linea(node);
		} else if (nombre.equalsIgnoreCase("salto")) {
			dsi.pdf_end_page(node);
		}
	}

	/**
	 * Realiza las tareas relacionadas con la finalizacion del nodo
	 * 
	 * @param node
	 * @param nombre
	 */
	public void endElementDSI(Node node, String nombre) {

		if (nombre.equalsIgnoreCase("Paginas")) {
			dsi.pdf_close();
		} else if (nombre.equalsIgnoreCase("Pagina")) {
			dsi.pdf_end_page(node);
		} else if (nombre.equalsIgnoreCase("salto")) {
			dsi.pdf_begin_page(node);
		}
	}

	public void startElementDS(Node nodeActual, String nombre) {
		//  Auto-generated method stub

	}

	public void endElementDS(Node nodeActual, String nombre) {
		//  Auto-generated method stub

	}

}
