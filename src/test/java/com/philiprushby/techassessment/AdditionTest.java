package com.philiprushby.techassessment;


import static org.junit.jupiter.api.Assertions.fail;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import com.philiprushby.techassessment.controller.TechnicalAssessmentController;
//Note: This does not handle the spring boot bug detailed here https://github.com/spring-projects/spring-framework/issues/23820
//      The effect of this is to treat a single string submission which contains a comma as two separate strings with the comma treated as a delimiter
//      This input would give an invalid response.
//      Any workround to this has consequences which are probably not intended for this exercise.

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdditionTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;

	private OperationTestHelper testHelper;
	private String uri;
	
	private Stream<AdditionTestData> streamOfTestData = Stream.of(new AdditionTestData(HttpStatus.BAD_REQUEST, null, Arrays.asList(",", "1")),
																  new AdditionTestData(HttpStatus.BAD_REQUEST, null, Arrays.asList("&n=1")),
																  new AdditionTestData(HttpStatus.BAD_REQUEST, null, Arrays.asList("+")),	
																  new AdditionTestData(HttpStatus.OK, 1.0F, Arrays.asList("1")),
																  new AdditionTestData(HttpStatus.OK, 3.0F, Arrays.asList("1", "2")),												   
																  new AdditionTestData(HttpStatus.OK, 304.6298F, Arrays.asList("1.2", "2.75", "300.6798"))
																);
	

	@PostConstruct
    public void init() {
		testHelper = new OperationTestHelper("addition", testRestTemplate);
        uri = "http://localhost:"+port+ "/" + testHelper.operationName;
    }
	
	@Test
	public void additionTest() throws Exception {
		streamOfTestData.allMatch(testData-> 
			{ 
				UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(uri);
				testData.params.stream().forEach(param -> {
					try {
						uriBuilder.queryParam(TechnicalAssessmentController.ADDITION_PARM_NAME, URLEncoder.encode(param, StandardCharsets.UTF_8.name()));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						fail("UnsupportedEncodingException for " + param);
					}
				});
				return testHelper.fetchAndTestResponse(uriBuilder, testData);
			});	
		
	}

	class AdditionTestData extends TestData{
		List<String> params;
		
		public AdditionTestData(HttpStatus expectedStatusCode, Float expectedValue, List<String> params) {
			super(expectedStatusCode, expectedValue);
			this.params = params;
		}		
	}
		
}
