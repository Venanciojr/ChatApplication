package ie.atu.sw;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The <b>class ChatServer</b> Represents a server that handles multiple client
 * connections for a chat application.
 * 
 * @author Venancio Moraes
 * @version 1.0
 * @since 03/01/24
 * 
 * 
 */

public class ChatServer {
	private final List<ClientHandler> clients = new ArrayList<>(); // Represents a list of connected clients.
	private final AtomicInteger clientIdGenerator = new AtomicInteger(1); // Represents a generator for assigning unique
																			// client IDs.
	private ExecutorService executorService; // Represents an executor service for managing client handling threads.

	/**
	 * The <b>start</> Starts the server on specified ports and waits for client
	 * connections.
	 *
	 * @param ports The port numbers on which the server will listen for incoming
	 *              connections.
	 */
	public void start(int... ports) {
		executorService = Executors.newCachedThreadPool(); // // Creates a thread pool for handling client connections.

		for (int port : ports) {
			try {
				ServerSocket serverSocket = new ServerSocket(port); //// Creates a server socket for the given port.
				System.out.println("Server started on port " + port + ". Waiting for clients..."); // Prints a message
																									// indicating that
																									// the server has
																									// started on the
																									// specified port.
				acceptClients(serverSocket); // // Accepts incoming client connections for the created server socket.
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * The <b>acceptClients</b> Accepts incoming client connections and creates a
	 * handler for each connected client.
	 *
	 * @param serverSocket The ServerSocket instance used for accepting client
	 *                     connections.
	 */

	private void acceptClients(ServerSocket serverSocket) {
		try {
			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Client connected: " + clientSocket);

				// Assign a unique identifier to the client
				int clientId = clientIdGenerator.getAndIncrement();

				// Create a new client handler for each connected client
				ClientHandler clientHandler = new ClientHandler(clientSocket, clientId);
				clients.add(clientHandler);
				executorService.execute(clientHandler);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * the <b>broadcastMessage</b> Broadcasts a message from one client to all other
	 * clients.
	 *
	 * @param message  The message to be broadcasted.
	 * @param senderId The ID of the client sending the message.
	 */

	private void broadcastMessage(String message, int senderId) {
		for (ClientHandler client : clients) {
			if (client.getClientId() != senderId) {
				client.sendMessage("Client number " + client.getClientId() + ": " + message);
			}
		}
	}

	/**
	 * The <b>ClientHandler</b> Represents a handler for a connected client.
	 * Implements the Runnable interface to handle client interactions in a separate
	 * thread.
	 */
	private class ClientHandler implements Runnable {
		private final Socket clientSocket;
		private final int clientId;

		private PrintWriter out;
		@SuppressWarnings("unused")
		private final String clientName;

		/**
		 * Constructs a ClientHandler with the given client socket and ID.
		 *
		 * @param socket   The client socket.
		 * @param clientId The ID of the client.
		 */

		public ClientHandler(Socket socket, int clientId) {
			this.clientSocket = socket;
			this.clientId = clientId;
			this.clientName = "Client " + clientId;
		}

		/**
		 * Retrieves the ID of the client.
		 *
		 * @return The ID of the client.
		 */

		public int getClientId() {
			return clientId;
		}

		/**
		 * Sends a message to the connected client.
		 *
		 * @param message The message to be sent.
		 */

		public void sendMessage(String message) {
			out.println("Server received: " + message);
		}

		/**
		 * The method <b> run</b> Handles incoming messages from the connected client.
		 * Reads messages from the client's input stream, broadcasts them to other
		 * clients, and prints the sent messages to the server console with the client
		 * ID. If an I/O exception occurs during communication with the client, it is
		 * printed to the error stream.
		 */

		public void run() {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					System.out.println("Client " + clientId + " sent: " + inputLine);
					broadcastMessage(inputLine, clientId);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
