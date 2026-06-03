package by.it.group510901.artykov.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * Max Heap (максимальная куча).
 *
 * Куча хранится в ArrayList.
 *
 * Свойство max-heap:
 * родитель всегда больше детей.
 *
 * Операции:
 * 1. insert(x)     -> вставка
 * 2. extractMax() -> извлечение максимума
 */

public class C_HeapMax {

    public static void main(String[] args)
            throws FileNotFoundException {

        InputStream stream =
                C_HeapMax.class.getResourceAsStream("dataC.txt");

        C_HeapMax instance = new C_HeapMax();

        System.out.println(
                "MAX=" + instance.findMaxValue(stream)
        );
    }

    /*
     * Чтение операций из файла.
     */
    Long findMaxValue(InputStream stream) {

        Long maxValue = 0L;

        MaxHeap heap = new MaxHeap();

        Scanner scanner = new Scanner(stream);

        Integer count = scanner.nextInt();

        for (int i = 0; i < count; ) {

            String s = scanner.nextLine();

            /*
             * Извлечение максимума
             */
            if (s.equalsIgnoreCase("extractMax")) {

                Long res = heap.extractMax();

                if (res != null && res > maxValue) {

                    maxValue = res;
                }

                System.out.println(res);

                i++;
            }

            /*
             * Вставка элемента
             */
            if (s.contains(" ")) {

                String[] p = s.split(" ");

                if (p[0].equalsIgnoreCase("insert")) {

                    heap.insert(
                            Long.parseLong(p[1])
                    );
                }

                i++;
            }
        }

        return maxValue;
    }

    // =========================================================
    // Класс максимальной кучи
    // =========================================================

    private class MaxHeap {

        /*
         * Массив для хранения кучи.
         */
        private List<Long> heap =
                new ArrayList<>();

        /*
         * Просеивание вниз.
         *
         * Используется после удаления максимума.
         */
        int siftDown(int i) {

            while (true) {

                // Левый ребёнок
                int left = 2 * i + 1;

                // Правый ребёнок
                int right = 2 * i + 2;

                // Индекс максимального элемента
                int max = i;

                /*
                 * Проверяем левого ребёнка
                 */
                if (left < heap.size()
                        && heap.get(left) > heap.get(max)) {

                    max = left;
                }

                /*
                 * Проверяем правого ребёнка
                 */
                if (right < heap.size()
                        && heap.get(right) > heap.get(max)) {

                    max = right;
                }

                /*
                 * Если максимум уже родитель,
                 * куча корректна
                 */
                if (max == i) {

                    break;
                }

                // Меняем местами
                swap(i, max);

                // Переходим ниже
                i = max;
            }

            return i;
        }

        /*
         * Просеивание вверх.
         *
         * Используется после вставки элемента.
         */
        int siftUp(int i) {

            while (i > 0) {

                // Индекс родителя
                int parent = (i - 1) / 2;

                /*
                 * Если родитель меньше —
                 * меняем местами
                 */
                if (heap.get(i) > heap.get(parent)) {

                    swap(i, parent);

                    i = parent;

                } else {

                    break;
                }
            }

            return i;
        }

        /*
         * Вставка нового элемента.
         */
        void insert(Long value) {

            // Добавляем в конец
            heap.add(value);

            // Восстанавливаем кучу
            siftUp(heap.size() - 1);
        }

        /*
         * Извлечение максимального элемента.
         */
        Long extractMax() {

            // Если куча пустая
            if (heap.isEmpty()) {

                return null;
            }

            // Максимум находится в корне
            Long result = heap.get(0);

            /*
             * Последний элемент переносим в корень
             */
            Long last = heap.remove(heap.size() - 1);

            /*
             * Если куча ещё не пустая
             */
            if (!heap.isEmpty()) {

                heap.set(0, last);

                // Восстанавливаем кучу
                siftDown(0);
            }

            return result;
        }

        /*
         * Обмен двух элементов местами.
         */
        void swap(int i, int j) {

            Long temp = heap.get(i);

            heap.set(i, heap.get(j));

            heap.set(j, temp);
        }

        /*
         * Красивый вывод кучи.
         */
        @Override
        public String toString() {

            return heap.toString();
        }
    }

    /*
     * В реальных проектах обычно используют:
     * PriorityQueue, TreeSet, TreeMap и т.д.
     */
}