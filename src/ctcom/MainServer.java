package ctcom;

import java.io.IOException;

public class MainServer {
	public static void main(String[] args) {
		System.out.println("Starting server");
		
		CtcomServer s = new CtcomServer(4242);
		
		try {
			s.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
