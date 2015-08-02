package ctcom;

import java.io.IOException;
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
	
	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	public ServerState getState() {
		return state;
	}

	public CtcomMessage getConnectRequest() throws OperationNotSupportedException {
		return state.getConnectRequest(this);
	}
	
	public void sendConnectAcknowledge(CtcomMessage message) throws OperationNotSupportedException {
		state.sendConnectionAck(this, message);
	}

	public Socket accept() throws OperationNotSupportedException {
		return state.accept(this);
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
