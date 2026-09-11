package com.example.socket.common;
import java.time.LocalDateTime;

/**
 * Represents a message sent in the chat application.
 * Stores the username, message content, and timestamp
 * of when the message was created.
 */

public class Message {
    // Username of the sender
    private String username;

    // Content of the message
    private String content;

    // Time the message was created
    private LocalDateTime timeStamp = LocalDateTime.now();

    /**
     * Creates a new message object using
     * a username and message content.
     */
    public Message(String username, String content) {
        this.username = username;
        this.content = content;
    }

    /**
     * Returns the username of the sender.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the message content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the timestamp of the message.
     */
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    /**
     * Formats the message into a readable string
     * with the username, timestamp, and content.
     */
    public String toString(){
        return username + ": " +timeStamp.format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a")) + "\n" +
                content;
    }

}
