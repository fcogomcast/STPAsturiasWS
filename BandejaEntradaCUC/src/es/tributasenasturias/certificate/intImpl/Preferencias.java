package es.tributasenasturias.certificate.intImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import es.tributasenasturias.certificate.interfaces.IPropertiesProvider;

public class Preferencias implements IPropertiesProvider {
	
	//Nombres de las preferencias.
	private enum PrefNames {ALMACEN,CLAVE,ENDPOINT};
	
	private Preferences mPreferencias;

    //constantes para trabajar con las preferencias
    private final static String FICHERO_PREFERENCIAS = "prefsBandejaEntradaCUC.xml";
    private final static String DIRECTORIO_PREFERENCIAS = "BandejaEntradaCUC";
    private final static String NOMBRE_PREF_ALMACEN = PrefNames.ALMACEN.toString();
    private final static String NOMBRE_PREF_PASSWORD = PrefNames.CLAVE.toString();
    private final static String NOMBRE_PREF_ENDPOINT = PrefNames.ENDPOINT.toString();
    //FIXME: Eliminar en explotación.
    private final static String VALOR_INICIAL_PREF_ALMACEN = "Localización de almacén de claves";
    private final static String VALOR_INICIAL_PREF_PASSWORD = "Clave del almacén";
    private final static String VALOR_INICIAL_PREF_ENDPOINT = "http://fake.com";
    //Tabla de preferencias
    private Map<String,String>tablaPreferencias =  new HashMap<String,String>();
    /** Creación del fichero de preferencias, en caso de que no exista.
     * 
     */
    private void CrearFicheroPreferencias()
    {
        //preferencias por defecto
        mPreferencias = Preferences.systemNodeForPackage(this.getClass());
        mPreferencias.put(NOMBRE_PREF_ALMACEN, VALOR_INICIAL_PREF_ALMACEN);
        mPreferencias.put(NOMBRE_PREF_PASSWORD, VALOR_INICIAL_PREF_PASSWORD);
        mPreferencias.put(NOMBRE_PREF_ENDPOINT, VALOR_INICIAL_PREF_ENDPOINT);

        FileOutputStream outputStream = null;
        File fichero;
        try
        {
            fichero = new File(DIRECTORIO_PREFERENCIAS);
            if(fichero.exists() == false)
            {
                fichero.mkdirs();
            }
            
            outputStream = new FileOutputStream(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
            mPreferencias.exportNode(outputStream);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(outputStream != null)
                {
                    outputStream.close();
                }
            }
            catch(Exception e)
            {
                System.out.println("Error cerrando fichero de preferencias -> " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    /** Carga del fichero de preferencias
     * 
     * @throws Exception
     */
	private void CargarPreferencias() throws Exception
    {
        File f = new File(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
        if (f.exists())
        {
            //si existe el fichero de preferencias lo cargamos
            FileInputStream inputStream = new FileInputStream(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
            Preferences.importPreferences(inputStream);
            inputStream.close();

            mPreferencias = Preferences.systemNodeForPackage(this.getClass());
            String mAlmacen = mPreferencias.get(NOMBRE_PREF_ALMACEN, "");
            String mPassword = mPreferencias.get(NOMBRE_PREF_PASSWORD, "");
            String mEndpoint = mPreferencias.get(NOMBRE_PREF_ENDPOINT, "");
            tablaPreferencias.put(PrefNames.ALMACEN.toString(), mAlmacen);
            tablaPreferencias.put(PrefNames.CLAVE.toString(),mPassword );
            tablaPreferencias.put(PrefNames.ENDPOINT.toString(),mEndpoint );

            //chequeo de valores por defecto
            if (mAlmacen.equals(VALOR_INICIAL_PREF_ALMACEN) || mPassword.equals(VALOR_INICIAL_PREF_PASSWORD))
            {
                //todavia no las ha inicializado en el fichero
                throw new Exception("Debe especificar primero las preferencias en el fichero: " + f.getAbsolutePath() + " (parar el servicio)");
            }
        }
        else
        {
            //si no existe el fichero de preferencias lo crearemos
            CrearFicheroPreferencias();

            throw new Exception("Debe especificar primero las preferencias en el fichero: " + f.getAbsolutePath() + " (parar el servicio)");
        }
    }
	private String getValueFromTablaPreferencias(String key)
    {
    	String toReturn="";
    	
    	if(tablaPreferencias.containsKey(key))
    	{
    		toReturn = tablaPreferencias.get(key);
    	}
    	
    	//Logger.debug("Se ha pedido la preferencia '"+key+"' a lo que el sistema devuelve '"+toReturn+"'");
    	
    	return toReturn;
    }
	/** Constructor
	 * 
	 */
	public Preferencias()
	{
		// Por defecto.
	}
	/**Recupera el nombre del almacén guardado en preferencias.
	 * 
	 * @return Cadena que contiene el nombre de almacén.
	 */
	public final String getNombreAlmacen()
	{
		return getValueFromTablaPreferencias(PrefNames.ALMACEN.toString());
	}
	/**Recupera la clave del almacén guardada en preferencias
	 * 
	 * @return Clave del almacén.
	 */
	public final String getClaveAlmacen()
	{
		return getValueFromTablaPreferencias(PrefNames.CLAVE.toString());
	}
	/**Recupera el nombre del almacén guardado en preferencias.
	 * 
	 * @return Cadena que contiene el nombre de almacén.
	 */
	public final String getEndPoint()
	{
		return getValueFromTablaPreferencias(PrefNames.ENDPOINT.toString());
	}
    /** Recarga las preferencias.
     * 
     */
	@Override
	public void refreshPreferences() throws Exception{
		CargarPreferencias();
	}

}
