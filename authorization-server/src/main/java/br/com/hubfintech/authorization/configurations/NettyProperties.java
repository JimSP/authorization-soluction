package br.com.hubfintech.authorization.configurations;
import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "authorization-server")
public  final class NettyProperties {

	@Value("${tcpPort:9876}")
    private Integer tcpPort;

	@Value("${bossCount:10}")
    private Integer bossCount;

	@Value("${workerCount:10}")
    private Integer workerCount;

	@Value("${keepAlive:50}")
    private Integer keepAlive;

	@Value("${backlog:5}")
    private Integer backlog;
}