package by.it.group551003.kalach.lesson05;

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
        // подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        // !!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        if (!scanner.hasNextInt()) return new int[0];

        int n = scanner.nextInt();
        int[] points = new int[n];

        // По условию числа не превышают 10.
        // Создаем массив для подсчета частоты появления чисел от 1 до 10.
        // Размер 11, чтобы индекс 10 был валидным.
        int[] count = new int[11];

        // 1. Читаем числа и сразу считаем их количество
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
            count[points[i]]++;
        }

        // 2. Формируем результирующий отсортированный массив
        int k = 0;
        // Проходим по массиву счетчиков (от 1 до 10 по возрастанию)
        for (int i = 1; i < count.length; i++) {
            // Пока счетчик для числа i не обнулится, записываем i в исходный массив
            while (count[i] > 0) {
                points[k++] = i;
                count[i]--;
            }
        }

        // !!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }

}
