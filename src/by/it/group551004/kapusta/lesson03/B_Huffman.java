package by.it.group551004.kapusta.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Lesson 3. B_Huffman.
// Восстановите строку по её коду и беспрефиксному коду символов.

public class B_Huffman {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = B_Huffman.class.getResourceAsStream("dataB.txt");
        B_Huffman instance = new B_Huffman();
        String result = instance.decode(inputStream);
        System.out.println(result);
    }

    String decode(InputStream inputStream) throws FileNotFoundException {
        StringBuilder result = new StringBuilder();

        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(inputStream);
        Integer count = scanner.nextInt();   // количество символов
        Integer length = scanner.nextInt();  // длина закодированной строки
        scanner.nextLine(); // перейти на следующую строку

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        // 1. Считываем словарь: code -> letter
        Map<String, Character> decodeMap = new HashMap<>();

        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine();   // например: "a: 0"
            String[] parts = line.split(": ");
            char letter = parts[0].charAt(0);
            String code = parts[1];
            decodeMap.put(code, letter);
        }

        // 2. Считываем закодированную строку
        String encoded = scanner.nextLine();

        // 3. Декодируем
        StringBuilder current = new StringBuilder();

        for (char bit : encoded.toCharArray()) {
            current.append(bit);

            // если текущая последовательность — готовый код
            if (decodeMap.containsKey(current.toString())) {
                result.append(decodeMap.get(current.toString()));
                current.setLength(0); // очищаем буфер
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        return result.toString();
    }
}
