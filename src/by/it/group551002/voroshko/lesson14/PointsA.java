package by.it.group551002.voroshko.lesson14;

import java.util.*;

public class PointsA {
    static class DSU {
        private int[] parent, rank;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        int find(int x) {
            if (x == parent[x]) return x;
            return parent[x] = find(parent[x]);
        }

        void union(int x, int y) {
            x = find(x);
            y = find(y);

            if (x == y) return;

            if (rank[x] < rank[y]) {
                int t = x;
                x = y;
                y = t;
            }

            parent[y] = x;

            rank[x] += rank[y];
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int D = sc.nextInt();
        int N = sc.nextInt();

        int[] x = new int[N];
        int[] y = new int[N];
        int[] z = new int[N];

        for (int i = 0; i < N; i++) {
            x[i] = sc.nextInt();
            y[i] = sc.nextInt();
            z[i] = sc.nextInt();
        }

        sc.close();

        DSU dsu = new DSU(N);

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                int dx = x[i] - x[j];
                int dy = y[i] - y[j];
                int dz = z[i] - z[j];
                if (dx * dx + dy * dy + dz * dz < D * D) {
                    dsu.union(i, j);
                }
            }
        }

        Map<Integer, Integer> sizes = new HashMap<>();

        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            sizes.merge(root, 1, Integer::sum);
        }

        List<Integer> res = new ArrayList<>(sizes.values());
        Collections.sort(res, Collections.reverseOrder());

        for (int i = 0; i < res.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(res.get(i));
        }
        System.out.println();
    }
}
