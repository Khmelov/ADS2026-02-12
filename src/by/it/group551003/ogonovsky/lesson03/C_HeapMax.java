package by.it.group551003.ogonovsky.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
                System.out.println();
                i++;
            }
            if (s.contains(" ")) {
                String[] p = s.split(" ");
                if (p[0].equalsIgnoreCase("insert"))
                    heap.insert(Long.parseLong(p[1]));
                i++;
            }
        }
        return maxValue;
    }

    private class MaxHeap {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        private List<Long> heap = new ArrayList<>();

        // просеивание вниз: элемент i «тонет», меняясь с большим из детей,
        // пока свойство max-кучи не восстановится
        int siftDown(int i) {
            int n = heap.size();
            while (true) {
                int left = 2 * i + 1;
                int right = 2 * i + 2;
                int largest = i;
                if (left < n && heap.get(left) > heap.get(largest)) {
                    largest = left;
                }
                if (right < n && heap.get(right) > heap.get(largest)) {
                    largest = right;
                }
                if (largest == i) break;
                Long tmp = heap.get(i);
                heap.set(i, heap.get(largest));
                heap.set(largest, tmp);
                i = largest;
            }
            return i;
        }

        // просеивание вверх: элемент i «всплывает», меняясь с родителем,
        // пока он больше родителя
        int siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap.get(i) > heap.get(parent)) {
                    Long tmp = heap.get(i);
                    heap.set(i, heap.get(parent));
                    heap.set(parent, tmp);
                    i = parent;
                } else {
                    break;
                }
            }
            return i;
        }

        // вставка: добавляем в конец и просеиваем вверх
        void insert(Long value) {
            heap.add(value);
            siftUp(heap.size() - 1);
        }

        // извлечение максимума: корень — это максимум;
        // на его место ставим последний элемент и просеиваем вниз
        Long extractMax() {
            if (heap.isEmpty()) return null;
            Long result = heap.get(0);
            Long last = heap.remove(heap.size() - 1);
            if (!heap.isEmpty()) {
                heap.set(0, last);
                siftDown(0);
            }
            return result;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }
}
