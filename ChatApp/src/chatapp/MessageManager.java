/*
Module name: Programming 1A
Module code: PROG5121/p/w
Assessment type: Portfolio of Evidence (POE)- PART 2
Student Name: Lethabo
Student surname: Mohapi
Student username: ST10291427@rcconnect.edu.za
Student number: ST10291427

Part 2 Chat Messaging Feature - Enhanced
MessageManager.java - Manages message creation, storage, and retrieval
*/
package chatapp;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageManager {
    // Map to store messages for each user (username -> list of messages)
    private HashMap<String, List<Message>> userMessages;
    // Map to store message counts for each user
    private HashMap<String, Integer> userMessageCounts;
    
    /**
     * Constructor for MessageManager
     */
    public MessageManager() {
        userMessages = new HashMap<>();
        userMessageCounts = new HashMap<>();
    }
    
    /**
     * Initialize a new user in the message system
     * @param username The username to initialize
     */
    public void initializeUser(String username) {
        if (!userMessages.containsKey(username)) {
            userMessages.put(username, new ArrayList<>());
            userMessageCounts.put(username, 0);
        }
    }
    
    /**
     * Get the number of messages a user has sent
     * @param username The username to check
     * @return The number of messages sent
     */
    public int getMessageCount(String username) {
        return userMessageCounts.getOrDefault(username, 0);
    }
    
    /**
     * Handle the message creation workflow
     * @param sender The username of the sender
     * @return true if a message was successfully created, false otherwise
     */
    public boolean createMessage(String sender) {
        try {
            // Get recipient phone number
            String recipient = JOptionPane.showInputDialog(
                null,
                "Enter recipient's phone number (with +27 prefix):",
                "New Message",
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (recipient == null) {
                return false; // User canceled
            }
            
            // Validate recipient format and show appropriate message
            String validationResult = Message.validateRecipientNumber(recipient);
            JOptionPane.showMessageDialog(
                null,
                validationResult,
                "Phone Number Validation",
                validationResult.startsWith("Cell phone number successfully") ? 
                    JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
            );
            
            if (!validationResult.startsWith("Cell phone number successfully")) {
                return false; // Invalid phone number
            }
            
            // Get message content
            String content = JOptionPane.showInputDialog(
                null,
                "Enter message (max 250 characters):",
                "New Message",
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (content == null) {
                return false; // User canceled
            }
            
            // Validate message length and show appropriate message
            String lengthValidation = Message.validateMessageLength(content);
            JOptionPane.showMessageDialog(
                null,
                lengthValidation,
                "Message Length Validation",
                lengthValidation.startsWith("Message ready") ? 
                    JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
            );
            
            if (!lengthValidation.startsWith("Message ready")) {
                return false; // Message too long
            }
            
            // Increment message count for this user
            int messageNumber = userMessageCounts.get(sender) + 1;
            
            // Create the message
            Message message = new Message(sender, recipient, content, messageNumber);
            
            // Show message ID was created
            JOptionPane.showMessageDialog(
                null,
                "Message ID generated: " + message.getMessageID(),
                "Message ID",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Show message hash
            JOptionPane.showMessageDialog(
                null,
                "Message Hash: " + message.getMessageHash(),
                "Message Hash Created",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Use the message's sentMessage method to handle user choice
            String result = message.sentMessage();
            
            // If the message was sent or stored, update our counts
            if (message.getStatus().equals("Sent") || message.getStatus().equals("Stored")) {
                userMessages.get(sender).add(message);
                userMessageCounts.put(sender, messageNumber);
                
                // If it was sent, show the total messages sent
                if (message.getStatus().equals("Sent")) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Total messages sent: " + Message.returnTotalMessages(),
                        "Message Count",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
                
                return true;
            }
            
            return false;
            
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                null,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }
    
    /**
     * Display the messages for a specific user
     * @param username The username whose messages to display
     */
    public void displayMessages(String username) {
        List<Message> messages = userMessages.get(username);
        
        if (messages == null || messages.isEmpty()) {
            JOptionPane.showMessageDialog(
                null,
                "No messages found.",
                "Messages",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        // Create a formatted string with all messages
        StringBuilder messageList = new StringBuilder("Your Messages:\n\n");
        
        for (Message msg : messages) {
            messageList.append("ID: ").append(msg.getMessageID())
                      .append(" | Hash: ").append(msg.getMessageHash())
                      .append(" | Status: ").append(msg.getStatus())
                      .append("\nTo: ").append(msg.getRecipient())
                      .append("\nMessage: ").append(msg.getMessageContent())
                      .append("\n\n");
        }
        
        // Add total messages count
        messageList.append("Total messages sent: ").append(Message.returnTotalMessages());
        
        // Show in a scrollable dialog
        JOptionPane.showMessageDialog(
            null,
            messageList.toString(),
            "Messages for " + username,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}