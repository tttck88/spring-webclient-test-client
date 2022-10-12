package com.example.websever.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ResponseDTO {
	private final HttpStatus httpStatus;
	private final String message;
}
