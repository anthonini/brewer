package com.anthonini.brewer.service;

public class UserPasswordRequiredException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserPasswordRequiredException(String message) {
		super(message);
	}
}
