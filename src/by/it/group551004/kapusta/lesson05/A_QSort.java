package by.it.group551004.kapusta.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // число отрезков
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];

        // число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // читаем отрезки
        for (int i = 0; i < n; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            segments[i] = new Segment(a, b);
        }

        // читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // сортируем отрезки по start
        Arrays.sort(segments);

        // для каждой точки считаем количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];
            result[i] = countSegments(segments, point);
        }

        return result;
    }

    // бинарный поиск + проверка попадания точки в отрезки
    private int countSegments(Segment[] segments, int point) {
        int count = 0;

        // бинарный поиск первого отрезка, у которого start <= point
        int left = 0;
        int right = segments.length - 1;
        int pos = -1;

        while (left <= right) {
            int mid = (left + right) / 2;

            if (segments[mid].start <= point) {
                pos = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        if (pos == -1) return 0;

        // идём назад от pos, пока start <= point
        for (int i = pos; i >= 0; i--) {
            if (segments[i].start <= point && point <= segments[i].stop) {
                count++;
            } else if (segments[i].start > point) {
                break;
            }
        }

        return count;
    }

    // отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start <= stop) {
                this.start = start;
                this.stop = stop;
            } else {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }
}
