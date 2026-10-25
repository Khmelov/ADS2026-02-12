package by.it.group510901.kachur.lesson01.lesson04;

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

    // Рекурсивная сортировка слиянием
    private void mergeSort(int[] array, int left, int right) {
        // Базовый случай: если остался один элемент - он уже отсортирован
        if (left >= right) {
            return;
        }

        // Находим середину
        int middle = left + (right - left) / 2;

        // Сортируем левую половину
        mergeSort(array, left, middle);

        // Сортируем правую половину
        mergeSort(array, middle + 1, right);

        // Сливаем две отсортированные половины
        merge(array, left, middle, right);
    }

    // Слияние двух отсортированных половин
    private void merge(int[] array, int left, int middle, int right) {
        // Создаем временный массив для результата слияния
        int[] temp = new int[right - left + 1];

        // Индексы для левой и правой половин
        int leftIndex = left;        // указатель на левую половину
        int rightIndex = middle + 1; // указатель на правую половину
        int tempIndex = 0;           // указатель во временном массиве

        // Сравниваем элементы из левой и правой половин и записываем меньший
        while (leftIndex <= middle && rightIndex <= right) {
            if (array[leftIndex] <= array[rightIndex]) {
                temp[tempIndex] = array[leftIndex];
                leftIndex++;
            } else {
                temp[tempIndex] = array[rightIndex];
                rightIndex++;
            }
            tempIndex++;
        }

        // Если в левой половине остались элементы - добавляем их
        while (leftIndex <= middle) {
            temp[tempIndex] = array[leftIndex];
            leftIndex++;
            tempIndex++;
        }

        // Если в правой половине остались элементы - добавляем их
        while (rightIndex <= right) {
            temp[tempIndex] = array[rightIndex];
            rightIndex++;
            tempIndex++;
        }

        // Копируем отсортированные элементы обратно в исходный массив
        for (int i = 0; i < temp.length; i++) {
            array[left + i] = temp[i];
        }
    }
}