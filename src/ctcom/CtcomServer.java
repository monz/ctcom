package ctcom;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.naming.OperationNotSupportedException;

import ctcom.messageImpl.CtcomMessage;
import ctcom.states.ServerState;
import ctcom.statesImpl.server.ClosedServerState;

public class CtcomServer extends CtcomProtocol {
	private ServerState state;
	private ServerSocket serverSocket;
	private Socket client;
	
	/**
	 * Create ctcom server. The given port number is bind to
	 * the local machine immediately.
	 * @param port - port number the ctcom server is listening to
	 * @throws IOException
	 */
	public CtcomServer(int port) throws IOException{
		// open server socket
		serverSocket = new ServerSocket(port);
		// starting in closed state
		state = new ClosedServerState();
	}
	
	/**
	 * Set client's TCP connection attribute with given connection
	 * @param client - TCP socket connection
	 */
	public void setClientSocket(Socket client) {
		this.client = client;
	}
	
	/**
	 * Return current client TCP connection
	 * @return
	 */
	public Socket getClientSocket() {
		return client;
	}
	
	/**
	 * Return server socket
	 * @return
	 */
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	/**
	 * Return current state the ctcom server is in.
	 * @return
	 */
	public ServerState getState() {
		return state;
	}
	
	/**
	 * Send ctcom connenction request acknowledge message. If a connection request
	 * was sent with a different protocol version number, this number gets replaced
	 * automatically in the acknowledgement message.
	 * @param message - ctcom connect message received from ctcom client
	 * @throws OperationNotSupportedException
	 */
	public void sendConnectAcknowledge(CtcomMessage message) throws OperationNotSupportedException {
		state.sendConnectionAck(this, message);
	}

	/**
	 * Allow clients to open new TCP connections to the server.
	 * @return client TCP socket connection
	 * @throws OperationNotSupportedException
	 */
	public Socket accept() throws OperationNotSupportedException {
		return state.accept(this);
	}

	/**
	 * Read next ctcom message sent from ctcom client
	 * @param timeout - time to wait until reading message string fails
	 * @return Received ctcom message
	 * @throws OperationNotSupportedException
	 */
	public CtcomMessage getMessage(int timeout) throws OperationNotSupportedException {
		return state.getMessage(this, timeout);
	}
	
	/**
	 * Send a ctcom message to the ctcom client
	 * @param message - ctcom message
	 * @throws OperationNotSupportedException
	 */
	public void sendMessage(CtcomMessage message) throws OperationNotSupportedException {
		state.sendMessage(this, message);
	}

	/**
	 * Quit ctcom connection. The client's TCP connection get closed as well
	 * @param message - message why the ctcom connection was closed
	 * @throws OperationNotSupportedException
	 */
	public void quit(String message) throws OperationNotSupportedException {
		state.quit(this, message);
	}
	
	/**
	 * Close ctcom server's TCP socket. This closes all client's TCP connections as well
	 * @throws OperationNotSupportedException
	 */
	public void close() throws OperationNotSupportedException {
		state.close(this);
	}
	
	/**
	 * The ctcom server can be in different states. Not all methods are available in every state.
	 * Some methods change the ctcom server's state to alter its behavior. Change ctcom server's state.
	 * @param state - new state the ctcom server should be in
	 */
	public void changeState(ServerState state) {
		this.state = state;
	}
}
