package ctcom.statesImpl.client;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.OperationNotSupportedException;

import ctcom.CtcomClient;
import ctcom.exceptions.ReadMessageException;
import ctcom.messageImpl.ConnectMessage;
import ctcom.messageImpl.CtcomMessage;
import ctcom.states.ClientState;
import ctcom.util.LogHelper;

public class SentConnectionRequestClientState implements ClientState {
	private static final Logger log = LogHelper.getLogger();

	@Override
	public void sendConnectionRequest(CtcomClient client, CtcomMessage message) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot send connection request, already sent.");
	}

	@Override
	public CtcomMessage getMessage(CtcomClient client, int timeout) throws OperationNotSupportedException, TimeoutException {
		log.info("Receive CTCOM message");

		Socket server = client.getServerSocket();
		String messageString;
		ConnectMessage message;
		// read connect message from client
		try {
			messageString = client.getMessageString(server, timeout);
			
			log.info("Received message string: \n" + messageString);

			message = new ConnectMessage(messageString);
		} catch (IOException e) {
			log.log(Level.WARNING, "Could not get message string", e);
			e.printStackTrace();
			// stay in listen state
			return null;
		} catch (ReadMessageException e) {
			log.log(Level.WARNING, "Received CTCOM message was invalid", e);
			e.printStackTrace();
			// stay in listen state
			return null;
		}
		
		// connection acknowledge received
		log.info("Change CTCOM client state to 'EstablishedClient'");
		client.changeState(new EstablishedClientState());
		
		return message;
	}

	@Override
	public void quit(CtcomClient client, String message) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot quit ctcom connection, not established.");
	}

	@Override
	public void sendMessage(CtcomClient client, CtcomMessage message) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot send ctcom data message, ctcom connection is not fully established yet. Waiting for connection acknowledge");
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
		log.info("Change CTCOM client state to 'ClosedClient'");
		client.changeState(new ClosedClientState());
	}

}
