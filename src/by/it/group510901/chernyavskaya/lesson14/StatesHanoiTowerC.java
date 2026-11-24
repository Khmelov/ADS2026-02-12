package by.it.group510901.chernyavskaya.lesson14;
//Симуляция Ханойских башен с группировкой всех шагов алгоритма по наибольшей высоте пирамидки
// на каждом шаге и выводом размеров групп по возрастанию.
import java.util.Arrays;
import java.util.Scanner;

public class StatesHanoiTowerC {
    static int[] parent;
    static int[] size;
    static int[] heights;
    static int[] firstStep;
    static int step;

    static int find(int i) {
        if (parent[i] != i) {
            parent[i] = find(parent[i]);
        }
        return parent[i];
    }

    static void union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if (rootA != rootB) {
            if (size[rootA] < size[rootB]) {
                parent[rootA] = rootB;
                size[rootB] += size[rootA];
            } else {
                parent[rootB] = rootA;
                size[rootA] += size[rootB];
            }
        }
    }

    static void hanoi(int n, int from, int to, int aux) {
        if (n == 0) return;
        hanoi(n - 1, from, aux, to);

        heights[from]--;
        heights[to]++;
        int maxH = Math.max(heights[0], Math.max(heights[1], heights[2]));

        if (firstStep[maxH] == -1) {
            firstStep[maxH] = step;
        } else {
            union(firstStep[maxH], step);
        }
        step++;

        hanoi(n - 1, aux, to, from);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int totalSteps = (1 << n) - 1;

        parent = new int[totalSteps];
        size = new int[totalSteps];
        for (int i = 0; i < totalSteps; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        heights = new int[]{n, 0, 0};
        firstStep = new int[n + 1];
        Arrays.fill(firstStep, -1);
        step = 0;

        hanoi(n, 0, 1, 2);

        int[] res = new int[n + 1];
        int count = 0;
        for (int h = 1; h <= n; h++) {
            if (firstStep[h] != -1) {
                res[count++] = size[find(firstStep[h])];
            }
        }

        Arrays.sort(res, 0, count);
        for (int i = 0; i < count; i++) {
            System.out.print(res[i] + (i == count - 1 ? "" : " "));
        }
        System.out.println();
    }
}