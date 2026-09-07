package by.it.group551001.Barusevich.lesson01.lesson03;

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
        private List<Long> heap = new ArrayList<>();

        // siftUp: поднимает элемент вверх, пока он больше родителя
        int siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap.get(i).compareTo(heap.get(parent)) <= 0)
                    break;
                // меняем местами
                Long tmp = heap.get(i);
                heap.set(i, heap.get(parent));
                heap.set(parent, tmp);
                i = parent;
            }
            return i;
        }

        // siftDown: просеивает элемент вниз, пока он меньше одного из детей
        int siftDown(int i) {
            int size = heap.size();
            while (true) {
                int left = 2 * i + 1;
                int right = 2 * i + 2;
                int largest = i;
                if (left < size && heap.get(left).compareTo(heap.get(largest)) > 0)
                    largest = left;
                if (right < size && heap.get(right).compareTo(heap.get(largest)) > 0)
                    largest = right;
                if (largest == i)
                    break;
                // меняем местами
                Long tmp = heap.get(i);
                heap.set(i, heap.get(largest));
                heap.set(largest, tmp);
                i = largest;
            }
            return i;
        }

        void insert(Long value) {
            heap.add(value);
            siftUp(heap.size() - 1);
        }

        Long extractMax() {
            if (heap.isEmpty())
                return null;
            Long max = heap.get(0);
            int lastIndex = heap.size() - 1;
            // переносим последний элемент в корень
            heap.set(0, heap.get(lastIndex));
            heap.remove(lastIndex);
            if (!heap.isEmpty())
                siftDown(0);
            return max;
        }
    }
}