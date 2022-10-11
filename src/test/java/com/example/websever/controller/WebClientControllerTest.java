package com.example.websever.controller;

import com.example.websever.dto.Worker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebClientControllerTest {

	@Autowired
	private WebClient webClient;

	@Test
	public void api호출테스트성공() {
	    // given
		final String url = "http://localhost:5011/webclient/test-builder";
	    
	    // when
		Worker worker = webClient
			.get()
			.uri(url)
			.retrieve()
			.bodyToMono(Worker.class)
			.flux()
			.toStream()
			.findFirst()
			.orElse(null);

		// then
		Assertions.assertThat(worker.getName()).isEqualTo("한정택");
	}

}