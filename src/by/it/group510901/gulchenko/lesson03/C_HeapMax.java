package by.it.group510901.gulchenko.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
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
        if (stream == null) {
            throw new FileNotFoundException("Файл dataC.txt не найден");
        }

        C_HeapMax instance = new C_HeapMax();
        System.out.println("MAX=" + instance.findMaxValue(stream));
    }

    //эта процедура читает данные из файла, ее можно не менять.
    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap heap = new MaxHeap();
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(stream);
        int count = scanner.nextInt();

        for (int i = 0; i < count; i++) {
            String operation = scanner.next();

            if (operation.equalsIgnoreCase("ExtractMax")) {
                Long result = heap.extractMax();
                if (result != null && result > maxValue) {
                    maxValue = result;
                }
            } else if (operation.equalsIgnoreCase("Insert")) {
                long value = scanner.nextLong();
                heap.insert(value);
            }
        }
        return maxValue;
    }

    private class MaxHeap {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        //тут запишите ваше решение.
        //Будет мало? Ну тогда можете его собрать как Generic и/или использовать в варианте B
        private final ArrayList<Long> heap = new ArrayList<>();

        int siftDown(int i) { //просеивание вверх
            int currentIndex = i;

            while (true) {
                int leftChildIndex = 2 * currentIndex + 1;
                int rightChildIndex = 2 * currentIndex + 2;
                int largestIndex = currentIndex;

                if (leftChildIndex < heap.size() && heap.get(leftChildIndex) > heap.get(largestIndex)) {
                    largestIndex = leftChildIndex;
                }

                if (rightChildIndex < heap.size() && heap.get(rightChildIndex) > heap.get(largestIndex)) {
                    largestIndex = rightChildIndex;
                }

                if (largestIndex == currentIndex) {
                    break;
                }

                swap(currentIndex, largestIndex);
                currentIndex = largestIndex;
            }

            return currentIndex;
        }

        int siftUp(int i) { //просеивание вниз
            int currentIndex = i;

            while (currentIndex > 0) {
                int parentIndex = (currentIndex - 1) / 2;

                if (heap.get(currentIndex) > heap.get(parentIndex)) {
                    swap(currentIndex, parentIndex);
                    currentIndex = parentIndex;
                } else {
                    break;
                }
            }

            return currentIndex;
        }

        void insert(Long value) { //вставка
            heap.add(value);
            siftUp(heap.size() - 1);
        }

        Long extractMax() { //извлечение и удаление максимума
            if (heap.isEmpty()) {
                return null;
            }

            Long result = heap.get(0);
            Long lastValue = heap.remove(heap.size() - 1);

            if (!heap.isEmpty()) {
                heap.set(0, lastValue);
                siftDown(0);
            }

            return result;
        }

        private void swap(int firstIndex, int secondIndex) {
            Long temp = heap.get(firstIndex);
            heap.set(firstIndex, heap.get(secondIndex));
            heap.set(secondIndex, temp);
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}