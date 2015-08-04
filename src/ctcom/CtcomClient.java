package ctcom;

import java.io.IOException;
import java.net.Socket;

import javax.naming.OperationNotSupportedException;

import ctcom.messageImpl.CtcomMessage;
import ctcom.states.ClientState;
import ctcom.statesImpl.client.ClosedClientState;

public class CtcomClient extends CtcomProtocol {
	private Socket server;
	private ClientState state;
	
	public CtcomClient(String host, int port) {
		try {
			// open server connection
			server = new Socket(host, port);
			// starting in closed client state
			state = new ClosedClientState();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendConnectionRequest(CtcomMessage message) throws OperationNotSupportedException {
		state.sendConnectionRequest(this, message);
	}
	
	public Socket getServerSocket() {
		return server;
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
	
	public void changeState(ClientState state) {
		this.state = state;
	}	
}
