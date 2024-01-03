package ie.atu.sw;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * The <b>class ChatClient</b> Represents a client-side implementation for a
 * chat application. This class manages the connection to a server, allows the
 * user to send messages to the server, reads messages from the server, and
 * handles the user's input and interaction via the console.
 * 
 * @author Venancio Moraes
 * @version 1.0
 * @since 03/01/24
 */
public class ChatClient {
	private Socket socket;
	private final String serverAddress;
	private final int port;

	/**
	 * the <b>ChatClient</b>Initializes a ChatClient instance with the provided
	 * server address, port, and client name.
	 *
	 * @param serverAddress The IP address or hostname of the server.
	 * @param port          The port number to connect to on the server.
	 * @param clientName    The name or identifier for this client.
	 */

	@SuppressWarnings("unused")
	private final String clientName;

	public ChatClient(String serverAddress, int port, String clientName) {
		this.serverAddress = serverAddress;
		this.port = port;
		this.clientName = clientName;
	}

	/**
	 * the method <b>start</b>Initiates the client-side connection to the server,
	 * handles user interaction, and message exchange with the server. Starts
	 * separate threads for reading server responses and managing user input.
	 */

	public void start() {
		try {
			socket = new Socket(serverAddress, port); // Attempt to establish a connection to the server
			System.out.println("Connected to server: " + socket); // // Print connection status and instructions for the
																	// user
			System.out.println("You are connected. You can start typing. Type 'quit' to cancel the connection.");

			new Thread(this::readMessages).start(); // Start a new thread to read messages from the server
			writeUserInput(); // Start capturing user input and sending it to the server
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The <b>readMessages</b>Reads messages received from the server and prints
	 * them to the client console. Closes the input stream if an I/O exception
	 * occurs.
	 */

	private void readMessages() {
		// Create a BufferedReader to read server responses from the socket's input
		// stream
		try (BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			String serverResponse;
			// Read each line of the server's response until the input stream is closed
			while ((serverResponse = serverIn.readLine()) != null) {
				System.out.println("Server: " + serverResponse);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The <b>writeUserInput</b>Reads user input from the console and sends it to
	 * the server. Handles the 'quit' command to close the client's connection
	 * gracefully. Closes the output stream after sending the 'quit' command.
	 */
	private void writeUserInput() {
		// Create a PrintWriter to send data to the server via the socket's output
		// stream
		try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				Scanner scanner = new Scanner(System.in)) { // Scanner to read user input from the console
			while (true) {
				// Read user input from the console
				String userInput = scanner.nextLine();
				out.println(userInput);
				if ("quit".equals(userInput)) { // Check if the user entered 'quit' to terminate the interaction
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	/**
	 * The method <b>close</b> Closes the socket connection if it's open. Handles
	 * any I/O exceptions that may occur during the closing process.
	 */
	private void close() {
		try {
			if (socket != null && !socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
