package cn.edu.zucc.Fenfei.bean;

import java.util.List;

public class TransWord {

	private String queryword;
	private String errorCode;
	private String simpletrans;
	private String basictrans;
	private List<WebData> webtrans;
	
	public String getQueryword() {
		return queryword;
	}
	public void setQueryword(String queryword) {
		this.queryword = queryword;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getSimpletrans() {
		return simpletrans;
	}
	public void setSimpletrans(String simpletrans) {
		this.simpletrans = simpletrans;
	}
	public String getBasictrans() {
		return basictrans;
	}
	public void setBasictrans(String basictrans) {
		this.basictrans = basictrans;
	}
	public List<WebData> getWebtrans() {
		return webtrans;
	}
	public void setWebtrans(List<WebData> webtrans) {
		this.webtrans = webtrans;
	}
	
}
