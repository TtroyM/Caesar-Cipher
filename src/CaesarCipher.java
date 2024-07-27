import java.util.*;

/**
 * This project demonstrates a simple implementation of the Caesar Cipher algorithm in Java.
 *
 * The Caesar Cipher is a type of substitution cipher where each letter in the plaintext is shifted
 * a certain number of places down or up the alphabet. This implementation allows for both encryption
 * and decryption of messages by shifting the characters appropriately.
 *
 * The goal of this project is to showcase proficiency in Java programming by implementing a classic
 * cryptographic technique.
 *
 * @author Troy May
 */

public class CaesarCipher {

    public static String encrypt(String input, int shift) {

        StringBuilder encryptedMessage = new StringBuilder();

        //Loop each character in message
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            //Check for lowercase
            if(Character.isLetter(currentChar)) {
                char base = Character.isLowerCase(currentChar) ? 'a' : 'A';
                currentChar = (char) ((currentChar - base + shift) % 26 + base); //This is the shifting work
            }

            encryptedMessage.append(currentChar); //Add each character to the encrypted message
        }
    return encryptedMessage.toString();
    }

    public static String decrypt(String encryptedMessage, int shift) {
        return encrypt(encryptedMessage, 26 -shift);
    }

    public static void main(String[] args) {
        String newMessage = "Julius, look out for unsuspecting knives!";
        int shift = 3;

        String encryptedMessage = encrypt(newMessage, shift);
        System.out.println("Encrypted Message: " + encryptedMessage);

        String decryptedMessage = decrypt(encryptedMessage, shift);
        System.out.println("Decrypted Message: " + decryptedMessage);
    }
}
