/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121_poe;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
/**
 *
 * @author Student
 */
public class Message {
    
     // Message attributes
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private static int totalMessagesSent = 0;
    private static ArrayList<String> sentMessages = new ArrayList<>(); 

    // Constructor
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = "";
        this.messageHash = "";
    }       
    
    // Checks that the recipient number is valid
    public String checkRecipientCell() {
    if (recipient != null && recipient.matches("^\\+[1-9]\\d{7,14}$")) {
        return "Cell phone number successfully captured.";
    } else {
        return "Cell phone number is incorrectly formatted or "
                + "does not contain an international code. "
                + "Please correct the number and try again.";
        }
    }
    
    //creates a unique message ID
    public String createMessageID() {
        messageID = String.format("%010d",
                java.util.concurrent.ThreadLocalRandom.current()
                        .nextLong(10000000000L));
        
        return messageID;
    }
    
    //checks that the message does not exceed 250 characters
    public boolean checkMessageLength() {
        return messageText != null
                && messageText.length() <= 250;
    }
    
    // Generates the message hash
    public String createMessageHash() {

        if (messageID == null || !messageID.matches("\\d{10}")) {
            return "Invalid message ID";
        }

        if (messageText == null || messageText.trim().isEmpty()) {
            return "Invalid message";
        }

        String[] words = messageText.trim().split("\\s+");

        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");

        messageHash = messageID.substring(0, 2)
                + ":" + messageNumber
                + ":" + firstWord.toUpperCase()
                + lastWord.toUpperCase();

        return messageHash;
}
    
    // Checks that the message ID contains exactly 10 digits
    public boolean checkMessageID() { 
        return messageID != null
            && messageID.matches("\\d{10}");
    }
    
    // Processes the user's message choice
    public String SentMessage(String choice) {
        
        if (choice == null) {            
            return "Invalid option. Please try again.";
        }

        if (choice.equalsIgnoreCase("Send")) {
            totalMessagesSent++;
            sentMessages.add(messageText);
            
            return "Message successfully sent.";
            
        } else if (choice.equalsIgnoreCase("Store")) {
            return storeMessage();
            
        } else if (choice.equalsIgnoreCase("Disregard")) {
            return "Press 0 to delete the message.";
            
        } else {
            return "Invalid option. Please try again.";
        }
    }
    
    //returns the total number of messages sent
    public int returnTotalMessagess() {
        return totalMessagesSent;
    }
    
    // Returns all messages sent during the program
    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent yet.";
        }

        return String.join("\n", sentMessages);
    }
    
    // Stores the message in a JSON file
    public String storeMessage() {
        String json = "{"
                + "\"messageID\":\"" + escapeJson(messageID) + "\","
                + "\"messageNumber\":" + messageNumber + ","
                + "\"recipient\":\"" + escapeJson(recipient) + "\","
                + "\"messageText\":\"" + escapeJson(messageText) + "\","
                + "\"messageHash\":\"" + escapeJson(messageHash) + "\""
                + "}";

        try (FileWriter writer = new FileWriter("messages.jsonl", true)) {
            writer.write(json + System.lineSeparator());
            return "Message successfully stored.";
        } catch (IOException e) {
            return "Error storing message: " + e.getMessage();
        }
    }

    // Escapes special characters for valid JSON
    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
