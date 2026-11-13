package by.it.group551003.kravtsov.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static int[] parent;
    static int[] size;

    static void initDSU(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }
    }

    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    static void union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra == rb) return;
        if (size[ra] < size[rb]) { int t = ra; ra = rb; rb = t; }
        parent[rb] = ra;
        size[ra] += size[rb];
    }

    static int[] h = new int[3];
    static int[] keys;
    static int stepCount = 0;

    static void move(int from, int to) {
        h[from]--;
        h[to]++;
        int mx = Math.max(h[0], Math.max(h[1], h[2]));
        keys[stepCount++] = mx;   // ключ после хода
    }

    static void hanoi(int n, int from, int to, int via) {
        if (n == 0) return;
        hanoi(n - 1, from, via, to);
        move(from, to);
        hanoi(n - 1, via, to, from);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        int total = (1 << n) - 1;
        h[0] = n; h[1] = 0; h[2] = 0;

        keys = new int[total];
        stepCount = 0;
        hanoi(n, 0, 1, 2);

        // DSU над шагами, но объединяем только шаги с одинаковым ключом
        // через "первый шаг с таким ключом"
        initDSU(total);
        int[] firstWithKey = new int[n + 1];   // 0 = ещё не было
        for (int i = 0; i < total; i++) {
            int k = keys[i];
            if (firstWithKey[k] == 0) {
                firstWithKey[k] = i + 1;       // +1 чтобы 0 был маркером
            } else {
                union(i, firstWithKey[k] - 1);
            }
        }

        // размеры кластеров
        int[] clusterSize = new int[total];
        int clusters = 0;
        for (int i = 0; i < total; i++) {
            int r = find(i);
            if (clusterSize[r] == 0) clusters++;
            clusterSize[r]++;
        }

        int[] res = new int[clusters];
        int k = 0;
        for (int i = 0; i < total; i++) {
            if (clusterSize[i] > 0) res[k++] = clusterSize[i];
        }

        // сортировка по возрастанию (пузырёк)
        for (int i = 0; i < res.length - 1; i++)
            for (int j = 0; j < res.length - 1 - i; j++)
                if (res[j] > res[j + 1]) {
                    int t = res[j]; res[j] = res[j + 1]; res[j + 1] = t;
                }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < res.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(res[i]);
        }
        System.out.println(sb.toString());
    }
}