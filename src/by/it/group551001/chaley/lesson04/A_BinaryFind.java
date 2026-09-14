package by.it.group551001.chaley.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_BinaryFind {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_BinaryFind.class.getResourceAsStream("dataA.txt");
        A_BinaryFind instance = new A_BinaryFind();
        //long startTime = System.currentTimeMillis();
        int[] result = instance.findIndex(stream);
        //long finishTime = System.currentTimeMillis();
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
            int l = 0;
            int r = n-1;
            result[i] = -1;
            while(l <= r){
                int index = (l+r)/2;
                if(a[index] == value){
                    result[i] = index+1;
                    break;
                }
                if(a[index] > value) r = index-1;
                if(a[index] < value) l = index+1;
            }
        }
        return result;
    }
}
