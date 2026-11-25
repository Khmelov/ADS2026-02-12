package by.it.group551002.kuzmenia.lesson14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class PointsA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Читаем расстояние и число точек
        double d = Double.parseDouble(scanner.next());
        int n = Integer.parseInt(scanner.next());

        // Читаем точки
        double[][] points = new double[n][3];
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < 3; k++) {
                points[i][k] = Double.parseDouble(scanner.next());
            }
        }

        // Объединение кластеров по расстоянию
        DSU dsu = new DSU(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (isClose(points[i], points[j], d)) {
                    dsu.union(i, j);
                }
            }
        }

        // Сортировка кластеров по убывнию (тест проверяет на убывание)
        List<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) == i) {
                sizes.add(dsu.size(i));
            }
        }
        Collections.sort(sizes, Collections.reverseOrder());

        // Постройка строки результата
        StringBuilder result = new StringBuilder();
        for (int s : sizes) {
            if (result.length() > 0) {
                result.append(' ');
            }
            result.append(s);
        }
        System.out.println(result);
    }

    // Метод для определения расстояния между точками
    private static boolean isClose(double[] a, double[] b, double d) {
        double dx = a[0] - b[0];
        double dy = a[1] - b[1];
        double dz = a[2] - b[2];
        return Math.sqrt(dx * dx + dy * dy + dz * dz) < d;
    }

    private static class DSU {
        private final int[] parent;
        private final int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            int root = x;
            while (parent[root] != root) {
                root = parent[root];
            }
            while (parent[x] != root) {
                int next = parent[x];
                parent[x] = root;
                x = next;
            }
            return root;
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) {
                return;
            }
            if (size[ra] < size[rb]) {
                int tmp = ra;
                ra = rb;
                rb = tmp;
            }
            parent[rb] = ra;
            size[ra] += size[rb];
        }

        int size(int x) {
            return size[find(x)];
        }
    }
}