package br.com.hubfintech.authorization.exception;

public final class AuthorizationWithProblemException extends RuntimeException{

	public AuthorizationWithProblemException(final InterruptedException e) {
		super(e);
	}

	private static final long serialVersionUID = -3879092993472539212L;

}
