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
import ctcom.messageImpl.CtcomMessage;
import ctcom.messageImpl.QuitMessage;
import ctcom.messageImpl.ReadDataMessage;
import ctcom.states.ClientState;
import ctcom.statesImpl.client.ClosedClientState;
import ctcom.statesImpl.client.EstablishedClientState;
import ctcom.statesImpl.client.SentConnectionRequestClientState;
import ctcomTest.stub.CtcomServerMock;
import ctcomTest.stub.ServerMockHelper;

public class CtcomClientEstablishedTest {

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
		// change to state which should get tested
		client.changeState(new SentConnectionRequestClientState());
	}
	
	@Before
	public void prepareForNewTest() {
		client.changeState(new EstablishedClientState());
	}
	
	@AfterClass
	public static void tearDown() throws IOException, InterruptedException {
		System.out.println("Waiting for server mock to shutdown.");
		ServerMockHelper.sendMessage(client.getServerSocket(), ServerMockHelper.QUIT, ServerMockHelper.NO_PAYLOAD);
		serverThread.join(waitForServerShutdownMillis);
		System.out.println("Server successfully shutdown");
	}

	@Test
	public void getMessageTest() throws IOException {
		try {
			
			String readDataMessageString = 
					"< \n"
					+ "type=\"readData\" \n"
					+ "transfer=\"file\" \n"
					+ "location=\"\\\\someserver\\somedirectory\\ct.ctmat\" \n"
					+ ">";
			
			// prepare mock server to send a ctcom read data message
			ServerMockHelper.sendMessage(client.getServerSocket(), ServerMockHelper.READ_DATA_MESSAGE, readDataMessageString);
			// receive read data message
			lastReceivedMessage = client.getMessage(timeout);
			
			// check if client is now in the correct state
			ClientState actualState = client.getState();
			ClientState expectedState = new EstablishedClientState();
			assertEquals(expectedState.getClass(), actualState.getClass());
			
			// check if received message equals sent message
			CtcomMessage message = new ReadDataMessage(readDataMessageString);
			assertEquals(message.getPayload(),lastReceivedMessage.getPayload());
			
		} catch (OperationNotSupportedException e) {
			fail("Operation should be implemented");
		} catch (ReadMessageException e) {
			fail("Should never be reached due to correct message string");
		} catch (TimeoutException e) {
			fail("Reading CTCOM message string timed out, should not happen");
		}
	}
	
	@Test
	public void quitTest() throws IOException {
		try {
			String quitMessageString =
					"< \n"
					+ "type= \"quit\" \n"
					+ "message= \"this is it, close this shit\" \n"
					+ "> \n";
			
			// prepare mock server for receiving a ctcom quit message
			ServerMockHelper.receiveMessage(client.getServerSocket(), ServerMockHelper.QUIT_MESSAGE);
			// send quit message
			QuitMessage message = new QuitMessage(quitMessageString);
			client.quit(message.getMessage());

			
			// check if client is now in the correct state
			ClientState actualState = client.getState();
			ClientState expectedState = new ClosedClientState();
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
	public void sendMessageTest() throws IOException {
		try {
			
			String readDataMessageString = 
					"< \n"
					+ "type=\"readData\" \n"
					+ "transfer=\"file\" \n"
					+ "location=\"\\\\someserver\\somedirectory\\ct.ctmat\" \n"
					+ ">";
			
			// prepare mock server for receiving a ctcom read data message
			ServerMockHelper.receiveMessage(client.getServerSocket(), ServerMockHelper.READ_DATA_MESSAGE);
			// send read data message
			CtcomMessage message = new ReadDataMessage(readDataMessageString);
			client.sendMessage(message);
			
			// check if client is now in the correct state
			ClientState actualState = client.getState();
			ClientState expectedState = new EstablishedClientState();
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

}
