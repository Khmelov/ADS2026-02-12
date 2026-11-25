package by.it.group551003.kakhno.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverseGraph = new HashMap<>();
        Set<String> allVertices = new TreeSet<>();

        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
            allVertices.add(from);
            allVertices.add(to);
        }

        // Алгоритм Косарайю для поиска компонент сильной связности
        List<List<String>> sccs = kosaraju(graph, reverseGraph, allVertices);

        for (List<String> scc : sccs) {
            StringBuilder sb = new StringBuilder();
            for (String v : scc)
                sb.append(v);
            System.out.println(sb.toString());
        }
    }

    private static List<List<String>> kosaraju(Map<String, List<String>> graph,
                                               Map<String, List<String>> reverseGraph,
                                               Set<String> vertices) {
        // Шаг 1: Получаем порядок завершения вершин в первом DFS на исходном графе
        Stack<String> orderStack = new Stack<>();
        Set<String> visited = new HashSet<>();

        for (String v : vertices)
            if (!visited.contains(v))
                dfs1(v, graph, visited, orderStack);

        // Шаг 2: Обрабатываем вершины в обратном порядке на транспонированном графе
        visited.clear();
        List<List<String>> components = new ArrayList<>();

        while (!orderStack.isEmpty()) {
            String v = orderStack.pop();
            if (!visited.contains(v)) {
                List<String> component = new ArrayList<>();
                dfs2(v, reverseGraph, visited, component);
                // Сортируем вершины в компоненте лексикографически
                Collections.sort(component);
                components.add(component);
            }
        }

        return components;
    }

    private static void dfs1(String vertex, Map<String, List<String>> graph,
                             Set<String> visited, Stack<String> orderStack) {
        visited.add(vertex);

        if (graph.containsKey(vertex))
            for (String neighbor : graph.get(vertex))
                if (!visited.contains(neighbor))
                    dfs1(neighbor, graph, visited, orderStack);

        orderStack.push(vertex);
    }

    private static void dfs2(String vertex, Map<String, List<String>> reverseGraph,
                             Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);

        if (reverseGraph.containsKey(vertex))
            for (String neighbor : reverseGraph.get(vertex))
                if (!visited.contains(neighbor))
                    dfs2(neighbor, reverseGraph, visited, component);
    }
}