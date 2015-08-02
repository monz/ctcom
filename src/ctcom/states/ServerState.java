package ctcom.states;

import java.net.Socket;

import javax.naming.OperationNotSupportedException;

import ctcom.CtcomServer;
import ctcom.messageImpl.CtcomMessage;

public interface ServerState {
	public Socket accept(CtcomServer server) throws OperationNotSupportedException;
	public void close(CtcomServer server) throws OperationNotSupportedException;
	public void sendConnectionAck(CtcomServer server, CtcomMessage message) throws OperationNotSupportedException;
	public CtcomMessage getConnectRequest(CtcomServer server, Socket client) throws OperationNotSupportedException;
	public CtcomMessage readData(CtcomServer server) throws OperationNotSupportedException;
	public void quit(CtcomServer server, String message) throws OperationNotSupportedException;
}
