package br.com.hubfintech.authorization.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.hubfintech.authorization.dto.AuthorizationRequest;
import br.com.hubfintech.authorization.dto.AuthorizationResponse;
import br.com.hubfintech.authorization.entities.AccountBalanceData;
import br.com.hubfintech.authorization.entities.AuthorizationRequestData;
import br.com.hubfintech.authorization.interfaces.AuthorizationService;
import br.com.hubfintech.authorization.repositories.AccountBalanceJpaRepository;
import br.com.hubfintech.authorization.repositories.AuthorizationRequestDataJpaRepository;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

	@Autowired
	private AuthorizationRequestDataJpaRepository authorizationRequestDataJpaRepository;

	@Autowired
	private AccountBalanceJpaRepository accountBalanceJpaRepository;

	@Autowired
	private ConversionService conversionService;

	@Autowired
	private Function<AuthorizationRequest, AuthorizationResponse> aprove;

	@Autowired
	private Function<AuthorizationRequest, AuthorizationResponse> notAuthotized;

	@Autowired
	private Function<AuthorizationRequest, AuthorizationResponse> invalidAccountNumber;

	@Transactional
	@Async
	public void persist(final AuthorizationRequest authorizationRequest) {
		final AuthorizationRequestData authorizationRequestData = conversionService //
				.convert(authorizationRequest, //
						AuthorizationRequestData.class);

		authorizationRequestDataJpaRepository //
				.save(authorizationRequestData);
	}

	@Transactional
	@Async
	public void decreaseBalance(final AuthorizationRequest authorizationRequest, final BigDecimal accountBalance) {
		final String cardNumber = authorizationRequest.getCardNumber();

		final BigDecimal balance = accountBalance //
				.subtract(conversionService.convert(authorizationRequest //
						.getAmount(), BigDecimal.class)); //

		accountBalanceJpaRepository //
				.save(AccountBalanceData //
						.builder() //
						.cardNumber(cardNumber) //
						.balance(balance) //
						.build());
	}

	public Pair<AuthorizationResponse, BigDecimal> authorization(final AuthorizationRequest authorizationRequest) {
		final String cardNumber = authorizationRequest.getCardNumber();
		final BigDecimal ammount = conversionService.convert(authorizationRequest //
				.getAmount(), BigDecimal.class);

		final Optional<AccountBalanceData> accountBalanceDataOptional = accountBalanceJpaRepository
				.findByCardNumber(cardNumber);

		if (accountBalanceDataOptional.isPresent()) {
			return Pair //
					.of(accountBalanceDataOptional //
							.filter(predicate -> predicate //
									.getBalance() //
									.compareTo(ammount) > 0) //
							.map(mapper -> aprove.apply(authorizationRequest)) //
							.orElse(notAuthotized.apply(authorizationRequest)) //
							, accountBalanceDataOptional //
									.get() //
									.getBalance());
		} else {
			return Pair //
					.of(invalidAccountNumber.apply(authorizationRequest), BigDecimal.ZERO);
		}
	}
}
