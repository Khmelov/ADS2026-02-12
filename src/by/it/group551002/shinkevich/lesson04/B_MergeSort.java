package by.it.group551002.shinkevich.lesson04;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }

		mergeSort(a, 0, n-1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

	private void mergeSort(int[] a, int left, int right) {
		if (left < right) {
			int mid = left + (right - left) / 2;

			mergeSort(a, left, mid);
			mergeSort(a, mid + 1, right);

			merge(a, left, mid, right);
		}
	}

	private void merge(int[] a, int left, int mid, int right) {
		int[] L = java.util.Arrays.copyOfRange(a, left, mid + 1);
		int[] R = java.util.Arrays.copyOfRange(a, mid + 1, right + 1);

		int i = 0;     // Index for L
		int j = 0;     // Index for R
		int k = left;  // Index for a

		while (i < L.length && j < R.length) {
			if (L[i] <= R[j]) {
				a[k] = L[i]; i++;
			} else {
				a[k] = R[j]; j++;
			}

			k++;
		}

		while (i < L.length) { a[k] = L[i]; i++; k++; }
		while (j < R.length) { a[k] = R[j]; j++; k++; }
	}

}
