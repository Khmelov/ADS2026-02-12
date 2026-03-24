package by.it.group551001.sidorova.lesson03;

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

        // читаем k и l
        int k = scanner.nextInt();
        int l = scanner.nextInt();
        scanner.nextLine(); // переход на следующую строку

        // таблица: код → буква
        Map<String, Character> decodeMap = new HashMap<>();

        // читаем k строк вида "a: 0"
        for (int i = 0; i < k; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(": ");
            char letter = parts[0].charAt(0);
            String code = parts[1];
            decodeMap.put(code, letter);
        }

        // читаем закодированную строку
        String encoded = scanner.nextLine();

        // декодирование
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < encoded.length(); i++) {
            current.append(encoded.charAt(i));
            if (decodeMap.containsKey(current.toString())) {
                result.append(decodeMap.get(current.toString()));
                current.setLength(0); // очистить буфер
            }
        }

        return result.toString();
    }
}
