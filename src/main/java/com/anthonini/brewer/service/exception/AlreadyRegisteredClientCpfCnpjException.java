package com.anthonini.brewer.service.exception;

public class AlreadyRegisteredClientCpfCnpjException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AlreadyRegisteredClientCpfCnpjException(String message) {
		super(message);
	}
}
