/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.prog5121_poe;
import java.util.Scanner;
/**
 *
 * @author Student
 */
public class PROG5121_POE {

    public static void main(String[] args) {
        
        Scanner input = new Scanner(System.in);
        
        System.out.println("Enter your first name: ");
        String firstName = input.nextLine();
        
        System.out.println("Enter your last name: ");
        String lastName = input.nextLine();
        
        System.out.println("Enter your username: ");
        String username = input.nextLine();
        
        System.out.println("Enter your password: ");
        String password = input.nextLine();
        
        System.out.println("Enter your South African cellphone number (+27): ");
        String cellPhoneNumber = input.nextLine();
        
        Login user = new Login(username, password, cellPhoneNumber, 
        firstName, lastName);
        
        System.out.println(user.registerUser());
        
        System.out.println("\n--- LOGIN ---");
        
        System.out.println("Enter your username to login: ");
        String loginUsername = input.nextLine();
        
        System.out.println("Enter your password to login: ");
        String loginPassword = input.nextLine();
        
        System.out.println(user.returnLoginStatus(loginUsername, loginPassword));
        
        if (user.loginUser(loginUsername, loginPassword)) {
            System.out.println("Welcome to QuikChat.");
            
            int choice;
            
            do {
                System.out.println("\n--- QUICKCHAT MENU ---");
                System.out.println("1. Send Messages");
                System.out.println("2. Show Recently Sent Messages");
                System.out.println("3. Stored Messages");
                System.out.println("4. Quit");
                System.out.print("Choose an option: ");

                while (!input.hasNextInt()) {
                    System.out.println("Invalid input. Enter a valid menu option (1-4).");
                    input.nextLine();
                }
                
                choice = input.nextInt();
                input.nextLine();
                
                switch (choice) {
                    case 1:
                        System.out.println("--- SEND MESSAGE ---");

                        System.out.print("Enter recipient number: ");
                        String recipient = input.nextLine();

                        System.out.print("Enter your message: ");
                        String messageText = input.nextLine();

                        Message messageCounter = new Message(0, "", "");
                        
                        Message message = new Message(
                                messageCounter.returnTotalMessagess(),
                                recipient,
                                messageText
                        );
                        
                        message.setSender(firstName + " " + lastName);

                        String recipientStatus = message.checkRecipientCell();
                        System.out.println(recipientStatus);
                        
                        if (!recipientStatus.equals("Cell phone number successfully captured.")) {
                            break;
                        }
                        
                        if (message.checkMessageLength()) {
                            System.out.println("Message ready to send.");

                            String messageID = message.createMessageID();
                            System.out.println("Message ID: " + messageID);

                            System.out.println("Message Hash: "
                            + message.createMessageHash());
                            
                            System.out.println("\nWhat would you like to do?");
                            System.out.println("1. Send");
                            System.out.println("2. Store");
                            System.out.println("3. Disregard");
                            System.out.print("Choose an option: ");
                            
                            String messageChoice = input.nextLine();
                            
                            switch (messageChoice) {
                                case "1":
                                    System.out.println(message.SentMessage("Send"));
                                    break;
                                    
                                case "2":
                                    System.out.println(message.SentMessage("Store"));
                                    break;
                                    
                                case "3":
                                    System.out.println(message.SentMessage("Disregard"));
                                    break;
                                    
                                default:
                                    System.out.println("Invalid message option.");
                            }

                        } else {
                            System.out.println("Message exceeds 250 characters by "
                                    + (messageText.length() - 250)
                                    + "; please reduce the size.");
                        }

                        break;

                    case 2:
                        System.out.println("--- RECENTLY SENT MESSAGES ---");
                        
                        Message messageHistory = new Message(0, "", "");
                        
                        System.out.println(messageHistory.printMessages());
                        
                        System.out.println("Total messages sent: "
                                + messageHistory.returnTotalMessagess());
                        
                        break;
                        
                    case 3:
                        int storedChoice;

                        do {
                            System.out.println("\n--- STORED MESSAGES ---");
                            System.out.println("1. Display senders and recipients");
                            System.out.println("2. Display longest stored message");
                            System.out.println("3. Search message by ID");
                            System.out.println("4. Search messages by recipient");
                            System.out.println("5. Delete message using hash");
                            System.out.println("6. Display full stored messages report");
                            System.out.println("7. Back to main menu");
                            System.out.print("Choose an option: ");

                            while (!input.hasNextInt()) {
                            System.out.println("Invalid input. Enter a number from 1 to 7.");
                            input.nextLine();
                            }

                            storedChoice = input.nextInt();
                            input.nextLine();

                            switch (storedChoice) {
                                case 1:
                                    System.out.println(
                                        Message.displayStoredSendersAndRecipients());
                                    break;

                                case 2:
                                    System.out.println("Longest stored message:");
                                    System.out.println(Message.getLongestStoredMessage());
                                    break;

                                case 3:
                                    System.out.print("Enter message ID: ");
                                    String searchID = input.nextLine();
                                    
                                    if (searchID.trim().isEmpty()) {
                                        System.out.println("Message ID cannot be empty.");
                                        break;
                                    }

                                    Message foundMessage =
                                            Message.searchMessageByID(searchID);

                                    if (foundMessage != null) {
                                        System.out.println("Recipient: "
                                            + foundMessage.getRecipient());
                                        System.out.println("Message: "
                                            + foundMessage.getMessageText());
                                    } else {
                                        System.out.println("Message ID not found.");
                                    }
                                    break;

                                case 4:
                                    System.out.print("Enter recipient number: ");
                                    String searchRecipient = input.nextLine();

                                    System.out.println(
                                            Message.searchMessagesByRecipient(searchRecipient));
                                    break;

                                case 5:
                                    System.out.print("Enter message hash: ");
                                    String searchHash = input.nextLine();

                                    System.out.println(
                                            Message.deleteMessageByHash(searchHash));
                                    break;

                                case 6:
                                    System.out.println(
                                            Message.generateStoredMessagesReport());
                                    break;

                                case 7:
                                    System.out.println("Returning to main menu.");
                                    break;

                                default:
                                    System.out.println("Invalid option. Choose 1 to 7.");
                            }

                        } while (storedChoice != 7);

                        break;

                    case 4:
                        System.out.println("Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid option. Try again.");
                }

            } while (choice != 4);
    
        } else {
            System.out.println("You must login successfully to use QuikChat.");
        }
    }
   
}
