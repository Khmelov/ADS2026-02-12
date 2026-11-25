package by.it.group510902.konchatov.lesson14;


import java.util.Scanner;

public class StatesHanoiTowerC {

    static class DSU {
        private final int[] parent;
        private final int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            int root = x;
            while (parent[root] != root) root = parent[root];
            while (parent[x] != root) {
                int next = parent[x];
                parent[x] = root;
                x = next;
            }
            return root;
        }

        void union(int x, int y) {
            int rx = find(x), ry = find(y);
            if (rx == ry) return;
            if (size[rx] < size[ry]) {
                parent[rx] = ry;
                size[ry] += size[rx];
            } else {
                parent[ry] = rx;
                size[rx] += size[ry];
            }
        }

        int sizeOf(int x) { return size[find(x)]; }
    }

    static int[] stackA, stackB, stackC;
    static int topA, topB, topC;
    static int hA, hB, hC;
    static int[] keys;
    static int stepCount;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        keys = new int[(1 << n) - 1];
        stepCount = 0;

        stackA = new int[n];
        stackB = new int[n];
        stackC = new int[n];
        for (int i = 0; i < n; i++) stackA[i] = n - i;
        topA = n; topB = 0; topC = 0;
        hA = n; hB = 0; hC = 0;

        hanoi(n, 'A', 'B', 'C');

        DSU dsu = new DSU((1 << n) - 1);
        int[] qw = new int[n + 1];
        for (int i = 0; i <= n; i++) qw[i] = -1;

        for (int s = 0; s < (1 << n) - 1; s++) {
            int k = keys[s];
            if (qw[k] == -1) {
                qw[k] = s;
            } else {
                dsu.union(s, qw[k]);
            }
        }

        int[] sizes = new int[(1 << n) - 1];
        int cnt = 0;
        for (int s = 0; s < (1 << n) - 1; s++) {
            if (dsu.find(s) == s) sizes[cnt++] = dsu.sizeOf(s);
        }

        for (int i = 1; i < cnt; i++) {
            int cur = sizes[i];
            int j = i - 1;
            while (j >= 0 && sizes[j] > cur) {
                sizes[j + 1] = sizes[j];
                j--;
            }
            sizes[j + 1] = cur;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cnt; i++) {
            if (i > 0) sb.append(' ');
            sb.append(sizes[i]);
        }
        System.out.println(sb);
    }

    static void hanoi(int k, char from, char to, char aux) {
        if (k == 0) return;
        hanoi(k - 1, from, aux, to);
        moveTop(from, to);
        int m = hA;
        if (hB > m) m = hB;
        if (hC > m) m = hC;
        keys[stepCount++] = m;
        hanoi(k - 1, aux, to, from);
    }

    static void moveTop(char src, char dst) {
        int disk;
        switch (src) {
            case 'A': disk = stackA[--topA]; hA = topA; break;
            case 'B': disk = stackB[--topB]; hB = topB; break;
            default:  disk = stackC[--topC]; hC = topC; break;
        }
        switch (dst) {
            case 'A': stackA[topA++] = disk; hA = topA; break;
            case 'B': stackB[topB++] = disk; hB = topB; break;
            default:  stackC[topC++] = disk; hC = topC; break;
        }
    }
}