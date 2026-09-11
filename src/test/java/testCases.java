import com.example.socket.client.Client;
import com.example.socket.common.Message;
import com.example.socket.server.Server;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the chat application.
 * Tests message creation, client connection,
 * and client disconnection.
 */
public class testCases {

    /**
     * Tests that the Message object stores
     * the correct username, content, and timestamp.
     */
    @Test
    public void messageCheck() {

        Message message = new Message("Fred", "Hello World");

        assertEquals("Fred", message.getUsername());

        assertEquals("Hello World", message.getContent());

        assertTrue(message.getTimeStamp() != null);
    }

    /**
     * Tests that a client can successfully
     * connect to the server.
     */
    @Test
    public void connectionCheck() throws InterruptedException {

        Client client = new Client();

        Server server = new Server();

        // Start server on separate thread
        new Thread(() -> {
            server.startServer();
        }).start();

        // Wait for server to start
        Thread.sleep(500);

        // Connect client to server
        client.connect("localhost", 1234);

        // Check if client connected
        assertTrue(client.getIsConnected());

        client.closeConnection();
    }

    /**
     * Tests that a client disconnects correctly.
     */
    @Test
    public void disconnectCheck() throws InterruptedException {

        Server server = new Server();

        // Start server on separate thread
        new Thread(() -> {
            server.startServer();
        }).start();

        // Wait for server to start
        Thread.sleep(500);

        Client client = new Client();

        // Connect client
        client.connect("localhost", 1234);

        // Disconnect client
        client.closeConnection();

        // Verify client disconnected
        assertFalse(client.getIsConnected());
    }
}
