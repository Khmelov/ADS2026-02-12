package by.it.group551003.popko.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class StatesHanoiTowerC {
    static class DSU {
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

        public int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            return parent[i] = find(parent[i]);
        }

        public void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                if (size[rootI] < size[rootJ]) {
                    int temp = rootI;
                    rootI = rootJ;
                    rootJ = temp;
                }
                parent[rootJ] = rootI;
                size[rootI] += size[rootJ];
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) return;
        int n = Integer.parseInt(line.trim());

        int totalSteps = (1 << n) - 1;
        DSU dsu = new DSU(totalSteps);

        int[] heights = new int[3];
        heights[0] = n; // Стержень A изначально содержит n дисков
        heights[1] = 0; // B пустой
        heights[2] = 0; // C пустой

        // Массив для отслеживания позиции каждого диска (0: A, 1: B, 2: C)
        int[] diskPeg = new int[n + 1];
        Arrays.fill(diskPeg, 0);

        int[] firstStepWithHeight = new int[n + 1];
        Arrays.fill(firstStepWithHeight, -1);

        for (int step = 1; step <= totalSteps; step++) {
            int disk = Integer.numberOfTrailingZeros(step) + 1;
            int from = diskPeg[disk];

            // Направление движения зависит от четности n и диска
            int direction = (n % 2 == 1) ? 1 : 2;
            if (disk % 2 == 0) {
                direction = 3 - direction;
            }
            int to = (from + direction) % 3;

            diskPeg[disk] = to;
            heights[from]--;
            heights[to]++;

            int maxHeight = Math.max(heights[0], Math.max(heights[1], heights[2]));

            int stepIdx = step - 1;
            if (firstStepWithHeight[maxHeight] == -1) {
                firstStepWithHeight[maxHeight] = stepIdx;
            } else {
                dsu.union(stepIdx, firstStepWithHeight[maxHeight]);
            }
        }

        int[] sizes = new int[totalSteps];
        int count = 0;
        for (int i = 0; i < totalSteps; i++) {
            if (dsu.parent[i] == i) {
                sizes[count++] = dsu.size[i];
            }
        }

        int[] result = Arrays.copyOf(sizes, count);
        Arrays.sort(result);

        StringBuilder sb = new StringBuilder();
        for (int size : result) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(size);
        }
        System.out.println(sb);
    }
}