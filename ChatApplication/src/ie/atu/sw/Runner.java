package ie.atu.sw;

/**
 * The <b> Runner class</b>responsible for initiating the chat server and
 * multiple client connections. The main method creates a chat server on
 * specified ports and starts multiple clients to simulate multiple users
 * interacting in the chat application.
 */
public class Runner {

	/**
	 * the <b>Main method</b> to start the chat server and multiple client
	 * connections. Initializes the chat server on specified ports and starts
	 * multiple client instances.
	 *
	 * @param args Command-line arguments (not used in this context)
	 */
	public static void main(String[] args) {
		int[] ports = { 9000 }; // Define ports where the server will be listening

		// Start a chat server on multiple ports
		ChatServer server = new ChatServer();
		server.start(ports);

		// Start multiple clients to simulate multiple users
		ChatClient client1 = new ChatClient("localhost", 9000, "Client 1");
		ChatClient client2 = new ChatClient("localhost", 9000, "Client 2");

		// Start clients in separate threads for concurrent operation
		new Thread(() -> client1.start()).start();
		new Thread(() -> client2.start()).start();
	}
}
