package by.it.group551002.loiko.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

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

        // Пропускаем пустую строку после чисел
        scanner.nextLine();

        // Создаём карту: код символа → сам символ
        // Например: "0" → 'a', "10" → 'b', "110" → 'c', "111" → 'd'
        Map<String, Character> codeToChar = new HashMap<>();

        // Читаем k строк с кодами символов
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine();
            // Формат: "a: 0" или "b: 10"
            String[] parts = line.split(": ");
            char symbol = parts[0].charAt(0);
            String code = parts[1];
            codeToChar.put(code, symbol);
        }

        // Читаем закодированную строку
        String encoded = scanner.nextLine();

        // Декодируем: идём по строке и собираем код, пока он не совпадёт с одним из известных
        String currentCode = "";
        for (int i = 0; i < encoded.length(); i++) {
            currentCode += encoded.charAt(i);

            if (codeToChar.containsKey(currentCode)) {
                // Нашли символ по коду
                result.append(codeToChar.get(currentCode));
                currentCode = ""; // сбрасываем для следующего символа
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString(); //01001100100111
    }
}