package by.it.group510902.mikhnovets.lesson04;

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
        mergeSort(a, 0, n - 1);

        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }
    private void mergeSort(int[] arr, int left, int right) {
        if(left < right) {
            int middle = (left + right) / 2;

            mergeSort(arr, left, middle);
            mergeSort(arr, middle +1, right);
            merge(arr, left, middle, right);


        }
    }
    private void merge(int[] arr, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;
        int[] arrLeft= new int[n1];
        int[] arrRight = new int[n2];
        for(int i = 0; i < n1; i++) {
            arrLeft[i] = arr[i];
        }
        for(int j = 0; j < n2; j++) {
            arrRight[j] = arr[middle + 1 + j ];
        }
        int i = 0;
        int j = 0;
        int c = 0;
        while(i < n1 && j < n2) {
            if(arrLeft[i] > arrRight[j]) {
                arr[c] = arrRight[j];
                j++;
            }
            else{
                arr[c] = arrLeft[i];
                i++;
            }
            c++;
        }
        while(i < n1) {
            arr[c] = arrLeft[i];
            c++;
            i++;
        }
        while(j < n2) {
            arr[c] = arrRight[j];
            c++;
            j++;
        }
    }




}
