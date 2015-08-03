package ctcom.statesImpl.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.naming.OperationNotSupportedException;

import ctcom.CtcomServer;
import ctcom.exceptions.ReadMessageException;
import ctcom.messageImpl.CtcomMessage;
import ctcom.messageImpl.QuitMessage;
import ctcom.messageImpl.ReadDataMessage;
import ctcom.messageTypes.MessageType;
import ctcom.states.ServerState;

public class EstablishedServerState implements ServerState {

	@Override
	public Socket accept(CtcomServer server) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot open connection, already open and established.");		
	}

	@Override
	public void close(CtcomServer server) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot close tcp connection, must quit ctcom first.");
	}

	@Override
	public void sendConnectionAck(CtcomServer server, CtcomMessage message) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot send connection acknowledge, already established.");
	}

	@Override
	public CtcomMessage getMessage(CtcomServer server) throws OperationNotSupportedException {
		Socket client = server.getClientSocket();
		CtcomMessage message = null;
		// read data message from client
		String messageString;
		MessageType messageType;
		try {
			messageString = server.getMessageString(client);
			messageType = server.getMessageType(messageString);
			// process readData message
			if ( messageType == MessageType.READ_DATA ) {
				message = new ReadDataMessage(messageString);
				// stay in established server state
			}
			// process quit message
			else if ( messageType == MessageType.QUIT ) {
				message = new QuitMessage(messageString);
				server.changeState(new QuitServerState());
			}
		} catch (IOException e) {
			e.printStackTrace();
			// stay in established server state
			return null;
		} catch (ReadMessageException e) {
			e.printStackTrace();
			// stay in established server state
			return null;
		}
				
		// return read data or null if no correct message type was received 
		return message;		
	}

	@Override
	public void quit(CtcomServer server, String message) throws OperationNotSupportedException {
		Socket client = server.getClientSocket();
		try {
			QuitMessage quitMessage = new QuitMessage(message);
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			writer.write(quitMessage.getPayload());
			writer.flush();
		} catch (IOException e) {
			// cannot open stream, or read data
			e.printStackTrace();
		} catch (ReadMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		server.changeState(new ListenServerState());
	}
}
