package br.com.hubfintech.authorization.worker;

import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.hubfintech.authorization.dto.AuthorizationRequest;
import br.com.hubfintech.authorization.dto.AuthorizationResponse;
import br.com.hubfintech.authorization.interfaces.AuthorizationService;

@Component
public final class AuthorizationWorker {

	@Autowired
	private Queue<AuthorizationRequest> requestQueue;

	@Autowired
	private Queue<AuthorizationResponse> responseQueue;

	@Autowired
	private AuthorizationService authorizationService;

	public void start() {
		while (true) {
			final AuthorizationRequest authorizationRequest = requestQueue.poll();
			if (authorizationRequest != null) {
				final AuthorizationResponse authorizationResponse = authorizationService
						.authorizationFlow(authorizationRequest);
				responseQueue.add(authorizationResponse);
			}
		}
	}
}
