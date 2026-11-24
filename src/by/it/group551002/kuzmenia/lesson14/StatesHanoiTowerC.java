package by.it.group551002.kuzmenia.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    // Состояние DSU и счетчики лежат в полях объекта
    private final int[] parent;
    private final int[] size;
    // Номер первого шага с наибольшей высотой h
    private final int[] firstStep;
    // Высоты стержней: 0 = A, 1 = B, 2 = C
    private final int[] heights = new int[3];
    private int step;

    private StatesHanoiTowerC(int n) {
        // Всего ходов 2^n - 1, каждый ход это элемент DSU
        int total = (1 << n) - 1;
        parent = new int[total];
        size = new int[total];
        for (int i = 0; i < total; i++) {
            parent[i] = i;
            size[i] = 1;
        }
        firstStep = new int[n + 1];
        for (int h = 0; h <= n; h++) {
            firstStep[h] = -1;
        }
        // Стартовое состояние: все кольца на A, оно не считается
        heights[0] = n;
        heights[1] = 0;
        heights[2] = 0;
        step = 0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        // Новый объект на каждый запуск main
        StatesHanoiTowerC solver = new StatesHanoiTowerC(n);
        solver.hanoi(n, 0, 1, 2);
        System.out.println(solver.result(n));
    }

    // Метод для переноса n колец со стержня from на to через via
    private void hanoi(int n, int from, int to, int via) {
        // База рекурсии: переносить нечего
        if (n == 0) {
            return;
        }
        // Сначала убираем верхние n-1 колец на вспомогательный стержень
        hanoi(n - 1, from, via, to);

        // Сам ход: кольца хранить не нужно, достаточно высот
        heights[from]--;
        heights[to]++;
        // Наибольшая высота после этого хода
        int max = Math.max(heights[0], Math.max(heights[1], heights[2]));
        if (firstStep[max] == -1) {
            firstStep[max] = step;
        } else {
            // Шаги с одинаковой высотой попадают в одну группу
            union(firstStep[max], step);
        }
        step++;

        // Затем те же n-1 колец переносим на целевой стержень
        hanoi(n - 1, via, to, from);
    }

    // Метод для сбора размеров групп
    private String result(int n) {
        // Групп не больше n, поэтому хватает обычного массива
        int[] sizes = new int[n + 1];
        int count = 0;
        // Размер берем только у корня
        for (int i = 0; i < parent.length; i++) {
            if (find(i) == i) {
                sizes[count++] = size[i];
            }
        }

        // Сортировка вставками по возрастанию, коллекциями пользоваться нельзя
        for (int i = 1; i < count; i++) {
            int key = sizes[i];
            int j = i - 1;
            while (j >= 0 && sizes[j] > key) {
                sizes[j + 1] = sizes[j];
                j--;
            }
            sizes[j + 1] = key;
        }

        // Постройка строки результата
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(sizes[i]);
        }
        return sb.toString();
    }

    // Метод для поиска корня группы со сжатием пути
    private int find(int x) {
        int root = x;
        // Сначала поднимаемся до корня
        while (parent[root] != root) {
            root = parent[root];
        }
        // Потом весь путь перевешиваем прямо на корень
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    // Метод для объединения групп по размеру поддерева
    private void union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        // Уже в одной группе
        if (ra == rb) {
            return;
        }
        // Меньшую группу подвешиваем к большей
        if (size[ra] < size[rb]) {
            int tmp = ra;
            ra = rb;
            rb = tmp;
        }
        parent[rb] = ra;
        size[ra] += size[rb];
    }
}
