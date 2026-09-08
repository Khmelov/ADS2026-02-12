package by.it.group510901.nekhviadovich.lesson04;

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

        int[] buffer = new int[n];

        sort(a, buffer, 0, a.length-1);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    void sort(int[] arr, int[] buffer, int left, int right){

        if(right <= left){
            return;
        }

        int mid= (left + right)/2;

        sort(arr, buffer, left, mid );

        sort(arr, buffer, mid + 1, right);

        merge(arr, buffer, left, mid, right );

    }

    void merge(int[] arr, int[] buffer, int left, int mid, int right){

        for (int i = left; i <= right; i++) {
            buffer[i] = arr[i];
        }

        int k = left;
        int i = left;
        int j = mid +1;

        while(i<= mid && j <= right){
            if(buffer[i] > buffer[j]){
                arr[k] = buffer[j];
                k++;
                j++;
            }
            else{
                arr[k] = buffer[i];
                k++;
                i++;
            }
        }

        while(i <= mid){
            arr[k] = buffer[i];
            k++;
            i++;
        }
        while(j <= right){
            arr[k] = buffer[j];
            k++;
            j++;
        }
    }

}
