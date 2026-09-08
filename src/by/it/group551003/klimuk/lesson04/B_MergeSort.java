package by.it.group551003.klimuk.lesson04;

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

        a = mergeSort(a,0,n-1);
        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }
    int[] mergeSort(int arr[], int l, int r){
        int[] resarr = new int[r-l+1];
        if(l == r){
            resarr[0] = arr[l];
            return arr;
        }
        int m = (l + r)/2;
        int[] l_buf = mergeSort(arr, l, m);
        int[] r_buf = mergeSort(arr, m+1, r);
        int i = 0, k = 0, j = 0;
        while(i <= (r - l)/2 && j < (r - l)/2 && k < r-l+1){
            if (l_buf[i] > r_buf[j]){
                resarr[k] = r_buf[j];
                j++;
            }else if(l_buf[i] < r_buf[j]){
                resarr[k] = l_buf[i];
                i++;
            }else{
                resarr[k] = l_buf[i];
                i++;
                j++;
            }
            k++;
        }
        return resarr;
    }

}
