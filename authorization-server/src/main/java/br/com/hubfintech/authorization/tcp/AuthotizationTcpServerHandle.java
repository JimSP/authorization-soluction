package br.com.hubfintech.authorization.tcp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.hubfintech.authorization.dto.AuthorizationRequest;
import br.com.hubfintech.authorization.queues.Sender;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Component
@ChannelHandler.Sharable
@Slf4j
public class AuthotizationTcpServerHandle extends ChannelInboundHandlerAdapter {

	@Autowired
	private Sender sender;

	@Override
	public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
		log.info("m=channelRead, ctx={}, msg={}", ctx, msg);
		
		if(msg instanceof AuthorizationRequest) {
			final AuthorizationRequest authorizationRequest = (AuthorizationRequest) msg;
			sender.submit(ctx, authorizationRequest);
		}
	}
}
