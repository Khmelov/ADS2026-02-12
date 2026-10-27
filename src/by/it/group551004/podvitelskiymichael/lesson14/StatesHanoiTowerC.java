package by.it.group551004.podvitelskiymichael.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    private static int[] parent;
    private static int[] size;

    private static int[] heights = new int[3];
    private static int[] firstStep;
    private static int step = 0;

    private static int find(int x) {
        int root = x;
        while (parent[root] != root) {
            root = parent[root];
        }
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    private static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (size[a] < size[b]) {
            int t = a;
            a = b;
            b = t;
        }
        parent[b] = a;
        size[a] += size[b];
    }

    private static void moveDisk(int from, int to) {
        heights[from]--;
        heights[to]++;

        int max = heights[0];
        if (heights[1] > max) {
            max = heights[1];
        }
        if (heights[2] > max) {
            max = heights[2];
        }

        int id = step++;
        parent[id] = id;
        size[id] = 1;
        if (firstStep[max] == -1) {
            firstStep[max] = id;
        } else {
            union(id, firstStep[max]);
        }
    }

    private static void hanoi(int n, int from, int to, int via) {
        if (n == 0) {
            return;
        }
        hanoi(n - 1, from, via, to);
        moveDisk(from, to);
        hanoi(n - 1, via, to, from);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        int total = (1 << n) - 1;
        parent = new int[total];
        size = new int[total];
        firstStep = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            firstStep[i] = -1;
        }

        step = 0;
        heights[0] = n;
        heights[1] = 0;
        heights[2] = 0;
        hanoi(n, 0, 1, 2);

        int[] sizes = new int[n + 1];
        int count = 0;
        for (int i = 0; i < total; i++) {
            if (find(i) == i) {
                sizes[count++] = size[i];
            }
        }

        for (int i = 1; i < count; i++) {
            int v = sizes[i];
            int j = i - 1;
            while (j >= 0 && sizes[j] > v) {
                sizes[j + 1] = sizes[j];
                j--;
            }
            sizes[j + 1] = v;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(sizes[i]);
        }
        System.out.println(sb);
    }
}