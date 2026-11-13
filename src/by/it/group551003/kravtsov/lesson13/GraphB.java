package by.it.group551003.kravtsov.lesson13;

import java.util.*;

public class GraphB {
    // Обходим все вершины графа из каждой ещё не посещённой запускаем DFS-поиск цикла.
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        // Парсинг входной строки (множ. вершин, спск. смежности)
        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0];
            String to = parts[1];

            vertices.add(from);
            vertices.add(to);
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        }

        // Проверка на циклы с помощью DFS, visited — вершина уже посещена,
        // recursionStack — вершина сейчас в текущем пути обхода.
        // Обходим все вершины: граф может быть несвязным.
        boolean hasCycle = false;
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (String vertex : vertices) {
            if (!visited.contains(vertex)) {
                if (hasCycleDFS(vertex, graph, visited, recursionStack)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    // Кладём вершину в путь и идём по соседям.
    // Если сосед уже в recursionStack — вернулись в текущий путь => цикл.
    // После обхода убираем вершину из пути (backtrack).
    private static boolean hasCycleDFS(String vertex, Map<String, List<String>> graph,
                                       Set<String> visited, Set<String> recursionStack) {
        visited.add(vertex);
        recursionStack.add(vertex);

        if (graph.containsKey(vertex)) {
            for (String neighbor : graph.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    if (hasCycleDFS(neighbor, graph, visited, recursionStack)) {
                        return true;
                    }
                } else if (recursionStack.contains(neighbor)) {
                    return true;
                }
            }
        }

        recursionStack.remove(vertex);
        return false;
    }
}
