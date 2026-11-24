package by.it.group510902.slesar.lesson14;

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
                    parent[rootI] = rootJ;
                    size[rootJ] += size[rootI];
                } else {
                    parent[rootJ] = rootI;
                    size[rootI] += size[rootJ];
                }
            }
        }
    }

    static int stepIndex = 0;
    static int[] heights = new int[3];
    static int[] maxHeights;

    static void solveHanoi(int n, int from, int to, int aux) {
        if (n == 0) {
            return;
        }
        solveHanoi(n - 1, from, aux, to);
        heights[from]--;
        heights[to]++;
        maxHeights[stepIndex++] = Math.max(heights[0], Math.max(heights[1], heights[2]));
        solveHanoi(n - 1, aux, to, from);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            return;
        }
        int n = scanner.nextInt();

        int totalSteps = (1 << n) - 1;
        maxHeights = new int[totalSteps];
        heights[0] = n;
        heights[1] = 0;
        heights[2] = 0;
        stepIndex = 0;

        solveHanoi(n, 0, 1, 2);

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

        int distinctCount = 0;
        for (int i = 0; i < totalSteps; i++) {
            if (dsu.parent[i] == i) {
                distinctCount++;
            }
        }

        int[] resultSizes = new int[distinctCount];
        int idx = 0;
        for (int i = 0; i < totalSteps; i++) {
            if (dsu.parent[i] == i) {
                resultSizes[idx++] = dsu.size[i];
            }
        }

        for (int i = 0; i < resultSizes.length - 1; i++) {
            for (int j = 0; j < resultSizes.length - 1 - i; j++) {
                if (resultSizes[j] > resultSizes[j + 1]) {
                    int temp = resultSizes[j];
                    resultSizes[j] = resultSizes[j + 1];
                    resultSizes[j + 1] = temp;
                }
            }
        }

        for (int i = 0; i < resultSizes.length; i++) {
            System.out.print(resultSizes[i] + (i == resultSizes.length - 1 ? "" : " "));
        }
        System.out.println();
    }
}