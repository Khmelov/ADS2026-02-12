package by.it.group510902.vinnichuk.lesson03;

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
        /**
         * Просеивание вниз (sift down) - восстановление свойства кучи после удаления элемента
         * Двигаем элемент вниз, пока он меньше хотя бы одного из своих детей
         * @param i индекс элемента, который нужно просеять вниз
         * @return индекс, на котором элемент оказался после просеивания
         */

        //просеивание вверх
            int siftDown(int i) {
                int leftChild = 2 * i + 1;  // индекс левого ребенка в массиве (0-индексация)
                int rightChild = 2 * i + 2; // индекс правого ребенка в массиве
                int largest = i;             // индекс наибольшего элемента (пока текущий)

                // Сравниваем с левым ребенком, если он существует и больше текущего
                if (leftChild < heap.size() && heap.get(leftChild) > heap.get(largest)) {
                    largest = leftChild;
                }
                // Сравниваем с правым ребенком, если он существует и больше текущего максимума
                if (rightChild < heap.size() && heap.get(rightChild) > heap.get(largest)) {
                    largest = rightChild;
                }

                // Если нашли ребенка больше текущего элемента
                if (largest != i) {
                    // Меняем местами текущий элемент с наибольшим ребенком
                    swap(i, largest);
                    // Рекурсивно продолжаем просеивание вниз для нового индекса
                    siftDown(largest);
                }
                return largest;
            }

        /**
        * Просеивание вверх (sift up) - восстановление свойства кучи после вставки элемента
        * Двигаем элемент вверх, пока он больше своего родителя
        * @param i индекс элемента, который нужно просеять вверх
        * @return индекс, на котором элемент оказался после просеивания
        */
        int siftUp(int i) { //просеивание вниз
// Если элемент уже в корне (индекс 0), то просеивание не нужно
                if (i == 0) return i;

                int parent = (i - 1) / 2; // индекс родителя в массиве (0-индексация)

                // Если текущий элемент больше родителя, нарушено свойство max-кучи
                if (heap.get(i) > heap.get(parent)) {
                    // Меняем местами с родителем
                    swap(i, parent);
                    // Рекурсивно продолжаем просеивание вверх для нового индекса (бывшего родителя)
                    return siftUp(parent);
                }
            return i;// Возвращаем текущий индекс, если просеивание не потребовалось
        }
            /**
             * Вставка нового элемента в кучу
             * @param value значение для вставки
             */

        void insert(Long value) {
                // Добавляем элемент в конец массива
                heap.add(value);
                // Восстанавливаем свойство кучи, просеивая добавленный элемент вверх
                siftUp(heap.size() - 1);
        }
            /**
             * Извлечение максимального элемента (корня кучи)
             * @return максимальный элемент или null, если куча пуста
             */

        Long extractMax() { //извлечение и удаление максимума
                // Проверяем, не пуста ли куча
                if (heap.isEmpty()) {
                    return null;
                }
                // Максимальный элемент всегда находится в корне (индекс 0)
                Long result = heap.get(0);

                // Перемещаем последний элемент в корень
                heap.set(0, heap.get(heap.size() - 1));
                // Удаляем последний элемент (бывший корень теперь в конце)
                heap.remove(heap.size() - 1);

                // Если куча не пуста после удаления, восстанавливаем свойство кучи
                if (!heap.isEmpty()) {
                    // Просеиваем новый корень вниз
                    siftDown(0);
                }

                return result;
            }

            /**
             * Вспомогательный метод для обмена двух элементов в куче
             * @param i индекс первого элемента
             * @param j индекс второго элемента
             */
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
