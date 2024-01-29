package es.tributasenasturias.webservices.Certificados.validacion.PermisoServicio;


import es.tributasenasturias.webservices.Certificados.Exceptions.DatosException;
import es.tributasenasturias.webservices.Certificados.General.IResultado;
import es.tributasenasturias.webservices.Certificados.General.InfoResultado;
import es.tributasenasturias.webservices.Certificados.bd.Datos;
import es.tributasenasturias.webservices.Certificados.validacion.IValidator;

public class PermisoServicioValidator implements IValidator<String>{
	
	InfoResultado res;
	private Datos bd=null;
	private java.util.Map<String,String> resBD= null;
	TInfoPermisoServicio infoPerm=null;
	public PermisoServicioValidator()
	{
		res = new InfoResultado();
	}
	@Override
	public IResultado getResultado() {
		return res;
	}

	@Override
	public boolean isValid(String id) {
		boolean valido=false;
		try
		{
			//Validamos el Id que nos han pasado.
			bd=new Datos();
			resBD= bd.permisoServicio(id);
			infoPerm = new TInfoPermisoServicio(resBD.get("AUTORIZACION"),resBD.get("TIPO"));
			String autorizacion = infoPerm.getAutorizacion();
			if (autorizacion.equalsIgnoreCase("01"))
			{
				res.addMessage("No está autorizado para utilizar el servicio");
			}
			else if (autorizacion.equalsIgnoreCase("02"))
			{
				res.addMessage("Error en consulta de la base de datos");
			}
			else 
			{
				valido=true;
			}
		}
		catch (DatosException ex)
		{
			res.addMessage(ex.getMessage()+"-"+ex.getCause().getMessage());
			valido=false;
		}
		return valido;
	}
	/**
	 * Recupera datos extra acerca del permiso.
	 * @return Datos del permiso.
	 */

	public TInfoPermisoServicio getInfoPermisoServicio()
	{
		return infoPerm;
	}


}
