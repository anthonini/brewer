package com.anthonini.brewer.service.exception;

public class UserCannotRemoveYourselfException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserCannotRemoveYourselfException() {
		super("Usuário não pode remover ou inativar a si mesmo!");
	}
}
