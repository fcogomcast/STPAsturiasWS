package es.stpa.smartmultas.entidades;

import javax.xml.bind.annotation.XmlElement;


public class EtiquetasBoletin {
	
	
	private String Agente;
	
    private String AgenteDenunciante;
	
    private String AgenteTestigo;
	
    private String ApartadoEntidad;
	
    private String ApartadoInfractor;
	
    private String Boletin;
	
    private String Calificacion;
	
    private String Clase;
	
    private String CodigoProcedimiento;
	
    private String DatosDenunciante;
	
    private String DatosInfraccion;
	
    private String DatosInfractor;

    private String DatosVehiculo;

    private String Detraccion;

    private String DigitoControl;

    private String DNI;
	
    private String Domicilio;
	
    private String Emisora;
	
    private String Expediente;
	
    private String Fecha;
	
    private String FechaDenuncia;
	
    private String FirmaInfractor;
	
    private String Hecho;
	
    private String HechosComprobados;
	
    private String Hora;
	
    private String Identificacion;
	
    private String Importe;
	
    private String ImporteIngresar;
	
    private String ImporteReducido;
	
    private String Justificante;
	
    private String Lugar;
	
    private String Marca;
	
    private String Matricula;
	
    private String Modelo;
	
    private String Modo;
	
    private String Nombre;
	
    private String Numero;
	
    private String OtrosDatos;
	
    private String Precepto;
	
    private String ReferenciaCobro;
	
    private String UltimoDia;
      
    
    public EtiquetasBoletin() { }

    
    @XmlElement(name = "Agente")
	public String getAgente() { return Agente; }
    
    @XmlElement(name = "AgenteDenunciante")
	public String getAgenteDenunciante() { return AgenteDenunciante; }

    @XmlElement(name = "AgenteTestigo")
	public String getAgenteTestigo() { return AgenteTestigo; }

    @XmlElement(name = "ApartadoEntidad")
	public String getApartadoEntidad() { return ApartadoEntidad; }

    @XmlElement(name = "ApartadoInfractor")
	public String getApartadoInfractor() { return ApartadoInfractor; }

    @XmlElement(name = "Boletin")
	public String getBoletin() { return Boletin; }

    @XmlElement(name = "Calificacion")
	public String getCalificacion() { return Calificacion; }

    @XmlElement(name = "Clase")
	public String getClase() { return Clase; }

    @XmlElement(name = "CodigoProcedimiento")
	public String getCodigoProcedimiento() { return CodigoProcedimiento; }

    @XmlElement(name = "DatosDenunciante")
	public String getDatosDenunciante() { return DatosDenunciante; }

    @XmlElement(name = "DatosInfraccion")
	public String getDatosInfraccion() { return DatosInfraccion; }

    @XmlElement(name = "DatosInfractor")
	public String getDatosInfractor() { return DatosInfractor; }

    @XmlElement(name = "DatosVehiculo")
	public String getDatosVehiculo() { return DatosVehiculo; }

    @XmlElement(name = "Detraccion")
	public String getDetraccion() { return Detraccion; }

    @XmlElement(name = "DigitoControl")
	public String getDigitoControl() { return DigitoControl; }

    @XmlElement(name = "DNI")
	public String getDNI() { return DNI; }

    @XmlElement(name = "Domicilio")
	public String getDomicilio() { return Domicilio; }

    @XmlElement(name = "Emisora")
	public String getEmisora() { return Emisora; }

    @XmlElement(name = "Expediente")
	public String getExpediente() { return Expediente; }

    @XmlElement(name = "Fecha")
	public String getFecha() { return Fecha; }

    @XmlElement(name = "FechaDenuncia")
	public String getFechaDenuncia() { return FechaDenuncia; }

    @XmlElement(name = "FirmaInfractor")
	public String getFirmaInfractor() { return FirmaInfractor; }

    @XmlElement(name = "Hecho")
	public String getHecho() { return Hecho; }

    @XmlElement(name = "HechosComprobados")
	public String getHechosComprobados() { return HechosComprobados; }

    @XmlElement(name = "Hora")
	public String getHora() { return Hora; }

    @XmlElement(name = "Identificacion")
	public String getIdentificacion() { return Identificacion; }

    @XmlElement(name = "Importe")
	public String getImporte() { return Importe; }

    @XmlElement(name = "ImporteIngresar")
	public String getImporteIngresar() { return ImporteIngresar; }

    @XmlElement(name = "ImporteReducido")
	public String getImporteReducido() { return ImporteReducido; }

    @XmlElement(name = "Justificante")
	public String getJustificante() {return Justificante; }

    @XmlElement(name = "Lugar")
	public String getLugar() { return Lugar; }

    @XmlElement(name = "Marca")
	public String getMarca() { return Marca; }

    @XmlElement(name = "Matricula")
	public String getMatricula() { return Matricula; }

    @XmlElement(name = "Modelo")
	public String getModelo() { return Modelo; }

    @XmlElement(name = "Modo")
	public String getModo() { return Modo; }

    @XmlElement(name = "Nombre")
	public String getNombre() { return Nombre; }

    @XmlElement(name = "Numero")
	public String getNumero() { return Numero; }
	
    @XmlElement(name = "OtrosDatos")
	public String getOtrosDatos() { return OtrosDatos; }
	
    @XmlElement(name = "Precepto")
	public String getPrecepto() { return Precepto; }
	
    @XmlElement(name = "ReferenciaCobro")
	public String getReferenciaCobro() { return ReferenciaCobro; }

    @XmlElement(name = "UltimoDia")
	public String getUltimoDia() { return UltimoDia; }
	
    
	
	public void setReferenciaCobro(String referenciaCobro) {ReferenciaCobro = referenciaCobro; }
	public void setPrecepto(String precepto) {Precepto = precepto; }
	public void setOtrosDatos(String otrosDatos) {OtrosDatos = otrosDatos; }
	public void setNumero(String numero) {Numero = numero; }
	public void setNombre(String nombre) {Nombre = nombre; }
	public void setModo(String modo) {Modo = modo; }
	public void setModelo(String modelo) {Modelo = modelo; }
	public void setMatricula(String matricula) {Matricula = matricula; }
	public void setMarca(String marca) {Marca = marca; }
	public void setLugar(String lugar) {Lugar = lugar; }
	public void setJustificante(String justificante) {Justificante = justificante; }
	public void setImporteReducido(String importeReducido) {ImporteReducido = importeReducido; }
	public void setImporteIngresar(String importeIngresar) {ImporteIngresar = importeIngresar; }
	public void setImporte(String importe) {Importe = importe; }
	public void setIdentificacion(String identificacion) {Identificacion = identificacion; }
	public void setHora(String hora) {Hora = hora; }
	public void setHechosComprobados(String hechosComprobados) {HechosComprobados = hechosComprobados; }
	public void setHecho(String hecho) {Hecho = hecho; }
	public void setFirmaInfractor(String firmaInfractor) {FirmaInfractor = firmaInfractor; }
	public void setFechaDenuncia(String fechaDenuncia) {FechaDenuncia = fechaDenuncia; }
	public void setFecha(String fecha) {Fecha = fecha; }
	public void setExpediente(String expediente) {Expediente = expediente; }
	public void setEmisora(String emisora) {Emisora = emisora; }
	public void setDomicilio(String domicilio) {Domicilio = domicilio; }
	public void setDNI(String dni) {DNI = dni; }
	public void setDigitoControl(String digitoControl) {DigitoControl = digitoControl; }
	public void setDetraccion(String detraccion) {Detraccion = detraccion; }
	public void setDatosVehiculo(String datosVehiculo) {DatosVehiculo = datosVehiculo; }
	public void setDatosInfractor(String datosInfractor) {DatosInfractor = datosInfractor; }
	public void setDatosInfraccion(String datosInfraccion) {DatosInfraccion = datosInfraccion; }
	public void setDatosDenunciante(String datosDenunciante) {DatosDenunciante = datosDenunciante; }
	public void setCodigoProcedimiento(String codigoProcedimiento) {CodigoProcedimiento = codigoProcedimiento; }
	public void setClase(String clase) {Clase = clase; }
	public void setCalificacion(String calificacion) {Calificacion = calificacion; }
	public void setBoletin(String boletin) {Boletin = boletin; }
	public void setApartadoInfractor(String apartadoInfractor) {ApartadoInfractor = apartadoInfractor; }
	public void setApartadoEntidad(String apartadoEntidad) {ApartadoEntidad = apartadoEntidad; }
	public void setAgente(String agente) { Agente = agente; }
	public void setAgenteDenunciante(String agenteDenunciante) { AgenteDenunciante = agenteDenunciante; }
	public void setAgenteTestigo(String agenteTestigo) {AgenteTestigo = agenteTestigo; }
	public void setUltimoDia(String ultimoDia) {UltimoDia = ultimoDia; }
}

