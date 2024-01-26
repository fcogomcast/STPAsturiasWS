package es.stpa.smartmultas.clases;

import org.w3c.dom.Document;

import es.stpa.smartmultas.preferencias.Preferencias;
import es.tributasenasturias.log.ILog;

public abstract class A_MultasBase {
	
	public Preferencias _pref;
	public String _idLlamada;
	public Document _datosEntrada;
	public ILog _log;
	
	public A_MultasBase(Preferencias pref, String idLlamada, Document datosEntrada, ILog log) {
		this._idLlamada = idLlamada;
		this._pref = pref;
		this._datosEntrada = datosEntrada;
		this._log = log;
	}
	
	public abstract String Inicializar();
}
