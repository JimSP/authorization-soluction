package br.com.hubfintech.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.hubfintech.authorization.service.Create2Account;
import br.com.hubfintech.authorization.worker.AuthorizationWorker;

@Component
public class AuthorizationCoreRunner implements CommandLineRunner{

	@Autowired
	private Create2Account create2Account;
	
	@Autowired
	private AuthorizationWorker authorizationWorker;
	
	@Override
	public void run(String... args) throws Exception {
		create2Account.create();
		authorizationWorker.start();
	}
}
