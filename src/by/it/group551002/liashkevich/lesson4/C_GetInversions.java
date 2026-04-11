package by.it.group551002.liashkevich.lesson4;

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
        result = mergeSort(a, 0, n-1);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    int merge(int[] Arr, int Left, int Median, int Right) {
        int i, j, k;
        int inversion = 0;
        int n1 = Median - Left + 1;
        int n2 = Right - Median;

        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];

        for (i = 0; i < n1; i++)
            leftArr[i] = Arr[Left + i];
        for (j = 0; j < n2; j++)
            rightArr[j] = Arr[Median + 1 + j];

        i = 0;
        j = 0;
        k = Left;
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                Arr[k] = leftArr[i];
                i++;
            }
            else {
                inversion += n1 - i;
                Arr[k] = rightArr[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            Arr[k] = leftArr[i];
            i++;
            k++;
        }

        while (j < n2) {
            Arr[k] = rightArr[j];
            j++;
            k++;
        }

        return inversion;

    }

    int mergeSort(int[] Arr, int Left, int Right) {
        int inversion = 0;
        if (Left < Right) {
            int Median = Left + (Right - Left) / 2;
            inversion += mergeSort(Arr, Left, Median);
            inversion += mergeSort(Arr, Median + 1, Right);
            inversion += merge(Arr, Left, Median, Right);
        }
        return inversion;
    }

}
