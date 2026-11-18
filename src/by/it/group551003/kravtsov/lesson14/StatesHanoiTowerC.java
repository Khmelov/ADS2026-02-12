package by.it.group551003.kravtsov.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    private static int[] heights;
    private static int[] parent;
    private static int[] size;
    private static int[] firstStep;
    private static int base;
    private static int step = -1;

    // поиск корня + сжатие пути
    private static int find(int i) {
        if (parent[i] == i) return i;
        return parent[i] = find(parent[i]);
    }

    // объединение по размеру поддерева
    private static void union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);
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

    // Рекурсивное решение Ханойских башен: переносим n дисков from -> to
    private static void solve(int n, int from, int to, int aux) {
        if (n == 0) return;

        solve(n - 1, from, aux, to);

        // Сам ход: снимаем верхний диск с from, кладём на to
        heights[from]--;
        heights[to]++;
        step++;

        // Ключ группы — тройка (A, B, C), кодируем в одно число
        int key = (heights[0] * base + heights[1]) * base + heights[2];

        // Если такая тройка уже встречалась — объединяем шаги в один кластер
        if (firstStep[key] == -1) {
            firstStep[key] = step;
        } else {
            union(firstStep[key], step);
        }

        solve(n - 1, aux, to, from);
    }

    // Быстрая сортировка по возрастанию
    private static void quickSort(int[] a, int l, int r) {
        if (l >= r) return;
        int pivot = a[l + (r - l) / 2];
        int i = l, j = r;
        while (i <= j) {
            while (a[i] < pivot) i++;
            while (a[j] > pivot) j--;
            if (i <= j) {
                int tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
                i++;
                j--;
            }
        }
        if (l < j) quickSort(a, l, j);
        if (i < r) quickSort(a, i, r);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();

        int total = (1 << n) - 1;      // всего шагов = 2^N - 1

        parent = new int[total];
        size = new int[total];
        for (int i = 0; i < total; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        // base = N+1, чтобы закодировать тройку (a,b,c) в одно число
        base = n + 1;
        firstStep = new int[base * base * base];
        for (int i = 0; i < firstStep.length; i++) {
            firstStep[i] = -1;
        }

        heights = new int[]{n, 0, 0};
        step = -1;

        solve(n, 0, 1, 2);

        // Собираем размеры кластеров: для каждой тройки берём размер её DSU-множества
        int[] result = new int[total];
        int k = 0;
        boolean[] seenRoot = new boolean[total];
        for (int key = 0; key < firstStep.length; key++) {
            if (firstStep[key] != -1) {
                int root = find(firstStep[key]);
                if (!seenRoot[root]) {
                    seenRoot[root] = true;
                    result[k++] = size[root];
                }
            }
        }

        quickSort(result, 0, k - 1);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < k; i++) {
            if (i > 0) sb.append(" ");
            sb.append(result[i]);
        }
        System.out.println(sb.toString());
    }
}