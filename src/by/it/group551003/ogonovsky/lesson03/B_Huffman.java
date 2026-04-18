package by.it.group551003.ogonovsky.lesson03;

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
        scanner.nextLine();
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1

        // Словарь: код -> буква
        Map<String, Character> codes = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine();
            // формат "letter: code"
            int colonIdx = line.indexOf(':');
            char letter = line.charAt(0);
            String code = line.substring(colonIdx + 1).trim();
            codes.put(code, letter);
        }

        // Закодированная строка
        String encoded = "";
        if (scanner.hasNextLine()) {
            encoded = scanner.nextLine().trim();
        }

        // Декодируем: идём по битам, накапливаем текущий префикс,
        // как только он совпадает с каким-либо кодом — добавляем букву и сбрасываем.
        // Так как коды беспрефиксные, совпадение однозначно.
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < encoded.length(); i++) {
            current.append(encoded.charAt(i));
            Character letter = codes.get(current.toString());
            if (letter != null) {
                result.append(letter);
                current.setLength(0);
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString();
    }
}