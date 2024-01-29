/**
 * 
 */
package es.tributasenasturias.webservices.Certificados.utils.Log;

import java.util.logging.LogRecord;

public class SimpleFileFormatter extends java.util.logging.Formatter
{

	@Override
	public String format(LogRecord record) {
		// TODO Auto-generated method stub
		return new java.util.Date().toString()+":"+":Log"+":"+record.getLevel()+":"+record.getMessage()+"\n";
	}
	
}