package es.tributasenasturias.escrituras.servlet;

import java.io.IOException;
import java.io.StringReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import es.tributasenasturias.webservice.EscriturasAncert;

/**
 * Servlet implementation class for Servlet: EscriturasServlet
 *
 */
 public class EscriturasServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   
   
private static final String ESCRITURA_GRANDE = "GRANDE";

/**
	 * 
	 */
	private static final long serialVersionUID = 8009230461680912368L;

	private static final int LONGITUD=280000; //Múltiplo de 4

    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public EscriturasServlet() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String codNotario= request.getParameter("codNotario");
		String codNotaria= request.getParameter("codNotaria");
		String numProtocolo = request.getParameter("protocolo");
		String protocoloBis = request.getParameter("protocoloBis");
		String fechaDocumento= request.getParameter("fechaDocumento");
		String codUsuario= request.getParameter("codUsuario");
		EscriturasAncert esc = new EscriturasAncert();
		String escritura=esc.consultaEscritura(codNotario, codNotaria, numProtocolo, protocoloBis, fechaDocumento, codUsuario);
		char[] buffer= new char[LONGITUD];
		int length=0;
		int leidos;
		StringReader sr = new StringReader(escritura);
		OutputStream resp = response.getOutputStream();
		byte[] bufferDecodificado;
		if (!escritura.equals(ESCRITURA_GRANDE) && !escritura.equals("KO" ))
		{
			response.setStatus(200);
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition","inline; filename=escritura.pdf");		
			while ((leidos=sr.read(buffer))!=-1) {
				if (leidos==LONGITUD){
					bufferDecodificado= Base64.decode(buffer);
				}
				else
				{
					char[] buf2 =  new  char[leidos];
					System.arraycopy(buffer,0,buf2,0,leidos);
					bufferDecodificado= Base64.decode(buf2);
				}
				length+=bufferDecodificado.length;
				resp.write(bufferDecodificado);
			}
			response.setContentLength(length);
		}
		else if (escritura.equals(ESCRITURA_GRANDE))
		{
			response.setStatus(200);
			response.setContentType("text/plain");
			resp.write(ESCRITURA_GRANDE.getBytes());
			response.setContentLength(ESCRITURA_GRANDE.getBytes().length);
		}
		else {
			response.setStatus(404);
		}
	}  	  	  	    
}