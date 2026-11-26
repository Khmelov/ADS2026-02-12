package by.it.group510902.ryzhkov.lesson14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PointsA {
    static class DSU {
        int[] parent, rank;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++)
                parent[i] = i;
        }

        int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int x, int y) {
            int rx = find(x), ry = find(y);
            if (rx == ry)
                return;
            if (rank[rx] < rank[ry])
                parent[rx] = ry;
            else if (rank[rx] > rank[ry])
                parent[ry] = rx;
            else {
                parent[ry] = rx;
                rank[rx]++;
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double D = sc.nextDouble();
        int N = sc.nextInt();
        double[][] pts = new double[N][3];
        for (int i = 0; i < N; i++) {
            pts[i][0] = sc.nextDouble();
            pts[i][1] = sc.nextDouble();
            pts[i][2] = sc.nextDouble();
        }
        DSU dsu = new DSU(N);
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                double dx = pts[i][0] - pts[j][0];
                double dy = pts[i][1] - pts[j][1];
                double dz = pts[i][2] - pts[j][2];
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (dist < D)
                    dsu.union(i, j);
            }
        }
        Map<Integer, Integer> count = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            count.put(root, count.getOrDefault(root, 0) + 1);
        }
        List<Integer> sizes = new ArrayList<>(count.values());
        sizes.sort((a, b) -> b - a);
        for (int size : sizes)
            System.out.print(size + " ");
    }
}
