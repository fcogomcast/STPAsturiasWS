package es.tributasenasturias.webservices.Certificados.soap.handler;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.webservices.Certificados.Exceptions.PreferenciasException;
import es.tributasenasturias.webservices.Certificados.utils.SOAPFaultHelper;
import es.tributasenasturias.webservices.Certificados.utils.Log.GenericAppLogger;
import es.tributasenasturias.webservices.Certificados.utils.Log.TributasLogger;
import es.tributasenasturias.webservices.Certificados.utils.Preferencias.Preferencias;
import es.tributasenasturias.webservices.Certificados.validacion.Certificado.TInfoCertificado;
import es.tributasenasturias.webservices.Certificados.validacion.PermisoServicio.PermisoServicioValidator;
import es.tributasenasturias.webservices.Certificados.validacion.PermisoServicio.TInfoPermisoServicio;

public class PermisoServicioHandler implements SOAPHandler<SOAPMessageContext> {

	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	@Override
	public void close(MessageContext context) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean handleFault(SOAPMessageContext context) {
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (salida)
		{
			return true; //Propagamos
		}
		return false;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		//Sólo para entrada.
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		GenericAppLogger log = new TributasLogger();
		Preferencias pref;
		try
		{
			pref= Preferencias.getPreferencias();
		}
		catch (PreferenciasException p)
		{
			log.error("No se ha podido realizar la validación de permiso. Problema con las preferencias:"+ p.getMessage());
			throw SOAPFaultHelper.createSOAPFaultException("Error.");
		}
		if (!salida.booleanValue())
		{
			if ("S".equalsIgnoreCase(pref.getValidaCertificado()))
			{
				log.info (">>>Se validan los permisos del certificado sobre el servicio.");
				try
				{
					//Recuperamos de contexto los datos del certificado.
					TInfoCertificado cert = (TInfoCertificado) context.get("InfoCertificado");
					PermisoServicioValidator per = new PermisoServicioValidator();
					String cifnif = cert.getCif()==null?cert.getNifNie():cert.getCif();
					if (!per.isValid(cifnif))
					{
						log.error("No se tienen permisos para ese certificado. Revise el resto del log para encontrar más información.");
						if (per.getResultado().toString().equals(""))
						{
							log.error(per.getResultado().toString());
						}
						throw SOAPFaultHelper.createSOAPFaultException("Error.");
					}
					else //Se deja en contexto el resultado del permiso de servicio. El siguiente manejador lo utilizará.
					{
						TInfoPermisoServicio info = per.getInfoPermisoServicio();
						context.put("InfoPermisoServicio", info);
						context.remove("InfoCertificado");
					}
				}
				catch (Exception ex)
				{
					log.error("Error al validar los permisos del certificado: "+ ex.getMessage());
					throw SOAPFaultHelper.createSOAPFaultException("Error.");
				}
			}
			else 
			{
				log.info(">>>No se validan los permisos del certificado.");
			}
		}
		return true;
	}
}
