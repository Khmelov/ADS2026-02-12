package by.it.group510901.kostykovich.lesson03;

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
        private int LeftChild(int i){
            return 2 * i +1;
        }
        private int RightChild(int i){
            return 2 * i +2;
        }
        private int parent(int i){
            return (i - 1)/2;
        }



        int siftDown(int i) { //просеивание вверх
            int current = i;
           while(true) {
               int left = LeftChild(i);
               int right = RightChild(i);
               int max = current;
                   if(right < heap.size() && heap.get(right) > heap.get(max)){
                      max = right;
                  }
                   if(left < heap.size() && heap.get(left) > heap.get(max)){
                       max = left;
                   }
                   if(max == current) break;
               long temp = heap.get(current);
               heap.set(current, heap.get(max));
               heap.set(max, temp);

                current = max;
           }

            return current;
        }

        int siftUp(int i) { //просеивание вниз
            int current = i;
            if(i == 0) return 0;
            while(current > 0){
                int Node = parent(current);
                int leaf = current;
                if( heap.get(leaf) > heap.get(Node)){
                    leaf = Node;
                }
                if(leaf == current) break;
                long temp = heap.get(current);
                heap.set(current, heap.get(leaf));
                heap.set(leaf, temp);
                current = parent(current);

            }
            return i;
        }

        void insert(Long value) { //вставка
            heap.add(value);
            siftUp(heap.size()-1);
        }

        Long extractMax() { //извлечение и удаление максимума
            Long result = null;
            if(heap.size() == 0)return null;
            long koren = heap.get(0);
            heap.set(0, heap.get(heap.size()-1));
            heap.remove(heap.size()-1);
            siftDown(0);
            result = koren;

            return result;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}
