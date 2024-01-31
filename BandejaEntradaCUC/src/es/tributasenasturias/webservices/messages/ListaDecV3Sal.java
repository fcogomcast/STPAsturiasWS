package es.tributasenasturias.webservices.messages;


import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListaDecV3Sal", propOrder = {
    "declaracion"
})
public class ListaDecV3Sal {

    @XmlElement(required = true)
    private List<DeclaracionType> declaracion;

    
    public final List<DeclaracionType> getDeclaracion() {
        if (declaracion == null) {
            declaracion = new ArrayList<DeclaracionType>();
        }
        return this.declaracion;
    }
}
    
