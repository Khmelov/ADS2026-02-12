package by.it.group510901.filimonova.lesson01.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
        //число отрезков отсортированного массива
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
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

        // Сортируем отрезки с помощью быстрой сортировки с 3-разбиением
        quickSort3Way(segments, 0, segments.length - 1);

        // Создаем отдельные массивы для начал и концов (для бинарного поиска)
        int[] starts = new int[n];
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

        // Для каждой точки находим количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Находим первый отрезок, который может покрывать точку (start <= point)
            int firstIndex = findFirst(starts, point);

            if (firstIndex != -1) {
                // Считаем количество отрезков с stop >= point, начиная с firstIndex
                int count = 0;
                for (int j = firstIndex; j < n && starts[j] <= point; j++) {
                    if (stops[j] >= point) {
                        count++;
                    }
                }
                result[i] = count;
            } else {
                result[i] = 0;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Быстрая сортировка с 3-разбиением (оптимизированная для повторяющихся элементов)
    private void quickSort3Way(Segment[] arr, int low, int high) {
        // Элиминация хвостовой рекурсии с помощью цикла
        while (low < high) {
            // Выполняем 3-разбиение
            int[] pivots = partition3Way(arr, low, high);
            int lt = pivots[0];  // индекс первого элемента, равного pivot
            int gt = pivots[1];  // индекс последнего элемента, равного pivot

            // Сортируем левую часть (элементы меньше pivot)
            quickSort3Way(arr, low, lt - 1);

            // Хвостовая рекурсия: обрабатываем правую часть (элементы больше pivot)
            low = gt + 1;
        }
    }

    // 3-разбиение: элементы < pivot, == pivot, > pivot
    private int[] partition3Way(Segment[] arr, int low, int high) {
        // Используем медиану из трех для выбора опорного элемента (улучшает производительность)
        int mid = low + (high - low) / 2;
        if (arr[mid].compareTo(arr[low]) < 0) swap(arr, low, mid);
        if (arr[high].compareTo(arr[low]) < 0) swap(arr, low, high);
        if (arr[high].compareTo(arr[mid]) < 0) swap(arr, mid, high);
        swap(arr, mid, high); // перемещаем опорный элемент в конец

        Segment pivot = arr[high];
        int lt = low;      // индекс для элементов < pivot
        int gt = high;     // индекс для элементов > pivot
        int i = low;       // текущий индекс

        while (i <= gt) {
            int cmp = arr[i].compareTo(pivot);
            if (cmp < 0) {
                swap(arr, lt++, i++);
            } else if (cmp > 0) {
                swap(arr, i, gt--);
            } else {
                i++;
            }
        }

        return new int[]{lt, gt};
    }

    // Бинарный поиск первого отрезка с start <= point
    private int findFirst(int[] starts, int point) {
        int left = 0;
        int right = starts.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (starts[mid] <= point) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }

    // Вспомогательный метод для обмена элементов
    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
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
            // Сравниваем по началу отрезка
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            // Если начала равны, сравниваем по концу
            return Integer.compare(this.stop, o.stop);
        }

        @Override
        public String toString() {
            return "[" + start + ", " + stop + "]";
        }
    }
}
/*1. QuickSort с 3-разбиением — эффективная сортировка на месте
   - Делит массив на: < pivot, == pivot, > pivot
   - Хорошо работает с повторяющимися элементами

2. Элиминация хвостовой рекурсии — экономия памяти стека
   - Рекурсия заменяется циклом для правой части
   - Глубина рекурсии уменьшается до O(log n)

3. Медиана из трех — выбор хорошего опорного элемента
   - Уменьшает вероятность худшего случая O(n²)

4. Бинарный поиск для нахождения отрезков
   - findFirst — последний отрезок с start <= point
   - Затем линейный проход по подходящим отрезкам

Сложность:
- Сортировка: O(n log n)
- Поиск: O(m × (log n + K)), где K — количество подходящих отрезков*/