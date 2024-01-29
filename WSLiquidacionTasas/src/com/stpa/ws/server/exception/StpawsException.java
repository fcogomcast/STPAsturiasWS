package com.stpa.ws.server.exception;


public class StpawsException extends Exception{
	
	private static final long serialVersionUID = -7062408123839357401L;
	private String error;
	private Throwable excepcionAnidada;
	
	public StpawsException(String error, Throwable throwable){
		this.error = error;
		excepcionAnidada = throwable;
	}
	
	public StpawsException(String error) {
		super(error);
		this.error = error;
	}

	public Throwable getCause(){
		return excepcionAnidada;
	}
	
	public String getMessage(){
		StringBuffer strBuffer = new StringBuffer();
		if(super.getMessage()!=null){
			strBuffer.append(super.getMessage());
		}
		if(excepcionAnidada !=null){
			strBuffer.append("[");
			strBuffer.append(excepcionAnidada.getMessage());
			strBuffer.append("]");
		}
		return strBuffer.toString();
	}
	
	public String getError(){
		return error;
	}
}
