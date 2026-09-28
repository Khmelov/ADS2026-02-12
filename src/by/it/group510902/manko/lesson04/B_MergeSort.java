package by.it.group510902.manko.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Реализуйте сортировку слиянием для одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо отсортировать полученный массив.

Sample Input:
5
2 3 9 2 9
Sample Output:
2 2 3 9 9
*/

public class B_MergeSort {

    private static int[] MyMergeSort(int[] arr){
        int[] a, b;
        int n = arr.length;
        a = new int[n / 2];
        b = new int[(n + 1) / 2];
        for(int i = 0; i < n / 2; i++) {
            a[i] = arr[i];
            b[i] = arr[n / 2 + i];
        }
        if(n % 2 == 1){
            b[(n + 1) / 2 - 1] = arr[n - 1];
        }
        if(a.length > 1){
            a = MyMergeSort(a);
        }
        if(b.length > 1){
            b = MyMergeSort(b);
        }
        int uka = 0, ukb = 0;
        for(int i = 0; i < n; i++){
            int cur;
            if(a.length <= uka){
                cur = b[ukb];
                ukb++;
            }else if(b.length <= ukb){
                cur = a[uka];
                uka++;
            }else{
                if(a[uka] < b[ukb]){
                    cur = a[uka];
                    uka++;
                }else{
                    cur = b[ukb];
                    ukb++;
                }
            }
            arr[i] = cur;
        }
        return arr;
    }

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
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }

        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием

        a = MyMergeSort(a);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
