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
			state = new ClosedClientState();
			server = new Socket(host, port);
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
	
	public void quit(String message) throws OperationNotSupportedException {
		state.quit(this, message);
	}
	
	public void changeState(ClientState state) {
		this.state = state;
	}	
}
