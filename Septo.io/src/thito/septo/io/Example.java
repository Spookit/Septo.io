package thito.septo.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;

public class Example implements ClientListener,UncaughtExceptionHandler {

	public static void main(String[]args) {
		Server server = new Server(8080);
		Example example = new Example();
		server.addListener(example);
		/* Optional */ server.setUncaughtExceptionHandler(example);
		server.startAsynchronously();
	}

	@Override
	public void accept(Client client) throws IOException {
		client.send(new Response(ResponseType.ACCEPTED));
		try (FileInputStream fis = new FileInputStream("C:/example.txt")) {
			client.send(fis);
		}
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.out.println("An error occured on Thread: "+t.getName());
		e.printStackTrace();
	}
}
