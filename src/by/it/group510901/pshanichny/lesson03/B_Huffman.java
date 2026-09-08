package by.it.group510901.pshanichny.lesson03;

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
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(inputStream);
        Integer count = scanner.nextInt();
        Integer length = scanner.nextInt();
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        //тут запишите ваше решение
        // 1. Создаем карту для хранения соответствия "код -> символ"
        // Нам удобнее искать символ по коду, а не наоборот
        Map<String, Character> codes = new HashMap<>();

        for (int i = 0; i < count; i++) {
            String line = scanner.next(); // Прочитает "a:"
            char letter = line.charAt(0); // Возьмет сам символ 'a'
            String code = scanner.next(); // Прочитает "0"
            codes.put(code, letter);
        }

        // 2. Читаем саму закодированную строку
        String encodedString = scanner.next();

        // 3. Восстанавливаем оригинал
        StringBuilder currentCode = new StringBuilder();
        for (int i = 0; i < encodedString.length(); i++) {
            // Добавляем текущий бит к временной строке
            currentCode.append(encodedString.charAt(i));

            // Если такой код уже есть в нашей карте — значит, мы нашли целую букву
            if (codes.containsKey(currentCode.toString())) {
                result.append(codes.get(currentCode.toString()));
                // Очищаем временную строку для поиска следующей буквы
                currentCode.setLength(0);
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString(); //01001100100111
    }


}
