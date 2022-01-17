package com.philiprushby.techassessment;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.philiprushby.techassessment.controller.TechnicalAssessmentController;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ControllerLoadTest {

	@Autowired
	private TechnicalAssessmentController controller;

	
	@Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

}
