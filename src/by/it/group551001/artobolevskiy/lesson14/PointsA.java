package by.it.group551001.artobolevskiy.lesson14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class PointsA {

    static class Point {
        double x, y, z;

        Point(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

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

        int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            return parent[i] = find(parent[i]); // Сжатие путей
        }

        void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                // Эвристика по размеру: меньшее дерево присоединяем к большему
                if (size[rootI] < size[rootJ]) {
                    int temp = rootI;
                    rootI = rootJ;
                    rootJ = temp;
                }
                parent[rootJ] = rootI;
                size[rootI] += size[rootJ];
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (!scanner.hasNextDouble()) return;
        double d = scanner.nextDouble();
        double dSquared = d * d; // Используем квадрат расстояния для точности и скорости

        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();

        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            double z = scanner.nextDouble();
            points[i] = new Point(x, y, z);
        }

        DSU dsu = new DSU(n);

        // Перебираем все пары точек
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dx = points[i].x - points[j].x;
                double dy = points[i].y - points[j].y;
                double dz = points[i].z - points[j].z;
                double distSquared = dx * dx + dy * dy + dz * dz;

                // Проверяем условие [0, D) строго меньше D
                if (distSquared < dSquared) {
                    dsu.union(i, j);
                }
            }
        }

        // Собираем размеры всех корневых вершин (уникальных кластеров)
        List<Integer> clusterSizes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (dsu.parent[i] == i) {
                clusterSizes.add(dsu.size[i]);
            }
        }

        // Сортируем размеры кластеров по возрастанию
        clusterSizes.sort(Collections.reverseOrder());

        // Выводим результат
        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i) + (i == clusterSizes.size() - 1 ? "" : " "));
        }
        System.out.println();

        scanner.close();
    }
}

