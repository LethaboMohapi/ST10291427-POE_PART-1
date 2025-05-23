/*
Module name: Programming 1A
Module code: PROG5121/p/w
Assessment type: Portfolio of Evidence (POE)- PART 2
Student Name: Lethabo
Student surname: Mohapi
Student username: ST10291427@rcconnect.edu.za
Student number: ST10291427

Part 2 Chat Messaging Feature - Enhanced
Message.java - Represents a single message in the chat application
*/
package chatapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

public class Message {
    private String messageID;       // 10-digit random number
    private int messageNumber;      // Auto-incremented message number
    private String recipient;       // Phone number of recipient (+27 format)
    private String messageContent;  // Message content (max 250 chars)
    private String messageHash;     // Auto-generated hash based on requirements
    private String sender;          // Username of the message sender
    private String status;          // Status: "Sent", "Stored", "Discarded"
    
    // Static collection to store all messages sent during runtime
    private static List<Message> allMessages = new ArrayList<>();
    // Static counter for total messages sent
    private static int totalMessagesSent = 0;
    
    /**
     * Constructor for a new message
     * @param sender Username of the sender
     * @param recipient Phone number of the recipient
     * @param messageContent Content of the message
     * @param messageNumber The number of this message (auto-incremented)
     * @throws IllegalArgumentException if validation fails
     */
    public Message(String sender, String recipient, String messageContent, int messageNumber) 
                  throws IllegalArgumentException {
        // Generate message ID first
        this.messageID = generateMessageID();
        
        // Validate message ID
        if (!checkMessageID()) {
            throw new IllegalArgumentException("Message ID must not exceed 10 characters.");
        }
        
        // Set sender and message number
        this.sender = sender;
        this.messageNumber = messageNumber;
        
        // Set and validate recipient
        this.recipient = recipient;
        if (checkRecipientCell() != 1) {
            throw new IllegalArgumentException("Recipient phone number must be in South African format (+27 followed by 9 digits)");
        }
        
        // Validate message content
        if (messageContent.length() > 250) {
            int excess = messageContent.length() - 250;
            throw new IllegalArgumentException("Message exceeds 250 characters by " + excess + ", please reduce size.");
        }
        
        this.messageContent = messageContent;
        this.messageHash = createMessageHash();
        this.status = "Pending"; // Default status
    }
    
    /**
     * Checks if the message ID is valid (not more than 10 characters)
     * @return true if the message ID is valid, false otherwise
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }
    
    /**
     * Checks if the recipient cell number is valid
     * @return 1 if valid, 0 if invalid
     */
    public int checkRecipientCell() {
        String regex = "^\\+27\\d{9}$";
        return (recipient != null && recipient.matches(regex)) ? 1 : 0;
    }
    
    /**
     * Generates a random 10-digit number for message ID
     * @return A 10-digit string
     */
    private String generateMessageID() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        
        // Generate 10 random digits
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        
        return sb.toString();
    }
    
    /**
     * Generates a message hash according to specifications:
     * First two numbers of message ID, colon, message number, colon, first and last words of the message
     * @return The message hash in uppercase
     */
    public String createMessageHash() {
        // Get first two digits of message ID
        String idPrefix = messageID.substring(0, 2);
        
        // Get first and last words of the message
        String[] words = messageContent.split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
        
        // Combine according to pattern: 00:0:HITHANKS
        String hash = idPrefix + ":" + messageNumber + ":" + firstWord + lastWord;
        
        return hash.toUpperCase();
    }
    
    /**
     * Allows user to decide what to do with the message
     * @return String indicating the action taken with the message
     */
    public String sentMessage() {
        String[] options = {"Send Message", "Store Message", "Discard Message"};
        int choice = JOptionPane.showOptionDialog(
            null,
            "What would you like to do with this message?",
            "Message Options",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        switch (choice) {
            case 0: // Send Message
                this.status = "Sent";
                allMessages.add(this);
                totalMessagesSent++;
                storeMessage();
                // Display full message details as required
                displayMessageDetails();
                return "Message sent successfully.";
                
            case 1: // Store Message
                this.status = "Stored";
                allMessages.add(this);
                storeMessage();
                return "Message stored for later.";
                
            case 2: // Discard Message
                this.status = "Discarded";
                return "Message discarded.";
                
            default: // Window closed or cancel
                return "Message action cancelled.";
        }
    }
    
    /**
     * Displays the full details of the message after sending
     */
    private void displayMessageDetails() {
        String details = 
            "MessageID: " + messageID + "\n" +
            "Message Hash: " + messageHash + "\n" +
            "Recipient: " + recipient + "\n" +
            "Message: " + messageContent;
            
        JOptionPane.showMessageDialog(
            null,
            details,
            "Message Details",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Returns a list of all messages sent while the program is running
     * @return A formatted string containing all messages
     */
    public static String printMessages() {
        if (allMessages.isEmpty()) {
            return "No messages have been sent.";
        }
        
        StringBuilder messageList = new StringBuilder("All Messages:\n\n");
        
        for (int i = 0; i < allMessages.size(); i++) {
            Message msg = allMessages.get(i);
            messageList.append("Message #").append(i + 1).append("\n")
                      .append("ID: ").append(msg.getMessageID()).append("\n")
                      .append("Hash: ").append(msg.getMessageHash()).append("\n")
                      .append("Sender: ").append(msg.getSender()).append("\n")
                      .append("To: ").append(msg.getRecipient()).append("\n")
                      .append("Content: ").append(msg.getMessageContent()).append("\n")
                      .append("Status: ").append(msg.getStatus()).append("\n\n");
        }
        
        messageList.append("Total Messages: ").append(returnTotalMessages());
        
        return messageList.toString();
    }
    
    /**
     * Returns the total number of messages sent
     * @return The total number of sent messages
     */
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }
    
    /**
     * Stores the message in JSON format
     */
    @SuppressWarnings("unchecked")
    public void storeMessage() {
        try {
            // Create JSON object for message
            JSONObject messageJson = new JSONObject();
            messageJson.put("messageID", messageID);
            messageJson.put("messageNumber", messageNumber);
            messageJson.put("sender", sender);
            messageJson.put("recipient", recipient);
            messageJson.put("content", messageContent);
            messageJson.put("hash", messageHash);
            messageJson.put("status", status);
            
            // Write to file named after the message ID
            String filename = "message_" + messageID + ".json";
            try (FileWriter file = new FileWriter(filename)) {
                file.write(messageJson.toJSONString());
                file.flush();
            }
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                null,
                "Error saving message to file: " + e.getMessage(),
                "File Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Validates message content length and returns appropriate message
     * @param content The message content to validate
     * @return Success or failure message based on validation
     */
    public static String validateMessageLength(String content) {
        if (content.length() <= 250) {
            return "Message ready to send";
        } else {
            int excess = content.length() - 250;
            return "Message exceeds 250 characters by " + excess + ", please reduce size.";
        }
    }
    
    /**
     * Validates recipient phone number format and returns appropriate message
     * @param phoneNumber The phone number to validate
     * @return Success or failure message based on validation
     */
    public static String validateRecipientNumber(String phoneNumber) {
        String regex = "^\\+27\\d{9}$";
        if (phoneNumber != null && phoneNumber.matches(regex)) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }
    
    // Getters
    public String getMessageID() {
        return messageID;
    }
    
    public int getMessageNumber() {
        return messageNumber;
    }
    
    public String getRecipient() {
        return recipient;
    }
    
    public String getMessageContent() {
        return messageContent;
    }
    
    public String getMessageHash() {
        return messageHash;
    }
    
    public String getSender() {
        return sender;
    }
    
    public String getStatus() {
        return status;
    }
    
    // Setter for status
    public void setStatus(String status) {
        if (status.equals("Sent") || status.equals("Stored") || status.equals("Discarded")) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Invalid status. Must be 'Sent', 'Stored', or 'Discarded'");
        }
    }
    
    /**
     * Returns a string representation of this message
     * @return A string with message details
     */
    @Override
    public String toString() {
        return "Message ID: " + messageID +
               "\nMessage #: " + messageNumber +
               "\nSender: " + sender +
               "\nRecipient: " + recipient +
               "\nContent: " + messageContent +
               "\nHash: " + messageHash +
               "\nStatus: " + status;
    }
}