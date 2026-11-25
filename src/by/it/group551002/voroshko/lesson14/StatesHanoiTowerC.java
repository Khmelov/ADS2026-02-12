package by.it.group551002.voroshko.lesson14;

import java.util.Scanner;
import java.util.Arrays;

public class StatesHanoiTowerC {
    static class DSU {
        private int[] parent, rank;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        int find(int x) {
            if (x == parent[x]) return x;
            return parent[x] = find(parent[x]);
        }

        void union(int x, int y) {
            x = find(x);
            y = find(y);

            if (x == y) return;

            if (rank[x] < rank[y]) {
                int t = x;
                x = y;
                y = t;
            }

            parent[y] = x;

            rank[x] += rank[y];
        }

        int getRank (int idx) { return rank[idx]; }
    }

    static int[] sizes = new int[3];
    static int[] heights;
    static int stepCount = 0;
    static int N;

    static void move(int from, int to) {
        sizes[from]--;
        sizes[to]++;

        int m = Math.max(sizes[0], Math.max(sizes[1], sizes[2]));
        heights[stepCount++] = m;
    }

    static void hanoi(int n, int from, int to, int aux) {
        if (n == 0) return;

        hanoi(n - 1, from, aux, to);
        move(from, to);
        hanoi(n - 1, aux, to, from);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        N = sc.nextInt();

        sc.close();

        sizes = new int[3];
        stepCount = 0;

        int total = Math.powExact(2, N) - 1;
        heights = new int[total];

        sizes[0] = N;
        hanoi(N, 0, 1, 2);

        int[] firstStep = new int[N + 1];
        Arrays.fill(firstStep, -1);

        DSU dsu = new DSU(total);
        for (int i = 0; i < total; i++) {
            int h = heights[i];

            if (firstStep[h] == -1) {
                firstStep[h] = i;
            } else {
                dsu.union(firstStep[h], i);
            }
        }

        int[] res = new int[total];
        int cnt = 0;

        boolean[] seen = new boolean[total];

        for (int i = 0; i < total; i++) {
            int r = dsu.find(i);

            if (!seen[r]) {
                seen[r] = true;
                res[cnt++] = dsu.getRank(r);
            }
        }

        for (int i = 0; i < cnt; i++) {
            for (int j = i + 1; j < cnt; j++) {
                if (res[i] > res[j]) {
                    int tmp = res[i];
                    res[i] = res[j];
                    res[j] = tmp;
                }
            }
        }

        for (int i = 0; i < cnt; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(res[i]);
        }
        System.out.println();
    }
}
