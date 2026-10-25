package by.it.group551003.tarasau.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Первая строка содержит число 1<=n<=10000, вторая - n натуральных чисел, не превышающих 10.
Выведите упорядоченную по неубыванию последовательность этих чисел.

При сортировке реализуйте метод со сложностью O(n)

Пример: https://karussell.wordpress.com/2010/03/01/fast-integer-sorting-algorithm-on/
Вольный перевод: http://programador.ru/sorting-positive-int-linear-time/
*/

public class B_CountSort {


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //размер массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        //читаем точки
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением сортировки подсчетом

        int maxElem = points[0];

        for (int i = 1; i < n; i++) {    // find max elem
            if (maxElem < points[i]) {
                maxElem = points[i];
            }
        }

        int[] countArray = new int[maxElem+1];
        for (int i = 0; i < maxElem+1; i++) {    // create new array initialised with 0
            countArray[i] = 0;
        }

        for (int i = 0; i < n; i++) {    // count frequency of each elem
            countArray[points[i]]++;
        }

        for (int i = 1; i < maxElem+1; i++) {   // count prefix sum
            countArray[i] += countArray[i-1];
        }

        int[] ans = new int[n];        // create sorted array
        for (int i = n-1; i > -1; i--) {
            int x = points[i];
            ans[countArray[x]-1] = x;
            countArray[x]--;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return ans;                   // profit
    }

}
