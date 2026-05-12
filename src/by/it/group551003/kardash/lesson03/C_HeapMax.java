package by.it.group551003.kardash.lesson03;

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

        private List<Long> heap = new ArrayList<>();

        // Вспомогательный метод: обмен элементов
        private void swap(int i, int j) {
            Long temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }

        int siftDown(int i) { // просеивание вниз (после удаления корня)
            int max = i;
            int left = 2 * i + 1;   // индекс левого потомка
            int right = 2 * i + 2;  // индекс правого потомка

            // Если левый потомок существует и больше текущего максимума
            if (left < heap.size() && heap.get(left) > heap.get(max)) {
                max = left;
            }
            // Если правый потомок существует и больше текущего максимума
            if (right < heap.size() && heap.get(right) > heap.get(max)) {
                max = right;
            }

            // Если максимум не в текущем узле — меняем местами и продолжаем
            if (max != i) {
                swap(i, max);
                return siftDown(max);
            }
            return i;
        }

        int siftUp(int i) { // просеивание вверх (после вставки)
            // Пока не достигли корня и родитель меньше текущего элемента
            while (i > 0 && heap.get((i - 1) / 2) < heap.get(i)) {
                swap(i, (i - 1) / 2);  // меняем с родителем
                i = (i - 1) / 2;        // переходим к родителю
            }
            return i;
        }

        void insert(Long value) { // вставка элемента
            heap.add(value);              // добавляем в конец
            siftUp(heap.size() - 1);      // просеиваем вверх
        }

        Long extractMax() { // извлечение и удаление максимума
            if (heap.isEmpty()) return null;

            Long result = heap.get(0);    // сохраняем корень (максимум)

            // Если в куче больше одного элемента
            if (heap.size() > 1) {
                heap.set(0, heap.get(heap.size() - 1));  // перемещаем последний на корень
                heap.remove(heap.size() - 1);            // удаляем последний
                siftDown(0);                              // просеиваем вниз от корня
            } else {
                heap.clear();  // если был один элемент — просто очищаем
            }

            return result;
        }
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1!!!!!!!!!!!!!!!!!!!!!!!!1
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}

