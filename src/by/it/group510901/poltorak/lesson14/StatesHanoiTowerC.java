package by.it.group510901.poltorak.lesson14;

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
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                if (size[rootX] < size[rootY]) {
                    parent[rootX] = rootY;
                    size[rootY] += size[rootX];
                } else {
                    parent[rootY] = rootX;
                    size[rootX] += size[rootY];
                }
            }
        }
    }

    private static int stepIndex = 0;
    private static int[] maxHeights;
    private static int[] pegHeights;

    private static void hanoi(int n, int from, int to, int aux) {
        if (n == 0) {
            return;
        }

        hanoi(n - 1, from, aux, to);

        pegHeights[from]--;
        pegHeights[to]++;

        int maxH = pegHeights[0];
        if (pegHeights[1] > maxH) {
            maxH = pegHeights[1];
        }
        if (pegHeights[2] > maxH) {
            maxH = pegHeights[2];
        }

        maxHeights[stepIndex++] = maxH;

        hanoi(n - 1, aux, to, from);
    }

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

        maxHeights = new int[totalSteps];
        pegHeights = new int[3];
        pegHeights[0] = n;
        pegHeights[1] = 0;
        pegHeights[2] = 0;
        stepIndex = 0;

        hanoi(n, 0, 1, 2);

        DSU dsu = new DSU(totalSteps);

        int[] firstStepWithHeight = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            firstStepWithHeight[i] = -1;
        }

        for (int i = 0; i < totalSteps; i++) {
            int h = maxHeights[i];
            if (firstStepWithHeight[h] == -1) {
                firstStepWithHeight[h] = i;
            } else {
                dsu.union(firstStepWithHeight[h], i);
            }
        }

        int[] resultSizes = new int[n + 1];
        int count = 0;

        for (int i = 0; i < totalSteps; i++) {
            if (dsu.parent[i] == i) {
                resultSizes[count++] = dsu.size[i];
            }
        }

        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - 1 - i; j++) {
                if (resultSizes[j] > resultSizes[j + 1]) {
                    int tmp = resultSizes[j];
                    resultSizes[j] = resultSizes[j + 1];
                    resultSizes[j + 1] = tmp;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(resultSizes[i]);
        }

        System.out.println(sb);
    }
}