package by.it.group510901.zuev.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

//Lesson 3. A_Huffman.
//Разработайте метод encode(File file) для кодирования строки (код Хаффмана)

// По данным файла (непустой строке ss длины не более 104104),
// состоящей из строчных букв латинского алфавита,
// постройте оптимальный по суммарной длине беспрефиксный код.

// Используйте Алгоритм Хаффмана — жадный алгоритм оптимального
// без префиксного кодирования алфавита с минимальной избыточностью.

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
        long startTime = System.currentTimeMillis();
        String result = instance.encode(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("%d %d\n", codes.size(), result.length());
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
        }
        System.out.println(result);
    }

    String encode(InputStream inputStream) throws FileNotFoundException {
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();

        // 1. Подсчёт частот символов
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        // 2. Очередь с приоритетом (компаратор: частота, затем минимальный символ)
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> {
            int cmp = Integer.compare(a.frequence, b.frequence);
            if (cmp != 0) return cmp;
            return Character.compare(a.getMinChar(), b.getMinChar());
        });

        // 3. Создание листьев
        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            pq.add(new LeafNode(entry.getValue(), entry.getKey()));
        }

        // 4. Построение дерева Хаффмана
        if (pq.size() == 1) {
            // Один символ – особый случай
            Node root = pq.poll();
            root.fillCodes("");
        } else {
            while (pq.size() > 1) {
                Node left = pq.poll();
                Node right = pq.poll();
                Node parent = new InternalNode(left, right);
                pq.add(parent);
            }
            Node root = pq.poll();
            root.fillCodes("");
        }

        // 5. Кодирование строки
        StringBuilder encoded = new StringBuilder();
        for (char c : s.toCharArray()) {
            encoded.append(codes.get(c));
        }
        return encoded.toString();
    }

    // ---------------------- Классы дерева ----------------------

    abstract class Node implements Comparable<Node> {
        final int frequence;

        private Node(int frequence) {
            this.frequence = frequence;
        }

        abstract void fillCodes(String code);
        abstract char getMinChar();

        @Override
        public int compareTo(Node o) {
            return Integer.compare(frequence, o.frequence);
        }
    }

    private class InternalNode extends Node {
        Node left, right;

        InternalNode(Node left, Node right) {
            super(left.frequence + right.frequence);
            this.left = left;
            this.right = right;
        }

        @Override
        void fillCodes(String code) {
            left.fillCodes(code + "0");
            right.fillCodes(code + "1");
        }

        @Override
        char getMinChar() {
            return (char) Math.min(left.getMinChar(), right.getMinChar());
        }
    }

    private class LeafNode extends Node {
        char symbol;

        LeafNode(int frequence, char symbol) {
            super(frequence);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            // Если код пустой (один символ), используем "0"
            codes.put(symbol, code.isEmpty() ? "0" : code);
        }

        @Override
        char getMinChar() {
            return symbol;
        }
    }
}
