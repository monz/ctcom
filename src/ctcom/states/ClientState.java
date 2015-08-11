package ctcom.states;

import javax.naming.OperationNotSupportedException;

import ctcom.CtcomClient;
import ctcom.messageImpl.CtcomMessage;

public interface ClientState {
	
	/**
	 * Send ctcom connection request to ctcom server
	 * @param client - ctcom client holding the server TCP connection
	 * @param message - Prepared/Filled {@link ConnectMessage}
	 * @throws OperationNotSupportedException
	 */
	public void sendConnectionRequest(CtcomClient client, CtcomMessage message) throws OperationNotSupportedException;
	
	/**
	 * Read next ctcom message from ctcom server
	 * @param client - ctcom client holding the server TCP connection
	 * @return - Received ctcom message
	 * @throws OperationNotSupportedException
	 */
	public CtcomMessage getMessage(CtcomClient client) throws OperationNotSupportedException;
	
	/**
	 * Send a ctcom message to the ctcom server
	 * @param client - ctcom client holding the server TCP connection
	 * @param message - ctcom message
	 * @throws OperationNotSupportedException
	 */
	public void sendMessage(CtcomClient client, CtcomMessage message) throws OperationNotSupportedException;
	
	/**
	 * Quit ctcom connection
	 * @param client - ctcom client holding the server TCP connection
	 * @param message - Explanation why the ctcom connection was closed
	 * @throws OperationNotSupportedException
	 */
	public void quit(CtcomClient client, String message) throws OperationNotSupportedException;
}
