package by.it.group551001.sidorova.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class A_Huffman {

    static private final Map<Character, String> codes = new TreeMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = A_Huffman.class.getResourceAsStream("dataA.txt");
        A_Huffman instance = new A_Huffman();
        String result = instance.encode(inputStream);
        System.out.printf("%d %d\n", codes.size(), result.length());
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
        }
        System.out.println(result);
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    String encode(InputStream inputStream) throws FileNotFoundException {

        codes.clear(); // важно!

        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();

        Map<Character, Integer> count = new HashMap<>();

        // 1. Подсчёт частот
        for (char c : s.toCharArray()) {
            count.put(c, count.getOrDefault(c, 0) + 1);
        }

        // 2. Создание очереди листьев
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> e : count.entrySet()) {
            priorityQueue.add(new LeafNode(e.getValue(), e.getKey()));
        }

        // 3–4. Построение дерева Хаффмана
        if (priorityQueue.size() == 1) {
            // случай одной буквы
            Node only = priorityQueue.poll();
            only.fillCodes("0");
        } else {
            while (priorityQueue.size() > 1) {
                Node left = priorityQueue.poll();
                Node right = priorityQueue.poll();
                priorityQueue.add(new InternalNode(left, right));
            }
            Node root = priorityQueue.poll();
            root.fillCodes("");
        }

        // Кодирование строки
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            sb.append(codes.get(c));
        }

        return sb.toString();
    }

    abstract class Node implements Comparable<Node> {
        private final int frequence;

        private Node(int frequence) {
            this.frequence = frequence;
        }

        abstract void fillCodes(String code);

        @Override
        public int compareTo(Node o) {
            return Integer.compare(frequence, o.frequence);
        }
    }

    private class InternalNode extends Node {
        Node left;
        Node right;

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
    }

    private class LeafNode extends Node {
        char symbol;

        LeafNode(int frequence, char symbol) {
            super(frequence);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            codes.put(symbol, code);
        }
    }
}
