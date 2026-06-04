package by.it.group551002.loiko.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Рассчитать число инверсий одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо посчитать число пар индексов 1<=i<j<n, для которых A[i]>A[j].

    (Такая пара элементов называется инверсией массива.
    Количество инверсий в массиве является в некотором смысле
    его мерой неупорядоченности: например, в упорядоченном по неубыванию
    массиве инверсий нет вообще, а в массиве, упорядоченном по убыванию,
    инверсию образуют каждые (т.е. любые) два элемента.
    )

Sample Input:
5
2 3 9 2 9
Sample Output:
2

Головоломка (т.е. не обязательно).
Попробуйте обеспечить скорость лучше, чем O(n log n) за счет многопоточности.
Докажите рост производительности замерами времени.
Большой тестовый массив можно прочитать свой или сгенерировать его программно.
*/


public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!
        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int result = 0;
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!
        // вызываем сортировку слиянием с подсчётом инверсий
        result = mergeSortAndCount(a, 0, a.length - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // рекурсивная сортировка слиянием с подсчётом инверсий
    private int mergeSortAndCount(int[] array, int left, int right) {
        int count = 0;
        if (left < right) {
            int mid = (left + right) / 2;

            // считаем инверсии в левой половине
            count += mergeSortAndCount(array, left, mid);
            // считаем инверсии в правой половине
            count += mergeSortAndCount(array, mid + 1, right);
            // считаем инверсии при слиянии
            count += mergeAndCount(array, left, mid, right);
        }
        return count;
    }

    // слияние двух отсортированных частей и подсчёт инверсий
    private int mergeAndCount(int[] array, int left, int mid, int right) {
        // размеры левой и правой половин
        int leftSize = mid - left + 1;
        int rightSize = right - mid;

        // создаём временные массивы
        int[] leftArray = new int[leftSize];
        int[] rightArray = new int[rightSize];

        // копируем данные во временные массивы
        for (int i = 0; i < leftSize; i++) {
            leftArray[i] = array[left + i];
        }
        for (int j = 0; j < rightSize; j++) {
            rightArray[j] = array[mid + 1 + j];
        }

        // слияние и подсчёт инверсий
        int i = 0;      // индекс в leftArray
        int j = 0;      // индекс в rightArray
        int k = left;   // индекс в исходном массиве
        int swaps = 0;  // счётчик инверсий

        while (i < leftSize && j < rightSize) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
                // ВСЕ оставшиеся элементы в leftArray больше чем rightArray[j]
                // потому что leftArray отсортирован!
                swaps += leftSize - i;
            }
            k++;
        }

        // копируем оставшиеся элементы из левого массива (если есть)
        while (i < leftSize) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        // копируем оставшиеся элементы из правого массива (если есть)
        while (j < rightSize) {
            array[k] = rightArray[j];
            j++;
            k++;
        }

        return swaps;
    }
}
