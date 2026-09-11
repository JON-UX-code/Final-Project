package com.example.socket.client;
import javafx.application.Platform;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.scene.control.ListView;

/**
 * Handles the client side of the chat application.
 * Connects to the server, sends messages, and listens for incoming messages.
 */
public class Client {

    // Socket connection to server
    private Socket clientSocket;

    // Reads incoming messages
    private BufferedReader bufferedReader;

    // Sends messages to server
    private PrintWriter printWriter;

    // Tracks connection status
    private boolean isConnected;

    // Connects client to server
    public void connect(String host, int port) {
        try {

            clientSocket = new Socket(host, port);

            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            printWriter = new PrintWriter(clientSocket.getOutputStream(), true);

            isConnected = true;

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sends a message to the server
    public void sendMessage(String message){
        printWriter.println(message);
    }

    // Listens for incoming messages
    public void listen(ListView<String> chatLog) {
        try {
            String msg;
            while (isConnected && (msg = bufferedReader.readLine()) != null) {
                String finalMsg = msg;
                Platform.runLater(() -> {
                    chatLog.getItems().add(finalMsg);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Closes client connection
    public void closeConnection() {
        try {
            isConnected = false;
            if (bufferedReader != null) {
                bufferedReader.close();
            }

            if (printWriter != null) {
                printWriter.close();
            }

            if (clientSocket != null) {
                clientSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Returns connection status
    public boolean getIsConnected() {
        return isConnected;
    }
}
