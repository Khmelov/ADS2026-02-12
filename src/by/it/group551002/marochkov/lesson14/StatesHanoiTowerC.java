package by.it.group551002.marochkov.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {
    private static int[] parent;
    private static int[] size;
    private static int[] first;
    private static int[] h = new int[3];
    private static int step = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        int total = (1 << n) - 1;
        parent = new int[total];
        size = new int[total];
        for (int i = 0; i < total; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        first = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            first[i] = -1;
        }

        h[0] = n;
        h[1] = 0;
        h[2] = 0;
        step = 0;

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
            if (i > 0) sb.append(' ');
            sb.append(sizes[i]);
        }
        System.out.println(sb);
    }

    private static void hanoi(int n, int from, int to, int via) {
        if (n == 0) return;

        hanoi(n - 1, from, via, to);
        move(from, to);
        hanoi(n - 1, via, to, from);
    }

    private static void move(int from, int to) {
        h[from]--;
        h[to]++;

        int key = Math.max(h[0], Math.max(h[1], h[2]));

        if (first[key] == -1) {
            first[key] = step;
        } else {
            union(step, first[key]);
        }
        step++;
    }

    private static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    private static void union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) return;
        if (size[ra] < size[rb]) {
            int t = ra;
            ra = rb;
            rb = t;
        }
        parent[rb] = ra;
        size[ra] += size[rb];
    }
























































}
