package com.example.websever.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

	@Bean
	public WebClient customWebClient() {
		return WebClient.builder()
			.clientConnector(new ReactorClientHttpConnector(httpClient()))
			.exchangeStrategies(exchangeStrategies())
			.build();
	}

	@Bean
	HttpClient httpClient() {
		return HttpClient.create(connectionProvider())
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
			.responseTimeout(Duration.ofMillis(5000))
			.doOnConnected(conn ->
					conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
						.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
				);
	}

	@Bean
	public ConnectionProvider connectionProvider() {
		return ConnectionProvider.builder("http-pool")
			.maxConnections(100)
			.pendingAcquireTimeout(Duration.ofMillis(0))
			.pendingAcquireMaxCount(-1)
			.maxIdleTime(Duration.ofMillis(2000L))
			.build();
	}

	@Bean
	ExchangeStrategies exchangeStrategies() {
		return ExchangeStrategies.builder()
			.codecs(config ->
				config.defaultCodecs().maxInMemorySize(1024 * 1024)
			)
			.build();
	}
}
