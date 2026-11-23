package by.it.group510901.chibisov.lesson14;

import java.util.*;

public class PointsA {

    private static class DSU {

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

        public int find(int x) {
            if (parent[x] == x) return x;
            return parent[x] = find(parent[x]);
        }

        public void union(int x, int y) {
            x = find(x);
            y = find(y);

            if (x == y) return;

            if (size[x] < size[y]) {
                parent[x] = y;
                size[y] += size[x];
            } else {
                parent[y] = x;
                size[x] += size[y];
            }
        }
    }

    private static class Point {

        int x, y, z;

        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double distanceTo(Point o) {
            int dx = this.x - o.x;
            int dy = this.y - o.y;
            int dz = this.z - o.z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int d = scanner.nextInt();
        int n = scanner.nextInt();

        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            pts[i] = new Point(x, y, z);
        }

        DSU dsu = new DSU(n);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (pts[i].distanceTo(pts[j]) < d) {
                    dsu.union(i, j);
                }
            }
        }

        int[] result = new int[n];
        int count = 0;

        for (int i = 0; i < n; i++) {
            if (dsu.parent[i] == i) {
                result[count++] = dsu.size[i];
            }
        }
        Arrays.sort(result, 0, count);

        StringBuilder sb = new StringBuilder();
        for (int i = count - 1; i >= 0; i--) {
            sb.append(result[i]);
            if (i > 0) sb.append(" ");
        }
        System.out.println(sb);
    }
}
