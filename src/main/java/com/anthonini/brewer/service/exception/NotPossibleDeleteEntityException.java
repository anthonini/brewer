package com.anthonini.brewer.service.exception;

public class NotPossibleDeleteEntityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotPossibleDeleteEntityException(String message) {
		super(message);
	}
}
