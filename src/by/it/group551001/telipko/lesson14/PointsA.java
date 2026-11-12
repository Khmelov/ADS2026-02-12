package by.it.group551001.telipko.lesson14;

import java.util.Scanner;


public class PointsA {

    static int[] parent;
    static int[] size;

    static int find(int x) {
        while (parent[x] != x) x = parent[x];
        return x;
    }

    static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) return;
        if (size[a] < size[b]) { int t = a; a = b; b = t; }
        parent[b] = a;
        size[a] += size[b];
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        double d = in.nextDouble();
        int n = in.nextInt();
        int[][] p = new int[n][3];
        for (int i = 0; i < n; i++) {
            p[i][0] = in.nextInt();
            p[i][1] = in.nextInt();
            p[i][2] = in.nextInt();
        }

        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }

        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {
                double dist = Math.hypot(Math.hypot(p[i][0] - p[j][0], p[i][1] - p[j][1]), p[i][2] - p[j][2]);
                if (dist < d) union(i, j);
            }

        System.out.println(clusterSizes(parent, size, n));
    }


    static String clusterSizes(int[] parent, int[] size, int n) {
        int[] res = new int[n];
        int k = 0;
        for (int i = 0; i < n; i++)
            if (parent[i] == i) res[k++] = size[i];
        for (int i = 1; i < k; i++) {
            int v = res[i], j = i - 1;
            while (j >= 0 && res[j] < v) { res[j + 1] = res[j]; j--; }
            res[j + 1] = v;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < k; i++) {
            if (i > 0) sb.append(' ');
            sb.append(res[i]);
        }
        return sb.toString();
    }
}
