package com.example.socket.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Server for the chat application.
 * Accepts client connections and broadcasts messages
 * to all connected clients.
 */
public class Server {

    // Stores all connected clients
    private List<ClientHandler> clientHandlers = new ArrayList<>();

    /**
     * Starts the server and waits for clients to connect.
     */
    public void startServer() {

        try {

            // Create server socket on port 1234
            ServerSocket serverSocket = new ServerSocket(1234);

            System.out.println("Server started...");

            // Continuously accept new clients
            while (true) {

                Socket clientSocket = serverSocket.accept();

                // Create handler for connected client
                ClientHandler clientHandler =
                        new ClientHandler(clientSocket, this);

                // Add client to list
                clientHandlers.add(clientHandler);

                // Start client thread
                clientHandler.start();

                System.out.println("Client connected");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to all connected clients.
     */
    public void broadcastMessage(String message) {

        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendMessage(message);
        }
    }

    /**
     * Removes a disconnected client from the server.
     */
    public void removeClient(ClientHandler clientHandler) {

        clientHandlers.remove(clientHandler);

        System.out.println("Client disconnected");
    }
}
