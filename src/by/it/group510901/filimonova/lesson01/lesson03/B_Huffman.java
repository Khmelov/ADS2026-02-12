package by.it.group510901.filimonova.lesson01.lesson03;

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
        int count = scanner.nextInt();
        int length = scanner.nextInt();
        scanner.nextLine(); // переходим на следующую строку после чисел

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1

        // Создаем map для быстрого поиска символа по коду
        Map<String, Character> codeToChar = new HashMap<>();

        // Читаем коды символов
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

        // Декодируем строку, проходя по битам
        StringBuilder currentCode = new StringBuilder();
        for (int i = 0; i < encoded.length(); i++) {
            currentCode.append(encoded.charAt(i));

            // Если нашли код в map, значит это целый символ
            if (codeToChar.containsKey(currentCode.toString())) {
                result.append(codeToChar.get(currentCode.toString()));
                currentCode.setLength(0); // очищаем для следующего символа
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString();
    }
}
/*1. Создаем обратное отображение: Map<код, символ>
   - "0" → 'a'
   - "10" → 'b'
   - "110" → 'c'
   - "111" → 'd'
2. Проходим по закодированной строке побитово, накапливая биты в currentCode
3. Как только currentCode находится в map → добавляем символ в результат и очищаем currentCode
4. Повторяем до конца строки*/