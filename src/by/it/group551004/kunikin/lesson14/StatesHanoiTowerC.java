package by.it.group551004.kunikin.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {
    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        void union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA != rootB) {
                // Union by size
                if (size[rootA] < size[rootB]) {
                    parent[rootA] = rootB;
                    size[rootB] += size[rootA];
                } else {
                    parent[rootB] = rootA;
                    size[rootA] += size[rootB];
                }
            }
        }
    }

    private static int stepIndex = 0;
    private static int[] pegHeights = new int[3];
    private static int[] lastWithHeight;
    private static DSU dsu;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            return;
        }

        int n = scanner.nextInt();
        if (n <= 0) {
            return;
        }

        int totalSteps = (1 << n) - 1;
        dsu = new DSU(totalSteps);

        pegHeights[0] = n;
        pegHeights[1] = 0;
        pegHeights[2] = 0;

        lastWithHeight = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            lastWithHeight[i] = -1;
        }

        stepIndex = 0;
        solveHanoi(n, 0, 1, 2);

        // Collect sizes of subtrees without using Collections
        int[] resultSizes = new int[n + 1];
        int count = 0;

        for (int h = 1; h <= n; h++) {
            if (lastWithHeight[h] != -1) {
                int root = dsu.find(lastWithHeight[h]);
                resultSizes[count++] = dsu.size[root];
            }
        }

        // Insertion sort in ascending order (no collections used)
        for (int i = 1; i < count; i++) {
            int key = resultSizes[i];
            int j = i - 1;
            while (j >= 0 && resultSizes[j] > key) {
                resultSizes[j + 1] = resultSizes[j];
                j--;
            }
            resultSizes[j + 1] = key;
        }

        // Output space-separated sizes
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(resultSizes[i]);
        }
        System.out.println(sb.toString());
    }

    private static void solveHanoi(int n, int from, int to, int aux) {
        if (n == 0) {
            return;
        }
        solveHanoi(n - 1, from, aux, to);

        // Perform move
        pegHeights[from]--;
        pegHeights[to]++;

        int currentStep = stepIndex++;
        int maxH = pegHeights[0];
        if (pegHeights[1] > maxH) {
            maxH = pegHeights[1];
        }
        if (pegHeights[2] > maxH) {
            maxH = pegHeights[2];
        }

        if (lastWithHeight[maxH] != -1) {
            dsu.union(lastWithHeight[maxH], currentStep);
        }
        lastWithHeight[maxH] = currentStep;

        solveHanoi(n - 1, aux, to, from);
    }
}
