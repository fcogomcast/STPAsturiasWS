package es.tributasenasturias.validacion;

import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.*;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;





/*
 * @autor:DavidSA::Validación de Documento XMLFirmado.
 */

public class ValidarXMLSign {
	public static boolean valida(String documentoXML) {
		boolean coreValidity = false;
		try {							
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(documentoXML);
			doc.getDocumentElement().normalize();						
			// Obtenemos el nodo con los datos de la firma
			NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS,"Signature");
			
			if (nl.getLength() == 0) {			
				throw new Exception(
						"No se encuentra elemento(Signature) para realizar la validación");
			}

			DOMValidateContext valContext = new DOMValidateContext(
					new X509KeySelector(), nl.item(0));

			// Unmarshal the XMLSignature.
			XMLSignatureFactory factory = XMLSignatureFactory
					.getInstance("DOM");
			XMLSignature signature = factory.unmarshalXMLSignature(valContext);
			// Validate the XMLSignature.
			coreValidity = signature.validate(valContext);

		} catch (Exception e) {
			System.out.println("Error genérico al validar la firma::"
					+ e.getMessage());
			e.printStackTrace();
		}

		return coreValidity;
	}

	public static void main(String[] args) {
		File file = new File("Q:\\noexiste_archivos\\david\\PDAFirma2.xml");					
		if (ValidarXMLSign.valida(file.toString()))
			System.out.println("Firma Valida");
		else
			System.out.println("Firma NO Valida");
	}
}
