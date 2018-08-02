package br.com.hubfintech.authorization.tcp;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.com.hubfintech.authorization.exception.AuthorizationWithProblemException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;

@Component
public class AuthorizationTcpServer {

	@Autowired
	private ServerBootstrap serverBootstrap;

	@Autowired
	private InetSocketAddress tcpPort;

	@Async
	public Channel start() {
		try {
			return serverBootstrap //
					.bind(tcpPort) //
					.sync() //
					.channel() //
					.closeFuture() //
					.sync() //
					.channel();
		} catch (InterruptedException e) {
			throw new AuthorizationWithProblemException(e);
		}
	}
}
