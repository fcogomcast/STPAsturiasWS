package es.stpa.smartmultas.configuracion.ATEX;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class TipoVehiculo {

    private String CodigoVehiculo;
    private String DecripcionVehiculo;
    
    
	public TipoVehiculo(String codigoVehiculo, String decripcionVehiculo) {
		CodigoVehiculo = codigoVehiculo;
		DecripcionVehiculo = decripcionVehiculo;
	}
	
	
	/**
	 * @return the codigoVehiculo
	 */
	public String getCodigoVehiculo() {
		return CodigoVehiculo;
	}
	/**
	 * @param codigoVehiculo the codigoVehiculo to set
	 */
	public void setCodigoVehiculo(String codigoVehiculo) {
		CodigoVehiculo = codigoVehiculo;
	}
	/**
	 * @return the decripcionVehiculo
	 */
	public String getDecripcionVehiculo() {
		return DecripcionVehiculo;
	}
	/**
	 * @param decripcionVehiculo the decripcionVehiculo to set
	 */
	public void setDecripcionVehiculo(String decripcionVehiculo) {
		DecripcionVehiculo = decripcionVehiculo;
	}
    
    
	
}
