/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.prog5121_poe;

import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String sender;

    private static int totalMessagesSent = 0;

    private static ArrayList<String> sentMessages = new ArrayList<>();
    private static ArrayList<String> disregardedMessages = new ArrayList<>();
    private static ArrayList<Message> storedMessages = new ArrayList<>();
    private static ArrayList<Message> allMessages = new ArrayList<>();

    private static ArrayList<String> messageHashes = new ArrayList<>();
    private static ArrayList<String> messageIDs = new ArrayList<>();

    private static final String FILE_NAME = "messages.jsonl";
    private static final Gson gson = new Gson();

    // Constructor
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = "";
        this.messageHash = "";
        this.sender = "User";
    }

    // Sets the sender's name
    public void setSender(String sender) {
        this.sender = sender;
    }

    // Checks the recipient's cellphone number
    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+[1-9]\\d{7,14}$")) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or "
                    + "does not contain an international code. "
                    + "Please correct the number and try again.";
        }
    }

    // Creates a unique 10-digit message ID
    public String createMessageID() {
        messageID = String.format("%010d",
                java.util.concurrent.ThreadLocalRandom.current()
                        .nextLong(10000000000L));

        return messageID;
    }

    // Checks that the message does not exceed 250 characters
    public boolean checkMessageLength() {
        return messageText != null && messageText.length() <= 250;
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
        String lastWord = words[words.length - 1]
                .replaceAll("[^a-zA-Z0-9]", "");

        messageHash = messageID.substring(0, 2)
                + ":" + messageNumber
                + ":" + firstWord.toUpperCase()
                + lastWord.toUpperCase();

        return messageHash;
    }

    // Checks that the message ID contains exactly 10 digits
    public boolean checkMessageID() {
        return messageID != null && messageID.matches("\\d{10}");
    }

    // Processes the user's message choice
    public String SentMessage(String choice) {

        if (choice == null) {
            return "Invalid option. Please try again.";
        }

        if (!choice.equalsIgnoreCase("Send")
                && !choice.equalsIgnoreCase("Store")
                && !choice.equalsIgnoreCase("Disregard")) {
            return "Invalid option. Please try again.";
        }

        if (checkMessageID() && !messageIDs.contains(messageID)) {
            messageIDs.add(messageID);
        }

        if (messageHash != null
                && !messageHash.isEmpty()
                && !messageHash.equals("Invalid message")
                && !messageHashes.contains(messageHash)){
            messageHashes.add(messageHash);
        }
        
        if (!allMessages.contains(this)) {
            allMessages.add(this);
        }

        if (choice.equalsIgnoreCase("Send")) {
            totalMessagesSent++;
            sentMessages.add(messageText);

            return "Message successfully sent.";

        } else if (choice.equalsIgnoreCase("Store")) {
            return storeMessage();

        } else {
            disregardedMessages.add(messageText);

            return "Press 0 to delete the message.";
        }
    }

    // Returns the total number of messages sent
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

    // Returns the sent messages as a dynamic array
    public static String[] getSentMessagesArray() {
        return sentMessages.toArray(new String[0]);
    }

    // Returns the disregarded messages as a dynamic array
    public static String[] getDisregardedMessagesArray() {
        return disregardedMessages.toArray(new String[0]);
    }

    // Returns the stored messages as an array
    public static Message[] getStoredMessagesArray() {
        loadStoredMessages();
        return storedMessages.toArray(new Message[0]);
    }

    // Returns all stored message hashes
    public static String[] getMessageHashesArray() {

        loadStoredMessages();

        ArrayList<String> allHashes = new ArrayList<>(messageHashes);

        for (Message message : storedMessages) {
            if (message.messageHash != null
                    && !message.messageHash.isEmpty()
                    && !allHashes.contains(message.messageHash)) {
                allHashes.add(message.messageHash);
            }
        }

        return allHashes.toArray(new String[0]);
    }

    // Returns all stored message IDs
    public static String[] getMessageIDsArray() {

        loadStoredMessages();

        ArrayList<String> allIDs = new ArrayList<>(messageIDs);

        for (Message message : storedMessages) {
            if (message.messageID != null
                    && !message.messageID.isEmpty()
                    && !allIDs.contains(message.messageID)) {
                allIDs.add(message.messageID);
            }
        }

        return allIDs.toArray(new String[0]);
    }

    // Stores the message in a JSON Lines file
    public String storeMessage() {

        try {
            loadStoredMessages();

            storedMessages.add(this);

            saveStoredMessages();

            return "Message successfully stored.";

        } catch (IOException e) {
            storedMessages.remove(this);
            return "Error storing message: " + e.getMessage();
        }
    }

    // Reads stored messages from the JSON file
    public static void loadStoredMessages() {

        storedMessages.clear();

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(file.toPath());

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    Message message = gson.fromJson(
                            JsonParser.parseString(line),
                            Message.class
                    );

                    if (message != null) {
                        storedMessages.add(message);
                    }
                }
            }

        } catch (IOException | RuntimeException e) {
            System.out.println("Error reading stored messages: "
                    + e.getMessage());
        }
    }

    // Saves all stored messages to the JSON file
    private static void saveStoredMessages() throws IOException {

        try (FileWriter writer = new FileWriter(FILE_NAME, false)) {

            for (Message message : storedMessages) {
                writer.write(gson.toJson(message));
                writer.write(System.lineSeparator());
            }
        }
    }

    // Displays the sender and recipient of stored messages
    public static String displayStoredSendersAndRecipients() {

        loadStoredMessages();

        if (storedMessages.isEmpty()) {
            return "No stored messages available.";
        }

        StringBuilder result = new StringBuilder();

        for (Message message : storedMessages) {
            result.append("Sender: ")
                    .append(message.sender)
                    .append("\nRecipient: ")
                    .append(message.recipient)
                    .append("\n\n");
        }

        return result.toString();
    }

    // Finds the longest stored message
    public static String getLongestStoredMessage() {

        loadStoredMessages();

        if (storedMessages.isEmpty()) {
            return "No stored messages available.";
        }

        Message longest = storedMessages.get(0);

        for (Message message : storedMessages) {
            if (message.messageText.length()
                    > longest.messageText.length()) {
                longest = message;
            }
        }

        return longest.messageText;
    }

    // Searches for a stored message using its ID
    public static Message searchMessageByID(String id) {
        
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        loadStoredMessages(); 

        // Search messages stored during the current session.
        for (Message message : allMessages) {
            if (message.getMessageID().equals(id)) {
                return message;
            }
        }

        // Also search messages loaded from the JSON file.
        for (Message message : storedMessages) {
            if (message.getMessageID().equals(id)) {
                return message;
            }
        }

        return null;
    }

    // Searches stored messages using the recipient's number
    public static String searchMessagesByRecipient(String number) {
        
        if (number == null || number.trim().isEmpty()) {
            return "Recipient number cannot be empty.";
        }

        loadStoredMessages();

        StringBuilder result = new StringBuilder();

        for (Message message : storedMessages) {
            if (message.recipient.equals(number)) {
                result.append(message.messageText).append("\n");
            }
        }

        if (result.length() == 0) {
            return "No messages found for this recipient.";
        }

        return result.toString().trim();
    }

    // Deletes a stored message using its hash
    public static String deleteMessageByHash(String hash) {

        if (hash == null || hash.trim().isEmpty()) {
            return "Message hash cannot be empty.";
        }

        loadStoredMessages();

        Message messageToDelete = null;

        for (Message message : storedMessages) {
            if (message.messageHash != null
                    && message.messageHash.equals(hash)) {
                messageToDelete = message;
                break;
            }
        }

        if (messageToDelete == null) {
            return "Message not found.";
        }

        int originalIndex = storedMessages.indexOf(messageToDelete);

        storedMessages.remove(messageToDelete);

        try {
            saveStoredMessages();

        } catch (IOException e) {
            storedMessages.add(originalIndex, messageToDelete);
            return "Error deleting message: " + e.getMessage();
        }

        // Remove the deleted message from the other collections.
        final String deletedID = messageToDelete.messageID;
        final String deletedHash = messageToDelete.messageHash;

        messageHashes.remove(deletedHash);
        messageIDs.remove(deletedID);

        allMessages.removeIf(message ->
                deletedID.equals(message.messageID));

        return "Message: “" + messageToDelete.messageText
                + "” successfully deleted.";
    }

    // Displays the full report of stored messages
    public static String generateStoredMessagesReport() {

        loadStoredMessages();

        if (storedMessages.isEmpty()) {
            return "No stored messages available.";
        }

        StringBuilder report = new StringBuilder();

        for (Message message : storedMessages) {

            report.append("Message ID: ")
                    .append(message.messageID)
                    .append("\nMessage Hash: ")
                    .append(message.messageHash)
                    .append("\nMessage Number: ")
                    .append(message.messageNumber)
                    .append("\nSender: ")
                    .append(message.sender)
                    .append("\nRecipient: ")
                    .append(message.recipient)
                    .append("\nMessage: ")
                    .append(message.messageText)
                    .append("\n--------------------------\n");
        }

        return report.toString();
    }

    // Getter methods
    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getSender() {
        return sender;
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
