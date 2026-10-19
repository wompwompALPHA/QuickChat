package za.co.quickchat;

import java.util.UUID;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String status;

    // Required for JSON loading
    public Message() {
    }

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;

        long randomNumber = Math.abs(
                UUID.randomUUID().getMostSignificantBits()
                        % 10_000_000_000L
        );

        this.messageID = String.format("%010d", randomNumber);
        this.status = "Pending";
        this.messageHash = createMessageHash();
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.matches("[0-9]{10}");
    }

    public boolean checkRecipientCell() {
        return recipient != null
                && recipient.matches("^\\+[1-9][0-9]{7,14}$");
    }

    public boolean checkMessageLength() {
        return messageText != null && messageText.length() <= 250;
    }

    public String createMessageHash() {
        String firstWord = "";
        String lastWord = "";

        if (messageText != null && !messageText.trim().isEmpty()) {
            String cleaned = messageText.trim()
                    .replaceAll("[^a-zA-Z0-9\\s]", "");

            String[] words = cleaned.trim().split("\\s+");

            if (words.length > 0 && !words[0].isEmpty()) {
                firstWord = words[0].toUpperCase();
                lastWord = words[words.length - 1].toUpperCase();
            }
        }

        String idPrefix = messageID == null
                ? "00" : messageID.substring(0, 2);

        messageHash = idPrefix + ":" + messageNumber + ":"
                + firstWord + lastWord;

        return messageHash;
    }

    public String SentMessage() {
        if (!checkMessageID()
                || !checkRecipientCell()
                || !checkMessageLength()) {
            return "Message could not be sent. Check the recipient and message.";
        }

        status = "Sent";
        return "Message successfully sent";
    }

    public void discardMessage() {
        status = "Disregarded";
    }

    public void storeMessage() {
        if (checkMessageID() && checkRecipientCell()
                && checkMessageLength()) {
            status = "Stored";
        }
    }

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

    public String getStatus() {
        return status;
    }
}
