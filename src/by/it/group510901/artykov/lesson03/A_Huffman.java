package by.it.group510901.artykov.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/*
 * Алгоритм Хаффмана.
 *
 * Необходимо:
 * 1. Подсчитать частоту символов
 * 2. Построить дерево Хаффмана
 * 3. Создать бинарные коды символов
 * 4. Закодировать строку
 *
 * Используется жадный алгоритм.
 */

public class A_Huffman {

    /*
     * Карта кодов символов.
     *
     * Например:
     * a -> 0
     * b -> 10
     */
    static private final Map<Character, String> codes = new TreeMap<>();

    public static void main(String[] args) throws FileNotFoundException {

        InputStream inputStream =
                A_Huffman.class.getResourceAsStream("dataA.txt");

        A_Huffman instance = new A_Huffman();

        long startTime = System.currentTimeMillis();

        String result = instance.encode(inputStream);

        long finishTime = System.currentTimeMillis();

        // Количество символов и длина кода
        System.out.printf(
                "%d %d\n",
                codes.size(),
                result.length()
        );

        // Вывод кодов
        for (Map.Entry<Character, String> entry : codes.entrySet()) {

            System.out.printf(
                    "%s: %s\n",
                    entry.getKey(),
                    entry.getValue()
            );
        }

        // Вывод закодированной строки
        System.out.println(result);
    }

    /*
     * Метод кодирует строку
     * с помощью алгоритма Хаффмана.
     */
    String encode(InputStream inputStream)
            throws FileNotFoundException {

        // Чтение строки из файла
        Scanner scanner = new Scanner(inputStream);

        String s = scanner.next();

        /*
         * Карта частот символов.
         *
         * Например:
         * a -> 4
         * b -> 2
         */
        Map<Character, Integer> count = new HashMap<>();

        /*
         * Подсчёт количества каждого символа
         */
        for (char c : s.toCharArray()) {

            if (count.containsKey(c)) {

                count.put(c, count.get(c) + 1);

            } else {

                count.put(c, 1);
            }
        }

        /*
         * Приоритетная очередь.
         *
         * Узлы с меньшей частотой
         * имеют больший приоритет.
         */
        PriorityQueue<Node> priorityQueue =
                new PriorityQueue<>();

        /*
         * Создание листьев дерева
         */
        for (Map.Entry<Character, Integer> entry : count.entrySet()) {

            priorityQueue.add(
                    new LeafNode(
                            entry.getValue(),
                            entry.getKey()
                    )
            );
        }

        /*
         * Построение дерева Хаффмана
         */
        while (priorityQueue.size() > 1) {

            // Берём два минимальных узла
            Node left = priorityQueue.poll();

            Node right = priorityQueue.poll();

            // Создаём родителя
            InternalNode parent =
                    new InternalNode(left, right);

            // Возвращаем обратно в очередь
            priorityQueue.add(parent);
        }

        /*
         * Корень дерева
         */
        Node root = priorityQueue.poll();

        /*
         * Генерация кодов символов
         */
        if (root != null) {

            /*
             * Если символ только один,
             * код должен быть "0"
             */
            if (root instanceof LeafNode) {

                root.fillCodes("0");

            } else {

                root.fillCodes("");
            }
        }

        /*
         * Кодирование строки
         */
        StringBuilder sb = new StringBuilder();

        for (char c : s.toCharArray()) {

            sb.append(codes.get(c));
        }

        return sb.toString();
    }

    // =========================================================
    // Базовый класс узла дерева
    // =========================================================

    abstract class Node implements Comparable<Node> {

        // Частота символа
        private final int frequence;

        /*
         * Конструктор узла
         */
        private Node(int frequence) {

            this.frequence = frequence;
        }

        /*
         * Метод генерации кодов
         */
        abstract void fillCodes(String code);

        /*
         * Сравнение узлов по частоте.
         *
         * Нужно для PriorityQueue.
         */
        @Override
        public int compareTo(Node o) {

            return Integer.compare(
                    frequence,
                    o.frequence
            );
        }
    }

    // =========================================================
    // Внутренний узел дерева
    // =========================================================

    private class InternalNode extends Node {

        // Левый ребёнок
        Node left;

        // Правый ребёнок
        Node right;

        /*
         * Конструктор внутреннего узла
         */
        InternalNode(Node left, Node right) {

            super(left.frequence + right.frequence);

            this.left = left;
            this.right = right;
        }

        /*
         * Рекурсивное построение кодов.
         *
         * Влево -> 0
         * Вправо -> 1
         */
        @Override
        void fillCodes(String code) {

            left.fillCodes(code + "0");

            right.fillCodes(code + "1");
        }
    }

    // =========================================================
    // Лист дерева
    // =========================================================

    private class LeafNode extends Node {

        // Символ хранится только в листе
        char symbol;

        /*
         * Конструктор листа
         */
        LeafNode(int frequence, char symbol) {

            super(frequence);

            this.symbol = symbol;
        }

        /*
         * Когда дошли до листа,
         * сохраняем готовый код символа.
         */
        @Override
        void fillCodes(String code) {

            codes.put(this.symbol, code);
        }
    }
}