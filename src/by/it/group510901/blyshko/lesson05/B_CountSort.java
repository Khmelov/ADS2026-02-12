package by.it.group510901.blyshko.lesson05;

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

        
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }
        
        int[] count = new int[11];
        for (int point : points) {
            count[point]++;
        }
        int index = 0;
        for (int value = 0; value < count.length; value++) {
            while (count[value] > 0) {
                points[index++] = value;
                count[value]--;
            }
        }

        
        return points;
    }

}
