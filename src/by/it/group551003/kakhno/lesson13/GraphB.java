package by.it.group551003.kakhno.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allVertices = new TreeSet<>();

        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            allVertices.add(from);
            allVertices.add(to);
        }

        boolean hasCycle = hasCycle(graph, allVertices);

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycle(Map<String, List<String>> graph, Set<String> vertices) {
        // 0 - не посещена
        // 1 - в процессе обработки (в стеке)
        // 2 - полностью обработана

        Map<String, Integer> state = new HashMap<>();
        for (String v : vertices)
            state.put(v, 0);

        for (String v : vertices)
            if (state.get(v) == 0)
                if (dfs(v, graph, state))
                    return true;

        return false;
    }

    private static boolean dfs(String vertex, Map<String, List<String>> graph, Map<String, Integer> state) {
        state.put(vertex, 1);

        if (graph.containsKey(vertex))
            for (String neighbor : graph.get(vertex)) {
                if (state.get(neighbor) == 1)
                    return true;
                if (state.get(neighbor) == 0)
                    if (dfs(neighbor, graph, state))
                        return true;
            }

        state.put(vertex, 2);
        return false;
    }
}