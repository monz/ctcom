package ctcom.states;

import java.net.Socket;

import javax.naming.OperationNotSupportedException;

import ctcom.CtcomServer;
import ctcom.messageImpl.CtcomMessage;

public interface ServerState {
	
	/**
	 * Allow clients to open new TCP connections to the server.
	 * @param server - ctcom server holding the client TCP connection
	 * @return - client TCP socket connection
	 * @throws OperationNotSupportedException
	 */
	public Socket accept(CtcomServer server) throws OperationNotSupportedException;
	
	/**
	 * Close server TCP connection, unbind server port from local machine.
	 * This closes all client TCP connections
	 * @param server - ctcom server holding the client TCP connection
	 * @throws OperationNotSupportedException
	 */
	public void close(CtcomServer server) throws OperationNotSupportedException;
	
	/**
	 * Send ctcom connenction request acknowledge message. If a connection request
	 * was sent with a different protocol version number, this number gets replaced
	 * automatically in the acknowledgement message.
	 * @param server - ctcom server holding the client TCP connection
	 * @param message - ctcom connect message received from ctcom client
	 * @throws OperationNotSupportedException
	 */
	public void sendConnectionAck(CtcomServer server, CtcomMessage message) throws OperationNotSupportedException;
	
	/**
	 * Read next ctcom message sent from ctcom client
	 * @param server - ctcom server holding the client TCP connection
	 * @return - Received ctcom message
	 * @throws OperationNotSupportedException
	 */
	public CtcomMessage getMessage(CtcomServer server) throws OperationNotSupportedException;
	
	/**
	 * Send a ctcom message to the ctcom client
	 * @param server - ctcom server holding the client TCP connection
	 * @param message - ctcom message
	 * @throws OperationNotSupportedException
	 */
	public void sendMessage(CtcomServer server, CtcomMessage message) throws OperationNotSupportedException;
	
	/**
	 * Quit ctcom connection. The client's TCP connection get closed as well
	 * @param server - ctcom server holding the client TCP connection
	 * @param message - message why the ctcom connection was closed
	 * @throws OperationNotSupportedException
	 */
	public void quit(CtcomServer server, String message) throws OperationNotSupportedException;
}
