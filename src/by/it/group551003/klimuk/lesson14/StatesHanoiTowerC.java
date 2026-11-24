package by.it.group551003.klimuk.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class StatesHanoiTowerC {

    static int[] parent;
    static int[] size;

    static int find(int i) {
        if (parent[i] == i)
            return i;
        return parent[i] = find(parent[i]);
    }

    static void union(int i, int j) {
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

    static void hanoi(int n, int from, int to, int aux,
                      int[] maxHeights, int stepIndex, int[] pegSizes) {
        if (n == 0) return;

        stepIndex = hanoiRec(n - 1, from, aux, to, maxHeights, stepIndex, pegSizes);

        pegSizes[from]--;
        pegSizes[to]++;

        int maxH = pegSizes[0];
        if (pegSizes[1] > maxH) maxH = pegSizes[1];
        if (pegSizes[2] > maxH) maxH = pegSizes[2];
        maxHeights[stepIndex] = maxH;
        stepIndex++;

        hanoiRec(n - 1, aux, to, from, maxHeights, stepIndex, pegSizes);
    }

    static int hanoiRec(int n, int from, int to, int aux,
                        int[] maxHeights, int stepIndex, int[] pegSizes) {
        if (n == 0) return stepIndex;

        int idx = hanoiRec(n - 1, from, aux, to, maxHeights, stepIndex, pegSizes);

        pegSizes[from]--;
        pegSizes[to]++;
        int maxH = pegSizes[0];
        if (pegSizes[1] > maxH) maxH = pegSizes[1];
        if (pegSizes[2] > maxH) maxH = pegSizes[2];
        maxHeights[idx] = maxH;
        idx++;

        return hanoiRec(n - 1, aux, to, from, maxHeights, idx, pegSizes);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null) return;
        int N = Integer.parseInt(line.trim());

        int totalSteps = (1 << N) - 1;
        int[] maxHeights = new int[totalSteps];
        int[] pegSizes = new int[3];
        pegSizes[0] = N;
        pegSizes[1] = 0;
        pegSizes[2] = 0;

        hanoi(N, 0, 1, 2, maxHeights, 0, pegSizes);

        parent = new int[totalSteps];
        size = new int[totalSteps];
        for (int i = 0; i < totalSteps; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        int[] firstOccurrence = new int[N + 1];
        for (int i = 0; i <= N; i++) {
            firstOccurrence[i] = -1;
        }

        for (int i = 0; i < totalSteps; i++) {
            int h = maxHeights[i];
            if (firstOccurrence[h] == -1) {
                firstOccurrence[h] = i;
            } else {
                union(firstOccurrence[h], i);
            }
        }

        int[] clusterSizes = new int[totalSteps];
        int count = 0;
        for (int i = 0; i < totalSteps; i++) {
            if (parent[i] == i) {
                clusterSizes[count++] = size[i];
            }
        }

        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (clusterSizes[j] > clusterSizes[j + 1]) {
                    int tmp = clusterSizes[j];
                    clusterSizes[j] = clusterSizes[j + 1];
                    clusterSizes[j + 1] = tmp;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(clusterSizes[i]);
            if (i < count - 1) {
                sb.append(" ");
            }
        }
        System.out.println(sb.toString());
    }
}