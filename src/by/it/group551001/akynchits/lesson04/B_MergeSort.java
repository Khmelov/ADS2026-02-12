package by.it.group551001.akynchits.lesson04;

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
    void merge(int[] array, int left, int right, int mid){
        int[] LowHalf = new int[mid - left + 1];
        int[] HighHalf = new int[right - mid];
        for(int i = 0; i < mid - left + 1; i++){
            LowHalf[i] = array[left + i];
        }
        for(int j = 0; j < right - mid; j++){
            HighHalf[j] = array[mid + 1 + j];
        }
        int k = left; 
        int i = 0;
        int j = 0;
        while(i < LowHalf.length && j < HighHalf.length){
            if (LowHalf[i] > HighHalf[j]){
                array[k] = HighHalf[j];
                k++;
                j++;
            } else  {
                array[k] = LowHalf[i];
                k++;
                i++;
            }
        }
        while(i < LowHalf.length){
            array[k] = LowHalf[i];
            k++;
            i++;
        }
        while(j < HighHalf.length){
            array[k] = HighHalf[j];
            k++;
            j++;
        }
    }

    int[] mergesort(int[] array,int p,int r){
        if (p < r){
            int q = (p+r) / 2;
            mergesort(array,p, q);
            mergesort(array, q+1, r);
            merge(array, p, r, q);
        }
        return array;
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

        a = mergesort(a, 0, n - 1);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
