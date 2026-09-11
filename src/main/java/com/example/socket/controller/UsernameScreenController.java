package com.example.socket.controller;
import com.example.socket.client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;



/**
 * Controller for the username screen.
 * Handles joining the chat and exiting the application.
 */
public class UsernameScreenController {

    // Client used for connecting to the server
    private Client client;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Label errorLabel;

    /**
     * Checks the username entered by the user.
     * If valid, loads the chat screen and connects the client.
     */
    @FXML
    public void handlesJoinButtonClick() throws IOException {

        String userName = usernameTextField.getText().trim();

        // Checks if username is empty
        boolean userNameCheck = userName.isEmpty();

        if (userNameCheck) {

            errorLabel.setText("Enter Username");

        } else {

            errorLabel.setText("");

            // Load chat screen
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/socket/ChatScreen.fxml")
            );

            Parent root = loader.load();

            // Get chat screen controller
            ChatScreenController controller = loader.getController();

            // Pass username to chat screen
            controller.setUsername(userName);

            // Start client connection
            controller.initializeClient();

            // Change current stage to chat screen
            Stage stage = (Stage) usernameTextField.getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setTitle("Stream.Node Chat");

            stage.setScene(scene);

            stage.show();
        }
    }

    /**
     * Closes the application.
     */
    @FXML
    public void handleExitButton() {
        Stage stage = (Stage) usernameTextField.getScene().getWindow();
        stage.close();
    }
}