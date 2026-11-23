package by.it.group551001.artobolevskiy.lesson14;

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

        int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            return parent[i] = find(parent[i]);
        }

        void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                if (size[rootI] < size[rootJ]) {
                    int temp = rootI;
                    rootI = rootJ;
                    rootJ = temp;
                }
                parent[rootJ] = rootI;
                size[rootI] += size[rootJ];
            }
        }
    }

    static int[] heights;
    static int[] firstStepWithMaxHeight;
    static DSU dsu;
    static int stepIndex;

    static void solveHanoi(int n, int from, int to, int aux) {
        if (n == 0) {
            return;
        }
        solveHanoi(n - 1, from, aux, to);

        heights[from]--;
        heights[to]++;

        int maxH = heights[0];
        if (heights[1] > maxH) maxH = heights[1];
        if (heights[2] > maxH) maxH = heights[2];

        if (firstStepWithMaxHeight[maxH] == -1) {
            firstStepWithMaxHeight[maxH] = stepIndex;
        } else {
            dsu.union(firstStepWithMaxHeight[maxH], stepIndex);
        }
        stepIndex++;

        solveHanoi(n - 1, aux, to, from);
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

        stepIndex = 0;
        int totalSteps = (1 << n) - 1;
        dsu = new DSU(totalSteps);

        heights = new int[]{n, 0, 0};

        firstStepWithMaxHeight = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            firstStepWithMaxHeight[i] = -1;
        }

        solveHanoi(n, 0, 1, 2);

        int[] clusterSizes = new int[n + 1];
        int clusterCount = 0;
        for (int i = 0; i < totalSteps; i++) {
            if (dsu.parent[i] == i) {
                clusterSizes[clusterCount++] = dsu.size[i];
            }
        }

        for (int i = 1; i < clusterCount; i++) {
            int key = clusterSizes[i];
            int j = i - 1;
            while (j >= 0 && clusterSizes[j] > key) {
                clusterSizes[j + 1] = clusterSizes[j];
                j--;
            }
            clusterSizes[j + 1] = key;
        }

        for (int i = 0; i < clusterCount; i++) {
            System.out.print(clusterSizes[i] + (i == clusterCount - 1 ? "" : " "));
        }
        System.out.println();
    }
}
