package br.com.hubfintech.authorization.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.hubfintech.authorization.dto.AuthorizationRequest;
import br.com.hubfintech.authorization.entities.AuthorizationRequestData;

@Component
public class AuthorizationRequestToAuthorizationDataConverter
		implements Converter<AuthorizationRequest, AuthorizationRequestData> {

	@Autowired
	private StringToBigDecimalConverter stringToBigDecimal;
	
	@Override
	public AuthorizationRequestData convert(final AuthorizationRequest authorizationRequest) {

		return AuthorizationRequestData //
				.builder() //
				.action(authorizationRequest.getAction().name()) //
				.amount(stringToBigDecimal.convert(authorizationRequest.getAmount())) //
				.cardNumber(authorizationRequest.getCardNumber()) //
				.uuidRequest(authorizationRequest.getUuid().toString())
				.build();
	}

}
