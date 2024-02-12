/**
 * 
 */
package es.tributasenasturias.impl;

import https.www3_aeat_es.adua.internet.es.aeat.dit.adu.adht.banent.listadecv3.ListaDecV3;
import https.www3_aeat_es.adua.internet.es.aeat.dit.adu.adht.banent.listadecv3.ListaDecV3Service;

import java.util.List;

import javax.xml.ws.BindingProvider;

import es.tributasenasturias.abstracts.ServiceProxy;
import es.tributasenasturias.certificate.intImpl.BandejaEntradaPortSecurityProvider;
import es.tributasenasturias.certificate.intImpl.Preferencias;
import es.tributasenasturias.certificate.interfaces.IPropertiesProvider;
import es.tributasenasturias.webservices.messages.DeclaracionType;
import es.tributasenasturias.webservices.messages.ListaDecV3Ent;
import es.tributasenasturias.webservices.messages.ListaDecV3Sal;

/**
 * @author CarlosRuben
 *
 */
public class BandejaEntradaProxy extends ServiceProxy {

	private ListaDecV3 miPort=null;
	public BandejaEntradaProxy() throws Exception
	{
		this.secProv=new BandejaEntradaPortSecurityProvider();
		// Se inicializan los datos del servicio remoto.
		ListaDecV3Service miServ = new ListaDecV3Service();
		miPort= miServ.getListaDecV3();
		secProv.setPortSecurity(miPort);//TODO: ¿Modifica el valor o habrá que devolver el objeto completo?.
	}
	/* (non-Javadoc)
	 * @see es.tributasenasturias.abstracts.ServiceProxy#getEndPoint()
	 */
	@Override
	public final String getEndPoint() {
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) miPort;
		return bpr.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY).toString();
	}

	/* (non-Javadoc)
	 * @see es.tributasenasturias.abstracts.ServiceProxy#getInnerObject()
	 */
	@Override
	public final Object getInnerObject() {
		return (Object) miPort;
	}

	/* (non-Javadoc)
	 * @see es.tributasenasturias.abstracts.ServiceProxy#setEndPoint(java.lang.String)
	 */
	@Override
	public void setEndPoint(String endPoint) {
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) miPort;
		bpr.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPoint);
	}
	/** Realiza la llamada al servicio remoto para realizar la operación de bandeja de entrada
	 * 
	 * @param entrada
	 * @return
	 */ 
	public final ListaDecV3Sal callOperation (ListaDecV3Ent entrada) 
	{
		//Objeto de lectura de preferencias.
		IPropertiesProvider pref = new Preferencias();
		// Objetos remotos.
		https.www3_aeat_es.adua.internet.es.aeat.dit.adu.adht.banent.listadecv3ent.ListaDecV3Ent remEntrada=
			new https.www3_aeat_es.adua.internet.es.aeat.dit.adu.adht.banent.listadecv3ent.ListaDecV3Ent();
		https.www3_aeat_es.adua.internet.es.aeat.dit.adu.adht.banent.listadecv3sal.ListaDecV3Sal remSalida=null;
		https.www3_aeat_es.adua.internet.es.aeat.dit.adu.adht.banent.listadecv3ent.DeclaranteType declarante=
			 new https.www3_aeat_es.adua.internet.es.aeat.dit.adu.adht.banent.listadecv3ent.DeclaranteType();
		//Se mapea la entrada a la entrada remota.
		declarante.setNifDeclarante(entrada.getDeclarante().getNifDeclarante());
		declarante.setNombreDeclarante(entrada.getDeclarante().getNombreDeclarante());
		remEntrada.setDeclarante(declarante);
		//Se establece el endpoint
		String endpoint="";
		try{
			pref.refreshPreferences();
			endpoint=((Preferencias)pref).getEndPoint();
			setEndPoint(endpoint);
		}
		catch (Exception ex)
		{
			//EndPoint vacío, para que falle.
			endpoint="";
		}
		//Llamada al procedimiento remoto.
		remSalida=miPort.listaDecV3(remEntrada);
		//Recogemos la salida y la transformamos.
		ListaDecV3Sal salida=new ListaDecV3Sal();
		List<DeclaracionType> decList=salida.getDeclaracion();
		DeclaracionType dclt=null;
		//Se recorre y rellena la lista de salida.
		for (https.www3_aeat_es.adua.internet.es.aeat.dit.adu.adht.banent.listadecv3sal.DeclaracionType d: remSalida.getDeclaracion())
		{
			dclt=new DeclaracionType();
			dclt.setClave(d.getClave());
			dclt.setReferencia(d.getReferencia());
			dclt.setTipoRespuesta(d.getTipoRespuesta());
			decList.add(dclt);
		}
		return salida;
		}

}
