package by.it.group510901.gulchenko.lesson04;

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
        if (stream == null) {
            throw new FileNotFoundException("Файл dataC.txt не найден");
        }

        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        if (stream == null) {
            throw new FileNotFoundException("Входной поток не найден");
        }

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
        result = mergeSortAndCount(a, 0, a.length - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private int mergeSortAndCount(int[] array, int left, int right) {
        if (left >= right) {
            return 0;
        }

        int middle = left + (right - left) / 2;
        int inversions = 0;

        inversions += mergeSortAndCount(array, left, middle);
        inversions += mergeSortAndCount(array, middle + 1, right);
        inversions += mergeAndCount(array, left, middle, right);

        return inversions;
    }

    private int mergeAndCount(int[] array, int left, int middle, int right) {
        int[] temp = new int[right - left + 1];

        int leftIndex = left;
        int rightIndex = middle + 1;
        int tempIndex = 0;
        int inversions = 0;

        while (leftIndex <= middle && rightIndex <= right) {
            if (array[leftIndex] <= array[rightIndex]) {
                temp[tempIndex] = array[leftIndex];
                leftIndex++;
            } else {
                temp[tempIndex] = array[rightIndex];
                inversions += middle - leftIndex + 1;
                rightIndex++;
            }
            tempIndex++;
        }

        while (leftIndex <= middle) {
            temp[tempIndex] = array[leftIndex];
            leftIndex++;
            tempIndex++;
        }

        while (rightIndex <= right) {
            temp[tempIndex] = array[rightIndex];
            rightIndex++;
            tempIndex++;
        }

        for (int i = 0; i < temp.length; i++) {
            array[left + i] = temp[i];
        }

        return inversions;
    }
}