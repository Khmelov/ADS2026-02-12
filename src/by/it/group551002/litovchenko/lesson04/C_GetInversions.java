package by.it.group551002.litovchenko.lesson04;

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
        result = mergesort(a, 0, n - 1);
        // !!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    int merge(int[] array, int left, int right, int mid) {
        int[] LowHalf = new int[mid - left + 1];
        int[] HighHalf = new int[right - mid];
        for (int i = 0; i < mid - left + 1; i++) {
            LowHalf[i] = array[left + i];
        }
        for (int j = 0; j < right - mid; j++) {
            HighHalf[j] = array[mid + 1 + j];
        }
        int k = left;
        int i = 0;
        int j = 0;
        int invCount = 0;
        while (i < LowHalf.length && j < HighHalf.length) {
            if (LowHalf[i] > HighHalf[j]) {
                array[k] = HighHalf[j];
                j++;
                invCount += (LowHalf.length - i);
            } else {
                array[k] = LowHalf[i];
                i++;
            }

            k++;

        }
        while (i < LowHalf.length) {
            array[k] = LowHalf[i];
            k++;
            i++;
        }
        while (j < HighHalf.length) {
            array[k] = HighHalf[j];
            k++;
            j++;
        }
        return invCount;
    }

    int mergesort(int[] array, int left, int right) {
        int count = 0;
        if (left < right) {
            int mid = (left + right) / 2;
            mergesort(array, left, mid);
            mergesort(array, mid + 1, right);
            count = merge(array, left, right, mid);
        }
        return count;
    }

}
