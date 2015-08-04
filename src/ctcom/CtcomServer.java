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
	
	public CtcomServer(int port) throws IOException{
		// open server socket
		serverSocket = new ServerSocket(port);
		// starting in closed state
		state = new ClosedServerState();
	}
	
	public void setClientSocket(Socket client) {
		this.client = client;
	}
	
	public Socket getClientSocket() {
		return client;
	}
	
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	public ServerState getState() {
		return state;
	}
	
	public void sendConnectAcknowledge(CtcomMessage message) throws OperationNotSupportedException {
		state.sendConnectionAck(this, message);
	}

	public Socket accept() throws OperationNotSupportedException {
		return state.accept(this);
	}

	public CtcomMessage getMessage() throws OperationNotSupportedException {
		return state.getMessage(this);
	}
	
	public void sendMessage(CtcomMessage message) throws OperationNotSupportedException {
		state.sendMessage(this, message);
	}

	public void quit(String message) throws OperationNotSupportedException {
		state.quit(this, message);
	}
	
	public void close() throws OperationNotSupportedException {
		state.close(this);
	}
	
	public void changeState(ServerState state) {
		this.state = state;
	}
}
