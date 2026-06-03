package by.it.group510901.chernyavskaya.lesson05;

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

        // Шаг 1: Определяем диапазон значений
        // По условию числа натуральные и не превышают 10
        // Значит возможные значения: от 1 до 10
        // Но для надежности найдем минимум и максимум

        // Находим минимальное и максимальное значение в массиве
        int minValue = points[0];
        int maxValue = points[0];
        for (int i = 1; i < n; i++) {
            if (points[i] < minValue) {
                minValue = points[i];
            }
            if (points[i] > maxValue) {
                maxValue = points[i];
            }
        }

        // Шаг 2: Создаем массив для подсчета количества каждого числа
        // Размер массива = диапазон значений + 1
        int range = maxValue - minValue + 1;
        int[] count = new int[range];

        // Шаг 3: Подсчитываем количество вхождений каждого числа
        // Для каждого числа в исходном массиве увеличиваем счетчик в соответствующей позиции
        for (int i = 0; i < n; i++) {
            // Смещаем индекс, чтобы начать с 0
            int index = points[i] - minValue;
            count[index]++;
        }

        // Шаг 4 Восстанавливаем отсортированный массив
        // Проходим по массиву подсчета и заполняем исходный массив
        int currentIndex = 0;  // текущая позиция для вставки в результирующий массив

        for (int i = 0; i < range; i++) {
            // i - это смещение относительно minValue
            // Само число = minValue + i

            // Пока в count[i] есть элементы (count[i] > 0)
            while (count[i] > 0) {
                // Вставляем число в текущую позицию
                points[currentIndex] = minValue + i;
                // Увеличиваем счетчик позиции
                currentIndex++;
                // Уменьшаем количество оставшихся элементов
                count[i]--;
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }

}