package by.it.group551002.kurzhalov.lesson14;

import java.util.Scanner;

public class PointsA {

    // ================= DSU с эвристикой по рангу =================

    private static class DSU {
        private final int[] parent;
        private final int[] rank;
        private final int[] size;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
                size[i] = 1;
            }
        }

        int find(int x) {
            while (parent[x] != x) {
                parent[x] = parent[parent[x]];
                x = parent[x];
            }
            return x;
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;
            if (rank[ra] < rank[rb]) {
                parent[ra] = rb;
                size[rb] += size[ra];
            } else if (rank[ra] > rank[rb]) {
                parent[rb] = ra;
                size[ra] += size[rb];
            } else {
                parent[rb] = ra;
                size[ra] += size[rb];
                rank[ra]++;
            }
        }

        int componentSize(int x) {
            return size[find(x)];
        }
    }

    // ================= main =================

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int distance = scanner.nextInt();
        int n = scanner.nextInt();

        int[] x = new int[n];
        int[] y = new int[n];
        int[] z = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = scanner.nextInt();
            y[i] = scanner.nextInt();
            z[i] = scanner.nextInt();
        }
        scanner.close();

        DSU dsu = new DSU(n);
        long d2 = (long) distance * distance;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dx = (long) x[i] - x[j];
                long dy = (long) y[i] - y[j];
                long dz = (long) z[i] - z[j];
                long dist2 = dx * dx + dy * dy + dz * dz;
                if (dist2 < d2) {
                    dsu.union(i, j);
                }
            }
        }

        int[] sizes = new int[n];
        int count = 0;
        boolean[] seenRoot = new boolean[n];
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            if (!seenRoot[root]) {
                seenRoot[root] = true;
                sizes[count++] = dsu.componentSize(root);
            }
        }

        // Сортировка по УБЫВАНИЮ (как требует тест calculateTestOut)
        for (int i = 1; i < count; i++) {
            int key = sizes[i];
            int j = i - 1;
            while (j >= 0 && sizes[j] < key) {
                sizes[j + 1] = sizes[j];
                j--;
            }
            sizes[j + 1] = key;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) sb.append(' ');
            sb.append(sizes[i]);
        }
        System.out.println(sb.toString());
    }
}