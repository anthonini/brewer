package com.anthonini.brewer.controller.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.anthonini.brewer.service.exception.StyleNameAlreadyRegisteredException;

@ControllerAdvice
public class ControllerAdviceExceptionHandler {

	@ExceptionHandler(StyleNameAlreadyRegisteredException.class)
	public ResponseEntity<String> handlerStyleNameAlreadyRegisteredException(StyleNameAlreadyRegisteredException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
