package br.com.hubfintech.authorization.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.util.Pair;

import br.com.hubfintech.authorization.dto.Action;
import br.com.hubfintech.authorization.dto.AuthorizationRequest;
import br.com.hubfintech.authorization.dto.AuthorizationResponse;
import br.com.hubfintech.authorization.entities.AccountBalanceData;
import br.com.hubfintech.authorization.entities.AuthorizationRequestData;
import br.com.hubfintech.authorization.repositories.AccountBalanceJpaRepository;
import br.com.hubfintech.authorization.repositories.AuthorizationRequestDataJpaRepository;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationServiceImplTest {

	@InjectMocks
	private AuthorizationServiceImpl authorizationServiceImpl;

	@Mock
	private AuthorizationRequestDataJpaRepository authorizationRequestDataJpaRepository;

	@Mock
	private AccountBalanceJpaRepository accountBalanceJpaRepository;

	@Mock
	private ConversionService conversionService;

	@Mock
	private Function<AuthorizationRequest, AuthorizationResponse> aprove;

	@Mock
	private Function<AuthorizationRequest, AuthorizationResponse> notAuthotized;

	@Mock
	private Function<AuthorizationRequest, AuthorizationResponse> invalidAccountNumber;

	@Test
	public void persistTest() {

		final AuthorizationRequest authorizationRequest = createAuthorizationRequest();
		final AuthorizationRequestData authorizationRequestData = createAuthorizationRequestData();

		Mockito //
				.when(conversionService //
						.convert(authorizationRequest, //
								AuthorizationRequestData.class))
				.thenReturn(authorizationRequestData);

		Mockito //
				.when(authorizationRequestDataJpaRepository //
						.save(authorizationRequestData)) //
				.thenReturn(authorizationRequestData);

		authorizationServiceImpl.persist(authorizationRequest);
	}

	private AuthorizationRequestData createAuthorizationRequestData() {
		return AuthorizationRequestData //
				.builder() //
				.action("withdraw") //
				.amount(new BigDecimal("10.01")) //
				.cardNumber("1234567891234567") //
				.build();
	}

	private AuthorizationRequest createAuthorizationRequest() {
		final AuthorizationRequest authorizationRequest = AuthorizationRequest //
				.builder() //
				.action(Action.withdraw) //
				.amount("10,01") //
				.cardNumber("1234567891234567") //
				.uuid(UUID.randomUUID()) //
				.build();
		return authorizationRequest;
	}

	@Test
	public void authorizationTestOK() {
		final AuthorizationRequest authorizationRequest = createAuthorizationRequest();
		final AccountBalanceData accountBalanceData = createAccountBalanceData();

		Mockito //
				.when(conversionService.convert(authorizationRequest //
						.getAmount(), BigDecimal.class))
				.thenReturn(new BigDecimal(authorizationRequest //
						.getAmount() //
						.replace(',', '.')));

		Mockito //
				.when(accountBalanceJpaRepository.findByCardNumber(authorizationRequest.getCardNumber()))
				.thenReturn(Optional.of(accountBalanceData));

		Mockito //
				.when(aprove.apply(authorizationRequest)) //
				.thenReturn(AuthorizationResponse //
						.builder() //
						.action(Action.withdraw) //
						.authorizationCode("000000") //
						.code("00") //
						.build());

		Mockito //
				.when(notAuthotized.apply(authorizationRequest)) //
				.thenReturn(AuthorizationResponse //
						.builder() //
						.action(Action.withdraw) //
						.authorizationCode("000000") //
						.code("01") //
						.build());

		final Pair<AuthorizationResponse, BigDecimal> pair = authorizationServiceImpl
				.authorization(authorizationRequest);

		Assert.assertEquals(Action.withdraw, pair.getFirst().getAction());
		Assert.assertEquals("000000", pair.getFirst().getAuthorizationCode());
		Assert.assertEquals("00", pair.getFirst().getCode());
		Assert.assertEquals(new BigDecimal("100.01"), pair.getSecond());
	}

	@Test
	public void authorizationTestInvalidAccountNumber() {
		final AuthorizationRequest authorizationRequest = createAuthorizationRequest();

		Mockito //
				.when(conversionService.convert(authorizationRequest //
						.getAmount(), BigDecimal.class))
				.thenReturn(new BigDecimal(authorizationRequest //
						.getAmount() //
						.replace(',', '.')));

		Mockito //
				.when(accountBalanceJpaRepository.findByCardNumber(authorizationRequest.getCardNumber()))
				.thenReturn(Optional.empty());

		Mockito //
				.when(invalidAccountNumber.apply(authorizationRequest)) //
				.thenReturn(AuthorizationResponse //
						.builder() //
						.action(Action.withdraw) //
						.code("01").build());

		//
		final Pair<AuthorizationResponse, BigDecimal> pair = authorizationServiceImpl
				.authorization(authorizationRequest);

		Assert.assertEquals(Action.withdraw, pair.getFirst().getAction());
		Assert.assertEquals("01", pair.getFirst().getCode());
		Assert.assertEquals(BigDecimal.ZERO, pair.getSecond());
	}

	@Test
	public void authorizationTestNotAuthorization() {
		final AuthorizationRequest authorizationRequest = createAuthorizationRequest() //
				.toBuilder() //
				.amount("10000.00") //
				.build();

		final AccountBalanceData accountBalanceData = createAccountBalanceData();

		Mockito //
				.when(conversionService.convert(authorizationRequest //
						.getAmount(), BigDecimal.class))
				.thenReturn(new BigDecimal(authorizationRequest //
						.getAmount() //
						.replace(',', '.')));

		Mockito //
				.when(accountBalanceJpaRepository.findByCardNumber(authorizationRequest.getCardNumber()))
				.thenReturn(Optional.of(accountBalanceData));

		Mockito //
				.when(notAuthotized.apply(authorizationRequest)) //
				.thenReturn(AuthorizationResponse //
						.builder() //
						.action(Action.withdraw) //
						.code("01") //
						.build());

		//
		final Pair<AuthorizationResponse, BigDecimal> pair = authorizationServiceImpl
				.authorization(authorizationRequest);

		Assert.assertEquals(Action.withdraw, pair.getFirst().getAction());
		Assert.assertEquals("01", pair.getFirst().getCode());
		Assert.assertEquals(new BigDecimal("100.01"), pair.getSecond());
	}

	@Test
	public void testeDecreaseBalance() {

		final AuthorizationRequest authorizationRequest = createAuthorizationRequest();

		Mockito //
				.when(conversionService.convert(authorizationRequest //
						.getAmount(), BigDecimal.class))
				.thenReturn(new BigDecimal(authorizationRequest //
						.getAmount() //
						.replace(',', '.')));

		authorizationServiceImpl.decreaseBalance(authorizationRequest, new BigDecimal("10.00"));
	}

	private AccountBalanceData createAccountBalanceData() {
		return AccountBalanceData //
				.builder() //
				.balance(new BigDecimal("100.01")) //
				.cardNumber("1234567891234567") //
				.build();
	}

}
