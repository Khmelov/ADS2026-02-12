package by.it.group510902.klimantovich.lesson01.lesson05;

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

        //Определяем диапазон значений
        // По условию числа натуральные и не превышают 10, значит диапазон от 1 до 10
        // Но для универсальности найдем реальные min и max
        int minValue = Integer.MAX_VALUE;
        int maxValue = Integer.MIN_VALUE;
        // Находим минимальное и максимальное значение в массиве
        for (int i = 0; i < n; i++) {
            if (points[i] < minValue) {
                minValue = points[i];
            }
            if (points[i] > maxValue) {
                maxValue = points[i];
            }
        }
        //Создаем массив для подсчета частоты встречаемости чисел
        // Размер массива = (max - min + 1) - количество возможных различных значений
        int range = maxValue - minValue + 1;
        int[] count = new int[range];
        // Заполняем массив подсчета: для каждого числа увеличиваем счетчик
        // Индекс в массиве count = значение элемента - minValue
        for (int i = 0; i < n; i++) {
            int index = points[i] - minValue;// Смещение относительно минимального значения
            count[index]++;// Увеличиваем счетчик для этого числа
        }
        // Преобразуем массив подсчета в массив накопленных частот
        // Теперь count[i] будет содержать количество элементов <= (minValue + i)
        for (int i = 1; i < range; i++) {
            count[i] = count[i] + count[i - 1];
        }
        //Создаем результирующий массив для отсортированных элементов
        int[] sorted = new int[n];
        // Проходим по исходному массиву СПРАВА НАЛЕВО для устойчивости сортировки
        // Устойчивость означает, что элементы с одинаковыми значениями сохраняют
        // свой относительный порядок (для данной задачи это не критично, но это свойство counting sort)
        for (int i = n - 1; i >= 0; i--) {
            int value = points[i];
            int index = value - minValue;// Индекс в count для этого значения
            int position = count[index] - 1;// Позиция в отсортированном массиве (индексация с 0)
            sorted[position] = value;// Помещаем значение на правильную позицию
            count[index]--;// Уменьшаем счетчик для этого значения
        }
        // Копируем отсортированный массив обратно в points
        for (int i = 0; i < n; i++) {
            points[i] = sorted[i];
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }
}