package by.it.group510901.blyshko.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
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
        Integer count = scanner.nextInt();
        Integer length = scanner.nextInt();
        
        
        Map<String, Character> codes = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String letter = scanner.next();
            String code = scanner.next();
            codes.put(code, letter.charAt(0));
        }
        String encoded = scanner.next();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(encoded.charAt(i));
            Character symbol = codes.get(code.toString());
            if (symbol != null) {
                result.append(symbol);
                code.setLength(0);
            }
        }

        
        return result.toString(); 
    }


}
