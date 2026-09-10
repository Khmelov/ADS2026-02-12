package by.it.group510901.blyshko.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;




public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        
        int result = instance.calc(stream);
        
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        
        Scanner scanner = new Scanner(stream);
        
        
        int n = scanner.nextInt();
        
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int result = 0;
        
        result = (int) mergeSortAndCount(a, new int[n], 0, n);

        
        return result;
    }

    private long mergeSortAndCount(int[] a, int[] temp, int left, int right) {
        if (right - left < 2) {
            return 0;
        }
        int middle = left + (right - left) / 2;
        long result = mergeSortAndCount(a, temp, left, middle);
        result += mergeSortAndCount(a, temp, middle, right);
        result += mergeAndCount(a, temp, left, middle, right);
        return result;
    }

    private long mergeAndCount(int[] a, int[] temp, int left, int middle, int right) {
        long result = 0;
        int i = left;
        int j = middle;
        int k = left;
        while (i < middle && j < right) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++];
            } else {
                temp[k++] = a[j++];
                result += middle - i;
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
        return result;
    }
}
