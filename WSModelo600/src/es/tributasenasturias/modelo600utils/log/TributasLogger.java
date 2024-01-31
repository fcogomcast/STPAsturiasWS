package es.tributasenasturias.modelo600utils.log;

/**Implementa un log para la aplicación.
 * @author crubencvs
 */
public class TributasLogger extends GenericAppLogger{

	public TributasLogger(String pathToLog){
		String dir=getDirFromPath(pathToLog);
		String nombre=getNameFromPath(pathToLog);
		this.setLogFile(nombre);
		this.setLogDir(dir);
		this.setNombre("Modelo600"); //Nombre de proceso que aparecerá en el log.
	}

	private String getDirFromPath(String path){
		String dir="";
		if (path.length()!=0){
			dir=path.replace('\\', '/');
			dir=dir.substring(0,dir.lastIndexOf('/', dir.length()-1));
		}
		return dir;
	}

	private String getNameFromPath(String path){
		String dir="";
		if (path.length()!=0){
			dir=path.replace('\\', '/');
			dir=dir.substring(dir.lastIndexOf('/', dir.length()-1)+1,dir.length());
		}
		return dir;
	}
}
