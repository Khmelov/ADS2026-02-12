package by.it.group551001.Barusevich.lesson01.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
        Scanner scanner = new Scanner(inputStream);
        Integer count = scanner.nextInt();
        Integer length = scanner.nextInt();
        scanner.nextLine(); // перейти на следующую строку после чисел

        // Корень дерева
        Node root = new Node();

        // Читаем k строк с кодами и строим дерево
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine();
            // Формат: "a: 0"
            String[] parts = line.split(": ");
            char symbol = parts[0].charAt(0);
            String code = parts[1];
            Node current = root;
            for (char bit : code.toCharArray()) {
                if (bit == '0') {
                    if (current.left == null) current.left = new Node();
                    current = current.left;
                } else {
                    if (current.right == null) current.right = new Node();
                    current = current.right;
                }
            }
            current.symbol = symbol;
        }

        // Последняя строка – закодированная строка
        String encoded = scanner.nextLine();
        Node current = root;
        for (char bit : encoded.toCharArray()) {
            if (bit == '0') {
                current = current.left;
            } else {
                current = current.right;
            }
            if (current.symbol != '\0') {
                result.append(current.symbol);
                current = root;
            }
        }
        return result.toString();
    }

    // Вспомогательный класс для узла дерева
    private static class Node {
        Node left;
        Node right;
        char symbol = '\0'; // '\0' означает, что узел не является листом
    }
}