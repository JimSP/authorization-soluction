package br.com.hubfintech.authorization.converters;

import java.math.BigDecimal;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToBigDecimalConverter implements Converter<String, BigDecimal>{

	@Override
	public BigDecimal convert(final String ammount) {
		return new BigDecimal(ammount.replace(',', '.'));
	}

}
