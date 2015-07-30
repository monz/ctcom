package ctcom;

import java.net.ServerSocket;
import java.net.Socket;

import javax.naming.OperationNotSupportedException;

import ctcom.messageImpl.CtcomMessage;
import ctcom.states.ServerState;
import ctcom.statesImpl.server.ClosedServerState;

public class CtcomServer {
	private ServerState state;
	private ServerSocket serverSocket;
	private Socket client;
	
	public CtcomServer(){
		// starting in closed state
		state = new ClosedServerState();
	}
	
	public void setClientSocket(Socket client) {
		this.client = client;
	}
	
	public Socket getClientSocket() {
		return client;
	}
	
	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	public ServerState getState() {
		return state;
	}

	public CtcomMessage receiveConnectRequest(Socket client) throws OperationNotSupportedException {
		return state.receiveConnectRequest(this, client);
	}
	
	public void sendConnectAcknowledge(CtcomMessage message) throws OperationNotSupportedException {
		state.sendConnectionAck(this, message);
	}

	public Socket open(int port) throws OperationNotSupportedException {
		return state.open(this, port);
	}

	public CtcomMessage readData() throws OperationNotSupportedException {
		return state.readData(this);
	}

	public void quit(String message) throws OperationNotSupportedException {
		state.quit(this, message);
	}
	
	public void changeState(ServerState state) {
		this.state = state;
	}
}
