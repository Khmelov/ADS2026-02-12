package by.it.group551001.bogush.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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

        result = Merge_sort(a,0,a.length - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    int Merge_sort(int[] arr,int left,int right) {
        if (left >= right)
            return 0;
        int mid = (left + right) / 2;
        int count = 0;
        count += Merge_sort(arr,left,mid);
        count += Merge_sort(arr,mid+1,right);

        count += Merge(arr,left,mid,right);

        return count;
    }

    int Merge(int[] arr,int left,int mid,int right) {

        int[] left_arr = Arrays.copyOfRange(arr, left, mid + 1);
        int[] right_arr = Arrays.copyOfRange(arr, mid + 1, right + 1);
        int k = left;
        int i = 0;
        int j = 0;
        int count = 0;

        while (i < left_arr.length && j < right_arr.length) {
            if (left_arr[i] <= right_arr[j]) {
                arr[k] = left_arr[i];
                k++;
                i++;
            } else {
                arr[k] = right_arr[j];
                k++;
                j++;
                count += left_arr.length - i;
            }
        }

        while (i < left_arr.length) {
            arr[k] = left_arr[i];
            k++;
            i++;
        }
        while (j < right_arr.length) {
            arr[k] = right_arr[j];
            k++;
            j++;
        }
        return count;
    }
}
