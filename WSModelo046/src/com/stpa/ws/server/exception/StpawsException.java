package com.stpa.ws.server.exception;

import com.stpa.ws.server.util.Logger;

public class StpawsException extends Exception{
	
	private static final long serialVersionUID = -7062408123839357401L;
	private String error;
	private Throwable excepcionAnidada;
	
	public StpawsException(String error, Throwable throwable){
		com.stpa.ws.server.util.Logger.error(error,throwable,Logger.LOGTYPE.APPLOG);
		this.error = error;
		excepcionAnidada = throwable;
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
