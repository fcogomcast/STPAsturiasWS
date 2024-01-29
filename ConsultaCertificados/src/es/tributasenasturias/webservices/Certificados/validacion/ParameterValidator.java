/**
 * 
 */
package es.tributasenasturias.webservices.Certificados.validacion;



import es.tributasenasturias.webservices.Certificados.SERVICIOWEB;
import es.tributasenasturias.webservices.Certificados.General.IResultado;
import es.tributasenasturias.webservices.Certificados.General.InfoResultado;
import es.tributasenasturias.webservices.Certificados.SERVICIOWEB.PETICION.CERTIFICADO;


/**
 * @author CarlosRuben
 * Comprueba si los datos de entrada al servicio son válidos.
 */
public class ParameterValidator implements IValidator<SERVICIOWEB> {

	InfoResultado resultado;
	private String []codigosPostales=
	{
			"33001",
			"33002",
			"33003",
			"33004",
			"33005",
			"33006",
			"33007",
			"33008",
			"33009",
			"33010",
			"33011",
			"33012",
			"33013",
			"33014",
			"33015",
			"33016",
			"33017",
			"33018",
			"33019",
			"33020",
			"33021",
			"33022",
			"33023",
			"33024",
			"33025",
			"33026",
			"33027",
			"33028",
			"33029",
			"33030",
			"33031",
			"33032",
			"33033",
			"33034",
			"33035",
			"33036",
			"33037",
			"33038",
			"33039",
			"33040",
			"33041",
			"33042",
			"33043",
			"33044",
			"33045",
			"33046",
			"33047",
			"33048",
			"33049",
			"33050",
			"33051",
			"33052",
			"33053",
			"33054",
			"33055",
			"33056",
			"33057",
			"33058",
			"33059",
			"33060",
			"33061",
			"33062",
			"33063",
			"33064",
			"33065",
			"33066",
			"33067",
			"33068",
			"33069",
			"33070",
			"33071",
			"33072",
			"33073",
			"33074",
			"33075",
			"33076",
			"33077",
			"33078",
			"33090"
	};
	public ParameterValidator()
	{
		resultado=new InfoResultado();
	}
	/* (non-Javadoc)
	 * @see es.tributasenasturias.interfaces.IValidator#isValid(java.lang.Object)
	 */
	@Override
	public boolean isValid(SERVICIOWEB entrada) {
		boolean valido=true;
		if (entrada==null)
		{	
			resultado.addMessage("Entrada vacía");
			valido=false;
		}
		if (valido)
		{
			if (entrada.getPETICION()==null)
			{
				resultado.addMessage("Petición vacía");
				valido=false;
			}
		}
		CERTIFICADO cert=null;
		if (valido)
		{
			cert = entrada.getPETICION().getCERTIFICADO();
			if (cert==null)
			{
				resultado.addMessage("Certificado vacío");
				valido=false;
			}
		}
		if (valido && cert.getNIF().trim().equals(""))
		{
			resultado.addMessage("NIF vacío");
			valido=false;
		}
		if (valido && cert.getREQUIRENTE().trim().equals(""))
		{
			resultado.addMessage("Requirente vacío");
			valido=false;
		}
		if (valido && cert.getNIFSOLICITANTE().trim().equals(""))
		{
			resultado.addMessage("Nif Solicitante vacío");
			valido=false;
		}
		if (valido && cert.getNOMBRESOLICITANTE().trim().equals(""))
		{
			resultado.addMessage("Nombre solicitante vacío");
			valido=false;
		}
		if (valido && !"SI".equalsIgnoreCase(cert.getCONSENTIMIENTO().trim()) && !"LEY".equalsIgnoreCase(cert.getCONSENTIMIENTO().trim()))
		{
			resultado.addMessage("Valor de consentimiento no válido.");
			valido=false;
		}
		if (valido && !"101".equals(cert.getMOTIVO().trim()) && !"102".equals(cert.getMOTIVO().trim()) && !"103".equals(cert.getMOTIVO().trim()) && !"104".equals(cert.getMOTIVO().trim()))
		{
			resultado.addMessage("Valor de motivo no válido");
			valido=false;
		}
		//El 104 sólo es válido si se consulta para débitos autonómicos
		if (valido && "104".equals(cert.getMOTIVO().trim()) && !"33090".equals(cert.getTIPO().trim()))
		{
			resultado.addMessage("El motivo \"104\" sólo puede indicarse para tipo \"33090\"");
			valido=false;
		}
		if (valido)
		{
			boolean encontrado=false;
			for (String i: codigosPostales)
				{
					if (i.equalsIgnoreCase(cert.getTIPO().trim()))
						{
						encontrado=true;
						break;
						}
				}
			if (!encontrado)
			{
				resultado.addMessage("Valor de tipo no válido.");
				valido=false;
			}
		}
		return valido;
	}
	/* (non-Javadoc)
	 * @see es.tributasenasturias.validacion.IValidator#getResultados()
	 */
	@Override
	public IResultado getResultado() {
		
		return resultado;
	}

}
