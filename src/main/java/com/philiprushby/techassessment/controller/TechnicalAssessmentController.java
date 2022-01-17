package com.philiprushby.techassessment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.philiprushby.techassessment.payload.ResultResponse;
import com.philiprushby.techassessment.service.TechnicalAssessmentService;

import io.swagger.annotations.ApiParam;

@RestController

public class TechnicalAssessmentController {
	
	public final static String CONCAT_PARM_NAME = "s";
	public final static String ADDITION_PARM_NAME = "n";
	public final static String MODULO_DIVIDEND_PARM_NAME = "a";
	public final static String MODULO_DIVISOR_PARM_NAME = "b";
	
	@Autowired
	private TechnicalAssessmentService technicalAssessmentService;
	
	@GetMapping("/concat")
	public ResultResponse concat(
			@ApiParam(
				    type = "String",
				    value = "One for each string to be concatenated",
				    allowMultiple = true,
				    example = "abcd",
				    required = true)
			@RequestParam(value=CONCAT_PARM_NAME) String[]strings) {
		return new ResultResponse("concat", technicalAssessmentService.concat(strings));
	}
	
	@GetMapping("/addition")
	public ResultResponse addition(
			@ApiParam(
				    type = "Number",
				    value = "One for each number to be summed",
				    allowMultiple = true,
				    example = "123.45",
				    required = true)
			@RequestParam(value=ADDITION_PARM_NAME)String[]strings) {
		return new ResultResponse ("addition", technicalAssessmentService.addition(strings));
	}
	
	@GetMapping("/modulo")
	public ResultResponse modulo(
			@ApiParam(
				    type = "Integer",
				    value = "Numerator",
				    allowMultiple = false,
				    example = "1234",
				    required = true)
			@RequestParam(value=MODULO_DIVIDEND_PARM_NAME)String n, 
			@ApiParam(
				    //name = "n",
				    type = "Integer",
				    value = "Non-zero divisor",
				    example = "12",
				    required = true)			
			@RequestParam(value=MODULO_DIVISOR_PARM_NAME)String d) throws Exception {
		return new ResultResponse ("modulo", technicalAssessmentService.modulo(n, d));
	}
}
