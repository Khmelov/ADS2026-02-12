package by.it.group551001.bogush.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static int[] parent;
    static int[] size;

    static int[] h = new int[3];          // высоты стержней A, B, C
    static int[] firstOfHeight;           // первый шаг с данной наибольшей высотой
    static int step = 0;

    // поиск корня со сжатием пути
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

    // объединение по размеру поддерева
    static void union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) {
            return;
        }
        if (size[ra] < size[rb]) {
            parent[ra] = rb;
            size[rb] += size[ra];
        } else {
            parent[rb] = ra;
            size[ra] += size[rb];
        }
    }

    // один ход: меняем высоты и относим шаг к группе по наибольшей высоте
    static void move(int from, int to) {
        h[from]--;
        h[to]++;

        int max = h[0];
        if (h[1] > max) {
            max = h[1];
        }
        if (h[2] > max) {
            max = h[2];
        }

        parent[step] = step;
        size[step] = 1;

        if (firstOfHeight[max] == -1) {
            firstOfHeight[max] = step;
        } else {
            union(step, firstOfHeight[max]);
        }
        step++;
    }

    static void hanoi(int n, int from, int to, int via) {
        if (n == 0) {
            return;
        }
        hanoi(n - 1, from, via, to);
        move(from, to);
        hanoi(n - 1, via, to, from);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        step = 0;

        int total = (1 << n) - 1;
        parent = new int[total];
        size = new int[total];

        firstOfHeight = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            firstOfHeight[i] = -1;
        }

        h[0] = n;
        h[1] = 0;
        h[2] = 0;

        hanoi(n, 0, 1, 2);   // A -> B через C

        // размеры поддеревьев = размеры корней
        int[] res = new int[n + 1];
        int cnt = 0;
        for (int i = 0; i < total; i++) {
            if (find(i) == i) {
                res[cnt] = size[i];
                cnt++;
            }
        }

        // сортировка вставками по возрастанию
        for (int i = 1; i < cnt; i++) {
            int key = res[i];
            int j = i - 1;
            while (j >= 0 && res[j] > key) {
                res[j + 1] = res[j];
                j--;
            }
            res[j + 1] = key;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cnt; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(res[i]);
        }
        System.out.println(sb);
    }
}
