package by.it.group551001.chaley.lesson3;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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

        int k = scanner.nextInt();
        int l = scanner.nextInt();
        scanner.nextLine();

        Map<String, Character> codes = new HashMap<>();
        for (int i = 0; i < k; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(": ");
            char letter = parts[0].charAt(0);
            String code = parts[1];
            codes.put(code, letter);
        }

        String encodedString = scanner.next();

        StringBuilder currentPrefix = new StringBuilder();
        for (int i = 0; i < encodedString.length(); i++) {
            currentPrefix.append(encodedString.charAt(i));

            if (codes.containsKey(currentPrefix.toString())) {
                result.append(codes.get(currentPrefix.toString()));
                currentPrefix.setLength(0);
            }
        }

        return result.toString();
    }
}
