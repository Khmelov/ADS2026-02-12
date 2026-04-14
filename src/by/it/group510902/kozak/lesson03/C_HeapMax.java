package by.it.group510902.kozak.lesson03;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        //тут запишите ваше решение.
        //Будет мало? Ну тогда можете его собрать как Generic и/или использовать в варианте B
        private List<Long> heap = new ArrayList<>();

        int siftDown(int i) { //просеивание вверх
            int size = heap.size();

            while (true) {
                int left  = 2 * i + 1; // индекс левого ребёнка
                int right = 2 * i + 2; // индекс правого ребёнка
                int largest = i;       // предполагаем, что текущий — наибольший

                // Если левый ребёнок существует И больше текущего
                if (left < size && heap.get(left) > heap.get(largest)) {
                    largest = left;
                }
                // Если правый ребёнок существует И больше текущего наибольшего
                if (right < size && heap.get(right) > heap.get(largest)) {
                    largest = right;
                }

                // Если текущий уже наибольший — куча восстановлена, стоп
                if (largest == i) break;

                // Меняем местами текущий и наибольший ребёнок
                Long tmp = heap.get(i);
                heap.set(i, heap.get(largest));
                heap.set(largest, tmp);

                // Продолжаем просеивание уже на новой позиции
                i = largest;
            }
            return i;
        }

        int siftUp(int i) { //просеивание вниз
            while (i > 0) {
                int parent = (i - 1) / 2; // индекс родителя

                // Если текущий элемент больше родителя — нарушение кучи!
                if (heap.get(i) > heap.get(parent)) {
                    // Меняем местами с родителем
                    Long tmp = heap.get(i);
                    heap.set(i, heap.get(parent));
                    heap.set(parent, tmp);

                    // Продолжаем всплытие уже с позиции родителя
                    i = parent;
                } else {
                    // Родитель >= текущего — куча в порядке, стоп
                    break;
                }
            }
            return i;
        }

        void insert(Long value) { //вставка
            heap.add(value);              // шаг 1: добавить в конец
            siftUp(heap.size() - 1);     // шаг 2: восстановить кучу всплытием
        }

        Long extractMax() { //извлечение и удаление максимума
            if (heap.isEmpty()) return null;
            Long result = heap.get(0);
            int last = heap.size() - 1;

            heap.set(0, heap.get(last));           // шаг 2: переставить последний в корень
            heap.remove(last);                     // шаг 3: удалить последний

            if (!heap.isEmpty()) siftDown(0);      // шаг 4: восстановить кучу погружением

            System.out.print(result);              // вывод согласно findMaxValue
            return result;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}
