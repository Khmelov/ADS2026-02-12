package by.it.group551002.efimchikov.lesson14;

import java.util.Scanner;

public class PointsA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;

        int d = sc.nextInt();
        int n = sc.nextInt();

        int[][] points = new int[n][3];
        for (int i = 0; i < n; i++) {
            points[i][0] = sc.nextInt();
            points[i][1] = sc.nextInt();
            points[i][2] = sc.nextInt();
        }

        DSU dsu = new DSU(n);
        long d2 = (long) d * d;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dx = (long) points[i][0] - points[j][0];
                long dy = (long) points[i][1] - points[j][1];
                long dz = (long) points[i][2] - points[j][2];

                if (dx * dx + dy * dy + dz * dz < d2) {
                    dsu.union(i, j);
                }
            }
        }

        int[] sizes = dsu.getSizesDescending();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sizes.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(sizes[i]);
        }
        System.out.println(sb.toString().trim());
    }

    private static class DSU {
        private final int[] parent;
        private final int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            int root = x;
            while (parent[root] != root) {
                root = parent[root];
            }

            while (parent[x] != x) {
                int next = parent[x];
                parent[x] = root;
                x = next;
            }
            return root;
        }

        void union(int a, int b) {
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

        int[] getSizesDescending() {
            int n = parent.length;
            int[] result = new int[n];
            int k = 0;

            for (int i = 0; i < n; i++) {
                if (find(i) == i) {
                    result[k++] = size[i];
                }
            }

            for (int i = 1; i < k; i++) {
                int x = result[i];
                int j = i - 1;
                while (j >= 0 && result[j] < x) {
                    result[j + 1] = result[j];
                    j--;
                }
                result[j + 1] = x;
            }

            int[] answer = new int[k];
            System.arraycopy(result, 0, answer, 0, k);
            return answer;
        }
    }
}