package by.it.group510902.ryzhkov.lesson04;

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
        // long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        // long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    private static int mergeSort(int[] arr, int l, int r) {
        int inv = 0;
        if (l < r) {
            inv = mergeSort(arr, l, (l + r) / 2);
            inv += mergeSort(arr, (l + r) / 2 + 1, r);

            inv += merge(arr, l, (l + r) / 2, r);
        }
        return inv;
    }

    private static int merge(int[] arr, int l, int mid, int r) {
        int n1 = mid - l + 1;
        int n2 = r - mid;

        int[] LArr = new int[n1];
        int[] RArr = new int[n2];

        System.arraycopy(arr, l, LArr, 0, n1);
        System.arraycopy(arr, mid + 1, RArr, 0, n2);

        int i = 0;
        int j = 0;
        int k = l;
        int inv = 0;
        while (i < n1 && j < n2) {
            if (LArr[i] <= RArr[j]) {
                arr[k] = LArr[i];
                i++;
            } else {
                arr[k] = RArr[j];
                j++;
                inv += (n1 - i);
            }
            k++;
        }

        while (i < n1) {
            arr[k] = LArr[i];
            i++;
            k++;
        }
        while (j < n2) {
            arr[k] = RArr[j];
            j++;
            k++;
        }

        return inv;
    }

    int calc(InputStream stream) throws FileNotFoundException {
        // подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        // !!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!
        // размер массива
        int n = scanner.nextInt();
        // сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int result = 0;
        // !!!!!!!!!!!!!!!!!!!!!!!! тут ваше решение !!!!!!!!!!!!!!!!!!!!!!!!
        // int l = 0;
        // while (l < n) {
        // for (int i = l + 1; i < n; i++) {
        // if (a[i] < a[l]) {
        // result++;
        // }
        // }
        // l++;
        // }

        result = mergeSort(a, 0, n - 1);
        // !!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}
