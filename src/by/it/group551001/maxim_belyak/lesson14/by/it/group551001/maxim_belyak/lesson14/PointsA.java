package by.it.group551001.maxim_belyak.lesson14;

import java.util.*;

public class PointsA {


    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }


        int find(int v) {
            if (parent[v] != v) {
                parent[v] = find(parent[v]);
            }

            return parent[v];
        }


        void union(int a, int b) {
            a = find(a);
            b = find(b);

            if (a == b) {
                return;
            }


            if (size[a] < size[b]) {
                int temp = a;
                a = b;
                b = temp;
            }

            parent[b] = a;
            size[a] += size[b];
        }
    }


    static class Point {
        double x;
        double y;
        double z;

        Point(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        // Вычисление расстояния между двумя точками
        double distance(Point other) {
            double dx = x - other.x;
            double dy = y - other.y;
            double dz = z - other.z;

            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Считываем пороговое расстояние и количество точек
        double D = scanner.nextDouble();
        int N = scanner.nextInt();

        Point[] points = new Point[N];

        // Считываем координаты точек
        for (int i = 0; i < N; i++) {
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            double z = scanner.nextDouble();

            points[i] = new Point(x, y, z);
        }

        // Создаем систему непересекающихся множеств
        DSU dsu = new DSU(N);

        // Проверяем расстояние между каждой парой точек
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {

                if (points[i].distance(points[j]) < D) {
                    dsu.union(i, j);
                }
            }
        }

        // Подсчитываем количество точек в каждом кластере
        int[] clusterSizes = new int[N];

        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            clusterSizes[root]++;
        }

        // Собираем размеры непустых кластеров
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            if (clusterSizes[i] > 0) {
                result.add(clusterSizes[i]);
            }
        }

        // Сортируем размеры кластеров по убыванию
        result.sort(Collections.reverseOrder());

        // Выводим результат
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }

            System.out.print(result.get(i));
        }
    }
}