package by.it.group510902.makaruk.lesson14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class PointsA {

    private static class Point {
        final double x;
        final double y;
        final double z;

        Point(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
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
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA == rootB) {
                return;
            }
            if (size[rootA] < size[rootB]) {
                int temp = rootA;
                rootA = rootB;
                rootB = temp;
            }
            parent[rootB] = rootA;
            size[rootA] += size[rootB];
        }

        List<Integer> componentSizesDescending() {
            List<Integer> sizes = new ArrayList<>();
            for (int i = 0; i < parent.length; i++) {
                if (parent[i] == i) {
                    sizes.add(size[i]);
                }
            }
            sizes.sort(Collections.reverseOrder());
            return sizes;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
        if (!scanner.hasNextDouble() || !scanner.hasNextInt()) {
            return;
        }

        double d = scanner.nextDouble();
        int n = scanner.nextInt();
        if (n <= 0) {
            return;
        }

        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            double z = scanner.nextDouble();
            points[i] = new Point(x, y, z);
        }

        DSU dsu = new DSU(n);
        if (d > 0) {
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    double dx = points[i].x - points[j].x;
                    double dy = points[i].y - points[j].y;
                    double dz = points[i].z - points[j].z;
                    double dist = Math.hypot(Math.hypot(dx, dy), dz);
                    if (dist < d) {
                        dsu.union(i, j);
                    }
                }
            }
        }

        List<Integer> result = dsu.componentSizesDescending();
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(result.get(i));
        }
        if (!result.isEmpty()) {
            System.out.println();
        }
    }
}