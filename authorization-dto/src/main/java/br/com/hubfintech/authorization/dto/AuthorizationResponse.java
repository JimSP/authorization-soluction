package br.com.hubfintech.authorization.dto;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder=true)
public final class AuthorizationResponse implements Serializable{

	private static final long serialVersionUID = 4312071614756007799L;

	private final Action action;
	private final String code;
	private final String authorizationCode;
	
	@JsonIgnore
	private final UUID uuid;

	@JsonCreator
	public AuthorizationResponse( //
			@JsonProperty("action") final Action action, //
			@JsonProperty("code") final String code, //
			@JsonProperty("authorization_code") final String authorizationCode,
			final UUID uuid) {

		this.action = action;
		this.code = code;
		this.authorizationCode = authorizationCode;
		this.uuid = uuid;
	}
}