package by.it.group551001.Barusevich.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class PointsA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextDouble()) return;

        double distance = scanner.nextDouble();
        int count = scanner.nextInt();

        double[][] points = new double[count][3];

        for (int i = 0; i < count; i++) {
            points[i][0] = scanner.nextDouble();
            points[i][1] = scanner.nextDouble();
            points[i][2] = scanner.nextDouble();
        }

        DisjointSet dsu = new DisjointSet(count);
        double limitSquared = distance * distance;

        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                double dx = points[i][0] - points[j][0];
                double dy = points[i][1] - points[j][1];
                double dz = points[i][2] - points[j][2];

                if (dx * dx + dy * dy + dz * dz < limitSquared) {
                    dsu.union(i, j);
                }
            }
        }

        int[] clusters = dsu.clusterSizes();
        Arrays.sort(clusters);

        for (int left = 0, right = clusters.length - 1;
             left < right; left++, right--) {
            int temp = clusters[left];
            clusters[left] = clusters[right];
            clusters[right] = temp;
        }

        print(clusters);
    }

    private static void print(int[] values) {
        StringBuilder result = new StringBuilder();

        for (int value : values) {
            if (result.length() > 0) {
                result.append(' ');
            }
            result.append(value);
        }

        System.out.println(result);
    }

    private static final class DisjointSet {
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

        int find(int value) {
            if (parent[value] != value) {
                parent[value] = find(parent[value]);
            }
            return parent[value];
        }

        void union(int first, int second) {
            int a = find(first);
            int b = find(second);

            if (a == b) return;

            if (size[a] < size[b]) {
                int temp = a;
                a = b;
                b = temp;
            }

            parent[b] = a;
            size[a] += size[b];
        }

        int[] clusterSizes() {
            int clusters = 0;

            for (int i = 0; i < parent.length; i++) {
                if (find(i) == i) clusters++;
            }

            int[] result = new int[clusters];
            int index = 0;

            for (int i = 0; i < parent.length; i++) {
                if (find(i) == i) {
                    result[index++] = size[i];
                }
            }

            return result;
        }
    }
}