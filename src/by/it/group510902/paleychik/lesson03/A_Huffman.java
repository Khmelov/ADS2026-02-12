package by.it.group510902.paleychik.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

//Lesson 3. A_Huffman.
//Разработайте метод encode(File file) для кодирования строки (код Хаффмана)

// По данным файла (непустой строке ss длины не более 104104),
// состоящей из строчных букв латинского алфавита,
// постройте оптимальный по суммарной длине беспрефиксный код.

// Используйте Алгоритм Хаффмана — жадный алгоритм оптимального
// безпрефиксного кодирования алфавита с минимальной избыточностью.

// В первой строке выведите количество различных букв kk,
// встречающихся в строке, и размер получившейся закодированной строки.
// В следующих kk строках запишите коды букв в формате "letter: code".
// В последней строке выведите закодированную строку. Примеры ниже

//        Sample Input 1:
//        a
//
//        Sample Output 1:
//        1 1
//        a: 0
//        0

//        Sample Input 2:
//        abacabad
//
//        Sample Output 2:
//        4 14
//        a: 0
//        b: 10
//        c: 110
//        d: 111
//        01001100100111

public class A_Huffman {

    private static final Map<Character, String> codes = new TreeMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = A_Huffman.class.getResourceAsStream("dataA.txt");
        A_Huffman instance = new A_Huffman();

        String result = instance.encode(inputStream);

        System.out.printf("%d %d\n", codes.size(), result.length());
        codes.forEach((k, v) -> System.out.printf("%s: %s\n", k, v));
        System.out.println(result);
    }

    String encode(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();

        // 1. Подсчёт частот
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        // 2. Очередь с приоритетом
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (var entry : freq.entrySet()) {
            pq.add(new LeafNode(entry.getValue(), entry.getKey()));
        }

        // 3. Построение дерева
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            pq.add(new InternalNode(left, right));
        }

        Node root = pq.poll();

        // ОСОБЫЙ СЛУЧАЙ: если один символ
        if (root instanceof LeafNode) {
            codes.put(((LeafNode) root).symbol, "0");
        } else {
            root.fillCodes("");
        }

        // 4. Кодирование строки
        StringBuilder encoded = new StringBuilder();
        for (char c : s.toCharArray()) {
            encoded.append(codes.get(c));
        }

        return encoded.toString();
    }

    // ================== ДЕРЕВО ==================

    abstract class Node implements Comparable<Node> {
        protected final int freq;

        protected Node(int freq) {
            this.freq = freq;
        }

        abstract void fillCodes(String code);

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.freq, o.freq);
        }
    }

    private class InternalNode extends Node {
        Node left, right;

        InternalNode(Node left, Node right) {
            super(left.freq + right.freq);
            this.left = left;
            this.right = right;
        }

        @Override
        void fillCodes(String code) {
            left.fillCodes(code + "0");
            right.fillCodes(code + "1");
        }
    }

    private class LeafNode extends Node {
        char symbol;

        LeafNode(int freq, char symbol) {
            super(freq);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            codes.put(symbol, code);
        }
    }
}