package br.com.hubfintech.authorization.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "cardNumber" }, callSuper = false)
@ToString(exclude = { "cardNumber" })
public class AccountBalanceData extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2129124534057119626L;

	@Id
	private String cardNumber;

	@Column(nullable = false, length=18, precision=4)
	private BigDecimal balance;

}
