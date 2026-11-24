
package by.it.group551004.sheev.lesson14;

import java.util.*;

public class PointsA {

    static class DSU {
        int[] parent;
        int[] rank;
        int[] size;

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
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);

            if (ra != rb) {
                if (rank[ra] < rank[rb]) {
                    int t = ra;
                    ra = rb;
                    rb = t;
                }

                parent[rb] = ra;
                size[ra] += size[rb];

                if (rank[ra] == rank[rb]) {
                    rank[ra]++;
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int d = sc.nextInt();
        int n = sc.nextInt();

        long[] x = new long[n];
        long[] y = new long[n];
        long[] z = new long[n];

        for (int i = 0; i < n; i++) {
            x[i] = sc.nextInt();
            y[i] = sc.nextInt();
            z[i] = sc.nextInt();
        }

        DSU dsu = new DSU(n);
        long limit = (long) d * d;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dx = x[i] - x[j];
                long dy = y[i] - y[j];
                long dz = z[i] - z[j];

                long distSq = dx * dx + dy * dy + dz * dz;

                if (distSq < limit) {
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

        Collections.sort(sizes, Collections.reverseOrder());

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(sizes.get(i));
        }

        System.out.println(sb);
    }
}
