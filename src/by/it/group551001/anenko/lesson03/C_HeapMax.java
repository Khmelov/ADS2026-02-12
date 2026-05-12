package by.it.group551001.anenko.lesson03;

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
        if (!scanner.hasNextInt()) return 0L;
        Integer count = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < count; ) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null && res > maxValue) maxValue = res;
                System.out.println(res);
                i++;
            } else if (s.toLowerCase().startsWith("insert")) {
                String[] p = s.split(" ");
                if (p.length > 1) {
                    heap.insert(Long.parseLong(p[1]));
                }
                i++;
            }
        }
        return maxValue;
    }

    private class MaxHeap {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        private List<Long> heap = new ArrayList<>();

        int siftDown(int i) {
            while (2 * i + 1 < heap.size()) {
                int leftChild = 2 * i + 1;
                int rightChild = 2 * i + 2;
                int largestChild = leftChild;

                if (rightChild < heap.size() && heap.get(rightChild) > heap.get(leftChild)) {
                    largestChild = rightChild;
                }

                if (heap.get(i) >= heap.get(largestChild)) {
                    break;
                }

                Long temp = heap.get(i);
                heap.set(i, heap.get(largestChild));
                heap.set(largestChild, temp);
                i = largestChild;
            }
            return i;
        }

        int siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap.get(i) <= heap.get(parent)) {
                    break;
                }
                Long temp = heap.get(i);
                heap.set(i, heap.get(parent));
                heap.set(parent, temp);
                i = parent;
            }
            return i;
        }

        void insert(Long value) {
            heap.add(value);
            siftUp(heap.size() - 1);
        }

        Long extractMax() {
            if (heap.isEmpty()) return null;
            Long result = heap.get(0);

            Long lastElement = heap.remove(heap.size() - 1);
            if (!heap.isEmpty()) {
                heap.set(0, lastElement);
                siftDown(0);
            }

            return result;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }
}