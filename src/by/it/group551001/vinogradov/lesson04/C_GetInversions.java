package by.it.group551001.vinogradov.lesson04;

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
    int merge(int[] s, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        int cnt = 0;
        int[] left = new int[n1]; int[] right = new int[n2];
        for (int i = 0; i < n1; i++) {
            left[i] = s[l + i];
        }
        for (int i = 0; i < n2; i++) {
            right[i] = s[m + 1 + i];
        }
        int i = 0;
        int j = 0;
        int k = l;
        while ((i < n1) && (j < n2)) {
            if (left[i] <= right[j]) {
                s[k] = left[i];
                i++;
            }
            else {
                cnt+= n1 - i;
                s[k] = right[j];
                j++;
            }
            k++;
        }
        while (i < n1) {
            s[k] = left[i];
            i++;
            k++;
        }
        while (j < n2) {
            s[k] = right[j];
            j++;
            k++;
        }
        return cnt;
    }

    int mergeSort(int[] s, int l, int r) {
        int cnt = 0;
        if (l < r) {
            int m = l + (r - l) / 2;
            cnt += mergeSort(s, l, m);
            cnt += mergeSort(s, m + 1, r);
            cnt += merge(s, l, m, r);
        }
        return cnt;
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

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return mergeSort(a, 0, n-1);
    }
}
