package br.com.hubfintech.authorization.queues;

import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.com.hubfintech.authorization.dto.AuthorizationResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

@Component
public class Receiver {

	@Autowired
	private QueuePooling queuePooling;

	@Autowired
	private Queue<AuthorizationResponse> responseQueue;

	@Async
	public void start() {
		while (true) {
			final AuthorizationResponse authorizationResponse = responseQueue.poll();
			if (authorizationResponse != null) {
				
				final ChannelHandlerContext channelHandlerContext = queuePooling //
						.getMapChannelHandlerContext() //
						.get(authorizationResponse.getUuid());
				
				final ChannelFuture future = channelHandlerContext.writeAndFlush(authorizationResponse);
				future.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
			}
		}
	}
}
