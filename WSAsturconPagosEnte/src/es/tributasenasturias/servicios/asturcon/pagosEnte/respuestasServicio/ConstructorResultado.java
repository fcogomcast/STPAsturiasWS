package es.tributasenasturias.servicios.asturcon.pagosEnte.respuestasServicio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.PropertyResourceBundle;

import es.tributasenasturias.servicios.asturcon.pagosEnte.RESULTADOType;
import es.tributasenasturias.servicios.asturcon.pagosEnte.context.CallContext;
import es.tributasenasturias.servicios.asturcon.pagosEnte.context.IContextReader;
import es.tributasenasturias.servicios.asturcon.pagosEnte.preferencias.Preferencias;
import es.tributasenasturias.servicios.asturcon.pagosEnte.preferencias.PreferenciasException;
import es.tributasenasturias.servicios.asturcon.pagosEnte.preferencias.PreferenciasFactory;

public class ConstructorResultado implements IContextReader{
	private CallContext context;
	private PropertyResourceBundle prop;
	//Sólo soportamos por ahora español, aunque esta propiedad, por el momento, no la usamos
	//private Locale localES = new Locale("es");
	private String ficheroRecursos;
	protected ConstructorResultado(CallContext context) throws RespuestasException{
		try
		{
			Preferencias pref= PreferenciasFactory.getPreferenciasContexto(context);
			ficheroRecursos=pref.getRutaFicheroMensajes();
			prop = new PropertyResourceBundle(new BufferedInputStream(new FileInputStream(ficheroRecursos)));
		}
		catch (PreferenciasException ex)
		{
			throw new RespuestasException ("Error al recuperar los textos de los mensajes:"+ex.getMessage(),ex);
		}catch (IOException ex)
		{
			throw new RespuestasException ("Error al leer de disco para recuperar los textos de los mensajes:"+ex.getMessage(),ex);
		}
	}
	public RESULTADOType getResultadoOK()
	{
		RESULTADOType resultado = new RESULTADOType();
		resultado.setCODIGO(prop.getString("msg.resultado.OK.code"));
		resultado.setDESCRIPCION(prop.getString("msg.resultado.OK.desc"));
		return resultado;
	}
	public RESULTADOType getResultadoDuplicada()
	{
		RESULTADOType resultado = new RESULTADOType();
		resultado.setCODIGO(prop.getString("msg.resultado.duplicada.code"));
		resultado.setDESCRIPCION(prop.getString("msg.resultado.duplicada.desc"));
		return resultado;
	}
	public RESULTADOType getResultadoError(String razon)
	{
		String descripcion="";
		RESULTADOType resultado = new RESULTADOType();
		resultado.setCODIGO(prop.getString("msg.resultado.error.code"));
		descripcion=prop.getString("msg.resultado.error.desc");
		if (razon!=null && !"".equals(razon))
		{
			descripcion = descripcion + "Razón:"+razon;
		}
		resultado.setDESCRIPCION(descripcion);
		return resultado;
	}

	@Override
	public CallContext getCallContext() {
		return context;
	}
	@Override
	public void setCallContext(CallContext ctx) {
		context= ctx;
	}
	
}
