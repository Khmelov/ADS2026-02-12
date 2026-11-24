package by.it.group551002.yermakou.lesson13;

import java.util.*;

public class GraphB {

    // Состояния вершин для алгоритма поиска в глубину (DFS)
    private static final int UNVISITED = 0; // Белая: вершина еще не посещена
    private static final int VISITING = 1;  // Серая: вершина находится в текущем стеке рекурсии
    private static final int VISITED = 2;   // Черная: вершина и все ее потомки полностью обработаны

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            System.out.println("no");
            return;
        }

        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("no");
            return;
        }

        // Список смежности для хранения орграфа
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        // Разбиваем входную строку по запятым на отдельные ребра
        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                allVertices.add(from);
                allVertices.add(to);
            }
        }

        if (hasCycle(graph, allVertices)) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }

    private static boolean hasCycle(Map<String, List<String>> graph, Set<String> allVertices) {
        Map<String, Integer> state = new HashMap<>();
        for (String vertex : allVertices) {
            state.put(vertex, UNVISITED);
        }

        // Запускаем DFS из каждой непосещенной вершины (на случай несвязного графа)
        for (String vertex : allVertices) {
            if (state.get(vertex) == UNVISITED) {
                if (dfs(vertex, graph, state)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfs(String current, Map<String, List<String>> graph, Map<String, Integer> state) {
        state.put(current, VISITING);

        List<String> neighbors = graph.get(current);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                int neighborState = state.get(neighbor);
                // Если встретили серую вершину, значит, нашли обратное ребро (цикл)
                if (neighborState == VISITING) {
                    return true;
                }
                if (neighborState == UNVISITED) {
                    if (dfs(neighbor, graph, state)) {
                        return true;
                    }
                }
            }
        }

        state.put(current, VISITED);
        return false;
    }
}
