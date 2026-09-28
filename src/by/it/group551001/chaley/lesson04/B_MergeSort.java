package by.it.group551001.chaley.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        //long startTime = System.currentTimeMillis();
        int[] result = instance.getMergeSort(stream);
        //long finishTime = System.currentTimeMillis();
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();

        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }
        a = mergeSort(a);
        return a;
    }

    int[] mergeSort(int[] a){
        if(a.length <= 1) return a;
        int[] b = new int[a.length/2];
        for(int i=0; i < b.length; i++)
            b[i] = a[i];
        int[] c = new int[a.length - a.length/2];
        for(int i=0; i < c.length; i++){
            c[i] = a[i+b.length];
        }
        b = mergeSort(b);
        c = mergeSort(c);

        return mergeArray(b, c);
    }

    int[] mergeArray(int[] a, int[] b){
        int[] c = new int[a.length + b.length];
        int posA = 0, posB = 0;
        for (int i=0; i < c.length; i++){
            if(posA == a.length){
                c[i] = b[posB];
                posB++;
            } else if(posB == b.length){
                c[i] = a[posA];
                posA++;
            } else if (a[posA] < b[posB]){
                c[i] = a[posA];
                posA++;
            } else {
                c[i] = b[posB];
                posB++;
            }
        }
        return c;
    }


}
