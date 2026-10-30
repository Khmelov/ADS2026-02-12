package by.it.group551002.efimchikov.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {
    private static int[] parent;
    private static int[] size;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;

        int n = sc.nextInt();
        int totalSteps = (1 << n) - 1;

        parent = new int[totalSteps + 1];
        size = new int[totalSteps + 1];

        for (int i = 0; i <= totalSteps; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        int[] heights = new int[3];
        heights[0] = n;

        int[] representative = new int[n + 1];
        int[] step = new int[]{0};

        hanoi(n, 0, 1, 2, heights, representative, step);

        int[] clusterSizes = new int[n + 1];
        int k = 0;

        for (int i = 1; i <= totalSteps; i++) {
            if (find(i) == i) {
                clusterSizes[k++] = size[i];
            }
        }

        for (int i = 1; i < k; i++) {
            int x = clusterSizes[i];
            int j = i - 1;
            while (j >= 0 && clusterSizes[j] > x) {
                clusterSizes[j + 1] = clusterSizes[j];
                j--;
            }
            clusterSizes[j + 1] = x;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < k; i++) {
            if (i > 0) sb.append(' ');
            sb.append(clusterSizes[i]);
        }

        System.out.println(sb.toString().trim());
    }

    private static void hanoi(
            int n,
            int from,
            int to,
            int aux,
            int[] heights,
            int[] representative,
            int[] step
    ) {
        if (n == 0) return;

        hanoi(n - 1, from, aux, to, heights, representative, step);

        heights[from]--;
        heights[to]++;

        step[0]++;
        int currentStep = step[0];

        int maxHeight = Math.max(
                heights[0],
                Math.max(heights[1], heights[2])
        );

        if (representative[maxHeight] == 0) {
            representative[maxHeight] = currentStep;
        } else {
            union(currentStep, representative[maxHeight]);
        }

        hanoi(n - 1, aux, to, from, heights, representative, step);
    }

    private static int find(int x) {
        int root = x;
        while (parent[root] != root) {
            root = parent[root];
        }

        while (parent[x] != x) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }

        return root;
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
