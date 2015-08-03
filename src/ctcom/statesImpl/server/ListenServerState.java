package ctcom.statesImpl.server;

import java.io.IOException;
import java.net.Socket;

import javax.naming.OperationNotSupportedException;

import ctcom.CtcomServer;
import ctcom.exceptions.ReadMessageException;
import ctcom.messageImpl.ConnectMessage;
import ctcom.messageImpl.CtcomMessage;
import ctcom.states.ServerState;

public class ListenServerState implements ServerState {

	@Override
	public Socket accept(CtcomServer server) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Could not open connection, already open.");
	}

	@Override
	public void close(CtcomServer server) throws OperationNotSupportedException {
		try {
			server.getServerSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.changeState(new ClosedServerState());
	}

	@Override
	public void sendConnectionAck(CtcomServer server, CtcomMessage message) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot send ctcom connection acknowledge, no connection request received yet");
	}

	@Override
	public CtcomMessage getMessage(CtcomServer server) throws OperationNotSupportedException {
		Socket client = server.getClientSocket();
		String messageString;
		ConnectMessage message;
		// read connect message from client
		try {
			messageString = server.getMessageString(client);
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
		
		// connection request received
		server.changeState(new RequestReceivedServerState());
		
		return message;
	}

	@Override
	public void quit(CtcomServer server, String message) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Faild to quit connection, connection is not established.");
	}
}
