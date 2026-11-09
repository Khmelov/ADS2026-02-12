package by.it.group551001.docenko.lesson14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

public class PointsA {

    private static final class Dsu {
        private final int[] parent;
        private final int[] size;

        Dsu(int count) {
            parent = new int[count];
            size = new int[count];
            for (int i = 0; i < count; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int element) {
            while (parent[element] != element) {
                element = parent[element];
            }
            return element;
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

        List<Integer> clusterSizes() {
            List<Integer> sizes = new ArrayList<>();
            for (int i = 0; i < parent.length; i++) {
                if (parent[i] == i) {
                    sizes.add(size[i]);
                }
            }
            return sizes;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double maxDistance = Double.parseDouble(scanner.next());
        int count = Integer.parseInt(scanner.next());

        double[][] points = new double[count][3];
        for (int i = 0; i < count; i++) {
            for (int axis = 0; axis < 3; axis++) {
                points[i][axis] = Double.parseDouble(scanner.next());
            }
        }

        Dsu dsu = new Dsu(count);
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                if (distance(points[i], points[j]) < maxDistance) {
                    dsu.union(i, j);
                }
            }
        }

        List<Integer> sizes = dsu.clusterSizes();
        sizes.sort(Collections.reverseOrder());
        StringJoiner out = new StringJoiner(" ");
        for (int size : sizes) {
            out.add(String.valueOf(size));
        }
        System.out.println(out);
    }

    private static double distance(double[] first, double[] second) {
        double dx = first[0] - second[0];
        double dy = first[1] - second[1];
        double dz = first[2] - second[2];
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
