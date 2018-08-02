package br.com.hubfintech.authorization.tcp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hubfintech.authorization.dto.Action;
import br.com.hubfintech.authorization.dto.AuthorizationResponse;

@Ignore //teste integrado, os dois servers devem estar em execução.
public class AuthorizationTcpClient {
	
	private static final int BUFFER_SIZE = 1024;
	
	private Socket socket;
	

	@Before
	public void before() throws UnknownHostException, IOException {
		socket = new Socket("localhost", 9876);
	}
	
	@After
	public void after() throws IOException {
		if(socket != null && !socket.isClosed()) {
			socket.close();
		}
	}

	@Test
	public void test() throws UnknownHostException, IOException {
		final ObjectMapper objectMapper = new ObjectMapper();
		
		final byte[] request = Arrays.copyOf("{\"action\": \"withdraw\",\"cardnumber\":\"1234567890123456\",\"amount\": \"1,10\"}".getBytes(), BUFFER_SIZE);
		socket.getOutputStream().write(request);
		byte[] response = new byte[BUFFER_SIZE];
		socket.getInputStream().read(response);
		Assert.assertNotNull(response);
		final AuthorizationResponse authorizationResponse = objectMapper.readValue(response, AuthorizationResponse.class);
		Assert.assertNotNull(authorizationResponse);
		Assert.assertEquals(Action.withdraw, authorizationResponse.getAction());
		Assert.assertNotNull(authorizationResponse.getAuthorizationCode());
		Assert.assertTrue(authorizationResponse.getAuthorizationCode().length() == 6);
		Assert.assertEquals("00", authorizationResponse.getCode());
	}
	
	@Test
	public void testInvalidAccountNumber() throws UnknownHostException, IOException {
		final ObjectMapper objectMapper = new ObjectMapper();
		
		final byte[] request = Arrays.copyOf("{\"action\": \"withdraw\",\"cardnumber\":\"9999999999999999\",\"amount\": \"1,10\"}".getBytes(), BUFFER_SIZE);
		socket.getOutputStream().write(request);
		byte[] response = new byte[BUFFER_SIZE];
		socket.getInputStream().read(response);
		Assert.assertNotNull(response);
		final AuthorizationResponse authorizationResponse = objectMapper.readValue(response, AuthorizationResponse.class);
		Assert.assertNotNull(authorizationResponse);
		Assert.assertEquals(Action.withdraw, authorizationResponse.getAction());
		Assert.assertEquals("14", authorizationResponse.getCode());
	}

	@Test
	public void testInsulficientBalance() throws UnknownHostException, IOException {
		final ObjectMapper objectMapper = new ObjectMapper();
		
		final byte[] request = Arrays.copyOf("{\"action\": \"withdraw\",\"cardnumber\":\"1234567890123456\",\"amount\": \"10000,00\"}".getBytes(), BUFFER_SIZE);
		socket.getOutputStream().write(request);
		byte[] response = new byte[BUFFER_SIZE];
		socket.getInputStream().read(response);
		Assert.assertNotNull(response);
		final AuthorizationResponse authorizationResponse = objectMapper.readValue(response, AuthorizationResponse.class);
		Assert.assertNotNull(authorizationResponse);
		Assert.assertEquals(Action.withdraw, authorizationResponse.getAction());
		Assert.assertEquals("51", authorizationResponse.getCode());
	}
	
	@Test
	public void testWithProblem() throws UnknownHostException, IOException {
		final ObjectMapper objectMapper = new ObjectMapper();
		
		final byte[] request = Arrays.copyOf("{\"action\": \"withdraw\",\"cardnumber\":\"AAAAAAAAAAAAAAAAAAAA\",\"amount\": \"10000,00\"}".getBytes(), BUFFER_SIZE);
		socket.getOutputStream().write(request);
		byte[] response = new byte[BUFFER_SIZE];
		socket.getInputStream().read(response);
		Assert.assertNotNull(response);
		final AuthorizationResponse authorizationResponse = objectMapper.readValue(response, AuthorizationResponse.class);
		Assert.assertNotNull(authorizationResponse);
		Assert.assertEquals("96", authorizationResponse.getCode());
	}
}
