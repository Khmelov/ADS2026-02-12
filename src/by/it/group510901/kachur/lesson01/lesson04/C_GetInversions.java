package by.it.group510901.kachur.lesson01.lesson04;

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
        // вызываем сортировку слиянием, которая вернет количество инверсий
        int result = mergeSortAndCount(a, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Рекурсивная сортировка слиянием с подсчетом инверсий
    private int mergeSortAndCount(int[] array, int left, int right) {
        int inversionCount = 0;

        // если остался один элемент - инверсий нет
        if (left >= right) {
            return 0;
        }

        // Находим середину
        int middle = left + (right - left) / 2;

        // Считаем инверсии в левой половине
        inversionCount += mergeSortAndCount(array, left, middle);

        // Считаем инверсии в правой половине
        inversionCount += mergeSortAndCount(array, middle + 1, right);

        // Считаем инверсии при слиянии (когда элементы из правой части меньше элементов из левой)
        inversionCount += mergeAndCount(array, left, middle, right);

        return inversionCount;
    }

    // Слияние двух отсортированных половин и подсчет инверсий
    private int mergeAndCount(int[] array, int left, int middle, int right) {
        // Создаем временный массив для результата слияния
        int[] temp = new int[right - left + 1];


        int leftIndex = left;        // указатель на левую половину
        int rightIndex = middle + 1; // указатель на правую половину
        int tempIndex = 0;           // указатель во временном массиве

        int inversionCount = 0;      // счетчик инверсий

        // Сливаем две половины и считаем инверсии
        while (leftIndex <= middle && rightIndex <= right) {
            if (array[leftIndex] <= array[rightIndex]) {
                // Левый элемент меньше или равен правому - не инверсия
                temp[tempIndex] = array[leftIndex];
                leftIndex++;
            } else {
                // Правый элемент меньше левого - это инверсия
                // Все оставшиеся элементы в левой половине (от leftIndex до middle)
                // будут больше текущего правого элемента, так как левая половина отсортирована
                temp[tempIndex] = array[rightIndex];
                inversionCount += (middle - leftIndex + 1);
                rightIndex++;
            }
            tempIndex++;
        }

        // Если в левой половине остались элементы - добавляем их
        while (leftIndex <= middle) {
            temp[tempIndex] = array[leftIndex];
            leftIndex++;
            tempIndex++;
        }

        // Если в правой половине остались элементы - добавляем их
        while (rightIndex <= right) {
            temp[tempIndex] = array[rightIndex];
            rightIndex++;
            tempIndex++;
        }

        // Копируем отсортированные элементы обратно в исходный массив
        for (int i = 0; i < temp.length; i++) {
            array[left + i] = temp[i];
        }

        return inversionCount;
    }
}
