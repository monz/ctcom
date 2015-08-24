package ctcom.statesImpl.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.OperationNotSupportedException;

import ctcom.CtcomClient;
import ctcom.exceptions.ReadMessageException;
import ctcom.messageImpl.CtcomMessage;
import ctcom.messageImpl.QuitMessage;
import ctcom.messageImpl.ReadDataMessage;
import ctcom.messageTypes.MessageType;
import ctcom.states.ClientState;
import ctcom.util.LogHelper;

public class EstablishedClientState implements ClientState {
	private static final Logger log = LogHelper.getLogger();

	@Override
	public void sendConnectionRequest(CtcomClient client, CtcomMessage message) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot send connection request, already established.");
	}

	@Override
	public CtcomMessage getMessage(CtcomClient client) throws OperationNotSupportedException {
		log.info("Receive CTCOM message");
		
		Socket server = client.getServerSocket();
		CtcomMessage message = null;
		// read data message from client
		String messageString;
		MessageType messageType;
		try {
			messageString = client.getMessageString(server);
			
			log.info("Received message string: \n" + messageString);
			
			messageType = client.getMessageType(messageString);
			// process readData message
			if ( messageType == MessageType.READ_DATA ) {
				log.info("Received CTCOM readData message");
				message = new ReadDataMessage(messageString);
				// stay in established client state
			}
			// process quit message
			else if ( messageType == MessageType.QUIT ) {
				log.info("Received CTCOM quit message");
				message = new QuitMessage(messageString);
				
				log.info("Change CTCOM client state to 'ClosedClient'");
				client.changeState(new ClosedClientState());
			}
		} catch (IOException e) {
			log.log(Level.WARNING, "Could not get message string", e);
			e.printStackTrace();
			// stay in established client state
			return null;
		} catch (ReadMessageException e) {
			log.log(Level.WARNING, "Received CTCOM message was invalid", e);
			e.printStackTrace();
			// stay in established client state
			return null;
		}
				
		// return read data or null if no correct message type was received 
		return message;		
	}

	@Override
	public void quit(CtcomClient client, String message) throws OperationNotSupportedException {
		log.info("Quit CTCOM client");
		
		Socket server = client.getServerSocket();
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));

			log.info("Send CTCOM quit message to CTCOM server");
			
			QuitMessage quitMessage = new QuitMessage();
			quitMessage.setMessage(message);
			
			writer.write(quitMessage.getPayload());
			writer.flush();
		} catch (IOException e) {
			log.log(Level.WARNING, "IOError on output stream", e);
			// cannot open stream, or write data
			e.printStackTrace();
		}
		
		log.info("Change CTCOM client state to 'ClosedClient'");
		client.changeState(new ClosedClientState());
		
	}

	@Override
	public void sendMessage(CtcomClient client, CtcomMessage message) throws OperationNotSupportedException {
		log.info("Send CTCOM message to CTCOM server");
		
		try {
			Socket server = client.getServerSocket();
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
			log.info("Sending payload: \n" + message.getPayload());
			writer.write(message.getPayload());
			writer.flush();
			
		} catch (IOException e) {
			log.log(Level.WARNING, "IOError on output stream", e);
			e.printStackTrace();
		}
		// stay in established client state
	}

	@Override
	public void close(CtcomClient client) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot close TCP connection, quit ctcom connection first");
	}

}
