package by.it.group510901.blyshko.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;


public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        
        int[] result = instance.getMergeSort(stream);
        
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
        }

        
        
        mergeSort(a, new int[n], 0, n);

        
        return a;
    }

    private void mergeSort(int[] a, int[] temp, int left, int right) {
        if (right - left < 2) {
            return;
        }
        int middle = left + (right - left) / 2;
        mergeSort(a, temp, left, middle);
        mergeSort(a, temp, middle, right);
        merge(a, temp, left, middle, right);
    }

    private void merge(int[] a, int[] temp, int left, int middle, int right) {
        int i = left;
        int j = middle;
        int k = left;
        while (i < middle && j < right) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++];
            } else {
                temp[k++] = a[j++];
            }
        }
        while (i < middle) {
            temp[k++] = a[i++];
        }
        while (j < right) {
            temp[k++] = a[j++];
        }
        for (int index = left; index < right; index++) {
            a[index] = temp[index];
        }
    }

}
