package by.it.group551001.akynchits.lesson13;


import java.util.Arrays;
import java.util.Scanner;

public class GraphB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) {
            System.out.println("no");
            return;
        }

        String input = sc.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("no");
            return;
        }

        // 1. Извлекаем пары ребер
        String[] pairs = input.split(",\\s*");
        String[] fromNodes = new String[pairs.length];
        String[] toNodes = new String[pairs.length];
        String[] rawVertices = new String[pairs.length * 2];
        int vCount = 0;

        for (int i = 0; i < pairs.length; i++) {
            String[] parts = pairs[i].split("\\s*->\\s*");
            fromNodes[i] = parts[0].trim();
            toNodes[i] = parts[1].trim();
            rawVertices[vCount++] = fromNodes[i];
            rawVertices[vCount++] = toNodes[i];
        }

        // 2. Получаем массив уникальных вершин в один проход
        Arrays.sort(rawVertices);
        int uniqueCount = 0;
        for (int i = 0; i < vCount; i++) {
            if (i == 0 || !rawVertices[i].equals(rawVertices[i - 1])) {
                rawVertices[uniqueCount++] = rawVertices[i];
            }
        }
        String[] vertices = Arrays.copyOf(rawVertices, uniqueCount);

        // 3. Заполняем матрицу смежности на массивах
        int n = vertices.length;
        boolean[][] adj = new boolean[n][n];
        for (int i = 0; i < pairs.length; i++) {
            int u = Arrays.binarySearch(vertices, fromNodes[i]);
            int v = Arrays.binarySearch(vertices, toNodes[i]);
            adj[u][v] = true;
        }

        // 4. Проверка на циклы обходом в глубину (DFS) с раскраской:
        // 0 — не посещена (белая)
        // 1 — в текущем пути рекурсии (серая)
        // 2 — полностью пройдена (черная)
        int[] color = new int[n];
        for (int i = 0; i < n; i++) {
            if (color[i] == 0) {
                if (hasCycle(i, adj, color, n)) {
                    System.out.println("yes");
                    return;
                }
            }
        }

        System.out.println("no");
    }

    private static boolean hasCycle(int u, boolean[][] adj, int[] color, int n) {
        color[u] = 1; // Вошли в вершину (пометили серым)

        for (int v = 0; v < n; v++) {
            if (adj[u][v]) {
                // Если сосед уже в текущем стеке вызовов — цикл найден
                if (color[v] == 1) {
                    return true;
                }
                // Если сосед еще не посещен, идем в него глубже
                if (color[v] == 0 && hasCycle(v, adj, color, n)) {
                    return true;
                }
            }
        }

        color[u] = 2; // Вышли из вершины (пометили черным)
        return false;
    }
}
