package by.it.group551003.kakhno.lesson14;

import java.util.*;

public class PointsA {

    static class DSU {
        int[] parent;
        int[] rank;
        int[] size;

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
                size[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY)
                return;

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
                size[rootX] += size[rootY];
            }
        }

        public int getSize(int x) {
            return size[find(x)];
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double D = scanner.nextDouble();
        int N = scanner.nextInt();

        int[][] points = new int[N][3];
        for (int i = 0; i < N; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
            points[i][2] = scanner.nextInt();
        }

        DSU dsu = new DSU(N);

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                double dx = points[i][0] - points[j][0];
                double dy = points[i][1] - points[j][1];
                double dz = points[i][2] - points[j][2];
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (dist < D)
                    dsu.union(i, j);
            }
        }

        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, dsu.getSize(root));
        }

        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        sizes.sort((a, b) -> b - a);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0)
                result.append(" ");
            result.append(sizes.get(i));
        }
        System.out.println(result.toString());

        scanner.close();
    }
}
