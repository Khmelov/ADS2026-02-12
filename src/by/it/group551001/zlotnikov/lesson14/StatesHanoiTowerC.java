package by.it.group551001.zlotnikov.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {
    private static int[] parent;
    private static int[] size;
    private static int[] heights;
    private static int[] firstStepForMaxH;
    private static int stepIndex;

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
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) {
            return;
        }
        if (size[ra] < size[rb]) {
            int tmp = ra;
            ra = rb;
            rb = tmp;
        }
        parent[rb] = ra;
        size[ra] += size[rb];
    }

    private static void hanoi(int n, int from, int to, int aux) {
        if (n == 0) {
            return;
        }
        hanoi(n - 1, from, aux, to);

        heights[from]--;
        heights[to]++;
        int maxH = heights[0];
        if (heights[1] > maxH) {
            maxH = heights[1];
        }
        if (heights[2] > maxH) {
            maxH = heights[2];
        }

        int current = stepIndex++;
        if (firstStepForMaxH[maxH] == -1) {
            firstStepForMaxH[maxH] = current;
        } else {
            union(firstStepForMaxH[maxH], current);
        }

        hanoi(n - 1, aux, to, from);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            return;
        }
        int n = scanner.nextInt();
        int totalSteps = (1 << n) - 1;

        parent = new int[totalSteps];
        size = new int[totalSteps];
        for (int i = 0; i < totalSteps; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        heights = new int[]{n, 0, 0};
        firstStepForMaxH = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            firstStepForMaxH[i] = -1;
        }
        stepIndex = 0;

        hanoi(n, 0, 1, 2);

        int[] result = new int[n + 1];
        int count = 0;
        for (int h = 0; h <= n; h++) {
            if (firstStepForMaxH[h] != -1) {
                result[count++] = size[find(firstStepForMaxH[h])];
            }
        }

        for (int i = 1; i < count; i++) {
            int key = result[i];
            int j = i - 1;
            while (j >= 0 && result[j] > key) {
                result[j + 1] = result[j];
                j--;
            }
            result[j + 1] = key;
        }

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                out.append(' ');
            }
            out.append(result[i]);
        }
        System.out.println(out);
    }
}
