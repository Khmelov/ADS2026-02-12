package by.it.group510902.makaruk.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    private static class DSU {
        private final int[] parent;
        private final int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            int root = x;
            while (parent[root] != root) {
                root = parent[root];
            }
            // Сокращение пути (path compression)
            while (parent[x] != root) {
                int next = parent[x];
                parent[x] = root;
                x = next;
            }
            return root;
        }

        void union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA == rootB) {
                return;
            }
            // Эвристика по размеру поддерева (union by size)
            if (size[rootA] < size[rootB]) {
                int temp = rootA;
                rootA = rootB;
                rootB = temp;
            }
            parent[rootB] = rootA;
            size[rootA] += size[rootB];
        }

        int getSize(int x) {
            return size[find(x)];
        }
    }

    private static class Solver {
        private final int[] heights = new int[3];
        private final int[] stepMaxHeight;
        private final DSU dsu;
        private final int[] firstStepForMaxHeight;
        private int stepIndex = 0;

        Solver(int n, int totalSteps) {
            heights[0] = n; // стержень A
            heights[1] = 0; // стержень B
            heights[2] = 0; // стержень C
            stepMaxHeight = new int[totalSteps];
            dsu = new DSU(totalSteps);
            firstStepForMaxHeight = new int[n + 1];
            for (int i = 0; i <= n; i++) {
                firstStepForMaxHeight[i] = -1;
            }
        }

        void hanoi(int disks, int from, int to, int aux) {
            if (disks == 0) {
                return;
            }
            hanoi(disks - 1, from, aux, to);

            heights[from]--;
            heights[to]++;

            int maxH = heights[0];
            if (heights[1] > maxH) {
                maxH = heights[1];
            }
            if (heights[2] > maxH) {
                maxH = heights[2];
            }

            stepMaxHeight[stepIndex] = maxH;
            if (firstStepForMaxHeight[maxH] == -1) {
                firstStepForMaxHeight[maxH] = stepIndex;
            } else {
                dsu.union(firstStepForMaxHeight[maxH], stepIndex);
            }

            stepIndex++;
            hanoi(disks - 1, aux, to, from);
        }

        int[] getSortedComponentSizes() {
            int count = 0;
            for (int i = 0; i < stepMaxHeight.length; i++) {
                if (dsu.find(i) == i) {
                    count++;
                }
            }

            int[] result = new int[count];
            int idx = 0;
            for (int i = 0; i < stepMaxHeight.length; i++) {
                if (dsu.find(i) == i) {
                    result[idx++] = dsu.getSize(i);
                }
            }

            // Сортировка по возрастанию без использования коллекций
            for (int i = 0; i < result.length - 1; i++) {
                for (int j = i + 1; j < result.length; j++) {
                    if (result[j] < result[i]) {
                        int temp = result[i];
                        result[i] = result[j];
                        result[j] = temp;
                    }
                }
            }
            return result;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            return;
        }
        int n = scanner.nextInt();
        if (n <= 0) {
            return;
        }

        int totalSteps = (1 << n) - 1;
        Solver solver = new Solver(n, totalSteps);
        solver.hanoi(n, 0, 1, 2);

        int[] sizes = solver.getSortedComponentSizes();
        for (int i = 0; i < sizes.length; i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(sizes[i]);
        }
        if (sizes.length > 0) {
            System.out.println();
        }
    }
}