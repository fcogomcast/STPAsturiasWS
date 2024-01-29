package es.tributasenasturias.webservices.Certificados.validacion.Certificado;

import javax.xml.soap.SOAPHeader;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.tributasenasturias.webservices.Certificados.Exceptions.ValidacionException;
import es.tributasenasturias.webservices.Certificados.General.IResultado;
import es.tributasenasturias.webservices.Certificados.General.InfoResultado;
import es.tributasenasturias.webservices.Certificados.validacion.IValidator;
/**
 * Validador de certificado de cabecera
 * @author crubencvs
 *
 */
public class CertificadoValidator implements IValidator<SOAPHeader>{

	InfoResultado res;
	TInfoCertificado info=null;
	
	public TInfoCertificado getInfoCertificado() {
		return info;
	}
	private String getCertificadoCabecera (SOAPHeader cabecera)
	{
		String certificado="";
		//Recuperamos la lista de nodos de la cabecera con certificado. Sólo debería haber uno.
		NodeList certificados= cabecera.getElementsByTagNameNS("*", "X509Certificate");
		if (certificados!=null && certificados.getLength()!=0)
		{
			//Recuperamos el valor del nodo. Debe ser el nodo "0".
			Node nodoCert = certificados.item(0);
			if (nodoCert.getChildNodes().getLength()!=0)
			{
				certificado = certificados.item(0).getChildNodes().item(0).getNodeValue();
			}
			else 
			{
				certificado=null;
			}
		}
		else
		{
			certificado=null;
		}
		return certificado;
	}
	public CertificadoValidator()
	{
		res = new InfoResultado();
	}
	@Override
	public IResultado getResultado() {
		return res;
	}

	@Override
	public boolean isValid(SOAPHeader cabecera) {
		boolean valido=false;
		//Recuperamos el certificado.
		String certificado = getCertificadoCabecera(cabecera);
		if (certificado!=null && !certificado.equals(""))
		{
			//Lo enviamos al servicio del Principado, para que nos indique si es válido.
			AutenticacionPAHelper aut = new AutenticacionPAHelper();
			try
			{
				info = aut.login(certificado);
				if ((info.getNifNie()!=null  && !"".equals(info.getNifNie()))|| 
					  (info.getCif()!=null && !"".equals(info.getCif())))
				{
					valido=true;
				}
				else
				{
					res.addMessage("Se ha devuelto un identificador vacío en la autorización. Revise el log para obtener más detalle");
					valido=false;
				}
			}
			catch (ValidacionException ex)
			{
				res.addMessage(ex.getMessage()+"-"+ex.getCause().getMessage());
				valido=false;
			}
		}
		else
		{
			valido=false;
			res.addMessage("No se ha podido validar el certificado. El certificado está vacío.");
		}
		return valido;
	}

}
