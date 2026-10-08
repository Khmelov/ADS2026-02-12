package by.it.group551002.kubarko.lesson04;

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

        result = mergeCount(a, 0, a.length-1);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    int mergeCount(int arr[], int l, int r)
    {
        int inv = 0;
        if (l < r)
        {
            int m = l + (r - l) / 2;

            inv += mergeCount(arr, l, m);
            inv += mergeCount(arr, m + 1, r);

            inv += merge(arr, l, m, r);
        }
        return inv;
    }

    int merge(int arr[], int l, int m, int r)
    {
        int sizeL = m-l+1;
        int sizeR = r-m;

        int[] L = new int[sizeL];
        int[] R = new int[sizeR];


        for (int i = 0; i < sizeL; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < sizeR; ++j)
            R[j] = arr[m + 1 + j];

        int i = 0, j = 0;
        int write = l;
        int inv = 0;
        while (i < sizeL && j < sizeR)
        {
            if(L[i] <= R[j])
            {
                arr[write] = L[i];
                i++;
            }
            else
            {
                arr[write] = R[j];
                j++;
                inv+=(sizeL-i);
            }
            write++;
        }

        while(i < sizeL)
        {
            arr[write] = L[i];
            i++;
            write++;
        }

        while(j < sizeR)
        {
            arr[write] = R[j];
            j++;
            write++;
        }
        return inv;
    }
}
