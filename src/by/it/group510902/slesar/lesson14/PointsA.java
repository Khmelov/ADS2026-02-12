package by.it.group510902.slesar.lesson14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

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

        int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            return parent[i] = find(parent[i]);
        }

        void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                if (size[rootI] < size[rootJ]) {
                    parent[rootI] = rootJ;
                    size[rootJ] += size[rootI];
                } else {
                    parent[rootJ] = rootI;
                    size[rootI] += size[rootJ];
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextDouble()) {
            return;
        }
        double d = scanner.nextDouble();
        int n = scanner.nextInt();
        double[][] points = new double[n][3];
        for (int i = 0; i < n; i++) {
            points[i][0] = scanner.nextDouble();
            points[i][1] = scanner.nextDouble();
            points[i][2] = scanner.nextDouble();
        }

        DSU dsu = new DSU(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dx = points[i][0] - points[j][0];
                double dy = points[i][1] - points[j][1];
                double dz = points[i][2] - points[j][2];
                double distSq = dx * dx + dy * dy + dz * dz;
                if (distSq < d * d) {
                    dsu.union(i, j);
                }
            }
        }

        List<Integer> clusterSizes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (dsu.parent[i] == i) {
                clusterSizes.add(dsu.size[i]);
            }
        }

        clusterSizes.sort(Collections.reverseOrder());
        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i) + (i == clusterSizes.size() - 1 ? "" : " "));
        }
        System.out.println();
    }
}