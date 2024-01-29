package es.tributasenasturias.pagopresentacionmodelo600utils;

/**
 * Ayuda a medir los tiempos de ejecuci�n de una secci�n.
 * @author crubencvs
 *
 */
public class Crono {
	private long start;
	private long end;
	public void start()
	{
		start=System.currentTimeMillis();
	}
	public void stop()
	{
		end=System.currentTimeMillis();
	}
	public long getTime()
	{
		return end-start;
	}
}
