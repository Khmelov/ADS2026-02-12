package by.it.group551001.telipko.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

// Lesson 3. B_Huffman.
// Восстановите строку по её коду и беспрефиксному коду символов.

// В первой строке входного файла заданы два целых числа
// kk и ll через пробел — количество различных букв, встречающихся в строке,
// и размер получившейся закодированной строки, соответственно.
//
// В следующих kk строках записаны коды букв в формате "letter: code".
// Ни один код не является префиксом другого.
// Буквы могут быть перечислены в любом порядке.
// В качестве букв могут встречаться лишь строчные буквы латинского алфавита;
// каждая из этих букв встречается в строке хотя бы один раз.
// Наконец, в последней строке записана закодированная строка.
// Исходная строка и коды всех букв непусты.
// Заданный код таков, что закодированная строка имеет минимальный возможный размер.
//
//        Sample Input 1:
//        1 1
//        a: 0
//        0

//        Sample Output 1:
//        a


//        Sample Input 2:
//        4 14
//        a: 0
//        b: 10
//        c: 110
//        d: 111
//        01001100100111

//        Sample Output 2:
//        abacabad

public class B_Huffman {

    static private final Map<String, Character> codes = new TreeMap<>();

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
        int count = scanner.nextInt();
        int length = scanner.nextInt();
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        //тут запишите ваше решение
        scanner.nextLine();
        for (int i = 0; i < count; i++) {
            String[] pair = scanner.nextLine().split(": ", 2);
            codes.put(pair[1], pair[0].charAt(0));
        }


        String line = scanner.nextLine();
        Node root = new Node();

        codes.entrySet().iterator().forEachRemaining(
                entry ->
                {
                    String code = entry.getKey();
                    Character val = entry.getValue();
                    Node current = root;

                    for (char ch : code.toCharArray()) {
                        if (ch == '0') {
                            if (current.left == null) current.left = new Node();
                            current = current.left;
                        } else {
                            if (current.right == null) current.right = new Node();
                            current = current.right;
                        }
                    }
                    current.value = val;
                }

        );
        Node current = root;
        for (int i = 0; i < length; i++) {
            char ch = line.charAt(i);
            Node next = (ch == '0') ? current.left : current.right;

            if (next == null) {
                current = root;
                continue;
            }

            current = next;

            if (current.value != null) {
                result.append(current.value);
                current = root;
            }
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString(); //01001100100111
    }


    static class Node {
        Node left;
        Node right;
        Character value;
    }

}
