package br.com.hubfintech.authorization.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.hubfintech.authorization.entities.AccountBalanceData;
import br.com.hubfintech.authorization.repositories.AccountBalanceJpaRepository;

@Component
public class Create2Account {
	
	@Autowired
	private AccountBalanceJpaRepository accountBalanceJpaRepository;

	@Transactional
	public void create() {
		
		final AccountBalanceData accountBalanceData1 = AccountBalanceData //
				.builder() //
				.balance(new BigDecimal("1000.00")) //
				.cardNumber("1234567890123456") //
				.build();
		
		final AccountBalanceData accountBalanceData2 = AccountBalanceData //
				.builder() //
				.balance(new BigDecimal("1000.00")) //
				.cardNumber("9876547894563687") //
				.build();
		
		accountBalanceJpaRepository.save(accountBalanceData1);
		accountBalanceJpaRepository.save(accountBalanceData2);
	}
}
