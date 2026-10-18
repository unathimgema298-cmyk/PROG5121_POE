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
                "Message disregarded.",
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
}
