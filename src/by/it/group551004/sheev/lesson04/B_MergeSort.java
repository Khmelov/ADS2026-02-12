package by.it.group551004.sheev.lesson04;

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
        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием
        mergeSort(a, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }
    // Рекурсия
    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;// Находим середину
            mergeSort(array, left, mid);// Рекурсивно сортируем левую
            mergeSort(array, mid + 1, right);// Сортируем правую
            merge(array, left, mid, right);// Сливаю отсортированные половины
        }
    }
    // Слияния двух подмассивов
    private void merge(int[] array, int left, int mid, int right) {
        int leftSize = mid - left + 1;// Размеры массивов
        int rightSize = right - mid;

        int[] leftArray = new int[leftSize]; // Создаю временные массивы
        int[] rightArray = new int[rightSize];

        for (int i = 0; i < leftSize; i++) {// Копирую данные
            leftArray[i] = array[left + i];
        }
        for (int j = 0; j < rightSize; j++) {
            rightArray[j] = array[mid + 1 + j];
        }
        // Слияние
        int i = 0;
        int j = 0;
        int k = left;

        while (i < leftSize && j < rightSize) {// Сравниваю элементы и вставляю мин
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }
        while (i < leftSize) {// Копи левт
            array[k] = leftArray[i];
            i++;
            k++;
        }
        while (j < rightSize) {// Копи райт
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }

}
