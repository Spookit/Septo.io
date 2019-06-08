package thito.septo.io;

import java.io.IOException;

public interface ClientListener {

	public void accept(Client client) throws IOException;

}
