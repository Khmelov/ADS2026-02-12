package by.it.group551003.kravchenkovikk.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static class DSU {
        private static int[] parent;
        private static int[] size;
        private static int[] heights;
        private static int[] firstStepByMaxHeight;
        private static int currentStep;

        DSU(int totalSteps) {
            parent = new int[totalSteps];
            size = new int[totalSteps];
            for (int i = 0; i < totalSteps; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            parent[i] = find(parent[i]);
            return parent[i];
        }

        void union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA != rootB) {
                if (size[rootA] < size[rootB]) {
                    int temp = rootA;
                    rootA = rootB;
                    rootB = temp;
                }
                parent[rootB] = rootA;
                size[rootA] += size[rootB];
            }
        }

        void hanoi(int n, int from, int to, int aux) {
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

            if (firstStepByMaxHeight[maxH] == -1) {
                firstStepByMaxHeight[maxH] = currentStep;
            } else {
                union(firstStepByMaxHeight[maxH], currentStep);
            }
            currentStep++;

            hanoi(n - 1, aux, to, from);
        }

        void sortAscending(int[] arr, int length) {
            for (int i = 1; i < length; i++) {
                int key = arr[i];
                int j = i - 1;
                while (j >= 0 && arr[j] > key) {
                    arr[j + 1] = arr[j];
                    j--;
                }
                arr[j + 1] = key;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        scan.close();
        if (n <= 0) {
            return;
        }

        int totalSteps = (1 << n) - 1;
        DSU dsu = new DSU(totalSteps);
        dsu.heights = new int[]{n, 0, 0};
        dsu.firstStepByMaxHeight = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            dsu.firstStepByMaxHeight[i] = -1;
        }

        dsu.currentStep = 0;
        dsu.hanoi(n, 0, 1, 2);
        int[] subtreeSizes = new int[n + 1];
        int count = 0;
        for (int i = 0; i < totalSteps; i++) {
            if (dsu.find(i) == i) {
                subtreeSizes[count++] = dsu.size[i];
            }
        }

        dsu.sortAscending(subtreeSizes, count);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(subtreeSizes[i]);
        }
        System.out.println(sb.toString());
    }
}
