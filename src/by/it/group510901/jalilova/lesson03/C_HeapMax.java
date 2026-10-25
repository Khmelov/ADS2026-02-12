package by.it.group510901.jalilova.lesson03;

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

        // Возвращает индекс родителя
        private int parent(int i) {
            return (i - 1) / 2;
        }

        // Возвращает индекс левого ребёнка
        private int leftChild(int i) {
            return 2 * i + 1;
        }

        // Возвращает индекс правого ребёнка
        private int rightChild(int i) {
            return 2 * i + 2;
        }

        // Просеивание вверх (восстановление свойства кучи после вставки)
        int siftUp(int i) {
            while (i > 0 && heap.get(parent(i)) < heap.get(i)) {
                // меняем местами родителя и текущий элемент
                Long temp = heap.get(parent(i));
                heap.set(parent(i), heap.get(i));
                heap.set(i, temp);
                i = parent(i);
            }
            return i;
        }

        // Просеивание вниз (восстановление свойства кучи после извлечения максимума)
        int siftDown(int i) {
            int size = heap.size();
            while (true) {
                int left = leftChild(i);
                int right = rightChild(i);
                int largest = i;

                if (left < size && heap.get(left) > heap.get(largest)) {
                    largest = left;
                }
                if (right < size && heap.get(right) > heap.get(largest)) {
                    largest = right;
                }
                if (largest == i) {
                    break;
                }
                // меняем местами i и наибольшего ребёнка
                Long temp = heap.get(i);
                heap.set(i, heap.get(largest));
                heap.set(largest, temp);
                i = largest;
            }
            return i;
        }

        // Вставка нового элемента
        void insert(Long value) {
            heap.add(value);          // добавляем в конец
            siftUp(heap.size() - 1); // восстанавливаем кучу
        }

        // Извлечение максимума (корня)
        Long extractMax() {
            if (heap.isEmpty()) {
                return null;
            }
            Long max = heap.get(0);
            // Перемещаем последний элемент в корень
            heap.set(0, heap.get(heap.size() - 1));
            heap.remove(heap.size() - 1);
            if (!heap.isEmpty()) {
                siftDown(0); // восстанавливаем кучу
            }
            return max;
        }
    }
}