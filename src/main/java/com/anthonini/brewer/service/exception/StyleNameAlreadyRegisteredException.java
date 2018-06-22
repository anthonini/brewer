package com.anthonini.brewer.service.exception;

public class StyleNameAlreadyRegisteredException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public StyleNameAlreadyRegisteredException(String message) {
		super(message);
	}
}
