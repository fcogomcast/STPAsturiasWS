package es.stpa.smartmultas.clases;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import org.w3c.dom.Document;

import es.stpa.smartmultas.configuracion.utils.ConfiguracionUtils;
import es.stpa.smartmultas.configuracion.utils.Utiles;
import es.stpa.smartmultas.entidades.FileStatusData;
import es.stpa.smartmultas.entidades.UploadFileRequest;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.stpa.smartmultas.responses.UploadFileResponse;
import es.stpa.smartmultas.soap.SoapClientHandler;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.services.lanzador.client.LanzadorFactory;
import es.tributasenasturias.services.lanzador.client.ParamType;
import es.tributasenasturias.services.lanzador.client.ProcedimientoAlmacenado;
import es.tributasenasturias.services.lanzador.client.TLanzador;
import es.tributasenasturias.services.lanzador.client.response.RespuestaLanzador;
import es.tributasenasturias.xml.XMLDOMUtils;

// ServiciosSmartFines.Multas.UploadFilePiece
public class UploadFilePiece extends A_MultasBase {
	
	final String ES05 = "ES05_ESTRUCTURA_UNI05";
	final String WTEA = "WTEA_ESTRUCTURA_ARQUITECTURA";
	
	public UploadFilePiece(Preferencias pref, String idLlamada, Document datosEntrada, ILog log) {
		super(pref, idLlamada, datosEntrada, log);
	}
	
	@Override
	public String Inicializar() { // Incorporar archivos de audio y vídeo
		
		UploadFileResponse datosRespuesta = new UploadFileResponse();
		
		try
		{
			String tmpXML = XMLDOMUtils.getXMLText(_datosEntrada);

			UploadFileRequest datosEntrada = XMLDOMUtils.XMLtoObject(tmpXML, UploadFileRequest.class);

            // 1. Comprobamos los datos que recibimos
			CheckData(datosEntrada, datosRespuesta);
					
			if(datosRespuesta.getError() == null)
			{
			   datosRespuesta = SavePiece(datosEntrada);	// 2. Guardamos los trozos
			   
			   if(datosRespuesta.getError() == null && (datosEntrada.getTrozoActual() == datosEntrada.getTrozosTotal()))
			   {
				   String idFic = datosEntrada.getIdFichero();
				   if(idFic == null)
				   {
					   idFic = datosRespuesta.getIdFichero();
				   }
				   FileStatusData fileStatus = unirTrozosFichero(datosEntrada, idFic); // 3. Si es el último trozo, unimos y custodiamos
				   String err = fileStatus.getError();
				   if(err != null && !err.isEmpty()) 	// Si hay error lo devolvemos como respuesta, aunque se hayan guardado los trozos
				   {
					   datosRespuesta.setError(err);
				   }
			   }
			}	
		} 
		catch (Exception ex) 
		{

		}
		
		return XMLDOMUtils.Serialize(datosRespuesta);
	}
	
	
	 private void CheckData(UploadFileRequest datosEntrada, UploadFileResponse datosRespuesta)
     {
		 int trozoActual = datosEntrada.getTrozoActual();
		 int trozoTotal = datosEntrada.getTrozosTotal();
		 String idFic = datosEntrada.getIdFichero();
		 
         if (trozoActual == 1 && idFic != null)
         {
        	 datosRespuesta.setError("IdFichero no puede tener valor para el primer envío.");
         }

         if (idFic == null && trozoActual > 1)
         {
        	 datosRespuesta.setError("IdFichero no puede ser nulo para un trozo que no sea el inicial.");
         }

         if (trozoActual < 1)
         {
        	 datosRespuesta.setError("TrozoActual no puede ser menor a 1.");
         }

         if (trozoActual > trozoTotal)
         {
        	 datosRespuesta.setError("TrozoActual no puede ser mayor a TrozosTotal.");
         }

         if (trozoTotal < 1)
         {
        	 datosRespuesta.setError("TrozosTotal no puede ser menor a 1.");
         }

         if (datosEntrada.getNombreFichero()  == null || datosEntrada.getNombreFichero().isEmpty())
         {
        	 datosRespuesta.setError("NombreFichero no puede estar vacío.");
         }

         if (datosEntrada.getDataB64()  == null || datosEntrada.getDataB64().isEmpty())
         {
        	 datosRespuesta.setError("DataB64 no puede estar vacío.");
         }

         if (datosEntrada.getNumeroExpediente()  == null || datosEntrada.getNumeroExpediente().isEmpty())
         {
        	 datosRespuesta.setError("NumeroExpediente no puede estar vacío.");
         }

         if (datosEntrada.getIdEper() < 1)
         {
        	 datosRespuesta.setError("IdEper no puede estar vacío.");
         }

         if (datosEntrada.getTipoFichero() != null && datosEntrada.getTipoFichero().toString().equalsIgnoreCase("Indefinido"))
         {
        	 datosRespuesta.setError("TipoFichero no puede estar vacío.");
         }
     }
	 
	 
	 private UploadFileResponse SavePiece(UploadFileRequest datosEntrada) {
		 
		UploadFileResponse datosRespuesta = null;

		try 
		{
			if (datosEntrada.getTrozoActual() == 1) 
			{
				String idFicNew = UUID.randomUUID().toString(); // Guid.NewGuid()
				datosEntrada.setIdFichero(idFicNew);
			}

			boolean uploadedPiece = UploadFilePieceBBDD(datosEntrada);
			
			String err = uploadedPiece ? null : "Se ha producido un error al guardar este trozo.";
			datosRespuesta = new UploadFileResponse (
					datosEntrada.getIdFichero(),
					datosEntrada.getTrozoActual(),
					err
					);
		} 
		catch (Exception ex) 
		{
			datosRespuesta = new UploadFileResponse (
					datosEntrada.getIdFichero(),
					datosEntrada.getTrozoActual(),
					"Se ha producido un error al guardar este trozo."
					);
		}

		return datosRespuesta;
	}
	 
	 
	 public boolean UploadFilePieceBBDD(UploadFileRequest datosEntrada)
     {
         boolean uploadState = false;

         // Insertamos los datos del trozo de fichero en la base de datos
         try
         {
        	 TLanzador lanzador = LanzadorFactory.newTLanzador(_pref.getEndpointLanzadorMasivo(), new SoapClientHandler(_idLlamada));
        	 ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.SUBIR_TROZO_ARCHIVO", _pref.getEsquemaBD());
        	 Utiles.AgregarCabeceraGeneralPL(proc);

 			 proc.param(datosEntrada.getIdFichero(), ParamType.CADENA);					// ID_SWUF
 			 proc.param(datosEntrada.getTrozoActual() + "", ParamType.NUMERO);			// TROZO_SWUF
 			 proc.param(datosEntrada.getTrozosTotal() + "", ParamType.NUMERO);			// TOTAL_SWUF
 			 proc.param(datosEntrada.getNombreFichero(), ParamType.CADENA); 			// NOMBRE_SWUF
 			 proc.param(datosEntrada.getIdEper() + "", ParamType.NUMERO); 				// IDEPER_SWUF
 			 proc.param(datosEntrada.getNumeroExpediente(), ParamType.CADENA); 			// NUMEROEXP_SWUF
 			 proc.param(datosEntrada.getTipoFichero().toString(), ParamType.CADENA); 	// TIPO_SWUF

 			 String strDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format((Calendar.getInstance()).getTime());
 			 proc.param(strDate, ParamType.CADENA); 									// FECHA_SWUF.. TIMESTAMP(6)
 			 
 			 proc.param(datosEntrada.getDataB64(), ParamType.CLOB); 					// DATOSB64_SWUF
 			
 			 String soapResponse = lanzador.ejecutar(proc);
 			 RespuestaLanzador response = new RespuestaLanzador(soapResponse);

 			if (!response.esErronea() && response.getNumFilasEstructura(ES05) > 0 && response.getValue(ES05, 1, "C1").equals("OK")) 
 			{
 				uploadState = true; 
 			}
         }
         catch (Exception ex)
         {
             uploadState = false;
         }

         return uploadState;
     }
	 
	 
	 private FileStatusData unirTrozosFichero(UploadFileRequest datosEntrada, String idFichero) {

		FileStatusData fileStatus = null;
		String resCustodia = null;
		 
		try 
		{			
			fileStatus = new FileStatusData(
					idFichero,
					datosEntrada.getNombreFichero(),
					datosEntrada.getNumeroExpediente(),
					datosEntrada.getIdEper(),
					Long.parseLong("-1"),
					datosEntrada.getTipoFichero(),
					"N"
			);	


        	   String tipoElemento = "";
        	   String tipoDocumento = "";
        		   
        	   if(datosEntrada.getTipoFichero() != null && datosEntrada.getTipoFichero().toString().equalsIgnoreCase("Video"))	 
        	   {
        		   tipoElemento = "VIDE"; 
        		   tipoDocumento = "V";
        	   }
        	   else if(datosEntrada.getTipoFichero() != null && datosEntrada.getTipoFichero().toString().equalsIgnoreCase("Audio"))
        	   {
        		   tipoElemento = "AUDI"; 
        		   tipoDocumento = "A";
        	   }
        	   else
        	   {
        		   tipoElemento = "DESC"; 
        		   tipoDocumento = "F";
        	   }
        		   
        	   // Debe devolver "OK" o "Error..."
               String auxIdAdar = UnirTrozosYCustodiarFichero(datosEntrada, datosEntrada.getIdEper()+ "", datosEntrada.getNombreFichero(), tipoElemento); // 4. Custodiamos el fichero
        	   
          	   if(auxIdAdar != null)
        	   {
          		 Integer idAdar = Integer.parseInt(auxIdAdar);
          		 if(idAdar > 0)
          		 {
          			 //5. Adjuntamos las fotos al expediente
          			String datosResultado = ConfiguracionUtils.AdjuntarImagenesExp(_pref, _idLlamada, _log, _datosEntrada, idAdar, tipoDocumento, datosEntrada.getNombreFichero());
          			
          			if(datosResultado != null && !datosResultado.isEmpty())
          			{
                   	   boolean saveFileStatus = ActualizarEstadoFichero(fileStatus, auxIdAdar); // 6. Actualizamos el estado del fichero en base de datos
                	   if(saveFileStatus)
                	   {
                		   // Eliminamos todos los trozos del fichero si todo ha ido bien. No se gestiona el gallo de esta parte, simplemente no se borrará de la tañla temporal
                		   EliminarTrozosFichero(fileStatus.getIdFichero()); // 7. Si es posible, borramos los trozos de la tabla temporal
                	   }
          			}
          		 }
          		 else
          		 {
          			fileStatus.setError("No se ha podido custodiar el archivo correctamente");
          		 }      	
        	   }
          	   else
          	   {
          		   fileStatus.setError(resCustodia);
          	   }
		} 
		catch (Exception ex) {}

		return fileStatus;
	}
	 
	 
	 private String UnirTrozosYCustodiarFichero(UploadFileRequest datosEntrada, String idInterno, String nomFic, String tipoElemento) {

		String idAdar = null;
		 
		try 
		{						
            // Unimos los trozos en el pl y custodiamos el fichero
			TLanzador lanzador = LanzadorFactory.newTLanzador(_pref.getEndpointLanzador(), new SoapClientHandler(_idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.UNIR_TROZOS_FICHERO", _pref.getEsquemaBD());
			Utiles.AgregarCabeceraGeneralPL(proc);

			proc.param(datosEntrada.getIdFichero(), ParamType.CADENA);	//p_in_idFichero
			proc.param(idInterno, ParamType.CADENA);					// p_in_id_interno_tributas
			proc.param(nomFic, ParamType.CADENA);						// p_in_nom_fichero
			proc.param(tipoElemento, ParamType.CADENA);					// p_in_tipo_elemento

			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);

			if (!response.esErronea() && response.getNumFilasEstructura(WTEA) > 0) 
			{		
				// obtenemos el idadar
				idAdar = response.getValue(WTEA, 1, "N1");
			}
		} 
		catch (Exception ex) 
		{

		}

		return idAdar;
	}
	 
	 
	 private boolean ActualizarEstadoFichero(FileStatusData fileStatus, String idAdar){
		 
         boolean uploadState = false;
		 
		 try
		 {
		 	// Actualizamos el estado de la custodia del fichero en la base de datos  
			 if(idAdar != null)
			 {
				 fileStatus.setIdAdar(Long.parseLong(idAdar));
			 }

			 fileStatus.setCustodiado("S");
			 
			 TLanzador lanzador = LanzadorFactory.newTLanzador(_pref.getEndpointLanzador(), new SoapClientHandler(_idLlamada));
			 ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.ACTUALIZAR_ESTADO_FICHERO", _pref.getEsquemaBD());
			 Utiles.AgregarCabeceraGeneralPL(proc);
		
 			 proc.param(fileStatus.getIdFichero(), ParamType.CADENA);					// ID_SWFS
 			 proc.param(fileStatus.getNombreFichero(), ParamType.CADENA); 				// NOMBRE_SWFS
 			 proc.param(fileStatus.getIdEper() + "", ParamType.NUMERO); 				// IDEPER_SWFS
 			 proc.param(fileStatus.getIdAdar() + "", ParamType.NUMERO); 				// IDADAR_SWFS
 			 proc.param(fileStatus.getNumeroExpediente(), ParamType.CADENA); 			// NUMEROEXP_SWFS
 			 proc.param(fileStatus.getTipoFichero().toString(), ParamType.CADENA); 		// TIPO_SWFS

 			 String strDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format((Calendar.getInstance()).getTime());
 			 proc.param(strDate, ParamType.CADENA); 									// FECHA_SWUF.. TIMESTAMP(6)
 			 
 			 proc.param(fileStatus.getCustodiado(), ParamType.CADENA); 					// CUSTODIADO_SWFS
 			 proc.param(fileStatus.getError(), ParamType.CADENA); 						// ERROR_SWFS
				
			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);
			
			if (!response.esErronea() && response.getNumFilasEstructura(ES05) > 0 && response.getValue(ES05, 1, "C1").equals("OK")) 
			{
				uploadState = true;
			}
		 }
		 catch (Exception e) 
		 {
			 uploadState = false;
		 }
		 
		 return uploadState;
	 }
	 
	 
	 private boolean EliminarTrozosFichero(String idFichero){
		 
         boolean uploadState = false;
		 
		 try
		 {
			 TLanzador lanzador = LanzadorFactory.newTLanzador(_pref.getEndpointLanzador(), new SoapClientHandler(_idLlamada));
			 ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.ELIMINAR_TROZOS_FICHERO", _pref.getEsquemaBD());
			 Utiles.AgregarCabeceraGeneralPL(proc);
		
 			 proc.param(idFichero, ParamType.CADENA);					// ID_SWFS
				
			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);
			
			if (!response.esErronea() && response.getNumFilasEstructura(ES05) > 0 && response.getValue(ES05, 1, "C1").equals("OK")) 
			{
				uploadState = true;
			}
		 }
		 catch (Exception e){ }
		 
		 return uploadState;
	 }
}
