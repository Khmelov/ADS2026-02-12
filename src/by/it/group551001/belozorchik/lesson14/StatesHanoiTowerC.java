package by.it.group551001.belozorchik.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {
    static int[] height = new int[3];   // высоты пирамид
    static int[] stepHeight;            // stepHeight - макс высота после i-го хода
    static int[] parent;                // родитель
    static int[] treeSize;              // размер поддерева
    static int moveCount;               // сколько ходов сделано

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // высота стартовой пирамиды на стержне A
        int N = Integer.parseInt(scanner.nextLine().trim());

        int totalMoves = (1 << N) - 1;   // мин число ходов: 2^N - 1
        height[0] = N;                   // A
        height[1] = 0;                   // B
        height[2] = 0;                   // C

        stepHeight = new int[totalMoves];
        parent = new int[totalMoves];
        treeSize = new int[totalMoves];
        moveCount = 0;

        // перенос N колец со стержня A на пустой B, используя C
        hanoi(N, 0, 1, 2);

        // каждый шаг - своё мн-во
        for (int i = 0; i < totalMoves; i++) {
            parent[i] = i;
            treeSize[i] = 1;
        }

        // группируем шаги по одинаковой наибольшей высоте
        // firstWithHeight[h] - индекс первого увиденного шага с макс высотой h
        // каждый следующий такой же шаг объединяем с ним в одно мн-во
        int[] firstWithHeight = new int[N + 1];
        for (int h = 0; h <= N; h++) {
            firstWithHeight[h] = -1;
        }
        for (int i = 0; i < totalMoves; i++) {
            int h = stepHeight[i];
            if (firstWithHeight[h] == -1) {
                firstWithHeight[h] = i;
            } else {
                union(i, firstWithHeight[h]);
            }
        }

        // сколько шагов в каждом поддереве
        int[] clusterSize = new int[totalMoves];
        for (int i = 0; i < totalMoves; i++) {
            int root = find(i);
            clusterSize[root]++;
        }

        // собир ненулевые размеры, разных значений не больше N+1
        int[] sizes = new int[N + 1];
        int k = 0;
        for (int i = 0; i < totalMoves; i++) {
            if (clusterSize[i] > 0) {
                sizes[k] = clusterSize[i];
                k++;
            }
        }

        // сортировка (вставками)
        for (int i = 1; i < k; i++) {
            int current = sizes[i];
            int j = i - 1;
            while (j >= 0 && sizes[j] > current) {
                sizes[j + 1] = sizes[j];
                j--;
            }
            sizes[j + 1] = current;
        }

        // печать
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < k; i++) {
            if (i > 0) {
                out.append(' ');
            }
            out.append(sizes[i]);
        }
        System.out.println(out);
    }

    // после каждого реального хода запоминаем наибольшую высоту пирамид
    static void hanoi(int n, int from, int to, int aux) {
        if (n == 0) {
            return;
        }
        hanoi(n - 1, from, aux, to);

        // ход - одно кольцо со стержня from на стержень to
        height[from]--;
        height[to]++;
        stepHeight[moveCount] = Math.max(height[0], Math.max(height[1], height[2]));
        moveCount++;

        hanoi(n - 1, aux, to, from);
    }

    // поиск представителя множества с сжатием пути
    static int find(int v) {
        int root = v;
        while (parent[root] != root) {
            root = parent[root];
        }
        int current = v;
        while (parent[current] != root) {
            int next = parent[current];
            parent[current] = root;
            current = next;
        }
        return root;
    }

    // объединение по размеру
    static void union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if (rootA == rootB) {
            return;
        }
        if (treeSize[rootA] < treeSize[rootB]) {
            int tmp = rootA;
            rootA = rootB;
            rootB = tmp;
        }
        parent[rootB] = rootA;
        treeSize[rootA] += treeSize[rootB];
    }
}
