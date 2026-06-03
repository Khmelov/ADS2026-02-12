package by.it.group551003.yurkevich.lesson04;

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

    public static int merge(int[] arr, int left, int medium, int right){
        int inversions = 0;
        int n1 = medium - left + 1;
        int n2 = right - medium;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++){
            L[i] = arr[left + i];
        }
        for (int i =0; i < n2; i++){
            R[i] = arr[medium + 1 +i];
        }

        int i =0; int j =0; int k =left;
        while(i < n1 && j < n2){
            if (L[i] <= R[j]){
                arr[k++] = L[i++];
            }
            else{
                arr[k++] = R[j++];
                inversions += (n1 - i);
            }
        }

        while(i < n1){
            arr[k++] = L[i++];
        }
        while(j < n2){
            arr[k++] = R[j++];
        }

        return inversions;
    }

    public static int mergeSort(int[] arr, int left, int right){
        int inversions = 0;
        if (left < right){
            int medium = (left + right) / 2;

            inversions += mergeSort(arr, left, medium);
            inversions += mergeSort(arr, medium + 1, right);
            inversions += merge(arr, left, medium, right);
        }
        return inversions;
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

        result = mergeSort(a, 0, n - 1);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}
