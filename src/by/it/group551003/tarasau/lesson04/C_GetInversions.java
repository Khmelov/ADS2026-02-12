package by.it.group551003.tarasau.lesson04;

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

        result = mergeSortForSure(a, 0, a.length-1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    int mergeSortForSure(int[] arr, int begin, int end) {
        int inverseCount = 0;

        if (begin >= end) {
            return 0;
        }

        int mid = begin + (end - begin) / 2;
        inverseCount += mergeSortForSure(arr, begin, mid);
        inverseCount += mergeSortForSure(arr, mid+1, end);
        inverseCount += mergeTheFellas(arr, begin, mid, end);

        return inverseCount;
    }

    int mergeTheFellas(int[] arr, int begin, int mid, int end) {
        int inverseCount = 0;
        int n1 = mid - begin + 1;
        int n2 = end - mid;

        int[] left = new int[n1];
        int[] right = new int[n2];

        for (int i = 0; i < n1; i++) {
            left[i] = arr[begin+i];
        }

        for (int j = 0; j < n2; j++) {
            right[j] = arr[mid + j + 1];
        }

        int i = 0;
        int j = 0;
        int k = begin;

        while (i < n1 && i < n2) {
            if (left[i] <= right[j]) {
                arr[k] = left[i];
                ++i;
            } else {
                arr[k] = right[j];
                ++j;
                inverseCount += (n1 - i);
            }

            ++k;
        }

        while (i < n1) {
            arr[k] = left[i];
            ++i;
            ++k;
        }

        while (j < n2) {
            arr[k] = right[j];
            ++j;
            ++k;
        }

        return inverseCount;
    }
}
