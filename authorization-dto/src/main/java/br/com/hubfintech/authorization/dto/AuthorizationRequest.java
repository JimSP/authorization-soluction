package br.com.hubfintech.authorization.dto;

import java.io.Serializable;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder(toBuilder = true)
@ToString(exclude = "cardNumber")
public final class AuthorizationRequest implements Serializable {

	private static final long serialVersionUID = 6023525608874165452L;

	@NotNull
	private final Action action;

	@NotBlank
	private final String cardNumber;

	@NotBlank
	private final String amount;

	private final UUID uuid;

	@JsonCreator
	public AuthorizationRequest( //
			@JsonProperty("action") final Action action, //
			@JsonProperty("cardnumber") final String cardNumber, //
			@JsonProperty("amount") final String amount, //
			@JsonProperty(value="uuid", required = false) final UUID uuid) {
		this.action = action;
		this.cardNumber = cardNumber;
		this.amount = amount;
		this.uuid = uuid;
	}
}
