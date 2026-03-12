package by.it.group551002.shinkevich.lesson03;

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

		// Создаем словарь для обратного поиска: код -> буква
		Map<String, Character> map = new HashMap<>();

		// Читаем словарь из файла
		for (int i = 0; i < count; i++) {
			String letterWithColon = scanner.next(); // читает, например, "a:"
			char letter = letterWithColon.charAt(0); // забираем саму букву 'a'
			String code = scanner.next();            // читает код, например, "0"
			map.put(code, letter);
		}

		// Читаем саму закодированную строку (последняя строчка в файле)
		String encodedString = scanner.next();

		// Буфер для накопления текущего кода
		StringBuilder tempCode = new StringBuilder();

		// Проходим по закодированной строке
		for (int i = 0; i < length; i++) {
			tempCode.append(encodedString.charAt(i)); // добавляем очередной нолик или единичку

			// Если накопленный код есть в нашем словаре
			if (map.containsKey(tempCode.toString())) {
				// Добавляем раскодированную букву в итоговый результат
				result.append(map.get(tempCode.toString()));
				// Очищаем буфер для поиска следующей буквы
				tempCode.setLength(0);
			}
		}

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString(); //01001100100111
    }


}
