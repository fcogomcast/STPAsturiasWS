package es.stpa.servicios.clave.sp;

		
import es.stpa.servicios.clave.preferencias.Preferencias;
import eu.eidas.auth.engine.ProtocolEngineFactoryNoMetadata;
import eu.eidas.auth.engine.ProtocolEngineNoMetadataI;
import eu.eidas.auth.engine.configuration.ProtocolConfigurationAccessorNoMetadata;
import eu.eidas.auth.engine.configuration.SamlEngineConfigurationException;
import eu.eidas.auth.engine.configuration.dom.ProtocolEngineConfigurationFactoryNoMetadata;
import eu.eidas.util.Preconditions;

/**
 * Sp ProtocolEngineFactory
 */
public final class SpProtocolEngineFactory extends ProtocolEngineFactoryNoMetadata {

	/**
     * Initialization-on-demand holder idiom.
     * <p/>
     * See item 71 of Effective Java 2nd Edition.
     * <p/>
     * See http://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom.
     */
    /*private static final class LazyHolder {

        private static final SpProtocolEngineFactory DEFAULT_SAML_ENGINE_FACTORY;

        private static final Exception INITIALIZATION_EXCEPTION;

        static {
            Exception initializationException = null;
            SpProtocolEngineFactory defaultProtocolEngineFactory = null;
            try {
                ProtocolEngineConfigurationFactoryNoMetadata protocolEngineConfigurationFactory = 
                		new ProtocolEngineConfigurationFactoryNoMetadata(Constants.SP_SAMLENGINE_FILE, null,
                				SPUtil.getConfigFilePath());

                defaultProtocolEngineFactory =
                        new SpProtocolEngineFactory(protocolEngineConfigurationFactory);
            } catch (Exception ex) {
                initializationException = ex;

            }
            DEFAULT_SAML_ENGINE_FACTORY = defaultProtocolEngineFactory;
            INITIALIZATION_EXCEPTION = initializationException;
        }

        static SpProtocolEngineFactory getDefaultSamlEngineFactory() {
            if (null == INITIALIZATION_EXCEPTION) {
                return DEFAULT_SAML_ENGINE_FACTORY;
            } else {
                throw new IllegalStateException(INITIALIZATION_EXCEPTION);
            }
        }
    }
    

    public static SpProtocolEngineFactory getInstance() {
        return LazyHolder.getDefaultSamlEngineFactory();
    }
    */
	//Debido a que nos interesa tener parametrizado el fichero de configuración del motor SAML, 
	//lo instanciaremos de cada vez en base a las propiedades. Esto hace que sea más costoso y más lento,
	//pero si queremos que se puedan modificar las propiedades en caliente, no hay otra.
	
	public static SpProtocolEngineFactory newInstance(Preferencias pref){
		try {
            ProtocolEngineConfigurationFactoryNoMetadata protocolEngineConfigurationFactory = 
            		new ProtocolEngineConfigurationFactoryNoMetadata(pref.getSAMLEngineFile(), null,
            				pref.getDirectorioConfig());

             return new SpProtocolEngineFactory(protocolEngineConfigurationFactory);
        } catch (Exception ex) {
        	throw new IllegalStateException(ex);
        }

	}
    /**
     * Returns a default ProtocolEngine instance matching the given name retrieved from the configuration file.
     *
     * @param instanceName the instance name
     * @return the ProtocolEngine instance matching the given name retrieved from the configuration file
     */
    public static SpProtocolEngineI getSpProtocolEngine(Preferencias pref) {
        Preconditions.checkNotBlank(pref.getNombreInstanciaSP(), "instanceName");
        return (SpProtocolEngineI) newInstance(pref).getProtocolEngine(pref.getNombreInstanciaSP());
    }

    private SpProtocolEngineFactory(ProtocolEngineConfigurationFactoryNoMetadata configurationFactory)
            throws SamlEngineConfigurationException {
        super(configurationFactory);
    }

    @Override
    protected ProtocolEngineNoMetadataI createProtocolEngine(ProtocolConfigurationAccessorNoMetadata configurationAccessor) {
        return new SpProtocolEngine(configurationAccessor);
    }
}