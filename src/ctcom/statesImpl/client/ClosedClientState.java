package ctcom.statesImpl.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.naming.OperationNotSupportedException;

import ctcom.CtcomClient;
import ctcom.messageImpl.CtcomMessage;
import ctcom.states.ClientState;

public class ClosedClientState implements ClientState {

	@Override
	public void sendConnectionRequest(CtcomClient client, CtcomMessage message) throws OperationNotSupportedException {
		try {
			Socket server = client.getServerSocket();
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
			writer.write(message.getPayload());
			writer.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.changeState(new SentConnectionRequestClientState());
	}

	@Override
	public CtcomMessage getMessage(CtcomClient client) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot receive ctcom message, connection is not established.");
	}

	@Override
	public void quit(CtcomClient client, String message) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot close ctcom connection, not established.");
	}

	@Override
	public void sendMessage(CtcomClient client, CtcomMessage message) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot send ctcom message, ctcom connection is not established");
	}

}
