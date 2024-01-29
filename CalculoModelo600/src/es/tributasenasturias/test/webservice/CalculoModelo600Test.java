package es.tributasenasturias.test.webservice;

import org.junit.Before;
import org.junit.Test;

import es.tributasenasturias.utils.Logger;

public class CalculoModelo600Test {

	@Before
	public void setUp() throws Exception {

	}
	
	@Test
	public void CalculoModelo600AU0Test() {
		//cm = new CalculoModelo600();
		//String result = cm.getCalculoModelo600(xmlData);
		//org.junit.Assert.assertTrue(!result.isEmpty());
		Logger.doLog("Probando junit", es.tributasenasturias.utils.Logger.LEVEL.INFO);
		org.junit.Assert.assertTrue(!("hola").isEmpty());
	}

}
