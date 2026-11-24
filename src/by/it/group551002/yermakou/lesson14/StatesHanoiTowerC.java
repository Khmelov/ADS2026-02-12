package by.it.group551002.yermakou.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {


    static int[] parent;
    static int[] size;
    static int[] height;
    static int[] firstStepWithMax;
    static int step;

    static int find(int x) {
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

    static void union(int a, int b) {
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

    static void move(int n, int from, int to, int via) {
        if (n == 0) return;
        move(n - 1, from, via, to);

        height[from]--;
        height[to]++;
        registerStep();

        move(n - 1, via, to, from);
    }

    static void registerStep() {
        int max = Math.max(height[0], Math.max(height[1], height[2]));
        int current = step++;
        if (firstStepWithMax[max] == -1) {
            firstStepWithMax[max] = current;
        } else {
            union(firstStepWithMax[max], current);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        int steps = (1 << n) - 1;
        parent = new int[steps];
        size = new int[steps];
        for (int i = 0; i < steps; i++) {
            parent[i] = i;
            size[i] = 1;
        }
        height = new int[]{n, 0, 0};
        firstStepWithMax = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            firstStepWithMax[i] = -1;
        }
        step = 0;

        move(n, 0, 1, 2);

        // Размеры поддеревьев (корней DSU)
        int[] sizes = new int[n + 1];
        int cnt = 0;
        for (int i = 0; i < steps; i++) {
            if (find(i) == i) {
                sizes[cnt++] = size[i];
            }
        }
        for (int i = 1; i < cnt; i++) {
            int v = sizes[i];
            int j = i - 1;
            while (j >= 0 && sizes[j] > v) {
                sizes[j + 1] = sizes[j];
                j--;
            }
            sizes[j + 1] = v;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cnt; i++) {
            if (i > 0) sb.append(' ');
            sb.append(sizes[i]);
        }
        System.out.println(sb);
    }
}