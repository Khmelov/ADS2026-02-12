package by.it.group551004.alexeenko.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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

        // Читаем таблицу кодов: код -> символ
        Map<String, Character> codes = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String letter = scanner.next(); // "a:"
            char symbol = letter.charAt(0);
            String code = scanner.next();   // "0" / "10" / "110" ...
            codes.put(code, symbol);
        }

        String encoded = scanner.next();

        // Особый случай: один символ, код = "0"
        if (count == 1) {
            char only = codes.values().iterator().next();
            for (int i = 0; i < length; i++) result.append(only);
            return result.toString();
        }

        // Идём по закодированной строке, накапливаем биты до совпадения с кодом
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < encoded.length(); i++) {
            current.append(encoded.charAt(i));
            Character sym = codes.get(current.toString());
            if (sym != null) {
                result.append(sym);
                current.setLength(0);
            }
        }

        return result.toString();
    }


}
