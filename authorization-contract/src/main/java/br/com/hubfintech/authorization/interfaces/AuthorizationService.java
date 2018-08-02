package br.com.hubfintech.authorization.interfaces;

import java.math.BigDecimal;

import org.springframework.data.util.Pair;

import br.com.hubfintech.authorization.dto.AuthorizationRequest;
import br.com.hubfintech.authorization.dto.AuthorizationResponse;

public interface AuthorizationService {

	public default AuthorizationResponse authorizationFlow(final AuthorizationRequest authorizationRequest) {
		try {
			persist(authorizationRequest);
			final Pair<AuthorizationResponse, BigDecimal> pair = authorization(authorizationRequest);

			if ("00".equals(pair.getFirst().getAuthorizationCode())) {
				decreaseBalance(authorizationRequest, pair.getSecond());
			}

			return pair //
					.getFirst() //
					.toBuilder() //
					.uuid(authorizationRequest.getUuid()) //
					.build();
		} catch (Throwable e) {
			return AuthorizationResponseSystemError.create(authorizationRequest);
		}
	}

	public void persist(final AuthorizationRequest authorizationRequest);

	public void decreaseBalance(final AuthorizationRequest authorizationRequest, final BigDecimal accountBalance);

	public Pair<AuthorizationResponse, BigDecimal> authorization(final AuthorizationRequest authorizationRequest);
}
