package by.it.group551004.kondratova.lesson04;

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
Сортировка слиянием.
1 Если массив из 1 элемента → он уже отсортирован
2 Делим массив на две половины
3 Рекурсивно сортируем левую половину
4 Рекурсивно сортируем правую половину
5 Сливаем две отсортированные половины в одну
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

    // Рекурсивная сортировка слиянием
    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            // Сортируем левую и правую половины
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);

            // Сливаем отсортированные половины
            merge(array, left, mid, right);
        }
    }

    // Слияние двух отсортированных половин
    private void merge(int[] array, int left, int mid, int right) {
        // Размеры временных массивов
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Создаём временные массивы
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // Копируем данные во временные массивы
        for (int i = 0; i < n1; i++) {
            leftArray[i] = array[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = array[mid + 1 + j];
        }

        // Слияние временных массивов обратно в основной
        int i = 0, j = 0;
        int k = left;

        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        // Копируем оставшиеся элементы из leftArray
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        // Копируем оставшиеся элементы из rightArray
        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
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
        }

        // сортировка слиянием
        mergeSort(a, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }
}