/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.prog5121_poe;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MessageTest {

    // Test 1: Valid recipient number
    @Test
    public void testCheckRecipientCell() {
        Message message = new Message(
                0,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell()
        );
    }

    // Test 2: Invalid recipient number
    @Test
    public void testInvalidRecipientCell() {
        Message message = new Message(
                0,
                "12345",
                "Hello Mike"
        );

        assertEquals(
                "Cell phone number is incorrectly formatted or "
                + "does not contain an international code. "
                + "Please correct the number and try again.",
                message.checkRecipientCell()
        );
    }

    // Test 3: Generate a 10-digit message ID
    @Test
    public void testCreateMessageID() {
        Message message = new Message(
                0,
                "+27718693002",
                "Hello Mike"
        );

        String messageID = message.createMessageID();

        assertTrue(messageID.matches("\\d{10}"));
        assertTrue(message.checkMessageID());
    }

    // Test 4: Validate the message ID
    @Test
    public void testCheckMessageID() {
        Message message = new Message(
                0,
                "+27718693002",
                "Hello Mike"
        );

        message.createMessageID();

        assertTrue(message.checkMessageID());
    }

    // Test 5: Valid message length
    @Test
    public void testValidMessageLength() {
        Message message = new Message(
                0,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertTrue(message.checkMessageLength());
    }

    // Test 6: Invalid message length
    @Test
    public void testInvalidMessageLength() {
        String longMessage = "A".repeat(251);

        Message message = new Message(
                0,
                "+27718693002",
                longMessage
        );

        assertFalse(message.checkMessageLength());
    }

    // Test 7: Message hash generation
    @Test
    public void testCreateMessageHash() {
        Message message = new Message(
                0,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        String messageID = message.createMessageID();

        String expected = messageID.substring(0, 2)
                + ":0:HITONIGHT";

        assertEquals(expected, message.createMessageHash());
    }

    // Test 8: Send a message
    @Test
    public void testSentMessage() {
        Message message = new Message(
                0,
                "+27718693002",
                "Hello Mike"
        );

        assertEquals(
                "Message successfully sent.",
                message.SentMessage("Send")
        );
    }

    // Test 9: Disregard a message
    @Test
    public void testDisregardMessage() {
        Message message = new Message(
                0,
                "+27718693002",
                "Hello Mike"
        );

        assertEquals(
                "Press 0 to delete the message.",
                message.SentMessage("Disregard")
        );
    }

    // Test 10: Total messages sent
    @Test
    public void testReturnTotalMessagess() {
        Message message = new Message(
                0,
                "+27718693002",
                "Hello Mike"
        );

        int initialTotal = message.returnTotalMessagess();

        message.SentMessage("Send");

        assertEquals(
                initialTotal + 1,
                message.returnTotalMessagess()
        );
    }

    // Test 11: Display sent messages
    @Test
    public void testPrintMessages() {
        String uniqueText = "JUnitMessage"
                + System.nanoTime();

        Message message = new Message(
                0,
                "+27718693002",
                uniqueText
        );

        message.SentMessage("Send");

        assertTrue(
                message.printMessages().contains(uniqueText)
        );
    }

    // Test 12: Store a message in JSON Lines
    @Test
    public void testStoreMessage() throws IOException {
        Message message = new Message(
                0,
                "+27718693002",
                "Hello Mike"
        );

        message.createMessageID();
        message.createMessageHash();

        File file = new File("messages.jsonl");
        boolean existedBefore = file.exists();
        long originalSize = existedBefore ? file.length() : 0;

        try {
            assertEquals(
                    "Message successfully stored.",
                    message.storeMessage()
            );

            assertTrue(file.exists());

            String content = Files.readString(file.toPath());

            assertTrue(content.contains("\"recipient\""));
            assertTrue(content.contains("+27718693002"));
            assertTrue(content.contains("Hello Mike"));

        } finally {
            // Restore the original file length after the test
            if (existedBefore) {
                try (java.io.RandomAccessFile raf =
                        new java.io.RandomAccessFile(file, "rw")) {
                    raf.setLength(originalSize);
                }
            } else {
                Files.deleteIfExists(Path.of("messages.jsonl"));
            }
        }
    }
    
    
    // Test 13: Sent messages array
    @Test
    public void testSentMessagesArray() {
        String uniqueText = "SentArray" + System.nanoTime();

        Message message = new Message(
                1, "+27834557896", uniqueText
        );

        message.createMessageID();
        message.createMessageHash();
        message.SentMessage("Send");

        assertTrue(
                java.util.Arrays.asList(
                        Message.getSentMessagesArray()
                ).contains(uniqueText)
        );
    }

    // Test 14: Disregarded messages array
    @Test
    public void testDisregardedMessagesArray() {
        String uniqueText = "DisregardedArray" + System.nanoTime();

        Message message = new Message(
                2, "+27834484567", uniqueText
        );

        message.createMessageID();
        message.createMessageHash();
        message.SentMessage("Disregard");

        assertTrue(
                java.util.Arrays.asList(
                        Message.getDisregardedMessagesArray()
                ).contains(uniqueText)
        );
    }

    // Test 15: Stored messages, hashes and IDs arrays
    @Test
    public void testStoredMessagesArrays() throws IOException {
        Path file = Path.of("messages.jsonl");
        byte[] originalContent = backupStoredFile(file);

        try {
            Message message = new Message(
                    3, "+27838884567", "StoredArrayTest"
            );

            message.createMessageID();
            message.createMessageHash();

            assertEquals(
                    "Message successfully stored.",
                    message.storeMessage()
            );

            assertTrue(
                    java.util.Arrays.asList(
                            Message.getStoredMessagesArray()
                    ).stream().anyMatch(
                            stored -> stored.getMessageID().equals(
                                    message.getMessageID()
                            )
                    )
            );

            assertTrue(
                    java.util.Arrays.asList(
                            Message.getMessageIDsArray()
                    ).contains(message.getMessageID())
            );

            assertTrue(
                    java.util.Arrays.asList(
                            Message.getMessageHashesArray()
                    ).contains(message.getMessageHash())
            );

        } finally {
            restoreStoredFile(file, originalContent);
        }
    }

    private byte[] backupStoredFile(Path file) throws IOException {
        return Files.exists(file)
                ? Files.readAllBytes(file)
                : null;
    }

    private void restoreStoredFile(
            Path file, byte[] originalContent
    ) throws IOException {
        if (originalContent == null) {
            Files.deleteIfExists(file);
        } else {
            Files.write(file, originalContent);
        }
    }
    
    // Helper: creates and stores a test message
    private Message createStoredTestMessage(
            int number, String recipient, String text, String sender) {

        Message message = new Message(number, recipient, text);
        message.setSender(sender);
        message.createMessageID();
        message.createMessageHash();

        assertEquals(
                "Message successfully stored.",
                message.storeMessage()
        );

        return message;
    }

    // Test 16: Display stored senders and recipients
    @Test
    public void testDisplayStoredSendersAndRecipients()
            throws IOException {

        Path file = Path.of("messages.jsonl");
        byte[] originalContent = backupStoredFile(file);

        try {
            createStoredTestMessage(
                    1, "+27834557896",
                    "Sender display test", "Test Sender"
            );

            String result =
                    Message.displayStoredSendersAndRecipients();

            assertTrue(result.contains("Test Sender"));
            assertTrue(result.contains("+27834557896"));

        } finally {
            restoreStoredFile(file, originalContent);
        }
    }

    // Test 17: Find the longest stored message
    @Test
    public void testGetLongestStoredMessage()
            throws IOException {

        Path file = Path.of("messages.jsonl");
        byte[] originalContent = backupStoredFile(file);

        try {
            String longestText =
                    "This is the longest stored message for testing.";

            createStoredTestMessage(
                    1, "+27834557896", "Short message", "Sender"
            );

            createStoredTestMessage(
                    2, "+27838884567", longestText, "Sender"
            );

            assertEquals(
                    longestText,
                    Message.getLongestStoredMessage()
            );

        } finally {
            restoreStoredFile(file, originalContent);
        }
    }

    // Test 18: Search for a message using its ID
    @Test
    public void testSearchMessageByID()
            throws IOException {

        Path file = Path.of("messages.jsonl");
        byte[] originalContent = backupStoredFile(file);

        try {
            Message message = createStoredTestMessage(
                    1, "+27834557896",
                    "Search by ID test", "Sender"
            );

            Message result =
                    Message.searchMessageByID(message.getMessageID());

            assertNotNull(result);
            assertEquals(message.getMessageText(),
                    result.getMessageText());

        } finally {
            restoreStoredFile(file, originalContent);
        }
    }

    // Test 19: Search stored messages by recipient
    @Test
    public void testSearchMessagesByRecipient()
            throws IOException {

        Path file = Path.of("messages.jsonl");
        byte[] originalContent = backupStoredFile(file);

        try {
            createStoredTestMessage(
                    1, "+27838884567",
                    "First recipient message", "Sender"
            );

            createStoredTestMessage(
                    2, "+27838884567",
                    "Second recipient message", "Sender"
            );

            String result =
                    Message.searchMessagesByRecipient("+27838884567");

            assertTrue(result.contains("First recipient message"));
            assertTrue(result.contains("Second recipient message"));

        } finally {
            restoreStoredFile(file, originalContent);
        }
    }

    // Test 20: Delete a stored message using its hash
    @Test
    public void testDeleteMessageByHash()
            throws IOException {

        Path file = Path.of("messages.jsonl");
        byte[] originalContent = backupStoredFile(file);

        try {
            Message message = createStoredTestMessage(
                    1, "+27834557896",
                    "Delete by hash test", "Sender"
            );

            String result =
                    Message.deleteMessageByHash(
                            message.getMessageHash()
                    );

            assertTrue(result.contains("successfully deleted"));
            assertNull(
                    Message.searchMessageByID(message.getMessageID())
            );

        } finally {
            restoreStoredFile(file, originalContent);
        }
    }

    // Test 21: Generate the full stored messages report
    @Test
    public void testGenerateStoredMessagesReport()
            throws IOException {

        Path file = Path.of("messages.jsonl");
        byte[] originalContent = backupStoredFile(file);

        try {
            Message message = createStoredTestMessage(
                    1, "+27834557896",
                    "Full report test", "Report Sender"
            );

            String report =
                    Message.generateStoredMessagesReport();

            assertTrue(report.contains(message.getMessageID()));
            assertTrue(report.contains(message.getMessageHash()));
            assertTrue(report.contains("Report Sender"));
            assertTrue(report.contains("+27834557896"));
            assertTrue(report.contains("Full report test"));

        } finally {
            restoreStoredFile(file, originalContent);
        }
    }

}
