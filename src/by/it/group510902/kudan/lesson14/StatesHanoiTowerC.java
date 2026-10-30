package by.it.group510902.kudan.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    //текущие высоты пирамид, индексы 0 1 2 это стержни a b c
    private static int[] h;
    //для каждого шага запоминаем наибольшую высоту после него
    private static int[] max;
    private static int step;

    private static int[] parent;
    private static int[] size;

    //рекурсивное решение, все кольца кроме самого большого убираем на вспомогательный стержень
    //самое большое кольцо кладем на нужный стержень и сверху возвращаем остальные
    private static void hanoi(int n, int from, int to, int aux) {
        if (n == 0) {
            return;
        }
        hanoi(n - 1, from, aux, to);

        //делаем ход, одно кольцо снимаем с from и кладем на to
        h[from]--;
        h[to]++;
        max[step++] = Math.max(h[0], Math.max(h[1], h[2]));

        hanoi(n - 1, aux, to, from);
    }

    //поиск корня с сокращением пути
    private static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    //объединение по размеру поддерева
    private static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (size[a] < size[b]) {
            int t = a;
            a = b;
            b = t;
        }
        parent[b] = a;
        size[a] += size[b];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        //всего шагов 2^n минус 1, стартовое состояние не считаем
        int steps = (1 << n) - 1;
        max = new int[steps];
        h = new int[3];
        h[0] = n;
        step = 0;
        hanoi(n, 0, 1, 2);

        parent = new int[steps];
        size = new int[steps];
        for (int i = 0; i < steps; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        //first[k] первый шаг с наибольшей высотой k, остальные такие шаги объединяем с ним
        int[] first = new int[n + 1];
        for (int k = 0; k <= n; k++) {
            first[k] = -1;
        }
        for (int i = 0; i < steps; i++) {
            if (first[max[i]] == -1) {
                first[max[i]] = i;
            } else {
                union(first[max[i]], i);
            }
        }

        //собираем размеры поддеревьев, их не больше n
        int[] res = new int[n];
        int count = 0;
        for (int i = 0; i < steps; i++) {
            if (parent[i] == i) {
                res[count++] = size[i];
            }
        }

        //коллекциями нельзя, поэтому сортируем вставками, элементов совсем мало
        for (int i = 1; i < count; i++) {
            int x = res[i];
            int j = i - 1;
            while (j >= 0 && res[j] > x) {
                res[j + 1] = res[j];
                j--;
            }
            res[j + 1] = x;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(res[i]).append(" ");
        }
        System.out.println(sb.toString().trim());
    }
}