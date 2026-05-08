package by.it.group551001.belozorchik.lesson03;

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

        int siftUp(int i) { // просеивание вверх  - восстановление порядка
            while (i > 0) { //пока не дошли до корня
                int parent = (i - 1) / 2;   //из лк
                // родитель должен быть >= потомку
                // если i больше - правило нарушено - меняем
                if (heap.get(i) > heap.get(parent)) {
                    Long temp = heap.get(i);
                    heap.set(i, heap.get(parent));
                    heap.set(parent, temp);
                    i = parent; // вверх
                } else {
                    break;
                }
            }
            return i;
        }

        int siftDown(int i) { // просеивание вниз - для удаления самого большьго
            int size = heap.size();
            while (2 * i + 1 < size) {  //пока у текущего элемента есть хотя бы один ребенок
                int left = 2 * i + 1;
                int right = 2 * i + 2;
                int largest = left; // берем, что левый - самый большой

                // если есть правый ребенок > левого - меняем
                if (right < size && heap.get(right) > heap.get(left)) {
                    largest = right;
                }   // после обмена в узле окажнтся максимум

                // если текущий элемент меньше самого большого - меняем
                if (heap.get(i) < heap.get(largest)) {
                    Long temp = heap.get(i);
                    heap.set(i, heap.get(largest));
                    heap.set(largest, temp);
                    i = largest; // Двигаемся вниз
                } else {
                    break;
                }
            }
            return i;
        }

        void insert(Long value) { // вставка
            heap.add(value); // добавили  в конец
            siftUp(heap.size() - 1); // просеиваем наверх
        }

        Long extractMax() { // извлечение и удаление максимума
            if (heap.isEmpty()) return null;

            Long result = heap.get(0); // максимум в корне
            Long last = heap.remove(heap.size() - 1); // удаляем  последний элемент

            if (!heap.isEmpty()) {
                heap.set(0, last); // теперь последний элемент в корень
                siftDown(0);       // просеиваем  вниз
            }
            return result;
        }
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}
