package by.it.group551003.meshchenko.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_CountSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] points = new int[n];

        // read numbers (all in 1..10)
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // counting sort: values are from 1 to 10
        int[] count = new int[11];  // index 0 unused, 1..10
        for (int v : points) {
            count[v]++;
        }

        int idx = 0;
        for (int v = 1; v <= 10; v++) {
            for (int k = 0; k < count[v]; k++) {
                points[idx++] = v;
            }
        }
        return points;
    }
}