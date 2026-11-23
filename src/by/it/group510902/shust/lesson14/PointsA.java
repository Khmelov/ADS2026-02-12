package by.it.group510902.shust.lesson14;

import java.util.Scanner;
import java.util.Arrays;

public class PointsA {
    static int[] parent;
    static int[] size;

    static int find(int i) {
        if (parent[i] == i) return i;
        return parent[i] = find(parent[i]);
    }

    static void union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);
        if (rootI != rootJ) {
            if (size[rootI] < size[rootJ]) {
                int temp = rootI; rootI = rootJ; rootJ = temp;
            }
            parent[rootJ] = rootI;
            size[rootI] += size[rootJ];
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;
        int distance = scanner.nextInt();
        int n = scanner.nextInt();

        int[][] points = new int[n][3];
        for (int i = 0; i < n; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
            points[i][2] = scanner.nextInt();
        }

        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = Math.hypot(Math.hypot(points[i][0] - points[j][0], points[i][1] - points[j][1]), points[i][2] - points[j][2]);
                if (dist < distance) {
                    union(i, j);
                }
            }
        }

        int[] clusterSizes = new int[n];
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (parent[i] == i) {
                clusterSizes[count++] = size[i];
            }
        }

        int[] result = Arrays.copyOf(clusterSizes, count);
        Arrays.sort(result);

        for (int i = 0; i < result.length / 2; i++) {
            int temp = result[i];
            result[i] = result[result.length - 1 - i];
            result[result.length - 1 - i] = temp;
        }

        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + (i == result.length - 1 ? "" : " "));
        }
        System.out.println();
    }
}