package by.it.group551002.puteev.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class StatesHanoiTowerC {

    private static int[] heights;
    private static int[] parent;
    private static int[] size;
    private static int[] first;
    private static int step = -1;

    private static int find(int i) {
        if (parent[i] == i) return i;
        return parent[i] = find(parent[i]);
    }

    private static void union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);
        if (rootI != rootJ) {
            if (size[rootI] < size[rootJ]) {
                parent[rootI] = rootJ;
                size[rootJ] += size[rootI];
            } else {
                parent[rootJ] = rootI;
                size[rootI] += size[rootJ];
            }
        }
    }

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
            union(first[maxH], step);
        }

        solve(n - 1, aux, to, from);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();

        int total = (1 << n) - 1;
        parent = new int[total];
        size = new int[total];
        first = new int[n + 1];
        Arrays.fill(first, -1);
        for (int i = 0; i < total; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        heights = new int[]{n, 0, 0};
        step = -1;

        solve(n, 0, 1, 2);

        int count = 0;
        for (int i = 0; i <= n; i++) {
            if (first[i] != -1) {
                count++;
            }
        }

        int[] result = new int[count];
        int idx = 0;
        for (int i = 0; i <= n; i++) {
            if (first[i] != -1) {
                result[idx++] = size[find(first[i])];
            }
        }

        Arrays.sort(result);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            if (i > 0) sb.append(" ");
            sb.append(result[i]);
        }
        System.out.println(sb.toString());
    }
}
