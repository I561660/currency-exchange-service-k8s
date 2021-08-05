package com.in28minutes.microservices.currencyexchangeservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CurrencyExchangeCircuitBreakerController {

	private Logger log = LoggerFactory.getLogger(CurrencyExchangeCircuitBreakerController.class);
	
	@GetMapping("/sample-api")
	@Retry(name = "sample-api", fallbackMethod = "hardCodedResponse")
	@CircuitBreaker(name = "sample-api", fallbackMethod = "circuitBreakerOpenResponse")
	@RateLimiter(name="sample-api", fallbackMethod = "rateLimiterOpenResponse")
	public String sampleApi() {
		
		log.info("Sample Api Call Received");
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:8080/dummy-uri", String.class);
		
		return "Sample API";
	}
	
	public String hardCodedResponse(Exception exp) {
		return "fallback-response";
	}
	
	public String circuitBreakerOpenResponse(Exception e) {
		
		return "API Under Huge Load Now, Please Try Again Later";
	}
	
	public String rateLimiterOpenResponse(Exception e) {
		
		return "Request Rate on the API Exceed the Limitation, Please Increase the API Ability to Handle More Load";
	}
}
