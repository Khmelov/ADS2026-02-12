package by.it.group551004.kapusta.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

//Lesson 3. A_Huffman.

public class A_Huffman {

    static private final Map<Character, String> codes = new TreeMap<>();

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

    //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    String encode(InputStream inputStream) throws FileNotFoundException {
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();

        // 1. Считаем частоты символов
        Map<Character, Integer> count = new HashMap<>();
        for (char c : s.toCharArray()) {
            count.put(c, count.getOrDefault(c, 0) + 1);
        }

        // 2. Создаём очередь с приоритетом
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();

        // 3. Добавляем листья
        for (Map.Entry<Character, Integer> entry : count.entrySet()) {
            priorityQueue.add(new LeafNode(entry.getValue(), entry.getKey()));
        }

        // Особый случай: строка из одного символа
        if (priorityQueue.size() == 1) {
            Node only = priorityQueue.poll();
            only.fillCodes("0");
        } else {
            // 4. Строим дерево Хаффмана
            while (priorityQueue.size() > 1) {
                Node a = priorityQueue.poll();
                Node b = priorityQueue.poll();
                priorityQueue.add(new InternalNode(a, b));
            }

            // 5. Генерируем коды
            Node root = priorityQueue.poll();
            root.fillCodes("");
        }

        // 6. Кодируем строку
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            sb.append(codes.get(c));
        }

        return sb.toString();
    }

    // Узел дерева
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

    // Внутренний узел
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

    // Лист
    private class LeafNode extends Node {
        char symbol;

        LeafNode(int frequence, char symbol) {
            super(frequence);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            codes.put(this.symbol, code);
        }
    }
}
