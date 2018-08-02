package br.com.hubfintech.authorization.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.util.Pair;

import br.com.hubfintech.authorization.dto.Action;
import br.com.hubfintech.authorization.dto.AuthorizationRequest;
import br.com.hubfintech.authorization.dto.AuthorizationResponse;
import br.com.hubfintech.authorization.interfaces.AuthorizationService;

public class AuthorizationServiceTest {

	final AuthorizationService authorizationService = new AuthorizationService() {

		@Override
		public void persist(final AuthorizationRequest authorizationRequest) {
			Assert.assertNotNull(authorizationRequest);
			Assert.assertNotNull(authorizationRequest.getAmount());
			Assert.assertNotNull(authorizationRequest.getCardNumber());
			Assert.assertNotNull(authorizationRequest.getAction());
			Assert.assertNotNull(authorizationRequest.getUuid());
		}

		@Override
		public void decreaseBalance(final AuthorizationRequest authorizationRequest, final BigDecimal accountBalance) {
			Assert.assertNotNull(authorizationRequest);
			Assert.assertNotNull(authorizationRequest.getAmount());
			Assert.assertNotNull(authorizationRequest.getCardNumber());
			Assert.assertNotNull(authorizationRequest.getAction());
			Assert.assertNotNull(authorizationRequest.getUuid());

			Assert.assertEquals(new BigDecimal("100.00"), accountBalance);
		}

		@Override
		public Pair<AuthorizationResponse, BigDecimal> authorization(final AuthorizationRequest authorizationRequest) {

			Assert.assertNotNull(authorizationRequest);
			Assert.assertNotNull(authorizationRequest.getAmount());
			Assert.assertNotNull(authorizationRequest.getCardNumber());
			Assert.assertNotNull(authorizationRequest.getAction());
			Assert.assertNotNull(authorizationRequest.getUuid());

			return Pair.of(AuthorizationResponse //
					.builder() //
					.action(authorizationRequest //
							.getAction()) //
					.authorizationCode("000000") //
					.code("00") //
					.build(), //
					new BigDecimal("100.00"));
		}
	};

	@Test
	public void test() {
		authorizationService //
				.authorizationFlow( //
						AuthorizationRequest //
								.builder() //
								.action(Action.withdraw) //
								.amount("10,01") //
								.cardNumber("1234567891234567") //
								.uuid(UUID.randomUUID()) //
								.build());
	}

}
