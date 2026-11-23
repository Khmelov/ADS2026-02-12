
package by.it.group551001.maxim_belyak.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class StatesHanoiTowerC {

    static int[] parent;
    static int[] size;

    // Количество колец на стержнях A, B, C
    static int[] heights = new int[3];

    // Первый шаг с определённой максимальной высотой
    static int[] firstByHeight;

    static int stepCount = 0;

    // Инициализация DSU
    static void initDSU(int n) {
        parent = new int[n];
        size = new int[n];

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    // Поиск корня со сжатием пути
    static int find(int v) {
        if (parent[v] != v) {
            parent[v] = find(parent[v]);
        }

        return parent[v];
    }

    // Объединение по размеру
    static void union(int a, int b) {
        a = find(a);
        b = find(b);

        if (a == b) {
            return;
        }

        if (size[a] < size[b]) {
            int temp = a;
            a = b;
            b = temp;
        }

        parent[b] = a;
        size[a] += size[b];
    }

    // Перемещение одного кольца
    static void move(int from, int to) {
        heights[from]--;
        heights[to]++;

        int maxHeight = Math.max(
                heights[0],
                Math.max(heights[1], heights[2])
        );

        int currentStep = stepCount;
        stepCount++;

        if (firstByHeight[maxHeight] == -1) {
            firstByHeight[maxHeight] = currentStep;
        } else {
            union(currentStep, firstByHeight[maxHeight]);
        }
    }

    // Рекурсивное решение Ханойских башен
    static void hanoi(int n, int from, int to, int auxiliary) {
        if (n == 0) {
            return;
        }

        hanoi(n - 1, from, auxiliary, to);

        move(from, to);

        hanoi(n - 1, auxiliary, to, from);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();

        // Важно: сбрасываем состояние перед каждым запуском
        stepCount = 0;

        heights[0] = n;
        heights[1] = 0;
        heights[2] = 0;

        int totalMoves = (1 << n) - 1;

        initDSU(totalMoves);

        firstByHeight = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            firstByHeight[i] = -1;
        }

        // Переносим все кольца с A на B
        hanoi(n, 0, 1, 2);

        // Получаем размеры множеств
        int[] result = new int[totalMoves];
        int count = 0;

        for (int i = 0; i < totalMoves; i++) {
            if (parent[i] == i) {
                result[count] = size[i];
                count++;
            }
        }

        // Сортировка по возрастанию
        Arrays.sort(result, 0, count);

        for (int i = 0; i < count; i++) {
            if (i > 0) {
                System.out.print(" ");
            }

            System.out.print(result[i]);
        }
    }
}
