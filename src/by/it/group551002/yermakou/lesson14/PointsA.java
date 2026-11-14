package by.it.group551002.yermakou.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class PointsA {

    static int[] parent;
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
        if (size[ra] < size[rb]) {
            int t = ra;
            ra = rb;
            rb = t;
        }
        parent[rb] = ra;
        size[ra] += size[rb];
    }

    static double dist2(double[] p, double[] q) {
        double dx = p[0] - q[0];
        double dy = p[1] - q[1];
        double dz = p[2] - q[2];
        return dx * dx + dy * dy + dz * dz;
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

        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        double d2 = d * d;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (dist2(pts[i], pts[j]) < d2) {
                    union(i, j);
                }
            }
        }

        int cnt = 0;
        int[] sizes = new int[n];
        for (int i = 0; i < n; i++) {
            if (find(i) == i) {
                sizes[cnt++] = size[i];
            }
        }
        Arrays.sort(sizes, 0, cnt);

        StringBuilder sb = new StringBuilder();
        for (int i = cnt - 1; i >= 0; i--) {
            sb.append(sizes[i]);
            if (i > 0) sb.append(' ');
        }
        System.out.println(sb);
    }
}