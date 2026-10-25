package by.it.group551004.kondratova.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Рассчитать число инверсий одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо посчитать число пар индексов 1<=i<j<n, для которых A[i]>A[j].

Sample Input:
5
2 3 9 2 9
Sample Output:
2
Подсчёт инверсий
1 Делим массив на две половины
2 Рекурсивно считаем инверсии в левой половине
3 Рекурсивно считаем инверсии в правой половине
4 При слиянии: если элемент из правой части меньше элемента из левой → все оставшиеся элементы левой части образуют инверсии
*/

public class C_GetInversions {

    private long inversionCount = 0; // счётчик инверсий (long, так как может быть много)

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    // Рекурсивная сортировка слиянием с подсчётом инверсий
    private void mergeSortAndCount(int[] array, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            // Сортируем левую и правую половины
            mergeSortAndCount(array, left, mid);
            mergeSortAndCount(array, mid + 1, right);

            // Сливаем и считаем инверсии
            mergeAndCount(array, left, mid, right);
        }
    }

    // Слияние двух отсортированных половин с подсчётом инверсий
    private void mergeAndCount(int[] array, int left, int mid, int right) {
        // Размеры временных массивов
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Создаём временные массивы
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // Копируем данные во временные массивы
        for (int i = 0; i < n1; i++) {
            leftArray[i] = array[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = array[mid + 1 + j];
        }

        // Слияние временных массивов обратно в основной
        int i = 0, j = 0;
        int k = left;

        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                // Если leftArray[i] > rightArray[j], то это инверсия
                // Все оставшиеся элементы в leftArray больше rightArray[j]
                inversionCount += (n1 - i);
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        // Копируем оставшиеся элементы из leftArray
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        // Копируем оставшиеся элементы из rightArray
        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
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

        // Сброс счётчика и запуск сортировки с подсчётом инверсий
        inversionCount = 0;
        mergeSortAndCount(a, 0, n - 1);

        int result = (int) inversionCount; // приводим к int (n <= 10000, максимум ~50 млн, помещается в int)
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}