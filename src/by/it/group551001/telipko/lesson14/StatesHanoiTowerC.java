package by.it.group551001.telipko.lesson14;

import java.util.Scanner;


public class StatesHanoiTowerC {

    static int[] parent;
    static int[] size;

    static int heightA, heightB, heightC;
    static int step;
    static int[] firstStep;

    static int find(int x) {
        int root = x;
        while (parent[root] != root) root = parent[root];
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) return;
        if (size[a] < size[b]) { int t = a; a = b; b = t; }
        parent[b] = a;
        size[a] += size[b];
    }

    static void move(char from, char to) {
        if (from == 'A') heightA--; else if (from == 'B') heightB--; else heightC--;
        if (to == 'A') heightA++; else if (to == 'B') heightB++; else heightC++;

        int max = Math.max(heightA, Math.max(heightB, heightC));
        parent[step] = step;
        size[step] = 1;
        if (firstStep[max] == -1) firstStep[max] = step;
        else union(firstStep[max], step);
        step++;
    }


    static void hanoi(int n, char from, char to, char via) {
        if (n == 0) return;
        hanoi(n - 1, from, via, to);
        move(from, to);
        hanoi(n - 1, via, to, from);
    }

    public static void main(String[] args) {
        int n = new Scanner(System.in).nextInt();
        int steps = (1 << n) - 1;
        parent = new int[steps];
        size = new int[steps];
        firstStep = new int[n + 1];
        for (int i = 0; i <= n; i++) firstStep[i] = -1;

        heightA = n;
        heightB = heightC = 0;
        step = 0;
        hanoi(n, 'A', 'B', 'C');


        int[] res = new int[n + 1];
        int k = 0;
        for (int h = 0; h <= n; h++)
            if (firstStep[h] != -1) res[k++] = size[find(firstStep[h])];
        for (int i = 1; i < k; i++) {
            int v = res[i], j = i - 1;
            while (j >= 0 && res[j] > v) { res[j + 1] = res[j]; j--; }
            res[j + 1] = v;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < k; i++) {
            if (i > 0) sb.append(' ');
            sb.append(res[i]);
        }
        System.out.println(sb);
    }
}
