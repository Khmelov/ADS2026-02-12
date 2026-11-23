package by.it.group551002.kurzhalov.lesson13;

import java.util.Scanner;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        scanner.close();

        // ===== Парсинг =====
        int capacity = 16;
        String[] vertices = new String[capacity];
        int vertexCount = 0;

        int edgeCap = 16;
        String[] edgeFrom = new String[edgeCap];
        String[] edgeTo = new String[edgeCap];
        int edgeCount = 0;

        int i = 0;
        int len = line.length();
        while (i < len) {
            while (i < len && (line.charAt(i) == ' ' || line.charAt(i) == ',')) i++;
            if (i >= len) break;
            int start = i;
            while (i < len && line.charAt(i) != ' ') i++;
            String from = line.substring(start, i);
            while (i < len && line.charAt(i) == ' ') i++;
            if (i + 1 < len && line.charAt(i) == '-' && line.charAt(i + 1) == '>') {
                i += 2;
            } else {
                break;
            }
            while (i < len && line.charAt(i) == ' ') i++;
            start = i;
            while (i < len && line.charAt(i) != ',' && line.charAt(i) != ' ') i++;
            String to = line.substring(start, i);

            if (!contains(vertices, vertexCount, from)) {
                if (vertexCount == vertices.length) {
                    String[] bigger = new String[vertices.length * 2];
                    System.arraycopy(vertices, 0, bigger, 0, vertices.length);
                    vertices = bigger;
                }
                vertices[vertexCount++] = from;
            }
            if (!contains(vertices, vertexCount, to)) {
                if (vertexCount == vertices.length) {
                    String[] bigger = new String[vertices.length * 2];
                    System.arraycopy(vertices, 0, bigger, 0, vertices.length);
                    vertices = bigger;
                }
                vertices[vertexCount++] = to;
            }
            if (edgeCount == edgeFrom.length) {
                String[] f2 = new String[edgeFrom.length * 2];
                String[] t2 = new String[edgeTo.length * 2];
                System.arraycopy(edgeFrom, 0, f2, 0, edgeFrom.length);
                System.arraycopy(edgeTo, 0, t2, 0, edgeTo.length);
                edgeFrom = f2;
                edgeTo = t2;
            }
            edgeFrom[edgeCount] = from;
            edgeTo[edgeCount] = to;
            edgeCount++;
        }

        // ===== Список смежности через индексы =====
        int[] adjSize = new int[vertexCount];
        for (int e = 0; e < edgeCount; e++) {
            int u = indexOf(vertices, vertexCount, edgeFrom[e]);
            if (u >= 0) adjSize[u]++;
        }
        int[][] adj = new int[vertexCount][];
        for (int v = 0; v < vertexCount; v++) {
            adj[v] = new int[adjSize[v]];
        }
        int[] fill = new int[vertexCount];
        for (int e = 0; e < edgeCount; e++) {
            int u = indexOf(vertices, vertexCount, edgeFrom[e]);
            int v = indexOf(vertices, vertexCount, edgeTo[e]);
            if (u >= 0 && v >= 0) adj[u][fill[u]++] = v;
        }

        // ===== Поиск цикла (DFS с тремя цветами: 0 — белый, 1 — серый, 2 — чёрный) =====
        int[] color = new int[vertexCount]; // все 0
        boolean hasCycle = false;
        for (int v = 0; v < vertexCount && !hasCycle; v++) {
            if (color[v] == 0) {
                if (dfsHasCycle(v, adj, color)) {
                    hasCycle = true;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    // ================= DFS =================

    private static boolean dfsHasCycle(int start, int[][] adj, int[] color) {
        // Итеративный DFS со стеком (без рекурсии и без стандартных коллекций)
        int n = color.length;
        int[] stackNode = new int[n];
        int[] stackPos = new int[n];
        int top = 0;

        color[start] = 1; // серый
        stackNode[top] = start;
        stackPos[top] = 0;
        top++;

        while (top > 0) {
            int v = stackNode[top - 1];
            int pos = stackPos[top - 1];
            if (pos < adj[v].length) {
                int to = adj[v][pos];
                stackPos[top - 1] = pos + 1;
                if (color[to] == 0) {
                    color[to] = 1;
                    stackNode[top] = to;
                    stackPos[top] = 0;
                    top++;
                } else if (color[to] == 1) {
                    return true; // нашли обратное ребро — цикл
                }
                // если color[to] == 2 — уже обработана, пропускаем
            } else {
                color[v] = 2; // чёрный
                top--;
            }
        }
        return false;
    }

    // ================= вспомогательные =================

    private static boolean contains(String[] arr, int count, String s) {
        for (int i = 0; i < count; i++) {
            if (arr[i].equals(s)) return true;
        }
        return false;
    }

    private static int indexOf(String[] arr, int count, String s) {
        for (int i = 0; i < count; i++) {
            if (arr[i].equals(s)) return i;
        }
        return -1;
    }
}