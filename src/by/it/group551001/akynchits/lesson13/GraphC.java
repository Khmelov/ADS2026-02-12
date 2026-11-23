package by.it.group551001.akynchits.lesson13;

import java.util.Arrays;
import java.util.Scanner;

public class GraphC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) return;

        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;

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

        // 2. Получаем массив уникальных вершин в лексикографическом порядке
        Arrays.sort(rawVertices);
        int uniqueCount = 0;
        for (int i = 0; i < vCount; i++) {
            if (i == 0 || !rawVertices[i].equals(rawVertices[i - 1])) {
                rawVertices[uniqueCount++] = rawVertices[i];
            }
        }
        String[] vertices = Arrays.copyOf(rawVertices, uniqueCount);
        int n = vertices.length;

        // 3. Строим матрицу смежности прямого графа и транспонированного (обратного)
        boolean[][] adj = new boolean[n][n];
        boolean[][] radj = new boolean[n][n];

        for (int i = 0; i < pairs.length; i++) {
            int u = Arrays.binarySearch(vertices, fromNodes[i]);
            int v = Arrays.binarySearch(vertices, toNodes[i]);
            adj[u][v] = true;
            radj[v][u] = true;
        }

        // 4. Первый проход DFS (по прямому графу) — фиксация времени выхода
        boolean[] visited = new boolean[n];
        int[] order = new int[n];
        int[] orderIdx = new int[]{0};

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs1(i, adj, visited, order, orderIdx, n);
            }
        }

        // 5. Второй проход DFS (по транспонированному графу) в порядке убывания времени выхода.
        // Выделяет компоненты сильной связности в топологическом порядке (от истоков к стокам).
        Arrays.fill(visited, false);
        boolean[] inComp = new boolean[n];

        for (int i = n - 1; i >= 0; i--) {
            int v = order[i];
            if (!visited[v]) {
                Arrays.fill(inComp, false);
                dfs2(v, radj, visited, inComp, n);

                // Выводим вершины компонента по алфавиту без пробелов
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < n; j++) {
                    if (inComp[j]) {
                        sb.append(vertices[j]);
                    }
                }
                System.out.println(sb);
            }
        }
    }

    // Первый DFS: сохраняет вершины в массив order по мере завершения их обработки
    private static void dfs1(int u, boolean[][] adj, boolean[] visited, int[] order, int[] orderIdx, int n) {
        visited[u] = true;
        for (int v = 0; v < n; v++) {
            if (adj[u][v] && !visited[v]) {
                dfs1(v, adj, visited, order, orderIdx, n);
            }
        }
        order[orderIdx[0]++] = u;
    }

    // Второй DFS: обходит транспонированный граф и помечает все вершины текущей компоненты
    private static void dfs2(int u, boolean[][] radj, boolean[] visited, boolean[] inComp, int n) {
        visited[u] = true;
        inComp[u] = true;
        for (int v = 0; v < n; v++) {
            if (radj[u][v] && !visited[v]) {
                dfs2(v, radj, visited, inComp, n);
            }
        }
    }
}