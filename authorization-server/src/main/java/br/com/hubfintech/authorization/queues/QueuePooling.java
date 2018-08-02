package br.com.hubfintech.authorization.queues;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;

@Scope(value = "singleton")
@Component
public class QueuePooling {
	
	private final Map<UUID, ChannelHandlerContext> mapChannelHandlerContext = Collections.synchronizedMap(new HashMap<>());

	public Map<UUID, ChannelHandlerContext> getMapChannelHandlerContext() {
		return mapChannelHandlerContext;
	}

}