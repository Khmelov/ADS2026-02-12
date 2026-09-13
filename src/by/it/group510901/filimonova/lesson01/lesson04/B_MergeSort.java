package by.it.group510901.filimonova.lesson01.lesson04;

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

        // вызываем сортировку слиянием
        mergeSort(a, 0, a.length - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    // рекурсивная сортировка слиянием
    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            // находим середину
            int mid = left + (right - left) / 2;

            // рекурсивно сортируем левую и правую половины
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);

            // сливаем отсортированные половины
            merge(array, left, mid, right);
        }
    }

    // слияние двух отсортированных подмассивов
    private void merge(int[] array, int left, int mid, int right) {
        // размеры временных массивов
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // создаем временные массивы
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // копируем данные во временные массивы
        for (int i = 0; i < n1; i++) {
            leftArray[i] = array[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = array[mid + 1 + j];
        }

        // индексы для слияния
        int i = 0; // индекс левого массива
        int j = 0; // индекс правого массива
        int k = left; // индекс результирующего массива

        // сливаем массивы
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

        // копируем оставшиеся элементы левого массива (если есть)
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        // копируем оставшиеся элементы правого массива (если есть)
        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
}
/*1. Разделение (Divide): рекурсивно делим массив пополам, пока не останутся подмассивы из одного элемента

2. Слияние (Merge):
   - Создаем два временных массива (левая и правая половины)
   - Сравниваем элементы из обоих массивов
   - Берем меньший и помещаем в исходный массив
   - Добавляем оставшиеся элементы

Сложность: O(n log n) во всех случаях
Память: O(n) для временных массивов*/