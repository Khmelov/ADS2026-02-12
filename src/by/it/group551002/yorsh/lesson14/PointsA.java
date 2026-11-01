package by.it.group551002.yorsh.lesson14;

import java.util.*;

public class PointsA {
    static class Point {
        double x, y, z;
        Point(double x, double y, double z) {
            this.x = x; this.y = y; this.z = z;
        }
    }

    static int[] parent;
    static int[] size;

    static int find(int i) {
        if (parent[i] == i)
            return i;
        return parent[i] = find(parent[i]); // эвристика сжатия пути
    }

    static void union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);
        if (rootI != rootJ) {
            // эвристика по размеру поддерева
            if (size[rootI] < size[rootJ]) {
                parent[rootI] = rootJ;
                size[rootJ] += size[rootI];
            } else {
                parent[rootJ] = rootI;
                size[rootI] += size[rootJ];
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextDouble()) return;
        double D = scanner.nextDouble();
        int n = scanner.nextInt();

        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        }

        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dx = points[i].x - points[j].x;
                double dy = points[i].y - points[j].y;
                double dz = points[i].z - points[j].z;
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (dist < D) {
                    union(i, j);
                }
            }
        }

        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = find(i);
            clusterSizes.put(root, size[root]);
        }

        List<Integer> resultSizes = new ArrayList<>(clusterSizes.values());
        resultSizes.sort(Collections.reverseOrder()); // Сортировка по убыванию

        StringBuilder sb = new StringBuilder();
        for (int s : resultSizes) {
            sb.append(s).append(" ");
        }
        System.out.println(sb.toString().trim());
    }
}