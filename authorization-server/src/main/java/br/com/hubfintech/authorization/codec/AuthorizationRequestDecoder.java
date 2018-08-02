package br.com.hubfintech.authorization.codec;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hubfintech.authorization.dto.AuthorizationRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public final class AuthorizationRequestDecoder extends ByteToMessageDecoder {

	private final ObjectMapper objectMapper;

	public AuthorizationRequestDecoder(ObjectMapper objectMapper) {
		super();
		this.objectMapper = objectMapper;
	}

	@Override
	protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
		final byte[] message = new byte[1024];
		in.readBytes(message);
		final AuthorizationRequest authorizationRequest = objectMapper.readValue(message, AuthorizationRequest.class);
		out.add(authorizationRequest);
	}
}
