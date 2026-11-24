package by.it.group551002.litovchenko.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    private static int[] heights;
    private static DSU dsu;
    private static int[] first;
    private static int step;

    private static void solve(int n, int from, int to, int aux) {
        if (n == 0) return;

        solve(n - 1, from, aux, to);

        heights[from]--;
        heights[to]++;
        step++;

        int maxH = heights[0];
        if (heights[1] > maxH) maxH = heights[1];
        if (heights[2] > maxH) maxH = heights[2];

        if (first[maxH] == -1) {
            first[maxH] = step;
        } else {
            dsu.union(first[maxH], step);
        }

        solve(n - 1, aux, to, from);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();

        int total = (1 << n) - 1;
        dsu = new DSU(total);
        first = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            first[i] = -1;
        }

        heights = new int[]{n, 0, 0};
        step = -1;

        solve(n, 0, 1, 2);

        int count = 0;
        for (int i = 1; i <= n; i++) {
            if (first[i] != -1) {
                count++;
            }
        }

        int[] result = new int[count];
        int idx = 0;
        for (int i = 1; i <= n; i++) {
            if (first[i] != -1) {
                result[idx++] = dsu.getSize(dsu.find(first[i]));
            }
        }

        // Сортировка по возрастанию без коллекций (сортировка вставками)
        for (int i = 1; i < result.length; i++) {
            int key = result[i];
            int j = i - 1;
            while (j >= 0 && result[j] > key) {
                result[j + 1] = result[j];
                j--;
            }
            result[j + 1] = key;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            if (i > 0) sb.append(" ");
            sb.append(result[i]);
        }
        System.out.println(sb.toString());
    }
}
