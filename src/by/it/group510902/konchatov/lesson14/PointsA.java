package by.it.group510902.konchatov.lesson14;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PointsA {

    static class DSU {
        private final int[] parent;
        private final int[] size;
        private final int[] count;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            count = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 0;
                count[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rx = find(x);
            int ry = find(y);
            if (rx == ry) return;

            if (size[rx] < size[ry]) {
                parent[rx] = ry;
                count[ry] += count[rx];
            } else if (size[rx] > size[ry]) {
                parent[ry] = rx;
                count[rx] += count[ry];
            } else {
                parent[ry] = rx;
                count[rx] += count[ry];
                size[rx]++;
            }
        }

        int sizeOf(int x) {
            return count[find(x)];
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int d = sc.nextInt();
        int n = sc.nextInt();

        int[][] points = new int[n][3];
        for (int i = 0; i < n; i++) {
            points[i][0] = sc.nextInt();
            points[i][1] = sc.nextInt();
            points[i][2] = sc.nextInt();
        }

        DSU dsu = new DSU(n);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dx = points[i][0] - points[j][0];
                long dy = points[i][1] - points[j][1];
                long dz = points[i][2] - points[j][2];
                if (Math.hypot(Math.hypot(dx, dy), dz) < d) {
                    dsu.union(i, j);
                }
            }
        }

        List<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) == i) {
                sizes.add(dsu.sizeOf(i));
            }
        }

        sizes.sort((a, b) -> b - a);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(sizes.get(i));
        }
        System.out.println(sb);
    }
}