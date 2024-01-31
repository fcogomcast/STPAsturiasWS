package es.tributasenasturias.plataforma.unipay;

import com.google.gson.annotations.SerializedName;


public class TokenResponseJsonObject {
	@SerializedName(value="TOKEN")
	private String token;
	@SerializedName(value="TOKEN_RESULT")
    private TokenResult tokenResult;
	@SerializedName(value="PARAMS")
    private Params params;

    public String getToken() { return token; }
    public void setToken(String value) { this.token = value; }

    public TokenResult getTokenResult() { return tokenResult; }
    public void setTokenResult(TokenResult value) { this.tokenResult = value; }

    public Params getParams() { return params; }
    public void setParams(Params value) { this.params = value; }
}


class TokenResult {
	@SerializedName(value="CODE")
    private String code;
	@SerializedName(value="DESCRIPTION")
    private String description;

    public String getCode() { return code; }
    public void setCode(String value) { this.code = value; }

    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }
}