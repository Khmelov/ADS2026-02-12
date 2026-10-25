package by.it.group551002.sikorski.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Lesson 3. C_Heap.
// Задача: построить max-кучу = пирамиду = бинарное сбалансированное дерево на массиве.
// ВАЖНО! НЕЛЬЗЯ ИСПОЛЬЗОВАТЬ НИКАКИЕ КОЛЛЕКЦИИ, КРОМЕ ARRAYLIST (его можно, но только для массива)

//      Проверка проводится по данным файла
//      Первая строка входа содержит число операций 1 ≤ n ≤ 100000.
//      Каждая из последующих nn строк задают операцию одного из следующих двух типов:

//      Insert x, где 0 ≤ x ≤ 1000000000 — целое число;
//      ExtractMax.

//      Первая операция добавляет число x в очередь с приоритетами,
//      вторая — извлекает максимальное число и выводит его.

//      Sample Input:
//      6
//      Insert 200
//      Insert 10
//      ExtractMax
//      Insert 5
//      Insert 500
//      ExtractMax
//
//      Sample Output:
//      200
//      500


public class C_HeapMax {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_HeapMax.class.getResourceAsStream("dataC.txt");
        C_HeapMax instance = new C_HeapMax();
        System.out.println("MAX=" + instance.findMaxValue(stream));
    }

    //эта процедура читает данные из файла, ее можно не менять.
    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap heap = new MaxHeap();
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(stream);
        Integer count = scanner.nextInt();
        for (int i = 0; i < count; ) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null && res > maxValue) maxValue = res;
                System.out.println();
                i++;
            }
            if (s.contains(" ")) {
                String[] p = s.split(" ");
                if (p[0].equalsIgnoreCase("insert"))
                    heap.insert(Long.parseLong(p[1]));
                i++;
                //System.out.println(heap); //debug
            }
        }
        return maxValue;
    }

    private class MaxHeap {
        private List<Long> heap = new ArrayList<>();

        // Восстановление инварианта кучи путем перемещения элемента к началу массива
        int siftUp(int currentIndex) {
            while (currentIndex > 0) {
                // Вычисляем индекс вышестоящего узла по формуле (i-1)/2
                int upperIndex = (currentIndex - 1) / 2;


                if (heap.get(currentIndex) <= heap.get(upperIndex)) {
                    break;
                }

                // Нарушение инварианта: текущий элемент больше вышестоящего, меняем их местами
                swap(currentIndex, upperIndex);
                currentIndex = upperIndex;
            }
            return currentIndex;
        }

        // Восстановление инварианта кучи путем перемещения элемента к концу массива
        int siftDown(int currentIndex) {
            int heapSize = heap.size();

            while (true) {
                // Индексы узлов на следующем уровне
                int leftBranch = 2 * currentIndex + 1;
                int rightBranch = 2 * currentIndex + 2;
                int largestIndex = currentIndex;

                if (leftBranch < heapSize && heap.get(leftBranch) > heap.get(largestIndex)) {
                    largestIndex = leftBranch;
                }


                if (rightBranch < heapSize && heap.get(rightBranch) > heap.get(largestIndex)) {
                    largestIndex = rightBranch;
                }

                // Если текущий индекс остался наибольшим, инвариант восстановлен
                if (largestIndex == currentIndex) {
                    break;
                }

                // Перемещаем большее значение вверх, а текущее вниз
                swap(currentIndex, largestIndex);
                currentIndex = largestIndex;
            }
            return currentIndex;
        }

        void insert(Long value) {
            // Добавление элемента в конец структуры и запуск балансировки вверх
            heap.add(value);
            siftUp(heap.size() - 1);
        }

        Long extractMax() {
            if (heap.isEmpty()) return null;

            // Глобальный максимум всегда находится в нулевом индексе
            Long maxValue = heap.get(0);
            int lastIndex = heap.size() - 1;

            // Переносим последний элемент массива в начало для сохранения связности
            Long lastValue = heap.remove(lastIndex);

            if (!heap.isEmpty()) {
                heap.set(0, lastValue);
                // Балансировка нового корневого элемента вниз
                siftDown(0);
            }

            return maxValue;
        }

        private void swap(int i, int j) {
            Long temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}
