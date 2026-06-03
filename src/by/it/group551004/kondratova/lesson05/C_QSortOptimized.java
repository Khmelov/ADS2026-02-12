package by.it.group551004.kondratova.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0
    Часть 1: Чтение данных
Шаг 1: Чтение n (количество отрезков)
Шаг 2: Чтение m (количество точек)
Шаг 3: Чтение n отрезков (start, stop)
Шаг 4: Чтение m точек
Часть 2: Сортировка отрезков
Шаг 5: Сортировка отрезков по началу (быстрая сортировка с 3-разбиением)
Шаг 6: Создание массива starts (начала отрезков)
Шаг 7: Создание массива stops (концы отрезков)
Шаг 8: Сортировка массива stops по возрастани
Часть 3: Подсчёт для каждой точки
Шаг 9: Для каждой точки:
leftCount = количество отрезков с началом ≤ точка
rightCount = количество отрезков с концом < точка
result[i] = leftCount - rightCount
Часть 4: Вывод результата
Шаг 10: Вывод массива result
*/

public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        //число отрезков
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            if (start > stop) {
                int temp = start;
                start = stop;
                stop = temp;
            }
            segments[i] = new Segment(start, stop);
        }

        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки с помощью оптимизированной быстрой сортировки (3-разбиение)
        quickSort3Way(segments, 0, n - 1);

        // Создаем массив начал и концов
        int[] starts = new int[n];
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

        // Сортируем концы отдельно
        quickSort(stops, 0, n - 1);

        // Для каждой точки считаем количество отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Количество отрезков с началом <= point
            int leftCount = countLessOrEqual(starts, point);

            // Количество отрезков с концом < point
            int rightCount = countLess(stops, point);

            result[i] = leftCount - rightCount;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Быстрая сортировка с 3-разбиением (для отрезков)
    private void quickSort3Way(Segment[] arr, int left, int right) {
        if (left >= right) return;

        // Выбираем опорный элемент (медиана из трех)
        Segment pivot = medianOfThree(arr, left, right);

        // 3-разбиение: lt < pivot, eq = pivot, gt > pivot
        int lt = left;
        int gt = right;
        int i = left;

        while (i <= gt) {
            int cmp = arr[i].compareTo(pivot);
            if (cmp < 0) {
                swap(arr, lt, i);
                lt++;
                i++;
            } else if (cmp > 0) {
                swap(arr, i, gt);
                gt--;
            } else {
                i++;
            }
        }

        // Рекурсивно сортируем левую и правую части
        // Для левой части используем хвостовую рекурсию (итеративно)
        if (lt - left < right - gt) {
            quickSort3Way(arr, left, lt - 1);
            // Хвостовая рекурсия для правой части
            int newLeft = gt + 1;
            while (newLeft < right) {
                quickSort3Way(arr, newLeft, right);
                break;
            }
        } else {
            quickSort3Way(arr, gt + 1, right);
            // Хвостовая рекурсия для левой части
            int newRight = lt - 1;
            while (left < newRight) {
                quickSort3Way(arr, left, newRight);
                break;
            }
        }
    }

    // Быстрая сортировка для массива int (для концов отрезков)
    private void quickSort(int[] arr, int left, int right) {
        while (left < right) {
            int pivotIndex = partition(arr, left, right);
            // Рекурсивно сортируем меньшую часть, а большую обрабатываем итеративно (устранение хвостовой рекурсии)
            if (pivotIndex - left < right - pivotIndex) {
                quickSort(arr, left, pivotIndex - 1);
                left = pivotIndex + 1;
            } else {
                quickSort(arr, pivotIndex + 1, right);
                right = pivotIndex - 1;
            }
        }
    }

    // Разбиение для int массива
    private int partition(int[] arr, int left, int right) {
        int pivot = arr[left + (right - left) / 2];
        int i = left;
        int j = right;

        while (i <= j) {
            while (arr[i] < pivot) i++;
            while (arr[j] > pivot) j--;
            if (i <= j) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }
        return i;
    }

    // Медиана из трех для отрезков
    private Segment medianOfThree(Segment[] arr, int left, int right) {
        int mid = left + (right - left) / 2;

        if (arr[mid].compareTo(arr[left]) < 0) swap(arr, left, mid);
        if (arr[right].compareTo(arr[left]) < 0) swap(arr, left, right);
        if (arr[right].compareTo(arr[mid]) < 0) swap(arr, mid, right);

        return arr[mid];
    }

    // Обмен элементов для отрезков
    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Бинарный поиск: количество элементов <= x
    private int countLessOrEqual(int[] arr, int x) {
        int left = 0;
        int right = arr.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] <= x) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result + 1;
    }

    // Бинарный поиск: количество элементов < x
    private int countLess(int[] arr, int x) {
        int left = 0;
        int right = arr.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < x) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result + 1;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            // Сортируем по началу, если равны - по концу
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }
}