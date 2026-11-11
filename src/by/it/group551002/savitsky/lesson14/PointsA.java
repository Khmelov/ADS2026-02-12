package by.it.group551002.savitsky.lesson14;

import java.util.*;

public class PointsA {

    static class DSU {
        int[] parent;
        int[] size;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int i) {
            if (parent[i] == i) {
                return i;
            }

            return parent[i] = find(parent[i]);
        }

        public void union(int i, int j) {
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
        if (!scanner.hasNextDouble()) return;

        double D = scanner.nextDouble();
        int N = scanner.nextInt();

        List<int[]> points = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            points.add(new int[]{x, y, z});
        }

        DSU dsu = new DSU(N);

        for (int i = 0; i < N; i++) {
            int[] p1 = points.get(i);
            for (int j = i + 1; j < N; j++) {
                int[] p2 = points.get(j);

                double distance = Math.hypot(Math.hypot(p1[0] - p2[0], p1[1] - p2[1]), p1[2] - p2[2]);

                if (distance < D) {
                    dsu.union(i, j);
                }
            }
        }

        List<Integer> clusterSizes = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            if (dsu.parent[i] == i) {
                clusterSizes.add(dsu.size[i]);
            }
        }
        clusterSizes.sort(Collections.reverseOrder());

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < clusterSizes.size(); i++) {
            result.append(clusterSizes.get(i));

            if (i < clusterSizes.size() - 1) {
                result.append(" ");
            }
        }

        System.out.println(result.toString());
    }
}