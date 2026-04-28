package by.it.group510901.chernyavskaya.lesson04;

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
        // подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // размер массива
        int n = scanner.nextInt();
        // сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }

        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием

        // Вызываем сортировку слиянием для всего массива
        mergeSort(a, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    // Главная функция сортировки слиянием, которая рекурсивно делит массив
    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            // Находим среднюю точку
            int middle = left + (right - left) / 2;

            // Сортируем первую и вторую половины
            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);

            // Сливаем отсортированные половины
            merge(array, left, middle, right);
        }
    }

    // Сливает два подмассива
    private void merge(int[] array, int left, int middle, int right) {
        // Размеры двух подмассивов для слияния
        int leftSize = middle - left + 1;
        int rightSize = right - middle;

        // Создаём временные массивы
        int[] leftArray = new int[leftSize];
        int[] rightArray = new int[rightSize];

        // Копируем данные во временные массивы
        for (int i = 0; i < leftSize; i++) {
            leftArray[i] = array[left + i];
        }
        for (int j = 0; j < rightSize; j++) {
            rightArray[j] = array[middle + 1 + j];
        }

        // Сливаем временные массивы обратно в array[left..right]
        int i = 0; // Начальный индекс левого подмассива
        int j = 0; // Начальный индекс правого подмассива
        int k = left; // Начальный индекс сливаемого подмассива

        while (i < leftSize && j < rightSize) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        // Копируем оставшиеся элементы левого массива, если они есть
        while (i < leftSize) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        // Копируем оставшиеся элементы правого массива, если они есть
        while (j < rightSize) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
}