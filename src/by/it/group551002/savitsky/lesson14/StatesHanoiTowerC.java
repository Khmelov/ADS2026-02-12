package by.it.group551002.savitsky.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static class DSU {
        int[] parent;
        int[] size;

        public DSU(int n) {
            parent = new int[n + 1];
            size = new int[n + 1];

            for (int i = 1; i <= n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            return parent[i] = find(parent[i]);
        }

        public void union(int i, int j) {
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

    static int[] towerHeight;
    static int[] firstForHeight;
    static DSU dsu;
    static int step = 0;

    static void hanoi(int n, int from, int to, int aux) {
        if (n == 0) {
            return;
        }

        hanoi(n - 1, from, aux, to);

        towerHeight[from]--;
        towerHeight[to]++;

        step++;

        int maxHeight = Math.max(
                towerHeight[0],
                Math.max(towerHeight[1], towerHeight[2])
        );

        if (firstForHeight[maxHeight] == 0) {
            firstForHeight[maxHeight] = step;
        } else {
            dsu.union(step, firstForHeight[maxHeight]);
        }

        hanoi(n - 1, aux, to, from);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        int moves = (1 << n) - 1;

        towerHeight = new int[3];
        towerHeight[0] = n;
        step = 0;

        dsu = new DSU(moves);
        firstForHeight = new int[n + 1];

        hanoi(n, 0, 1, 2);

        int[] result = new int[moves];
        int count = 0;

        for (int i = 1; i <= moves; i++) {
            if (dsu.find(i) == i) {
                result[count] = dsu.size[i];
                count++;
            }
        }

        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (result[i] > result[j]) {
                    int temp = result[i];
                    result[i] = result[j];
                    result[j] = temp;
                }
            }
        }

        for (int i = 0; i < count; i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(result[i]);
        }
    }
}