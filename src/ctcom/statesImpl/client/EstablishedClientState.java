package ctcom.statesImpl.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.naming.OperationNotSupportedException;

import ctcom.CtcomClient;
import ctcom.exceptions.ReadMessageException;
import ctcom.messageImpl.CtcomMessage;
import ctcom.messageImpl.QuitMessage;
import ctcom.messageImpl.ReadDataMessage;
import ctcom.messageTypes.MessageType;
import ctcom.states.ClientState;

public class EstablishedClientState implements ClientState {

	@Override
	public void sendConnectionRequest(CtcomClient client, CtcomMessage message) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot send connection request, already established.");
	}

	@Override
	public CtcomMessage getMessage(CtcomClient client) throws OperationNotSupportedException {
		Socket server = client.getServerSocket();
		CtcomMessage message = null;
		// read data message from client
		String messageString;
		MessageType messageType;
		try {
			messageString = client.getMessageString(server);
			messageType = client.getMessageType(messageString);
			// process readData message
			if ( messageType == MessageType.READ_DATA ) {
				message = new ReadDataMessage(messageString);
				// stay in established client state
			}
			// process quit message
			else if ( messageType == MessageType.QUIT ) {
				message = new QuitMessage(messageString);
				client.changeState(new ClosedClientState());
			}
		} catch (IOException e) {
			e.printStackTrace();
			// stay in established client state
			return null;
		} catch (ReadMessageException e) {
			e.printStackTrace();
			// stay in established client state
			return null;
		}
				
		// return read data or null if no correct message type was received 
		return message;		
	}

	@Override
	public void quit(CtcomClient client, String message) throws OperationNotSupportedException {
		Socket server = client.getServerSocket();
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));

			QuitMessage quitMessage = new QuitMessage();
			quitMessage.setMessage(message);
			
			writer.write(quitMessage.getPayload());
			writer.flush();
		} catch (IOException e) {
			// cannot open stream, or read data
			e.printStackTrace();
		}
		
		client.changeState(new ClosedClientState());
		
	}

	@Override
	public void sendMessage(CtcomClient client, CtcomMessage message) throws OperationNotSupportedException {
		try {
			Socket server = client.getServerSocket();
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
			writer.write(message.getPayload());
			writer.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		// stay in established client state
	}

}
