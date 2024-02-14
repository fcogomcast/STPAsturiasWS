package es.tributasenasturias.apremios.proxy;

import es.tributasenasturias.apremios.ResultadoAEATType;

/**
 * Clase para el retorno del envío de PDF de apremio a la AEAT
 * @author crubencvs
 *
 */
public class ResultadoEnvioPdf {

	private boolean esError;
	private String codError;
	private ResultadoAEATType resultadoAEAT;
	
	/** 
	 * Para construir un objeto de la clase. 
	 * @author crubencvs
	 *
	 */
	public static class Builder {
		private boolean esError;
		private String codError;
		private ResultadoAEATType resultadoAEAT;
		private boolean errorEstablecido=false;
		
		public Builder setEsError(boolean esError) {
			this.esError = esError;
			errorEstablecido= true;
			return this;
		}
		public Builder setCodError(String codError) {
			this.codError = codError;
			return this;
		}
		public Builder setResultadoAEAT(ResultadoAEATType resultadoAEAT) {
			this.resultadoAEAT = resultadoAEAT;
			return this;
		}
		
		public ResultadoEnvioPdf build(){
			if (!errorEstablecido) {
				throw new IllegalStateException("Error de programación: no se ha establecido si el resultado de " + ResultadoEnvioPdf.class + " es un error o no");
			}
			ResultadoEnvioPdf r= new ResultadoEnvioPdf();
			r.codError= this.codError;
			r.esError= this.esError;
			r.resultadoAEAT= this.resultadoAEAT;
			return r;
		}
		
	}
	private ResultadoEnvioPdf() {
	}
	public boolean esError() {
		return esError;
	}
	
	public String getCodError() {
		return codError;
	}
	public ResultadoAEATType getResultadoAEAT() {
		return resultadoAEAT;
	}
	
	
	
}
