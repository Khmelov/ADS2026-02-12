package by.it.group551002.shinkevich.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь.
На площади установлена одна или несколько камер.
Известны данные о том, когда каждая из них включалась и выключалась (отрезки работы)
Известен список событий на площади (время начала каждого события).
Вам необходимо определить для каждого события сколько камер его записали.

В первой строке задано два целых числа:
    число включений камер (отрезки) 1<=n<=50000
    число событий (точки) 1<=m<=50000.

Следующие n строк содержат по два целых числа ai и bi (ai<=bi) -
координаты концов отрезков (время работы одной какой-то камеры).
Последняя строка содержит m целых чисел - координаты точек.
Все координаты не превышают 10E8 по модулю (!).

Точка считается принадлежащей отрезку, если она находится внутри него или на границе.

Для каждой точки в порядке их появления во вводе выведите,
скольким отрезкам она принадлежит.
    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/

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

		int n = scanner.nextInt();
		Segment[] segments = new Segment[n];

		int m = scanner.nextInt();
		int[] points = new int[m];
		int[] result = new int[m];

		for (int i = 0; i < n; i++) {
			segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
		}
		for (int i = 0; i < m; i++) {
			points[i] = scanner.nextInt();
		}


		quickSort(segments, 0, n - 1);

		for (int i = 0; i < m; i++) {
			int p = points[i];
			int count = 0;
			for (int j = 0; j < n; j++) {
				if (segments[j].start > p) {
					break;
				}

				if (segments[j].stop >= p) {
					count++;
				}
			}
			result[i] = count;
		}

		return result;
	}

	private void quickSort(Segment[] a, int left, int right) {
		if (left >= right) return;

		int i = left;
		int j = right;
		Segment pivot = a[left + (right - left) / 2];

		while (i <= j) {
			while (a[i].compareTo(pivot) < 0) i++;
			while (a[j].compareTo(pivot) > 0) j--;

			if (i <= j) {
				Segment temp = a[i];
				a[i] = a[j];
				a[j] = temp;
				i++;
				j--;
			}
		}

		quickSort(a, left, j);
		quickSort(a, i, right);
	}

	private class Segment implements Comparable<Segment> {
		int start;
		int stop;

		Segment(int start, int stop) {
			this.start = Math.min(start, stop);
			this.stop = Math.max(start, stop);
		}

		@Override
		public int compareTo(Segment o) {
			return Integer.compare(this.start, o.start);
		}
	}

}
