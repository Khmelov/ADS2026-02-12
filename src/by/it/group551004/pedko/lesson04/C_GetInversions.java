package by.it.group551004.pedko.lesson04;

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

    private int inversionCount = 0;

    public int[] merge(int[] array1, int[] array2) {
        int i;
        int j;
        int index;
        int size1;
        int size2;
        int answerSize;
        int[] answerArray;

        i = 0;
        j = 0;
        index = 0;
        size1 = 0;
        size2 = 0;
        answerSize = 0;

        size1 = array1.length;
        size2 = array2.length;

        answerSize = size1 + size2;
        answerArray = new int[answerSize];

        while (i < size1 && j < size2) {
            if (array1[i] <= array2[j]) {
                answerArray[index] = array1[i];
                i++;
            } else {
                answerArray[index] = array2[j];
                j++;
                inversionCount += (size1 - i);
            }
            index++;
        }

        while (i < size1) {
            answerArray[index] = array1[i];
            i++;
            index++;
        }

        while (j < size2) {
            answerArray[index] = array2[j];
            j++;
            index++;
        }

        return answerArray;
    }

    public int[] mergesort(int[] up, int[] down, int left, int right) {

        if (left == right) {
            down[left] = up[left];
            down = new int[1];
            down[0] = up[left];
            return down;
        }

        int middle = left + (right - left) / 2;

        int[] leftBuffer = mergesort(up, down, left, middle);
        int[] rightBuffer = mergesort(up, down, middle + 1, right);

        int[] target;
        if (leftBuffer == up) {
            target = down;
        } else {
            target = up;
        }

        return merge(leftBuffer, rightBuffer);
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

        int[] buff;
        buff = new int[a.length];
        mergesort(a, buff, 0, a.length - 1);
        result = inversionCount;

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}
