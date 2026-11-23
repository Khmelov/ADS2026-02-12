package by.it.group551001.akynchits.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static class DSU {
        private final int[] parent;
        private final int[] size;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        // Эвристика 2: сокращение пути
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        // Эвристика 1: объединение по размеру поддерева
        public void union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA == rootB) {
                return;
            }
            if (size[rootA] < size[rootB]) {
                parent[rootA] = rootB;
                size[rootB] += size[rootA];
            } else {
                parent[rootB] = rootA;
                size[rootA] += size[rootB];
            }
        }

        public int getSize(int x) {
            return size[find(x)];
        }
    }

    private final int[] heights = new int[3]; // 0 -> A, 1 -> B, 2 -> C
    private final int[] firstStepWithMaxHeight;
    private final DSU dsu;
    private int stepIndex = 0;

    public StatesHanoiTowerC(int n) {
        int totalSteps = (1 << n) - 1;
        this.dsu = new DSU(totalSteps);
        this.firstStepWithMaxHeight = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            firstStepWithMaxHeight[i] = -1;
        }
        heights[0] = n;
        heights[1] = 0;
        heights[2] = 0;
    }

    // Рекурсивное решение задачи "Ханойские башни": перемещение с from на to через aux
    private void solveHanoi(int disks, int from, int to, int aux) {
        if (disks == 0) {
            return;
        }

        solveHanoi(disks - 1, from, aux, to);

        // Перенос одного диска со стержня from на стержень to
        heights[from]--;
        heights[to]++;

        int maxHeight = heights[0];
        if (heights[1] > maxHeight) {
            maxHeight = heights[1];
        }
        if (heights[2] > maxHeight) {
            maxHeight = heights[2];
        }

        int currentStep = stepIndex++;
        if (firstStepWithMaxHeight[maxHeight] == -1) {
            firstStepWithMaxHeight[maxHeight] = currentStep;
        } else {
            dsu.union(firstStepWithMaxHeight[maxHeight], currentStep);
        }

        solveHanoi(disks - 1, aux, to, from);
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

        StatesHanoiTowerC solver = new StatesHanoiTowerC(n);
        // Перемещаем N колец со стержня A (0) на стержень B (1), используя C (2)
        solver.solveHanoi(n, 0, 1, 2);

        int totalSteps = (1 << n) - 1;
        int[] clusterSizes = new int[n];
        int clusterCount = 0;

        for (int i = 0; i < totalSteps; i++) {
            if (solver.dsu.find(i) == i) {
                clusterSizes[clusterCount++] = solver.dsu.getSize(i);
            }
        }

        // Сортировка вставками в порядке возрастания (без использования коллекций)
        for (int i = 1; i < clusterCount; i++) {
            int key = clusterSizes[i];
            int j = i - 1;
            while (j >= 0 && clusterSizes[j] > key) {
                clusterSizes[j + 1] = clusterSizes[j];
                j--;
            }
            clusterSizes[j + 1] = key;
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < clusterCount; i++) {
            if (i > 0) {
                result.append(' ');
            }
            result.append(clusterSizes[i]);
        }
        System.out.println(result);
    }
}
