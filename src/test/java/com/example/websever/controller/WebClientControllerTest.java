package com.example.websever.controller;

import com.example.websever.Exception.BadWebClientRequestException;
import com.example.websever.dto.ResponseDTO;
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
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
		assertThat(worker.getName()).isEqualTo("한정택");
	}

	@Test
	public void api테스트성공_200() {
	    // given
		final String url = "http://localhost:5011/test/200";

	    // when
		ResponseDTO result = webClient
			.post()
			.uri(url)
			.retrieve()
			.bodyToMono(ResponseDTO.class)
			.flux()
			.toStream()
			.findFirst()
			.orElse(null);

	    // then
		assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	public void api테스트실패_400() {
	    // given
		final String url = "http://localhost:5011/test/400";
	    
	    // when
		try {
			webClient
				.post()
				.uri(url)
				.retrieve()
				.onStatus(status -> status.is4xxClientError()
					, clientResponse ->
						clientResponse.bodyToMono(String.class)
							.map(body -> new RuntimeException(body)))
				.bodyToMono(ResponseDTO.class)
				.flux()
				.toStream()
				.findFirst()
				.orElse(null);
		} catch (RuntimeException e) {
			// then
			return;
		}
		fail();
	}

	@Test
	public void api테스트실패_500() {
		// given
		final String url = "http://localhost:5011/test/500";

		// when
		try {
			webClient
				.post()
				.uri(url)
				.retrieve()
				.onStatus(status -> status.is5xxServerError()
					, clientResponse ->
						clientResponse.bodyToMono(String.class)
							.map(body -> new RuntimeException(body)))
				.bodyToMono(ResponseDTO.class)
				.flux()
				.toStream()
				.findFirst()
				.orElse(null);
		} catch (RuntimeException e) {
			// then
			e.printStackTrace();
			return;
		}
		fail();
	}

	@Test
	public void api테스트실패_500_timeout에러() {
		// given
		final String url = "http://localhost:5011/test/timeout";

		// when
		try {
			webClient
				.post()
				.uri(url)
				.retrieve()
				.onStatus(status -> status.is5xxServerError()
					, clientResponse ->
						clientResponse.bodyToMono(String.class)
							.map(body -> new RuntimeException(body)))
				.bodyToMono(ResponseDTO.class)
				.flux()
				.toStream()
				.findFirst()
				.orElse(null);
		} catch (WebClientException e) {
			// then
			e.printStackTrace();
			assertTrue(e.getMessage().contains("ReadTimeoutException"));

			return;
		}
		fail();
	}

}