package by.it.group551002.kuzmenia.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

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

        int k = scanner.nextInt(); // k - количество букв в полученной строке
        int l = scanner.nextInt(); // l - количество цифр закодированной строке

        // Создаём хэш-карту, делаем ключом код символа, а значением ключа сам символ
        Map<String, Character> codeMap = new HashMap<>();

        // Считываем k строк с кодами формата "a: 0"
        for (int i = 0; i < k; i++) {
            String letterWithColon = scanner.next(); // Считываем букву ("a:")
            char letter = letterWithColon.charAt(0); // Оставляем только букву
            String code = scanner.next();            // Считываем код буквы
            codeMap.put(code, letter);               // Добавляем в хэш-карту
        }

        // Читаем саму закодированную строку
        String encodedString = scanner.next();

        // Восстанавливаем исходную строку
        StringBuilder currentCode = new StringBuilder();
        for (int i = 0; i < encodedString.length(); i++) {
            currentCode.append(encodedString.charAt(i)); // Добавляем один символ кода

            // Если считанный код есть в хэш-карте, то добавляем букву соответствующую коду
            if (codeMap.containsKey(currentCode.toString())) {
                result.append(codeMap.get(currentCode.toString())); // Добавление буквы в результирующую строку
                currentCode.setLength(0);                           // Очищаем буфер для повторного ввода кода
            }
        }

        // Возвращаем строку
        return result.toString();
    }


}
