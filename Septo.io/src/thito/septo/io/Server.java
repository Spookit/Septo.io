package thito.septo.io;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	private UncaughtExceptionHandler handler = Thread.currentThread().getUncaughtExceptionHandler();
	private Set<ClientListener> listeners = new HashSet<>();
	private ServerSocket server;
	private ExecutorService clientHandler;
	private int port;

	public Server(int port) {
		this.port = port;
		clientHandler = Executors.newCachedThreadPool(run -> {
			Thread thread = new Thread(run);
			thread.setUncaughtExceptionHandler(handler);
			return thread;
		});

	}

	public Server(ServerSocket server) {
		clientHandler = Executors.newCachedThreadPool(run -> {
			Thread thread = new Thread(run);
			thread.setUncaughtExceptionHandler(handler);
			return thread;
		});
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
			clientHandler.submit(() -> {
				try {
					InputStream input = client.getInputStream();
					Client header = new Client(input, client.getOutputStream());
					listeners.forEach(l -> {
						try {
							l.accept(header);
						} catch (IOException e) {
							if (handler != null) {
								handler.uncaughtException(Thread.currentThread(), e);
							}
						}
					});
					input.close();
					header.getOutputStream().close();
					client.close();
				} catch (IOException e) {
					sneakyThrow(e);
				}
			});
		}
	}

	@SuppressWarnings("unchecked")
	private static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
		throw (E) e;
	}
	
}
