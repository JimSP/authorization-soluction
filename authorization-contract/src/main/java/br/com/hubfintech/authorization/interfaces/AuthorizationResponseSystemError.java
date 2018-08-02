package br.com.hubfintech.authorization.interfaces;

import br.com.hubfintech.authorization.dto.AuthorizationRequest;
import br.com.hubfintech.authorization.dto.AuthorizationResponse;

public final class AuthorizationResponseSystemError {
	
	public static AuthorizationResponse create(final AuthorizationRequest authorizationRequest) {
		return AuthorizationResponse //
				.builder() //
				.action(authorizationRequest.getAction()) //
				.uuid(authorizationRequest.getUuid()) //
				.code("96") //
				.build();
	}
	
	public static AuthorizationResponse create() {
		return AuthorizationResponse //
				.builder() //
				.code("96") //
				.build();
	}

}
