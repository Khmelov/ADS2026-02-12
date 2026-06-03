package by.it.group510901.artykov.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
 * Задача:
 * Восстановить исходную строку
 * по её коду Хаффмана.
 *
 * Дано:
 * 1. Таблица кодов символов
 * 2. Закодированная строка
 *
 * Необходимо:
 * расшифровать строку.
 */

public class B_Huffman {

    public static void main(String[] args)
            throws FileNotFoundException {

        InputStream inputStream =
                B_Huffman.class.getResourceAsStream("dataB.txt");

        B_Huffman instance = new B_Huffman();

        String result = instance.decode(inputStream);

        System.out.println(result);
    }

    /*
     * Метод декодирует строку,
     * закодированную алгоритмом Хаффмана.
     */
    String decode(InputStream inputStream)
            throws FileNotFoundException {

        // Результат декодирования
        StringBuilder result = new StringBuilder();

        // Чтение данных из файла
        Scanner scanner = new Scanner(inputStream);

        // Количество различных символов
        Integer count = scanner.nextInt();

        // Длина закодированной строки
        Integer length = scanner.nextInt();

        /*
         * Карта:
         * код -> символ
         *
         * Например:
         * 0 -> a
         * 10 -> b
         */
        Map<String, Character> codes = new HashMap<>();

        /*
         * Считываем коды символов
         */
        for (int i = 0; i < count; i++) {

            // Символ
            String letter = scanner.next();

            // Удаляем :
            letter = letter.substring(0, 1);

            // Код символа
            String code = scanner.next();

            // Сохраняем:
            // код -> символ
            codes.put(code, letter.charAt(0));
        }

        /*
         * Закодированная строка
         */
        String encoded = scanner.next();

        /*
         * Текущий считываемый код
         */
        StringBuilder currentCode =
                new StringBuilder();

        /*
         * Идём по всем битам строки
         */
        for (int i = 0; i < encoded.length(); i++) {

            // Добавляем очередной бит
            currentCode.append(encoded.charAt(i));

            /*
             * Если такой код найден,
             * значит получили символ
             */
            if (codes.containsKey(currentCode.toString())) {

                // Добавляем символ в ответ
                result.append(
                        codes.get(currentCode.toString())
                );

                // Очищаем текущий код
                currentCode.setLength(0);
            }
        }

        // Возвращаем декодированную строку
        return result.toString();
    }
}