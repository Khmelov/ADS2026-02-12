package by.it.group551002.sevkovich.lesson14;

import java.util.*;

public class PointsA {

    private static class DSU {
        private int[] size;
        private int[] parent;

        public DSU(int n) {
            size = new int[n];
            parent = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        private int find(int x) {
            if (parent[x] == x) return x;
            return parent[x] = find(parent[x]);
        }

        public boolean union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return false;
            if (size[ra] < size[rb]) {
                int t = ra; ra = rb; rb = t;
            }
            parent[rb] = ra;
            size[ra] += size[rb];
            return true;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int distance = sc.nextInt();
        int n = sc.nextInt();

        int[][] points = new int[n][3];
        for (int i = 0; i < n; i++) {
            points[i][0] = sc.nextInt();
            points[i][1] = sc.nextInt();
            points[i][2] = sc.nextInt();
        }

        DSU dsu = new DSU(n);
        long limitSq = (long) distance * distance;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dx = points[i][0] - points[j][0];
                long dy = points[i][1] - points[j][1];
                long dz = points[i][2] - points[j][2];
                if (dx * dx + dy * dy + dz * dz < limitSq) {
                    dsu.union(i, j);
                }
            }
        }

        List<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) == i) {
                sizes.add(dsu.size[i]);
            }
        }

        sizes.sort(Collections.reverseOrder());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(sizes.get(i));
        }
        System.out.println(sb);
    }
}