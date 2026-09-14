package by.it.group551002.rudominskaya.lesson04;

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
        }

        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием

        // Вызываем сортировку слиянием
        mergeSort(a, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    // Рекурсивный метод сортировки слиянием
    private void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            // Находим середину массива
            int mid = left + (right - left) / 2;

            // Рекурсивно сортируем левую половину
            mergeSort(arr, left, mid);

            // Рекурсивно сортируем правую половину
            mergeSort(arr, mid + 1, right);

            // Сливаем отсортированные половины
            merge(arr, left, mid, right);
        }
    }

    // Метод слияния двух отсортированных подмассивов
    private void merge(int[] arr, int left, int mid, int right) {
        // Размеры временных массивов
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Создаем временные массивы
        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];

        // Копируем данные во временные массивы
        for (int i = 0; i < n1; i++) {
            leftArr[i] = arr[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArr[j] = arr[mid + 1 + j];
        }

        // Слияние временных массивов обратно в основной
        int i = 0, j = 0;  // Индексы для leftArr и rightArr
        int k = left;      // Индекс для основного массива

        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }

        // Копируем оставшиеся элементы из leftArr (если есть)
        while (i < n1) {
            arr[k] = leftArr[i];
            i++;
            k++;
        }

        // Копируем оставшиеся элементы из rightArr (если есть)
        while (j < n2) {
            arr[k] = rightArr[j];
            j++;
            k++;
        }
    }
}