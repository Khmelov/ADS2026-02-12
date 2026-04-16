package by.it.group510901.chernyavskaya.lesson04;

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
        // подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!
        // размер массива
        int n = scanner.nextInt();
        // сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int result = 0;
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!

        // Создаём временный массив для сортировки слиянием
        int[] temp = new int[n];

        // Считаем инверсии с помощью модифицированной сортировки слиянием
        result = mergeSortAndCount(a, temp, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Рекурсивная функция, которая сортирует массив и подсчитывает инверсии
    private int mergeSortAndCount(int[] array, int[] temp, int left, int right) {
        int inversions = 0;

        if (left < right) {
            // Находим среднюю точку
            int middle = left + (right - left) / 2;

            // Считаем инверсии в левой половине
            inversions += mergeSortAndCount(array, temp, left, middle);

            // Считаем инверсии в правой половине
            inversions += mergeSortAndCount(array, temp, middle + 1, right);

            // Считаем инверсии во время слияния
            inversions += mergeAndCount(array, temp, left, middle, right);
        }

        return inversions;
    }

    // Сливает два отсортированных подмассива и подсчитывает инверсии
    private int mergeAndCount(int[] array, int[] temp, int left, int middle, int right) {
        int i = left;      // Начальный индекс левого подмассива
        int j = middle + 1; // Начальный индекс правого подмассива
        int k = left;      // Начальный индекс для сортировки
        int inversions = 0;

        // Сливаем два подмассива, одновременно подсчитывая инверсии
        while (i <= middle && j <= right) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                // Инверсия найдена: все оставшиеся элементы в левом подмассиве
                // (от i до middle) больше, чем array[j]
                temp[k++] = array[j++];
                inversions += (middle - i + 1);
            }
        }

        // Копируем оставшиеся элементы левого подмассива, если они есть
        while (i <= middle) {
            temp[k++] = array[i++];
        }

        // Копируем оставшиеся элементы правого подмассива, если они есть
        while (j <= right) {
            temp[k++] = array[j++];
        }

        // Копируем слитые элементы обратно в исходный массив
        for (i = left; i <= right; i++) {
            array[i] = temp[i];
        }

        return inversions;
    }
}