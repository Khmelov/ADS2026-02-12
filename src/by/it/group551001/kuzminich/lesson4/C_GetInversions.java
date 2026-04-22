package by.it.group551001.kuzminich.lesson4;

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
    private int[] merge_sort ( int[] from_arr, int[] to_arr, int left, int right, int[] count) {

        if (left == right) {
            to_arr[left] = from_arr[left];
            return to_arr; // базовый случай
        }
        int mid = left + (right - left) / 2;
        int[] left_arr = merge_sort(from_arr, to_arr, left, mid, count);
        int[] right_arr = merge_sort(from_arr, to_arr, mid + 1, right, count);
        int target[];
        if (left_arr == from_arr) {
            target = to_arr;
        } else {
            target = from_arr;
        }

        int left_curr = left;
        int right_curr = mid + 1;

        for (int i = left; i <= right; i++) {
            if (left_curr <= mid && right_curr <= right) {
                if (left_arr[left_curr] <= right_arr[right_curr]) {
                    target[i] = left_arr[left_curr];
                    left_curr++;
                } else {
                    target[i] = right_arr[right_curr];
                    right_curr++;
                    // количество элементв, оставшихся в левом массиве
                    count[0] = count[0] + (mid+1-left_curr);
                }
            } else if (left_curr <= mid) {
                target[i] = left_arr[left_curr];
                left_curr++;
            } else {
                target[i] = right_arr[right_curr];
                right_curr++;
            }

        }
        return target;
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
        int[] temp = new int[n];
        int[] count = new int[1];
        count[0] = 0;
        int[] result_arr = merge_sort ( a, temp, 0, n-1, count);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return count[0];
    }
}
