package thito.septo.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {

	private UncaughtExceptionHandler handler = Thread.currentThread().getUncaughtExceptionHandler();
	private Set<ClientListener> listeners = new HashSet<>();
	private ServerSocket server;
	private int port;

	public Server(int port) {
		this.port = port;
	}

	public Server(ServerSocket server) {
		this.server = server;
	}

	public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh) {
		handler = eh;
	}

	public UncaughtExceptionHandler getUncaughtExceptionHandler() {
		return handler;
	}

	public int getPort() {
		return port;
	}

	public boolean addListener(ClientListener l) {
		return listeners.add(l);
	}

	public boolean removeListener(ClientListener l) {
		return listeners.remove(l);
	}

	public Thread createAsynchronousThread() {
		Thread thread = new Thread("Server#" + hashCode()) {
			public void run() {
				try {
					startSynchronously();
				} catch (IOException e) {
					sneakyThrow(e);
				}
			}
		};
		thread.setUncaughtExceptionHandler(handler);
		return thread;
	}

	public void startAsynchronously() {
		createAsynchronousThread().start();
	}

	public void startSynchronously() throws IOException {
		server = new ServerSocket(port);
		port = server.getLocalPort();
		while (!server.isClosed()) {
			Socket client = server.accept();
			Thread th = new Thread("Client:"+client.getInetAddress()) {
				public void run() {
					try {
						client.setKeepAlive(true);
						InputStream input = client.getInputStream();
						OutputStream output = client.getOutputStream();
						Client header = new Client(input, output);
						listeners.forEach(l -> {
							try {
								l.accept(header);
							} catch (Throwable e) {
								if (handler != null) {
									handler.uncaughtException(Thread.currentThread(), e);
								}
							}
						});
						client.close();
					} catch (Throwable e) {
						if (handler != null) {
							handler.uncaughtException(Thread.currentThread(), e);
						}
					}
				}
			};
			th.setUncaughtExceptionHandler(handler);
			th.setDaemon(true);
			th.start();
		}
	}

	@SuppressWarnings("unchecked")
	private static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
		throw (E) e;
	}
	
}
