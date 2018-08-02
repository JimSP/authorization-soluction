package br.com.hubfintech.authorization.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.hubfintech.authorization.entities.AccountBalanceData;

@Repository
public interface AccountBalanceJpaRepository extends JpaRepository<AccountBalanceData, Long>{

	public Optional<AccountBalanceData> findByCardNumber(final String cardNumber);

}
