package ctcom.statesImpl.server;

import java.io.IOException;
import java.net.Socket;

import javax.naming.OperationNotSupportedException;

import ctcom.CtcomServer;
import ctcom.messageImpl.CtcomMessage;
import ctcom.states.ServerState;

public class ClosedServerState implements ServerState {

	@Override
	public Socket accept(CtcomServer server) throws OperationNotSupportedException {
		server.changeState(new ListenServerState());
		
		Socket client = null;
		try {
			client = server.getServerSocket().accept();
		} catch (IOException e) {
			e.printStackTrace();
			server.changeState(new ClosedServerState());
		}
		return client;
	}

	@Override
	public CtcomMessage readData(CtcomServer server) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot read data, connection is closed.");
	}

	@Override
	public void quit(CtcomServer server, String message) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Faild to quit connection, connection is closed.");
	}

	@Override
	public void close(CtcomServer server) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Faild to close connection, already closed.");		
	}

	@Override
	public void sendConnectionAck(CtcomServer server, CtcomMessage message) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Faild to send connection acknowledge, connection is closed.");		
	}

	@Override
	public CtcomMessage getConnectRequest(CtcomServer server, Socket client) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Failed to serve client, connection is closed.");
	}
}
