package com.example.socket.presentation;
import com.example.socket.server.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * Main JavaFX application class.
 * Launches the server and opens the username screen.
 */
public class MainApp extends Application {

    /**
     * Loads and displays the username screen.
     */
    @Override
    public void start(Stage stage) throws IOException {

        // Load username screen FXML
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/socket/UsernameScreen.fxml")
        );

        Parent root = loader.load();

        Scene scene = new Scene(root);

        // Set application title
        stage.setTitle("Stream.Node Chat");

        // Display scene
        stage.setScene(scene);

        stage.show();
    }

    /**
     * Starts the server and launches JavaFX.
     */
    public static void main(String[] args) {

        // Create server object
        Server server = new Server();

        // Run server on separate thread
        new Thread(() -> {
            server.startServer();
        }).start();

        // Launch JavaFX application
        launch(args);
    }
}