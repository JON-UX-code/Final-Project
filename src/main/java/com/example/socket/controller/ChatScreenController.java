package com.example.socket.controller;
import com.example.socket.client.Client;
import com.example.socket.common.Message;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.io.IOException;

/**
 * Controller for the chat screen.
 * Handles sending messages, connecting the client,
 * and disconnecting back to the username screen.
 */
public class ChatScreenController {

    // Stores the username entered on the first screen
    private String username;

    // Client used to connect to the chat server
    private Client client;

    @FXML
    private ListView<String> chatLog;

    @FXML
    private TextField textBox;

    /**
     * Sends the message typed by the user.
     * Empty messages are ignored.
     */
    @FXML
    public void handleSendButton() {
        String content = textBox.getText().trim();

        if (!content.isEmpty()) {
            Message message = new Message(username, content);
            client.sendMessage(message.toString());
            textBox.clear();
        }
    }

    /**
     * Stores the username passed from the username screen.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Creates the client, connects it to the server,
     * and starts listening for incoming messages.
     */
    public void initializeClient() throws IOException {
        client = new Client();

        client.connect("localhost", 1234);

        new Thread(() -> {
            client.listen(chatLog);
        }).start();
    }

    /**
     * Disconnects the client and returns to the username screen.
     */
    @FXML
    public void handleDisconnectButton() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/socket/UsernameScreen.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) textBox.getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setTitle("Stream.Node Chat");
        stage.setScene(scene);
        stage.show();

        if (client != null) {
            new Thread(() -> {
                client.closeConnection();
            }).start();
        }
    }
}