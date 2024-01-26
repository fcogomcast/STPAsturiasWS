package es.tributasenasturias.documentopagoutils;

public class CampoWS implements Comparable<CampoWS>{
	
	
	public String campo; //nombre del campo
	public String tipoCampo;
	public String valor;
	public String orden; 
	public String tipoParam;
	
	
	
	public CampoWS() {
	}
	
	public CampoWS(String orden, String campo, String tipoCampo) {
		super();
		this.campo = campo;
		setTipoCampo(tipoCampo);
		this.orden = orden;
	}
	
	public CampoWS(String orden, String campo, String tipoCampo, String valor) {
		super();
		this.campo = campo;
		setTipoCampo(tipoCampo);
		this.orden = orden;
		this.valor = valor;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getTipoCampo() {
		return tipoCampo;
	}

	public void setTipoCampo(String tipoCampo) {
		this.tipoCampo = tipoCampo;
		
		if(tipoCampo != null){
			if("CADENA".equals(tipoCampo)){
				this.tipoParam = "1";
			}
			else if("NUMERO".equals(tipoCampo)){
				this.tipoParam = "2";
			}
		}
	}

	public String getTipoParam() {
		return tipoParam;
	}

	public void setTipoParam(String tipoParam) {
		this.tipoParam = tipoParam;
	}

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}



	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public int compareTo(CampoWS otroCampo) {
		int compare = 0;
		if(otroCampo != null){
			int orden = -1;
			try {
				orden = Integer.parseInt(this.orden);
			}
			catch (Exception e){
				//El orden no está informado
			}
			
			
			int ordenOtro = -1;
			
			try {
				ordenOtro = Integer.parseInt(otroCampo.getOrden());
			}
			catch (Exception e){
				//El orden no está informado en alguno de los casos
			}
				
			if(orden < ordenOtro){
				compare = -1;
			}
			else if(orden == ordenOtro){
				compare = 0;
			}
			else if(orden > ordenOtro){
				compare = 1;
			}
		}	
		return compare;
	}
	
	
	

}
