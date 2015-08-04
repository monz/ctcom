package ctcom.states;

import javax.naming.OperationNotSupportedException;

import ctcom.CtcomClient;
import ctcom.messageImpl.CtcomMessage;

public interface ClientState {
	public void sendConnectionRequest(CtcomClient client, CtcomMessage message) throws OperationNotSupportedException;
	public CtcomMessage getMessage(CtcomClient client) throws OperationNotSupportedException;
	public void sendMessage(CtcomClient client, CtcomMessage message) throws OperationNotSupportedException;
	public void quit(CtcomClient client, String message) throws OperationNotSupportedException;
}
