package by.it.group510901.krot.lesson03;

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

        // Просеивание вниз (исправление кучи сверху вниз)
        int siftDown(int i) {
            int maxIndex = i;
            int left = 2 * i + 1;
            int right = 2 * i + 2;

            // Ищем максимальный среди родителя и детей
            if (left < heap.size() && heap.get(left) > heap.get(maxIndex)) {
                maxIndex = left;
            }
            if (right < heap.size() && heap.get(right) > heap.get(maxIndex)) {
                maxIndex = right;
            }

            // Если максимальный не родитель — меняем и продолжаем
            if (i != maxIndex) {
                Long temp = heap.get(i);
                heap.set(i, heap.get(maxIndex));
                heap.set(maxIndex, temp);
                return siftDown(maxIndex);
            }
            return i;
        }

        // Просеивание вверх (исправление кучи снизу вверх)
        int siftUp(int i) {
            if (i == 0) return i;

            int parent = (i - 1) / 2;

            // Если ребёнок больше родителя — меняем
            if (heap.get(i) > heap.get(parent)) {
                Long temp = heap.get(i);
                heap.set(i, heap.get(parent));
                heap.set(parent, temp);
                return siftUp(parent);
            }
            return i;
        }

        // Вставка элемента
        void insert(Long value) {
            heap.add(value);           // Добавляем в конец
            siftUp(heap.size() - 1);   // Поднимаем наверх
        }

        // Извлечение максимума
        Long extractMax() {
            if (heap.isEmpty()) {
                return null;
            }

            Long result = heap.get(0);           // Запоминаем максимум
            heap.set(0, heap.get(heap.size() - 1)); // На место корня ставим последний
            heap.remove(heap.size() - 1);         // Удаляем последний
            if (!heap.isEmpty()) {
                siftDown(0);                      // Опускаем новый корень вниз
            }
            return result;
        }
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}
