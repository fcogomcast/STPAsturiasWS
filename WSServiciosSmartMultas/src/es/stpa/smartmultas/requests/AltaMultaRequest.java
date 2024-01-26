package es.stpa.smartmultas.requests;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.stpa.smartmultas.entidades.Imagen;
import es.stpa.smartmultas.entidades.ImagenesList;
import es.stpa.smartmultas.entidades.NotasList;
import es.stpa.smartmultas.entidades.PropiedadesList;

@XmlRootElement(name = "AltaMultaRequest")
public class AltaMultaRequest {

	@XmlElement
	private String NumeroAgenteAgmu;
	@XmlElement
	private String NifInfractor = "";
	@XmlElement
	private String NombreCompletoInfractor = "";
	@XmlElement
	private String PaisInfractor = "";
	@XmlElement
	private String CalleInfractor = "";
	@XmlElement
	private String PoblacionInfractor = "";
	@XmlElement
	private String ProvinciaInfractor = "";
	@XmlElement
	private String DistritoPostalInfractor = "";
	@XmlElement
	private String NifPropietario = "";
	@XmlElement
	private String NombreCompletoPropietario = "";
	@XmlElement
	private String PaisPropietario = "";
	@XmlElement
	private String CallePropietario = "";
	@XmlElement
	private String PoblacionPropietario = "";
	@XmlElement
	private String ProvinciaPropietario = "";
	@XmlElement
	private String DistritoPostalPropietario = "";
	@XmlElement
	private String NifConductor = "";
	@XmlElement
	private String NombreCompletoConductor = "";
	@XmlElement
	private String PaisConductor = "";
	@XmlElement
	private String CalleConductor = "";
	@XmlElement
	private String PoblacionConductor = "";
	@XmlElement
	private String ProvinciaConductor = "";
	@XmlElement
	private String DistritoPostalConductor = "";
	@XmlElement
	private String Matricula = "";
	@XmlElement
	private String Marca = "";
	@XmlElement
	private String Modelo = "";
	@XmlElement
	private String NumBoletin = "";
	@XmlElement
	private String NumExpediente = "";
	@XmlElement
	private Integer CodigoArmu;
	@XmlElement
	private String HechoDenunciado;
	@XmlElement
	private String HechoDenunciadoCooficial = "";
	@XmlElement
	private String IdTimu = "";
	@XmlElement
	private Integer IdCamu  = null;
	@XmlElement
	private Integer IdClve = null;
	@XmlElement
	private String IdTamu = "";
	@XmlElement
	private String FechaInfraccion;
	@XmlElement
	private String HoraInfraccion = "";
	@XmlElement
	private String NombreVia = null;
	@XmlElement
	private Integer NumVia = null;
	@XmlElement
	private String AmpliacionVia = "";
	@XmlElement
	private Integer Pagado;
	@XmlElement
	private String FormaPago = "";
	@XmlElement
	private String FechaCobro = null;
	@XmlElement
	private Integer Importe = null;
	@XmlElement
	private String Justificante = "";
	@XmlElement
	private Float Latitud = null;
	@XmlElement
	private Float Longitud = null;
	@XmlElement
	private String ViaPenal = "N";
	@XmlElement
	private Integer Velocidad = null;
	@XmlElement
	private Integer VelocidadMaxima = null;
	@XmlElement
	private Float VelocidadCorregida = null;
	@XmlElement
	private String DispositivoMedicion = null;
	@XmlElement
	private String Retencion = "N";
	@XmlElement
	private PropiedadesList Propiedades;
	@XmlElement
	private ImagenesList Imagenes;
	@XmlElement
	private Integer IdComa;
	@XmlElement
	private String ModificadaDireccion;
	@XmlElement
	private String IdTamu2 = "";
	@XmlElement
	private String NumeroAgenteAgmu2 = "";
	@XmlElement
	private NotasList Notas;
	@XmlElement
	private Imagen ImagenOCR;
	@XmlElement
	private String TipoDenuncia = "O";
	@XmlElement
	private String VoluntariaNombreDenunciante;
	@XmlElement
	private String VoluntariaDireccionDenunciante;
	@XmlElement
	private String VoluntariaOtrosDatosDenunciante;
	@XmlElement
	private String VoluntariaHechosComprobados;

	
	public AltaMultaRequest() { }
	

	public String getNumeroAgenteAgmu() {return NumeroAgenteAgmu; }
	public String getNifInfractor() {return NifInfractor; }
	public String getNombreCompletoInfractor() {return NombreCompletoInfractor; }
	public String getPaisInfractor() { return PaisInfractor; }
	public String getCalleInfractor() { return CalleInfractor; }
	public String getPoblacionInfractor() { return PoblacionInfractor; }
	public String getProvinciaInfractor() { return ProvinciaInfractor; }
	public String getNifPropietario() { return NifPropietario; }
	public String getNombreCompletoPropietario() { return NombreCompletoPropietario; }
	public String getDistritoPostalInfractor() { return DistritoPostalInfractor; }
	public String getPaisPropietario() { return PaisPropietario; }
	public String getCallePropietario() { return CallePropietario; }
	public String getPoblacionPropietario() { return PoblacionPropietario; }
	public String getProvinciaPropietario() { return ProvinciaPropietario; }
	public String getDistritoPostalPropietario() { return DistritoPostalPropietario; }
	public String getNifConductor() { return NifConductor; }
	public String getNombreCompletoConductor() { return NombreCompletoConductor; }
	public String getPaisConductor() { return PaisConductor; }
	public String getCalleConductor() { return CalleConductor; }
	public String getPoblacionConductor() { return PoblacionConductor; }
	public String getProvinciaConductor() { return ProvinciaConductor; }
	public String getDistritoPostalConductor() { return DistritoPostalConductor; }
	public String getMatricula() { return Matricula; }
	public String getMarca() { return Marca; }
	public String getModelo() { return Modelo; }
	public String getNumBoletin() { return NumBoletin; }
	public String getNumExpediente() { return NumExpediente; }
	public Integer getCodigoArmu() { return CodigoArmu; }
	public String getHechoDenunciado() { return HechoDenunciado; }
	public String getHechoDenunciadoCooficial() { return HechoDenunciadoCooficial; }
	public String getIdTimu() { return IdTimu; }
	public Integer getIdCamu() { return IdCamu; }
	public Integer getIdClve() { return IdClve; }
	public String getIdTamu() { return IdTamu; }
	public String getFechaInfraccion() { return FechaInfraccion; }
	public String getHoraInfraccion() { return HoraInfraccion; }
	public String getNombreVia() { return NombreVia; }
	public Integer getNumVia() { return NumVia; }
	public String getAmpliacionVia() { return AmpliacionVia; }
	public Integer getPagado() { return Pagado; }
	public String getFormaPago() { return FormaPago; }
	public String getFechaCobro() { return FechaCobro; }
	public Integer getImporte() { return Importe; }
	public Float getLatitud() { return Latitud; }
	public String getJustificante() { return Justificante; }
	public Float getLongitud() { return Longitud; }
	public String getViaPenal() { return ViaPenal; }
	public Integer getVelocidad() { return Velocidad; }
	public Integer getVelocidadMaxima() { return VelocidadMaxima; }
	public Float getVelocidadCorregida() { return VelocidadCorregida; }
	public String getDispositivoMedicion() { return DispositivoMedicion; }
	public String getRetencion() { return Retencion; }
	public Integer getIdComa() { return IdComa; }
	public String getModificadaDireccion() { return ModificadaDireccion; }
	public String getIdTamu2() { return IdTamu2; }
	public String getNumeroAgenteAgmu2() { return NumeroAgenteAgmu2; }
	public Imagen getImagenOCR() { return ImagenOCR; }
	public String getTipoDenuncia() { return TipoDenuncia; }
	public String getVoluntariaNombreDenunciante() { return VoluntariaNombreDenunciante; }
	public String getVoluntariaDireccionDenunciante() { return VoluntariaDireccionDenunciante; }
	public String getVoluntariaOtrosDatosDenunciante() { return VoluntariaOtrosDatosDenunciante; }
	public String getVoluntariaHechosComprobados() { return VoluntariaHechosComprobados; }  
	public PropiedadesList getPropiedades() { return Propiedades;}
	public ImagenesList getImagenes() { return Imagenes;}
	public NotasList getNotas() {return Notas;}
	
	
	public void setNotas(NotasList notas) { Notas = notas; }
	public void setImagenes(ImagenesList imagenes) { Imagenes = imagenes; }
	public void setNumeroAgenteAgmu(String numeroAgenteAgmu) {NumeroAgenteAgmu = numeroAgenteAgmu; }
	public void setNifInfractor(String nifInfractor) {NifInfractor = nifInfractor; }
	public void setNombreCompletoInfractor(String nombreCompletoInfractor) {NombreCompletoInfractor = nombreCompletoInfractor; }
	public void setPaisInfractor(String paisInfractor) {PaisInfractor = paisInfractor; }
	public void setCalleInfractor(String calleInfractor) {CalleInfractor = calleInfractor; }
	public void setPoblacionInfractor(String poblacionInfractor) {PoblacionInfractor = poblacionInfractor; } 
	public void setProvinciaInfractor(String provinciaInfractor) { ProvinciaInfractor = provinciaInfractor; }
	public void setDistritoPostalInfractor(String distritoPostalInfractor) {DistritoPostalInfractor = distritoPostalInfractor; }
	public void setNifPropietario(String nifPropietario) {NifPropietario = nifPropietario; }
	public void setNombreCompletoPropietario(String nombreCompletoPropietario) {NombreCompletoPropietario = nombreCompletoPropietario; } 
	public void setPaisPropietario(String paisPropietario) {PaisPropietario = paisPropietario; }
	public void setCallePropietario(String callePropietario) {CallePropietario = callePropietario; }
	public void setPoblacionPropietario(String poblacionPropietario) {PoblacionPropietario = poblacionPropietario; }
	public void setProvinciaPropietario(String provinciaPropietario) {ProvinciaPropietario = provinciaPropietario; }
	public void setDistritoPostalPropietario(String distritoPostalPropietario) {DistritoPostalPropietario = distritoPostalPropietario; }
	public void setNifConductor(String nifConductor) {NifConductor = nifConductor; }
	public void setNumExpediente(String numExpediente) {NumExpediente = numExpediente; } 
	public void setNombreCompletoConductor(String nombreCompletoConductor) {NombreCompletoConductor = nombreCompletoConductor; }
	public void setPaisConductor(String paisConductor) {PaisConductor = paisConductor; }
	public void setCalleConductor(String calleConductor) {CalleConductor = calleConductor; }
	public void setPoblacionConductor(String poblacionConductor) {PoblacionConductor = poblacionConductor; }
	public void setProvinciaConductor(String provinciaConductor) {ProvinciaConductor = provinciaConductor; }
	public void setDistritoPostalConductor(String distritoPostalConductor) {DistritoPostalConductor = distritoPostalConductor; }
	public void setMatricula(String matricula) {Matricula = matricula; }
	public void setMarca(String marca) {Marca = marca; }
	public void setModelo(String modelo) {Modelo = modelo; }
	public void setNumBoletin(String numBoletin) {NumBoletin = numBoletin; }
	public void setCodigoArmu(Integer codigoArmu) {CodigoArmu = codigoArmu; }
	public void setHechoDenunciado(String hechoDenunciado) {HechoDenunciado = hechoDenunciado; }
	public void setHechoDenunciadoCooficial(String hechoDenunciadoCooficial) {HechoDenunciadoCooficial = hechoDenunciadoCooficial; }
	public void setIdTimu(String idTimu) {IdTimu = idTimu; }
	public void setIdCamu(Integer idCamu) {IdCamu = idCamu; }
	public void setIdClve(Integer idClve) {IdClve = idClve; }
	public void setIdTamu(String idTamu) {IdTamu = idTamu; }
	public void setFechaInfraccion(String fechaInfraccion) {FechaInfraccion = fechaInfraccion; }
	public void setHoraInfraccion(String horaInfraccion) {HoraInfraccion = horaInfraccion; }
	public void setNombreVia(String nombreVia) {NombreVia = nombreVia; }
	public void setNumVia(Integer numVia) {NumVia = numVia; }
	public void setAmpliacionVia(String ampliacionVia) {AmpliacionVia = ampliacionVia; }
	public void setPagado(Integer pagado) {Pagado = pagado; }
	public void setFormaPago(String formaPago) {FormaPago = formaPago; }
	public void setFechaCobro(String fechaCobro) {FechaCobro = fechaCobro; }
	public void setImporte(Integer importe) {Importe = importe; }
	public void setJustificante(String justificante) {Justificante = justificante; }
	public void setLatitud(Float latitud) {Latitud = latitud; }
	public void setLongitud(Float longitud) {Longitud = longitud; }
	public void setViaPenal(String viaPenal) {ViaPenal = viaPenal; }
	public void setVelocidad(Integer velocidad) {Velocidad = velocidad; }
	public void setVelocidadMaxima(Integer velocidadMaxima) {VelocidadMaxima = velocidadMaxima; }
	public void setVelocidadCorregida(Float velocidadCorregida) {VelocidadCorregida = velocidadCorregida; }
	public void setDispositivoMedicion(String dispositivoMedicion) {DispositivoMedicion = dispositivoMedicion; }
	public void setRetencion(String retencion) {Retencion = retencion; }
	public void setIdComa(Integer idComa) {IdComa = idComa; }
	public void setModificadaDireccion(String modificadaDireccion) {ModificadaDireccion = modificadaDireccion; }
	public void setIdTamu2(String idTamu2) {IdTamu2 = idTamu2; }
	public void setNumeroAgenteAgmu2(String numeroAgenteAgmu2) {NumeroAgenteAgmu2 = numeroAgenteAgmu2; }
	public void setPropiedades(PropiedadesList propiedades) { Propiedades = propiedades;}
	public void setImagenOCR(Imagen imagenOCR) {ImagenOCR = imagenOCR; }
	public void setTipoDenuncia(String tipoDenuncia) {TipoDenuncia = tipoDenuncia; }
	public void setVoluntariaNombreDenunciante(String voluntariaNombreDenunciante) {VoluntariaNombreDenunciante = voluntariaNombreDenunciante; }
	public void setVoluntariaDireccionDenunciante(String voluntariaDireccionDenunciante) {VoluntariaDireccionDenunciante = voluntariaDireccionDenunciante; }
	public void setVoluntariaOtrosDatosDenunciante(String voluntariaOtrosDatosDenunciante) {VoluntariaOtrosDatosDenunciante = voluntariaOtrosDatosDenunciante; }
	public void setVoluntariaHechosComprobados(String voluntariaHechosComprobados) {VoluntariaHechosComprobados = voluntariaHechosComprobados; }
}
