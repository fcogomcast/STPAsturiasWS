package es.tributasenasturias.traslado.mensajes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * Clase raíz para modelar los mensajes de aplicación. Todos tendrán el siguiente formato:
 * <pre>
 * <code>
 *     &lt;mensajes&gt;
*	&lt;grupo id=&quot;...&quot;&gt;
*		&lt;mensaje id=&quot;...&quot;&gt;
*			&lt;codigo&gt;...&lt;/codigo&gt;
*			&lt;descripcion&gt;...&lt;/descripcion&gt;
*		&lt;/mensaje&gt;
*		&lt;mensaje id=&quot;...&quot;&gt;
*			&lt;codigo&gt;...&lt;/codigo&gt;
*			&lt;descripcion&gt;...&lt;/descripcion&gt;
*		&lt;/mensaje&gt;
*	&lt;/grupo&gt;
*	&lt;grupo id=&quot;...&quot;&gt;
*    	...
*	&lt;/grupo&gt;
* &lt;/mensajes&gt;
 * </code>
 * </pre>  
 * @author crubencvs
 *
 */
public class Mensajes {
	private List<Grupo> grupos= new ArrayList<Grupo>();
	
	@XmlElement(name="grupo")
	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}
	
}
