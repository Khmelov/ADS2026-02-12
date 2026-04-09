package by.it.group551003.parkhaniuk.lesson04;

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
        int[] buffer = new int[n];
        int[] invs = new int[1];
        mergeSort(a, buffer, 0, n - 1, invs);
        result = invs[0];

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static int[] mergeSort(int[] up, int[] down, int left, int right, int[] totalInversions) {
        if (left == right) {
            down[left] = up[left];
            return down;
        }

        int mid = left + (right - left) / 2;

        int[] l_buff = mergeSort(up, down, left, mid, totalInversions);
        int[] r_buff = mergeSort(up, down, mid + 1, right, totalInversions);

        int[] target;
        int l_cur = left;
        int r_cur = mid + 1;

        if (l_buff == up)
            target = down;
        else
            target = up;

        for (int i = left; i <= right; i++) {
            if (l_cur <= mid && r_cur <= right) {
                if (l_buff[l_cur] <= r_buff[r_cur])
                {
                    target[i] = l_buff[l_cur];
                    l_cur++;
                }
                else
                {
                    target[i] = r_buff[r_cur];
                    r_cur++;
                    totalInversions[0] += (mid - l_cur + 1);
                }
            } else if (l_cur <= mid)
            {
                target[i] = l_buff[l_cur];
                l_cur++;
            }
            else
            {
                target[i] = r_buff[r_cur];
                r_cur++;
            }
        }
        return target;
    }

}
