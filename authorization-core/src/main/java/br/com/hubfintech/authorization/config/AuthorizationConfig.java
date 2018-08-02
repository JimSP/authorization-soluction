package br.com.hubfintech.authorization.config;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import br.com.hubfintech.authorization.converters.AuthorizationRequestToAuthorizationDataConverter;
import br.com.hubfintech.authorization.converters.StringToBigDecimalConverter;
import br.com.hubfintech.authorization.dto.AuthorizationRequest;
import br.com.hubfintech.authorization.dto.AuthorizationResponse;

@Configuration
@EnableAsync
@EnableTransactionManagement
@EnableJpaRepositories(basePackages="br.com.hubfintech.authorization.repositories")
public class AuthorizationConfig {

	private static final int LIMIT_CODES = 1000000;

	private static final AtomicInteger SERVER_COUNT = new AtomicInteger(0);

	@Autowired
	private AuthorizationRequestToAuthorizationDataConverter authorizationRequestToAuthorizationDataConverter;

	@Autowired
	private StringToBigDecimalConverter stringToBigDecimalConverter;

	private final static String[] AUTHORIZATION_CODES = createAuthorizationCodes();

	private static String[] createAuthorizationCodes() {
		final String[] authorizationCodes = new String[LIMIT_CODES];
		int i = 0;
		while (i < LIMIT_CODES) {
			authorizationCodes[i] = String.format("%06d", i++);
		}
		return authorizationCodes;
	}

	@Bean
	public ConversionService conversionService() {
		final DefaultConversionService defaultConversionService = new DefaultConversionService();
		defaultConversionService.addConverter(authorizationRequestToAuthorizationDataConverter);
		defaultConversionService.addConverter(stringToBigDecimalConverter);
		return defaultConversionService;
	}

	@Bean
	public Function<AuthorizationRequest, AuthorizationResponse> aprove() {

		if (SERVER_COUNT.get() == 1000000) {
			SERVER_COUNT.set(0);
		}

		return authorizationRequest -> AuthorizationResponse //
				.builder() //
				.action(authorizationRequest.getAction()) //
				.authorizationCode(AUTHORIZATION_CODES[SERVER_COUNT.getAndIncrement()]) //
				.code("00") //
				.uuid(authorizationRequest.getUuid()) //
				.build();
	}

	@Bean
	public Function<AuthorizationRequest, AuthorizationResponse> notAuthotized() {
		return authorizationRequest -> AuthorizationResponse //
				.builder() //
				.code("51") //
				.action(authorizationRequest.getAction()) //
				.uuid(authorizationRequest.getUuid()) //
				.build();
	}

	@Bean
	public Function<AuthorizationRequest, AuthorizationResponse> invalidAccountNumber() {
		return authorizationRequest -> AuthorizationResponse //
				.builder() //
				.action(authorizationRequest.getAction()) //
				.uuid(authorizationRequest.getUuid()) //
				.build();
	}

	@Bean
	public Executor asyncExecutor() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("Authorization-Core-");
		executor.initialize();
		return executor;
	}
}
