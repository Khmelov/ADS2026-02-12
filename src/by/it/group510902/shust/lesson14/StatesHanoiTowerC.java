package by.it.group510902.shust.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static int[] parent;
    static int[] size;

    static int find(int i) {
        if (parent[i] == i) return i;
        return parent[i] = find(parent[i]);
    }

    static void union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);
        if (rootI != rootJ) {
            if (size[rootI] < size[rootJ]) {
                int temp = rootI; rootI = rootJ; rootJ = temp;
            }
            parent[rootJ] = rootI;
            size[rootI] += size[rootJ];
        }
    }

    static int szA, szB, szC;
    static int stepCount = 0;
    static int[] firstWithHeight;

    static void hanoi(int n, int from, int to, int aux) {
        if (n == 0) return;
        hanoi(n - 1, from, aux, to);

        if (from == 1) szA--;
        else if (from == 2) szB--;
        else szC--;

        if (to == 1) szA++;
        else if (to == 2) szB++;
        else szC++;

        int maxSz = szA;
        if (szB > maxSz) maxSz = szB;
        if (szC > maxSz) maxSz = szC;

        if (firstWithHeight[maxSz] == -1) {
            firstWithHeight[maxSz] = stepCount;
        } else {
            union(stepCount, firstWithHeight[maxSz]);
        }
        stepCount++;

        hanoi(n - 1, aux, to, from);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();

        int numSteps = (1 << n) - 1;
        parent = new int[numSteps];
        size = new int[numSteps];
        for (int i = 0; i < numSteps; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        firstWithHeight = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            firstWithHeight[i] = -1;
        }

        szA = n; szB = 0; szC = 0;
        stepCount = 0;

        hanoi(n, 1, 2, 3);

        int[] resultSizes = new int[n + 1];
        int count = 0;
        for (int i = 0; i < numSteps; i++) {
            if (parent[i] == i) {
                resultSizes[count++] = size[i];
            }
        }

        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (resultSizes[j] > resultSizes[j + 1]) {
                    int t = resultSizes[j];
                    resultSizes[j] = resultSizes[j + 1];
                    resultSizes[j + 1] = t;
                }
            }
        }

        for (int i = 0; i < count; i++) {
            System.out.print(resultSizes[i] + (i == count - 1 ? "" : " "));
        }
        System.out.println();
    }
}