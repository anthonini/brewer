package com.anthonini.brewer.service.exception;

public class CityAlreadyRegisteredException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CityAlreadyRegisteredException(String message) {
		super(message);
	}
}
