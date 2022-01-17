package com.philiprushby.techassessment.service;

import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.philiprushby.techassessment.exception.ClientException;

@Service

public class TechnicalAssessmentService {
	public String concat(final String[] strings) {
		StringBuilder sb = new StringBuilder();
		Arrays.asList(strings).forEach(sb::append);
		return sb.toString();
	}
	
	
	public Double addition(final String[] strings) throws ClientException {
		try {
			Stream<Double> doubles = Arrays.asList(strings).stream().map(doubleString -> Double.parseDouble(doubleString));
			return doubles.reduce(0.0, (a,b) -> a + b);
		} catch (NumberFormatException e) {
			throw new ClientException("Addition: Invalid number format");
		}
	}
	
	public Long modulo(final String n, final String d) throws ClientException {
		try {
			Long numerator = Long.parseLong(n);
			Long divisor = Long.parseLong(d);
			if (divisor == 0) {
				throw new ClientException("Modulo: Divisor may not be 0");
			}
			return numerator % divisor;
		} catch (NumberFormatException e) {
			throw new ClientException("Modulo: Invalid number format");
		}
	}
}
