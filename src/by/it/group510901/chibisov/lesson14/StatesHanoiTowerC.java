package by.it.group510901.chibisov.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class StatesHanoiTowerC {

    private static class DSU {

        int[] parent;
        int[] size;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] == x) return x;
            return parent[x] = find(parent[x]);
        }

        public void union(int x, int y) {
            x = find(x);
            y = find(y);

            if (x == y) return;

            if (size[x] < size[y]) {
                parent[x] = y;
                size[y] += size[x];
            } else {
                parent[y] = x;
                size[x] += size[y];
            }
        }
    }

    private static DSU dsu;
    private static int[] towers;
    private static int[] firstStepWithHeight;
    private static int currentStep;

    private static void hanoi(int n, int from, int to) {
        if (n == 0) return;

        int tmp = 3 - from - to;

        hanoi(n - 1, from, tmp);
        move(from, to);
        hanoi(n - 1, tmp, to);
    }

    private static void move(int from, int to) {
        towers[from]--;
        towers[to]++;

        int height = Math.max(towers[0], Math.max(towers[1], towers[2]));
        if (firstStepWithHeight[height] == -1) {
            firstStepWithHeight[height] = currentStep;
        } else {
            dsu.union(firstStepWithHeight[height], currentStep);
        }
        currentStep++;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();

        towers = new int[3];
        towers[0] = n;

        int totalSteps = (1 << n) - 1;
        firstStepWithHeight = new int[n + 1];
        Arrays.fill(firstStepWithHeight, -1);
        currentStep = 0;

        dsu = new DSU(totalSteps);

        hanoi(n, 0, 1);

        int[] result = new int[n];
        int count = 0;

        for (int i = 0; i < totalSteps; i++) {
            if (dsu.parent[i] == i) {
                result[count++] = dsu.size[i];
            }
        }
        Arrays.sort(result, 0, count);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) sb.append(" ");
            sb.append(result[i]);
        }
        System.out.println(sb);
    }
}
