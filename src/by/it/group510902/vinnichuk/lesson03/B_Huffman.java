package by.it.group510902.vinnichuk.lesson03;

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
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(inputStream);
        Integer count = scanner.nextInt();
        Integer length = scanner.nextInt();
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        //тут запишите ваше решение
        // 1. Создаем карту (Map) для хранения кодов.
        // Ключом будет сам двоичный код (String), а значением — соответствующий символ (Character).
        // Это позволит  мгновенно находить букву, когда соберется её код.
        java.util.Map<String, Character> codes = new java.util.HashMap<>();

        // Пропускаем остаток строки после чтения чисел, чтобы перейти к кодам букв
        scanner.nextLine();

        // Читаем k строк с кодами букв
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine(); // читаем строку вида "a: 0"
            // Разбиваем строку по ": " (двоеточие с пробелом)
            String[] parts = line.split(": ");
            char letter = parts[0].charAt(0);
            String code = parts[1];
            codes.put(code, letter); // Кладем в карту: код -> буква
        }

        // Читаем закодированную строку
        String encodedString = scanner.next();
        // Декодируем строку, проходя по ней слева направо
        // Так как коды префиксные (ни один код не является префиксом другого),
        // мы можем просто накапливать биты, пока не найдем совпадение в словаре

        StringBuilder tempCode = new StringBuilder(); // текущий накапливаемый код

        for (int i = 0; i < encodedString.length(); i++) {
            // Добавляем текущий символ к накапливаемому коду
            tempCode.append(encodedString.charAt(i)); // Добавляем бит (0 или 1)

            // Проверяем, есть ли такой код в нашей карте
            if (codes.containsKey(tempCode.toString())) {
                // Если есть — добавляем найденную букву в итоговый результат
                result.append(codes.get(tempCode.toString()));
                // Очищаем временный накопитель для поиска следующей буквы
                tempCode.setLength(0);
            }
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString(); //01001100100111
    }
}



