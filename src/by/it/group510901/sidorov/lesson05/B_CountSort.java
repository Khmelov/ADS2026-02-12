package by.it.group510901.sidorov.lesson05;

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

        int min = 1;
        int max = 10;
        int[] count = new int[max - min + 1];

        //читаем точки
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
            count[points[i] - min]++;
        }
        //тут реализуйте логику задачи с применением сортировки подсчетом

        int idx = 0;
        // Пробегаем по нашему вспомогательному массиву
        for (int i = 0; i < count.length; i++) {
            // Пока счетчик для этого числа не обнулится
            while (count[i] > 0) {
                points[idx++] = i + min; // Записываем число обратно в массив
                count[i]--;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }

}
