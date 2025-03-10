# ChatApplication

The ChatApplication is a simple console-based chat client-server application developed in Java. It allows users to connect to a server, send messages, and receive responses in real time. This application demonstrates the basic principles of network communication and client-server architecture.

Features
Real-time communication: Allows clients to send and receive messages via a socket connection to a server.
Multi-threaded: The client runs two threads simultaneously—one to handle incoming messages from the server and another to capture user input from the console.
Graceful termination: The client can exit the chat session by typing the quit command.

Technologies Used
Java: Core programming language.
Sockets: Utilizes Java's Socket class to establish a connection between the client and server.
Multithreading: Separate threads for reading and writing messages to ensure real-time communication.

How to Run the Application
Clone this repository:
git clone https://github.com/yourusername/ChatApplication.git

Compile the Java code: Ensure you have Java installed, then compile the code:
javac ChatClient.java

Run the ChatClient: After compiling, run the ChatClient by specifying the server address and port number:
java ChatClient <server-address> <port-number>

Interaction:
Once connected, you can start typing messages.
Type quit to close the connection.

Contributions
Feel free to contribute to this project. Fork it, make improvements, and send pull requests. Contributions are welcome!

License
This project is licensed under the MIT License.

