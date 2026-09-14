package by.it.group510901.filimonova.lesson01.lesson03;

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
        private List<Long> heap = new ArrayList<>();

        //просеивание вниз (для восстановления свойства кучи после extractMax)
        int siftDown(int i) {
            int leftChild = 2 * i + 1;
            int rightChild = 2 * i + 2;
            int largest = i;

            // находим наибольший среди текущего узла и его детей
            if (leftChild < heap.size() && heap.get(leftChild) > heap.get(largest)) {
                largest = leftChild;
            }
            if (rightChild < heap.size() && heap.get(rightChild) > heap.get(largest)) {
                largest = rightChild;
            }

            // если наибольший не текущий узел, меняем местами и продолжаем просеивание
            if (largest != i) {
                swap(i, largest);
                siftDown(largest);
            }
            return i;
        }

        //просеивание вверх (для восстановления свойства кучи после insert)
        int siftUp(int i) {
            // пока не дошли до корня и родитель меньше текущего
            while (i > 0 && heap.get(parent(i)) < heap.get(i)) {
                swap(i, parent(i));
                i = parent(i);
            }
            return i;
        }

        //вставка нового элемента
        void insert(Long value) {
            heap.add(value);           // добавляем в конец
            siftUp(heap.size() - 1);  // просеиваем вверх
        }

        //извлечение и удаление максимума
        Long extractMax() {
            if (heap.isEmpty()) {
                return null;
            }

            Long result = heap.get(0);     // максимальный элемент в корне

            // перемещаем последний элемент в корень
            heap.set(0, heap.get(heap.size() - 1));
            heap.remove(heap.size() - 1);   // удаляем последний элемент

            if (!heap.isEmpty()) {
                siftDown(0);                // восстанавливаем свойство кучи
            }

            return result;
        }

        //вспомогательные методы
        private int parent(int i) {
            return (i - 1) / 2;
        }

        private void swap(int i, int j) {
            Long temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }
}
/*Свойство max-кучи: родитель всегда больше или равен детям

siftUp (просеивание вверх):
- Используется при insert
- Новый элемент добавляется в конец массива
- Сравнивается с родителем: если больше → меняются местами
- Повторяется до корня

siftDown (просеивание вниз):
- Используется при extractMax
- Корень заменяется последним элементом
- Сравнивается с детьми: если меньше → меняется с большим ребенком
- Повторяется до восстановления свойства

Сложность:
- insert: O(log n)
- extractMax: O(log n)
- Память: O(n)*/