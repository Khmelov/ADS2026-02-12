package by.it.group551004.kunikin.lesson14;

import java.util.*;

public class PointsA {
    static class DisjointSet {
        private final int[] parent;
        private final int[] size;

        DisjointSet(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        void union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA != rootB) {
                // Union by size
                if (size[rootA] < size[rootB]) {
                    parent[rootA] = rootB;
                    size[rootB] += size[rootA];
                } else {
                    parent[rootB] = rootA;
                    size[rootA] += size[rootB];
                }
            }
        }

        int getSize(int root) {
            return size[root];
        }

        boolean isRoot(int x) {
            return parent[x] == x;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextDouble()) {
            return;
        }

        double maxDist = scanner.nextDouble();
        int count = scanner.nextInt();

        int[][] coords = new int[count][3];
        for (int i = 0; i < count; i++) {
            coords[i][0] = scanner.nextInt();
            coords[i][1] = scanner.nextInt();
            coords[i][2] = scanner.nextInt();
        }

        DisjointSet dsu = new DisjointSet(count);
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                double dx = coords[i][0] - coords[j][0];
                double dy = coords[i][1] - coords[j][1];
                double dz = coords[i][2] - coords[j][2];
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

                if (dist < maxDist) {
                    dsu.union(i, j);
                }
            }
        }

        List<Integer> clusterSizes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (dsu.isRoot(i)) {
                clusterSizes.add(dsu.getSize(i));
            }
        }

        clusterSizes.sort(Collections.reverseOrder());

        StringJoiner joiner = new StringJoiner(" ");
        for (int sz : clusterSizes) {
            joiner.add(String.valueOf(sz));
        }

        System.out.println(joiner);
    }
}
