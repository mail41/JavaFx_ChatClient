package ch.fixy.common;

public enum ServerSplitCode {
	SPLIT("#SPLIT_CODE#");
	
	private String code;
	
	private ServerSplitCode(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}