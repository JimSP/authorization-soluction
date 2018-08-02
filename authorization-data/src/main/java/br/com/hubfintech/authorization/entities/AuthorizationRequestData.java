package br.com.hubfintech.authorization.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@ToString(exclude = "cardNumber")
public class AuthorizationRequestData extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 5392316609657728920L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 10)
	private String action;

	@Column(nullable = false, length = 19)
	private String cardNumber;

	@Column(nullable = false, length=18)
	private BigDecimal amount;

	@Column(nullable = false, length=36)
	private String uuidRequest;
}
