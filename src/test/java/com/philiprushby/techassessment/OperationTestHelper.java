package com.philiprushby.techassessment;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.philiprushby.techassessment.payload.ResultResponse;

public class OperationTestHelper {
	protected String operationName;
	private TestRestTemplate testRestTemplate;
	
	public OperationTestHelper(String operationName, TestRestTemplate restTemplate) {
		this.operationName = operationName;
		this.testRestTemplate = restTemplate;
	}
	
	public void addParameterToUriBuilder (UriComponentsBuilder uriBuilder, String paramName, String paramValue) {
		if (paramValue != null) {
			try {
		    	uriBuilder.queryParam(paramName, URLEncoder.encode(paramValue, StandardCharsets.UTF_8.name()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				fail("UnsupportedEncodingException for parm " + paramName + "=" + paramValue);
			}
		}
	}

	public boolean fetchAndTestResponse(UriComponentsBuilder uriBuilder, TestData testData) {
		URI uri = uriBuilder.build(true).encode().toUri();
		ResponseEntity<String> response = this.testRestTemplate.getForEntity(uri, String.class);
		System.out.print("Testing: " + uri + " ==> Response:" + response);
		assertEquals(response.getStatusCode().compareTo(testData.expectedStatusCode), 0, "Unexpected status code received (codes following are NOT the status codes)");
		boolean success = true;
		if (response.getStatusCode().compareTo(HttpStatus.OK) == 0) {
			success = isExpectedJsonResult(response.getBody(), testData.expectedValue);
		}
		System.out.println(success ? " - Test succeeded" : " - Test failed");
		return success;
	}
	
	public boolean isExpectedJsonResult(String jsonResult, Object expectedValue) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonResult);
		} catch (JSONException e) {
			e.printStackTrace();
			fail("Http result is not JSON format " + jsonResult);
		}
		try {
			JSONAssert.assertEquals("Operation received = " + jsonObject.getString(ResultResponse.OPERATION_KEY_NAME) + " but expected " + this.operationName,
									"{"+ResultResponse.OPERATION_KEY_NAME+":"+this.operationName+"}", 					
									jsonResult,
								    JSONCompareMode.LENIENT);
		} catch (JSONException e1) {
			e1.printStackTrace();
			fail("JSONException for operation check - " + jsonResult);
		}
		
		try {
			JSONAssert.assertEquals("Value received = " + jsonObject.get(ResultResponse.VALUE_KEY_NAME) + " but expected " + expectedValue,
									"{"+ResultResponse.VALUE_KEY_NAME+":"+ makeJsonFieldString(expectedValue)+ "}", 
									jsonResult, 
									JSONCompareMode.LENIENT);
		} catch (JSONException e1) {
			e1.printStackTrace();
			fail("JSONException for value check - " + jsonResult);
		}
		return true;
	}

	private String makeJsonFieldString (Object value) {
		return value instanceof String ? JSONObject.quote((String)value) : value.toString();
	}
}
