module org.example.finaljavaproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.socket to javafx.fxml;
    exports com.example.socket.client;
    exports com.example.socket.server;
    exports com.example.socket.common;
    exports com.example.socket.presentation;
    opens com.example.socket.presentation to javafx.fxml;
    exports com.example.socket.controller;
    opens com.example.socket.controller to javafx.fxml;
}