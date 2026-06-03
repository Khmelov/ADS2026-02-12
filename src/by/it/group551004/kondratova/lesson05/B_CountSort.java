package by.it.group551004.kondratova.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Первая строка содержит число 1<=n<=10000, вторая - n натуральных чисел, не превышающих 10.
Выведите упорядоченную по неубыванию последовательность этих чисел.

При сортировке реализуйте метод со сложностью O(n)
Шаг 1: Чтение размера массива n
Шаг 2: Чтение n чисел в массив points
Шаг 3: Создание массива count размером 11 (0..10)
Шаг 4: Обнуление всех элементов count
Шаг 5: Подсчёт вхождений:
count[points[i]]++ для каждого элемента
Шаг 6: Создание переменной index = 0
Шаг 7: Цикл value от 0 до 10:
Вложенный цикл j от 0 до count[value]
points[index++] = value
Шаг 8: Возврат отсортированного массива points
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

        // Сортировка подсчётом (Counting Sort)
        // Так как числа не превышают 10, диапазон значений от 0 до 10
        int maxValue = 10;
        int[] count = new int[maxValue + 1]; // массив для подсчёта (0..10)

        // 1. Подсчитываем количество вхождений каждого числа
        for (int i = 0; i < n; i++) {
            count[points[i]]++;
        }

        // 2. Восстанавливаем отсортированный массив
        int index = 0;
        for (int value = 0; value <= maxValue; value++) {
            for (int j = 0; j < count[value]; j++) {
                points[index++] = value;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }
}