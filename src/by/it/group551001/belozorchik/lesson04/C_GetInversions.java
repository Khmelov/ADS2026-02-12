package by.it.group551001.belozorchik.lesson04;

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
        //int result = 0;
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!

        int[] temp = new int[n];
        System.arraycopy(a, 0, temp, 0, n);

        // тк результат мб большим, используем long
        long result = my_merge_sort(temp, a, 0, n - 1);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return (int) result;
    }

    private long my_merge_sort(int[] from_arr, int[] to_arr, int left, int right) {

        //если в подмассиве 1 эл
        if (left == right) {
            to_arr[left] = from_arr[left];
            return 0;   // для 1 эл - 0 инверсий
        }
        //разделяй и сортируй
        int middle = left + (right - left) / 2;

        // считаем инверсии в подмассивах и при слиянии
        long inversions = 0;
        inversions += my_merge_sort(to_arr, from_arr, left, middle);
        inversions += my_merge_sort(to_arr, from_arr, middle + 1, right);
        //слияние двух отсортированных массивов
        int i = left;
        int j = middle + 1;
        int k = left;

        while (i <= middle && j <= right) {
            if (from_arr[i] <= from_arr[j]) {
                to_arr[k++] = from_arr[i++];
            } else {
                to_arr[k++] = from_arr[j++];
                // эл слева > эл справа ->
                // все эл слева от i до middle образуют инверсию с j
                inversions += (middle - i + 1);
                //инверсия не только с тек эл i, но и со всеми оставшимися эл слева до границы middle
            }
        }

        while (i <= middle) {
            to_arr[k++] = from_arr[i++];
        }
        while (j <= right) {
            to_arr[k++] = from_arr[j++];
        }

        return inversions;
    }
}
