package by.it.group551001.gavrusev.lesson04;

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

        result = mergeSortAndCount(a, 0, n - 1);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    private int mergeSortAndCount(int[] arr, int left, int right) {
        int invCount = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;

            // рекурсивно сортируем левую и правую половины и считаем инверсии
            invCount += mergeSortAndCount(arr, left, mid);
            invCount += mergeSortAndCount(arr, mid + 1, right);

            // сливаем две половины и считаем инверсии
            invCount += mergeAndCount(arr, left, mid, right);
        }
        return invCount;
    }

    private int mergeAndCount(int[] arr, int left, int mid, int right) {
        // создаем временные массивы для левой и правой половин
        int[] leftArr = new int[mid - left + 1];
        int[] rightArr = new int[right - mid];

        // копируем данные во временные массивы
        for (int i = 0; i < leftArr.length; i++) {
            leftArr[i] = arr[left + i];
        }
        for (int i = 0; i < rightArr.length; i++) {
            rightArr[i] = arr[mid + 1 + i];
        }

        int i = 0, j = 0, k = left;
        int invCount = 0;

        // сливаем два временных массива обратно в основной
        while (i < leftArr.length && j < rightArr.length) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++];
            } else {
                // если элемент из правого массива меньше элемента из левого,
                // то все оставшиеся элементы в левом массиве образуют инверсии
                // с текущим элементом правого массива
                arr[k++] = rightArr[j++];
                invCount += (leftArr.length - i);
            }
        }

        // копируем оставшиеся элементы левого массива
        while (i < leftArr.length) {
            arr[k++] = leftArr[i++];
        }

        // копируем оставшиеся элементы правого массива
        while (j < rightArr.length) {
            arr[k++] = rightArr[j++];
        }

        return invCount;
    }
}

