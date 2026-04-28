package by.it.group551004.velichko.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
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

        // Массивы для кодов и символов (букв не более 26)
        String[] codes = new String[count];
        char[] symbols = new char[count];

        // Читаем коды в формате "a: 0"
        for (int i = 0; i < count; i++) {
            String letter = scanner.next().replace(":", ""); // "a:" → "a"
            codes[i] = scanner.next();                        // "0"
            symbols[i] = letter.charAt(0);                    // 'a'
        }

        // Читаем закодированную строку
        String encoded = scanner.next();

        // Декодируем: идём по строке и ищем подходящий код
        int pos = 0;
        while (pos < encoded.length()) {
            for (int i = 0; i < count; i++) {
                // Проверяем, начинается ли подстрока с текущего кода
                if (encoded.startsWith(codes[i], pos)) {
                    result.append(symbols[i]);      // нашли символ
                    pos += codes[i].length();       // прыгаем на длину кода
                    break;                          // переходим к следующему символу
                }
            }
        }

        return result.toString();
    }


}
