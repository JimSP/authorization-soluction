package br.com.hubfintech.authorization.configurations;

import java.util.Queue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import br.com.hubfintech.authorization.dto.AuthorizationRequest;
import br.com.hubfintech.authorization.dto.AuthorizationResponse;

@Configuration
public class HazelcastConfiguration {
	
	@Bean
	public HazelcastInstance hazelcastInstance() {
		return Hazelcast.getOrCreateHazelcastInstance(config());
	}
	
	@Bean
	public Queue<AuthorizationRequest> requestQueue(){
		return hazelcastInstance().getQueue("requestQueue");
	}
	
	@Bean
	public Queue<AuthorizationResponse> responseQueue(){
		return hazelcastInstance().getQueue("responseQueue");
	}

	private Config config() {
		return new Config("authorization-server") //
				.addQueueConfig(requestQueueConfig())
				.addQueueConfig(responseQueueConfig());
	}

	private QueueConfig requestQueueConfig() {
		return new QueueConfig("requestQueue");
	}
	
	private QueueConfig responseQueueConfig() {
		return new QueueConfig("responseQueue");
	}
}
