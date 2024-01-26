package es.stpa.smartmultas.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ExpedientePdtRatificar")
public class ExpedientePdtRatificar {


	private String NumExp;

	private String ModeloVehiculo;

	private String Matricula;	

	private String Gdre;	

	private String Gdel;	

	private String Tipo;

	private String Marca;

	private String IdEperPropietario;

	private String PropietarioNombre;

	private String IdEper;

	private String Nif;

	private String Pdf;

	
	
	public ExpedientePdtRatificar() { }
	
	public ExpedientePdtRatificar(String numExp, String modeloVehiculo,
			String matricula, String marca, String nif, String tipo,
			String idEper, String idEperPropietario, String gdel, String gdre, 
			String propietarioNombre) 
	{
		super();
		NumExp = numExp;
		ModeloVehiculo = modeloVehiculo;
		Matricula = matricula;
		Gdre = gdre;
		Gdel = gdel;
		Tipo = tipo;
		Marca = marca;
		IdEperPropietario = idEperPropietario;
		PropietarioNombre = propietarioNombre;
		IdEper = idEper;
		Nif = nif;
	}

	@XmlElement(name="NumExp")
	public String getNumExp() { return NumExp; }

	@XmlElement(name="ModeloVehiculo")
	public String getModeloVehiculo() { return ModeloVehiculo; }

	@XmlElement(name="Matricula")
	public String getMatricula() { return Matricula; }

	@XmlElement(name="Gdre")
	public String getGdre() { return Gdre; }

	@XmlElement(name="Gdel")
	public String getGdel() { return Gdel; }

	@XmlElement(name="Tipo")
	public String getTipo() { return Tipo; }

	@XmlElement(name="Marca")
	public String getMarca() { return Marca; }

	@XmlElement(name="IdEperPropietario")
	public String getIdEperPropietario() { return IdEperPropietario; }

	@XmlElement(name="PropietarioNombre")
	public String getPropietarioNombre() { return PropietarioNombre; }

	@XmlElement(name="IdEper")
	public String getIdEper() { return IdEper;}

	@XmlElement(name="Nif")
	public String getNif() { return Nif; }

	@XmlElement(name="pdf")
	public String getPdf() { return Pdf; }

	
	
	public void setNumExp(String NumExp) {
		this.NumExp = NumExp;
	}

	public void setModeloVehiculo(String ModeloVehiculo) {
		this.ModeloVehiculo = ModeloVehiculo;
	}

	public void setMatricula(String Matricula) {
		this.Matricula = Matricula;
	}

	public void setGdre(String Gdre) {
		this.Gdre = Gdre;
	}

	public void setGdel(String Gdel) {
		this.Gdel = Gdel;
	}

	public void setTipo(String Tipo) {
		this.Tipo = Tipo;
	}

	public void setMarca(String Marca) {
		this.Marca = Marca;
	}

	public void setIdEperPropietario(String IdEperPropietario) {
		this.IdEperPropietario = IdEperPropietario;
	}

	public void setPropietarioNombre(String PropietarioNombre) {
		this.PropietarioNombre = PropietarioNombre;
	}

	public void setIdEper(String IdEper) {
		this.IdEper = IdEper;
	}

	public void setNif(String Nif) {
		this.Nif = Nif;
	}

	public void setPdf(String Pdf) {
		this.Pdf = Pdf;
	}
}
