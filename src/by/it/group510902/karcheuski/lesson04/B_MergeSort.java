package by.it.group510902.karcheuski.lesson04;

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
        int[] temp = new int[n];

        // Будем сливать подмассивы размером size, затем удваивать size
        for (int size = 1; size < n; size *= 2) {
            // Проходим по массиву с шагом size*2
            for (int leftStart = 0; leftStart < n - size; leftStart += 2 * size) {
                // Определяем границы сливаемых подмассивов
                int left = leftStart;
                int mid = leftStart + size - 1;
                int right = Math.min(leftStart + 2 * size - 1, n - 1);

                // Слияние двух отсортированных подмассивов: [left..mid] и [mid+1..right]
                int i = left;
                int j = mid + 1;
                int k = left;

                // Копируем во временный массив
                while (i <= mid && j <= right) {
                    if (a[i] <= a[j]) {
                        temp[k] = a[i];
                        i++;
                    } else {
                        temp[k] = a[j];
                        j++;
                    }
                    k++;
                }

                // Копируем остатки левой части
                while (i <= mid) {
                    temp[k] = a[i];
                    i++;
                    k++;
                }

                // Копируем остатки правой части
                while (j <= right) {
                    temp[k] = a[j];
                    j++;
                    k++;
                }

                // Копируем обратно из временного массива в исходный
                for (int t = left; t <= right; t++) {
                    a[t] = temp[t];
                }
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
