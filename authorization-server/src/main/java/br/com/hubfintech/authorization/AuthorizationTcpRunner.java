package br.com.hubfintech.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.hubfintech.authorization.queues.Receiver;
import br.com.hubfintech.authorization.tcp.AuthorizationTcpServer;

@Component
public class AuthorizationTcpRunner implements CommandLineRunner {

	@Autowired
	private AuthorizationTcpServer authorizationTcpServer;

	@Autowired
	private Receiver receiver;

	@Override
	public void run(String... args) throws Exception {
		receiver.start();
		authorizationTcpServer.start();
	}
}
