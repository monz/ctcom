package ctcom.states;

import java.net.Socket;

import javax.naming.OperationNotSupportedException;

import ctcom.CtcomServer;
import ctcom.messageImpl.CtcomMessage;

public interface ServerState {
	public Socket open(CtcomServer server, int port) throws OperationNotSupportedException;
	public void close(CtcomServer server) throws OperationNotSupportedException;
	public void sendConnectionAck(CtcomServer server, CtcomMessage message) throws OperationNotSupportedException;
	public CtcomMessage receiveConnectRequest(CtcomServer server, Socket client) throws OperationNotSupportedException;
	public CtcomMessage readData(CtcomServer server) throws OperationNotSupportedException;
	public void quit(CtcomServer server, String message) throws OperationNotSupportedException;
}
