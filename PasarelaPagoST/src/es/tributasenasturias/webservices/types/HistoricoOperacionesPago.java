package es.tributasenasturias.webservices.types;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

public class HistoricoOperacionesPago {

	public static class AnulacionRealizada{
		private String fecha;
		private String importe;

		/**
		 * @return the fecha
		 */
		public final String getFecha() {
			return fecha;
		}

		/**
		 * @param fecha the fecha to set
		 */
		public final void setFecha(String fecha) {
			this.fecha = fecha;
		}

		/**
		 * @return the importe
		 */
		public final String getImporte() {
			return importe;
		}

		/**
		 * @param importe the importe to set
		 */
		public final void setImporte(String importe) {
			this.importe = importe;
		}
		
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(propOrder = {
			"identificador",
		    "fecha",
		    "resultado",
		    "anulaciones"
	})
	public static class Operacion{
		private String identificador;
		private String fecha;
		private String resultado;
		@XmlElementWrapper(name="anulacionesRealizadas")
	    @XmlElement(name="anulacion")
		private List<AnulacionRealizada> anulaciones = new ArrayList<AnulacionRealizada>();
		/**
		 * @return the fecha
		 */
		public final String getFecha() {
			return fecha;
		}
		/**
		 * @param fecha the fecha to set
		 */
		public final void setFecha(String fecha) {
			this.fecha = fecha;
		}
		/**
		 * @return the resultado
		 */
		public final String getResultado() {
			return resultado;
		}
		/**
		 * @param resultado the resultado to set
		 */
		public final void setResultado(String resultado) {
			this.resultado = resultado;
		}
		/**
		 * @return the anulaciones
		 */
		public final List<AnulacionRealizada> getAnulaciones() {
			return anulaciones;
		}
		/**
		 * @param anulaciones the anulaciones to set
		 */
		public final void setAnulaciones(List<AnulacionRealizada> anulaciones) {
			this.anulaciones = anulaciones;
		}
		/**
		 * @return the identificador
		 */
		public final String getIdentificador() {
			return identificador;
		}
		/**
		 * @param identificador the identificador to set
		 */
		public final void setIdentificador(String identificador) {
			this.identificador = identificador;
		}
		
	}
	@XmlElement(name="operacion")
	private List<Operacion> operaciones = new ArrayList<Operacion>();

	/**
	 * @return the operacion
	 */
	public final List<Operacion> getListaOperaciones() {
		return operaciones;
	}

	/**
	 * @param operaciones the operaciones to set
	 */
	public final void setListaOperacion(List<Operacion> operaciones) {
		this.operaciones = operaciones;
	}
	
}
