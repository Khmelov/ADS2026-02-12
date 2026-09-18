package by.it.group551001.bondarenko.lesson05;

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
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        quickSort3WayIterative(segments, 0, n - 1);

        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            stops[i] = segments[i].stop;
        }
        quickSortInts3WayIterative(stops, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];
            int started = findFirstGreater(segments, point);
            int ended = findFirstNotLess(stops, point);
            result[i] = started - ended;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private void quickSort3WayIterative(Segment[] arr, int low, int high) {
        int[] stackLow = new int[high - low + 1];
        int[] stackHigh = new int[high - low + 1];
        int top = -1;

        stackLow[++top] = low;
        stackHigh[top] = high;

        while (top >= 0) {
            low = stackLow[top];
            high = stackHigh[top--];

            if (low < high) {
                int lt = low;
                int gt = high;
                Segment pivot = arr[low];
                int i = low;

                while (i <= gt) {
                    int cmp = arr[i].compareTo(pivot);
                    if (cmp < 0) {
                        swapSegments(arr, lt, i);
                        lt++;
                        i++;
                    } else if (cmp > 0) {
                        swapSegments(arr, i, gt);
                        gt--;
                    } else {
                        i++;
                    }
                }

                if (lt - 1 - low > high - gt - 1) {
                    if (low < lt - 1) {
                        stackLow[++top] = low;
                        stackHigh[top] = lt - 1;
                    }
                    if (gt + 1 < high) {
                        stackLow[++top] = gt + 1;
                        stackHigh[top] = high;
                    }
                } else {
                    if (gt + 1 < high) {
                        stackLow[++top] = gt + 1;
                        stackHigh[top] = high;
                    }
                    if (low < lt - 1) {
                        stackLow[++top] = low;
                        stackHigh[top] = lt - 1;
                    }
                }
            }
        }
    }

    private void swapSegments(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private void quickSortInts3WayIterative(int[] arr, int low, int high) {
        int[] stackLow = new int[high - low + 1];
        int[] stackHigh = new int[high - low + 1];
        int top = -1;

        stackLow[++top] = low;
        stackHigh[top] = high;

        while (top >= 0) {
            low = stackLow[top];
            high = stackHigh[top--];

            if (low < high) {
                int lt = low;
                int gt = high;
                int pivot = arr[low];
                int i = low;

                while (i <= gt) {
                    if (arr[i] < pivot) {
                        int temp = arr[lt];
                        arr[lt] = arr[i];
                        arr[i] = temp;
                        lt++;
                        i++;
                    } else if (arr[i] > pivot) {
                        int temp = arr[i];
                        arr[i] = arr[gt];
                        arr[gt] = temp;
                        gt--;
                    } else {
                        i++;
                    }
                }

                if (lt - 1 - low > high - gt - 1) {
                    if (low < lt - 1) {
                        stackLow[++top] = low;
                        stackHigh[top] = lt - 1;
                    }
                    if (gt + 1 < high) {
                        stackLow[++top] = gt + 1;
                        stackHigh[top] = high;
                    }
                } else {
                    if (gt + 1 < high) {
                        stackLow[++top] = gt + 1;
                        stackHigh[top] = high;
                    }
                    if (low < lt - 1) {
                        stackLow[++top] = low;
                        stackHigh[top] = lt - 1;
                    }
                }
            }
        }
    }

    private int findFirstGreater(Segment[] arr, int point) {
        int left = 0;
        int right = arr.length - 1;
        int result = 0;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid].start <= point) {
                left = mid + 1;
                result = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }

    private int findFirstNotLess(int[] arr, int point) {
        int left = 0;
        int right = arr.length - 1;
        int result = 0;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < point) {
                left = mid + 1;
                result = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
            //тут вообще-то лучше доделать конструктор на случай если
            //концы отрезков придут в обратном порядке
            if (this.start > this.stop) {
                int temp = this.start;
                this.start = this.stop;
                this.stop = temp;
            }
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            return Integer.compare(this.start, o.start);
        }
    }

}