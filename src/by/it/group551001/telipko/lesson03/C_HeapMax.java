package by.it.group551001.telipko.lesson03;

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
        long maxValue = 0L;
        MaxHeap heap = new MaxHeap();
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(stream);
        int count = scanner.nextInt();
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

    private static class MaxHeap {

        private final List<Long> heap = new ArrayList<>();

        void siftDown(int i) {

            while (2 * i + 1 < heap.size()) {

                int childIndex1 = 2 * i + 1;
                int childIndex2 = 2 * i + 2;

                int childIndex = childIndex1;

                if (childIndex2 < heap.size() &&
                        heap.get(childIndex2) > heap.get(childIndex1)) {
                    childIndex = childIndex2;
                }

                if (heap.get(i) >= heap.get(childIndex))
                    break;

                long child = heap.get(i);
                heap.set(i, heap.get(childIndex));
                heap.set(childIndex, child);

                i = childIndex;
            }
        }

        void siftUp(int i) {


            while (i > 0) {

                int parentIndex = (i - 1) / 2;
                long child = heap.get(i);
                long parent = heap.get(parentIndex);

                if (parent >= child) {
                    break;
                }

                heap.set(parentIndex, child);
                heap.set(i, parent);

                i = parentIndex;
                parentIndex = (i - 1) / 2;
            }
        }

        void insert(Long value) {
            heap.add(value);
            siftUp(heap.size() - 1);
        }

        Long extractMax() {
            Long result = heap.removeFirst();

            if (!heap.isEmpty()) {
                heap.addFirst(heap.removeLast());
            }
            siftDown(0);

            return result;
        }
    }

    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}
