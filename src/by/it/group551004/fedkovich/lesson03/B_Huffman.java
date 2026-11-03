package by.it.group551004.fedkovich.lesson03;

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

    private void parseSymCodePairs(Scanner scanner, char[] symbols, String[] codes, int entries) {
        char letter;
        String code;
        int i;

        for (i = 0; i < entries; ++i) {
            symbols[i] = scanner.next().charAt(0);
            codes[i] = scanner.next();
        }
    }

    private void countCodesWithLen(int[] count_with_len, String[] codes) {
        int i;

        for (i = 0; i < codes.length; ++i) {
            ++count_with_len[codes[i].length()];
        }
    }

    private void findFirstCodes(int[] first_code, String[] codes) {
        int code_len, prev_len;
        String code;
        int i;

        prev_len = 0;
        for (i = 0; i < codes.length; ++i) {
            code = codes[i];
            code_len = code.length();

            if (code_len > prev_len) {
                prev_len = code_len;
                first_code[code_len] = Integer.parseInt(code, 2);
            }
        }
    }

    void findStartIndices(int[] start, int[] count_with_len, int longest_len) {
        int len, idx;

        idx = 0;
        for (len = 1; len <= longest_len; ++len) {
            start[len] = idx;
            idx += count_with_len[len];
        }
    }

    String decode(InputStream inputStream) throws FileNotFoundException {
        StringBuilder result = new StringBuilder();
        // прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(inputStream);
        Integer count = scanner.nextInt();
        Integer length = scanner.nextInt();
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        // тут запишите ваше решение
        char[] symbols = new char[count];
        String[] codes = new String[count];

        String encoded;
        int longest_len;
        int[] first_code;
        int[] count_with_len;
        int[] start;

        parseSymCodePairs(scanner, symbols, codes, count);
        encoded = scanner.next();
        longest_len = codes[count - 1].length();

        count_with_len = new int[longest_len + 1];
        countCodesWithLen(count_with_len, codes);

        first_code = new int[longest_len + 1];
        findFirstCodes(first_code, codes);

        start = new int[longest_len + 1];
        findStartIndices(start, count_with_len, longest_len);

        int bit, bits, value, offset;
        int i;

        bit = 0;
        value = 0;
        bits = 0;
        offset = 0;

        for (i = 0; i < length; ++i) {
            bit = encoded.charAt(i) - '0';
            value = (value << 1) | bit;
            ++bits;

            if (bits <= longest_len && count_with_len[bits] > 0) {
                offset = value - first_code[bits];
                if (offset >= 0 && offset < count_with_len[bits]) {
                    result.append(symbols[start[bits] + offset]);
                    value = 0;
                    bits = 0;
                }
            }
        }

        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString(); // 01001100100111
    }
}
