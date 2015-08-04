package ctcom.statesImpl.client;

import java.io.IOException;
import java.net.Socket;

import javax.naming.OperationNotSupportedException;

import ctcom.CtcomClient;
import ctcom.exceptions.ReadMessageException;
import ctcom.messageImpl.ConnectMessage;
import ctcom.messageImpl.CtcomMessage;
import ctcom.states.ClientState;

public class SentConnectionRequestClientState implements ClientState {

	@Override
	public void sendConnectionRequest(CtcomClient client, CtcomMessage message) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot send connection request, already sent.");
	}

	@Override
	public CtcomMessage getMessage(CtcomClient client) throws OperationNotSupportedException {
		Socket server = client.getServerSocket();
		String messageString;
		ConnectMessage message;
		// read connect message from client
		try {
			messageString = client.getMessageString(server);
			message = new ConnectMessage(messageString);
		} catch (IOException e) {
			e.printStackTrace();
			// stay in listen state
			return null;
		} catch (ReadMessageException e) {
			e.printStackTrace();
			// stay in listen state
			return null;
		}
		
		// connection acknowledge received
		client.changeState(new EstablishedClientState());
		
		return message;
	}

	@Override
	public void quit(CtcomClient client, String message) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot quit ctcom connection, not established.");
	}

}
