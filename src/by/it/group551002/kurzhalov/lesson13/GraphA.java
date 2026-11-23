package by.it.group551002.kurzhalov.lesson13;

import java.util.Scanner;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        scanner.close();

        // Парсим рёбра вида "A -> B, C -> D, ..."
        // Собираем вершины и рёбра
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
            // пропускаем пробелы и запятые
            while (i < len && (line.charAt(i) == ' ' || line.charAt(i) == ',')) i++;
            if (i >= len) break;
            // читаем from до ' '
            int start = i;
            while (i < len && line.charAt(i) != ' ') i++;
            String from = line.substring(start, i);
            // пропускаем пробелы
            while (i < len && line.charAt(i) == ' ') i++;
            // ожидаем "->"
            if (i + 1 < len && line.charAt(i) == '-' && line.charAt(i + 1) == '>') {
                i += 2;
            } else {
                break;
            }
            // пропускаем пробелы
            while (i < len && line.charAt(i) == ' ') i++;
            start = i;
            while (i < len && line.charAt(i) != ',' && line.charAt(i) != ' ') i++;
            String to = line.substring(start, i);

            // добавляем вершины
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
            // добавляем ребро
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

        // Сортируем вершины лексикографически (для стабильного вывода)
        sortStrings(vertices, vertexCount);

        // Матрица смежности / список смежности через индексы
        int[] inDegree = new int[vertexCount];
        // adjacency: для каждой вершины массив индексов
        int[][] adj = new int[vertexCount][];
        int[] adjSize = new int[vertexCount];

        // Сначала посчитаем исходящие степени
        for (int e = 0; e < edgeCount; e++) {
            int u = indexOf(vertices, vertexCount, edgeFrom[e]);
            int v = indexOf(vertices, vertexCount, edgeTo[e]);
            if (u < 0 || v < 0) continue;
            adjSize[u]++;
            inDegree[v]++;
        }
        for (int v = 0; v < vertexCount; v++) {
            adj[v] = new int[adjSize[v]];
            adjSize[v] = 0;
        }
        for (int e = 0; e < edgeCount; e++) {
            int u = indexOf(vertices, vertexCount, edgeFrom[e]);
            int v = indexOf(vertices, vertexCount, edgeTo[e]);
            if (u < 0 || v < 0) continue;
            adj[u][adjSize[u]++] = v;
        }

        // Алгоритм Кана с лексикографическим порядком (min-heap на индексах)
        // Мини-куча реализована вручную (без java.util.PriorityQueue по условию)
        int[] heap = new int[vertexCount + 1];
        int heapSize = 0;

        for (int v = 0; v < vertexCount; v++) {
            if (inDegree[v] == 0) {
                heapPush(heap, heapSize++, v, vertices);
            }
        }

        int[] result = new int[vertexCount];
        int resultSize = 0;

        while (heapSize > 0) {
            int v = heapPop(heap, --heapSize, vertices);
            result[resultSize++] = v;
            for (int j = 0; j < adj[v].length; j++) {
                int to = adj[v][j];
                inDegree[to]--;
                if (inDegree[to] == 0) {
                    heapPush(heap, heapSize++, to, vertices);
                }
            }
        }

        // Вывод через пробел
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < resultSize; r++) {
            if (r > 0) sb.append(' ');
            sb.append(vertices[result[r]]);
        }
        System.out.println(sb.toString());
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

    // сортировка вставками (без стандартных коллекций)
    private static void sortStrings(String[] arr, int count) {
        for (int i = 1; i < count; i++) {
            String key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j].compareTo(key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // Мини-куча по лексикографическому порядку строк vertices[idx]
    private static void heapPush(int[] heap, int size, int value, String[] vertices) {
        heap[size] = value;
        int i = size;
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (vertices[heap[parent]].compareTo(vertices[heap[i]]) <= 0) break;
            int tmp = heap[parent];
            heap[parent] = heap[i];
            heap[i] = tmp;
            i = parent;
        }
    }

    private static int heapPop(int[] heap, int newSize, String[] vertices) {
        int result = heap[0];
        if (newSize == 0) return result;
        heap[0] = heap[newSize];
        int i = 0;
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;
            if (left < newSize && vertices[heap[left]].compareTo(vertices[heap[smallest]]) < 0) {
                smallest = left;
            }
            if (right < newSize && vertices[heap[right]].compareTo(vertices[heap[smallest]]) < 0) {
                smallest = right;
            }
            if (smallest == i) break;
            int tmp = heap[i];
            heap[i] = heap[smallest];
            heap[smallest] = tmp;
            i = smallest;
        }
        return result;
    }
}