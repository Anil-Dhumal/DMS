package com.dms.exceptionHandle;

public class FieldErrorResponse {
	
	private String field;
	private String message;
	
	
	public FieldErrorResponse(String field, String message) {
		super();
		this.field = field;
		this.message = message;
	}


	public String getField() {
		return field;
	}


	public void setField(String field) {
		this.field = field;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
