package by.it.group551002.kurzhalov.lesson13;

import java.util.Scanner;

public class GraphC {

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
            while (i < len && line.charAt(i) != ' ' && line.charAt(i) != '-') i++;
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

        // ===== Списки смежности: прямой и обратный =====
        int[] outSize = new int[vertexCount];
        int[] inSize = new int[vertexCount];
        for (int e = 0; e < edgeCount; e++) {
            int u = indexOf(vertices, vertexCount, edgeFrom[e]);
            int v = indexOf(vertices, vertexCount, edgeTo[e]);
            if (u >= 0 && v >= 0) {
                outSize[u]++;
                inSize[v]++;
            }
        }
        int[][] outAdj = new int[vertexCount][];
        int[][] inAdj = new int[vertexCount][];
        for (int v = 0; v < vertexCount; v++) {
            outAdj[v] = new int[outSize[v]];
            inAdj[v] = new int[inSize[v]];
        }
        int[] outFill = new int[vertexCount];
        int[] inFill = new int[vertexCount];
        for (int e = 0; e < edgeCount; e++) {
            int u = indexOf(vertices, vertexCount, edgeFrom[e]);
            int v = indexOf(vertices, vertexCount, edgeTo[e]);
            if (u >= 0 && v >= 0) {
                outAdj[u][outFill[u]++] = v;
                inAdj[v][inFill[v]++] = u;
            }
        }

        // ===== Алгоритм Косараю =====
        // 1) Первый проход DFS по прямому графу, порядок выхода (finish order)
        int[] color = new int[vertexCount];
        int[] order = new int[vertexCount];
        int[] orderSize = new int[1];
        // итеративный DFS, чтобы не тянуть рекурсию
        int[] stackNode = new int[vertexCount];
        int[] stackPos = new int[vertexCount];

        for (int v = 0; v < vertexCount; v++) {
            if (color[v] != 0) continue;
            int top = 0;
            color[v] = 1;
            stackNode[top] = v;
            stackPos[top] = 0;
            top++;
            while (top > 0) {
                int cur = stackNode[top - 1];
                int pos = stackPos[top - 1];
                if (pos < outAdj[cur].length) {
                    int to = outAdj[cur][pos];
                    stackPos[top - 1] = pos + 1;
                    if (color[to] == 0) {
                        color[to] = 1;
                        stackNode[top] = to;
                        stackPos[top] = 0;
                        top++;
                    }
                } else {
                    color[cur] = 2;
                    order[orderSize[0]++] = cur;
                    top--;
                }
            }
        }

        // 2) Второй проход по обратному графу в порядке, обратном order
        int[] comp = new int[vertexCount]; // -1 = не назначена
        for (int v = 0; v < vertexCount; v++) comp[v] = -1;
        int compCount = 0;
        // массив компонент: для каждой компоненты — список вершин
        int[][] components = new int[vertexCount][];
        int[] compSizes = new int[vertexCount];

        // временный буфер для вершин одной компоненты
        int[] buf = new int[vertexCount];

        for (int k = orderSize[0] - 1; k >= 0; k--) {
            int start = order[k];
            if (comp[start] != -1) continue;
            // DFS по обратному графу
            int bufSize = 0;
            int top = 0;
            stackNode[top] = start;
            stackPos[top] = 0;
            top++;
            comp[start] = compCount;
            while (top > 0) {
                int cur = stackNode[top - 1];
                int pos = stackPos[top - 1];
                if (pos < inAdj[cur].length) {
                    int to = inAdj[cur][pos];
                    stackPos[top - 1] = pos + 1;
                    if (comp[to] == -1) {
                        comp[to] = compCount;
                        stackNode[top] = to;
                        stackPos[top] = 0;
                        top++;
                    }
                } else {
                    buf[bufSize++] = cur;
                    top--;
                }
            }
            int[] component = new int[bufSize];
            System.arraycopy(buf, 0, component, 0, bufSize);
            components[compCount] = component;
            compSizes[compCount] = bufSize;
            compCount++;
        }

        // ===== Топологический порядок компонент =====
        // Ребро u -> v в исходном графе даёт ребро comp[u] -> comp[v] в конденсате.
        // Нужно вывести компоненты в топологическом порядке конденсата.
        // Строим граф конденсата (рёбра между разными компонентами).
        int[] condOutSize = new int[compCount];
        // посчитаем уникальные рёбра конденсата
        // для простоты используем матрицу достижимости размера compCount x compCount
        // (compCount <= vertexCount, так что O(n^2) памяти — приемлемо)
        boolean[][] condEdge = new boolean[compCount][compCount];
        for (int e = 0; e < edgeCount; e++) {
            int u = indexOf(vertices, vertexCount, edgeFrom[e]);
            int v = indexOf(vertices, vertexCount, edgeTo[e]);
            if (u < 0 || v < 0) continue;
            int cu = comp[u];
            int cv = comp[v];
            if (cu != cv && !condEdge[cu][cv]) {
                condEdge[cu][cv] = true;
                condOutSize[cu]++;
            }
        }

        // Алгоритм Кана на конденсате с лексикографическим порядком компонент.
        // Ключ компоненты для сравнения — лексикографически минимальная вершина?
        // По условию "первый - исток, последний - сток", а при равнозначности —
        // лексикографический порядок вершин В КОМПОНЕНТЕ. Для порядка компонент
        // между собой используем лексикографически минимальную вершину компоненты
        // как ключ (это даёт стабильный результат, совпадающий с примером).
        int[] inDeg = new int[compCount];
        for (int a = 0; a < compCount; a++) {
            for (int b = 0; b < compCount; b++) {
                if (condEdge[a][b]) inDeg[b]++;
            }
        }

        // Ключ компоненты = минимальная вершина (по строке)
        String[] compKey = new String[compCount];
        for (int c = 0; c < compCount; c++) {
            String min = null;
            for (int j = 0; j < compSizes[c]; j++) {
                String s = vertices[components[c][j]];
                if (min == null || s.compareTo(min) < 0) min = s;
            }
            compKey[c] = min;
        }

        // Мини-куча по compKey
        int[] heap = new int[compCount + 1];
        int heapSize = 0;
        for (int c = 0; c < compCount; c++) {
            if (inDeg[c] == 0) {
                heapPush(heap, heapSize++, c, compKey);
            }
        }

        int[] topoOrder = new int[compCount];
        int topoSize = 0;
        while (heapSize > 0) {
            int c = heapPop(heap, --heapSize, compKey);
            topoOrder[topoSize++] = c;
            for (int b = 0; b < compCount; b++) {
                if (condEdge[c][b]) {
                    inDeg[b]--;
                    if (inDeg[b] == 0) {
                        heapPush(heap, heapSize++, b, compKey);
                    }
                }
            }
        }

        // ===== Вывод =====
        StringBuilder sb = new StringBuilder();
        for (int t = 0; t < topoSize; t++) {
            int c = topoOrder[t];
            // сортируем вершины компоненты лексикографически
            sortComponent(components[c], compSizes[c], vertices);
            for (int j = 0; j < compSizes[c]; j++) {
                sb.append(vertices[components[c][j]]);
            }
            sb.append('\n');
        }
        System.out.print(sb.toString());
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

    // сортировка вершин компоненты по строке (индексы -> строки vertices)
    private static void sortComponent(int[] comp, int count, String[] vertices) {
        for (int i = 1; i < count; i++) {
            int key = comp[i];
            String keyStr = vertices[key];
            int j = i - 1;
            while (j >= 0 && vertices[comp[j]].compareTo(keyStr) > 0) {
                comp[j + 1] = comp[j];
                j--;
            }
            comp[j + 1] = key;
        }
    }

    // Мини-куча по строкам compKey[idx]
    private static void heapPush(int[] heap, int size, int value, String[] keys) {
        heap[size] = value;
        int i = size;
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (keys[heap[parent]].compareTo(keys[heap[i]]) <= 0) break;
            int tmp = heap[parent];
            heap[parent] = heap[i];
            heap[i] = tmp;
            i = parent;
        }
    }

    private static int heapPop(int[] heap, int newSize, String[] keys) {
        int result = heap[0];
        if (newSize == 0) return result;
        heap[0] = heap[newSize];
        int i = 0;
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;
            if (left < newSize && keys[heap[left]].compareTo(keys[heap[smallest]]) < 0) smallest = left;
            if (right < newSize && keys[heap[right]].compareTo(keys[heap[smallest]]) < 0) smallest = right;
            if (smallest == i) break;
            int tmp = heap[i];
            heap[i] = heap[smallest];
            heap[smallest] = tmp;
            i = smallest;
        }
        return result;
    }
}