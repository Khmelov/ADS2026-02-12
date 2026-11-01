package by.it.group551002.yorsh.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static class IntList {
        int[] data = new int[10];
        int size = 0;

        void add(int val) {
            if (size == data.length) {
                int[] newData = new int[data.length * 2];
                System.arraycopy(data, 0, newData, 0, data.length);
                data = newData;
            }
            data[size++] = val;
        }

        int get(int idx) { return data[idx]; }
    }

    static int[] parent;
    static int[] sizeTree;

    static int find(int i) {
        if (parent[i] == i) return i;
        return parent[i] = find(parent[i]);
    }

    static void union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);
        if (rootI != rootJ) {
            if (sizeTree[rootI] < sizeTree[rootJ]) {
                parent[rootI] = rootJ;
                sizeTree[rootJ] += sizeTree[rootI];
            } else {
                parent[rootJ] = rootI;
                sizeTree[rootI] += sizeTree[rootJ];
            }
        }
    }

    static int totalSteps;
    static int[] maxHeight;

    static void hanoi(int n, int from, int to, int aux, int[][] peg, int[] pegSizes) {
        if (n == 0) return;
        hanoi(n - 1, from, aux, to, peg, pegSizes);

        int disk = peg[from][--pegSizes[from]];
        peg[to][pegSizes[to]++] = disk;

        int maxH = Math.max(Math.max(pegSizes[0], pegSizes[1]), pegSizes[2]);
        maxHeight[totalSteps] = maxH;
        totalSteps++;

        hanoi(n - 1, aux, to, from, peg, pegSizes);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();

        int maxMoves = (1 << n) - 1;
        maxHeight = new int[maxMoves];
        totalSteps = 0;

        int[][] peg = new int[3][n + 1];
        int[] pegSizes = new int[3];

        for (int i = n; i >= 1; i--) {
            peg[0][pegSizes[0]++] = i;
        }

        hanoi(n, 0, 1, 2, peg, pegSizes);

        parent = new int[totalSteps];
        sizeTree = new int[totalSteps];
        for (int i = 0; i < totalSteps; i++) {
            parent[i] = i;
            sizeTree[i] = 1;
        }

        // Оптимизированное объединение за O(N) вместо O(N^2)
        // Запоминаем первый встреченный шаг для каждой высоты
        int[] firstOccurrence = new int[n + 2];
        for (int i = 0; i <= n + 1; i++) {
            firstOccurrence[i] = -1;
        }

        for (int i = 0; i < totalSteps; i++) {
            int h = maxHeight[i];
            if (firstOccurrence[h] == -1) {
                firstOccurrence[h] = i;
            } else {
                union(firstOccurrence[h], i);
            }
        }

        IntList roots = new IntList();
        boolean[] counted = new boolean[totalSteps];
        for (int i = 0; i < totalSteps; i++) {
            int root = find(i);
            if (!counted[root]) {
                counted[root] = true;
                roots.add(sizeTree[root]);
            }
        }

        int[] res = new int[roots.size];
        for (int i = 0; i < roots.size; i++) res[i] = roots.get(i);

        for (int i = 0; i < res.length - 1; i++) {
            for (int j = 0; j < res.length - i - 1; j++) {
                if (res[j] > res[j + 1]) {
                    int temp = res[j];
                    res[j] = res[j + 1];
                    res[j + 1] = temp;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int val : res) {
            sb.append(val).append(" ");
        }
        System.out.println(sb.toString().trim());
    }
}