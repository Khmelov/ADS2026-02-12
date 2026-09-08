package by.it.group551001.anenko.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

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

        if (!scanner.hasNextInt()) return "";
        int count = scanner.nextInt();
        int length = scanner.nextInt();

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        char[] letters = new char[count];
        String[] codes = new String[count];

        for (int i = 0; i < count; i++) {
            String letterWithColon = scanner.next();
            letters[i] = letterWithColon.charAt(0);
            codes[i] = scanner.next();
        }

        String encodedString = scanner.next();
        StringBuilder currentPart = new StringBuilder();

        for (int i = 0; i < encodedString.length(); i++) {
            currentPart.append(encodedString.charAt(i));
            String currentStr = currentPart.toString();

            for (int j = 0; j < count; j++) {
                if (currentStr.equals(codes[j])) {
                    result.append(letters[j]);
                    currentPart.setLength(0);
                    break;
                }
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        return result.toString();
    }
}