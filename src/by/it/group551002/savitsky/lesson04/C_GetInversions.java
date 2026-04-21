package by.it.group551002.savitsky.lesson04;

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
    private int inversions;

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    int[] sortArray(int[] arrA) {

        if (arrA == null)
            return null;

        if (arrA.length < 2)
            return arrA;

        int mid = arrA.length / 2;
        int[] arrB = new int[mid];
        System.arraycopy(arrA, 0, arrB, 0, mid);

        int[] arrC = new int[arrA.length - mid];
        System.arraycopy(arrA, arrA.length / 2, arrC, 0, arrA.length - mid);

        arrB = sortArray(arrB);
        arrC = sortArray(arrC);

        return mergeSort(arrB, arrC);
    }

    int[] mergeSort(int[] arrA, int[] arrB) {
        int[] arrC = new int[arrA.length + arrB.length];
        int posA = 0;
        int posB = 0;

        for (int i = 0; i < arrC.length; i++) {
            if (posA == arrA.length) {
                arrC[i] = arrB[posB];
                ++posB;

            } else if (posB == arrB.length) {
                arrC[i] = arrA[posA];
                ++posA;

            } else if (arrA[posA] <= arrB[posB]) {
                arrC[i] = arrA[posA];
                ++posA;

            } else {
                arrC[i] = arrB[posB];
                ++posB;
                inversions += arrA.length - posA;
            }
        }
        return arrC;
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
        sortArray(a);
        result = inversions;
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}
