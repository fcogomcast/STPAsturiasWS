package es.tributasenasturias.docs;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//DatosSalidaImpresa dsi = new DatosSalidaImpresa();
		//System.out.println(dsi.findText("4"));
		try {
			String numeroAutoliq = new String("6002900183856");
			String numeroAutoliqjpExento = new String("6002900179461");
			Comparecencia docComparecencia = new Comparecencia(numeroAutoliq);
			JustificanteCobro jc = new JustificanteCobro(numeroAutoliq);
			JustificantePresentacion jp = new JustificantePresentacion(numeroAutoliq);
			String pdf=PDFUtils.generarPdf(jp);
			System.out.println(pdf);
		} catch (Exception se) {
			System.out.println(se.getMessage());
		}
		
	}

}
