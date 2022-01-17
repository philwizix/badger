package com.philiprushby.techassessment;

import org.springframework.http.HttpStatus;

public class TestData  {
	public HttpStatus expectedStatusCode = null;
	public Object expectedValue = null;
	
	public TestData (HttpStatus expectedStatusCode, Object expectedValue) {
		this.expectedStatusCode = expectedStatusCode;
		this.expectedValue = expectedValue;
	}
}
