package es.tributasenasturias.docs;

import java.io.OutputStream;
import java.rmi.RemoteException;

import es.tributasenasturias.documentos.DatosSalidaImpresa;

public class ReqDoc extends PdfBase{

	public ReqDoc() {
		Session.put("cgestor", "");
		plantilla = "impresos\\xml\\reqDoc.xml";
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void compila(String id, String xml, String xsl, OutputStream output) throws RemoteException {
		try {
			requerirDocumento(id, xml, xsl, output);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("static-access")
	public void requerirDocumento(String id, String xml, String xsl, OutputStream output) {
		
		DatosSalidaImpresa s = new DatosSalidaImpresa(xml, xsl, output);
		s.Mostrar();
	}
	
}

