package by.it.group551004.gavrilovroman.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;


public class C_GetInversions {

    int inv;

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
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
        inv = 0;
        mergeSort(a);
        result = inv;

        return result;
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

        return modMergeArray(b, c);
    }

    int[] modMergeArray(int[] a, int[] b){
        int[] c = new int[a.length + b.length];
        int posA = 0, posB = 0;
        for (int i=0; i < c.length; i++){
            if(posA == a.length){
                c[i] = b[posB];
                posB++;
            } else if(posB == b.length){
                c[i] = a[posA];
                posA++;
            } else if (a[posA] <= b[posB]){
                c[i] = a[posA];
                posA++;
            } else {
                c[i] = b[posB];
                posB++;
                inv++;
            }
        }
        return c;
    }
}
