package br.com.hubfintech.authorization.tcp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class AuthorizationTcpClient {
	
	private static final int BUFFER_SIZE = 1024;
	private final AtomicInteger receiverCount = new AtomicInteger(0);

	@Test
	public void test() throws UnknownHostException, IOException {
		
		final Long init = System.currentTimeMillis();
		final byte[] request = Arrays.copyOf("{\"action\": \"withdraw\",\"cardnumber\":\"1234567890123456\",\"amount\": \"1,10\"}".getBytes(), BUFFER_SIZE);
		try(final Socket socket = new Socket("localhost", 9876)){
			socket.getOutputStream().write(request);
			byte[] response = new byte[BUFFER_SIZE];
			socket.getInputStream().read(response);
			System.out.println(new String(response));
			receiverCount.incrementAndGet();
		}finally {
			System.out.println((System.currentTimeMillis() - init));
		}
	}

}
