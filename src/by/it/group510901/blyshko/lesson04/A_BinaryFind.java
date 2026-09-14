package by.it.group510901.blyshko.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;



public class A_BinaryFind {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_BinaryFind.class.getResourceAsStream("dataA.txt");
        A_BinaryFind instance = new A_BinaryFind();
        
        int[] result = instance.findIndex(stream);
        
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    public int[] findIndex(InputStream stream) throws FileNotFoundException {
        
        Scanner scanner = new Scanner(stream);
        

        
        int n = scanner.nextInt();
        
        int[] a = new int[n];
        for (int i = 1; i <= n; i++) {
            a[i - 1] = scanner.nextInt();
        }

        
        int k = scanner.nextInt();
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt();
            
            int left = 0;
            int right = n - 1;
            int index = -1;
            while (left <= right) {
                int middle = left + (right - left) / 2;
                if (a[middle] == value) {
                    index = middle + 1;
                    break;
                }
                if (a[middle] < value) {
                    left = middle + 1;
                } else {
                    right = middle - 1;
                }
            }
            result[i] = index;
        }
        return result;
    }

}
