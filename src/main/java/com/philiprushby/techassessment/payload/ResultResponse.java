package com.philiprushby.techassessment.payload;

public class ResultResponse {
	private String operation;
	private Object value;
	
	public final static String OPERATION_KEY_NAME = "operation";
	public final static String VALUE_KEY_NAME = "value";
	
	
	public ResultResponse(String operation, Object value) {
		this.operation = operation;
		this.value = value;
	}

	public String getOperation() {
		return operation;
	}

	public Object getValue() {
		return value;
	}	
}
