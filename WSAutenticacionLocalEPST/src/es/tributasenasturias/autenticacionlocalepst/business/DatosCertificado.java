package es.tributasenasturias.autenticacionlocalepst.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.tributasenasturias.autenticacionlocalepst.utils.Constantes;

/**
 * Datos que interesan del certificado, tal como están almacenados de manera local
 * @author crubencvs
 *
 */
public class DatosCertificado {
	
	public static final String FISICO="F";
	public static final String JURIDICO="J";
	private String identificadorFiscal;
	private boolean esValido;
	private Calendar fechaCaducidad;
	private Calendar fechaUltimaComprobacion;
	private String nombre;
	private String razonSocial;
	private String apellido1;
	private String apellido2;
	private String tipo;
	// CRUBENCVS 47535
	private String periodicidadComprobacion;
	public String getIdentificadorFiscal() {
		return identificadorFiscal;
	}
	public void setIdentificadorFiscal(String identificadorFiscal) {
		this.identificadorFiscal = identificadorFiscal;
	}
	public boolean isValido() {
		return esValido;
	}
	public void setEsValido(boolean esValido) {
		this.esValido = esValido;
	}
	public Calendar getFechaCaducidad() {
		return fechaCaducidad;
	}
	public void setFechaCaducidad(String fechaCaducidad) throws ParseException{
		if ("".equals(fechaCaducidad)) {
			return;
		}
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cl= Calendar.getInstance(Constantes.local);
		cl.setTime(sf.parse(fechaCaducidad));
		this.fechaCaducidad = cl;
	}
	public Calendar getFechaUltimaComprobacion() {
		return fechaUltimaComprobacion;
	}
	public void setFechaUltimaComprobacion(String fechaUltimaComprobacion) throws ParseException{
		if ("".equals(fechaUltimaComprobacion)) {
			return;
		}
		SimpleDateFormat sf= new SimpleDateFormat("dd/MM/yyyy");
		Calendar cl= Calendar.getInstance(Constantes.local);
		cl.setTime(sf.parse(fechaUltimaComprobacion));
		this.fechaUltimaComprobacion = cl;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getApellido1() {
		return apellido1;
	}
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	public String getApellido2() {
		return apellido2;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getPeriodicidadComprobacion() {
		return periodicidadComprobacion;
	}
	public void setPeriodicidadComprobacion(String periodicidadComprobacion) {
		this.periodicidadComprobacion = periodicidadComprobacion;
	}
    
	
}
