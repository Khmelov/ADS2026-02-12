package by.it.group510902.Borisyuk.lesson03;

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
        scanner.nextLine(); // переходим на следующую строку после числа
        for (int i = 0; i < count; i++) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null && res > maxValue) maxValue = res;
                System.out.println(res);
            }
            if (s.contains(" ")) {
                String[] p = s.split(" ");
                if (p[0].equalsIgnoreCase("insert"))
                    heap.insert(Long.parseLong(p[1]));
            }
        }
        return maxValue;
    }

    private class MaxHeap {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        //тут запишите ваше решение.
        //Будет мало? Ну тогда можете его собрать как Generic и/или использовать в варианте B
        private List<Long> heap = new ArrayList<>();

        int siftDown(int i) { //просеивание вниз
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int largest = i;

            // Если левый ребенок существует и больше текущего
            if (left < heap.size() && heap.get(left) > heap.get(largest)) {
                largest = left;
            }

            // Если правый ребенок существует и больше текущего максимума
            if (right < heap.size() && heap.get(right) > heap.get(largest)) {
                largest = right;
            }

            // Если наибольший элемент не текущий, меняем их местами и продолжаем просеивание
            if (largest != i) {
                swap(i, largest);
                siftDown(largest);
            }

            return i;
        }

        int siftUp(int i) { //просеивание вверх
            while (i > 0) {
                int parent = (i - 1) / 2;
                // Если текущий элемент больше родительского, меняем их местами
                if (heap.get(i) > heap.get(parent)) {
                    swap(i, parent);
                    i = parent;
                } else {
                    break;
                }
            }
            return i;
        }

        void insert(Long value) { //вставка
            // Добавляем элемент в конец кучи
            heap.add(value);
            // Просеиваем его вверх для восстановления свойства max-кучи
            siftUp(heap.size() - 1);
        }

        Long extractMax() { //извлечение и удаление максимума
            if (heap.isEmpty()) {
                return null;
            }

            // Максимальный элемент находится в корне (индекс 0)
            Long result = heap.get(0);

            // Перемещаем последний элемент в корень
            heap.set(0, heap.get(heap.size() - 1));
            // Удаляем последний элемент
            heap.remove(heap.size() - 1);

            // Если куча не пуста, просеиваем новый корень вниз
            if (!heap.isEmpty()) {
                siftDown(0);
            }

            return result;
        }

        // Вспомогательный метод для обмена элементов
        private void swap(int i, int j) {
            Long temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}