package es.tributasenasturias.docs;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public abstract class PdfBase {

	public String plantilla;
	public Map<String, String> session = new HashMap<String, String>();
	
	public abstract String getPlantilla();
	
	public abstract void compila(String id, String xml, String xsl, OutputStream output) throws RemoteException;
}

