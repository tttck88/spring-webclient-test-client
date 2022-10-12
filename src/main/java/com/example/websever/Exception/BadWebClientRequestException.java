package com.example.websever.Exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class BadWebClientRequestException extends RuntimeException {
	private int statusCode;

	private String statusText;

	public BadWebClientRequestException(int statusCode) {
		super();
		this.statusCode = statusCode;
	}

	public BadWebClientRequestException(int statusCode, String msg) {
		super(msg);
		this.statusCode = statusCode;
	}

	public BadWebClientRequestException(int statusCode, String msg, String statusText) {
		super(msg);
		this.statusCode = statusCode;
		this.statusText = statusText;
	}
}
