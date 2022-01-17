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
public class ConcatTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;

	private OperationTestHelper testHelper;
	private String uri;
	
	private Stream<ConcatTestData> streamOfTestData = Stream.of(new ConcatTestData(HttpStatus.BAD_REQUEST, null, Arrays.asList()),
															    new ConcatTestData(HttpStatus.OK, "&s=1", Arrays.asList("&s=1")),
															    new ConcatTestData(HttpStatus.OK, ":+", Arrays.asList(":+")),	
															    new ConcatTestData(HttpStatus.OK, " \",+=%&123", Arrays.asList(" \",+=%&", "123")),		
															    new ConcatTestData(HttpStatus.OK, "1", Arrays.asList("1")),
															    new ConcatTestData(HttpStatus.OK, "12", Arrays.asList("1", "2")),													   
															    new ConcatTestData(HttpStatus.OK, "123", Arrays.asList("1", "2", "3")),
															    new ConcatTestData(HttpStatus.OK, "1234", Arrays.asList("1", "2", "3", "4")),
															    new ConcatTestData(HttpStatus.OK, "12345", Arrays.asList("1", "2", "3", "4", "5"))
															   );
	

	@PostConstruct
    public void init() {
		testHelper = new OperationTestHelper("concat", testRestTemplate);
        uri = "http://localhost:"+port+ "/" + testHelper.operationName;
    }
	
	@Test
	public void concatTest() throws Exception {
		streamOfTestData.allMatch(testData-> 
			{ 
			    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(uri);
			    testData.params.stream().forEach(param -> {
					try {
						uriBuilder.queryParam(TechnicalAssessmentController.CONCAT_PARM_NAME, URLEncoder.encode(param, StandardCharsets.UTF_8.name()));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						fail("UnsupportedEncodingException for " + param);
					}
				});
			    return testHelper.fetchAndTestResponse(uriBuilder, testData);
			});			
	}
	
	class ConcatTestData extends TestData{
		List<String> params;		
		
		public ConcatTestData(HttpStatus expectedStatusCode, String expectedValue, List<String> params) {
			super(expectedStatusCode, expectedValue);
			this.params = params;
		}		
	}	
		
}
