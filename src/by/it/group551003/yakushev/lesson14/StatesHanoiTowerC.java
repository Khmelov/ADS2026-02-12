package by.it.group551003.yakushev.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {
    private static int[] dsuParent, dsuSize, stateMaxH;
    private static int stepIdx, totalSteps;
    private static int[] stA, stB, stC;
    private static int szA, szB, szC;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        totalSteps = (1 << n) - 1;
        if (totalSteps <= 0) {
            System.out.println();
            return;
        }

        dsuParent = new int[totalSteps];
        dsuSize = new int[totalSteps];
        stateMaxH = new int[totalSteps];
        for (int i = 0; i < totalSteps; i++) {
            dsuParent[i] = i;
            dsuSize[i] = 1; // Эвристика по размеру поддерева
        }

        stA = new int[n];
        stB = new int[n];
        stC = new int[n];
        szA = n;
        szB = 0;
        szC = 0;
        for (int i = 0; i < n; i++) stA[i] = n - i;

        stepIdx = 0;
        // ВАЖНО: Перемещаем с A на B, вспомогательный C (по условию задачи!)
        hanoi(n, 'A', 'B', 'C');

        // Группировка через representative для каждой высоты
        int[] rep = new int[n + 1];
        java.util.Arrays.fill(rep, -1);
        for (int i = 0; i < totalSteps; i++) {
            int h = stateMaxH[i];
            if (rep[h] == -1) rep[h] = i;
            else union(rep[h], i);
        }

        // Сбор размеров корней
        int[] sizes = new int[totalSteps];
        int num = 0;
        for (int i = 0; i < totalSteps; i++) {
            if (find(i) == i) {
                sizes[num++] = dsuSize[i];
            }
        }

        // Сортировка ПО ВОЗРАСТАНИЮ (тест для N=2 ожидает "1 2")
        for (int i = 1; i < num; i++) {
            int key = sizes[i];
            int j = i - 1;
            while (j >= 0 && sizes[j] > key) {
                sizes[j + 1] = sizes[j];
                j--;
            }
            sizes[j + 1] = key;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            if (i > 0) sb.append(' ');
            sb.append(sizes[i]);
        }
        System.out.println(sb);
    }

    private static void hanoi(int n, char from, char to, char aux) {
        if (n == 0) return;
        hanoi(n - 1, from, aux, to);
        move(from, to);
        stateMaxH[stepIdx++] = Math.max(szA, Math.max(szB, szC));
        hanoi(n - 1, aux, to, from);
    }

    private static void move(char from, char to) {
        int disk;
        if (from == 'A') disk = stA[--szA];
        else if (from == 'B') disk = stB[--szB];
        else disk = stC[--szC];

        if (to == 'A') stA[szA++] = disk;
        else if (to == 'B') stB[szB++] = disk;
        else stC[szC++] = disk;
    }

    private static int find(int x) {
        if (dsuParent[x] != x) dsuParent[x] = find(dsuParent[x]);
        return dsuParent[x];
    }

    private static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) return;
        // Эвристика по размеру поддерева
        if (dsuSize[a] < dsuSize[b]) {
            dsuParent[a] = b;
            dsuSize[b] += dsuSize[a];
        } else {
            dsuParent[b] = a;
            dsuSize[a] += dsuSize[b];
        }
    }
}