package by.it.group551002.bohonmaxim.lesson03;

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
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(inputStream);
        Integer count = scanner.nextInt(); // k - количество букв
        Integer length = scanner.nextInt(); // l - длина строки
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        //тут запишите ваше решение

        // Используем карту для хранения соответствия "код -> символ"
        Map<String, Character> huffmanMap = new HashMap<>();

        for (int i = 0; i < count; i++) {
            String letterPart = scanner.next(); // Считает "a:"
            char letter = letterPart.charAt(0); // Берем сам символ
            String code = scanner.next();       // Считает "0" (сам код)
            huffmanMap.put(code, letter);
        }

        // Считываем саму закодированную строку
        String encodedString = scanner.next();
        StringBuilder currentBits = new StringBuilder();

        // Проходим по строке бит за битом
        for (int i = 0; i < encodedString.length(); i++) {
            currentBits.append(encodedString.charAt(i));
            // Если накопленная последовательность бит есть в нашей карте
            if (huffmanMap.containsKey(currentBits.toString())) {
                result.append(huffmanMap.get(currentBits.toString()));
                currentBits.setLength(0); // Очищаем буфер для поиска следующего кода
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString(); //01001100100111
    }
}
