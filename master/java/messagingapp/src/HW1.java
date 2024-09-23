/*
        Organizes and displays a series of messages from a range of contacts. Keeps track of when messages are sent,
        even if they're sent out of order, and displays chat logs in the correct order.
 */


// Import utilities to be able to scan through input files.
import java.io.File;
import java.util.Scanner;


public class HW1 {
    // Initialize the linked lists for each of the contacts.
    static Message headAlice = null;
    static Message headBob = null;
    static Message headCarol = null;
    static int sizeAlice = 0;
    static int sizeBob = 0;
    static int sizeCarol = 0;


    // Class for storing all the data surrounding a message: the name of who sent it, the timestamp, and the message.
    // Also contains a pointer to the next message in the contact's conversation.
    public static class Message {
        String name;
        int timeStamp;
        String message;
        Message next = null;

        // Method to write a new message for placement into a linked list.
        public Message (int timeStamp, String name, String message) {
            this.name = name;
            this.timeStamp = timeStamp;
            this.message = message;
        }
    }


    public static void main(String[] args) {
        // Initialize the file from command line input, and start scanning through it.
        File file = new File(args[0]);
        try {
            // Based on the first string on each line, executes a different method.
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                switch (scanner.next()) {
                    case "ReceiveMessage": ReceiveMessage(scanner.nextInt(), scanner.next(), scanner.nextLine());
                        break;
                    case "OpenApp": OpenApp();
                        break;
                    case "DisplayConversation": DisplayConversation(scanner.next());
                        break;
                    case "SendMessage": SendMessage(scanner.nextInt(), scanner.next(), scanner.nextLine());
                        break;
                    case "DeleteMessage": DeleteMessage(scanner.nextInt(), scanner.next());
                        break;
                    case "CloseApp": System.out.printf("CloseApp\n");
                        break;
                    default: System.out.printf("Invalid command.\n");
                        break;
                }
            }
        // If the scanner encounters an error reading the file, the exception will be caught.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Saves the message into their linked list and alerts the user.
    public static void ReceiveMessage(int timeStamp, String contact, String message) {
        // Saves the new message as a newMessage, and a tempMessage that can parse through the linked list.
        Message newMessage = new Message(timeStamp, contact, message);
        Message tempMessage;
        switch (contact) {
            case "Alice":
                // Beginning at the head of the linked list...
                tempMessage = headAlice;
                // If the linked list is empty...
                if (sizeAlice == 0) {
                    // Set the new message as the head of the list, and increase the size.
                    headAlice = newMessage;
                    sizeAlice++;
                    break;
                // If the linked list has only one message...
                } else if (sizeAlice == 1) {
                    // If the new message was sent after the first message...
                    if (timeStamp > tempMessage.timeStamp) {
                        // Save the new message after the head.
                        tempMessage.next = newMessage;
                    } else {
                        // Otherwise, make the new message the new head.
                        newMessage.next = headAlice;
                        headAlice = newMessage;
                    }
                    // Either way, increase the size and break.
                    sizeAlice++;
                    break;
                }
                // For every message in the linked list...
                for (int i = 0; i < sizeAlice; i++) {
                    // If the message we're looking at was sent before the new message...
                    if (timeStamp > tempMessage.timeStamp) {
                        // and the message we're looking at is the tail...
                        if (tempMessage.next == null) {
                            // Make the new message the new tail. Increase size and break.
                            tempMessage.next = newMessage;
                            sizeAlice++;
                            break;
                        }
                        // and the message after the message we're looking at was sent after the new message...
                        if (timeStamp < tempMessage.next.timeStamp) {
                            // Put the new message between the message we're looking at and the one after it.
                            newMessage.next = tempMessage.next;
                            tempMessage.next = newMessage;
                            sizeAlice++;
                            break;
                        }
                    // Or if the new message comes before the earliest message
                    } else if (timeStamp < tempMessage.timeStamp) {
                        // Make the new message the new head.
                        newMessage.next = headAlice;
                        headAlice = newMessage;
                        sizeAlice++;
                        break;
                    }
                    // If none of the above were true, look at the next message and loop again.
                    tempMessage = tempMessage.next;
                }
                break;

            /*

                The next two cases are almost the exact same as the previous case.

            */

            case "Bob":
                tempMessage = headBob;
                if (sizeBob == 0) {
                    headBob = newMessage;
                    sizeBob++;
                    break;
                } else if (sizeBob == 1) {
                    if (timeStamp > tempMessage.timeStamp) {
                        tempMessage.next = newMessage;
                    } else {
                        newMessage.next = headBob;
                        headBob = newMessage;
                    }
                    sizeBob++;
                    break;
                }
                for (int i = 0; i < sizeBob; i++) {
                    if (timeStamp > tempMessage.timeStamp) {
                        if (timeStamp < tempMessage.next.timeStamp) {
                            newMessage.next = tempMessage.next;
                            tempMessage.next = newMessage;
                            sizeBob++;
                            break;
                        }
                        newMessage.next = headBob;
                        headBob = newMessage;
                        sizeBob++;
                        break;
                    } else if (timeStamp < tempMessage.timeStamp) {
                        newMessage.next = headBob;
                        headBob = newMessage;
                        sizeBob++;
                        break;
                    }
                    tempMessage = tempMessage.next;
                }
                break;
            case "Carol":
                tempMessage = headCarol;
                if (sizeCarol == 0) {
                    headCarol = newMessage;
                    sizeCarol++;
                    break;
                } else if (sizeCarol == 1) {
                    if (timeStamp > tempMessage.timeStamp) {
                        tempMessage.next = newMessage;
                    } else {
                        newMessage.next = headCarol;
                        headCarol = newMessage;
                    }
                    sizeCarol++;
                    break;
                }
                for (int i = 0; i < sizeCarol; i++) {
                    if (timeStamp > tempMessage.timeStamp) {
                        if (timeStamp < tempMessage.next.timeStamp) {
                            newMessage.next = tempMessage.next;
                            tempMessage.next = newMessage;
                            sizeCarol++;
                            break;
                        }
                        newMessage.next = headCarol;
                        headCarol = newMessage;
                        sizeCarol++;
                        break;
                    } else if (timeStamp < tempMessage.timeStamp) {
                        newMessage.next = headCarol;
                        headCarol = newMessage;
                        sizeCarol++;
                        break;
                    }
                    tempMessage = tempMessage.next;
                }
                break;
        }
        System.out.printf("NotifyUser %s\n", contact);
    }

    // "Opens" the messaging app and tells the user how many messages they have saved with each contact.
    public static void OpenApp() {
        System.out.printf("OpenApp\nAlice %d\nBob %d\nCarol %d\n", sizeAlice, sizeBob, sizeCarol);
    }

    // Iterates through the contact's conversation linked list and displays each message with their associated
    // timestamp and name.
    public static void DisplayConversation(String contact) {
        System.out.printf("DisplayConversation ");
        Message tempMessage;
        switch (contact) {
            case "Alice":
                System.out.printf("Alice\n");
                tempMessage = headAlice;
                // For every message in Alice's conversation...
                for (int i = 0; i < sizeAlice; i++) {
                    // If the timestamp has less than 6 digits...
                    if (tempMessage.timeStamp < 100000) {
                        // Print the timestamp (with 0 appended), contact, and their message.
                        System.out.printf("0%d %s%s\n", tempMessage.timeStamp, tempMessage.name, tempMessage.message);
                        tempMessage = tempMessage.next;
                    } else {
                        // Otherwise, print the timestamp (as is), contact, and their message.
                        System.out.printf("%d %s%s\n", tempMessage.timeStamp, tempMessage.name, tempMessage.message);
                        tempMessage = tempMessage.next;
                    }
                }
                break;

            /*

                The next two cases are almost the exact same as the previous case.

            */

            case "Bob":
                System.out.printf("Bob\n");
                tempMessage = headBob;
                for (int i = 0; i < sizeBob; i++) {
                    if (tempMessage.timeStamp < 100000) {
                        System.out.printf("0%d %s%s\n", tempMessage.timeStamp, tempMessage.name, tempMessage.message);
                        tempMessage = tempMessage.next;
                    } else {
                        System.out.printf("%d %s%s\n", tempMessage.timeStamp, tempMessage.name, tempMessage.message);
                        tempMessage = tempMessage.next;
                    }
                }
                break;
            case "Carol":
                System.out.printf("Carol\n");
                tempMessage = headCarol;
                for (int i = 0; i < sizeCarol; i++) {
                    if (tempMessage.timeStamp < 100000) {
                        System.out.printf("0%d %s%s\n", tempMessage.timeStamp, tempMessage.name, tempMessage.message);
                        tempMessage = tempMessage.next;
                    } else {
                        System.out.printf("%d %s%s\n", tempMessage.timeStamp, tempMessage.name, tempMessage.message);
                        tempMessage = tempMessage.next;
                    }
                }
                break;
        }
    }

    /*
        ReceiveMessages from a new contact, "me", and inserts it into the contact's conversation at the given
        timestamp. After that, it DisplayConversations including the new message from "me". Refer to
        ReceiveMessage and DisplayConversation for notation.
    */
    public static void SendMessage(int timeStamp, String contact, String message) {
        Message newMessage = new Message(timeStamp, "me", message);
        Message tempMessage;
        switch (contact) {
            case "Alice":
                tempMessage = headAlice;
                if (sizeAlice == 0) {
                    headAlice = newMessage;
                    sizeAlice++;
                    break;
                } else if (sizeAlice == 1) {
                    if (timeStamp > tempMessage.timeStamp) {
                        tempMessage.next = newMessage;
                    } else {
                        newMessage.next = headAlice;
                        headAlice = newMessage;
                    }
                    sizeAlice++;
                    break;
                }
                for (int i = 0; i < sizeAlice; i++) {
                    if (timeStamp > tempMessage.timeStamp) {
                        if (tempMessage.next == null) {
                            tempMessage.next = newMessage;
                            sizeAlice++;
                            break;
                        }
                        if (timeStamp < tempMessage.next.timeStamp) {
                            newMessage.next = tempMessage.next;
                            tempMessage.next = newMessage;
                            sizeAlice++;
                            break;
                        }
                    } else if (timeStamp < tempMessage.timeStamp) {
                        newMessage.next = headAlice;
                        headAlice = newMessage;
                        sizeAlice++;
                        break;
                    }
                    tempMessage = tempMessage.next;
                }
                break;
            case "Bob":
                tempMessage = headBob;
                if (sizeBob == 0) {
                    headBob = newMessage;
                    sizeBob++;
                    break;
                } else if (sizeBob == 1) {
                    if (timeStamp > tempMessage.timeStamp) {
                        tempMessage.next = newMessage;
                    } else {
                        newMessage.next = headBob;
                        headBob = newMessage;
                    }
                    sizeBob++;
                    break;
                }
                for (int i = 0; i < sizeBob; i++) {
                    if (timeStamp > tempMessage.timeStamp) {
                        if (timeStamp < tempMessage.next.timeStamp) {
                            newMessage.next = tempMessage.next;
                            tempMessage.next = newMessage;
                            sizeBob++;
                            break;
                        }
                        newMessage.next = headBob;
                        headBob = newMessage;
                        sizeBob++;
                        break;
                    } else if (timeStamp < tempMessage.timeStamp) {
                        newMessage.next = headBob;
                        headBob = newMessage;
                        sizeBob++;
                        break;
                    }
                    tempMessage = tempMessage.next;
                }
                break;
            case "Carol":
                tempMessage = headCarol;
                if (sizeCarol == 0) {
                    headCarol = newMessage;
                    sizeCarol++;
                    break;
                } else if (sizeCarol == 1) {
                    if (timeStamp > tempMessage.timeStamp) {
                        tempMessage.next = newMessage;
                    } else {
                        newMessage.next = headCarol;
                        headCarol = newMessage;
                    }
                    sizeCarol++;
                    break;
                }
                for (int i = 0; i < sizeCarol; i++) {
                    if (timeStamp > tempMessage.timeStamp) {
                        if (timeStamp < tempMessage.next.timeStamp) {
                            newMessage.next = tempMessage.next;
                            tempMessage.next = newMessage;
                            sizeCarol++;
                            break;
                        }
                        newMessage.next = headCarol;
                        headCarol = newMessage;
                        sizeCarol++;
                        break;
                    } else if (timeStamp < tempMessage.timeStamp) {
                        newMessage.next = headCarol;
                        headCarol = newMessage;
                        sizeCarol++;
                        break;
                    }
                    tempMessage = tempMessage.next;
                }
                break;
        }

        /*
            ^^^^^
            Similar to ReceiveMessage

            Similar to DisplayConversation
            VVVVV
        */

        System.out.printf("SendMessage ");
        switch (contact) {
            case "Alice":
                tempMessage = headAlice;
                if (tempMessage.timeStamp < 100000) {
                    System.out.printf("0%d %s%s\n", timeStamp, tempMessage.name, newMessage.message);
                    tempMessage = tempMessage.next;
                } else {
                    System.out.printf("%d %s%s\n", timeStamp, tempMessage.name, newMessage.message);
                    tempMessage = tempMessage.next;
                }
                tempMessage = headAlice;
                for (int i = 0; i < sizeAlice; i++) {
                    if (tempMessage.timeStamp < 100000) {
                        System.out.printf("0%d %s%s\n", tempMessage.timeStamp, tempMessage.name, tempMessage.message);
                        tempMessage = tempMessage.next;
                    } else {
                        System.out.printf("%d %s%s\n", tempMessage.timeStamp, tempMessage.name, tempMessage.message);
                        tempMessage = tempMessage.next;
                    }
                }
                break;
            case "Bob":
                tempMessage = headBob;
                if (tempMessage.timeStamp < 100000) {
                    System.out.printf("0%d %s%s\n", timeStamp, tempMessage.name, newMessage.message);
                    tempMessage = tempMessage.next;
                } else {
                    System.out.printf("%d %s%s\n", timeStamp, tempMessage.name, newMessage.message);
                    tempMessage = tempMessage.next;
                }
                tempMessage = headBob;
                for (int i = 0; i < sizeBob; i++) {
                    if (tempMessage.timeStamp < 100000) {
                        System.out.printf("0%d %s%s\n", tempMessage.timeStamp, tempMessage.name, tempMessage.message);
                        tempMessage = tempMessage.next;
                    } else {
                        System.out.printf("%d %s%s\n", tempMessage.timeStamp, tempMessage.name, tempMessage.message);
                        tempMessage = tempMessage.next;
                    }
                }
                break;
            case "Carol":
                tempMessage = headCarol;
                if (tempMessage.timeStamp < 100000) {
                    System.out.printf("0%d %s%s\n", timeStamp, tempMessage.name, newMessage.message);
                    tempMessage = tempMessage.next;
                } else {
                    System.out.printf("%d %s%s\n", timeStamp, tempMessage.name, newMessage.message);
                    tempMessage = tempMessage.next;
                }
                tempMessage = headCarol;
                for (int i = 0; i < sizeCarol; i++) {
                    if (tempMessage.timeStamp < 100000) {
                        System.out.printf("0%d %s%s\n", tempMessage.timeStamp, tempMessage.name, tempMessage.message);
                        tempMessage = tempMessage.next;
                    } else {
                        System.out.printf("%d %s%s\n", tempMessage.timeStamp, tempMessage.name, tempMessage.message);
                        tempMessage = tempMessage.next;
                    }
                }
                break;
        }
    }

    // Removes the message with the given timestamp from the given contact's conversation. Then DisplayConversations.
    // Refer to DisplayConversation for notation.
    public static void DeleteMessage(int timeStamp, String contact) {
        // If the time stamp has less than 6 digits
        if (timeStamp < 100000) {
            // Print the timestamp with a 0 in front of it.
            System.out.printf("DeleteMessage 0%d %s\n", timeStamp, contact);
        } else {
            // Otherwise, print it normally.
            System.out.printf("DeleteMessage %d %s\n", timeStamp, contact);
        }
        Message tempMessage;
        switch (contact) {
            case "Alice":
                tempMessage = headAlice;
                // If the list is empty...
                if (sizeAlice == 0) {
                    // Then there are no messages to delete.
                    System.out.printf("No messages to delete.\n");
                    break;
                // Otherwise if there is only one message, then just delete it.
                } else if (sizeAlice == 1) {
                    headAlice = null;
                    sizeAlice--;
                    break;
                }
                // If the message to delete is the head...
                if (timeStamp == tempMessage.timeStamp) {
                    // Then make the next message the new head.
                    headAlice = tempMessage.next;
                    sizeAlice--;
                    break;
                }
                // For every message...
                for (int i = 0; i < sizeAlice; i++) {
                    // As long as the message we're looking at isn't the tail...
                    if (tempMessage.next != null) {
                        // If the next message is the message to delete...
                        if (timeStamp == tempMessage.next.timeStamp) {
                            // Delete the next message by making the current message's next the next-next message.
                            tempMessage.next = tempMessage.next.next;
                            sizeAlice--;
                            break;
                        }
                    // Otherwise, the message we were searching for isn't there.
                    } else {
                        System.out.printf("Message not found.\n");
                        break;
                    }
                    tempMessage = tempMessage.next;
                }
                break;

            /*

                The next two cases are almost the exact same as the previous case.

            */

            case "Bob":
                tempMessage = headBob;
                if (sizeBob == 0) {
                    System.out.printf("No messages to delete.\n");
                    break;
                } else if (sizeBob == 1) {
                    headBob = null;
                    sizeBob--;
                    break;
                }
                if (timeStamp == tempMessage.timeStamp) {
                    headBob = tempMessage.next;
                    sizeBob--;
                    break;
                }
                for (int i = 0; i < sizeBob; i++) {
                    if (tempMessage.next != null) {
                        if (timeStamp == tempMessage.next.timeStamp) {
                            tempMessage.next = tempMessage.next.next;
                            sizeBob--;
                            break;
                        }
                    } else {
                        System.out.printf("Message not found.\n");
                        break;
                    }
                    tempMessage = tempMessage.next;
                }
                break;
            case "Carol":
                tempMessage = headCarol;
                if (sizeCarol == 0) {
                    System.out.printf("No messages to delete.\n");
                    break;
                } else if (sizeCarol == 1) {
                    headCarol = null;
                    sizeCarol--;
                    break;
                }
                if (timeStamp == tempMessage.timeStamp) {
                    headCarol = tempMessage.next;
                    sizeCarol--;
                    break;
                }
                for (int i = 0; i < sizeCarol; i++) {
                    if (tempMessage.next != null) {
                        if (timeStamp == tempMessage.next.timeStamp) {
                            tempMessage.next = tempMessage.next.next;
                            sizeCarol--;
                            break;
                        }
                    } else {
                        System.out.printf("Message not found.\n");
                        break;
                    }
                    tempMessage = tempMessage.next;
                }
                break;
        }

        // Similar to DisplayConversation
        // VVVVV
        switch (contact) {
            case "Alice":
                tempMessage = headAlice;
                for (int i = 0; i < sizeAlice; i++) {
                    if (tempMessage.timeStamp < 100000) {
                        System.out.printf("0%d %s%s\n", tempMessage.timeStamp, tempMessage.name, tempMessage.message);
                        tempMessage = tempMessage.next;
                    } else {
                        System.out.printf("%d %s%s\n", tempMessage.timeStamp, tempMessage.name, tempMessage.message);
                        tempMessage = tempMessage.next;
                    }
                }
                break;
            case "Bob":
                tempMessage = headBob;
                for (int i = 0; i < sizeBob; i++) {
                    if (tempMessage.timeStamp < 100000) {
                        System.out.printf("0%d %s%s\n", tempMessage.timeStamp, tempMessage.name, tempMessage.message);
                        tempMessage = tempMessage.next;
                    } else {
                        System.out.printf("%d %s%s\n", tempMessage.timeStamp, tempMessage.name, tempMessage.message);
                        tempMessage = tempMessage.next;
                    }
                }
                break;
            case "Carol":
                tempMessage = headCarol;
                for (int i = 0; i < sizeCarol; i++) {
                    if (tempMessage.timeStamp < 100000) {
                        System.out.printf("0%d %s%s\n", tempMessage.timeStamp, tempMessage.name, tempMessage.message);
                        tempMessage = tempMessage.next;
                    } else {
                        System.out.printf("%d %s%s\n", tempMessage.timeStamp, tempMessage.name, tempMessage.message);
                        tempMessage = tempMessage.next;
                    }
                }
                break;
        }
    }
}