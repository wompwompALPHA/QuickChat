package za.co.quickchat;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuickChatApp {

    private final Scanner scanner = new Scanner(System.in);
    private final Login login = new Login();
    private final MessageStorage storage = new MessageStorage();
    private final List<Message> messages = new ArrayList<>();

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private int nextMessageNumber = 0;

    public static void main(String[] args) {
        QuickChatApp app = new QuickChatApp();
        app.start();
    }

    public void start() {
        System.out.println("Welcome to QuickChat.");

        loadMessages();

        System.out.print("Enter first name: ");
        firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        lastName = scanner.nextLine();

        boolean registered = false;

        while (!registered) {
            System.out.println("\n--- Registration ---");

            System.out.print("Create username (maximum 5 characters, including _): ");
            username = scanner.nextLine();

            System.out.print("Create password: ");
            password = scanner.nextLine();

            System.out.print("Enter phone number with international code: ");
            String phone = scanner.nextLine();

            String result = login.registerUser(username, password, phone);
            System.out.println(result);

            registered = result.equals("User registered successfully.");

            if (!registered) {
                System.out.println("Please try registration again.");
            }
        }

        if (loginUser()) {
            showMenu();
        } else {
            System.out.println("Login failed. Goodbye.");
        }
    }

    private boolean loginUser() {
        System.out.println("\n--- Login ---");

        System.out.print("Username: ");
        String enteredUsername = scanner.nextLine();

        System.out.print("Password: ");
        String enteredPassword = scanner.nextLine();

        boolean success = login.loginUser(
                username, password, enteredUsername, enteredPassword
        );

        System.out.println(
                login.returnLoginStatus(success, firstName, lastName)
        );

        return success;
    }

    private void showMenu() {
        String choice = "";

        while (!choice.equals("8")) {
            System.out.println("\n--- QuickChat Menu ---");
            System.out.println("1. Send a message");
            System.out.println("2. View sent messages");
            System.out.println("3. View stored messages");
            System.out.println("4. Search by message ID");
            System.out.println("5. Search by recipient");
            System.out.println("6. Delete stored message by hash");
            System.out.println("7. Display message report");
            System.out.println("8. Quit");
            System.out.print("Choose an option: ");

            choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    sendMessages();
                    break;
                case "2":
                    displayByStatus("Sent");
                    break;
                case "3":
                    displayByStatus("Stored");
                    break;
                case "4":
                    searchByID();
                    break;
                case "5":
                    searchByRecipient();
                    break;
                case "6":
                    deleteByHash();
                    break;
                case "7":
                    displayReport();
                    break;
                case "8":
                    System.out.println("Thank you for using QuickChat.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void sendMessages() {
        System.out.print("How many messages would you like to create? ");

        int amount;

        try {
            amount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException error) {
            System.out.println("Please enter a valid number.");
            return;
        }

        if (amount < 1) {
            System.out.println("Enter at least one message.");
            return;
        }

        for (int i = 0; i < amount; i++) {
            System.out.print("Recipient number with country code: ");
            String recipient = scanner.nextLine();

            System.out.print("Message (maximum 250 characters): ");
            String text = scanner.nextLine();

            if (text.length() > 250) {
                System.out.println("Message is too long.");
                continue;
            }

            Message message = new Message(
                    nextMessageNumber, recipient, text
            );

            if (!message.checkRecipientCell()) {
                System.out.println("Invalid recipient number.");
                continue;
            }

            System.out.println("1. Send");
            System.out.println("2. Discard");
            System.out.println("3. Store");
            System.out.print("Choose an action: ");

            String action = scanner.nextLine();

            if (action.equals("1")) {
                System.out.println(message.SentMessage());

                if ("Sent".equals(message.getStatus())) {
                    messages.add(message);
                    nextMessageNumber++;
                    saveMessages();
                }
            } else if (action.equals("2")) {
                message.discardMessage();
                messages.add(message);
                nextMessageNumber++;
                saveMessages();
                System.out.println("Message discarded.");
            } else if (action.equals("3")) {
                message.storeMessage();
                messages.add(message);
                nextMessageNumber++;
                saveMessages();
                System.out.println("Message successfully stored.");
            } else {
                System.out.println("Invalid action.");
            }
        }
    }

    private void displayByStatus(String status) {
        boolean found = false;

        for (Message message : messages) {
            if (status.equals(message.getStatus())) {
                printMessage(message);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No " + status.toLowerCase() + " messages found.");
        }
    }

    private void printMessage(Message message) {
        System.out.println("\nMessage ID: " + message.getMessageID());
        System.out.println("Message Hash: " + message.getMessageHash());
        System.out.println("Recipient: " + message.getRecipient());
        System.out.println("Message: " + message.getMessageText());
        System.out.println("Status: " + message.getStatus());
    }

    private void searchByID() {
        System.out.print("Enter message ID: ");
        String id = scanner.nextLine();

        for (Message message : messages) {
            if (message.getMessageID().equals(id)) {
                printMessage(message);
                return;
            }
        }

        System.out.println("Message not found.");
    }

    private void searchByRecipient() {
        System.out.print("Enter recipient number: ");
        String recipient = scanner.nextLine();
        boolean found = false;

        for (Message message : messages) {
            if (message.getRecipient().equals(recipient)) {
                printMessage(message);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No messages found for that recipient.");
        }
    }

    private void deleteByHash() {
        System.out.print("Enter message hash: ");
        String hash = scanner.nextLine();

        for (int i = 0; i < messages.size(); i++) {
            Message message = messages.get(i);

            if (message.getMessageHash().equalsIgnoreCase(hash)
                    && "Stored".equals(message.getStatus())) {
                messages.remove(i);
                saveMessages();
                System.out.println("Stored message deleted.");
                return;
            }
        }

        System.out.println("Stored message not found.");
    }

    private void displayReport() {
        if (messages.isEmpty()) {
            System.out.println("No messages available.");
            return;
        }

        Message longest = null;

        for (Message message : messages) {
            printMessage(message);

            if ("Stored".equals(message.getStatus())
                    && (longest == null
                    || message.getMessageText().length()
                    > longest.getMessageText().length())) {
                longest = message;
            }
        }

        if (longest != null) {
            System.out.println("\nLongest stored message:");
            printMessage(longest);
        }

        System.out.println("Total messages sent: " + returnTotalMessages());
    }

    public int returnTotalMessages() {
        int total = 0;

        for (Message message : messages) {
            if ("Sent".equals(message.getStatus())) {
                total++;
            }
        }

        return total;
    }

    private void saveMessages() {
        try {
            storage.saveMessages(messages);
        } catch (Exception error) {
            System.out.println("Saving failed: " + error.getMessage());
        }
    }

    private void loadMessages() {
        try {
            messages.addAll(storage.loadMessages());

            for (Message message : messages) {
                nextMessageNumber = Math.max(
                        nextMessageNumber,
                        message.getMessageNumber() + 1
                );
            }
        } catch (Exception error) {
            System.out.println("Loading failed: " + error.getMessage());
        }
    }
}
