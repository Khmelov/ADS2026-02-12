package by.it.group510902.mikhnovets.lesson04;

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

        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!
        int[] tempp = new int[n];
        int result = mergesort(a, tempp, 0, n-1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    private int mergesort(int[] arr, int[] temp, int left, int right) {
        int inversion = 0;
        if(left < right) {
            int middle = ( left + right ) / 2;

            inversion += mergesort(arr, temp, left, middle );
            inversion += mergesort(arr, temp, middle + 1, right);
            inversion += mergesortA(arr, temp, left, middle, right);

        }
        return inversion;
    }
    private int mergesortA(int[] arr, int[] temp, int left, int middle, int right) {
        int i = left;
        int j = middle + 1;
        int k = left;
        int inversion = 0;
        while(i <= middle && j <= right) {
            if(arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            }
            else {
                inversion += middle - i + 1;
                temp[k++] = arr[j++];
            }
        }
        while(i <= middle) {
            temp[k++] = arr[i++];
        }
        while(j <= right) {
            temp[k++] = arr[j++];
        }
        for (i = left; i <= right; i++) {
            arr[i] = temp[i];
        }
        return inversion;
    }

}
