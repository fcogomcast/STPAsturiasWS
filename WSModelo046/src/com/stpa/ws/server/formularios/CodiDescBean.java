package com.stpa.ws.server.formularios;

import java.io.Serializable;

public class CodiDescBean implements Comparable<CodiDescBean>, Serializable{
	
	private static final long serialVersionUID = -5113211611545513014L;
	public String codi;
	public String desc;
	
	public CodiDescBean(){
		
	}
	public CodiDescBean(String p_codi, String p_desc){
		codi = p_codi;
		desc = p_desc;
	}
	
	public String getCodi() {
		return codi;
	}
	
	public String toString(){
		return codi;
	}

	public void setCodi(String codi) {
		this.codi = codi;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public int compareTo(CodiDescBean o) {
		if (o instanceof CodiDescBean){
			CodiDescBean elem = (CodiDescBean)o;
			if (desc != null)
				return desc.compareTo(elem.getDesc());
			else
				return 1;
		}
		return 1;
	}
}
