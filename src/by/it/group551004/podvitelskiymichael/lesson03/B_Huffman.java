package by.it.group551004.podvitelskiymichael.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class B_Huffman {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = B_Huffman.class.getResourceAsStream("dataB.txt");
        B_Huffman instance = new B_Huffman();
        String result = instance.decode(inputStream);
        System.out.println(result);
    }

    String decode(InputStream inputStream) throws FileNotFoundException {
        StringBuilder result = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);

        int count = scanner.nextInt();
        int length = scanner.nextInt();
        scanner.nextLine();

        Map<String, Character> codes = new HashMap<>();

        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(": ");
            char letter = parts[0].charAt(0);
            String code = parts[1];
            codes.put(code, letter);
        }

        String encoded = scanner.nextLine();

        StringBuilder currentCode = new StringBuilder();

        for (char bit : encoded.toCharArray()) {
            currentCode.append(bit);

            if (codes.containsKey(currentCode.toString())) {
                result.append(codes.get(currentCode.toString()));
                currentCode.setLength(0);
            }
        }

        return result.toString();
    }
}