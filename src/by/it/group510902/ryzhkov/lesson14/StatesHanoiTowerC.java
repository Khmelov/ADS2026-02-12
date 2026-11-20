package by.it.group510902.ryzhkov.lesson14;

public class StatesHanoiTowerC {
    static class DSU {
        int[] parent, size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
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
            if (size[rx] < size[ry]) {
                parent[rx] = ry;
                size[ry] += size[rx];
            } else {
                parent[ry] = rx;
                size[rx] += size[ry];
            }
        }

        int getSize(int x) {
            return size[find(x)];
        }
    }

    static int[] heights = new int[3];
    static int[] representative;
    static DSU dsu;
    static int step = 0;

    static void hanoi(int n, int from, int to, int aux) {
        if (n == 0)
            return;
        hanoi(n - 1, from, aux, to);

        heights[from]--;
        heights[to]++;
        int maxH = Math.max(heights[0], Math.max(heights[1], heights[2]));
        if (representative[maxH] == -1) {
            representative[maxH] = step;
        } else {
            dsu.union(step, representative[maxH]);
        }
        step++;
        hanoi(n - 1, aux, to, from);
    }

    public static void main(String[] args) {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        int N = sc.nextInt();
        int totalSteps = (int) Math.pow(2, N) - 1;
        dsu = new DSU(totalSteps);
        representative = new int[N + 1];
        java.util.Arrays.fill(representative, -1);
        step = 0;
        java.util.Arrays.fill(heights, 0);
        heights[0] = N;
        hanoi(N, 0, 1, 2);

        int[] clusterSizes = new int[totalSteps];
        boolean[] counted = new boolean[totalSteps];
        int clusterCount = 0;
        for (int i = 0; i < totalSteps; i++) {
            int root = dsu.find(i);
            if (!counted[root]) {
                counted[root] = true;
                clusterSizes[clusterCount++] = dsu.getSize(root);
            }
        }
        java.util.Arrays.sort(clusterSizes, 0, clusterCount);
        for (int i = 0; i < clusterCount; i++) {
            System.out.print(clusterSizes[i] + " ");
        }
    }
}
