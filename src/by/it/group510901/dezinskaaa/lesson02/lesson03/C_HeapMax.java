package by.it.group510901.dezinskaaa.lesson02.lesson03;

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
        Integer count = scanner.nextInt();
        for (int i = 0; i < count; ) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null && res > maxValue) maxValue = res;
                System.out.println(res); //для вывода извлеченного значения
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
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        //тут запишите ваше решение.
        //Будет мало? Ну тогда можете его собрать как Generic и/или использовать в варианте B
        private List<Long> heap = new ArrayList<>();

        // Просеивание вниз (восстановление свойства max-heap после удаления корня)
        int siftDown(int i) {
            int size = heap.size();
            while (true) {
                int left = 2 * i + 1;
                int right = 2 * i + 2;
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
                // меняем местами текущий элемент с наибольшим ребёнком
                Long temp = heap.get(i);
                heap.set(i, heap.get(largest));
                heap.set(largest, temp);
                i = largest;
            }
            return i;
        }

        // Просеивание вверх (восстановление свойства max-heap после вставки)
        int siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap.get(i) <= heap.get(parent)) {
                    break;
                }
                // меняем местами с родителем
                Long temp = heap.get(i);
                heap.set(i, heap.get(parent));
                heap.set(parent, temp);
                i = parent;
            }
            return i;
        }

        // Вставка нового элемента в кучу
        void insert(Long value) {
            heap.add(value);          // добавляем в конец
            siftUp(heap.size() - 1); // восстанавливаем свойство кучи
        }

        // Извлечение и удаление максимального элемента (корня)
        Long extractMax() {
            if (heap.isEmpty()) {
                return null; // куча пуста
            }
            Long max = heap.get(0);   // корень — максимальный элемент
            Long last = heap.remove(heap.size() - 1); // удаляем последний
            if (!heap.isEmpty()) {
                heap.set(0, last);    // перемещаем последний элемент в корень
                siftDown(0);          // восстанавливаем свойство кучи
            }
            return max;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}