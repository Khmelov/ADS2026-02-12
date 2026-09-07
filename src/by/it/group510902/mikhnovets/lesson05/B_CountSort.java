package by.it.group510902.mikhnovets.lesson05;

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
        Countsoort(points);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }
    public void Countsoort(int[] arr) {
        if(arr.length == 0) return;
        int maxVal = arr[0];
        for(int x : arr) {
            if(x > maxVal) {
                maxVal = x;
            }
        }
        int[] count = new int[maxVal + 1];
        for(int x : arr) {
            count[x]++;
        }
        for(int i = 1; i <= maxVal; i++) {
            count[i] += count[i - 1];
        }
        int[] output = new int[arr.length];
        for(int i = arr.length - 1; i >= 0; i--) {
            int x = arr[i];
            output[count[x] - 1] = x;
            count[x]--;
        }
        System.arraycopy(output, 0, arr, 0, arr.length);
    }
}
