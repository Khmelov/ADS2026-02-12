package by.it.group551002.puteev.lesson04;

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
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }
        a = mergeSort(a,0,n-1);
        return a;
    }

    private int[] mergeSort(int[] a, int left, int right){
        if (left == right) {
            return new int[]{a[left]};
        }
        int mid = left+(right-left)/2;

        int[] leftPart = mergeSort(a,left,mid);
        int[] rightPart = mergeSort(a,mid+1,right);

        return merge(leftPart,rightPart);
    }

    private int[] merge(int[] left, int[] right){
        int[] result = new int[left.length+ right.length];
        int i=0,j=0,k=0;
        while(i < left.length && j < right.length){
            if (left[i] <= right[j]){
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }

        while(i < left.length) result[k++] = left[i++];
        while(j < right.length) result[k++] = right[j++];

        return  result;
    }

}
