package ctcomTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

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
import ctcom.statesImpl.client.EstablishedClientState;
import ctcom.statesImpl.client.SentConnectionRequestClientState;
import ctcomTest.stub.CtcomServerMock;
import ctcomTest.stub.ServerMockHelper;

public class CtcomClientSentConnectionRequestTest {

	private static CtcomClient client;
	private static final int port = 4745;
	private static final long waitForServerShutdownMillis = 1000;
	private static final long waitForServerInitMillis = 50;
	
	private static Thread serverThread;
	private static CtcomServerMock server;
	private static CtcomMessage lastReceivedMessage;
	
	@BeforeClass
	public static void initialize() throws IOException, InterruptedException {
		initMockedServer();
		client = new CtcomClient("localhost", port);
		// change to state which should get tested
		client.changeState(new SentConnectionRequestClientState());
	}
	
	@Before
	public void prepareForNewTest() {
		client.changeState(new SentConnectionRequestClientState());
	}
	
	@AfterClass
	public static void tearDown() throws IOException, InterruptedException {
		System.out.println("Waiting for server mock to shutdown.");
		ServerMockHelper.SendMessage(client.getServerSocket(), ServerMockHelper.QUIT, ServerMockHelper.NO_PAYLOAD);
		serverThread.join(waitForServerShutdownMillis);
		System.out.println("Server successfully shutdown");
	}

	@Test
	public void getMessageTest() throws IOException {
		try {
			
			String acknowledgeMessageString = 
					"< \n"
					+ "type=\"connect\" \n"
					+ "protocol=\"2014.01\" \n"
					+ "testbenchRead=\"header, partInfo\" \n"
					+ "testbenchWrite=\"header,warnings\" \n"
					+ ">";
			
			// prepare mock server to send a ctcom connect acknowledge message
			ServerMockHelper.SendMessage(client.getServerSocket(), ServerMockHelper.CONNECT_MESSAGE, acknowledgeMessageString);
			// receive connect acknowledge message
			lastReceivedMessage = client.getMessage();
			
			// check if client is now in the correct state
			ClientState actualState = client.getState();
			ClientState expectedState = new EstablishedClientState();
			assertEquals(expectedState.getClass(), actualState.getClass());
			
			// check if received message equals sent message
			CtcomMessage message = new ConnectMessage(acknowledgeMessageString);
			assertEquals(message.getPayload(),lastReceivedMessage.getPayload());
			
		} catch (OperationNotSupportedException e) {
			fail("Operation should be implemented");
		} catch (ReadMessageException e) {
			fail("Should never be reached due to correct message string");
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
	public void sendConnectionRequestTest() {
		try {
			client.sendConnectionRequest(null);
			fail("Should have thrown OperationNotSupportedException");
		} catch (OperationNotSupportedException e) {
			// thrown exception was expected
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
