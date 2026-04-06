package by.it.group510902.paleychik.lesson03;

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

    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap heap = new MaxHeap();

        Scanner scanner = new Scanner(stream);
        int count = scanner.nextInt();
        scanner.nextLine(); // важно! очистка после nextInt

        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine();

            if (line.equalsIgnoreCase("ExtractMax")) {
                Long res = heap.extractMax();
                if (res != null) {
                    System.out.println(res);
                    if (res > maxValue) maxValue = res;
                }
            } else if (line.startsWith("Insert")) {
                String[] parts = line.split(" ");
                heap.insert(Long.parseLong(parts[1]));
            }
        }

        return maxValue;
    }

    private class MaxHeap {

        private List<Long> heap = new ArrayList<>();

        // Просеивание вниз
        void siftDown(int i) {
            while (true) {
                int left = 2 * i + 1;
                int right = 2 * i + 2;
                int largest = i;

                if (left < heap.size() && heap.get(left) > heap.get(largest)) {
                    largest = left;
                }

                if (right < heap.size() && heap.get(right) > heap.get(largest)) {
                    largest = right;
                }

                if (largest == i) break;

                swap(i, largest);
                i = largest;
            }
        }

        // Просеивание вверх
        void siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;

                if (heap.get(i) <= heap.get(parent)) break;

                swap(i, parent);
                i = parent;
            }
        }

        // Вставка
        void insert(Long value) {
            heap.add(value);
            siftUp(heap.size() - 1);
        }

        // Извлечение максимума
        Long extractMax() {
            if (heap.isEmpty()) return null;

            Long max = heap.get(0);
            Long last = heap.remove(heap.size() - 1);

            if (!heap.isEmpty()) {
                heap.set(0, last);
                siftDown(0);
            }

            return max;
        }

        // Обмен элементов
        private void swap(int i, int j) {
            Long temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }
    }
}