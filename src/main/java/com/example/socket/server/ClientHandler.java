package com.example.socket.server;
import java.io.*;
import java.net.Socket;

/**
 * Handles one client connected to the server.
 * Reads messages from that client and sends messages back when needed.
 */
public class ClientHandler extends Thread {

    // Socket connection for one client
    private Socket clientSocket;

    // Reads messages from the client
    private BufferedReader bufferedReader;

    // Sends messages to the client
    private PrintWriter printWriter;

    // Reference to the server for broadcasting messages
    private Server server;

    /**
     * Creates a client handler and sets up input/output streams.
     */
    public ClientHandler(Socket socket, Server server) {
        this.clientSocket = socket;
        this.server = server;

        try {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
            );

            printWriter = new PrintWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream()), true
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs the client handler thread.
     * Reads incoming messages and asks the server to broadcast them.
     */
    public void run() {
        String message;

        try {
            while ((message = bufferedReader.readLine()) != null) {
                server.broadcastMessage(message);
            }

            closeConnection();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to this client.
     */
    public void sendMessage(String message) {
        printWriter.println(message);
    }

    /**
     * Closes this client's connection and removes it from the server.
     */
    public void closeConnection() {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }

            if (printWriter != null) {
                printWriter.close();
            }

            if (clientSocket != null) {
                clientSocket.close();
            }

            server.removeClient(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}