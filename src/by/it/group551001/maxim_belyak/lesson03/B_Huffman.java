package by.it.group551001.maxim_belyak.lesson03;

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
        Integer count = scanner.nextInt();
        Integer length = scanner.nextInt();

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        // Build a map: binary code -> character
        Map<String, Character> codeMap = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String letter = scanner.next();          // "a:"
            char ch = letter.charAt(0);              // 'a'
            String code = scanner.next();            // "0" or "10" etc.
            codeMap.put(code, ch);
        }

        String encoded = scanner.next();             // the encoded binary string

        // Special case: only one unique character
        // Its code is "0", so every bit maps to that character
        if (count == 1) {
            char onlyChar = codeMap.values().iterator().next();
            for (int i = 0; i < length; i++) {
                result.append(onlyChar);
            }
        } else {
            // Walk through the encoded string, accumulating bits until we find a match
            StringBuilder current = new StringBuilder();
            for (int i = 0; i < encoded.length(); i++) {
                current.append(encoded.charAt(i));
                Character ch = codeMap.get(current.toString());
                if (ch != null) {
                    result.append(ch);
                    current.setLength(0);  // reset the buffer
                }
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString();
    }
}