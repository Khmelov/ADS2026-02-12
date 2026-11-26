package by.it.group510902.sorokoletov.lesson14;

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
            while (parent[x] != x) {
                parent[x] = parent[parent[x]];
                x = parent[x];
            }
            return x;
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;
            if (size[ra] < size[rb]) {
                int tmp = ra;
                ra = rb;
                rb = tmp;
            }
            parent[rb] = ra;
            size[ra] += size[rb];
        }
    }

    static int[][] towers;
    static int[] heights;

    static void move(int from, int to) {
        int disk = towers[from][heights[from] - 1];
        heights[from]--;
        towers[to][heights[to]] = disk;
        heights[to]++;
    }

    static int solve(int n, int from, int to, int aux, int stepIndex, int[] stepKeys) {
        if (n == 0) return stepIndex;

        stepIndex = solve(n - 1, from, aux, to, stepIndex, stepKeys);

        move(from, to);
        stepKeys[stepIndex] = Math.max(heights[0], Math.max(heights[1], heights[2]));
        stepIndex++;

        stepIndex = solve(n - 1, aux, to, from, stepIndex, stepKeys);

        return stepIndex;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        scanner.close();

        if (N <= 0) {
            System.out.println();
            return;
        }

        long stepsLong = (1L << N) - 1;
        if (stepsLong > Integer.MAX_VALUE - 8) {
            System.out.println("N слишком велико");
            return;
        }
        int steps = (int) stepsLong;

        towers = new int[3][N];
        heights = new int[3];
        for (int i = 0; i < N; i++) {
            towers[0][i] = N - i;
        }
        heights[0] = N;
        heights[1] = 0;
        heights[2] = 0;

        int[] stepKeys = new int[steps];
        solve(N, 0, 1, 2, 0, stepKeys);

        DSU dsu = new DSU(steps);

        int[] firstStepOfKey = new int[N + 2];
        for (int k = 0; k <= N + 1; k++) firstStepOfKey[k] = -1;

        for (int i = 0; i < steps; i++) {
            int key = stepKeys[i];
            if (firstStepOfKey[key] == -1) {
                firstStepOfKey[key] = i;
            } else {
                dsu.union(i, firstStepOfKey[key]);
            }
        }

        int[] rootSizes = new int[steps];
        int countRoots = 0;

        boolean[] isRoot = new boolean[steps];
        for (int i = 0; i < steps; i++) {
            isRoot[dsu.find(i)] = true;
        }
        for (int i = 0; i < steps; i++) {
            if (isRoot[i]) {
                rootSizes[countRoots++] = dsu.size[i];
            }
        }

        for (int i = 1; i < countRoots; i++) {
            int key = rootSizes[i];
            int j = i - 1;
            while (j >= 0 && rootSizes[j] > key) {
                rootSizes[j + 1] = rootSizes[j];
                j--;
            }
            rootSizes[j + 1] = key;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < countRoots; i++) {
            if (i > 0) sb.append(' ');
            sb.append(rootSizes[i]);
        }
        System.out.println(sb);
    }
}