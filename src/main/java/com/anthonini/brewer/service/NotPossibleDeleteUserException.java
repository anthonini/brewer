package com.anthonini.brewer.service;

public class NotPossibleDeleteUserException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NotPossibleDeleteUserException(String message) {
		super(message);
	}

}
