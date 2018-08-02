package br.com.hubfintech.authorization.codec;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hubfintech.authorization.dto.AuthorizationRequest;
import br.com.hubfintech.authorization.interfaces.AuthorizationResponseSystemError;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class AuthorizationRequestDecoder extends ByteToMessageDecoder {

	private final ObjectMapper objectMapper;
	private final Validator validator;

	public AuthorizationRequestDecoder(ObjectMapper objectMapper, final Validator validator) {
		this.objectMapper = objectMapper;
		this.validator = validator;
	}

	@Override
	protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
		final byte[] message = new byte[1024];
		in.readBytes(message);
		final AuthorizationRequest authorizationRequest = objectMapper.readValue(message, AuthorizationRequest.class);

		final Set<ConstraintViolation<AuthorizationRequest>> constraintViolationAuthorizationRequest = validator
				.validate(authorizationRequest);
		if (constraintViolationAuthorizationRequest.isEmpty()) {
			out.add(authorizationRequest);
		} else {
			log.error("m=decode, msg=\"validation fail.\"");
			constraintViolationAuthorizationRequest //
					.stream() //
					.forEach(consumer -> {
						log.error("m=decode, constraintViolation={}", consumer);
					});

			out.add(AuthorizationResponseSystemError.create(authorizationRequest));
		}

	}
}
