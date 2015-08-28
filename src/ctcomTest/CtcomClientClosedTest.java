package ctcomTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.naming.OperationNotSupportedException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ctcom.CtcomClient;
import ctcom.exceptions.ReadMessageException;
import ctcom.messageImpl.ConnectMessage;
import ctcom.messageImpl.CtcomMessage;
import ctcom.states.ClientState;
import ctcom.statesImpl.client.ClosedClientState;
import ctcom.statesImpl.client.SentConnectionRequestClientState;
import ctcomTest.stub.CtcomServerMock;
import ctcomTest.stub.ServerMockHelper;

public class CtcomClientClosedTest {

	private static CtcomClient client;
	private static final int port = 4745;
	private static final long waitForServerShutdownMillis = 1000;
	private static final long waitForServerInitMillis = 50;
	private static final int timeout = 1;
	
	private static Thread serverThread;
	private static CtcomServerMock server;
	private static CtcomMessage lastReceivedMessage;
	
	@BeforeClass
	public static void initialize() throws IOException, InterruptedException {
		initMockedServer();
		client = new CtcomClient("localhost", port);
	}
	
	@Before
	public void prepareForNewTest() {
		client.changeState(new ClosedClientState());
	}
	
	@AfterClass
	public static void tearDown() throws IOException, InterruptedException {
		System.out.println("Waiting for server mock to shutdown.");
		ServerMockHelper.sendMessage(client.getServerSocket(), ServerMockHelper.QUIT, ServerMockHelper.NO_PAYLOAD);
		serverThread.join(waitForServerShutdownMillis);
		System.out.println("Server successfully shutdown");
	}

	@Test
	public void getMessageTest() {
		try {
			client.getMessage(timeout);
			fail("Should have thrown OperationNotSupportedException");
		} catch (OperationNotSupportedException e) {
			// thrown exception was expected
		} catch (TimeoutException e) {
			fail("Reading CTCOM message string timed out, should not happen");
		}
	}
	
	@Test
	public void quitTest() {
		try {
			client.quit("this is it, close this shit");
			fail("Should have thrown OperationNotSupportedException");
		} catch (OperationNotSupportedException e) {
			// thrown exception was expected
		}
	}
	
	@Test
	public void sendConnectionRequestTest() throws IOException {
		try {
			
			String messageString = 
					"< \n"
					+ "type=\"connect\" \n"
					+ "protocol=\"2014.01\" \n"
					+ "testbenchRead=\"header, partInfo\" \n"
					+ "testbenchWrite=\"header,warnings\" \n"
					+ ">";
			
			// prepare mock server for receiving a ctcom connect message
			ServerMockHelper.receiveMessage(client.getServerSocket(), ServerMockHelper.CONNECT_MESSAGE);
			// send actual connect message
			ConnectMessage message = new ConnectMessage(messageString);
			client.sendConnectionRequest(message);
			
			// check if client is now in the correct state
			ClientState actualState = client.getState();
			ClientState expectedState = new SentConnectionRequestClientState();
			assertEquals(expectedState.getClass(), actualState.getClass());
			
			// check if received message equals sent message
			lastReceivedMessage = server.getReceivedMessage();
			assertEquals(message.getPayload(),lastReceivedMessage.getPayload());
			
		} catch (OperationNotSupportedException e) {
			fail("Operation should be implemented");
		} catch (ReadMessageException e) {
			fail("Should never be reached due to correct message string");
		} catch (InterruptedException e) {
			fail("Should never be interrupted");
		}
	}
	
	@Test
	public void acknowledgeMessageTimeoutTest() throws IOException {
		try {
			int serverDelay = 2000; // milliseconds
			int readMsgTimeout = 1; // seconds
			
			String messageString = 
					"< \n"
					+ "type=\"connect\" \n"
					+ "protocol=\"2014.01\" \n"
					+ "testbenchRead=\"header, partInfo\" \n"
					+ "testbenchWrite=\"header,warnings\" \n"
					+ ">";
			
			// delay mock server by 3 seconds, to simulate timeout
			ServerMockHelper.wait(client.getServerSocket(), serverDelay);
			
			// send CTCOM connect message to change to the next CTCOM state
			
			// prepare mock server for receiving a ctcom connect message
			ServerMockHelper.receiveMessage(client.getServerSocket(), ServerMockHelper.CONNECT_MESSAGE);
			
			// send actual connect message
			ConnectMessage message = new ConnectMessage(messageString);
			client.sendConnectionRequest(message);
			
			// check if client is now in the correct state
			ClientState actualState = client.getState();
			ClientState expectedState = new SentConnectionRequestClientState();
			assertEquals(expectedState.getClass(), actualState.getClass());
			
			// try to receive acknowledge message from CTCOM server
			try {
				client.getMessage(readMsgTimeout);
				fail("Should not be reached, due to timeout exception");
			} catch (TimeoutException e) {
				// thrown exception was expected
			}
			
			// check if received message equals sent message
			lastReceivedMessage = server.getReceivedMessage();
			assertEquals(message.getPayload(),lastReceivedMessage.getPayload());
			
		} catch (OperationNotSupportedException e) {
			fail("Operation should be implemented");
		} catch (ReadMessageException e) {
			fail("Should never be reached due to correct message string");
		} catch (InterruptedException e) {
			fail("Should never be interrupted");
		}
	}
	
	private static void initMockedServer() throws InterruptedException {
		server = new CtcomServerMock();
		serverThread = new Thread(server);
		serverThread.start();

		// wait for server initialization
		Thread.sleep(waitForServerInitMillis);
	}
	
	@Test
	public void sendMessageTest() {
		try {
			client.sendMessage(null);
			fail("Should have thrown OperationNotSupportedException");
		} catch (OperationNotSupportedException e) {
			// thrown exception was expected
		}
	}

}
