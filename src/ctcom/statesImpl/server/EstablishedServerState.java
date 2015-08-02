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
	public CtcomMessage readData(CtcomServer server) throws OperationNotSupportedException {
		Socket client = server.getClientSocket();
		CtcomMessage message = new ReadDataMessage();
		// read data message from client
		try {
			message.readMessage(client);
		} catch (IOException e) {
			e.printStackTrace();
			// stay in established server state
			return null;
		} catch (ReadMessageException e) {
			e.printStackTrace();
			// stay in established server state
			return null;
		}
		
		// stay in established server state
		
		// return read data
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
		}
		
		server.changeState(new ListenServerState());
	}

	@Override
	public CtcomMessage getConnectRequest(CtcomServer server) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot serve client, connection already established.");
	}

}
