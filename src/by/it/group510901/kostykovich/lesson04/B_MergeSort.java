package by.it.group510901.kostykovich.lesson04;

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

        MergeSort(a);
        for (int i = 0; i < n; i++) {
            System.out.println(a[i]);
        }
        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    private void MergeSort(int[] arr){
       int arrayLength = arr.length;
       int midIndex = arr.length/2;
       int[] leftHalf = new int[midIndex];
       int[] rightHalf = new int[arr.length - midIndex];
       if(leftHalf.length == 0 || rightHalf.length == 0){
           return;
       }

        for(int i = 0; i < midIndex; i++){
            leftHalf[i] = arr[i];
        }
        for(int i = midIndex; i < arr.length; i++){
            rightHalf[i-midIndex] = arr[i];
        }

        MergeSort(leftHalf);
        MergeSort(rightHalf);
        Merge(arr, leftHalf, rightHalf);
    }

    private static void Merge(int[] arr, int[] leftHalf, int[] rightHalf){
            int leftSize = leftHalf.length;
            int rightSize = rightHalf.length;
            int i = 0, j = 0, k = 0;
            while(i < leftSize && j < rightSize){
                if(leftHalf[i] <= rightHalf[j]){
                    arr[k] = leftHalf[i];
                    i++;
                }else{
                    arr[k] = rightHalf[j];
                    j++;
                }
                k++;
            }
            while(i < leftSize){
                arr[k] = leftHalf[i];
                i++;
                k++;
            }
            while(j < rightSize){
                arr[k] = rightHalf[j];
                j++;
                k++;
            }
    }

}
