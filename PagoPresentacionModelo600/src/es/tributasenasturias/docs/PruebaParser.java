package es.tributasenasturias.docs;
import org.w3c.dom.Document;
import org.w3c.dom.Element;	 
import es.tributasenasturias.documentos.util.XMLUtils;

public class PruebaParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        // Alta de la escritura:
        try {                   	         	       
        
        	String xmlData = new String("<remesa><declaracion><Escritura><documentoescritura>dsadadsdskjlaskjladskjlads</documentoescritura></Escritura></declaracion><declaracion><Escritura><documentoescritura>documento2</documentoescritura></Escritura></declaracion></remesa>");
        	Document docRespuesta = (Document) XMLUtils.compilaXMLObject(xmlData, null);         	
        	Element[] escritura = XMLUtils.selectNodes(docRespuesta, "//remesa//declaracion//Escritura");        	
        	System.out.println ("retorno:" + XMLUtils.selectSingleNode(escritura[0], "documentoescritura").getTextContent());        
        } catch (Exception e) {
        	System.out.println ("Error" + e.getMessage());
		}        
               
	}

}
