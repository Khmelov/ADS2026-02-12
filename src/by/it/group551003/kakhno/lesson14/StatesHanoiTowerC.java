package by.it.group551003.kakhno.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static class DSU {
        int[] parent;
        int[] size;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
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

            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }

        public int getSize(int x) {
            return size[find(x)];
        }
    }

    static void solveHanoi(int n, char from, char to, char aux,
                           int[] maxHeightArray, int[] stateCount,
                           int[][] towerA, int[][] towerB, int[][] towerC,
                           int[] countA, int[] countB, int[] countC) {
        if (n == 0)
            return;

        solveHanoi(n - 1, from, aux, to, maxHeightArray, stateCount,
                towerA, towerB, towerC, countA, countB, countC);

        moveDisk(from, to, towerA, towerB, towerC, countA, countB, countC);

        int idx = stateCount[0]++;
        maxHeightArray[idx] = Math.max(countA[0], Math.max(countB[0], countC[0]));

        solveHanoi(n - 1, aux, to, from, maxHeightArray, stateCount,
                towerA, towerB, towerC, countA, countB, countC);
    }

    static void moveDisk(char from, char to,
                         int[][] towerA, int[][] towerB, int[][] towerC,
                         int[] countA, int[] countB, int[] countC) {
        int disk;

        if (from == 'A')
            countA[0]--;
        else if (from == 'B')
            countB[0]--;
        else
            countC[0]--;

        if (to == 'A')
            countA[0]++;
        else if (to == 'B')
            countB[0]++;
        else
            countC[0]++;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();

        int totalMoves = (1 << N) - 1;

        int[] maxHeightArray = new int[totalMoves];
        int[] stateCount = {0};

        int[][] towerA = {new int[N]};
        int[][] towerB = {new int[N]};
        int[][] towerC = {new int[N]};
        int[] countA = {N};
        int[] countB = {0};
        int[] countC = {0};

        solveHanoi(N, 'A', 'B', 'C', maxHeightArray, stateCount,
                towerA, towerB, towerC, countA, countB, countC);

        DSU dsu = new DSU(totalMoves);

        int maxPossibleHeight = N;
        int[] firstIndex = new int[maxPossibleHeight + 1];
        for (int i = 0; i <= maxPossibleHeight; i++)
            firstIndex[i] = -1;

        for (int i = 0; i < totalMoves; i++) {
            int height = maxHeightArray[i];
            if (firstIndex[height] == -1)
                firstIndex[height] = i;
            else
                dsu.union(firstIndex[height], i);
        }

        boolean[] visited = new boolean[totalMoves];
        int[] clusterSizes = new int[totalMoves];
        int clusterCount = 0;

        for (int i = 0; i < totalMoves; i++) {
            int root = dsu.find(i);
            if (!visited[root]) {
                visited[root] = true;
                clusterSizes[clusterCount++] = dsu.getSize(root);
            }
        }

        for (int i = 0; i < clusterCount - 1; i++)
            for (int j = 0; j < clusterCount - i - 1; j++)
                if (clusterSizes[j] > clusterSizes[j + 1]) {
                    int temp = clusterSizes[j];
                    clusterSizes[j] = clusterSizes[j + 1];
                    clusterSizes[j + 1] = temp;
                }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < clusterCount; i++) {
            if (i > 0)
                result.append(" ");
            result.append(clusterSizes[i]);
        }
        System.out.println(result.toString());

        scanner.close();
    }
}