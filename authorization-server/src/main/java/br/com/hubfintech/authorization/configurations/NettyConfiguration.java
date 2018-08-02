package br.com.hubfintech.authorization.configurations;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hubfintech.authorization.codec.AuthorizationRequestDecoder;
import br.com.hubfintech.authorization.codec.AuthorizationRequestEncoder;
import br.com.hubfintech.authorization.tcp.AuthotizationTcpServerHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Configuration
@EnableConfigurationProperties(NettyProperties.class)
public class NettyConfiguration {

	@Autowired
	private AuthotizationTcpServerHandle authotizationTcpServerHandle;

	@Autowired
	private NettyProperties nettyProperties;

	@Autowired
	private AuthorizationRequestEncoder authorizationRequestEncoder;
	
	@Autowired
	private ObjectMapper objectMapper;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public ServerBootstrap serverBootstrap() {
		
		final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		final Validator validator = factory.getValidator();
		
		final ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup(), workerGroup()) //
				.channel(NioServerSocketChannel.class) //
				.handler(new LoggingHandler(LogLevel.DEBUG)) //
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch //
								.pipeline() //
								.addLast(new AuthorizationRequestDecoder(objectMapper, validator), //
										authorizationRequestEncoder, //
										authotizationTcpServerHandle);

					}
				});

		final Map<ChannelOption, Object> tcpChannelOptions = tcpChannelOptions();

		tcpChannelOptions //
				.keySet() //
				.forEach(option -> {
					serverBootstrap //
							.option(option, tcpChannelOptions.get(option));
				});

		return serverBootstrap;
	}

	@SuppressWarnings("rawtypes")
	@Bean
	public Map<ChannelOption, Object> tcpChannelOptions() {
		Map<ChannelOption, Object> options = new HashMap<>();
		options.put(ChannelOption.SO_BACKLOG, nettyProperties.getBacklog());
		return options;
	}

	@Bean(destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup bossGroup() {
		return new NioEventLoopGroup(nettyProperties.getBossCount());
	}

	@Bean(destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup workerGroup() {
		return new NioEventLoopGroup(nettyProperties.getWorkerCount());
	}

	@Bean
	public InetSocketAddress tcpSocketAddress() {
		return new InetSocketAddress(nettyProperties.getTcpPort());
	}
}
