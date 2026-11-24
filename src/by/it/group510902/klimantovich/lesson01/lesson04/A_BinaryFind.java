package by.it.group510902.klimantovich.lesson01.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача: Найти индексы заданных чисел в отсортированном массиве
Принцип работы:
1. Читаем отсортированный массив чисел
2. Для каждого искомого числа используем бинарный поиск (деление массива пополам)
3. Возвращаем индекс (начиная с 1) или -1, если число не найдено

Сложность: O(log n) для каждого поиска, где n - размер массива
*/
public class A_BinaryFind {
    public static void main(String[] args) throws FileNotFoundException {
        // Загружаем файл с тестовыми данными из ресурсов
        InputStream stream = A_BinaryFind.class.getResourceAsStream("dataA.txt");
        A_BinaryFind instance = new A_BinaryFind();

        // Выполняем бинарный поиск и получаем массив индексов
        int[] result = instance.findIndex(stream);

        // Выводим результат
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    public int[] findIndex(InputStream stream) throws FileNotFoundException {
        // Scanner используется для чтения данных из потока
        Scanner scanner = new Scanner(stream);

        // Читаем размер массива
        int n = scanner.nextInt();

        // Создаем и заполняем массив (он уже отсортирован по условию)
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Читаем количество чисел, которые нужно найти
        int k = scanner.nextInt();
        int[] result = new int[k];

        // Для каждого искомого числа выполняем бинарный поиск
        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt();  // число, которое ищем
            int left = 0;                   // левая граница поиска
            int right = n - 1;              // правая граница поиска
            int foundIndex = -1;            // индекс найденного элемента (-1 = не найден)

            /* БИНАРНЫЙ ПОИСК:
             * Принцип: на каждом шаге делим массив пополам
             * и определяем, в какой половине может находиться искомое число
             *
             * Пример: массив [1,5,8,12,13], ищем 8
             * Шаг 1: left=0, right=4, mid=2 -> a[2]=8, нашли!
             *
             * Пример: ищем 10 (которого нет)
             * Шаг 1: left=0, right=4, mid=2 -> a[2]=8 < 10, ищем справа (left=3)
             * Шаг 2: left=3, right=4, mid=3 -> a[3]=12 > 10, ищем слева (right=2)
             * Шаг 3: left=3 > right=2 -> выход, не нашли
             */
            while (left <= right) {
                // Находим середину (такая формула избегает переполнения)
                int mid = left + (right - left) / 2;

                if (a[mid] == value) {
                    // Нашли! Индекс в задаче начинается с 1, поэтому +1
                    foundIndex = mid + 1;
                    break;
                } else if (a[mid] < value) {
                    // Искомое число больше среднего -> ищем в правой половине
                    left = mid + 1;
                } else {
                    // Искомое число меньше среднего -> ищем в левой половине
                    right = mid - 1;
                }
            }

            result[i] = foundIndex;  // сохраняем результат (-1 если не нашли)
        }

        return result;
    }
}