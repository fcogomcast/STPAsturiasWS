package es.tributasenasturias.services.ws.wsmultas;
import javax.xml.datatype.XMLGregorianCalendar;

import es.tributasenasturias.services.ws.wsmultas.bd.Datos;
import es.tributasenasturias.services.ws.wsmultas.utils.Respuesta;
import es.tributasenasturias.services.ws.wsmultas.utils.Log.Logger;
import es.tributasenasturias.services.ws.wsmultas.utils.Preferencias.Preferencias;

public class AltaMulta {

	Datos data = null;
	private Preferencias preferencias;
	private Logger logger = null;

	/**
	 * Constructor por defecto
	 */
	public AltaMulta() {
		// Realizamos inicializaciones
		data = new Datos();
		logger = new Logger();
		try {
			logger.debug("INICIO CONSTRUCTOR DE AltaMulta");
			
			preferencias = Preferencias.getPreferencias();

			logger.debug("FIN CONSTRUCTOR DE AltaMulta");
		} catch (Exception ex) {
			logger.error("En constructor de clase AltaMulta: " + ex.getMessage());
		}		
	}

	public Respuesta darAltaMulta(String udid, String suborganismo,
							String numeroAgenteAgmu, String nifPropietario,
							String nombreCompletoPropietario, String paisPropietario,
							String callePropietario, String poblacionPropietario,
							String provinciaPropietario, String distritoPostalPropietario,
							String nifInfractor, String nombreCompletoInfractor,
							String paisInfractor, String calleInfractor,
							String poblacionInfractor, String provinciaInfractor,
							String distritoPostalInfractor, String nifConductor,
							String nombreCompletoConductor, String paisConductor,
							String calleConductor, String poblacionConductor,
							String provinciaConductor, String distritoPostalConductor,
							String matricula, String marca, String modelo, String numBoletin,
							String numExpediente, String codigoArmu, String hechoDenunciado,
							String hechoDenunciadoCoof, String idTimu, String idCamu,
							String idClve, IdTamuType idTamu, CadenaSN viaPenal,
							CadenaSN retencion, Double velocidad, Double velocidadMaxima,
							Double velocidadCorregida, Integer dispositivoMedicion,
							XMLGregorianCalendar fechaInfraccion, String horaInfraccion,
							String nombreVia, String numVia, String ampliacionVia,
							Double latitud, Double longitud, String pagado, String formaPago,
							XMLGregorianCalendar fechaCobro, String importe,
							String justificante, String motivoAnulacion,
							PropiedadesType propiedades, ImagenesType imagenes, String idComa,
							CadenaSN modificadaDireccion) throws Exception {

		Respuesta resultadoConsulta = new Respuesta();
		try {
			logger.debug("Inicio Ejecutar");
			String codigoUsuario = "USU_WEB_SAC";
			if (propiedades != null)
			{
				for (PropiedadType propiedad : propiedades.getPropiedades())
				{
		              if (propiedad.nombre.equals("VELOCIDAD")) velocidad = Double.parseDouble(propiedad.valor);
		              if (propiedad.nombre.equals("VELOCIDADLIMITE")) velocidadMaxima = Double.parseDouble(propiedad.valor);
		              if (propiedad.nombre.equals("VELOCIDADCORREGIDA")) velocidadCorregida = Double.parseDouble(propiedad.valor);
		              if (propiedad.nombre.equals("DISPOSITIVO")) dispositivoMedicion = Integer.parseInt(propiedad.valor);
				}
			}

			resultadoConsulta = data.ejecutaAltaMulta(udid, suborganismo,
												numeroAgenteAgmu, nifPropietario,
												nombreCompletoPropietario, paisPropietario,
												callePropietario, poblacionPropietario,
												provinciaPropietario, distritoPostalPropietario,
												nifInfractor, nombreCompletoInfractor,
												paisInfractor, calleInfractor,
												poblacionInfractor, provinciaInfractor,
												distritoPostalInfractor, nifConductor,
												nombreCompletoConductor, paisConductor,
												calleConductor, poblacionConductor,
												provinciaConductor, distritoPostalConductor,
												matricula, marca, modelo, numBoletin,
												numExpediente, codigoArmu, hechoDenunciado,
												hechoDenunciadoCoof, idTimu, idCamu,
												idClve, idTamu, viaPenal,
												retencion, velocidad, velocidadMaxima,
												velocidadCorregida, dispositivoMedicion,
												fechaInfraccion, horaInfraccion,
												nombreVia, numVia, ampliacionVia,
												latitud, longitud, pagado, formaPago,
												fechaCobro, importe,
												justificante, motivoAnulacion, propiedades,
												imagenes, idComa,
												modificadaDireccion);
			
			//Comprobamos la existencia de documento a tratar.
			if (!resultadoConsulta.getCodresultado().equals(preferencias.getCodResultadoOK())){
				logger.error("Error al dar de alta la multa: " + resultadoConsulta.getResultado());
				return resultadoConsulta;
			}
			// Recuperar el ideper del expediente de trafico para relacionarlo con las posibles imagenes
            String idEper = "";
            idEper = resultadoConsulta.getIdeper();
			if (idEper == null) idEper = "";
			// Si no ha habido errores guardar las imagenes en el archivo digital si las hubiera y tenemos expedientede trafico
			if (imagenes != null && !idEper.isEmpty())
			{
				Respuesta resultadoAltaDoc = new Respuesta();
				for (ImagenType imagen : imagenes.getImagenes())
				{
					resultadoAltaDoc = data.AltaDocumentoMultas(codigoUsuario, imagen.nombre, imagen.base64, idEper);
					
					if (!resultadoConsulta.getCodresultado().equals(preferencias.getCodResultadoOK())){
						// ha habido algun error procesando alguna imagen y salimos
						return resultadoAltaDoc;
					}
				}
			}			

			return resultadoConsulta;
		} catch (Exception ex) {
			logger.error("Error Ejecutar: "+ex.getMessage());
			resultadoConsulta.setCodresultado("98");
			resultadoConsulta.setResultado("Error Ejecutar: "+ex.getMessage());
			return resultadoConsulta;
		} finally {
			logger.debug("Fin Ejecutar");
		}
	}
}