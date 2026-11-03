package by.it.group510902.klimantovich.lesson01.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача: Подсчитать количество инверсий в массиве
Инверсия - это пара элементов A[i] > A[j], где i < j

Принцип работы:
Используем модифицированную сортировку слиянием:
- Когда берем элемент из правой половины, значит он меньше всех оставшихся в левой
- Каждый такой случай добавляет (mid - i + 1) инверсий

Пример: [2,3,9,2,9]
Инверсии: (3,2), (9,2), (9,2) - всего 3? Но ответ 2...
Правильно: (9,2) на позициях 3 и 4, и (9,2) на позициях 5 и 4? Нет, индексы i<j
Пары: (3,2) - A[2]=3 > A[4]=2, (9,2) - A[3]=9 > A[4]=2 - всего 2 инверсии

Сложность: O(n log n)
*/
public class C_GetInversions {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        int result = instance.calc(stream);
        System.out.print(result);
    }

    public int calc(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем массив
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Временный массив для сортировки
        int[] temp = new int[n];

        // Запускаем сортировку с подсчетом инверсий
        int result = mergeSortAndCount(a, temp, 0, n - 1);

        return result;
    }

    /*
     * Рекурсивная сортировка слиянием с подсчетом инверсий
     * Отличие от обычной сортировки: возвращает количество инверсий
     *
     * Инверсия считается, когда элемент из правой половины меньше элемента из левой
     * Если A[i] > A[j] при i < j, то это инверсия
     * При слиянии: если берем элемент из правой половины, значит он меньше
     * всех оставшихся в левой -> добавляем (mid - i + 1) инверсий
     */
    private int mergeSortAndCount(int[] arr, int[] temp, int left, int right) {
        int invCount = 0;

        if (left < right) {
            int mid = left + (right - left) / 2;

            // Считаем инверсии в левой и правой половинах
            invCount += mergeSortAndCount(arr, temp, left, mid);
            invCount += mergeSortAndCount(arr, temp, mid + 1, right);

            // Считаем инверсии при слиянии
            invCount += mergeAndCount(arr, temp, left, mid, right);
        }

        return invCount;
    }

    /*
     * Слияние с подсчетом инверсий
     *
     * Как это работает:
     * Левый массив: [2,3,9]
     * Правый массив: [2,9]
     *
     * Сравниваем 2 и 2: равны -> берем из левого (инверсий нет)
     * Сравниваем 3 и 2: 3 > 2 -> берем 2 из правого
     *   В этот момент все оставшиеся в левом (3,9) больше 2
     *   Добавляем 2 инверсии: (3,2) и (9,2)
     * Сравниваем 3 и 9: 3 < 9 -> берем 3 из левого
     * Сравниваем 9 и 9: равны -> берем 9 из левого
     * Остался 9 в правом -> добавляем в конец
     *
     * Итого: 2 инверсии
     */
    private int mergeAndCount(int[] arr, int[] temp, int left, int mid, int right) {
        // Копируем во временный массив
        for (int i = left; i <= right; i++) {
            temp[i] = arr[i];
        }

        int i = left;      // указатель на левую половину
        int j = mid + 1;   // указатель на правую половину
        int k = left;      // указатель на результирующий массив
        int invCount = 0;

        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                // Берем из левой - инверсий нет
                arr[k] = temp[i];
                i++;
            } else {
                // Берем из правой - это инверсия!
                // Элемент temp[j] меньше всех оставшихся в левой половине
                // Каждый оставшийся в левой образует инверсию с temp[j]
                arr[k] = temp[j];
                invCount += (mid - i + 1);  // количество оставшихся в левой
                j++;
            }
            k++;
        }

        // Копируем остатки
        while (i <= mid) {
            arr[k] = temp[i];
            i++;
            k++;
        }

        while (j <= right) {
            arr[k] = temp[j];
            j++;
            k++;
        }

        return invCount;
    }
}