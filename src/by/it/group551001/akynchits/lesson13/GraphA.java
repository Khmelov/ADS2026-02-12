package by.it.group551001.akynchits.lesson13;

import java.util.Arrays;
import java.util.Scanner;

public class GraphA {
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

        // 2. Сортируем и оставляем только уникальные вершины
        Arrays.sort(rawVertices);
        int uniqueCount = 0;
        for (int i = 0; i < vCount; i++) {
            if (i == 0 || !rawVertices[i].equals(rawVertices[i - 1])) {
                uniqueCount++;
            }
        }

        String[] vertices = new String[uniqueCount];
        int idx = 0;
        for (int i = 0; i < vCount; i++) {
            if (i == 0 || !rawVertices[i].equals(rawVertices[i - 1])) {
                vertices[idx++] = rawVertices[i];
            }
        }

        // 3. Заполняем матрицу смежности и степени захода
        int n = vertices.length;
        boolean[][] adj = new boolean[n][n];
        int[] inDegree = new int[n];

        for (int i = 0; i < pairs.length; i++) {
            int u = Arrays.binarySearch(vertices, fromNodes[i]);
            int v = Arrays.binarySearch(vertices, toNodes[i]);
            if (!adj[u][v]) {
                adj[u][v] = true;
                inDegree[v]++;
            }
        }

        // 4. Топологическая сортировка обходом массива
        boolean[] visited = new boolean[n];
        StringBuilder result = new StringBuilder();

        for (int step = 0; step < n; step++) {
            int current = -1;

            // Ищем первую непосещенную вершину с inDegree == 0.
            // Так как массив vertices отсортирован, первый найденный индекс
            // автоматически является наименьшим по алфавиту.
            for (int i = 0; i < n; i++) {
                if (!visited[i] && inDegree[i] == 0) {
                    current = i;
                    break;
                }
            }

            if (current == -1) break; // Найден цикл

            visited[current] = true;
            result.append(vertices[current]).append(" ");

            for (int next = 0; next < n; next++) {
                if (adj[current][next]) {
                    inDegree[next]--;
                }
            }
        }

        System.out.println(result.toString().trim());
    }
}