package by.it.group551002.rybik.lesson05;

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

        int min, max = min = points[0];
        for (int i = 1; i < n; i++){
            if (points[i] < min) min = points[i];
            if (points[i] > max) max = points[i];
        }


        int[] numbers = new int[max-min+1];
        for(int i = 0; i < n; i++){
            numbers[points[i]-min]++;
        }

        for (int i = 0; i < n; i++){
            System.out.print(points[i]);
        }
        System.out.println("  ");
        for (int i = 0; i < max-min+1; i++){
            System.out.print(numbers[i]);
        }
        System.out.println("  ");


        int i = 0;
        int j = 0;
        while (numbers[max-min] != 0){
            if (numbers[j] != 0){
                points[i++] = min+j;
                numbers[j]--;
            } else {
                j++;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }

}
