package br.com.hubfintech.authorization.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.hubfintech.authorization.entities.AuthorizationRequestData;

@Repository
public interface AuthorizationRequestDataJpaRepository extends JpaRepository<AuthorizationRequestData, Long>{

}
