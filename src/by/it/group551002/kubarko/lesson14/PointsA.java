package by.it.group551002.kubarko.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class PointsA {

    private static class DisjointSet {
        private final int[] parent;
        private final int[] size;

        DisjointSet(int count) {
            parent = new int[count];
            size = new int[count];
            for (int i = 0; i < count; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int item) {
            if (parent[item] != item) {
                parent[item] = find(parent[item]);
            }
            return parent[item];
        }

        void union(int first, int second) {
            int firstRoot = find(first);
            int secondRoot = find(second);
            if (firstRoot == secondRoot) {
                return;
            }
            if (size[firstRoot] < size[secondRoot]) {
                int swap = firstRoot;
                firstRoot = secondRoot;
                secondRoot = swap;
            }
            parent[secondRoot] = firstRoot;
            size[firstRoot] += size[secondRoot];
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int distance = scanner.nextInt();
        int pointCount = scanner.nextInt();
        int[][] points = new int[pointCount][3];
        for (int i = 0; i < pointCount; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
            points[i][2] = scanner.nextInt();
        }

        DisjointSet clusters = new DisjointSet(pointCount);
        double limitSquared = (double) distance * distance;
        for (int i = 0; i < pointCount; i++) {
            for (int j = i + 1; j < pointCount; j++) {
                double dx = (double) points[i][0] - points[j][0];
                double dy = (double) points[i][1] - points[j][1];
                double dz = (double) points[i][2] - points[j][2];
                if (dx * dx + dy * dy + dz * dz < limitSquared) {
                    clusters.union(i, j);
                }
            }
        }

        int[] clusterSizes = new int[pointCount];
        int clusterCount = 0;
        for (int i = 0; i < pointCount; i++) {
            if (clusters.find(i) == i) {
                clusterSizes[clusterCount++] = clusters.size[i];
            }
        }
        Arrays.sort(clusterSizes, 0, clusterCount);
        printSizes(clusterSizes, clusterCount);
    }

    private static void printSizes(int[] sizes, int count) {
        StringBuilder result = new StringBuilder();
        for (int i = count - 1; i >= 0; i--) {
            if (i < count - 1) {
                result.append(' ');
            }
            result.append(sizes[i]);
        }
        System.out.println(result);
    }
}
