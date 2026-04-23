package by.it.group551003.dobrolovich.lesson04;

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

    void sort(int a[], int left, int right, int[] swaps){
        if (left >= right) return;
        int mid = left + (right - left)/2;
        sort (a, left, mid, swaps);
        sort (a, mid + 1, right, swaps);
        merge(a, left, right, swaps);
    }

    void merge(int a[], int left, int right, int[] swaps) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        int L[] = new int[mid - left + 1];
        int R[] = new int[right - mid];

        for (int i = 0; i < L.length; i++) L[i] = a[i + left];
        for (int i = 0; i < R.length; i++) R[i] = a[i + mid + 1];

        int li = 0, ri = 0, i = left;
        while (li < L.length || ri < R.length) {
            if (li < L.length && ri < R.length) {
                if (L[li] <= R[ri]) a[i++] =  L[li++];
                else {
                    a[i++] = R[ri++];
                    swaps[0] += (L.length - li);
                }
            }
            else if (li < L.length) a[i++] =  L[li++];
            else a[i++] =  R[ri++];
        }
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
        int[] swaps = new int[1];
        swaps[0] = 0;
        sort(a, 0, n - 1, swaps);
        result = swaps[0];
        return result;
    }
}
