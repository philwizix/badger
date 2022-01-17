package com.philiprushby.techassessment;


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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ModuloTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	private OperationTestHelper testHelper;
	private String uri;
	
	private Long maxIntPlus1 = (Long)(1L*(Integer.MAX_VALUE)+1);
			
	private Stream<ModuloTestData> streamOfTestData = Stream.of(new ModuloTestData(HttpStatus.BAD_REQUEST, null, "10", "0"),
																new ModuloTestData(HttpStatus.BAD_REQUEST, null, "10X", "1"),																
																new ModuloTestData(HttpStatus.BAD_REQUEST, null, "10", null),
																new ModuloTestData(HttpStatus.BAD_REQUEST, null, null, "2"),
																new ModuloTestData(HttpStatus.BAD_REQUEST, null, null, null),
																new ModuloTestData(HttpStatus.OK, 2L, "10", "4"),
																new ModuloTestData(HttpStatus.OK, 0L, "10", "5"),	
																new ModuloTestData(HttpStatus.OK, 0L, "10", "10"),	
																new ModuloTestData(HttpStatus.OK, 10L, "10", "15"),
																new ModuloTestData(HttpStatus.OK, maxIntPlus1-1, ((Long)(2*maxIntPlus1-1)).toString(), maxIntPlus1.toString()),
																new ModuloTestData(HttpStatus.OK, -1L, "-11", "5"),
																new ModuloTestData(HttpStatus.OK, -2L, "-12", "-5")
																);

	
	@PostConstruct
    public void init() {
		testHelper = new OperationTestHelper("modulo", testRestTemplate);
        uri = "http://localhost:"+port+ "/" + testHelper.operationName;
    }
	
	@Test
	public void moduloTest() throws Exception {
		streamOfTestData.allMatch(testData-> 
			{ 
				UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(uri);
			    testHelper.addParameterToUriBuilder (uriBuilder, TechnicalAssessmentController.MODULO_DIVIDEND_PARM_NAME, testData.inputDividend);
			    testHelper.addParameterToUriBuilder (uriBuilder, TechnicalAssessmentController.MODULO_DIVISOR_PARM_NAME, testData.inputDivisor);
			    return testHelper.fetchAndTestResponse(uriBuilder, testData);
			});	
	}	
	
	class ModuloTestData extends TestData{
		String inputDividend;
		String inputDivisor;		
		
		public ModuloTestData(HttpStatus expectedStatusCode, Long expectedValue, String dividend, String divisor) {
			super(expectedStatusCode, expectedValue);
			this.inputDividend = dividend;
			this.inputDivisor = divisor;
		}		
	}			
}
