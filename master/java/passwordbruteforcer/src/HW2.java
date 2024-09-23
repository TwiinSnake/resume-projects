/*
    This program takes in an encrypted string and brute force decrypts through recursion based on the maximum password
    length given in the same file.
 */


// Import utilities to be able to scan through input files.
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class HW2 {
    // Linked list head and an array containing the alphabet.
    public static password headPassword = null;
    public static char ALPHABET[] = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u'
            ,'v','w','x','y','z'};


    // Node for password linked list, to attach decrypted passwords to their encrypted counterparts.
    public static class password {
        public String encrypted;
        public String decrypted;
        public password next;

        public password (String encrypted, String decrypted, password next) {
            this.encrypted = encrypted;
            this.decrypted = decrypted;
            this.next = next;
        }
    }


    public static void main(String[] args) {
        // Initialize the file from command line input...
        File file = new File(args[0]);
        try {
            // ... and its scanner.
            Scanner scanner = new Scanner(file);
            // Keep the password size from the beginning of the file in a variable we can access later.
            int maxPassSize = Integer.parseInt(scanner.nextLine());
            // Create a temporary password node to use as a pointer later.
            password tempPassword = headPassword;
            // Start with writing each encrypted password into the linked list, in order.
            while (scanner.hasNextLine()) {
                // If there is no head, make the first password the head. Make the password pointer the new head.
                if (headPassword == null) {
                    headPassword = new password(scanner.nextLine().trim(), "", null);
                    tempPassword = headPassword;
                }
                // Otherwise, write the next password into the end of the linked list, and move the pointer to the new tail.
                else {
                    tempPassword.next = new password(scanner.nextLine().trim(), "", null);
                    tempPassword = tempPassword.next;
                }
            }
            // Start the recurring decrypt method.
            decrypt(maxPassSize, "");
            // Move the password pointer back to the head, and start writing out each decrypted password.
            tempPassword = headPassword;
            while (tempPassword != null) {
                System.out.println(tempPassword.decrypted);
                tempPassword = tempPassword.next;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
        This method will iterate through each possible string of characters and determine of the encrypted version of
        that string matches any of the strings already in the linked list.
    */
    public static void decrypt(int maxLetters, String testPassword) {
        // If the current string length isn't empty (It starts out as such)
        if (testPassword.length() > 0) {
            // Move the pointer to the head and create an encrypted version of the test password.
            password tempPassword = headPassword;
            String encryptedTest = HW2crypto.encrypt(testPassword);
            // Compare the test password to the linked list. If it matches anything, set the .decrypted to the test.
            while (tempPassword != null) {
                if (encryptedTest.equals(tempPassword.encrypted)) {
                    tempPassword.decrypted = testPassword;
                }
                tempPassword = tempPassword.next;
            }
        }
        // If there are no more strings to try, end the recursion.
        if (maxLetters == 0) {
            return;
        }
        // For every letter in the alphabet, recurse and test another password.
        for (char letter: ALPHABET) {
            decrypt(maxLetters - 1, testPassword + letter);
        }
        // No matter what, after brute forcing every password, stop the function.
        return;
    }
}