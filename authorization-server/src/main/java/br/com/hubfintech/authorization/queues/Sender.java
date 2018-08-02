package br.com.hubfintech.authorization.queues;

import java.util.Queue;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.hubfintech.authorization.dto.AuthorizationRequest;
import io.netty.channel.ChannelHandlerContext;

@Component
public class Sender {

	@Autowired
	private QueuePooling queuePooling;

	@Autowired
	private Queue<AuthorizationRequest> requestQueue;

	public void submit(final ChannelHandlerContext ctx, final AuthorizationRequest authorizationRequest) {
		final UUID uuid = UUID.randomUUID();
		
		queuePooling //
				.getMapChannelHandlerContext() //
				.put(uuid, ctx);
		
		requestQueue //
				.add(authorizationRequest //
						.toBuilder() //
						.uuid(uuid) //
						.build());
	}
}
