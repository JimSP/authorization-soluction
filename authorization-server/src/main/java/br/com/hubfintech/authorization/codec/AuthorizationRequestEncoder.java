package br.com.hubfintech.authorization.codec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hubfintech.authorization.dto.AuthorizationResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@Component
@ChannelHandler.Sharable
public class AuthorizationRequestEncoder extends MessageToByteEncoder<AuthorizationResponse> {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	protected void encode(final ChannelHandlerContext ctx, final AuthorizationResponse authorizationResponse,
			final ByteBuf out) throws Exception {
		out.writeBytes(objectMapper.writeValueAsBytes(authorizationResponse));
	}
}
