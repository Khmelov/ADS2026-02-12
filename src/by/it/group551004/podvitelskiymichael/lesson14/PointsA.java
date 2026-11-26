package by.it.group551004.podvitelskiymichael.lesson14;

import java.util.Scanner;

public class PointsA {

    private static int[] parent;
    private static int[] size;

    private static int find(int x) {
        int root = x;
        while (parent[root] != root) {
            root = parent[root];
        }
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    private static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (size[a] < size[b]) {
            int t = a;
            a = b;
            b = t;
        }
        parent[b] = a;
        size[a] += size[b];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long d = scanner.nextLong();
        int n = scanner.nextInt();

        long[] x = new long[n];
        long[] y = new long[n];
        long[] z = new long[n];
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = scanner.nextLong();
            y[i] = scanner.nextLong();
            z[i] = scanner.nextLong();
            parent[i] = i;
            size[i] = 1;
        }

        long d2 = d * d;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dx = x[i] - x[j];
                long dy = y[i] - y[j];
                long dz = z[i] - z[j];
                // расстояние строго меньше D
                if (d > 0 && dx * dx + dy * dy + dz * dz < d2) {
                    union(i, j);
                }
            }
        }

        int[] sizes = new int[n];
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (find(i) == i) {
                sizes[count++] = size[i];
            }
        }

        for (int i = 1; i < count; i++) {
            int v = sizes[i];
            int j = i - 1;
            while (j >= 0 && sizes[j] < v) {
                sizes[j + 1] = sizes[j];
                j--;
            }
            sizes[j + 1] = v;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(sizes[i]);
        }
        System.out.println(sb);
    }
}