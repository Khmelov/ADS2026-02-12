package by.it.group551001.bochilo.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    private static int[] parent;
    private static int[] size;
    private static int[] heights;      // высоты стержней A, B, C
    private static int[] firstStep;    // первый шаг с данной максимальной высотой
    private static int step;

    // эвристика сокращения пути
    private static int find(int x) {
        int root = x;
        while (parent[root] != root) root = parent[root];
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    // эвристика по размеру поддерева
    private static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) return;
        if (size[a] < size[b]) { int t = a; a = b; b = t; }
        parent[b] = a;
        size[a] += size[b];
    }

    private static void move(int from, int to) {
        heights[from]--;
        heights[to]++;
        int max = Math.max(heights[0], Math.max(heights[1], heights[2]));
        if (firstStep[max] < 0) firstStep[max] = step;
        else union(firstStep[max], step);
        step++;
    }

    private static void hanoi(int n, int from, int to, int via) {
        if (n == 0) return;
        hanoi(n - 1, from, via, to);
        move(from, to);
        hanoi(n - 1, via, to, from);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        int steps = (1 << n) - 1;
        parent = new int[steps];
        size = new int[steps];
        for (int i = 0; i < steps; i++) {
            parent[i] = i;
            size[i] = 1;
        }
        heights = new int[]{n, 0, 0};
        firstStep = new int[n + 1];
        for (int i = 0; i <= n; i++) firstStep[i] = -1;
        step = 0;

        hanoi(n, 0, 1, 2); // A -> B через C

        // размеры поддеревьев (по одному на каждую встреченную высоту)
        int[] sizes = new int[n + 1];
        int count = 0;
        for (int h = 0; h <= n; h++) {
            if (firstStep[h] >= 0) sizes[count++] = size[find(firstStep[h])];
        }

        // сортировка вставками по возрастанию
        for (int i = 1; i < count; i++) {
            int key = sizes[i];
            int j = i - 1;
            while (j >= 0 && sizes[j] > key) {
                sizes[j + 1] = sizes[j];
                j--;
            }
            sizes[j + 1] = key;
        }

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) out.append(' ');
            out.append(sizes[i]);
        }
        System.out.println(out);
    }
}
