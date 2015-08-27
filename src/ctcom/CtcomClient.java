package ctcom;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeoutException;

import javax.naming.OperationNotSupportedException;

import ctcom.messageImpl.ConnectMessage;
import ctcom.messageImpl.CtcomMessage;
import ctcom.states.ClientState;
import ctcom.statesImpl.client.ClosedClientState;

public class CtcomClient extends CtcomProtocol {
	private Socket server;
	private ClientState state;
	
	/**
	 * Create ctcom client. The TCP connection to the ctcom
	 * server is established immediately.
	 * @param host - ctcom server's IP address, name
	 * @param port - ctcom server's listening port number
	 */
	public CtcomClient(String host, int port) throws IOException {
		// open server connection
		server = new Socket(host, port);
		// starting in closed client state
		state = new ClosedClientState();
	}
	
	/**
	 * Send ctcom connection request to ctcom server
	 * @param message - Prepared/Filled {@link ConnectMessage}
	 * @throws OperationNotSupportedException
	 */
	public void sendConnectionRequest(CtcomMessage message) throws OperationNotSupportedException {
		state.sendConnectionRequest(this, message);
	}
	
	/**
	 * Return ctcom server's TCP connection this client is connected to
	 * @return - ctcom server's TCP socket
	 */
	public Socket getServerSocket() {
		return server;
	}
	
	/**
	 * Read next ctcom message from ctcom server
	 * @param timeout - time to wait until reading message string fails
	 * @return - Received ctcom message
	 * @throws OperationNotSupportedException
	 * @throws TimeoutException 
	 */
	public CtcomMessage getMessage(int timeout) throws OperationNotSupportedException, TimeoutException {
		return state.getMessage(this, timeout);
	}
	
	/**
	 * Send a ctcom message to the ctcom server
	 * @param message - ctcom message
	 * @throws OperationNotSupportedException
	 */
	public void sendMessage(CtcomMessage message) throws OperationNotSupportedException {
		state.sendMessage(this, message);
	}
	
	/**
	 * Quit ctcom connection
	 * @param message - Explanation why the ctcom connection was closed
	 * @throws OperationNotSupportedException
	 */
	public void quit(String message) throws OperationNotSupportedException {
		state.quit(this, message);
	}

	/**
	 * Return current state the ctcom client is in.
	 * @return
	 */
	public ClientState getState() {
		return state;
	}
	
	/**
	 * The ctcom client can be in different states. Not all methods are available in every state.
	 * Some methods change the ctcom client's state to alter its behavior. Change ctcom client's state.
	 * @param state - new state the client should be in
	 */
	public void changeState(ClientState state) {
		this.state = state;
	}	
	
	/**
	 * Close client TCP connection, close connection to ctcom server.
	 * After closing the TCP connection, the ctcom client is unusable.
	 * Therefore a new ctcom client must be created for new connections.
	 * @throws OperationNotSupportedException
	 */
	public void close() throws OperationNotSupportedException {
		this.state.close(this);
	}
}
