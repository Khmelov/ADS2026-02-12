package by.it.group551003.klimuk.lesson14;

import java.util.Scanner;

public class PointsA {

    static int[] parent;
    static int[] rank;
    static int[] size;

    static int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    static void union(int a, int b) {
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
            rank[ra]++;
            size[ra] += size[rb];
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double d = sc.nextDouble();
        int n = sc.nextInt();

        double[][] pts = new double[n][3];
        for (int i = 0; i < n; i++) {
            pts[i][0] = sc.nextDouble();
            pts[i][1] = sc.nextDouble();
            pts[i][2] = sc.nextDouble();
        }
        sc.close();

        parent = new int[n];
        rank = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
            size[i] = 1;
        }

        double d2 = d * d;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dx = pts[i][0] - pts[j][0];
                double dy = pts[i][1] - pts[j][1];
                double dz = pts[i][2] - pts[j][2];
                double dist2 = dx * dx + dy * dy + dz * dz;
                if (dist2 < d2) {
                    union(i, j);
                }
            }
        }

        int[] counts = new int[n];
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            if (find(i) == i) {
                counts[cnt++] = size[i];
            }
        }

        for (int i = 1; i < cnt; i++) {
            int key = counts[i];
            int j = i - 1;
            while (j >= 0 && counts[j] < key) {
                counts[j + 1] = counts[j];
                j--;
            }
            counts[j + 1] = key;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cnt; i++) {
            if (i > 0) sb.append(' ');
            sb.append(counts[i]);
        }
        System.out.println(sb.toString());
    }
}
