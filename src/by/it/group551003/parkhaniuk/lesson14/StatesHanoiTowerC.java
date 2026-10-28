package by.it.group551003.parkhaniuk.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class StatesHanoiTowerC {

    public static int find(int[] parent, int i) {
        if (parent[i] == i) {
            return i;
        }
        return parent[i] = find(parent, parent[i]);
    }

    public static void union(int[] parent, int[] size, int i, int j) {
        int rootI = find(parent, i);
        int rootJ = find(parent, j);
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

    private static void generateHanoi(int n, int from, int to, int aux,
                                      int[] maxHeights, int[] stepIndex,
                                      int[] pegA, int[] pegB, int[] pegC,
                                      int[] topA, int[] topB, int[] topC) {
        if (n == 0) {
            return;
        }
        generateHanoi(n - 1, from, aux, to, maxHeights, stepIndex, pegA, pegB, pegC, topA, topB, topC);

        int disk = 0;
        if (from == 0) { disk = pegA[topA[0]--]; }
        else if (from == 1) { disk = pegB[topB[0]--]; }
        else { disk = pegC[topC[0]--]; }

        if (to == 0) { pegA[++topA[0]] = disk; }
        else if (to == 1) { pegB[++topB[0]] = disk; }
        else { pegC[++topC[0]] = disk; }

        int hA = topA[0] + 1;
        int hB = topB[0] + 1;
        int hC = topC[0] + 1;

        int maxH = hA;
        if (hB > maxH) maxH = hB;
        if (hC > maxH) maxH = hC;

        maxHeights[stepIndex[0]++] = maxH;

        generateHanoi(n - 1, aux, to, from, maxHeights, stepIndex, pegA, pegB, pegC, topA, topB, topC);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null) return;
        int N = Integer.parseInt(line.trim());

        int totalSteps = (1 << N) - 1; // 2^N - 1 шагов

        int[] pegA = new int[N];
        int[] pegB = new int[N];
        int[] pegC = new int[N];
        int[] topA = new int[] { -1 };
        int[] topB = new int[] { -1 };
        int[] topC = new int[] { -1 };

        for (int i = N - 1; i >= 0; i--) {
            pegA[++topA[0]] = N - i;
        }

        int[] maxHeights = new int[totalSteps];
        int[] stepIndex = new int[] { 0 };

        generateHanoi(N, 0, 1, 2, maxHeights, stepIndex, pegA, pegB, pegC, topA, topB, topC);

        int[] parent = new int[totalSteps];
        int[] size = new int[totalSteps];
        for (int i = 0; i < totalSteps; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        // Массив для хранения индекса первого шага, на котором встретилась конкретная максимальная высота
        // Возможные высоты лежат в диапазоне от 1 до N
        int[] firstSeen = new int[N + 1];
        for (int i = 1; i <= N; i++) {
            firstSeen[i] = -1;
        }

        // Объединяем все шаги с одинаковой максимальной высотой с самым первым встреченным шагом для этой высоты
        for (int i = 0; i < totalSteps; i++) {
            int h = maxHeights[i];
            if (firstSeen[h] == -1) {
                firstSeen[h] = i; // запоминаем первый шаг для данной высоты
            } else {
                union(parent, size, i, firstSeen[h]); // объединяем текущий шаг с первым
            }
        }

        int[] clusterSizes = new int[totalSteps];
        int clusterCount = 0;
        for (int i = 0; i < totalSteps; i++) {
            if (parent[i] == i) {
                clusterSizes[clusterCount++] = size[i];
            }
        }

        // Сортировка размеров по возрастанию (сортировка вставками)
        for (int i = 1; i < clusterCount; i++) {
            int key = clusterSizes[i];
            int j = i - 1;
            while (j >= 0 && clusterSizes[j] > key) {
                clusterSizes[j + 1] = clusterSizes[j];
                j--;
            }
            clusterSizes[j + 1] = key;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clusterCount; i++) {
            sb.append(clusterSizes[i]);
            if (i < clusterCount - 1) {
                sb.append(" ");
            }
        }
        System.out.println(sb.toString());
    }
}