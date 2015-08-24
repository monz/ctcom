package ctcom.statesImpl.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.OperationNotSupportedException;

import ctcom.CtcomClient;
import ctcom.messageImpl.CtcomMessage;
import ctcom.states.ClientState;
import ctcom.util.LogHelper;

public class ClosedClientState implements ClientState {
	private static final Logger log = LogHelper.getLogger();

	@Override
	public void sendConnectionRequest(CtcomClient client, CtcomMessage message) throws OperationNotSupportedException {
		try {
			log.info("Sending connection request");
			
			Socket server = client.getServerSocket();
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
			writer.write(message.getPayload());
			writer.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("Change CTCOM client state to 'SentConnectionRequest'");
		
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

	@Override
	public void close(CtcomClient client) throws OperationNotSupportedException {
		log.info("Try to close TCP connection to CTCOM server");
		
		Socket server = client.getServerSocket();
		try {
			server.close();
		} catch (IOException e) {
			log.log(Level.WARNING, "Could not close TCP connection to CTCOM server", e);
			e.printStackTrace();
		}
		// stay in closed client state
	}

}
